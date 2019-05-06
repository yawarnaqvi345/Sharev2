package com.example.share;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.share.fragments.mainfragments.FileExplorer;
import com.example.share.fragments.mainfragments.TextStream;
import com.example.share.fragments.mainfragments.Transfer;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        getPermissions();
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_section, new Transfer()).commit();
    }



    BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Log.d(TAG, "NavigationMenuClicked");
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.nav_transfers:
                            selectedFragment = new Transfer();
                            break;
                        case R.id.nav_files_explorer:
                            selectedFragment = new FileExplorer();
                            break;
                        case R.id.nav_text_stream:
                            selectedFragment = new TextStream();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_section, selectedFragment).commit();
                    return true;
                }
            };
   void getPermissions(){
       if (ContextCompat.checkSelfPermission(MainActivity.this,
               Manifest.permission.READ_EXTERNAL_STORAGE)
               != PackageManager.PERMISSION_GRANTED) {
           // No explanation needed; request the permission
           ActivityCompat.requestPermissions(MainActivity.this,
                   new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                   0);
       }

       if (ContextCompat.checkSelfPermission(MainActivity.this,
               Manifest.permission.WRITE_EXTERNAL_STORAGE)
               != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(MainActivity.this,
                   new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                   1);
       }

       if (ContextCompat.checkSelfPermission(MainActivity.this,
               Manifest.permission.ACCESS_COARSE_LOCATION)
               != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(MainActivity.this,
                   new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                   2);
       }
}
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       switch (requestCode){
           case 0:
               if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                   if (ContextCompat.checkSelfPermission(MainActivity.this,
                           Manifest.permission.WRITE_EXTERNAL_STORAGE)
                           != PackageManager.PERMISSION_GRANTED) {
                       ActivityCompat.requestPermissions(MainActivity.this,
                               new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                               1);}

                   if (ContextCompat.checkSelfPermission(MainActivity.this,
                           Manifest.permission.ACCESS_COARSE_LOCATION)
                           != PackageManager.PERMISSION_GRANTED) {
                       ActivityCompat.requestPermissions(MainActivity.this,
                               new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                               2);}
               }else{
                  this.finish();}
               break;
           case 1:
               if(grantResults[0]== PackageManager.PERMISSION_GRANTED){

                   if (ContextCompat.checkSelfPermission(MainActivity.this,
                           Manifest.permission.READ_EXTERNAL_STORAGE)
                           != PackageManager.PERMISSION_GRANTED) {
                       // No explanation needed; request the permission
                       ActivityCompat.requestPermissions(MainActivity.this,
                               new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                               0);}

                   if (ContextCompat.checkSelfPermission(MainActivity.this,
                           Manifest.permission.ACCESS_COARSE_LOCATION)
                           != PackageManager.PERMISSION_GRANTED) {
                       ActivityCompat.requestPermissions(MainActivity.this,
                               new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                               2);}

               }else{
                   this.finish();}
               break;
           case 2:
               if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                   if (ContextCompat.checkSelfPermission(MainActivity.this,
                           Manifest.permission.READ_EXTERNAL_STORAGE)
                           != PackageManager.PERMISSION_GRANTED) {
                       // No explanation needed; request the permission
                       ActivityCompat.requestPermissions(MainActivity.this,
                               new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                               0);}


                   if (ContextCompat.checkSelfPermission(MainActivity.this,
                           Manifest.permission.WRITE_EXTERNAL_STORAGE)
                           != PackageManager.PERMISSION_GRANTED) {
                       ActivityCompat.requestPermissions(MainActivity.this,
                               new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                               1);}

               }else{
                   this.finish();}
               break;
        default:
            break;
       }
    }
}
