package com.example.juanito.r2_pi0;

/**
 * Created by juanito on 22/12/16.
 */

import android.bluetooth.*;
import android.content.Intent;
import android.os.ParcelUuid;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class BluetoothConnection {

    public void create(){

        // Get local Bluetooth adapter
       BluetoothAdapter mBluetoothAdapter = null;

        // If BT is not on, request that it be enabled.
        // setupCommand() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        // otherwise set up the command service
        else {
            if (mCommandService==null)
                setupCommand();
        }
        }

    }

}
