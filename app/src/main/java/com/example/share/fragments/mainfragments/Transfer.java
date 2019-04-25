package com.example.share.fragments.mainfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.share.R;
import com.example.share.ReceiveActivity;
import com.example.share.SendActivity;
import com.tml.sharethem.receiver.ReceiverActivity;


public class Transfer extends Fragment {
    final String TAG = "TransferFragment";
    Button buttonSend;
    Button buttonRecieve;

    public Transfer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Oncreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "OncreateView");
        View rootView = inflater.inflate(R.layout.fragment_transfer, container, false);
        buttonSend = rootView.findViewById(R.id.button_send);
        buttonRecieve = rootView.findViewById(R.id.button_recieve);
        buttonRecieve.setOnClickListener(buttonSendListener);
        buttonSend.setOnClickListener(buttonSendListener);
        // Inflate the layout for this fragment
        return rootView;
    }

    View.OnClickListener buttonSendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "buttonSendListener");
            switch (v.getId()) {
                case R.id.button_send:
                    Intent sendIntent = new Intent(getContext(), SendActivity.class);
                    startActivity(sendIntent);
                    Toast.makeText(getContext(), "Send pressed!!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button_recieve:
                    Intent recIntent = new Intent(getContext(), ReceiveActivity.class);
                    startActivity(recIntent);
                    Toast.makeText(getContext(), "Recieve Pressed!!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
