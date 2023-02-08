package com.ayustark.ayushassignment.ui.dashboard

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ayustark.ayushassignment.databinding.ActivityDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private var binding: ActivityDashboardBinding? = null
    private val bind get() = binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        ViewModelProvider(this@DashboardActivity)[DashboardViewModel::class.java]
        setupActionBar()
        setContentView(binding?.root)
    }

    private fun setupActionBar() {
        setSupportActionBar(bind.Toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return true
    }*/

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile -> supportFragmentManager.beginTransaction().addToBackStack("current").replace(R.id.navHost, ProfileFragment::class.java, null).commit()
        }
        return super.onOptionsItemSelected(item)
    }*/
}