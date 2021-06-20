package com.zagori.bottomnavbar.sample

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zagori.bottomnavbar.BottomNavBar
import com.zagori.bottomnavbar.BottomNavBar.OnBottomNavigationListener

class MainActivity : AppCompatActivity() {
    private var mTextMessage: TextView? = null
    private val mOnBottomNavItemSelectedListener: OnBottomNavigationListener =
        object : OnBottomNavigationListener {
            override fun onNavigationItemSelected(menuItem: MenuItem?): Boolean {
                when (menuItem!!.itemId) {
                    R.id.navigation_home -> {
                        mTextMessage!!.setText(R.string.title_home)
                        return true
                    }
                    R.id.navigation_payment -> {
                        mTextMessage!!.setText(R.string.title_payment)
                        return true
                    }
                    R.id.navigation_new_cart -> {
                        mTextMessage!!.setText(R.string.title_new_cart)
                        return true
                    }
                    R.id.navigation_dashboard -> {
                        mTextMessage!!.setText(R.string.title_dashboard)
                        return true
                    }
                    R.id.navigation_notifications -> {
                        mTextMessage!!.setText(R.string.title_notifications)
                        return true
                    }
                }
                return false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTextMessage = findViewById(R.id.message)
        val bottomNavView = findViewById<BottomNavBar>(R.id.bottom_nav_view)
        bottomNavView.setBottomNavigationListener(mOnBottomNavItemSelectedListener)
    }
}