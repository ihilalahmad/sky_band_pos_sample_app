package com.girmiti.skybandecr.sdk;

import com.girmiti.skybandecr.sdk.logger.Logger;

public class CLibraryLoad {

    private Logger logger = Logger.getNewLogger(ConnectionManager.class.getName());

    static {
        System.loadLibrary("ecrcore-lib");
    }

    private static CLibraryLoad instance;
    private CLibraryLoad() {

    }

    public static CLibraryLoad getInstance() {
        if ( instance == null ) {
            instance = new CLibraryLoad();
        }
        return instance;
    }

    public byte[] getPackData(String reqData, int tranType, String szSignature) {

        logger.debug("Calling Pack >>> " + reqData + " szSignature >>> " + szSignature);

        byte[] packedData = pack(reqData, tranType, szSignature, null);
        String packData = new String(packedData);

        logger.debug("Packed Data:" + packData);
        logger.debug("Sending Packed Data to Terminal>>>");

        return packedData;
    }

    public String getParseData(String respData) {

        logger.debug("Calling Parse >>> " + respData );

        byte[] parseData = parse(respData, null);
        String parsedData = new String(parseData);

        logger.debug("Parse data" + parsedData);

        return parsedData;
    }

    public native byte[] pack(String inputReqData, int transactionType, String szSignature, String szEcrBuffer);

    public native byte[] parse(String respData, String respOutData);
}
