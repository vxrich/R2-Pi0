package com.example.juanito.r2_pi0;

/**
 * Created by juanito on 22/12/16.
 */

import android.bluetooth.*;
import android.content.Intent;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class BluetoothConnection extends ActivityCompat {

    private OutputStream outputStream;
    private InputStream inputStream;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public void connection()
    {
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            Integer REQUEST_ENABLE_BT = 0;
            startActivityForResult(turnOn, REQUEST_ENABLE_BT);
        }

        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();

        if (bondedDevices.size() > 0) {

            for(BluetoothDevice device: bondedDevices){
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
            }
        }

    }

        public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }

        public void run() {
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytes = 0;
        int b = BUFFER_SIZE;

        while (true) {
            try {
                bytes = inputStream.read(buffer, bytes, BUFFER_SIZE - bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
