package com.affle.bluetoothchatting;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by akash on 21/8/17.
 */

public class AcceptThread extends Thread {
    private BluetoothServerSocket bluetoothServerSocket;
    public AcceptThread(BluetoothAdapter bluetoothAdapter, UUID chatUUID) {
        BluetoothServerSocket bluetoothServerSocketTmp = null;
        try {
            bluetoothServerSocketTmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("BluetoothChat",chatUUID);
            Log.d("listen bluetooth","Bluetooth listening devices");
        } catch (IOException e) {
            e.printStackTrace();
        }
        bluetoothServerSocket = bluetoothServerSocketTmp;
    }

    public void run()
    {
        BluetoothSocket bluetoothSocket =null;
        while(true)
        {
            try {
                Log.d("accept bluetooth","Bluetooth start accepting devices");
                bluetoothSocket =   bluetoothServerSocket.accept();

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

            if(bluetoothSocket!=null)
            {
                Log.d("accepted bluetooth","Bluetooth connected");
                cancel();
            }
        }
    }

    public void cancel()
    {
        try {
            bluetoothServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
