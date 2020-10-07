package com.skyband.ecr.model;

import com.skyband.ecr.transaction.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActiveTxnData {

    private String reqData;
    private TransactionType transactionType;
    private String terminalID;
    private String szSignature;
    private String ecrReferenceNo;
    private boolean registered;
    private boolean sessionStarted;
    private String resData;
    private String[] replacedArray;
    private String[] summaryReportArray;
    private int position;

    private static ActiveTxnData activeTxnData;

    private ActiveTxnData() {

    }

    public static ActiveTxnData getInstance() {

        if (activeTxnData == null) {
            activeTxnData = new ActiveTxnData();
        }

        return activeTxnData;
    }

}
