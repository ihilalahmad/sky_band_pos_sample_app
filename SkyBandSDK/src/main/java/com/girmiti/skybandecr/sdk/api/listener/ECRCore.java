package com.girmiti.skybandecr.sdk.api.listener;

import java.io.IOException;

public interface ECRCore {

    String doTCPIPTransaction(String ipAddress, int portNumber, String requestData, int transactionType, String signature) throws Exception;

    int doTCPIPConnection(String ipAddress, int portNumber) throws IOException;

    int doDisconnection() throws IOException;
}