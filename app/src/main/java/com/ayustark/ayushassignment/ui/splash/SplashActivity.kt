package com.ayustark.ayushassignment.ui.splash

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.ayustark.ayushassignment.R
import com.ayustark.ayushassignment.databinding.ActivitySplashBinding
import com.ayustark.ayushassignment.ui.authenticate.AuthenticateActivity
import com.ayustark.ayushassignment.ui.dashboard.DashboardActivity
import com.ayustark.ayushassignment.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private var binding: ActivitySplashBinding? = null
    private val bind
        get() = binding!!

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding?.root)
        bind.logo.setBackgroundResource(R.drawable.logo_animation)
        val anim = bind.logo.background as AnimationDrawable
        CoroutineScope(Dispatchers.Main).launch {
            try {
                anim.start()
                delay(1000)
            } catch (e: InterruptedException) {
                Log.e("Splash", e.message.toString())
            }
            if (sharedPreferences.getBoolean(Constants.IS_LOGGED, false)) {
                startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
                finish()
                return@launch
            }
            startActivity(Intent(this@SplashActivity, AuthenticateActivity::class.java))
            finish()
        }
    }
}