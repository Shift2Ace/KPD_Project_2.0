package com.example.kpd_project_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UdpServer.runUdpServer();
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


}