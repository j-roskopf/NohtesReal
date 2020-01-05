package joetr.com.nohtes_real.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import joetr.com.nohtes_real.R
import joetr.com.nohtes_real.android.base.BaseFragment
import joetr.com.nohtes_real.di.component.FragmentComponent
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : BaseFragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun injectSelf(component: FragmentComponent) = component.inject(this)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainAddButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_mainFragment_to_addNoteFragment)
        }
    }
}
