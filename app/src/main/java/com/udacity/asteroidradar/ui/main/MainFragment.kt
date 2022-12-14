package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.provideFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
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
        viewModel.asteroids.observe(viewLifecycleOwner) { asteroids ->
            // Update the recycler view's list.
            asteroidListAdapter.submitList(asteroids)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            // Show a snackbar with the network error's message.
            if (error != null) {
                showSnackbar(error)
                viewModel.onReceivedErrorMessage()
            }
        }
        // Add an options menu. We pass `viewLifecycleOwner`
        // to remove it when the view is destroyed.
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)
    }

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.main_overflow_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            viewModel.applyFilter(
                when (val id = menuItem.itemId) {
                    R.id.show_all_menu -> null
                    R.id.show_today_menu -> AsteroidFilter.TODAY
                    R.id.show_week_menu -> AsteroidFilter.THIS_WEEK
                    else -> error("Unknown menu item id: $id")
                }
            )
            return true
        }
    }

    /**
     * Shows a [Snackbar] for [duration] milliseconds.
     */
    private fun showSnackbar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(
            // We use `android.R.id.content` to show the snackbar correctly.
            // Using `this.view` is not correct for this use case.
            requireActivity().findViewById(android.R.id.content), message, duration
        ).show()
    }
}