package com.girmiti.skybandecr.sdk;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Logger;

public class SocketHostConnector {

    private Logger logger;
    public Socket socket;
    private OutputStream output;
    private InputStream input;
    private String SERVER_IP ;
    private int SERVER_PORT;

    static {

        System.loadLibrary("jniWrapper");
    }

    public SocketHostConnector(String ip, int port) {

        SERVER_IP=ip;
        SERVER_PORT=port;
    }

    public String createConnection() throws IOException {

        socket = new Socket();
        socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
        Log.d("msg", "connected");
        return "connected";
    }

    public void cleanup() throws IOException {

        if (input != null) {
            try {
                input.close();
                Log.e("msg", "inputStream Closed");
            } catch (IOException e) {
                logger.severe("IOException: inputStream");
            }
        }

        if (output != null) {
            output.close();
            Log.e("msg", "OutputStream Closed");
        }

        if (socket != null) {
            try {
                socket.close();
                Log.e("msg", "Socket successfully Closed");
            } catch (IOException e) {
                logger.severe("IOException: socket");
            }
        }
        input = null;
        output = null;
        socket = null;
    }

    public String sendPacketToTerminal(byte[] in) throws IOException {
        System.out.println("Is Connected");

        output = socket.getOutputStream();
        input = socket.getInputStream();
        output.write(in);
        output.flush();
        Log.e("msg", "sent");

        byte[] responseBytes = new byte[15000];
        int noOfBytesRead = input.read(responseBytes);
        System.out.println("Reading data >>: " + noOfBytesRead);

        byte[] finalResponse = new byte[noOfBytesRead];
        System.arraycopy(responseBytes, 0, finalResponse, 0, noOfBytesRead);

        for (int i = 0; i < finalResponse.length; i++) {
            System.out.println("Output" + i + "<<" + finalResponse[i]);

        }

        String a = new String(finalResponse);
        System.out.println("Final Output is<<" + a);
        System.out.println("Final Output2 is<<" + Arrays.toString(finalResponse));
        return a;
    }

    public String tcpIpSend(String reqData, int tranType, String szEcrBuffer) throws IOException {

       byte[] packedData= pack(reqData,tranType,szEcrBuffer);
       String terminalResponse=sendPacketToTerminal(packedData);
       return terminalResponse;
    }

    public native byte[] pack(String inputReqData, int transactionType, String szEcrBuffer);
}
