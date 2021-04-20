package com.udacity.shoestore.views

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentShoeDetailBinding
import com.udacity.shoestore.models.Shoe
import com.udacity.shoestore.viewmodels.ShoeViewModel

class ShoeDetailFragment : Fragment() {

    private lateinit var binding: FragmentShoeDetailBinding
    private val viewModel: ShoeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shoe_detail, container, false)
        binding.shoeDetailsFragment = this
        binding.shoeModel = Shoe("", 0.0, "", "")
        binding.lifecycleOwner = this //allows to use LiveData to automatically update dataBinding layouts

        return binding.root
    }

    fun saveShoe(shoe: Shoe){
        if (checkFieldsValid()) {
           viewModel.saveShoe(shoe)
            findNavController().navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
        }
    }

    fun cancel() {
        findNavController().navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
    }

    private fun checkFieldsValid() : Boolean{
        var valid = true

        if (TextUtils.isEmpty(binding.shoeNameField.text)){
            binding.shoeNameField.error = "The item name cannot be empty!"
            valid = false
        }

        if (TextUtils.isEmpty(binding.companyField.text)){
            binding.companyField.error = "The item company cannot be empty!"
            valid = false
        }

        if (TextUtils.isEmpty(binding.sizeField.text)){
            binding.sizeField.error = "The item shoe size cannot be empty!"
            valid = false
        }

        if (TextUtils.isEmpty(binding.descriptionField.text)){
            binding.descriptionField.error = "The item description cannot be empty!"
            valid = false
        }
        return valid
    }
}