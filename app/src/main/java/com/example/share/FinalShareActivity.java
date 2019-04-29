package com.example.share;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FinalShareActivity extends AppCompatActivity {
    ConstraintLayout view;
    Activity mActivity=this;
    TextView finalshareTextView;
    File fileToSend;
     List<FileToSendPath> mPathsList;
     Payload filePayload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_share);
        mPathsList=SendActivity.mPathsList;
        Uri uri= Uri.fromFile(new File(mPathsList.get(0).path));
        try {
            ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "r");
            filePayload = Payload.fromFile(pfd);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        finalshareTextView=findViewById(R.id.finalshare_activity_text);
     //   view = findViewById(R.id.mainlayout);
        // AdvertisingIdClient advertiserEndpointId;
        EndpointDiscoveryCallback mEndpointDiscoveryCallback = new EndpointDiscoveryCallback() {
            @Override
            public void onEndpointFound(String s, DiscoveredEndpointInfo discoveredEndpointInfo) {

                //   Snackbar.make(view,"onEndpointFound",Snackbar.LENGTH_SHORT).show();
                Nearby.getConnectionsClient(mActivity).requestConnection(discoveredEndpointInfo.getEndpointName()
                        ,discoveredEndpointInfo.getServiceId(),mConnectionLifecycleCallback);
                Nearby.getConnectionsClient(getApplicationContext())
                        .requestConnection(
                                /* endpointName= */ discoveredEndpointInfo.getEndpointName(),
                                s,
                                mConnectionLifecycleCallback).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "on Success connection", Toast.LENGTH_SHORT).show();
                        Nearby.getConnectionsClient(mActivity)
                                .stopDiscovery();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "on faliure connection", Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(getApplicationContext(), "onEndpointFound", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEndpointLost(String s) {
//                int b = 54;
                Toast.makeText(getApplicationContext(), "onEndpointLost", Toast.LENGTH_SHORT).show();
                Snackbar.make(view, "onEndpointLost", Snackbar.LENGTH_SHORT).show();

            }
        };


        Nearby.getConnectionsClient(this)
                .startDiscovery(
                        /* serviceId= */ getPackageName(),
                        mEndpointDiscoveryCallback,
                        new DiscoveryOptions(Strategy.P2P_CLUSTER));
    }

   private final ConnectionLifecycleCallback mConnectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(String s, ConnectionInfo connectionInfo) {
            Toast.makeText(getApplicationContext(), "onConnectionInitiated", Toast.LENGTH_SHORT).show();

            Nearby.getConnectionsClient(getApplicationContext()).acceptConnection(s, mPayLoadCallback);
        }

        @Override
        public void onConnectionResult(String s, ConnectionResolution connectionResolution) {
            Toast.makeText(getApplicationContext(), "onConnectionResult", Toast.LENGTH_SHORT).show();
            switch (connectionResolution.getStatus().getStatusCode()) {
                case ConnectionsStatusCodes.STATUS_OK:
                   finalshareTextView.setText("connected to "+s);

                    Nearby.getConnectionsClient(getApplicationContext()).sendPayload(s, filePayload);
                    Toast.makeText(getApplicationContext(), "Connection Accepted", Toast.LENGTH_SHORT).show();
                    break;
                case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                    finalshareTextView.setText("connection to "+s+" rejected");
                    // The connection was rejected by one or both sides.
                    Toast.makeText(getApplicationContext(), "Rejected Connection", Toast.LENGTH_SHORT).show();
                    break;
                case ConnectionsStatusCodes.STATUS_ERROR:
                    finalshareTextView.setText("There was an error connecting to "+s);
                    // The connection broke before it was able to be accepted.
                    Toast.makeText(getApplicationContext(), "Rejected Connection", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    // Unknown status code
            }
        }

        @Override
        public void onDisconnected(String s) {
            Toast.makeText(getApplicationContext(), "onDisconnected", Toast.LENGTH_SHORT).show();
        }
    };
    PayloadCallback mPayLoadCallback=new PayloadCallback() {
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
