package com.example.share.SendActivityFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.share.R;

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

}
