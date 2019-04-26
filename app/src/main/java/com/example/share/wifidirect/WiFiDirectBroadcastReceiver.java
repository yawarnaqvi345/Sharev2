package com.example.share.wifidirect;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.share.R;
import com.example.share.ReceiveActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private Activity mActivity;
    ListView listView;
    MyListAdapter adapter;
    ArrayList<WifiP2pDevice> mList=null;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       Activity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
        listView=mActivity.findViewById(R.id.receive_activity_listrview);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
            } else {
                // Wi-Fi P2P is not enabled
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            WifiP2pManager.PeerListListener myPeerListListener = new WifiP2pManager.PeerListListener() {
                @Override
                public void onPeersAvailable(WifiP2pDeviceList peers) {
            Collection<WifiP2pDevice> list = peers.getDeviceList();
                    mList = new ArrayList<>(list);
                  //  mList =(ArrayList<WifiP2pDevice>) peers.getDeviceList();
                    adapter=new MyListAdapter(mActivity,R.layout.receive_listview,mList);
                    listView.setAdapter(adapter);
                   TextView txt= mActivity.findViewById(R.id.receive_activity_text);
                    LottieAnimationView animationView=mActivity.findViewById(R.id.receive_animation_view);
                 //  txt.setText(peers.toString());
                   txt.setVisibility(View.VISIBLE);
                   animationView.setVisibility(View.GONE);
                }
            };
            if (mManager != null) {
                mManager.requestPeers(mChannel, myPeerListListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }


    }
    class MyListAdapter extends ArrayAdapter<WifiP2pDevice>{
        Context cxt;
        ArrayList<WifiP2pDevice> list;
        public MyListAdapter(Context context, int resource,ArrayList<WifiP2pDevice> mList) {
            super(context, resource);
            cxt=context;
            list=mList;
        }

        @Override
        public int getCount() {
            return list.size();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) cxt
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rootView = inflater.inflate(R.layout.receive_listview, null);
            TextView txt=rootView.findViewById(R.id.rec_devicename);
            txt.setText(list.get(position).deviceName);
            return rootView;
        }
    }
}
