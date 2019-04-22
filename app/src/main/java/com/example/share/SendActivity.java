package com.example.share;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.share.fragments.sendactivityfragments.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SendActivity extends AppCompatActivity {
    final String TAG = "SendActivity";
   public static List<FileToSendPath> mPathsList=new ArrayList<>();
  public static LinearLayout selectedDisplayLayout;
    public static TextView numOfFilesSelected;

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
        selectedDisplayLayout=(LinearLayout)findViewById(R.id.selected_display_layout);
        numOfFilesSelected=findViewById(R.id.num_of_files_selected);
    }
    public static void UpdateView(){
        numOfFilesSelected.setText(String.valueOf(mPathsList.size()));
        if(mPathsList.size()<1){
            selectedDisplayLayout.setVisibility(View.INVISIBLE);}
            else {selectedDisplayLayout.setVisibility(View.VISIBLE);}
    }
}
