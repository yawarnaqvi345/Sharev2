package com.example.share;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.share.SendActivityFragments.ViewPagerAdapter;

public class SendActivity extends AppCompatActivity {
    final String TAG = "SendActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate");
        setContentView(R.layout.activity_send);
        ViewPager viewPager = (ViewPager) findViewById(R.id.send_activity_view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.send_activity_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
