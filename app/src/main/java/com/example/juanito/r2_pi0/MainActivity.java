package com.example.juanito.r2_pi0;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity{

    private BluetoothCommunication blueComm;

    private JoystickTranslator trans = new JoystickTrigonometricTranslator();

    private static final int REQUEST_ENABLE_BT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Context context = getApplicationContext();
        final Activity activity = this;

        blueComm = new BluetoothCommunication(context, activity);
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



        JoyStickView joy = (JoyStickView) findViewById(R.id.joy);

        joy.setOnJoystickMoveListener(new JoyStickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                blueComm.move(trans.getSpeed(power, angle));
                blueComm.rotate(trans.getRotation(power, angle));
            }
        }, 100);

        ImageButton shutdown = (ImageButton) findViewById(R.id.shutdown);
        ImageButton music = (ImageButton) findViewById(R.id.music);
        ImageButton bluetooth = (ImageButton) findViewById(R.id.bluetooth);

            shutdown.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   blueComm.shutdown();
                }
            });

            music.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   blueComm.makeSound();
                }
            });

            bluetooth.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    //Apre l'activity con la lista dei device associati e trovati
                    blueComm.connection();
                }
            });
    }




}
