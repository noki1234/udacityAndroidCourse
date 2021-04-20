package com.example.android.navigation

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentTitleBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)
        binding.playButton.setOnClickListener{view : View -> findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToGameFragment()) }


        // binding.playButton.setOnClickListener { view: View ->
           // findNavController().navigate(R.id.action_titleFragment_to_gameFragment)
       // }

        //1: Tell titleFragment that it will have an options menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) { //2: Override method onCreateOptionsMenu()
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu) // 3: inflate our menu (overflow_menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {  //4: when menu is selected - override method onOptionsItemSelected()
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) || super.onOptionsItemSelected(item)
    }
}