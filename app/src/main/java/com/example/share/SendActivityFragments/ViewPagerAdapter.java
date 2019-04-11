package com.example.share.SendActivityFragments;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.example.share.R;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    Fragment apps,files,photos,videos,music;
    private Context mContext;
    TabLayout.Tab tabLayout;
    public ViewPagerAdapter(Context cntxt,FragmentManager fm) {
        super(fm);
        mContext=cntxt;
        int a;
        apps=new Apps();
        files=new Files();
        photos=new Photos();
        videos=new Videos();
        music=new Music();
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0:
                return apps;
            case 1:
                return files;
            case 2:
                return photos;
            case 3:
                return videos;
            case 4:
                return music;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.String_Apps);
            case 1:
                return mContext.getString(R.string.String_Files);
            case 2:
                return mContext.getString(R.string.String_photos);
            case 3:
                return mContext.getString(R.string.String_videos);
            case 4:
                return mContext.getString(R.string.String_nusic);
            default:
                return null;
        }
    }
}
