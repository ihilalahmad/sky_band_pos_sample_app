package com.girmiti.skybandecr.sdk;

import com.girmiti.skybandecr.sdk.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import lombok.Getter;
import lombok.Setter;

public class ConnectionManager {

    private Logger logger = Logger.getNewLogger(ConnectionManager.class.getName());

    private Socket socket;
    private OutputStream output;
    private InputStream input;
    @Getter
    private static String serverIp;
    @Setter
    private static int serverPort;
    private final int SOCKET_TIMEOUT = 120000;
    private static ConnectionManager socketHostConnector;

    public static ConnectionManager Instance(String ip, int port) throws IOException {

        // First time
        if (socketHostConnector == null) {
            socketHostConnector = new ConnectionManager(ip, port);
            socketHostConnector.createConnection();
        } else {
            socketHostConnector.cleanup();
            socketHostConnector = new ConnectionManager(ip, port);
            socketHostConnector.createConnection();
        }

        return socketHostConnector;
    }

    /**
     * This use only for exiting connection access
     *
     * @return
     * @throws IOException
     */
    public static ConnectionManager Instance() {

        if (socketHostConnector != null) {
            return socketHostConnector;
        }

        return null;
    }

    private Socket getSocket() {
        return socket;
    }

    private ConnectionManager(String ip, int port) {

        serverIp = ip;
        serverPort = port;
    }

    public void createConnection() throws IOException {

        logger.debug(getClass() + "::Connecting to IP: " + serverIp + " and port: " + serverPort);
        socket = new Socket();
        socket.connect(new InetSocketAddress(serverIp, serverPort), SOCKET_TIMEOUT);
        socket.setSoTimeout(SOCKET_TIMEOUT);
        logger.debug(getClass() + "::" + "Created connection : Ip" + serverIp + "port:" + serverPort);
    }

    public void disconnect() throws IOException {
        if (socket != null && socket.isConnected()) {
            cleanup();
        }
    }

    public void cleanup() throws IOException {

        if (input != null) {

            try {
                input.close();
                logger.debug("InputStream Closed>>>");
            } catch (IOException e) {
                logger.severe("IOException: inputStream", e);
            }
        }

        if (output != null) {

            output.close();
            logger.debug("OutputStream Closed>>>");
        }

        if (socket != null) {

            try {
                socket.close();
                logger.debug("Socket successfully Closed>>>");
            } catch (IOException e) {
                logger.severe("IOException: socket", e);
            }
        }

        input = null;
        output = null;
        socket = null;
    }

    public String sendAndRecv(byte[] in) throws IOException {

        output = socket.getOutputStream();
        input = socket.getInputStream();

        output.write(in);
        output.flush();

        logger.debug("Write Data >>:" + new String(in));

        byte[] responseBytes = new byte[15000];

        int noOfBytesRead = input.read(responseBytes);

        logger.debug("Reading data >>: " + noOfBytesRead);

        if (noOfBytesRead <= 0) {
            throw new IOException();
        }


        byte[] finalResponse = new byte[noOfBytesRead];
        System.arraycopy(responseBytes, 0, finalResponse, 0, noOfBytesRead);
        String terminalResponse = new String(finalResponse);

        logger.debug("Response Data >>: " + terminalResponse);

        return terminalResponse;
    }

    public boolean isConnected() {

        if (socket != null) {
            return socket.isConnected();
        }

        return false;
    }
}
