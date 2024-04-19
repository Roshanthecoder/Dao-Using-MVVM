package com.example.localdatabaseproject.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.localdatabaseproject.R
import com.example.localdatabaseproject.databinding.FragmentSignupBinding
import com.example.localdatabaseproject.models.User
import com.example.localdatabaseproject.viewmodel.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Signup : Fragment(R.layout.fragment_signup) {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val model: RoomViewModel by viewModels()

    // RoomRepo and ViewModelFactory instances
    /*  private val roomRepo by lazy { RoomRepo(AppDatabase.getInstance(requireContext()).userDao()) }
      private val viewModelFactory by lazy { RoomViewModelFactory(roomRepo) }*/


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignupBinding.bind(view)
        // ViewModel instance initialization
        // model = ViewModelProvider(this, viewModelFactory)[RoomViewModel::class.java]
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        model.signupSuccess.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                if (it) {
                    findNavController().popBackStack()
                    "SignUp Success"
                } else {
                    "Already Registered"
                },
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initListeners() {
        binding.buttonSignUp.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonLoginOrSignup.setOnClickListener {
            // Create a new User object with user input
            val user = User(
                0,
                binding.editTextName.text.toString(), binding.editTextEmail.text.toString(),
                binding.editTextPassword.text.toString(),
                binding.editTextCompanyName.text.toString()
            )

            // Call the signUpProcess function of the ViewModel
            model.signUp(user)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}