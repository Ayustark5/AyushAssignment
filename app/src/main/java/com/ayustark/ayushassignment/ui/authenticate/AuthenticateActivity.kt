package com.ayustark.ayushassignment.ui.authenticate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ayustark.ayushassignment.databinding.ActivityAuthenticateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticateActivity : AppCompatActivity() {
    private var binding: ActivityAuthenticateBinding? = null
    private val bind get() = binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticateBinding.inflate(layoutInflater)
        ViewModelProvider(this@AuthenticateActivity)[AuthenticateViewModel::class.java]
        setContentView(binding?.root)
    }
}