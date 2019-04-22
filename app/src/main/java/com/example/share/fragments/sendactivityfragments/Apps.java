package com.example.share.fragments.sendactivityfragments;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.share.FileToSendPath;
import com.example.share.R;
import com.example.share.models.AppIication;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Apps extends Fragment {

GridView appsGridView;

    public Apps() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_apps, container, false);
        appsGridView=rootView.findViewById(R.id.apps_gridview);
       // ArrayList<AppIication> appsList=getInstalledApps(false);
        appsGridView.setAdapter(new AppsAdapter(getActivity()));
       // ArrayList<AppIication> appsList=getInstalledApps(false);
        // Inflate the layout for this fragment
        return rootView;
    }


    private ArrayList<AppIication> getInstalledApps(boolean getSysPackages) {
        ArrayList<AppIication> appsList = new ArrayList<AppIication>();
        List<PackageInfo> packs = getContext().getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }
            AppIication newApp = new AppIication();
            newApp.setAppname(p.applicationInfo.loadLabel(getContext().getPackageManager()).toString());
            newApp.setPname(p.packageName);
           // newApp.setVersionName(p.versionName);
           // newApp.setVersionCode(p.versionCode);
            newApp.setIcon(p.applicationInfo.loadIcon(getContext().getPackageManager()));
            appsList.add(newApp);
        }
        return appsList;
    }

    private class AppsAdapter extends BaseAdapter {
        /**
         * The context.
         */
        private Activity context;
        private  ArrayList<AppIication> appList;
        /**
         * Instantiates a new image adapter.
         *
         * @param localContext the local context
         */
        public AppsAdapter(Activity localContext) {
            context = localContext;
            appList=getInstalledApps(false);;
            //ArrayList<AppIication> appsList=getInstalledApps(false);
          //  images = getAllShownImagesPath(context);
        }
        public int getCount() {
            return appList.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rootView = inflater.inflate(R.layout.grid_apps_layout, null);
            ImageView appIcon=rootView.findViewById(R.id.imageview_app_icon);
            TextView appName =rootView.findViewById(R.id.textview_app_name);
            if (convertView == null) {
                // picturesView = new ImageView(context);
                appIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                // picturesView
                //  .setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                //rootView = (View) convertView;
            }
            Glide.with(context).load(appList.get(position).getIcon())
                    .placeholder(R.drawable.ic_launcher_foreground).centerCrop()
                    .into(appIcon);
            appName.setText(appList.get(position).getAppname());

            return rootView;
        }
    }

}
