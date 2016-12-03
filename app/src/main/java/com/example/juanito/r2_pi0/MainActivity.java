package com.example.juanito.r2_pi0;

import android.app.Activity;
import android.content.Context;
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

        Context context = getApplicationContext();

        setContentView(R.layout.activity_main);

        //WiFiConnection.connection(context);



        ImageButton forward = (ImageButton) findViewById(R.id.forward);
        ImageButton stop = (ImageButton) findViewById(R.id.stop);
        ImageButton left = (ImageButton) findViewById(R.id.left);
        ImageButton right = (ImageButton) findViewById(R.id.right);
        ImageButton shutdown = (ImageButton) findViewById(R.id.shutdown);
        ImageButton music = (ImageButton) findViewById(R.id.music);

            final TcpClient  mTcpClient = new TcpClient();

            forward.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage("forward");
                    }
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage("stop");
                    }
                }
            });

            left.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage("left");
                    }
                }
            });

            right.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage("right");
                    }
                }
            });

            shutdown.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage("shutdown");
                    }
                }
            });

            music.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage("music");
                    }
                }
            });
}}
