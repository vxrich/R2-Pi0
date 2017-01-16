package com.example.juanito.r2_pi0;

/**
 * Created by Riccardo on 22/12/16.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothCommunication {

    private BluetoothAdapter blueAdapter = null;
    private BluetoothDevice blueDev = null;
    private BluetoothSocket sock = null;
    private PrintWriter blueOut = null;

    private Context context = null;
    private Activity activity = null;

    List<String> s = new ArrayList<String>();
    String blueDevName;

    public BluetoothCommunication(Context context, Activity activity){

        this.context = context;
        this.activity = activity;

    }


    public void connection(){

        blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter == null) {
            return;// Device does not support Bluetooth
        }
        if (!blueAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            this.activity.startActivityForResult(enableBtIntent, 100);
        }

        final Set<BluetoothDevice> pairedDevices = blueAdapter.getBondedDevices();

        final List<String> devNames = new ArrayList<>();

        for (BluetoothDevice dev : pairedDevices)
        {
            devNames.add(dev.getName());
        }

        //StringBuilder sb = new StringBuilder();

        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);

        builder.setTitle(R.string.choose);
        builder.setItems(devNames.toArray(new String[devNames.size()]), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item

                String devName = devNames.get(which);

                if (pairedDevices.size() > 0) {
                    // There are paired devices. Get the name and address of each paired device.
                    for (BluetoothDevice device : pairedDevices) {
                        if (device.getName().equals(devName)) {
                            blueDev = device;
                            break;
                        }
                    }
                }

                if (blueDev != null)
                {

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
        });


        AlertDialog dialog = builder.create();

        dialog.show();




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

    private void send (String msg)
    {
        if (blueOut != null)
        {
            blueOut.print(msg);
            blueOut.flush();
        }
    }

    public void move (int speed)
    {
        send(String.format("s %d;", speed));
    }


    public void rotate (int rotation)
    {
        send(String.format("r %d;", rotation));
    }

    public void makeSound ()
    {
        makeSound("");
    }

    public void makeSound (String name)
    {
        send(String.format("sound %s;", name));
    }

    public void shutdown ()
    {
        send ("exit;");
    }

    public void moveForward (){ send(String.format("s 60;"));};

    public void rotationZero (){send(String.format("r 0;"));}

    public void shutup (){ send(String.format("shutup"));}
}
