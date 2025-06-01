package com.example.onlinecoursesapp.activities.dashboard;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.fragments.admin.LessonManagementFragment;
import com.example.onlinecoursesapp.fragments.admin.OverviewFragment;
import com.example.onlinecoursesapp.fragments.admin.SettingsFragment;
import com.example.onlinecoursesapp.fragments.admin.UsersManagementFragment;
import com.example.onlinecoursesapp.fragments.admin.CategoryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

@SuppressWarnings("deprecation")
public class DashboardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);

        // Load default fragment
        loadFragment(new OverviewFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;

        int id = item.getItemId();
        if (id == R.id.nav_overview) {
            fragment = new OverviewFragment();
        } else if (id == R.id.nav_courses) {
            fragment = new CategoryFragment();
        } else if (id == R.id.nav_users) {
            fragment = new UsersManagementFragment();
        } else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
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
