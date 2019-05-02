package com.example.share.fragments.sendactivityfragments;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.share.FileToSendPath;
import com.example.share.R;
import com.example.share.SendActivity;
import com.example.share.models.AppIication;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Videos extends Fragment {
    private ArrayList<String> videos;
    GridView videoGridView;
    View rootViewMain;
    RelativeLayout progress;


    public Videos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_videos, container, false);
       // rootViewMain = rootView;
        progress=rootView.findViewById(R.id.video_loadingPanel);
        videoGridView = rootView.findViewById(R.id.videos_grid_view);
        new AsyncTaskRunner().execute();



        // Inflate the layout for this fragment
        return rootView;
    }


    private class VideoAdapter extends BaseAdapter {
        private ArrayList<String> videos;
        /**
         * The context.
         */
        private Activity context;


        /**
         * Instantiates a new image adapter.
         *
         * @param localContext the local context
         */
        public VideoAdapter(Activity localContext, ArrayList<String> vids) {
            context = localContext;
            videos=vids;

        }

        public int getCount() {
            return videos.size();
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
            View rootView = inflater.inflate(R.layout.grid_video_layout, null);

            CheckBox videoCheckBox=rootView.findViewById(R.id.video_checkbox);
            videoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    FileToSendPath path=new FileToSendPath();
                    path.setPath(videos.get(position));
                    path.setType("Video");
                    if(isChecked) {
                        SendActivity.mPathsList.add(path);
                        buttonView.setChecked(true);
                    }
                    else{
                        SendActivity.mPathsList.remove(SendActivity.getRefference(path));
                        // SendActivity.UpdateView();
                    }
                    SendActivity.UpdateView();
                }
            });


            ImageView picturesView;
            picturesView = rootView.findViewById(R.id.vid_thumb);
           // Button vidButton=rootView.findViewById(R.id.vid_layout_button);
            //vidButton.setClickable(true);
            if (convertView == null) {
                // picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                //  picturesView
                //         .setLayoutParams(new GridView.LayoutParams(270, 270));
            } else {
              //  rootView = (View) convertView;
            }
            Glide.with(context).load(videos.get(position))
                    .placeholder(R.drawable.ic_launcher_foreground).centerCrop()
                    .into(picturesView);
            // return picturesView ;
            return rootView;
        }
    }

    private ArrayList<String> getAllShownVideoPath(Context activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllVideos = new ArrayList<String>();
        String absolutePathOfVideo = null;
        uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfVideo = cursor.getString(column_index_data);

            listOfAllVideos.add(absolutePathOfVideo);
        }
        return listOfAllVideos;
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            videos = getAllShownVideoPath(getActivity());

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            videoGridView.setAdapter(new VideoAdapter(getActivity(),videos));
            progress.setVisibility(View.GONE );
            videoGridView.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPreExecute() {
            videoGridView.setVisibility(View.GONE);
        }



    }

}
