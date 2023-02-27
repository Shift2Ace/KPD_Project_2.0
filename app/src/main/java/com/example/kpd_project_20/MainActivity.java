package com.example.kpd_project_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView view;
    static String TAG = "Main Activity";
    static Boolean updateImage = false;
    private static byte[] imageData;
    Bitmap bmp;
    Button b_up, b_down, b_left, b_right, b_music, b_lightOff;
    SeekBar sb_brightness;
    TextView tv_brightness, info;



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

        b_up = findViewById(R.id.button_up);
        b_down = findViewById(R.id.button_down);
        b_left = findViewById(R.id.button_left);
        b_right = findViewById(R.id.button_right);
        b_music = findViewById(R.id.button_musicMode);
        b_lightOff = findViewById(R.id.button_lightOff);
        tv_brightness = (TextView)findViewById(R.id.tv_brightness);
        sb_brightness = (SeekBar)findViewById(R.id.sb_brightness);
        info = findViewById(R.id.info_context);

        b_up.setOnClickListener(this);
        b_down.setOnClickListener(this);
        b_left.setOnClickListener(this);
        b_right.setOnClickListener(this);
        b_music.setOnClickListener(this);
        b_lightOff.setOnClickListener(this);

        sb_brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG,"Seekbar change : "+progress);
                ATL.brightness = progress;
                udpService.send("LED_Light_B:"+progress);
                tv_brightness.setText("Brightness ("+progress+"/255)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                udpService.send("LED_Light_B:"+ATL.brightness);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
                }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_up:
                ATL.angleY -= 5;
                if (ATL.angleY < 60){
                    ATL.angleY = 60;
                }
                update();
                Log.d(TAG,"Up button : "+ATL.angleY);
                udpService.send("Servo_Y_Ang:"+ATL.angleY);
                break;
            case R.id.button_down:
                ATL.angleY += 5;
                if (ATL.angleY > 120){
                    ATL.angleY = 120;
                }
                update();
                Log.d(TAG,"Down button : "+ATL.angleY);
                udpService.send("Servo_Y_Ang:"+ATL.angleY);
                break;
            case R.id.button_left:
                ATL.angleX += 5;
                if (ATL.angleX > 135){
                    ATL.angleX = 135;
                }
                update();
                Log.d(TAG,"Left button : "+ATL.angleX);
                udpService.send("Servo_X_Ang:"+ATL.angleX);
                break;
            case R.id.button_right:
                ATL.angleX -= 5;
                if (ATL.angleX < 45){
                    ATL.angleX = 45;
                }
                update();
                Log.d(TAG,"Right button : "+ATL.angleX);
                udpService.send("Servo_X_Ang:"+ATL.angleX);
                break;
            case R.id.button_musicMode:
                Log.d(TAG,"Music mode button");
                update();
                break;
            case R.id.button_lightOff:
                update();
                Log.d(TAG,"Light off button");
                udpService.send("LED_Light_B:"+0);
                tv_brightness.setText("Brightness (off)");
                break;
        }
    }
    public void update(){
        info.setText("IP Address : "+ATL.ipAddress+"\nAngle X : "+ATL.angleX+"\nAngle Y : "+ATL.angleY);
    }
}