package com.example.localdatabaseproject.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.localdatabaseproject.R
import com.example.localdatabaseproject.databinding.FragmentLoginBinding
import com.example.localdatabaseproject.models.User
import com.example.localdatabaseproject.repository.RoomRepo
import com.example.localdatabaseproject.roomdb.AppDatabase
import com.example.localdatabaseproject.viewmodel.RoomViewModel
import com.example.localdatabaseproject.viewmodelfactory.RoomViewModelFactory


class Login : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var model: RoomViewModel

    // RoomRepo and ViewModelFactory instances
    private val roomRepo by lazy { RoomRepo(AppDatabase.getInstance(requireContext()).userDao()) }
    private val viewModelFactory by lazy { RoomViewModelFactory(roomRepo) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        model = ViewModelProvider(this, viewModelFactory)[RoomViewModel::class.java]

        initObservers()

        initListeners()
    }


    private fun initObservers() {
        model.loginCheckProcess.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                if (!it) {
                    "No Account Created"
                } else {
                    "Login Sucessfully"
                },
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    private fun initListeners() {
        binding.buttonSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signup)
        }
        binding.buttonLoginOrSignup.setOnClickListener {
            val user = User(
                0,
                null,
                binding.editTextEmail.text.toString(),
                binding.editTextPassword.text.toString()
            )
            model.loginCheck(user)
        }
        binding.listUserRecyc.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_userList)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}