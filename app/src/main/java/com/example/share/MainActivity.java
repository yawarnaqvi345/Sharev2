package com.example.share;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.share.MainFragments.FileExplorer;
import com.example.share.MainFragments.TextStream;
import com.example.share.MainFragments.Transfer;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_section, new Transfer()).commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Log.d(TAG, "NavigationMenuClicked");
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_transfers:
                            selectedFragment = new Transfer();
                            break;
                        case R.id.nav_files_explorer:
                            selectedFragment = new FileExplorer();
                            break;
                        case R.id.nav_text_stream:
                            selectedFragment = new TextStream();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_section, selectedFragment).commit();
                    return true;
                }
            };
}
