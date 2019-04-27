package com.example.share;

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
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Strategy;

public class FinalShareActivity extends AppCompatActivity {
    ConstraintLayout view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_share);
        view = findViewById(R.id.mainlayout);
        // AdvertisingIdClient advertiserEndpointId;

        EndpointDiscoveryCallback mEndpointDiscoveryCallback = new EndpointDiscoveryCallback() {
            @Override
            public void onEndpointFound(String s, DiscoveredEndpointInfo discoveredEndpointInfo) {
                Toast.makeText(getApplicationContext(), "onEndpointFound", Toast.LENGTH_SHORT).show();
                //   Snackbar.make(view,"onEndpointFound",Snackbar.LENGTH_SHORT).show();
                Nearby.getConnectionsClient(getApplicationContext())
                        .requestConnection(
                                /* endpointName= */ discoveredEndpointInfo.getEndpointName(),
                                discoveredEndpointInfo.getServiceId(),
                                mConnectionLifecycleCallback);
            }

            @Override
            public void onEndpointLost(String s) {
                int b = 54;
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
        }

        @Override
        public void onConnectionResult(String s, ConnectionResolution connectionResolution) {
            Toast.makeText(getApplicationContext(), "onConnectionResult", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDisconnected(String s) {
            Toast.makeText(getApplicationContext(), "onDisconnected", Toast.LENGTH_SHORT).show();
        }
    };

}
