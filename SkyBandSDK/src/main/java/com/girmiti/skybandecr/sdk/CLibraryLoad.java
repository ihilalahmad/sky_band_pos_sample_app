package com.girmiti.skybandecr.sdk;

import com.girmiti.skybandecr.sdk.logger.Logger;

public class CLibraryLoad {

    private Logger logger = Logger.getNewLogger(ConnectionManager.class.getName());

    static {
        System.loadLibrary("ecrcore-lib");
    }

    public byte[] getPackData(String reqData, int tranType, String szSignature, String szEcrBuffer) {

        // For for test

            logger.debug("Calling Pack >>> " + reqData + " szSignature >>> " + szSignature + " szEcrBuffer >>> " + szEcrBuffer);

            byte[] packedData = pack(reqData, tranType, szSignature, szEcrBuffer);
            String packData = new String(packedData);

            logger.debug("Packed Data:" + packData);
            logger.debug("Sending Packed Data to Terminal>>>");

         return packedData;
    }

    public native byte[] pack(String inputReqData, int transactionType, String szSignature, String szEcrBuffer);
}
