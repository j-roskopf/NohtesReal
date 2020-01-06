package joetr.com.nohtes_real.ui.note

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.github.irshulx.Editor
import com.github.irshulx.models.EditorTextStyle
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
import joetr.com.nohtes_real.di.component.FragmentComponent
import joetr.com.nohtes_real.ui.note.label.LabelBottomSheet
import joetr.com.nohtes_real.ui.note.label.LabelInteraction
import kotlinx.android.synthetic.main.add_note_fragment.*
import timber.log.Timber
import javax.inject.Inject


class AddNoteFragment : BaseFragment(), LabelInteraction {

    // todo joe enable view binding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AddNoteViewModel

    private val labelBottomSheet = LabelBottomSheet()

    val chipMap : Map<LabelEntity, Boolean> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[AddNoteViewModel::class.java]

        viewModel.getLabels()

        compositeDisposable += viewModel.action()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handle) { Timber.e(it) }

        compositeDisposable += viewModel.state()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::render) { Timber.e(it) }
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

    private fun handle(addNoteAction: AddNoteAction) {
        when (addNoteAction) {
            AddNoteAction.Error -> {
                displaySnackBar(R.string.add_note_error)
            }
            AddNoteAction.InsertNote -> {
                viewModel.insertNote(createNoteEntity())
            }
            AddNoteAction.NoteAddedSuccessfully -> {
                displaySnackBar(R.string.add_note_success)
            }
        }.exhaustive
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun createNoteEntity(): NoteEntity {
        return NoteEntity(0, "", System.currentTimeMillis())
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
                labelBottomSheet.show(childFragmentManager, LabelBottomSheet.TAG)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEditor(view)
    }

    private fun setupEditor(view: View) {

        val editor = view.findViewById(R.id.editor) as Editor

        view.findViewById<Button>(R.id.action_h1).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.H1
            )
        }

        view.findViewById<Button>(R.id.action_h2).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.H2
            )
        }

        view.findViewById<Button>(R.id.action_h3).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.H3
            )
        }

        view.findViewById<ImageButton>(R.id.action_bold).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.BOLD
            )
        }

        view.findViewById<ImageButton>(R.id.action_Italic).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.ITALIC
            )
        }

        view.findViewById<ImageButton>(R.id.action_indent).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.INDENT
            )
        }

        view.findViewById<ImageButton>(R.id.action_outdent).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.OUTDENT
            )
        }

        view.findViewById<ImageButton>(R.id.action_bulleted).setOnClickListener {
            editor.insertList(
                false
            )
        }

        view.findViewById<ImageButton>(R.id.action_color).setOnClickListener {
            editor.updateTextColor(
                "#FF3333"
            )
        }

        view.findViewById<ImageButton>(R.id.action_unordered_numbered).setOnClickListener {
            editor.insertList(
                true
            )
        }

        view.findViewById<ImageButton>(R.id.action_hr).setOnClickListener { editor.insertDivider() }

        view.findViewById<ImageButton>(R.id.action_insert_image)
            .setOnClickListener { editor.openImagePicker() }

        view.findViewById<ImageButton>(R.id.action_insert_link)
            .setOnClickListener { editor.insertLink() }

        view.findViewById<ImageButton>(R.id.action_erase)
            .setOnClickListener { editor.clearAllContents() }

        view.findViewById<ImageButton>(R.id.action_blockquote).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.BLOCKQUOTE
            )
        }

        editor.render()
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        when(childFragment) {
            is LabelBottomSheet -> {
                childFragment.labelInteraction = this
                childFragment.labels = viewModel.labels
            }
        }
    }

    override fun labelClicked(label: LabelEntity) {
        viewModel.toggleLabel(label)

        addNoteChipGroup.removeAllViews()

        viewModel.labels.filter { it.checked }.forEach {
            val chip =
                layoutInflater.inflate(R.layout.add_note_chip, addNoteChipGroup, false) as Chip

            chip.text = it.label
            chip.setOnClickListener {
                labelBottomSheet.show(childFragmentManager, LabelBottomSheet.TAG)
            }
            addNoteChipGroup.addView(chip)
        }
    }
}
