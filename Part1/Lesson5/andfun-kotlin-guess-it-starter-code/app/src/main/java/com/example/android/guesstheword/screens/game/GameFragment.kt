/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.Buzz
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding
import java.util.EnumSet.of

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding: GameFragmentBinding
    private lateinit var buzzer: Buzz

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        buzzer = Buzz(requireActivity())
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        binding.gameViewModel = viewModel //Assigning viewModel into XML <variable> gameViewModel
        binding.lifecycleOwner = this //allows to use LiveData to automatically update dataBinding layouts

        viewModel.eventGameFinished.observe(viewLifecycleOwner, Observer {  hasFinished ->
            if (hasFinished) {
                val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0)
                findNavController(this).navigate(action)
                viewModel.onGameFinishedComplete()
            }
        })

        viewModel.buzzType.observe(viewLifecycleOwner, Observer { newBuzzType ->
            buzzer.buzz(newBuzzType)
        })

        return binding.root

    }
}
