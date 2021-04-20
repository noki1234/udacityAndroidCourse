package com.udacity.shoestore.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.models.Shoe

class ShoeViewModel : ViewModel() {

   private var _shoeList = MutableLiveData(mutableListOf<Shoe>())
   val shoeList : LiveData<MutableList<Shoe>>
    get() = _shoeList

    init {
         saveShoe(Shoe("Jordans", 42.5, "Nike", "Sport and popular shoe of known company Nike", listOf("")))
    }

    fun saveShoe(shoe: Shoe){
        _shoeList.value!!.add(shoe)
    }

}