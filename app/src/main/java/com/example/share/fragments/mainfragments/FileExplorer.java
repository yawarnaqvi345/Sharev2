package com.example.share.fragments.mainfragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.share.FileToSendPath;
import com.example.share.R;
import com.example.share.SendActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileExplorer extends Fragment {
    private String m_root= Environment.getExternalStorageDirectory().getPath();
    List<String> m_item;
    List<String> m_path;
    List<String> m_files;
    List<String> m_filesPath;
    String m_curDir;
    ListAdapter m_listAdapter;
    ListView m_RootList;
    public FileExplorer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_file_explorer, container, false);
        m_RootList=rootView.findViewById(R.id.rl_lvListRoot);
        getDirFromRoot(m_root);
        return rootView;
    }
    //get directories and files from selected path
    public void getDirFromRoot(String p_rootPath)
    {
        m_item = new ArrayList<String>();
        Boolean m_isRoot=true;
        m_path = new ArrayList<String>();
        m_files=new ArrayList<String>();
        m_filesPath=new ArrayList<String>();
        File m_file = new File(p_rootPath);
        File[] m_filesArray = m_file.listFiles();
        if(!p_rootPath.equals(m_root))
        {
            m_item.add("../");
            m_path.add(m_file.getParent());
            m_isRoot=false;
        }
        m_curDir=p_rootPath;
        //sorting file list in alphabetical order
        Arrays.sort(m_filesArray);
        for(int i=0; i < m_filesArray.length; i++)
        {
            File file = m_filesArray[i];
            if(file.isDirectory())
            {
                m_item.add(file.getName());
                m_path.add(file.getPath());
            }
            else
            {
                m_files.add(file.getName());
                m_filesPath.add(file.getPath());
            }
        }
        for(String m_AddFile:m_files)
        {
            m_item.add(m_AddFile);
        }
        for(String m_AddPath:m_filesPath)
        {
            m_path.add(m_AddPath);
        }
        m_listAdapter=new ListAdapter(getContext(),m_item,m_path,m_isRoot);
        m_RootList.setAdapter(m_listAdapter);
        m_RootList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                File m_isFile=new File(m_path.get(position));
                if(m_isFile.isDirectory())
                {
                    getDirFromRoot(m_isFile.toString());
                }
                else
                {
                    Toast.makeText(getContext(), "This is File", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public class ListAdapter extends BaseAdapter {
        private List<String> m_item;
        private List<String> m_path;
        public ArrayList<Integer> m_selectedItem;
        Context m_context;
        Boolean m_isRoot;
        public ListAdapter(Context p_context,List<String> p_item, List<String> p_path,Boolean p_isRoot) {
            m_context=p_context;
            m_item=p_item;
            m_path=p_path;
            m_selectedItem=new ArrayList<Integer>();
            m_isRoot=p_isRoot;
        }

        @Override
        public int getCount() {
            return m_item.size();
        }

        @Override
        public Object getItem(int position) {
            return m_item.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int p_position, View p_convertView, ViewGroup p_parent)
        {
            View m_view = null;
            ViewHolder m_viewHolder = null;
            if (p_convertView == null)
            {
                LayoutInflater m_inflater = LayoutInflater.from(m_context);
                m_view = m_inflater.inflate(R.layout.file_explorer_list_view, null);
                m_viewHolder = new ViewHolder();
                m_viewHolder.m_tvFileName = (TextView) m_view.findViewById(R.id.lr_tvFileName);
                m_viewHolder.m_tvDate = (TextView) m_view.findViewById(R.id.lr_tvdate);
                m_viewHolder.m_ivIcon = (ImageView) m_view.findViewById(R.id.lr_ivFileIcon);
                m_viewHolder.m_cbCheck = (CheckBox) m_view.findViewById(R.id.lr_cbCheck);
                m_view.setTag(m_viewHolder);
            }
            else
            {
                m_view = p_convertView;
                m_viewHolder = ((ViewHolder) m_view.getTag());
            }
            if(!m_isRoot && p_position == 0)
            {
                m_viewHolder.m_cbCheck.setVisibility(View.INVISIBLE);
            }
            m_viewHolder.m_cbCheck.setVisibility(View.INVISIBLE);
            m_viewHolder.m_tvFileName.setText(m_item.get(p_position));
            m_viewHolder.m_ivIcon.setImageResource(setFileImageType(new File(m_path.get(p_position))));
            m_viewHolder.m_tvDate.setText(getLastDate(p_position));
            m_viewHolder.m_cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        m_selectedItem.add(p_position);
                    }
                    else
                    {
                        m_selectedItem.remove(m_selectedItem.indexOf(p_position));
                    }

                }
            });
            return m_view;
        }

        class ViewHolder
        {
            CheckBox m_cbCheck;
            ImageView m_ivIcon;
            TextView m_tvFileName;
            TextView m_tvDate;
        }

        private int setFileImageType(File m_file)
        {
            int m_lastIndex=m_file.getAbsolutePath().lastIndexOf(".");
            String m_filepath=m_file.getAbsolutePath();
            if (m_file.isDirectory())
                return R.drawable.ic_folder_yellow_24dp;
            else
            {
                if(m_filepath.substring(m_lastIndex).equalsIgnoreCase(".png"))
                {
                    return R.drawable.ic_photo_blue_24dp;
                }
                else if(m_filepath.substring(m_lastIndex).equalsIgnoreCase(".jpg"))
                {
                    return R.drawable.ic_photo_blue_24dp;
                }
                else
                {
                    return R.drawable.ic_launcher_foreground;
                }
            }
        }

        String getLastDate(int p_pos)
        {
            File m_file=new File(m_path.get(p_pos));
            SimpleDateFormat m_dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return m_dateFormat.format(m_file.lastModified());
        }
    }

}
