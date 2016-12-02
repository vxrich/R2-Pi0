/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.juanito.r2_pi0;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Davide
 */
public class DiscoveryClient extends AsyncTask <Void, Void, List<InetAddress>> {
    private int port, remPort;

    public DiscoveryClient(int port, int remPort) {
        this.port = port;
        this.remPort = remPort;
    }
    
    public List<InetAddress> discovery ()
    {
        try {
             final List<InetAddress> lst = new ArrayList<>();
            
            Runnable list = new Runnable() {
                @Override
                public void run() {
                    try (DatagramSocket s = new DatagramSocket(port);){
                        s.setSoTimeout(1000);
                        while(true)
                        {
                            DatagramPacket p = new DatagramPacket(new byte[1000],1000);
                            s.receive(p);
                            
                            lst.add(p.getAddress());
                        }
                    } catch (SocketException ex) {
                        
                    } catch (IOException ex) {
                        
                    }
                }
            };
            
            Thread t = new Thread(list);
            
            t.start();
            getBroadcastAddresses();
            try (DatagramSocket s = new DatagramSocket();){
                
                
                DatagramPacket p = new DatagramPacket(new byte[1000],1000);
                p.setData(getPort(), 0, 2);
                p.setPort(remPort);
                for (InetAddress addr : getBroadcastAddresses())
                {
                    p.setAddress(addr);
                    try{
                        s.send(p);
                    } catch (SocketException ex) {

                    } catch (IOException ex) {

                    }
                }
                
            } catch (SocketException ex) {

            } catch (IOException ex) {

            }
            
            t.join();
            
            return lst;
        } catch (InterruptedException ex) {
            Logger.getLogger(DiscoveryClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private byte[] getPort ()
    {
        byte[] ris = new byte[2];
        
        ris[0] = (byte)(port & 0xFF);
        ris[1] = (byte)((port >> 8) & 0xFF);
        
        //System.out.println("0: " + ris[0] + " 1: " + ris[1]);
        
        return ris;
    }
    
    private ArrayList<InetAddress> getBroadcastAddresses() {
        ArrayList<InetAddress> listOfBroadcasts = new ArrayList();
        Enumeration list;
        try {
            list = NetworkInterface.getNetworkInterfaces();

            while(list.hasMoreElements()) {
                NetworkInterface iface = (NetworkInterface) list.nextElement();

                if(iface != null && !iface.isLoopback() && iface.isUp()) {
                    //System.out.println("Found non-loopback, up interface:" + iface);

                    for (InterfaceAddress address : iface.getInterfaceAddresses())
                    {
                        //System.out.println("Found address: " + address);

                        if(address == null) continue;
                        InetAddress broadcast = address.getBroadcast();
                        if(broadcast != null) listOfBroadcasts.add(broadcast);
                    }
                }
            }
        } catch (SocketException ex) {
            return new ArrayList<InetAddress>();
        }

        return listOfBroadcasts;
}

    @Override
    protected List<InetAddress> doInBackground(Void... params) {
        return discovery();
    }
}
