package com.example.kpd_project_20;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;


public class AddDevice extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    EditText ssidInput, passwordInput;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    String deviceName, deviceAddress;
    ConnectThread connectDevice;
    ConnectedThread connectedDevice;

    TextView bt_note ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



        Button cancal;
        Button apply;
        Button connect;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        bt_note = findViewById(R.id.BT_connect_note);

        cancal = findViewById(R.id.wifi_cancel_button);
        apply = findViewById(R.id.wifi_apply_button);
        connect = findViewById(R.id.bt_connect_button);

        if (!bluetoothAdapter.isEnabled()) {
            if (ContextCompat.checkSelfPermission(AddDevice.this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                {
                    ActivityCompat.requestPermissions(AddDevice.this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 2);
                    return;
                }
            }
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // setting onClickListener
        cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an intent to switch to second activity upon clicking
                Intent intent = new Intent(AddDevice.this, MainActivity.class);
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

    private void applyAddDevice() {
        String ssid, password;
        ssidInput = (EditText) findViewById(R.id.wifi_ssid);
        passwordInput = (EditText) findViewById(R.id.wifi_password);
        ssid = ssidInput.getText().toString();
        password = passwordInput.getText().toString();
        Log.d("apply input (SSID)", ssid);
        Log.d("apply input (Password)", password);
        try {
            connectedDevice = new ConnectedThread(connectDevice.mmSocket);
            connectedDevice.start();
            connectedDevice.write("wifi_connection:"+ssid+","+password);
        }catch (Exception e){
            bt_note.setText("Please connect ALT first");
        }

    }

    private void connectButton() {
        bt_note.setText("Connecting...");
        if (bluetoothAdapter.isEnabled()) {
            if (ContextCompat.checkSelfPermission(AddDevice.this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(AddDevice.this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 2);
                    return;
                }
            }
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            Log.d("Device Address", String.valueOf(pairedDevices.size()));
            if (pairedDevices.size() > 0) {
                boolean found_device = false;
                for (BluetoothDevice device1 : pairedDevices) {
                    String deviceNameTemp = device1.getName();
                    Log.d("Device Name:", deviceNameTemp);
                    Log.d("Device Address", device1.getAddress());

                    if (deviceNameTemp.equals("ATL Bluetooth")) {
                        device = device1;
                        deviceAddress = device1.getAddress();
                        deviceName = deviceNameTemp;
                        found_device = true;
                    }
                }
                if (!found_device) {
                    bt_note.setText("Cannot find any ATL");
                } else {
                    connectDevice = new ConnectThread(device);
                    connectDevice.start();
                }

            }
        }else {
            bt_note.setText("Please turn on Bluetooth");
        }

    }



    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.

                if (ContextCompat.checkSelfPermission(AddDevice.this, android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    {
                        ActivityCompat.requestPermissions(AddDevice.this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 2);
                        return;
                    }
                }


                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                Log.d("success", "tmp");
            } catch (IOException e) {
                Log.e("TAG", "Socket's create() method failed", e);
            }
            mmSocket = tmp;
            Log.d("debug", tmp.toString());
        }

        public void run() {
            Log.d("RUN", "connect device");
            // Cancel discovery because it otherwise slows down the connection.
            if (ContextCompat.checkSelfPermission(AddDevice.this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                {
                    ActivityCompat.requestPermissions(AddDevice.this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 2);
                    return;
                }
            }
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Log.d("success", "connected device");
                bt_note.setText("Connection Succeeded");
            } catch (IOException connectException) {
                bt_note.setText("Connection failed");
                Log.d("failed", "unable to connect device");
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e("TAG", "Could not close the client socket", closeException);
                }
                return;
            }
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("TAG", "Could not close the client socket", e);
            }
        }
    }







    private static final String TAG = "MY_APP_DEBUG_TAG";
    private Handler handler; // handler that gets info from Bluetooth service

    // Defines several constants used when transmitting messages between the
    // service and the UI.
    private class MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    // Send the obtained bytes to the UI activity.
                    Message readMsg = handler.obtainMessage(MessageConstants.MESSAGE_READ, numBytes, -1, mmBuffer);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(String string) {
            try {
                Log.d(TAG, string);
                mmOutStream.write(string.getBytes());

            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }


}