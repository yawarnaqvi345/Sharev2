package com.example.share;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
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
    public static ImageView crossButton;
    public static ImageView sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "OnCreate");
        setContentView(R.layout.activity_send);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.send_activity_view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.send_activity_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        selectedDisplayLayout=(LinearLayout)findViewById(R.id.selected_display_layout);
        numOfFilesSelected=findViewById(R.id.num_of_files_selected);
        crossButton=findViewById(R.id.cross_button);
        sendButton=findViewById(R.id.send_button);
        crossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPathsList.clear();
                UpdateView();
                viewPager.onRestoreInstanceState(null);
                CheckBox checkbox=findViewById(R.id.apps_checkbox);
                checkbox.setChecked(false);
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPathsList.clear();
    }

    public static void UpdateView(){
        numOfFilesSelected.setText(String.valueOf(mPathsList.size()));
        if(mPathsList.size()<1){
            selectedDisplayLayout.setVisibility(View.INVISIBLE);}
            else {selectedDisplayLayout.setVisibility(View.VISIBLE);}
    }

    public static FileToSendPath getRefference(FileToSendPath mPath){
        for(FileToSendPath path:SendActivity.mPathsList){
            if(path.getPath().equals(mPath.getPath()))
                return path;
        }
        return null;
    }
}
