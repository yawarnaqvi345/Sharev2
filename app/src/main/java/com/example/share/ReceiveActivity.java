package com.example.share;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.support.v4.text.PrecomputedTextCompat;
import android.support.v4.util.SimpleArrayMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    List<FileToSendPath> mPathsList;
    RecyclerView myRecyclerView;
    MyAdapter myAdapter;
    LinearLayout elseLinear;
    private SimpleArrayMap<String, String> deviceName = new SimpleArrayMap<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        mPathsList = new ArrayList<>();
      //  FileToSendPath d=new FileToSendPath();
       // d.setName(" ");
       // mPathsList.add(d);
        textView=findViewById(R.id.receive_activity_text);
        animationView=findViewById(R.id.receive_animation_view);
        elseLinear=findViewById(R.id.receive_linear_else);
        myRecyclerView=findViewById(R.id.receive_recycler);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

//        myAdapter=new MyAdapter(mPathsList);
//        myRecyclerView.setAdapter(myAdapter);


        Nearby.getConnectionsClient(ReceiveActivity.this)
                .startAdvertising(
                        /* endpointName= */ android.os.Build.MODEL,
                        /* serviceId= */ getPackageName(),
                        mConnectionLifecycleCallback,
                        new AdvertisingOptions(Strategy.P2P_POINT_TO_POINT));


    }

    @Override
    protected void onDestroy() {
        Nearby.getConnectionsClient(ReceiveActivity.this).stopAdvertising();
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
        Nearby.getConnectionsClient(ReceiveActivity.this).disconnectFromEndpoint(connectedDeviceId);
        Nearby.getConnectionsClient(ReceiveActivity.this).stopAdvertising();
    }
    ConnectionLifecycleCallback mConnectionLifecycleCallback=new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(final String s, ConnectionInfo connectionInfo) {
            deviceName.put(s,connectionInfo.getEndpointName());
            Toast.makeText(getApplicationContext(),"onConnectionInitiated",Toast.LENGTH_SHORT).show();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReceiveActivity.this);
            alertDialogBuilder.setMessage(s+" wants to connect with this device");
                    alertDialogBuilder.setPositiveButton("Allow",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Nearby.getConnectionsClient(ReceiveActivity.this).acceptConnection(s, mPayLoadCallback);
                                    //Toast.makeText(ReceiveActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                                }
                            });

            alertDialogBuilder.setNegativeButton("Decline",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Nearby.getConnectionsClient(ReceiveActivity.this).rejectConnection(s);
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
//            Nearby.getConnectionsClient(ReceiveActivity.this)
//                    .stopAdvertising();
          //  Nearby.getConnectionsClient(getApplicationContext()).acceptConnection(s, mPayLoadCallback);
        }

        @Override
        public void onConnectionResult(String s, ConnectionResolution connectionResolution) {
            Toast.makeText(getApplicationContext(),"onConnectionResult",Toast.LENGTH_SHORT).show();
            switch (connectionResolution.getStatus().getStatusCode()) {
                case ConnectionsStatusCodes.STATUS_OK:
                    Toast.makeText(getApplicationContext(), "Connection Accepted", Toast.LENGTH_SHORT).show();
                    textView.setText("Connected to  " +  deviceName.get(s));
                    textView.setVisibility(View.VISIBLE);
                    animationView.setVisibility(View.GONE);
                    myRecyclerView.setVisibility(View.VISIBLE);
                    elseLinear.setVisibility(View.GONE);
                    connectedDeviceId=s;
                    Nearby.getConnectionsClient(ReceiveActivity.this).stopAdvertising();
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
            Nearby.getConnectionsClient(ReceiveActivity.this)
                    .startAdvertising(
                            /* endpointName= */ android.os.Build.MODEL,
                            /* serviceId= */ getPackageName(),
                            mConnectionLifecycleCallback,
                            new AdvertisingOptions(Strategy.P2P_POINT_TO_POINT));
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
        private final SimpleArrayMap<Long, Integer> fileIdPosition = new SimpleArrayMap<>();
        private final SimpleArrayMap<Long, Integer> fileIdType = new SimpleArrayMap<>();
        private final SimpleArrayMap<Long, String> recfileIdType = new SimpleArrayMap<>();
        int index=0;


        @Override
        public void onPayloadReceived(String endpointId, Payload payload) {
          //  incomingFilePayloads.put(payload.getId(), payload);
            if (payload.getType() == Payload.Type.BYTES) {
                String payloadFilenameMessage = new String(payload.asBytes(), StandardCharsets.UTF_8);
                long payloadId = addPayloadFilename(payloadFilenameMessage);
                processFilePayload(payloadId);
            } else if (payload.getType() == Payload.Type.FILE) {


                // Add this to our tracking map, so that we can retrieve the payload later.
                incomingFilePayloads.put(payload.getId(), payload);
                FileToSendPath file=new FileToSendPath();
                file.setName(filePayloadFilenames.get(payload.getId()));
                mPathsList.add(file);
                fileIdPosition.put(payload.getId(),index);
                if(index==0){
                    myAdapter=new MyAdapter(mPathsList);
                    myRecyclerView.setAdapter(myAdapter);
                }
                index++;
                myAdapter.notifyDataSetChanged();
            }
            fileIdType.put(payload.getId(),payload.getType());

        }

        /**
         * Extracts the payloadId and filename from the message and stores it in the
         * filePayloadFilenames map. The format is payloadId:filename.
         */
        private long addPayloadFilename(String payloadFilenameMessage) {
            String[] parts = payloadFilenameMessage.split(":");
            long payloadId = Long.parseLong(parts[0]);

            String filename = parts[1];
            String type=parts[2];
            recfileIdType.put(payloadId,type);
            filePayloadFilenames.put(payloadId, filename);
            recfileIdType.put(payloadId,type);
           // mPathsList.get(fileIdPosition.get(payloadId)).setType(type);
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
                mPathsList.get( fileIdPosition.get(payloadId)).setPath((new File(payloadFile.getParentFile(), filename).getPath()));
                mPathsList.get(fileIdPosition.get(payloadId)).setType(recfileIdType.get(payloadId));
            }
        }

        @Override
        public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
            if (update.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
                long payloadId = update.getPayloadId();
                Payload payload= incomingFilePayloads.remove(payloadId);
                completedFilePayloads.put(payloadId, payload);
//                if (payload.getType() == Payload.Type.FILE) {
                    processFilePayload(payloadId);
  //              }
            }
           else if ((update.getStatus() == PayloadTransferUpdate.Status.IN_PROGRESS) &&  (fileIdType.get(update.getPayloadId())==Payload.Type.FILE) ) {
                float btrans=update.getBytesTransferred();
                float btotal=update.getTotalBytes();
                float factor=btrans/btotal;
                float progpercentage=factor*100;
                mPathsList.get( fileIdPosition.get(update.getPayloadId())).setProgress((int)progpercentage);
                myAdapter.notifyItemChanged(fileIdPosition.get(update.getPayloadId()));
            }
        }
    }








    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
      //  List<FileToSendPath> mPathsList;
        public MyAdapter(List<FileToSendPath> mPathsListR) {
          //  mPathsList=mPathsListR;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            View recyclerFile = layoutInflater.inflate(R.layout.receive_recycler_layout, viewGroup, false);
            MyViewHolder viewHolder = new MyViewHolder(recyclerFile);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
            myViewHolder.fileName.setText(mPathsList.get(i).name);
            myViewHolder.progressBar.setProgress(mPathsList.get(i).progress);
            if (String.valueOf(mPathsList.get(i).progress).equalsIgnoreCase("100")){
                myViewHolder.percentageText.setText("Completed!");
                myViewHolder.openButton.setVisibility(View.VISIBLE);
                myViewHolder.openButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = mPathsList.get(i).getPath();
                        Intent in = new Intent(Intent.ACTION_VIEW);
                        if(mPathsList.get(i).getType().equalsIgnoreCase("Photo")){
                        in.setDataAndType(Uri.parse(path),"image/*");}
                        else if(mPathsList.get(i).getType().equalsIgnoreCase("Video")){
                            in.setDataAndType(Uri.parse(path),"video/*");
                        }
                        else if(mPathsList.get(i).getType().equalsIgnoreCase("File")){
                            MimeTypeMap myMime = MimeTypeMap.getSingleton();
                           String seg= Uri.parse(path).getLastPathSegment();
                            String mimeType = myMime.getMimeTypeFromExtension(seg);
                            in.setDataAndType(Uri.parse(path),mimeType);
                        }
                        else if(mPathsList.get(i).getType().equalsIgnoreCase("Music")){
                            in.setDataAndType(Uri.parse(path),"music/*");
                        }
                        //i.setData(Uri.parse(path));
                        try {
                            startActivity(in);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(ReceiveActivity.this, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }else{
                myViewHolder.percentageText.setText(String.valueOf(mPathsList.get(i).progress)+" %");

            }
        }

        @Override
        public int getItemCount() {
            return mPathsList.size();
        }
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView fileName;
        ProgressBar progressBar;
        TextView percentageText;
        Button openButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName=itemView.findViewById(R.id.rec_file_name);
            progressBar=itemView.findViewById(R.id.rec_recycler_progressbar);
            percentageText=itemView.findViewById(R.id.rec_percentage_text);
            openButton=itemView.findViewById(R.id.rec_open_button);
            openButton.setVisibility(View.GONE);
            progressBar.setIndeterminate(false);
            progressBar.setMax(100);

        }
    }
    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }


}
