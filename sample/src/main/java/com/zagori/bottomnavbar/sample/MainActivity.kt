package com.zagori.bottomnavbar.sample

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zagori.bottomnavbar.BottomNavBar
import com.zagori.bottomnavbar.BottomNavBar.OnBottomNavigationListener

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textMessage: TextView = findViewById(R.id.message)
        val bottomNavView = findViewById<BottomNavBar>(R.id.bottom_nav_view)

        bottomNavView.setBottomNavigationListener(object: OnBottomNavigationListener{
            override fun onNavigationItemSelected(menuItem: MenuItem?): Boolean {
                return when (menuItem!!.itemId) {
                    R.id.navigation_home -> {
                        textMessage.setText(R.string.title_home)
                        true
                    }
                    R.id.navigation_payment -> {
                        textMessage.setText(R.string.title_payment)
                        true
                    }
                    R.id.navigation_new_cart -> {
                        textMessage.setText(R.string.title_new_cart)
                        true
                    }
                    R.id.navigation_dashboard -> {
                        textMessage.setText(R.string.title_dashboard)
                        true
                    }
                    R.id.navigation_notifications -> {
                        textMessage.setText(R.string.title_notifications)
                        true
                    }
                    else -> false
                }
            }
        })
    }
}