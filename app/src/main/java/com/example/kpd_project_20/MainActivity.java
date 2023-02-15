package com.example.kpd_project_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;



public class MainActivity extends AppCompatActivity {

    ImageView view;
    static String TAG = "Main Activity";
    static Boolean updateImage = false;
    private static byte[] imageData;
    Bitmap bmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UdpServer.runUdpServer();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if (updateImage){
                        view = findViewById(R.id.imageView);
                        bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.setImageBitmap(bmp);
                            }
                        });

                    }
                }
            }
        }).start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_device:
                if (checkBT()){
                    openAddDevice();
                };
                return true;
            case R.id.target_device:
                openTargetDevice();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openAddDevice(){
        Intent intent = new Intent(this, AddDevice.class);
        startActivity(intent);
    }
    public void openTargetDevice(){
        Intent intent = new Intent(this, TargetDevice.class);
        startActivity(intent);
    }
    public void openSettings() {
        Intent intent = new Intent(this, Setting.class);
        startActivity(intent);
    }

    public boolean checkBT(){
        Boolean haveBT;
        haveBT = true;
        if (BluetoothAdapter.getDefaultAdapter() == null){
            Dialog dialog = new Dialog();
            dialog.show(getSupportFragmentManager(),"dialog");
            haveBT = false;
        }
        return(haveBT);
    }
    public static void imageWrite(byte[] data){
        imageData = data;
        updateImage = true;
    }
}