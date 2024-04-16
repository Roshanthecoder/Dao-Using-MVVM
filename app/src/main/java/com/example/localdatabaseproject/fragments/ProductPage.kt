package com.example.localdatabaseproject.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.localdatabaseproject.R
import com.example.localdatabaseproject.adapter.ProductListAdapter
import com.example.localdatabaseproject.databinding.FragmentProductPageBinding
import com.example.localdatabaseproject.repository.RoomRepo
import com.example.localdatabaseproject.roomdb.Action
import com.example.localdatabaseproject.roomdb.AppDatabase
import com.example.localdatabaseproject.viewmodel.RoomViewModel
import com.example.localdatabaseproject.viewmodelfactory.RoomViewModelFactory


class ProductPage : Fragment(R.layout.fragment_product_page) {

    private var _binding: FragmentProductPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProductListAdapter
    private lateinit var model: RoomViewModel

    // RoomRepo and ViewModelFactory instances
    private val roomRepo by lazy { RoomRepo(AppDatabase.getInstance(requireContext()).userDao()) }
    private val viewModelFactory by lazy { RoomViewModelFactory(roomRepo) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductPageBinding.bind(view)

        model = ViewModelProvider(this, viewModelFactory)[RoomViewModel::class.java]
        model.getAllProductList()

        initObservers()

        //   initListeners()
    }

    private fun initObservers() {
        model.productList.observe(viewLifecycleOwner) {
            adapter = ProductListAdapter(it)
            binding.productpagerecy.adapter = adapter
            initListeners()
        }
    }

    private fun initListeners() {
        adapter.setOnItemClickListener { _, action ->
            when (action) {
                Action.PLUS -> {

                }

                Action.MINUS -> {

                }

                else -> ""

            }
        }
    }


}