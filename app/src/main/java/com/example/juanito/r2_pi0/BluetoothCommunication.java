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

// Classe per la gestione della connessione e invio dei messaggi via Bluetooth
public class BluetoothCommunication {

    // Creazione degli oggetti Bluetooth necessari alla connessione
    private BluetoothAdapter blueAdapter = null;
    private BluetoothDevice blueDev = null;
    private BluetoothSocket sock = null;
    private PrintWriter blueOut = null;

    // Creazione dei content e activity necessari per i messaggi Toast
    private Context context = null;
    private Activity activity = null;

    List<String> s = new ArrayList<String>();
    String blueDevName;

    public BluetoothCommunication(Context context, Activity activity){

        this.context = context;
        this.activity = activity;

    }

    // Metodo con il compito di instaurare la connessione con il dispositivo
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

                // L'argomento 'which' contiene l'indice della posizione dell'elemento selezionato
                String devName = devNames.get(which);

                if (pairedDevices.size() > 0) {
                    // Nome e indirizzo dei device associati
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
                        // Istanzia un BluetoothSocket per connettersi al BluetoothDevice selezionato
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

    // Metodo per resettare il Bluetooth
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

    // Metodo per gestire la caduta della connessione Bluetooth
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

    // Metodo base per l'invio di Stringhe
    private void send (String msg)
    {
        if (blueOut != null)
        {
            blueOut.print(msg);
            blueOut.flush();
        }
    }

    // Metodi che inviano le stringhe riguardandi le varie interazioni
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

    public void follow () { send("follow;");}

    public void reach() { send("reach;");}

    public void turn() { send("turn;");}

    public void shutup (){ send("shutup;");}

    public void project (){ send("project;");}
}
