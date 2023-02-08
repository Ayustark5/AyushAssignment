package com.ayustark.ayushassignment.ui.authenticate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ayustark.ayushassignment.R
import com.ayustark.ayushassignment.databinding.FragmentLoginBinding
import com.ayustark.ayushassignment.network.requests.LoginRequest
import com.ayustark.ayushassignment.ui.dashboard.DashboardActivity
import com.ayustark.ayushassignment.utils.Resource
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null

    private val bind get() = binding!!
    private lateinit var viewModel: AuthenticateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[AuthenticateViewModel::class.java]
        subscribeToObservers()
        setUpEventListeners()
        return binding?.root
    }

    private fun setUpEventListeners() {
        bind.signUp.setOnClickListener {
            Navigation.findNavController(bind.root).navigate(R.id.login_to_registration)
        }
        bind.btnLogin.setOnClickListener {
            val mobile = bind.mobileLayout.editText!!.text.toString()
            val pass = bind.passLayout.editText!!.text.toString()
            if (Patterns.PHONE.matcher(mobile).matches()) {
                if (pass.length > 4) {
                    viewModel.sendLoginRequest(LoginRequest(mobile, pass))
                } else {
                    bind.passLayout.error = "Incorrect Credentials"
                    bind.mobileLayout.error = "Incorrect Credentials"
                }
            } else {
                bind.passLayout.error = "Incorrect Credentials"
                bind.mobileLayout.error = "Incorrect Credentials"
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel.login.observe(viewLifecycleOwner) {
            when (val content = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    showSnackBar(content.message)
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    showSnackBar("Login Successful")
                    startActivity(Intent(activity as Context, DashboardActivity::class.java))
                    activity?.finish()
                }
                null -> {

                }
            }
        }
    }

    private fun showSnackBar(msg: String?) {
        if (msg != null) {
            Snackbar.make(bind.root, msg, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}