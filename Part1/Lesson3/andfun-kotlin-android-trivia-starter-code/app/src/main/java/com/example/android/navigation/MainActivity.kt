/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.navigation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout

        navController = this.findNavController(R.id.navigaitonHostFragment) //1: Find the navigation controller from navigationHostFragment
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout )//2: Link the navController to our ActionBar + drawer layout

        //prevent nav gesture if not on start destination
        navController.addOnDestinationChangedListener{ nc: NavController, nd: NavDestination, args: Bundle? ->
            //sets the DrawerLockMode depending on what destination we’re navigating to. When the id of our NavDestination matches
            // the startDestination of our graph, we’ll unlock the drawerLayout; otherwise, we’ll lock and close the drawerLayout
            if (nd.id == nc.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

        NavigationUI.setupWithNavController(binding.navView, navController) //4: Setup navigationUI to know about the navigation view
    }

    override fun onSupportNavigateUp(): Boolean {   //3: Override onSupportNavigateUp
       // return navController.navigateUp()

        //5: Replace navController.navigateUp() with NavigationUi.navigateUp with drawerLayout as a parameter
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}
