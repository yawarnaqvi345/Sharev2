package com.example.share;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
        view=findViewById(R.id.mainlayout);

        EndpointDiscoveryCallback mEndpointDiscoveryCallback=new EndpointDiscoveryCallback() {
            @Override
            public void onEndpointFound(String s, DiscoveredEndpointInfo discoveredEndpointInfo) {
                int a=88;
                Toast.makeText(getApplicationContext(),"onEndpointFound",Toast.LENGTH_SHORT).show();
             //   Snackbar.make(view,"onEndpointFound",Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onEndpointLost(String s) {
int b=54;
                Toast.makeText(getApplicationContext(),"onEndpointLost",Toast.LENGTH_SHORT).show();
                Snackbar.make(view,"onEndpointLost",Snackbar.LENGTH_SHORT).show();

            }
        };


        Nearby.getConnectionsClient(this)
                .startDiscovery(
                        /* serviceId= */ "com.example.package_name",
                        mEndpointDiscoveryCallback,
                        new DiscoveryOptions(Strategy.P2P_CLUSTER));
    }
}
