package com.example.juanito.r2_pi0;


import android.net.wifi.WifiConfiguration;

/**
 * Created by juanito on 02/12/16.
 */

public class WiFiConnection
{
    String networkSSID = "R2-Pi0";
    //String networkPass = "raspberry";

    WifiConfiguration conf = new WifiConfiguration();

    public void connection()
    {
        conf.SSID = "\"" + networkSSID + "\"";

        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
    }
}
