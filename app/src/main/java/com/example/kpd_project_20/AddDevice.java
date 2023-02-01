package com.example.kpd_project_20;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddDevice extends AppCompatActivity {
    EditText ssidInput, passwordInput;
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();

        getSupportActionBar().hide();
        Button cancal;
        Button apply;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        cancal = findViewById(R.id.wifi_cancel_button);
        apply = findViewById(R.id.wifi_apply_button);


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
}