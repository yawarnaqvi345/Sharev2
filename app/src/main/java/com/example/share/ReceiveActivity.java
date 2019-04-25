package com.example.share;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.support.v4.text.PrecomputedTextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.share.wifidirect.WiFiDirectBroadcastReceiver;

public class ReceiveActivity extends AppCompatActivity {
    WifiP2pManager manager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver receiver;
    LottieAnimationView animationView;
    ListView receiveListView;

    IntentFilter intentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = manager.initialize(this, getMainLooper(), null);
        receiver = new WiFiDirectBroadcastReceiver(manager, mChannel, this);
        receiveListView=findViewById(R.id.receive_activity_listrview);
       // ArrayAdapter<String> adptr=new ArrayAdapter(this,R.id.rec_devicename,);
       // receiveListView.setAdapter();
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        animationView=findViewById(R.id.receive_animation_view);


        manager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                int a=3;
            }

            @Override
            public void onFailure(int reasonCode) {
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }


}
