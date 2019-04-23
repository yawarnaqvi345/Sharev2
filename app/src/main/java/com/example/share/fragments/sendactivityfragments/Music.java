package com.example.share.fragments.sendactivityfragments;


import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.share.FileToSendPath;
import com.example.share.R;
import com.example.share.SendActivity;
import com.example.share.models.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Music extends Fragment {
    List<Track> tracks;
    RecyclerView recyclerView;
    public Music() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_music, container, false);
        recyclerView =  rootView.findViewById(R.id.music_recyclerview);
        tracks = getAllMusicTracks();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter(tracks));
        // Inflate the layout for this fragment
        return rootView;
    }

    List<Track> getAllMusicTracks(){
       // List<Track> mTracks= ArrayList<Track>();
        ArrayList<Track> mTracks = new ArrayList<Track>();
       Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
       Cursor cursor = getContext().getContentResolver().query(
               uri, // Uri
               null,
               null,
               null,
               null);
       if (cursor == null) {

           Toast.makeText(getContext(),"Something Went Wrong.", Toast.LENGTH_LONG);

       } else if (!cursor.moveToFirst()) {

           Toast.makeText(getContext(),"No Music Found on SD Card.", Toast.LENGTH_LONG);

       }
       else {

           int Title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
           int size = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
           int length = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

           //Getting Song ID From Cursor.
           //int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

           do {

               // You can also get the Song ID using cursor.getLong(id).
               //long SongID = cursor.getLong(id);

               String SongTitle = cursor.getString(Title);
               String songSize=cursor.getString(size);
               String songLength=cursor.getString(length);
               songLength=setCorrectDuration(songLength);
               float sz=cursor.getInt(length);
               Track track=new Track();
               track.setTitle(SongTitle);
               track.setSize(getFileSize(songSize));
               track.setLength(songLength);
               mTracks.add(track);
           } while (cursor.moveToNext());
       }
        return mTracks;
   }

   public class RecyclerViewAdapter extends  RecyclerView.Adapter<MyViewHolder>{
        List<Track> mTracks;
      public RecyclerViewAdapter(List<Track> trks){
          mTracks=trks;
      }

       @NonNull
       @Override
       public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
           LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
           View listItem= layoutInflater.inflate(R.layout.recycler_music_list, viewGroup, false);
          // RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(listItem);
          MyViewHolder viewHolder=new MyViewHolder(listItem);
           return viewHolder;
       }

       @Override
       public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
          final int pos=i;
           final String text = mTracks.get(i).getTitle();
           final String size = mTracks.get(i).getSize();
           final String length = mTracks.get(i).getLength();
           myViewHolder.title.setText(text);
           myViewHolder.size.setText(size);
           myViewHolder.length.setText(length);
           myViewHolder.musicCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   FileToSendPath path=new FileToSendPath();
                   path.setPath(mTracks.get(pos).getTitle());
                   path.setType("Music");
                   if(isChecked) {
                       SendActivity.mPathsList.add(path);
                   }
                   else{
                       SendActivity.mPathsList.remove(SendActivity.getRefference(path));
                   }
                   SendActivity.UpdateView();
               }
           });
       }



       @Override
       public int getItemCount() {
           return mTracks.size();
       }
   }

   public class MyViewHolder extends  RecyclerView.ViewHolder{
      public TextView title;
      public  TextView size;
      public TextView length;
      public CheckBox musicCheckbox;
       public MyViewHolder(@NonNull View itemView) {
           super(itemView);
           title=itemView.findViewById(R.id.track_title);
           size=itemView.findViewById(R.id.track_size);
           length=itemView.findViewById(R.id.track_length);
           musicCheckbox=itemView.findViewById(R.id.music_checkbox);
       }
   }
    private String setCorrectDuration(String songs_duration) {
        // TODO Auto-generated method stub

        if(Integer.valueOf(songs_duration) != null) {
            int time = Integer.valueOf(songs_duration);

            int seconds = time/1000;
            int minutes = seconds/60;
            seconds = seconds % 60;

            if(seconds<10) {
                songs_duration = String.valueOf(minutes) + ":0" + String.valueOf(seconds);
            } else {
                songs_duration = String.valueOf(minutes) + ":" + String.valueOf(seconds);
            }
            return songs_duration;
        }
        return null;
    }

    public String getFileSize(String size) {
        String modifiedFileSize = null;
        double fileSize = 0.0;
        if (size!=null) {
            //fileSize = Double.longBitsToDouble();//in Bytes
           fileSize= Double.parseDouble(size);

            if (fileSize < 1024) {
                modifiedFileSize = String.valueOf(fileSize).concat("B");
            } else if (fileSize > 1024 && fileSize < (1024 * 1024)) {
                modifiedFileSize = String.valueOf(Math.round((fileSize / 1024 * 100.0)) / 100.0).concat("KB");
            } else {
                modifiedFileSize = String.valueOf(Math.round((fileSize / (1024 * 1204) * 100.0)) / 100.0).concat("MB");
            }
        } else {
            modifiedFileSize = "Unknown";
        }

        return modifiedFileSize;
    }

}
