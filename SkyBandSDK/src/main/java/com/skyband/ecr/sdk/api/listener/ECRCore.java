package com.skyband.ecr.sdk.api.listener;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface ECRCore {

    String doTCPIPTransaction(String ipAddress, int portNumber, String requestData, int transactionType, String signature) throws Exception;

    int doTCPIPConnection(String ipAddress, int portNumber) throws IOException;

    int doDisconnection() throws IOException;

    String computeSha256Hash(String combinedValue) throws NoSuchAlgorithmException;
}