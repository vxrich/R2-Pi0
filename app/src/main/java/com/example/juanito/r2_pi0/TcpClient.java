package com.example.juanito.r2_pi0;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by juanito on 22/11/16.
 */

public class TcpClient {

    public final String SERVER_IP; //your computer IP address
    public static final int SERVER_PORT = 5003;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(String server_ip)
    {
        SERVER_IP = server_ip;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param message text entered by client
     */
    public void sendMessage(String message)
    {
        messageSender msg_send = new messageSender();
        msg_send.execute(message);
    }


    class messageSender extends AsyncTask<String, Void, Void>
    {


        @Override
        protected Void doInBackground(String... params) {

            try {

                    //here you must put your computer's IP address.
                    InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                    Log.e("TCP Client", "C: Connecting...");

                    //create a socket to make the connection with the server
                    Socket socket = new Socket(serverAddr, SERVER_PORT);

                    try {

                        //sends the message to the server
                        PrintWriter mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                        mBufferOut.print(params[0]);
                        mBufferOut.flush();

                    } catch (Exception e) {

                        Log.e("TCP", "S: Error", e);

                    } finally {
                        //the socket must be closed. It is not possible to reconnect to this socket
                        // after it is closed, which means a new socket instance has to be created.
                        socket.close();
                    }
            } catch (Exception e) {

                Log.e("TCP", "C: Error", e);

            }
            return null;
        }
    }
}
