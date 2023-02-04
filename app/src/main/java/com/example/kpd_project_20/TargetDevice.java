package com.example.kpd_project_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TargetDevice extends AppCompatActivity {
    Button cancal,apply;
    EditText targetName, targetPort, targetIP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_device);

        cancal = findViewById(R.id.target_cancel_button);
        apply = findViewById(R.id.target_apply_button);
        targetName = (EditText)findViewById(R.id.target_name);
        targetIP = (EditText)findViewById(R.id.target_ipAddress);
        targetPort = (EditText)findViewById(R.id.target_port);


        // setting onClickListener
        cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an intent to switch to second activity upon clicking
                Intent intent = new Intent(TargetDevice.this,MainActivity.class);
                startActivity(intent);
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    applyButton();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void applyButton() throws UnknownHostException {

        udpService.udpPort = 2333;
        udpService.addr = InetAddress.getByName("192.168.1.188");
        udpService.send("test");
    }
}