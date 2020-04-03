package com.girmiti.skybandecr.sdk;

import com.girmiti.skybandecr.sdk.logger.Logger;

public class CLibraryLoad {

    private Logger logger = Logger.getNewLogger(SocketHostConnector.class.getName());

    static {
        System.loadLibrary("jniWrapper");
    }

    public byte[] getPackData(String reqData, int tranType, String szSignature, String szEcrBuffer) {
        System.loadLibrary("jniWrapper");
        logger.debug("Calling Pack >>>");

        byte[] packedData = pack(reqData, tranType, szSignature, szEcrBuffer);
        String packData = new String(packedData);

        logger.debug("Packed Data:" + packData);
        logger.debug("Sending Packed Data to Terminal>>>");

        return packedData;
    }

    public native byte[] pack(String inputReqData, int transactionType, String szSignature, String szEcrBuffer);
}
