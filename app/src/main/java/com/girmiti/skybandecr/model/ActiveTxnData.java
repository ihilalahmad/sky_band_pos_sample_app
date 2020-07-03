package com.girmiti.skybandecr.model;

import com.girmiti.skybandecr.transaction.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActiveTxnData {

    private String reqData;
    public TransactionType transactionType;
    private String terminalID;
    private String szSignature;
    private String ecrReferenceNo;
    private boolean registered;
    private boolean sessionStarted;
    private String resData;
    private String[] replacedArray;

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