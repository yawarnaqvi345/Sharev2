package com.example.share.SendActivityFragments;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.share.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Music extends Fragment {


    public Music() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_music, container, false);
        // Inflate the layout for this fragment
        return rootView;
    }


//    private ArrayList<String> getAllMusicPath(Context activity) {
//        Uri uri;
//        Cursor cursor;
//        int column_index_data, column_index_folder_name;
//        ArrayList<String> listOfAllMusic = new ArrayList<String>();
//        String absolutePathOfMusic = null;
//        uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String[] projection = {MediaStore.MediaColumns.DATA,
//                MediaStore.Audio.Media.BUCKET_DISPLAY_NAME};
//        cursor = activity.getContentResolver().query(uri, projection, null,
//                null, null);
//        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//        column_index_folder_name = cursor
//                .getColumnIndexOrThrow(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME);
//        while (cursor.moveToNext()) {
//            absolutePathOfMusic = cursor.getString(column_index_data);
//            listOfAllMusic.add(absolutePathOfMusic);
//        }
//        return listOfAllMusic;
//    }

}
