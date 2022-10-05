package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.provideFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val asteroidListAdapter = AsteroidListAdapter { asteroid ->
            // Navigate to the detail fragment when an item is clicked.
            findNavController().navigate(
                MainFragmentDirections.actionShowDetail(asteroid)
            )
        }
        binding.asteroidRecycler.adapter = asteroidListAdapter
        viewModel.asteroidList.observe(viewLifecycleOwner) { asteroidList ->
            // Update the recycler view's list.
            asteroidListAdapter.submitList(asteroidList)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorMessage.collect { message -> showSnackbar(message) }
            }
        }
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)
    }

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.main_overflow_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return true
        }
    }

    /**
     * Shows a snackbar on the screen for [duration] milliseconds.
     */
    private fun showSnackbar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content), message, duration
        ).show()
    }
}