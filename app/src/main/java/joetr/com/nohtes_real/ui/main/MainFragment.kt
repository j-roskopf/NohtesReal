package joetr.com.nohtes_real.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.noties.markwon.Markwon
import io.noties.markwon.image.ImagesPlugin
import io.noties.markwon.image.file.FileSchemeHandler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import joetr.com.nohtes_real.NIGHT_MODE_KEY
import joetr.com.nohtes_real.R
import joetr.com.nohtes_real.android.base.BaseFragment
import joetr.com.nohtes_real.android.extensions.exhaustive
import joetr.com.nohtes_real.di.component.FragmentComponent
import joetr.com.nohtes_real.ui.note.NOTE_ARG
import joetr.com.swipereveal.SwipeToLeftCallback
import joetr.com.swipereveal.SwipeToRightCallback
import kotlinx.android.synthetic.main.main_fragment.*
import timber.log.Timber
import javax.inject.Inject


class MainFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel

    private val controller = MainController()

    lateinit var markwon: Markwon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]

        markwon = Markwon.builder(requireContext())
            .usePlugin(ImagesPlugin.create { plugin -> plugin.addSchemeHandler(FileSchemeHandler.create())})
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun injectSelf(component: FragmentComponent) = component.inject(this)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val item: MenuItem = menu.findItem(R.id.toggleTheme)

        val drawableWrap = if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_sun)!!
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_moon)!!
        }

        DrawableCompat.setTint(drawableWrap, ContextCompat.getColor(requireContext(), R.color.iconColor))
        item.icon = drawableWrap
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        return if (id == R.id.toggleTheme) {
            // toggle theme
            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                sharedPreferences.edit().putBoolean(NIGHT_MODE_KEY, false).apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                sharedPreferences.edit().putBoolean(NIGHT_MODE_KEY, true).apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    mainAddButton.hide()
                } else {
                    mainAddButton.show()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        mainRecyclerView.adapter = controller.adapter
        mainRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        compositeDisposable += viewModel.action()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handle) { Timber.e(it) }

        compositeDisposable += viewModel.state()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::render) { Timber.e(it) }

        handle(MainPageAction.GetAllNotes)

        mainAddButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_addNoteFragment)
        }

        val deleteIcon = if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.ic_delete_white_24dp
        } else {
            R.drawable.ic_delete_black_24dp
        }

        // swiping both directions will delete
        val swipeToLeftCallback = object : SwipeToLeftCallback(requireContext(), deleteIcon, android.R.color.transparent) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteItemAtPosition(viewHolder.adapterPosition)
            }
        }
        val swipeToRightCallback = object : SwipeToRightCallback(requireContext(), deleteIcon, android.R.color.transparent) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteItemAtPosition(viewHolder.adapterPosition)
            }
        }

        val swipeToLeftItemTouchHelper = ItemTouchHelper(swipeToLeftCallback)
        swipeToLeftItemTouchHelper.attachToRecyclerView(mainRecyclerView)

        val swipeToRightItemTouchHelper = ItemTouchHelper(swipeToRightCallback)
        swipeToRightItemTouchHelper.attachToRecyclerView(mainRecyclerView)
    }

    private fun handle(action : MainPageAction) {
        when(action) {
            MainPageAction.GetAllNotes -> {
                viewModel.getAllNotes()
            }
            is MainPageAction.NoteClicked -> {
                findNavController(this).navigate(R.id.action_mainFragment_to_addNoteFragment, bundleOf(NOTE_ARG to action.noteEntity))
            }
        }.exhaustive
    }

    private fun render(state : MainPageState) {
        when(state) {
            MainPageState.Loading -> {
                mainBaseLayout.displayedChild = R.id.mainLoadingContainer
            }
            MainPageState.Error -> {
                mainBaseLayout.displayedChild = R.id.mainErrorContainer
            }
            is MainPageState.Content -> {
                controller.setData(state.data, ::handle, markwon)
                mainBaseLayout.displayedChild = R.id.mainContentContainer

                // show the RV
                mainRecyclerView.visibility = View.VISIBLE

                // hide empty content
                emptyContentContainer.visibility = View.GONE
            }
            MainPageState.Empty -> {
                mainBaseLayout.displayedChild = R.id.mainContentContainer

                // hide the RV
                mainRecyclerView.visibility = View.GONE

                // display empty content
                emptyContentContainer.visibility = View.VISIBLE
            }
        }.exhaustive
    }
}
