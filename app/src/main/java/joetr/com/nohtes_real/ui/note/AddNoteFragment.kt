package joetr.com.nohtes_real.ui.note

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.minusAssign
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import joetr.com.data.entities.LabelEntity
import joetr.com.data.entities.NoteEntity
import joetr.com.nohtes_real.R
import joetr.com.nohtes_real.android.base.BaseFragment
import joetr.com.nohtes_real.android.extensions.exhaustive
import joetr.com.nohtes_real.android.extensions.hideKeyboard
import joetr.com.nohtes_real.di.component.FragmentComponent
import joetr.com.nohtes_real.ui.note.label.LabelBottomSheet
import joetr.com.nohtes_real.ui.note.label.LabelInteraction
import kotlinx.android.synthetic.main.add_note_fragment.*
import kotlinx.android.synthetic.main.link_input_dialog.view.*
import timber.log.Timber
import xute.markdeditor.EditorControlBar
import xute.markdeditor.Styles.TextComponentStyle.NORMAL
import xute.markdeditor.models.DraftModel
import xute.markdeditor.utilities.FilePathUtils
import javax.inject.Inject


const val NOTE_ARG = "note"
private const val REQUEST_IMAGE_PERMISSION = 24

class AddNoteFragment : BaseFragment(), LabelInteraction, EditorControlBar.EditorControlListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AddNoteViewModel

    private var noteEntity: NoteEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // avoid white flash of fragment transition in dark mode
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            requireActivity().window.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        } else {
            requireActivity().window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        }

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[AddNoteViewModel::class.java]

        viewModel.getLabels()
    }

    private fun render(addNoteState: AddNoteState) {
        when (addNoteState) {
            AddNoteState.Loading -> {
                addNoteBaseLayout.displayedChild = R.id.addNoteLoadingContainer
            }
            AddNoteState.AddingNote -> {
                addNoteBaseLayout.displayedChild = R.id.addNoteContentContainer
            }
        }.exhaustive
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    private fun handle(addNoteAction: AddNoteAction) {
        when (addNoteAction) {
            AddNoteAction.Error -> {
                displaySnackBar(R.string.add_note_error)
            }
            AddNoteAction.NoteAddedSuccessfully -> {
                displaySnackBar(R.string.add_note_success)
                NavHostFragment.findNavController(this).popBackStack()
            }
        }.exhaustive
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun displaySnackBar(@StringRes message: Int) {
        Snackbar.make(addNoteBaseLayout, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return layoutInflater.inflate(R.layout.add_note_fragment, container, false)
    }

    override fun injectSelf(component: FragmentComponent) = component.inject(this)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.addNoteLabel -> {
                LabelBottomSheet().show(childFragmentManager, LabelBottomSheet.TAG)
            }
            R.id.addNoteSave -> {
                hideKeyboard()
                viewModel.saveNote(editor.draft, editor.markdownContent, noteEntity)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compositeDisposable += viewModel.action()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handle) { Timber.e(it) }

        compositeDisposable += viewModel.state()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::render) { Timber.e(it) }

        setupEditor()
    }

    private fun displayTagsIfExist(noteEntity: NoteEntity) {
        viewModel.addTags(noteEntity)
        displayChips()
    }

    private fun setupEditor() {
        editor.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.borderColor))
        controlBar.setEditorControlListener(this)
        controlBar.setEditor(editor)

        if(arguments?.containsKey(NOTE_ARG) == true) { noteEntity = arguments?.getParcelable(NOTE_ARG)!!
            editor.loadDraft(DraftModel(items = noteEntity!!.draftContent))
            displayTagsIfExist(noteEntity!!)
        } else {
            editor.configureEditor(
                false,
                "Start Here...",
                NORMAL
            )
        }
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when(childFragment) {
            is LabelBottomSheet -> {
                childFragment.labelInteraction = this
                childFragment.labels = viewModel.getLabelsForBottomSheet()
            }
        }
    }

    override fun labelClicked(label: LabelEntity) {
        viewModel.toggleLabel(label)

        addNoteChipGroup.removeAllViews()

        displayChips()
    }

    override fun labelAdded(label: LabelEntity) {
        viewModel.addLabel(label)
    }

    override fun labelDeleted(label: LabelEntity) {
        viewModel.deleteLabel(label)

        val chip = addNoteChipGroup.children.firstOrNull {
            (it is Chip && it.text == label.label)
        }

        if(chip != null) {
            addNoteChipGroup -= chip
        }
    }

    private fun displayChips() {
        viewModel.userEnteredLabels.filter { it.checked }.forEach {
            val chip =
                layoutInflater.inflate(R.layout.add_note_chip, addNoteChipGroup, false) as Chip

            chip.text = it.label
            chip.setOnClickListener {
                LabelBottomSheet().show(childFragmentManager, LabelBottomSheet.TAG)
            }
            addNoteChipGroup.addView(chip)
        }
    }

    override fun onInsertImageClicked() {
        openImageGallery()
    }

    private fun openImageGallery() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_IMAGE_PERMISSION
            )
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, REQUEST_IMAGE_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_IMAGE_PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageGallery()
            } else {
                //todo joe ask for explanation via snack bar
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_PERMISSION) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                val uri = data.data;
                val filePath = FilePathUtils.getPath(requireContext(), uri);
                addImage(filePath);
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun addImage(filePath: String) {
        editor.insertImage(filePath)
    }


    override fun onInserLinkClicked() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        val inflater: LayoutInflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.link_input_dialog, null)
        builder.setView(dialogView)

        val dialog: AlertDialog = builder.create()

        // todo joe make display as link
        dialogView.linkAddButton.setOnClickListener {
            val linkName = dialogView.linkName.text.toString()
            val linkUrl = dialogView.linkUrl.text.toString()
            if(linkName.isNotEmpty() && linkUrl.isNotEmpty()) {
                editor.addLink(linkName, linkUrl)
                dialog.dismiss()
            } else {
                displaySnackBar(R.string.add_note_empty_link_error)
            }
        }

        dialog.show()
    }
}
