package com.example.share.fragments.sendactivityfragments;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.text.PrecomputedText;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.share.FileToSendPath;
import com.example.share.R;
import com.example.share.SendActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Photos extends Fragment {
    GridView photosGridView;
   LinearLayout photoLinearLayout;
    private ArrayList<String> images;

    public Photos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        photosGridView = rootView.findViewById(R.id.photos_grid_view);
        photoLinearLayout = rootView.findViewById(R.id.photo_linear_layout);
        photosGridView.setAdapter(new ImageAdapter(getActivity()));
        photosGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
              //  photoLinearLayout.setLayoutParams(Para);
                if (null != images && !images.isEmpty())
                    Toast.makeText(
                            getActivity().getApplicationContext(),
                            "position " + position + " " + images.get(position),
                            Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
    private ArrayList<String> getAllShownImagesPath(Context activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }
    private class ImageAdapter extends BaseAdapter {
        /**
         * The context.
         */
        private Activity context;
        /**
         * Instantiates a new image adapter.
         *
         * @param localContext the local context
         */
        public ImageAdapter(Activity localContext) {
            context = localContext;
            images = getAllShownImagesPath(context);
        }
        public int getCount() {
            return images.size();
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
            View rootView = inflater.inflate(R.layout.grid_photo_layout, null);
            CheckBox photoCheckBox=rootView.findViewById(R.id.photo_checkbox);
            ImageView photoThumb=rootView.findViewById(R.id.photo_thumb);
            photoThumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(
                            getActivity().getApplicationContext(),
                            "position of thumb" + position + " " + images.get(position),
                            Toast.LENGTH_SHORT).show();
                }
            });
            photoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    FileToSendPath path=new FileToSendPath();
                    path.setPath(images.get(position));
                    if(isChecked) {
                        SendActivity.mPathsList.add(path);
                        buttonView.setChecked(true);
                    }
                    else{
                        SendActivity.mPathsList.remove(getRefference(path));
                       // SendActivity.UpdateView();
                    }
                  SendActivity.UpdateView();
                }
            });
            ImageView picturesView;
            picturesView=rootView.findViewById(R.id.photo_thumb);
            if (convertView == null) {
               // picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
               // picturesView
                      //  .setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                //rootView = (View) convertView;
            }
            Glide.with(context).load(images.get(position))
                    .placeholder(R.drawable.ic_launcher_foreground).centerCrop()
                    .into(picturesView);
            return rootView;
        }
    }

   private FileToSendPath getRefference(FileToSendPath mPath){
        for(FileToSendPath path:SendActivity.mPathsList){
            if(path.getPath().equals(mPath.getPath()))
                return path;
        }
        return null;
   }


}
