package com.ayustark.ayushassignment.ui.authenticate

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

    private var binding: FragmentRegistrationBinding? = null

    private val bind get() = binding!!
    private lateinit var viewModel: AuthenticateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[AuthenticateViewModel::class.java]
        subscribeToObservers()
        setOnEventListeners()
        return binding?.root
    }

    private fun setOnEventListeners() {
        bind.signIn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        bind.btnSignUp.setOnClickListener {
            val user = UserEntity()
            user.name = bind.nameLayout.editText!!.text.toString()
            user.email = bind.emailLayout.editText!!.text.toString()
            user.mobile = bind.mobileLayout.editText!!.text.toString()
            user.password = bind.passLayout.editText!!.text.toString()
            user.address = bind.addressLayout.editText!!.text.toString()
            if (user.validate()) {
                viewModel.createNewUser(user)
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel.signUp.observe(viewLifecycleOwner) {
            when (val content = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    showSnackBar(content.message)
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    showSnackBar("Registration Successful")
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