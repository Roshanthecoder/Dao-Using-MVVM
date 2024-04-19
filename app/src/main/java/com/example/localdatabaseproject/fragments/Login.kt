package com.example.localdatabaseproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.localdatabaseproject.R
import com.example.localdatabaseproject.databinding.FragmentLoginBinding
import com.example.localdatabaseproject.models.User
import com.example.localdatabaseproject.viewmodel.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : Fragment(R.layout.fragment_login) {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: User

    private val model by viewModels<RoomViewModel>()

    // RoomRepo and ViewModelFactory instances
    /* private val roomRepo by lazy { RoomRepo(AppDatabase.getInstance(requireContext()).userDao()) }
     private val viewModelFactory by lazy { RoomViewModelFactory(roomRepo) }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //model = ViewModelProvider(this, viewModelFactory)[RoomViewModel::class.java]

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
            if (it) findNavController().navigate(
                R.id.action_login_to_welcomeScreen,
                bundleOf("user" to user.email)
            )
        }


    }

    private fun initListeners() {
        binding.buttonSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signup)
        }
        binding.buttonLoginOrSignup.setOnClickListener {
            user = User(
                0,
                null,
                binding.editTextEmail.text.toString(),
                binding.editTextPassword.text.toString()
            )
            model.loginCheck(user)
        }
        binding.listUserRecyc.setOnClickListener {
            // findNavController().navigate(R.id.action_login_to_userList)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}