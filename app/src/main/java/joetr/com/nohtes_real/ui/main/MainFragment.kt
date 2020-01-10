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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import joetr.com.nohtes_real.R
import joetr.com.nohtes_real.android.base.BaseFragment
import joetr.com.nohtes_real.android.extensions.exhaustive
import joetr.com.nohtes_real.di.component.FragmentComponent
import joetr.com.nohtes_real.ui.note.NOTE_ARG
import kotlinx.android.synthetic.main.main_fragment.*
import timber.log.Timber
import javax.inject.Inject


class MainFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel

    private val controller = MainController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
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
        var drawableWrap = DrawableCompat.wrap(item.icon).mutate()

        drawableWrap = if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
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
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        // todo joe display tag on main

        // joe todo hide fab on RV scroll
        mainAddButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_addNoteFragment)
        }
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
                controller.setData(state.data, ::handle)
                mainBaseLayout.displayedChild = R.id.mainContentContainer
            }
        }.exhaustive
    }
}
