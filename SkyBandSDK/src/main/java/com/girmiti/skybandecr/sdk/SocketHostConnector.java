package com.girmiti.skybandecr.sdk;

import com.girmiti.skybandecr.sdk.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketHostConnector {

    private Logger logger = Logger.getNewLogger(SocketHostConnector.class.getName());

    public  Socket socket;
    private OutputStream output;
    private InputStream input;
    private String SERVER_IP ;
    private int SERVER_PORT;
    private static final int SOCKET_TIMEOUT = 9000;

    static {
        System.loadLibrary("jniWrapper");
    }

    public SocketHostConnector(String ip, int port) {

        SERVER_IP=ip;
        SERVER_PORT=port;
    }

    public void createConnection() throws IOException {

        logger.debug(getClass() + "::Connecting to IP: " + SERVER_IP+ " and port: " + SERVER_PORT);
        socket = new Socket();
        socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
        logger.debug(getClass() + "::" + "Created connection");

    }

    public void cleanup() throws IOException {

        if (input != null) {

            try {
                input.close();
                logger.debug( "InputStream Closed>>>");
            } catch (IOException e) {
                logger.severe("IOException: inputStream",e);
            }
        }

        if (output != null) {

            output.close();
            logger.debug( "OutputStream Closed>>>");
        }

        if (socket != null) {

            try {
                socket.close();
                logger.debug("Socket successfully Closed>>>");
            } catch (IOException e) {
                logger.severe("IOException: socket",e);
            }
        }
        input = null;
        output = null;
        socket = null;
    }

    public String sendPacketToTerminal(byte[] in) throws IOException {

        output = socket.getOutputStream();
        input = socket.getInputStream();

        output.write(in);
        output.flush();

        logger.debug("Write Data >>:" + new String(in));

        byte[] responseBytes = new byte[15000];
        int noOfBytesRead = input.read(responseBytes);

        logger.debug("Reading data >>: " + noOfBytesRead);

        byte[] finalResponse = new byte[noOfBytesRead];
        System.arraycopy(responseBytes, 0, finalResponse, 0, noOfBytesRead);
        String a = new String(finalResponse);

        logger.debug("Response Data >>: " + a);

        return a;
    }

    public String tcpIpSend(String reqData, int tranType,String szSignature, String szEcrBuffer) throws IOException {

         String respOutData = " ";
        logger.debug("Calling Pack >>>");

       byte[] packedData= pack(reqData,tranType,szSignature,szEcrBuffer);
       String packData=new String(packedData);

       logger.debug("Packed Data:"+ packData);
       logger.debug("Sending Packed Data to Terminal>>>");

       String terminalResponse = sendPacketToTerminal(packedData);
        logger.debug("Terminal Response:"+ terminalResponse);

       /* byte[] parseData2= parse(terminalResponse,respOutData);
        String parsedData=new String(parseData2);
        logger.debug("parsed Data:"+ parsedData);*/

       return terminalResponse;

    }

  /*  public String[] parse(String terminalResponse) {

        String[] splittedArray = terminalResponse.split("�");

        return splittedArray;
    }*/

    public native byte[] pack(String inputReqData, int transactionType,String szSignature, String szEcrBuffer);
  //  public native byte[] parse(String respData , String respOutData);
}
