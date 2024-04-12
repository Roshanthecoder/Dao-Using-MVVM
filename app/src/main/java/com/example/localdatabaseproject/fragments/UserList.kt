package com.example.localdatabaseproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.localdatabaseproject.R
import com.example.localdatabaseproject.adapter.UserAdapter
import com.example.localdatabaseproject.databinding.FragmentUserListBinding
import com.example.localdatabaseproject.models.User
import com.example.localdatabaseproject.repository.RoomRepo
import com.example.localdatabaseproject.roomdb.AppDatabase
import com.example.localdatabaseproject.viewmodel.RoomViewModel
import com.example.localdatabaseproject.viewmodelfactory.RoomViewModelFactory

class UserList : Fragment(R.layout.fragment_user_list) {
    private var _binding: FragmentUserListBinding? = null
    private lateinit var model: RoomViewModel

    // RoomRepo and ViewModelFactory instances
    private val roomRepo by lazy { RoomRepo(AppDatabase.getInstance(requireContext()).userDao()) }
    private val viewModelFactory by lazy { RoomViewModelFactory(roomRepo) }
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserListBinding.bind(view)
        model = ViewModelProvider(this, viewModelFactory)[RoomViewModel::class.java]
        Log.e("roshan", "Userlist")
        model.getUserList()
        initObserver()
        initListener()

    }

    private fun initRecyclerview(users: List<User>) {
        binding.rvUserList.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rvUserList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val adapter = UserAdapter(users)
        binding.rvUserList.adapter = adapter
    }

    private fun initListener() {
    }

    private fun initObserver() {
        model.userList.observe(viewLifecycleOwner) {
            Log.e("roshan", "initObserver: $it")
            initRecyclerview(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}