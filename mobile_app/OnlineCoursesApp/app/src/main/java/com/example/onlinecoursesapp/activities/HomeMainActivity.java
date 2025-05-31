package com.example.onlinecoursesapp.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.fragments.home.HomeFragment;
import com.example.onlinecoursesapp.fragments.home.ProfileFragment;
import com.example.onlinecoursesapp.fragments.home.SearchFragment;
import com.example.onlinecoursesapp.fragments.home.SettingHomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

@SuppressWarnings("deprecation")
public class HomeMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);

        // Load default fragment
        loadFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;

        int id = item.getItemId();
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_users) {
            fragment = new ProfileFragment();
        } else if (id == R.id.nav_search) {
            fragment = new SearchFragment();
        } else if (id == R.id.nav_settings) {
            fragment = new SettingHomeFragment();
        } else {
            fragment = null;
        }

        return loadFragment(fragment);
    }


    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}

