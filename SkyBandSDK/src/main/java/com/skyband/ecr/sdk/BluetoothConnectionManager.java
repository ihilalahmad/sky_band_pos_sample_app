package com.skyband.ecr.sdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.skyband.ecr.sdk.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import lombok.SneakyThrows;

public class BluetoothConnectionManager {

    private static Logger logger = Logger.getNewLogger(BluetoothConnectionManager.class.getName());

    private static final String APP_NAME = "BluetoothChatApp";
    private static final UUID MY_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private final BluetoothAdapter bluetoothAdapter;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private Handler handler;
    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private int state;

    static final int STATE_NONE = 0;
    static final int STATE_LISTEN = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    public static final int MESSAGE_DEVICE_OBJECT = 4;
    public static final int MESSAGE_TOAST = 5;

    public BluetoothSocket socket;
    private BluetoothDevice device;

    private InputStream inputStream;
    private OutputStream outputStream;

    private static BluetoothConnectionManager bluetoothConnectionManager;

    public static BluetoothConnectionManager instance() {
        if (bluetoothConnectionManager != null) {
            return bluetoothConnectionManager;
        }
        return null;
    }

    public static BluetoothConnectionManager instance(Handler handler) {

        if (bluetoothConnectionManager == null) {
            bluetoothConnectionManager = new BluetoothConnectionManager(handler);
        } else {
            bluetoothConnectionManager.setHandler(handler);
        }
        return bluetoothConnectionManager;
    }

    public BluetoothConnectionManager(Handler handler) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        state = STATE_NONE;

        this.handler = handler;
    }

    // Set the current state of the chat connection
    private synchronized void setState(int state) {
        this.state = state;

        handler.obtainMessage(1, state, -1).sendToTarget();
    }

    // get current connection state
    public synchronized int getState() {
        return state;
    }

    // start service
    public synchronized void start() {
        // Cancel any thread
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }


        setState(STATE_LISTEN);
        if (acceptThread == null) {
            acceptThread = new AcceptThread();
            acceptThread.start();
        }
    }

    // initiate connection to remote device
    public synchronized void connect(BluetoothDevice device1) {

        device = device1;
        // Cancel any thread
        if (state == STATE_CONNECTING) {
            if (connectThread != null) {
                connectThread.cancel();
                connectThread = null;
            }
        }


        // Start the thread to connect with the given device
        connectThread = new ConnectThread();
        connectThread.start();
        setState(STATE_CONNECTING);
    }

    // manage Bluetooth connection
    public synchronized void connected() {
        // Cancel the thread
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        logger.info("connected Thread");

        // Send the name of the connected device back to the UI Activity
        Message msg = handler.obtainMessage(MESSAGE_DEVICE_OBJECT);
        Bundle bundle = new Bundle();
        bundle.putParcelable("device_name", device);
        msg.setData(bundle);
        handler.sendMessage(msg);
        setState(STATE_CONNECTED);

    }

    // stop all threads
    public synchronized void stop() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }
        setState(STATE_NONE);
    }

    /*public void write(byte[] out) {
        ReadWriteThread r;
        synchronized (this) {
            if (state != STATE_CONNECTED)
                return;
            r = connectedThread;
        }
        r.write(out);
    }*/

    private void connectionFailed() {
        Message msg = handler.obtainMessage(MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString("toast", "Unable to connect device");
        msg.setData(bundle);
        handler.sendMessage(msg);

        // Start the service over to restart listening mode
        BluetoothConnectionManager.this.start();
    }

    private void connectionLost() {
        Message msg = handler.obtainMessage(MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString("toast", "Device connection was lost");
        msg.setData(bundle);
        handler.sendMessage(msg);

        // Start the service over to restart listening mode
        BluetoothConnectionManager.this.start();
    }

    // runs while listening for incoming connections
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket serverSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME, MY_UUID);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            serverSocket = tmp;
        }

        public void run() {
            setName("AcceptThread");
            BluetoothSocket socket;
            while (state != STATE_CONNECTED) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothConnectionManager.this) {
                        switch (state) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // start the connected thread.
                                connected();
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate
                                // new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                }
                                break;
                        }
                    }
                }
            }
        }

        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
        }
    }

    // runs while attempting to make an outgoing connection
    private class ConnectThread extends Thread {

        public ConnectThread() {
            logger.info("Inside Connect thread");
            BluetoothSocket tmp = null;
            try {
                tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = tmp;
        }

        public void run() {
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            bluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                socket.connect();
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException e2) {
                }
                connectionFailed();
                return;
            }
            logger.info("After socket.connect()");
            // Reset the ConnectThread because we're done
            synchronized (BluetoothConnectionManager.this) {
                connectThread = null;
            }

            // Start the connected thread
            connected();
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

  /*  // runs during a connection with a remote device
    private class ReadWriteThread extends Thread {

        public ReadWriteThread() {
            bluetoothSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            inputStream = tmpIn;
            outputStream = tmpOut;
        }

        @SneakyThrows
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream
           *//* while (true) {
                try {
                    // Read from the InputStream
                    bytes = inputStream.read(buffer);

                    // Send the obtained bytes to the UI Activity
                    handler.obtainMessage(MESSAGE_READ, bytes, -1,
                            buffer).sendToTarget();
                } catch (IOException e) {
                    connectionLost();
                    // Start the service over to restart listening mode
                    BluetoothConnectionManager.this.start();
                    break;
                }
            }*//*
            int count = 0;
            byte[] terminalResponse = new byte[50000];
            long startTime = System.currentTimeMillis();
            long totalTime;
            do {
                logger.info("Waiting...");
                Thread.sleep(1l);
                if (inputStream.available() > 0) {
                    count = inputStream.read(terminalResponse, 0, terminalResponse.length);
                }
                logger.info("Count:" + count);
                long endTime = System.currentTimeMillis();
                totalTime = endTime - startTime;
                if (totalTime >= 120000) {

                }
            } while (count < 5);
            handler.obtainMessage(MESSAGE_READ, count, -1,
                    terminalResponse).sendToTarget();
        }

        // write to OutputStream
        public void write(byte[] buffer) {
            try {
                outputStream.write(buffer);
                handler.obtainMessage(MESSAGE_WRITE, -1, -1,
                        buffer).sendToTarget();
            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    public void write(byte[] buffer) throws IOException {
        inputStream = socket.getInputStream();
        outputStream  = socket.getOutputStream();
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
            Thread.sleep(1l);
            if (inputStream.available() > 0) {
                count = inputStream.read(terminalResponse, 0, terminalResponse.length);
            }
            logger.info("Count:" + count);
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

    static char[] nibbleToChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
}
