package com.udacity.shoestore.views

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentShoeListBinding
import com.udacity.shoestore.databinding.ShoeItemBinding
import com.udacity.shoestore.viewmodels.ShoeViewModel


class ShoeListFragment : Fragment() {

    private lateinit var binding: FragmentShoeListBinding
    private val viewModel: ShoeViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shoe_list, container, false)


        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(ShoeListFragmentDirections.actionShoeListFragmentToShoeDetailFragment())
        }

        viewModel.shoeList.observe(viewLifecycleOwner, Observer {  shoeList ->
            shoeList.forEach {
                var layoutBinding : ShoeItemBinding =  DataBindingUtil.inflate(inflater, R.layout.shoe_item, binding.linearLayout, false)
                layoutBinding.shoeItem = it

                binding.linearLayout.addView(layoutBinding.root)
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu) //Inflate our menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if  (item.title == "Logout"){
            findNavController().navigate(ShoeListFragmentDirections.actionShoeListFragmentToLoginFragment())
        }

            return super.onOptionsItemSelected(item)
      //  return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) || super.onOptionsItemSelected(item)
    }
}