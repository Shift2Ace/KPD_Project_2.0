package com.example.kpd_project_20;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TargetDevice extends AppCompatActivity {
    Button cancal, apply;
    EditText targetName, targetPort, targetIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Boolean running = true;
                while (running){
                    if (UdpServer.connect_state){
                        Toast.makeText(getApplicationContext(), "Device Added",
                                Toast.LENGTH_LONG).show();
                        running = false;
                        UdpServer.connect_state = false;
                        cancalButton();
                    }
                }
            }
        }).start();
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
                cancalButton();
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
        try {
            udpService.udpPort = Integer.valueOf(targetPort.getText().toString());
            udpService.addr = InetAddress.getByName(targetIP.getText().toString());
            udpService.send("UDP_Port:" + UdpServer.UDP_SERVER_PORT);
        }catch (Exception e){

        }

    }

    public void cancalButton(){
        Intent intent = new Intent(TargetDevice.this,MainActivity.class);
        startActivity(intent);
    }

}