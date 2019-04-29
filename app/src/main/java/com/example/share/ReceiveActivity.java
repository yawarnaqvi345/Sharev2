package com.example.share;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.support.v4.text.PrecomputedTextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.share.wifidirect.WiFiDirectBroadcastReceiver;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;

import java.util.Collection;
import java.util.List;

public class ReceiveActivity extends AppCompatActivity {
    WifiP2pManager manager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver receiver;
    LottieAnimationView animationView;
    ListView receiveListView;
    Collection<WifiP2pDevice> list;
    TextView textView;
    //LinearLayout listRecycler= findViewById()

    IntentFilter intentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        textView=findViewById(R.id.receive_activity_text);
        animationView=findViewById(R.id.receive_animation_view);

      /*  WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.getWifiState()==WifiManager.WIFI_STATE_DISABLED)
        wifiManager.setWifiEnabled(true);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = manager.initialize(this, getMainLooper(), null);
        receiver = new WiFiDirectBroadcastReceiver(manager, mChannel, this);
        receiveListView=findViewById(R.id.receive_activity_listrview);
       // ArrayAdapter<WifiP2pDevice> adptr=new ArrayAdapter(this,R.layout.receive_listview, (List) list);
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
            }
            @Override
            public void onFailure(int reasonCode) {
            }
        });*/

        Nearby.getConnectionsClient(this)
                .startAdvertising(
                        /* endpointName= */ android.os.Build.MODEL,
                        /* serviceId= */ getPackageName(),
                        mConnectionLifecycleCallback,
                        new AdvertisingOptions(Strategy.P2P_CLUSTER));

    }
    @Override
    protected void onResume() {
        super.onResume();
//        registerReceiver(receiver, intentFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();
       // unregisterReceiver(receiver);
    }
    ConnectionLifecycleCallback mConnectionLifecycleCallback=new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(final String s, ConnectionInfo connectionInfo) {
            Toast.makeText(getApplicationContext(),"onConnectionInitiated",Toast.LENGTH_SHORT).show();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReceiveActivity.this);
            alertDialogBuilder.setMessage(s+" wants to connect with this device");
                    alertDialogBuilder.setPositiveButton("Allow",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Nearby.getConnectionsClient(getApplicationContext()).acceptConnection(s, mPayLoadCallback);
                                    Toast.makeText(ReceiveActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                                }
                            });

            alertDialogBuilder.setNegativeButton("Decline",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Nearby.getConnectionsClient(getApplicationContext()).rejectConnection(s);
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            Nearby.getConnectionsClient(ReceiveActivity.this)
                    .stopAdvertising();
          //  Nearby.getConnectionsClient(getApplicationContext()).acceptConnection(s, mPayLoadCallback);
        }

        @Override
        public void onConnectionResult(String s, ConnectionResolution connectionResolution) {
            Toast.makeText(getApplicationContext(),"onConnectionResult",Toast.LENGTH_SHORT).show();
            switch (connectionResolution.getStatus().getStatusCode()) {
                case ConnectionsStatusCodes.STATUS_OK:
                    Toast.makeText(getApplicationContext(), "Connection Accepted", Toast.LENGTH_SHORT).show();
                    textView.setText("Connected to  "+s);
                    textView.setVisibility(View.VISIBLE);
                    animationView.setVisibility(View.INVISIBLE);
                    break;
                case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                    // The connection was rejected by one or both sides.
                    textView.setText("Could not connect to  "+s);
                    textView.setVisibility(View.VISIBLE);
                    animationView.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Rejected Connection", Toast.LENGTH_SHORT).show();
                    break;
                case ConnectionsStatusCodes.STATUS_ERROR:
                    // The connection broke before it was able to be accepted.
                    textView.setText("there was an error in connection to   "+s);
                    textView.setVisibility(View.VISIBLE);
                    animationView.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Rejected Connection", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    // Unknown status code
            }
        }

        @Override
        public void onDisconnected(String s) {
            int a=4;
        }
    };
    PayloadCallback mPayLoadCallback= new PayloadCallback() {
        @Override
        public void onPayloadReceived(String s, Payload payload) {
            Toast.makeText(getApplicationContext(),"Payload Received",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPayloadTransferUpdate(String s, PayloadTransferUpdate payloadTransferUpdate) {
            Toast.makeText(getApplicationContext(),"Payload Updated",Toast.LENGTH_SHORT).show();
        }
    };

}
