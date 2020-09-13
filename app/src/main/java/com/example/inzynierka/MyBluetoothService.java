package com.example.inzynierka;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class MyBluetoothService extends AppCompatActivity {
    private static final String TAG = "MY_APP_DEBUG_TAG";
    public final static String MODULE_MAC = "98:D3:51:FD:7C:E9";
    private static final String uuid = "00001101-0000-1000-8000-00805F9B34FB";
    public BluetoothDevice mmBluetoothDevice;
    private BluetoothSocket mmSocket;
    public ConnectedThread myThread;
    public boolean connected = false;
    public boolean initiated = false;

    public void initiateBluetoothProcess(final BluetoothAdapter adapter, final String alternateMAC) {
        final Runnable initiate = new Thread() {
            @Override
            public void run() {
                BluetoothSocket temp;
                mmBluetoothDevice = adapter.getRemoteDevice(alternateMAC);
                try{
                    temp = mmBluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
                    mmSocket = temp;
                    mmSocket.connect();
                    connected = true;
                } catch(IOException e){
                    connected = false;
                    try{mmSocket.close();}catch(IOException c){return;}
                }
                myThread = new ConnectedThread(mmSocket);
                myThread.start();
            }
        };

        final ExecutorService executor;
        executor = Executors.newSingleThreadExecutor();
        final Future future = executor.submit(initiate);
        executor.shutdown();
        try {
            future.get(3, TimeUnit.SECONDS);
        }
        catch (TimeoutException te) {
            connected = false;
            return;
        }
        catch (InterruptedException ignored) {}
        catch (ExecutionException ignored) {}

        if (!executor.isTerminated())
            executor.shutdownNow();
        initiated = true;
    }

    public class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);
            }
        }
    }
}