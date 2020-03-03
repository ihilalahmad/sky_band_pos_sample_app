package com.raj.skybandecr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Logger;

public class SocketHostConnector {

    private Logger logger;
    private Socket socket;
    private OutputStream output;
    private InputStream inputStream;

    private String SERVER_IP = "192.168.7.79";
    private int SERVER_PORT = 800;
    private final int SOCKET_TIMEOUT = 10000;


    public void createConnection() throws IOException {

        socket = new Socket();
        socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT), SOCKET_TIMEOUT);
        socket.setSoTimeout(SOCKET_TIMEOUT);

        output = socket.getOutputStream();
        inputStream = socket.getInputStream();

    }

    public void cleanup() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.severe("IOException: inputStream");
            }
        }
        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
                logger.severe("IOException: output");
            }
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                logger.severe("IOException: socket");
            }
        }
        inputStream = null;
        output = null;
        socket = null;
    }
}
