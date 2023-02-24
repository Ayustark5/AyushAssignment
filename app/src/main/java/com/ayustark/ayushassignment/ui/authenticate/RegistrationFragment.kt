package com.ayustark.ayushassignment.ui.authenticate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ayustark.ayushassignment.database.UserEntity
import com.ayustark.ayushassignment.databinding.FragmentRegistrationBinding
import com.ayustark.ayushassignment.ui.dashboard.DashboardActivity
import com.ayustark.ayushassignment.utils.Resource
import com.google.android.material.snackbar.Snackbar

class RegistrationFragment : Fragment() {

    private var bind: FragmentRegistrationBinding? = null

    private var viewModel: AuthenticateViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bind = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[AuthenticateViewModel::class.java]
        subscribeToObservers()
        setOnEventListeners()
        return bind?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bind = null
    }

    private fun setOnEventListeners() {
        bind?.apply {
            txtSignIn.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            btnSignUp.setOnClickListener {
                val user = UserEntity()
                user.name = nameLayout.editText?.text.toString()
                user.email = emailLayout.editText?.text.toString()
                user.mobile = mobileLayout.editText?.text.toString()
                user.password = passLayout.editText?.text.toString()
                user.address = addressLayout.editText?.text.toString()
                if (user.validate()) {
                    viewModel?.createNewUser(user)
                }
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel?.signUp?.observe(viewLifecycleOwner) {
            when (val signUpResponse = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    showSnackBar(signUpResponse.message)
                }
                is Resource.Loading -> {
                    Log.d("SignUpObserverResponse", "Loading")
                }
                is Resource.Success -> {
                    showSnackBar("Registration Successful")
                    startActivity(Intent(activity as Context, DashboardActivity::class.java))
                    activity?.finish()
                }
                null -> {
                    Log.d("SignUpObserverResponse", "null")
                }
            }
        }

    }

    private fun showSnackBar(msg: String?) {
        if (msg != null) {
            bind?.let { Snackbar.make(it.root, msg, Snackbar.LENGTH_SHORT).show() }
        }
    }
}