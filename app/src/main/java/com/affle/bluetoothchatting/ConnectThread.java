package com.affle.bluetoothchatting;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by akash on 21/8/17.
 */

public class ConnectThread extends Thread {
    BluetoothDevice mBluetoothdevice = null;
    BluetoothSocket mBluetoothSocket = null;
    public ConnectThread(BluetoothDevice bluetoothDevice) {
        UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        UUID second=UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
        BluetoothSocket tmp =null;
        mBluetoothdevice =bluetoothDevice;
        try {
            tmp = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mBluetoothSocket = tmp;

    }

    public  void run()
    {
        try {
            mBluetoothSocket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                mBluetoothSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        manageMyConnectedSocket(mBluetoothSocket);
    }

    private void manageMyConnectedSocket(BluetoothSocket bluetoothSocket) {
        UUID.randomUUID();
    }
}
