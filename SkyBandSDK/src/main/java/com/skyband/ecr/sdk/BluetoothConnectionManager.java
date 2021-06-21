package com.skyband.ecr.sdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.skyband.ecr.sdk.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnectionManager {

    private static Logger logger = Logger.getNewLogger(BluetoothConnectionManager.class.getName());

    private static final UUID MY_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private final BluetoothAdapter bluetoothAdapter;

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

    public static BluetoothConnectionManager instances() throws IOException {

        if (bluetoothConnectionManager != null) {
            bluetoothConnectionManager.cleanup();
        }
        bluetoothConnectionManager = new BluetoothConnectionManager();
        return bluetoothConnectionManager;
    }

    private void cleanup() throws IOException {
        logger.info("ConnectionManager | Cleanup | Entering");

        if (inputStream != null) {

            try {
                inputStream.close();
            } catch (IOException e) {
                logger.severe("Exception while closing",e);
            }
        }

        if (outputStream != null) {

            outputStream.close();
        }

        if (socket != null) {

            try {
                socket.close();
            } catch (IOException e) {
                logger.severe("Exception while closing",e);
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

    public void write(byte[] buffer) throws IOException {
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        outputStream.write(buffer);
        outputStream.flush();
        logger.info("SerialConnectionManager | SendData | Exiting");
    }

    public String receiveData() throws IOException, InterruptedException {
        logger.info("SerialConnectionManager | Receive | Entering");
        String returnData = "";
        StringBuilder response = new StringBuilder();
        int count = 0;
        byte[] reSizeTerminalResponse = null;
        byte[] terminalResponse = new byte[50000];
        long startTime = System.currentTimeMillis();
        long totalTime;
        do {
            logger.info("Waiting...");
            Thread.sleep(1L);
            if (inputStream.available() > 0) {
                count = inputStream.read(terminalResponse, 0, terminalResponse.length);
            }
            logger.info("Count:" + count);
            if(!socket.isConnected()){
                throw new InterruptedException();
            }
            long endTime = System.currentTimeMillis();
            totalTime = endTime - startTime;
            if (totalTime >= 120000) {
                logger.info("TimedOut");
            }
        } while (count < 5);
        reSizeTerminalResponse = terminalResponse;
        response.append(byteArrayToHexString(reSizeTerminalResponse));
        returnData = response.toString();
        logger.info("Serial Response" + hexToString(returnData));

        logger.info("SerialConnectionManager | Receive | Exiting");

        return hexToString(returnData);

    }

    public static String byteArrayToHexString(byte[] data) {
        StringBuilder buff = new StringBuilder();

        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                byte b = data[i];
                int j = ((char) b) & 0xFF;
                buff.append(nibbleToChar[j >>> 4]);
                buff.append(nibbleToChar[j & 0x0F]);
            }
        }

        return buff.toString();
    }// byteArrayToHexString

    public static String hexToString(String hex) {

        StringBuilder sb = new StringBuilder();

        // 49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            // grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            // convert hex to decimal
            int decimal = Integer.parseInt(output, 16);
            // convert the decimal to character
            sb.append((char) decimal);
        }

        return sb.toString();
    }

    public boolean isConnected() {

        logger.info("BluetoothConnectionManager | Isconnected | Entering");

        if (socket != null) {
            return socket.isConnected();
        }
        logger.info("BluetoothConnectionManager | Isconnected | Exiting");

        return false;
    }

    static char[] nibbleToChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
}
