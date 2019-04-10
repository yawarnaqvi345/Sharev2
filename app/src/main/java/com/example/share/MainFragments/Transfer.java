package com.example.share.MainFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.share.R;



public class Transfer extends Fragment {
    Button buttonSend;
    Button buttonRecieve;
    public Transfer() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_transfer, container, false);
        buttonSend=rootView.findViewById(R.id.button_send);
        buttonRecieve=rootView.findViewById(R.id.button_recieve);
        buttonRecieve.setOnClickListener(buttonSendListener);
        buttonSend.setOnClickListener(buttonSendListener);
        // Inflate the layout for this fragment
        return rootView;
    }
    View.OnClickListener buttonSendListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_send:
                    Toast.makeText(getContext(), "Send pressed!!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button_recieve:
                    Toast.makeText(getContext(), "Recieve Pressed!!", Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    };
}
