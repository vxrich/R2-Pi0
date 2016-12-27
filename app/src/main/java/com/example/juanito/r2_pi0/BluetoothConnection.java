package com.example.juanito.r2_pi0;

/**
 * Created by juanito on 22/12/16.
 */

import android.bluetooth.*;
import android.content.Intent;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class BluetoothConnection {

    private BluetoothAdapter blueAdapter = null;
    private BluetoothDevice blueDev = null;
    private BluetoothSocket sock = null;
    private PrintWriter blueOut = null;

    private final String BLUE_DEV_NAME = "rpi2";

    private void connection(){

        blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter == null) {
            // Device does not support Bluetooth
        }
        if (!blueAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 100);
        }

        Set<BluetoothDevice> pairedDevices = blueAdapter.getBondedDevices();

        StringBuilder sb = new StringBuilder();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.choose)
                .setItems(sb, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        blueDev = sb[which];
                    }
                });

        AlertDialog dialog = builder.create();

        /*if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals(BLUE_DEV_NAME))
                {
                    blueDev = device;
                }
            }
        } */

        if (blueDev != null)
        {
            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                sock = blueDev.createInsecureRfcommSocketToServiceRecord( UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

                sock.connect();

                blueOut = new PrintWriter (sock.getOutputStream());

            } catch (IOException e) {
                Log.e("AAA", "Socket's create() method failed", e);


                try {
                    Method createMethod = blueDev.getClass().getMethod("createInsecureRfcommSocket", new Class[] { int.class });
                    sock = (BluetoothSocket)createMethod.invoke(blueDev, 1);

                    sock.connect();

                    blueOut = new PrintWriter (sock.getOutputStream());

                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }


    }

}
