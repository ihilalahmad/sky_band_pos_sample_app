package com.skyband.ecr.sdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.skyband.ecr.sdk.logger.Logger;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

public class BluetoothConnectionManager {

    private static Logger logger = Logger.getNewLogger(BluetoothConnectionManager.class.getName());

    private static final UUID MY_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private final BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    public BluetoothSocket socket;

    private InputStream inputStream;
    private OutputStream outputStream;

    private static BluetoothConnectionManager bluetoothConnectionManager;

    public static BluetoothConnectionManager instance() {
        if (bluetoothConnectionManager != null) {
            return bluetoothConnectionManager;
        }
        return null;
    }

    public static BluetoothConnectionManager instances(BluetoothDevice device) throws IOException {

        if (bluetoothConnectionManager == null) {
            bluetoothConnectionManager = new BluetoothConnectionManager();
            bluetoothConnectionManager.connect1(device);
        } else {
            bluetoothConnectionManager.cleanup();
            bluetoothConnectionManager = new BluetoothConnectionManager();
            bluetoothConnectionManager.connect1(device);
        }
        return bluetoothConnectionManager;
    }

    private void cleanup() throws IOException {
        logger.info("ConnectionManager | Cleanup | Entering");

        if (inputStream != null) {

            try {
                inputStream.close();
            } catch (IOException e) {
                logger.severe("Exception while closing", e);
            }
        }

        if (outputStream != null) {

            outputStream.close();
        }

        if (socket != null) {

            try {
                socket.close();
            } catch (IOException e) {
                logger.severe("Exception while closing", e);
            }
        }

        inputStream = null;
        outputStream = null;
        socket = null;

        logger.info("ConnectionManager | Cleanup | Exiting");
    }

    public BluetoothConnectionManager() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public int connect1(BluetoothDevice device) throws IOException {
        socket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
        bluetoothAdapter.cancelDiscovery();

        try {
            socket.connect();
            return 0;
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e2) {
            }
            return 1;
        }
    }

    public String sendAndRecv(byte[] in) throws IOException, InterruptedException {

        logger.info("ConnectionManager | SedAndRecv | Entering");
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

        outputStream.write(in);

        outputStream.flush();

        byte[] responseBytes = new byte[50000];

        int noOfBytesRead = inputStream.read(responseBytes, 0, responseBytes.length);

        System.out.println(noOfBytesRead);

        if (noOfBytesRead <= 0) {
            throw new IOException();
        }

        byte[] finalResponse = new byte[noOfBytesRead];

        System.arraycopy(responseBytes, 0, finalResponse, 0, noOfBytesRead);
        String terminalResponse = new String(finalResponse);

        logger.info("ConnectionManager | SedAndRecv | Exiting");

        return terminalResponse;

    }


    public boolean isConnected() {

        logger.info("BluetoothConnectionManager | Isconnected | Entering");

        if (socket != null) {
            return socket.isConnected();
        }
        logger.info("BluetoothConnectionManager | Isconnected | Exiting");

        return false;
    }

}
