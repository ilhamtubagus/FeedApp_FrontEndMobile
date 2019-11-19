package com.example.feedapp.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.feedapp.R;
import com.example.feedapp.fragment.Feed;
import com.example.feedapp.fragment.More;
import com.example.feedapp.fragment.NewFeed;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, new Feed()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            switch (menuItem.getItemId()){
                case R.id.nav_feed:
                    selectedFragment = new Feed();

                    break;
                case R.id.nav_newFeed:
                    selectedFragment = new NewFeed();

                    break;
                case R.id.nav_more:
                    selectedFragment = new More();

                    break;
            }
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            ft.replace(R.id.fl_main, selectedFragment);
            ft.commit();
            return true;
        }
    };
}
