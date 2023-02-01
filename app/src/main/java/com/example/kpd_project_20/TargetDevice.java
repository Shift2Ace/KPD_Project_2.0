package com.example.kpd_project_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TargetDevice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button cancal;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_device);

        cancal = findViewById(R.id.target_cancel_button);

        // setting onClickListener
        cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an intent to switch to second activity upon clicking
                Intent intent = new Intent(TargetDevice.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}