package com.ayustark.ayushassignment.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ayustark.ayushassignment.database.UserEntity
import com.ayustark.ayushassignment.databinding.FragmentProfileBinding
import com.ayustark.ayushassignment.ui.authenticate.AuthenticateActivity
import com.ayustark.ayushassignment.utils.Resource
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : Fragment() {

    private var bind: FragmentProfileBinding? = null
    private var viewModel: DashboardViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bind = FragmentProfileBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[DashboardViewModel::class.java]
        (activity as DashboardActivity).supportActionBar?.apply {
            title = "Profile"
            setDisplayHomeAsUpEnabled(true)
        }
        subscribeToObservers()
        setupEventListeners()
        viewModel?.getUserDetails()
        return bind?.root
    }

    override fun onDestroyView() {
        bind = null
        super.onDestroyView()
    }

    private fun subscribeToObservers() {
        viewModel?.user?.observe(viewLifecycleOwner) {
            when (val userResponse = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    showSnackBar(userResponse.message)
                }
                is Resource.Loading -> {
                    Log.d("UserObserverResponse", "Loading")
                }
                is Resource.Success -> {
                    if (userResponse.data != null)
                        setupUI(userResponse.data)
                    else
                        showSnackBar("Some Error Occurred")
                }
                null -> {
                    Log.d("UserObserverResponse", "null")
                }
            }
        }
    }

    private fun setupEventListeners() {
        bind?.logout?.setOnClickListener {
            viewModel?.logout()
            startActivity(Intent(context, AuthenticateActivity::class.java))
            activity?.finish()
        }
    }

    private fun setupUI(data: UserEntity) {
        bind?.apply {
            name.text = data.name
            email.text = data.email
            mobile.text = data.mobile
            address.text = data.address
        }
    }

    private fun showSnackBar(msg: String?) {
        if (msg != null) {
            bind?.let { Snackbar.make(it.root, msg, Snackbar.LENGTH_SHORT).show() }
        }
    }

}