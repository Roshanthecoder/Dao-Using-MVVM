package com.example.localdatabaseproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.localdatabaseproject.R
import com.example.localdatabaseproject.adapter.ExerciseAdapter
import com.example.localdatabaseproject.databinding.FragmentExerciseBinding
import com.example.localdatabaseproject.viewmodel.ApiViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseFragment : Fragment(R.layout.fragment_exercise) {

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ExerciseAdapter
    private val models by viewModels<ApiViewModel>()
    private val TAG = "Roshan"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentExerciseBinding.bind(view)

        initViewObserver()
        initSetView()
        initListeners()
    }

    private fun initListeners() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle query submission (e.g., start a search)
                query?.let {
                    performSearch(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle text changes in the search view
                newText?.let {
                    // Perform search as the user types (optional)
                }
                return true
            }
        })
    }

    private fun performSearch(query: String) {

        if (query.length >= 3) {
            models.searchExercises(query)
        }
    }

    private fun initViewObserver() {
        models.exercisesList.observe(viewLifecycleOwner) { list ->
            if (list != null && list.isNotEmpty()) {
                Log.e(TAG, "Received exercise list: $list")
                adapter = ExerciseAdapter(list)
                binding.rvExerciseList.adapter = adapter
                // Bind data to views using `binding` here
            } else {
                Log.e(TAG, "Exercise list is empty or null")
            }
        }
        models.loader.observe(viewLifecycleOwner) {
            binding.progressdialog.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun initSetView() {
        lifecycleScope.launch {
            models.getExercises()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
