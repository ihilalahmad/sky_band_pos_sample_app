package com.girmiti.skybandecr.sdk.api;

import com.girmiti.skybandecr.sdk.CLibraryLoad;
import com.girmiti.skybandecr.sdk.ConnectionManager;
import com.girmiti.skybandecr.sdk.api.listener.ECRCore;
import com.girmiti.skybandecr.sdk.logger.Logger;

import java.io.IOException;

public class ECRImpl implements ECRCore {

    private static ECRCore ecrCore;
    private Logger logger = Logger.getNewLogger(ECRImpl.class.getName());

    public static ECRCore getConnectInstance() {
        if (ecrCore == null) {
            ecrCore = new ECRImpl();
            return ecrCore;
        }
        return ecrCore;
    }

    @Override
    public String doTCPIPTransaction(String ipAddress, int portNumber, String requestData, int transactionType, String signature) throws Exception {

        String terminalResponse = "";
        byte[] packData = CLibraryLoad.getInstance().getPackData(requestData, transactionType, signature);

        if (ConnectionManager.Instance() != null && ConnectionManager.Instance().isConnected()) {
            try {
                terminalResponse = ConnectionManager.Instance().sendAndRecv(packData);
            } catch (IOException e) {
                try {
                    ECRImpl.getConnectInstance().doDisconnection();
                    ECRImpl.getConnectInstance().doTCPIPConnection(ipAddress, portNumber);
                    throw new Exception("0");
                } catch (IOException ex) {
                    logger.severe("Exception in Disconnect and connect>>", ex);
                    throw new Exception("1");
                }
            }
        } else {
            throw new Exception("2");
        }
        //Library parsing is not giving correct response
      /*  String parseData = CLibraryLoad.getInstance().getParseData(terminalResponse);
        parseData = parseData.replace("�", ";");
        logger.debug("After Replace  with ;>>"+parseData);
        return parseData;*/
        terminalResponse = terminalResponse.replace("�", ";");
        logger.debug("After Replace  with ;>>" + terminalResponse);

        return terminalResponse;
    }

    @Override
    public int doTCPIPConnection(String ipAddress, int portNumber) throws IOException {
        if (ConnectionManager.Instance(ipAddress, portNumber).isConnected()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int doDisconnection() throws IOException {
        if (ConnectionManager.Instance() != null) {
            ConnectionManager.Instance().disconnect();
            return 0;
        } else {
            return 1;
        }
    }
}