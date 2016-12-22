package com.example.juanito.r2_pi0;

/**
 * Created by juanito on 22/12/16.
 */

import android.bluetooth.*;
import android.content.Intent;
import android.os.ParcelUuid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class BluetoothConnection {

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
            startActivityForResult(turnOn, 0);
        } else {
            Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();

            if (bondedDevices.size() > 0) {
                Object[] devices = (Object[]) bondedDevices.toArray();
                BluetoothDevice device = (BluetoothDevice) devices[position];
                ParcelUuid[] uuids = device.getUuids();
                BluetoothSocket socket = null;
                try {
                    socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inputStream = socket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                };

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
