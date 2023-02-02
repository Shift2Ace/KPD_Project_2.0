package com.example.kpd_project_20;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class AddDevice extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    EditText ssidInput, passwordInput;
    BluetoothAdapter bluetoothAdapter;
    MyBluetoothService mBluetoothConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        Log.d("BT","1");
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Log.d("BT","2");
        Button cancal;
        Button apply;
        Button connect;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        cancal = findViewById(R.id.wifi_cancel_button);
        apply = findViewById(R.id.wifi_apply_button);
        connect = findViewById(R.id.bt_connect_button);

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // setting onClickListener
        cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an intent to switch to second activity upon clicking
                Intent intent = new Intent(AddDevice.this,MainActivity.class);
                startActivity(intent);
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyAddDevice();
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectButton();
            }
        });

    }

    private void applyAddDevice(){
        String ssid, password;
        ssidInput = (EditText) findViewById(R.id.wifi_ssid);
        passwordInput = (EditText) findViewById(R.id.wifi_password);
        ssid = ssidInput.getText().toString();
        password = passwordInput.getText().toString();
        Log.d("apply input (SSID)",ssid);
        Log.d("apply input (Password)",password );

    }

    private void connectButton(){

    }
}