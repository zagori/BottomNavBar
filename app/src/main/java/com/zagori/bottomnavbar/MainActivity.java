package com.zagori.bottomnavbar;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private BottomNavBar.OnBottomNavigationListener mOnBottomNavItemSelectedListener =
            new BottomNavBar.OnBottomNavigationListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.navigation_home:
                            mTextMessage.setText(R.string.title_home);
                            return true;
                        case R.id.navigation_payment:
                            mTextMessage.setText(R.string.title_payment);
                            return true;
                        case R.id.navigation_new_cart:
                            mTextMessage.setText(R.string.title_new_cart);
                            return true;
                        case R.id.navigation_dashboard:
                            mTextMessage.setText(R.string.title_dashboard);
                            return true;
                        case R.id.navigation_notifications:
                            mTextMessage.setText(R.string.title_notifications);
                            return true;
                    }
                    return false;
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = findViewById(R.id.message);
        BottomNavBar bottomNavView = findViewById(R.id.bottom_nav_view);
        bottomNavView.setBottomNavigationListener(mOnBottomNavItemSelectedListener);
    }
}
