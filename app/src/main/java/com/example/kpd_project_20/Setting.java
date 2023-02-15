package com.example.kpd_project_20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }
}