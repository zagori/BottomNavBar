package com.zagori.bottomnavbar.sample

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.zagori.bottomnavbar.BottomNavBar.OnBottomNavigationListener
import com.zagori.bottomnavbar.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bottomNavView.setBottomNavigationListener(object: OnBottomNavigationListener{
            override fun onNavigationItemSelected(menuItem: MenuItem?): Boolean {
                return when (menuItem!!.itemId) {
                    R.id.navigation_home -> {
                        binding.message.setText(R.string.title_home)
                        true
                    }
                    R.id.navigation_payment -> {
                        binding.message.setText(R.string.title_payment)
                        true
                    }
                    R.id.navigation_new_cart -> {
                        binding.message.setText(R.string.title_new_cart)
                        true
                    }
                    R.id.navigation_dashboard -> {
                        binding.message.setText(R.string.title_dashboard)
                        true
                    }
                    R.id.navigation_notifications -> {
                        binding.message.setText(R.string.title_notifications)
                        true
                    }
                    else -> false
                }
            }
        })
    }
}