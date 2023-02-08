package com.ayustark.ayushassignment.ui.dashboard

import android.content.Intent
import android.os.Bundle
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

    private var binding: FragmentProfileBinding? = null
    private val bind get() = binding!!
    lateinit var viewModel: DashboardViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[DashboardViewModel::class.java]
        (activity as DashboardActivity).supportActionBar?.apply {
            title = "Profile"
            setDisplayHomeAsUpEnabled(true)
        }
        subscribeToObservers()
        setupEventListeners()
        viewModel.getUserDetails()
        return binding?.root
    }

    private fun subscribeToObservers() {
        viewModel.user.observe(viewLifecycleOwner) {
            when (val user = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    showSnackBar(user.message)
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    if (user.data != null)
                        setupUI(user.data)
                    else
                        showSnackBar("Some Error Occurred")
                }
                null -> {

                }
            }
        }
    }

    private fun setupEventListeners() {
        bind.logout.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(context, AuthenticateActivity::class.java))
            activity?.finish()
        }
    }

    private fun setupUI(data: UserEntity) {
        bind.name.text = data.name
        bind.email.text = data.email
        bind.mobile.text = data.mobile
        bind.address.text = data.address
    }

    private fun showSnackBar(msg: String?) {
        if (msg != null) {
            Snackbar.make(bind.root, msg, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}