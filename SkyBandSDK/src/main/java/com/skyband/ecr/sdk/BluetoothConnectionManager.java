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
    private ReadWriteThread connectedThread;
    private int state;

    static final int STATE_NONE = 0;
    static final int STATE_LISTEN = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_OBJECT = 4;
    public static final int MESSAGE_TOAST = 5;

    private static BluetoothConnectionManager bluetoothConnectionManager;

    public static BluetoothConnectionManager instance() {
        if (bluetoothConnectionManager != null) {
            return bluetoothConnectionManager;
        }
        return null;
    }

    public static BluetoothConnectionManager instance(Context context, Handler handler) {

        if (bluetoothConnectionManager == null) {
            bluetoothConnectionManager = new BluetoothConnectionManager(context, handler);
        } else {
            bluetoothConnectionManager.setHandler(handler);
        }
        return bluetoothConnectionManager;
    }

    public BluetoothConnectionManager(Context context, Handler handler) {
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

        // Cancel any running thresd
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        setState(STATE_LISTEN);
        if (acceptThread == null) {
            acceptThread = new AcceptThread();
            acceptThread.start();
        }
    }

    // initiate connection to remote device
    public synchronized void connect(BluetoothDevice device) {
        // Cancel any thread
        if (state == STATE_CONNECTING) {
            if (connectThread != null) {
                connectThread.cancel();
                connectThread = null;
            }
        }

        // Cancel running thread
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        // Start the thread to connect with the given device
        connectThread = new ConnectThread(device);
        connectThread.start();
        setState(STATE_CONNECTING);
    }

    // manage Bluetooth connection
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        // Cancel the thread
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        // Cancel running thread
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        logger.info("connected Thread");

        // Start the thread to manage the connection and perform transmissions
        connectedThread = new ReadWriteThread(socket);
        connectedThread.start();

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

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }
        setState(STATE_NONE);
    }

    public void write(byte[] out) {
        ReadWriteThread r;
        synchronized (this) {
            if (state != STATE_CONNECTED)
                return;
            r = connectedThread;
        }
        r.write(out);
    }

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
                                connected(socket, socket.getRemoteDevice());
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
        private final BluetoothSocket socket;
        private final BluetoothDevice device;

        public ConnectThread(BluetoothDevice device) {
            logger.info("Inside Connect thread");
            this.device = device;
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
            connected(socket, device);
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    // runs during a connection with a remote device
    private class ReadWriteThread extends Thread {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public ReadWriteThread(BluetoothSocket socket) {
            this.bluetoothSocket = socket;
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

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream
            while (true) {
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
            }
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
    }
}
