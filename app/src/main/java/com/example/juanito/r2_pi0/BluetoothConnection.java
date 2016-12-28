package com.example.juanito.r2_pi0;

/**
 * Created by juanito on 22/12/16.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class BluetoothConnection {

    private BluetoothAdapter blueAdapter = null;
    private BluetoothDevice blueDev = null;
    private BluetoothSocket sock = null;
    private PrintWriter blueOut = null;

    private Context context = null;
    private Activity activity = null;

    private final String BLUE_DEV_NAME = "rpi2";

    List<String> s = new ArrayList<String>();
    String blueDevName;

    public void BluetoothConnection(Context context, Activity activity){

        this.context = context;
        this.activity = activity;

    }


    public void connection(){

        blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter == null) {
            // Device does not support Bluetooth
        }
        if (!blueAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            this.activity.startActivityForResult(enableBtIntent, 100);
        }

        Set<BluetoothDevice> pairedDevices = blueAdapter.getBondedDevices();

        //StringBuilder sb = new StringBuilder();

            AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);

            builder.setTitle(R.string.choose)
                    .setItems(s, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            if (pairedDevices.size() > 0) {
                                // There are paired devices. Get the name and address of each paired device.
                                for (BluetoothDevice device : pairedDevices) {
                                    if (device.getName().equals(BLUE_DEV_NAME))
                                    {
                                        blueDev = device;
                                    }
                                }
                            }

                         }
                    });

        AlertDialog dialog = builder.create();

        if (blueDev != null)
        {
            ParcelUuid[] UUIDS = blueDev.getUuids();

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                sock = blueDev.createInsecureRfcommSocketToServiceRecord( UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee") );

                sock.connect();

                blueOut = new PrintWriter (sock.getOutputStream());

            } catch (IOException e) {
                blueFallback();
            }
        }


    }

    private void resetBlue ()
    {
        blueAdapter.disable();

        while (blueAdapter.isEnabled())
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        blueAdapter.enable();

        while (!blueAdapter.isEnabled())
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    private void blueFallback ()
    {
        Toast.makeText(this.context.getApplicationContext(), "WARNING: BLUETOOTH FALLBACK!", Toast.LENGTH_LONG).show();

        resetBlue();

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
