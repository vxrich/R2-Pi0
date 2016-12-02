package com.example.juanito.r2_pi0;

import android.app.Activity;
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

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

          setContentView(R.layout.activity_main);

        Toast.makeText(getApplicationContext(), "Sono vivo 1", Toast.LENGTH_SHORT).show();

        AsyncTask<Void, Void, List<InetAddress>> at = (new DiscoveryClient(5000, 5001).execute());

        InetAddress addr = null;

        Toast.makeText(getApplicationContext(), "Sono vivo 2", Toast.LENGTH_SHORT).show();

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

        Toast.makeText(getApplicationContext(), "Sono vivo 3", Toast.LENGTH_SHORT).show();

        if (addr!= null)
        {
            tcpClient = new TcpClient(addr.toString());
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Impossibile connettersi", Toast.LENGTH_SHORT);
            toast.show();
        }
        ImageButton forward = (ImageButton) findViewById(R.id.forward);
        ImageButton stop = (ImageButton) findViewById(R.id.stop);
        ImageButton left = (ImageButton) findViewById(R.id.left);
        ImageButton right = (ImageButton) findViewById(R.id.right);
        ImageButton shutdown = (ImageButton) findViewById(R.id.shutdown);
        ImageButton music = (ImageButton) findViewById(R.id.music);

        final TcpClient mTcpClient = tcpClient;



        forward.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (mTcpClient != null) {
                    mTcpClient.sendMessage("avanti");
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (mTcpClient != null) {
                    mTcpClient.sendMessage("stop");
                }
            }
        });

        left.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (mTcpClient != null) {
                    mTcpClient.sendMessage("left");
                }
            }
        });

        right.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (mTcpClient != null) {
                    mTcpClient.sendMessage("right");
                }
            }
        });

        shutdown.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (mTcpClient != null) {
                    mTcpClient.sendMessage("shutdown");
                }
            }
        });

        music.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (mTcpClient != null) {
                    mTcpClient.sendMessage("music");
                }
            }
        });

}}
