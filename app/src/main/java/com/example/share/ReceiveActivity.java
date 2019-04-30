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
import android.support.v4.util.SimpleArrayMap;
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

import java.io.File;
import java.nio.charset.StandardCharsets;
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
    String connectedDeviceId;
    IntentFilter intentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        textView=findViewById(R.id.receive_activity_text);
        animationView=findViewById(R.id.receive_animation_view);
        Nearby.getConnectionsClient(this)
                .startAdvertising(
                        /* endpointName= */ android.os.Build.MODEL,
                        /* serviceId= */ getPackageName(),
                        mConnectionLifecycleCallback,
                        new AdvertisingOptions(Strategy.P2P_CLUSTER));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(connectedDeviceId!=null)
        Nearby.getConnectionsClient(this).disconnectFromEndpoint(connectedDeviceId);
    }
    ConnectionLifecycleCallback mConnectionLifecycleCallback=new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(final String s, ConnectionInfo connectionInfo) {
            Toast.makeText(getApplicationContext(),"onConnectionInitiated",Toast.LENGTH_SHORT).show();
          /*  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
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
            alertDialog.show();*/
//            Nearby.getConnectionsClient(ReceiveActivity.this)
//                    .stopAdvertising();
            Nearby.getConnectionsClient(getApplicationContext()).acceptConnection(s, mPayLoadCallback);
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
                    connectedDeviceId=s;
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
                    break;
                    // Unknown status code
            }
        }

        @Override
        public void onDisconnected(String s) {
        }
    };
//    PayloadCallback mPayLoadCallback= new PayloadCallback() {
//        @Override
//        public void onPayloadReceived(String s, Payload payload) {
//           int type=payload.getType();
//            Toast.makeText(getApplicationContext(),"Payload Received",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onPayloadTransferUpdate(String s, PayloadTransferUpdate payloadTransferUpdate) {
//            Toast.makeText(getApplicationContext(),"Payload Updated",Toast.LENGTH_SHORT).show();
//            payloadTransferUpdate.getPayloadId();
//        }
//    };
PayloadCallback mPayLoadCallback= new ReceiveFilePayloadCallback();

    public class ReceiveFilePayloadCallback extends PayloadCallback {
        private final SimpleArrayMap<Long, Payload> incomingFilePayloads = new SimpleArrayMap<>();
        private final SimpleArrayMap<Long, Payload> completedFilePayloads = new SimpleArrayMap<>();
        private final SimpleArrayMap<Long, String> filePayloadFilenames = new SimpleArrayMap<>();

        @Override
        public void onPayloadReceived(String endpointId, Payload payload) {
            if (payload.getType() == Payload.Type.BYTES) {
                String payloadFilenameMessage = new String(payload.asBytes(), StandardCharsets.UTF_8);
                long payloadId = addPayloadFilename(payloadFilenameMessage);
                //processFilePayload(payloadId);
            } else if (payload.getType() == Payload.Type.FILE) {
                // Add this to our tracking map, so that we can retrieve the payload later.
                incomingFilePayloads.put(payload.getId(), payload);
            }
        }

        /**
         * Extracts the payloadId and filename from the message and stores it in the
         * filePayloadFilenames map. The format is payloadId:filename.
         */
        private long addPayloadFilename(String payloadFilenameMessage) {
            String[] parts = payloadFilenameMessage.split(":");
            long payloadId = Long.parseLong(parts[0]);
            String filename = parts[1];
            filePayloadFilenames.put(payloadId, filename);
            return payloadId;
        }

        private void processFilePayload(long payloadId) {
            // BYTES and FILE could be received in any order, so we call when either the BYTES or the FILE
            // payload is completely received. The file payload is considered complete only when both have
            // been received.
            Payload filePayload = completedFilePayloads.get(payloadId);
            String filename = filePayloadFilenames.get(payloadId);
            if (filePayload != null && filename != null) {
                completedFilePayloads.remove(payloadId);
                filePayloadFilenames.remove(payloadId);

                // Get the received file (which will be in the Downloads folder)
                File payloadFile = filePayload.asFile().asJavaFile();

                // Rename the file.
                payloadFile.renameTo(new File(payloadFile.getParentFile(), filename));
            }
        }

        @Override
        public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
            if (update.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
                long payloadId = update.getPayloadId();
                Payload payload= incomingFilePayloads.remove(payloadId);
                completedFilePayloads.put(payloadId, payload);
               // if (payload.getType() == Payload.Type.FILE) {
                    processFilePayload(payloadId);
               // }
            }
        }
    }

}
