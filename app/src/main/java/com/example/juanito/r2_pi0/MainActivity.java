package com.example.juanito.r2_pi0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.bluetooth.*;
import java.net.InetAddress;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class MainActivity extends AppCompatActivity{

    BluetoothConnection bluetoothConn = new BluetoothConnection();


    private static final int REQUEST_ENABLE_BT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = getApplicationContext();
        final Activity activity = this;
        //WiFiConnection.connection(context);
        setContentView(R.layout.activity_main);

       /* AsyncTask<Void, Void, List<InetAddress>> at = (new DiscoveryClient(5000, 5001).execute());

        InetAddress addr = null;

        try {
            List<InetAddress> lista_at = at.get();
            if (lista_at.size() > 0)
            {
                addr = lista_at.get(0);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        TcpClient tcpClient = null;


        if (addr!= null)
        {
            tcpClient = new TcpClient(addr.toString());
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Impossibile connettersi", Toast.LENGTH_SHORT);
            toast.show();
        }

        final TcpClient mTcpClient = tcpClient;
        */



        JoyStickView joystick = (JoyStickView) findViewById(R.id.joy);
        ImageButton shutdown = (ImageButton) findViewById(R.id.shutdown);
        ImageButton music = (ImageButton) findViewById(R.id.music);
        ImageButton bluetooth = (ImageButton) findViewById(R.id.bluetooth);

            shutdown.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   /* if (mTcpClient != null) {
                        mTcpClient.sendMessage("shutdown");
                    }*/
                }
            });

            music.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   /* if (mTcpClient != null) {
                        mTcpClient.sendMessage("music");
                    }*/
                }
            });

            bluetooth.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    //Apre l'activity con la lista dei device associati e trovati
                    bluetoothConn.connection(context, activity);
                }
            });
    }




}
