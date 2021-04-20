package com.udacity.shoestore.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.ActivityMainBinding
import com.udacity.shoestore.viewmodels.ShoeViewModel


class MainActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ShoeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(ShoeViewModel::class.java)

        val appBarConfiguration = AppBarConfiguration
                .Builder(R.id.loginFragment,
                        R.id.welcomeScreenFragment,
                        R.id.shoeListFragment)
                .build()

        setSupportActionBar(binding.toolbar)

        navController = this.findNavController(R.id.navigationHostFragment) //find the navigation controller from navigationHostFragment
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration) // Link the navController to ActionBar
    }

    override fun onSupportNavigateUp(): Boolean {   //3: Override onSupportNavigateUp
         return navController.navigateUp()
    }
}
