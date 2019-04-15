package com.example.share.fragments.sendactivityfragments;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.share.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Files extends Fragment {
    File[] files;


    public Files() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_files, container, false);

        files=getFilesInDirectory("dummy");
        RecyclerView fileRecycler=rootView.findViewById(R.id.files_recycler_view);
        fileRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        fileRecycler.setAdapter(new FileRecyclerViewAdapter(files));

        // Inflate the layout for this fragment
        return rootView;
    }

    File[] getFilesInDirectory(String pat){
        String path = Environment.getExternalStorageDirectory().toString();
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
//        Log.d("Files", "Size: "+ files.length);
//        for (int i = 0; i < files.length; i++)
//        {
//            Log.d("Files", "FileName:" + files[i].getName());
//        }
        return  files;
    }

    public class FileRecyclerViewAdapter extends RecyclerView.Adapter<MyFileViewHolder>{
        File[] mFilesList;

        public FileRecyclerViewAdapter(File[] files) {
            mFilesList = files;
        }

        @NonNull
        @Override
        public MyFileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View recyclerFile= layoutInflater.inflate(R.layout.recycler_file_list, viewGroup, false);
            // RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(listItem);
            MyFileViewHolder viewHolder=new MyFileViewHolder(recyclerFile);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyFileViewHolder myFileViewHolder, int i) {
            myFileViewHolder.fileName.setText(mFilesList[i].getName());
        }

        @Override
        public int getItemCount() {
            return mFilesList.length;
        }
    }
    public class MyFileViewHolder extends  RecyclerView.ViewHolder{
        TextView fileName;
        public MyFileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName=itemView.findViewById(R.id.textview_file_name);
        }
    }

}
