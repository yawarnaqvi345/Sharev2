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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.share.FileToSendPath;
import com.example.share.R;
import com.example.share.SendActivity;

import java.io.File;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Files extends Fragment {
    File[] files;
    String path=Environment.getExternalStorageDirectory().toString();


    public Files() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_files, container, false);

        files = getFilesInDirectory(path);
        RecyclerView fileRecycler = rootView.findViewById(R.id.files_recycler_view);
        fileRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        fileRecycler.setAdapter(new FileRecyclerViewAdapter(files));

        // Inflate the layout for this fragment
        return rootView;
    }

    File[] getFilesInDirectory(String pat) {
        String path = pat;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
//        Log.d("Files", "Size: "+ files.length);
//        for (int i = 0; i < files.length; i++)
//        {
//            Log.d("Files", "FileName:" + files[i].getName());
//        }
        return files;
    }

    public class FileRecyclerViewAdapter extends RecyclerView.Adapter<MyFileViewHolder> {
        File[] mFilesList;

        public FileRecyclerViewAdapter(File[] files) {
            mFilesList = files;
          //  List<File> mList= Arrays.asList(files);
           // Collections.sort(mList);

        }

        @NonNull
        @Override
        public MyFileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View recyclerFile = layoutInflater.inflate(R.layout.recycler_file_list, viewGroup, false);
            // RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(listItem);
            MyFileViewHolder viewHolder = new MyFileViewHolder(recyclerFile);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final MyFileViewHolder myFileViewHolder, final int i) {
            myFileViewHolder.fileName.setText(mFilesList[i].getName());
            Date lastModDate = new Date(mFilesList[i].lastModified());

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yy");
            String formattedDate= dateFormat.format(lastModDate);
            myFileViewHolder.lastModified.setText(formattedDate );
            if(!mFilesList[i].isDirectory()){
                myFileViewHolder.folderIcon.setVisibility(View.GONE);
            myFileViewHolder.fileCheckbox.setVisibility(View.VISIBLE);
                myFileViewHolder.fileCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        FileToSendPath path=new FileToSendPath();
                        path.setPath(mFilesList[i].getPath());
                        path.setType("File");
                        if(isChecked) {
                            SendActivity.mPathsList.add(path);
                            buttonView.setChecked(true);
                        }
                        else{
                            SendActivity.mPathsList.remove(SendActivity.getRefference(path));
                        }
                        SendActivity.UpdateView();
                    }
                });
            myFileViewHolder.enterFolderButtton.setVisibility(View.GONE);}
            if(mFilesList[i].isDirectory()){
                myFileViewHolder.enterFolderButtton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path=mFilesList[i].getPath();
                        mFilesList=getFilesInDirectory(path);
                        notifyItemRangeChanged(0,mFilesList.length);
                        notifyDataSetChanged();
                       Toast.makeText(getContext(), "Nexust", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mFilesList.length;
        }
    }

    public class MyFileViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        TextView lastModified;
        ImageView folderIcon;
        ImageView enterFolderButtton;
        CheckBox fileCheckbox;

        public MyFileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.textview_file_name);
            lastModified = itemView.findViewById(R.id.textview_file_last_modified);
            folderIcon = itemView.findViewById(R.id.file_folder_icon);
            enterFolderButtton = itemView.findViewById(R.id.enter_folder_button);
            fileCheckbox = itemView.findViewById(R.id.file_checkbox);
        }
    }

}
