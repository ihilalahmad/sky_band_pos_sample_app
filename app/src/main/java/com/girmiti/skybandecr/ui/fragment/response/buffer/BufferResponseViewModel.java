package com.girmiti.skybandecr.ui.fragment.response.buffer;

import androidx.lifecycle.ViewModel;

public class BufferResponseViewModel extends ViewModel {

    String printResponsePurchase(String[] resp) {
        return "Transaction type: " + resp[1] + "\n" +
                "Response Code      : " + resp[2] + "\n" +
                "Response Message   : " + resp[3] + "\n" +
                "PAN Number         : " + resp[4] + "\n" +
                "Transaction Amount : " + resp[5] + "\n" +
                "Stan No            : " + resp[6] + "\n" +
                "Date & Time        : " + resp[7] + "\n" +
                "Card Exp Date      : " + resp[8] + "\n" +
                "RRN                : " + resp[9] + "\n" +
                "Auth Code          : " + resp[10] + "\n" +
                "TID                : " + resp[11] + "\n" +
                "MID                : " + resp[12] + "\n" +
                "Batch No           : " + resp[13] + "\n" +
                "AID                : " + resp[14] + "\n" +
                "Application Cryptogram : " + resp[15] + "\n" +
                "CID                : " + resp[16] + "\n" +
                "CVR                : " + resp[17] + "\n" +
                "TVR                : " + resp[18] + "\n" +
                "TSI                : " + resp[19] + "\n" +
                "Card Entry Mode    : " + resp[20] + "\n" +
                "Merchant Category Code : " + resp[21] + "\n" +
                "Transaction Type   : " + resp[22] + "\n" +
                "Scheme Label       : " + resp[23] + "\n" +
                "Store and Cashier Info : " + resp[24] + "\n" +
                "Product Info       : " + resp[25] + "\n" +
                "Application Version: " + resp[26] + "\n" +
                "ECR Transaction Reference Number: " + resp[27] + "\n" +
                "Signature          : " + resp[28] + "\n";
    }

    String printResponseRegister(String[] resp) {
        return "TransactionType:" + resp[1] + "\n" + "ResponseCode:" + resp[2] + "\n" + "TerminalId:" + resp[3] + "\n";
    }

    String printResponseStartSession(String[] resp) {
        return "TransactionType:" + resp[1] + "\n" + "ResponseCode:" + resp[2] + "\n";
    }

    String printResponsePurchaseCashBack(String[] resp) {
        return "Transaction type: " + resp[1] + "\n" +
                "Response Code      : " + resp[2] + "\n" +
                "Response Message   : " + resp[3] + "\n" +
                "PAN Number         : " + resp[4] + "\n" +
                "Transaction Amount : " + resp[5] + "\n" +
                "Cash Back Amount   : " + resp[6] + "\n" +
                "Total Amount       : " + resp[7] + "\n" +
                "StanNo      		: " + resp[8] + "\n" +
                "Date&Time          : " + resp[9] + "\n" +
                "CardExpDate        : " + resp[10] + "\n" +
                "RRN                : " + resp[11] + "\n" +
                "AuthCode           : " + resp[12] + "\n" +
                "TID           		: " + resp[13] + "\n" +
                "MID                : " + resp[14] + "\n" +
                "BatchNo			: " + resp[15] + "\n" +
                "AID                : " + resp[16] + "\n" +
                "Application Cryptogram : " + resp[17] + "\n" +
                "CID                : " + resp[18] + "\n" +
                "CVR               	: " + resp[19] + "\n" +
                "TVR			    : " + resp[20] + "\n" +
                "TSI				: " + resp[21] + "\n" +
                "CardEntryMode	   	: " + resp[22] + "\n" +
                "Merchant Category Code: " + resp[23] + "\n" +
                "Transaction Type 	: " + resp[24] + "\n" +
                "SchemeLabel       	: " + resp[25] + "\n" +
                "Store and Cashier Info: " + resp[26] + "\n" +
                "ProductInfo		: " + resp[27] + "\n" +
                "ApplicationVersion : " + resp[28] + "\n" +
                "EcrTransactionReference:" + resp[29] + "\n" +
                "Signature			:" + resp[30] + "\n";
    }

    String printResponseReconcilation(String[] resp) {
        return "Transaction type: " + resp[1] + "\n" +
                "Response Code      : " + resp[2] + "\n" +
                "Response Message   : " + resp[3] + "\n" +
                "Date&Time          : " + resp[4] + "\n" +
                "TotalSchemeLength  :" + resp[5] + "\n" +
                "SchemeName			:" + resp[6] + "\n" +
                "TransactionAvaliableFlag:" + resp[7] + "\n" +
                "Total Debit Count	:" + resp[8] + "\n" +
                "Total Debit Amount :" + resp[9] + "\n" +
                "Total Credit Count :" + resp[10] + "\n" +
                "Total Credit Amount:" + resp[11] + "\n" +
                "NAQD Count			:" + resp[12] + "\n" +
                "NAQD Amount		:" + resp[13] + "\n" +
                "C/ADV Count		:" + resp[14] + "\n" +
                "C/ADV Amount		:" + resp[15] + "\n" +
                "Auth Count			:" + resp[16] + "\n" +
                "Auth Amount		:" + resp[17] + "\n" +
                "ECR Transaction Reference Number:" + resp[18] + "\n" +
                "Signature			:" + resp[19] + "\n";
    }

    String printResponseParameterDownload(String[] resp) {
        return "Transaction type: " + resp[1] + "\n" +
                "Response Code      : " + resp[2] + "\n" +
                "Response Message   : " + resp[3] + "\n" +
                "Date&Time          : " + resp[4] + "\n" +
                "ECR Transaction Reference Number:" + resp[5] + "\n" +
                "Signature			:" + resp[6] + "\n";
    }

    String printResponseGetParameter(String[] resp) {
        return "Transaction type: " + resp[1] + "\n" +
                "Response Code      : " + resp[2] + "\n" +
                "Response Message   : " + resp[3] + "\n" +
                "Date&Time          : " + resp[4] + "\n" +
                "Vendor ID 			:" + resp[5] + "\n" +
                "Vendor Terminal type:" + resp[6] + "\n" +
                "TRSM ID			:" + resp[7] + "\n" +
                "Vendor Key Index	:" + resp[8] + "\n" +
                "SAMA Key Index		:" + resp[9] + "\n" +
                "ECR Transaction Reference Number:" + resp[10] + "\n" +
                "Signature			:" + resp[11] + "\n";
    }


    String printResponseCheckStatus(String[] resp) {
        return "Transaction type: " + resp[1] + "\n" +
                "Response Code      : " + resp[2] + "\n" +
                "Response Message   : " + resp[3] + "\n" +
                "Date&Time          : " + resp[4] + "\n" +
                "PreviousTransactionResponse:" + resp[5] + "\n" +
                "PreviousEcrNo		:" + resp[6] + "\n" +
                "ECR Transaction Reference Number:" + resp[7] + "\n" +
                "Signature			:" + resp[8] + "\n";
    }

    String printResponseBillPayment(String[] resp) {
        return "Transaction type: " + resp[1] + "\n" +
                "Response Code      : " + resp[2] + "\n" +
                "Response Message   : " + resp[3] + "\n";
    }
}


