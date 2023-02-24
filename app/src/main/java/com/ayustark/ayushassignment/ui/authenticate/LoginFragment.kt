package com.ayustark.ayushassignment.ui.authenticate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    private var bind: FragmentLoginBinding? = null

    private var viewModel: AuthenticateViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bind = FragmentLoginBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[AuthenticateViewModel::class.java]
        subscribeToObservers()
        setUpEventListeners()
        return bind?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bind = null
    }

    private fun setUpEventListeners() {
        bind?.apply {
            txtSignUp.setOnClickListener {
                Navigation.findNavController(root).navigate(R.id.login_to_registration)
            }
            btnLogin.setOnClickListener {
                val mobileNumberData = mobileLayout.editText?.text.toString()
                val passwordData = passLayout.editText?.text.toString()
                if (Patterns.PHONE.matcher(mobileNumberData).matches()) {
                    if (passwordData.length > 4) {
                        viewModel?.sendLoginRequest(LoginRequest(mobileNumberData, passwordData))
                    } else {
                        passLayout.error = getString(R.string.incorrect_creds)
                        mobileLayout.error = getString(R.string.incorrect_creds)
                    }
                } else {
                    passLayout.error = getString(R.string.incorrect_creds)
                    mobileLayout.error = getString(R.string.incorrect_creds)
                }
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel?.login?.observe(viewLifecycleOwner) {
            when (val loginResponse = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    showSnackBar(loginResponse.message)
                }
                is Resource.Loading -> {
                    Log.d("LoginObserverResponse", "Loading")
                }
                is Resource.Success -> {
                    showSnackBar("Login Successful")
                    startActivity(Intent(activity as Context, DashboardActivity::class.java))
                    activity?.finish()
                }
                null -> {
                    Log.d("LoginObserverResponse", "null")
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