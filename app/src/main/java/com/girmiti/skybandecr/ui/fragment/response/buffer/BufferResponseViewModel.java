package com.girmiti.skybandecr.ui.fragment.response.buffer;

import androidx.lifecycle.ViewModel;

import com.girmiti.skybandecr.sdk.logger.Logger;

public class BufferResponseViewModel extends ViewModel {

    private Logger logger = Logger.getNewLogger(BufferResponseViewModel.class.getName());

    String printResponsePurchase(String[] resp) {
        String pan;
        if(resp[4].length() > 14) {
            pan = maskPAn(resp[4]);
        } else {
            pan = resp[4];
        }

        return "Transaction type    : " + resp[1] + "\n" +
                "Response Code      : " + resp[2] + "\n" +
                "Response Message   : " + resp[3] + "\n" +
                "PAN Number         : " + pan + "\n" +
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
        return "TransactionType:" + resp[1] + "\n"
                + "ResponseCode:" + resp[2] + "\n"
                + "TerminalId:" + resp[3] + "\n";
    }

    String printResponseStartSession(String[] resp) {
        return "TransactionType:" + resp[1] + "\n"
                + "ResponseCode:" + resp[2] + "\n";
    }

    String printResponsePurchaseCashBack(String[] resp) {
        String pan;
        if(resp[4].length() > 14) {
            pan = maskPAn(resp[4]);
        } else {
            pan = resp[4];
        }
        return  "Transaction type: " + resp[1] + "\n" +
                "Response Code      : " + resp[2] + "\n" +
                "Response Message   : " + resp[3] + "\n" +
                "PAN Number         : " +  pan + "\n" +
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

    String printResponseReconcilation(String[] receiveDataArray) {

        String printSettlment = "Transaction Scheme transactionNo\n" +
                "-------------------- \n" +
                "Scheme Name                : SchemeName \n" +

                "Transaction Available Flag : TransactionAvailableFlag \n" +
                "Total Debit Count          : TotalDebitCount \n" +
                "Total Debit Amount         : TotalDebitAmount \n" +
                "Total Credit Count         : TotalCreditCount \n" +
                "Total Credit Amount        : TotalCreditAmount \n" +
                "NAQD Count                 : NAQDCount \n" +
                "NAQD Amount                : NAQDAmount \n" +
                "C/ADV Count                : CADVCount \n" +
                "C/ADV Amount               : CADVAmount \n" +
                "Auth Count                 : AuthCount \n" +
                "Auth Amount                : AuthAmount \n";
        int k = 6;
        StringBuilder printFinalReport1 = new StringBuilder("");
        String printSettlement1 = printSettlment;
        int totalSchemeLength = Integer.parseInt(receiveDataArray[6]);
        for (int i = 1; i <= totalSchemeLength; i++) {
            if (receiveDataArray[k + 2].equals("0")) {

                String printSettlementNO = "Transaction Scheme " + i + "\n" +
                        "-------------------- \n" +
                        "Scheme Name                : " + receiveDataArray[k + 1] + "\n" +
                        "<No Transaction> \n";
                k = k + 2;
                printFinalReport1.append(printSettlementNO);
                printSettlement1 = printSettlementNO;
            } else {
                printSettlement1 = printSettlement1.replace("transactionNo", String.valueOf(i));
                printSettlement1 = printSettlement1.replace("SchemeName", receiveDataArray[k + 1]);
                printSettlement1 = printSettlement1.replace("TransactionAvailableFlag", receiveDataArray[k + 2]);
                printSettlement1 = printSettlement1.replace("TotalDebitCount", receiveDataArray[k + 3]);
                printSettlement1 = printSettlement1.replace("TotalDebitAmount", receiveDataArray[k + 4]);
                printSettlement1 = printSettlement1.replace("TotalCreditCount", receiveDataArray[k + 5]);
                printSettlement1 = printSettlement1.replace("TotalCreditAmount", receiveDataArray[k + 6]);
                printSettlement1 = printSettlement1.replace("NAQDCount", receiveDataArray[k + 7]);
                printSettlement1 = printSettlement1.replace("NAQDAmount", receiveDataArray[k + 8]);
                printSettlement1 = printSettlement1.replace("CADVCount", receiveDataArray[k + 9]);
                printSettlement1 = printSettlement1.replace("CADVAmount", receiveDataArray[k + 10]);
                printSettlement1 = printSettlement1.replace("AuthCount", receiveDataArray[k + 11]);
                printSettlement1 = printSettlement1.replace("AuthAmount", receiveDataArray[k + 12]);
                k = k + 12;
                printFinalReport1.append(printSettlement1);
                printSettlement1 = printSettlment;
            }
        }
        return "Transaction type            : " + receiveDataArray[1] + "\n" +
                "Response Code              : " + receiveDataArray[2] + "\n" +
                "Response Message           : " + receiveDataArray[3] + "\n" +
                "Date Time Stamp           : " + receiveDataArray[4] + "\n" +
                "Application Version        : " + receiveDataArray[5] + "\n" +
                "Total Scheme Length        : " + receiveDataArray[6] + "\n" +
                printFinalReport1 +
                "ECR Transaction Reference Number: " + receiveDataArray[k + 1] + "\n" +
                "Signature                  : " + receiveDataArray[k + 2] + "\n";

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


    String  printResponseCheckStatus(String[] resp) {
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

    public String printResponseDefault(String[] resp) {
        return "Response Code               : " + resp[2] + "\n" +
                "Response Message            : " + resp[3] + "\n";
    }

    public String printResponsePrintDetailReport(String[] receiveDataArray) {

        String printSettlment = "Transaction Scheme transactionNo\n" +
                "-------------------- \n" +
                "Scheme Name                : SchemeName \n" +

                "Transaction Available Flag : TransactionAvailableFlag \n" +
                "Total Debit Count          : TotalDebitCount \n" +
                "Total Debit Amount         : TotalDebitAmount \n" +
                "Total Credit Count         : TotalCreditCount \n" +
                "Total Credit Amount        : TotalCreditAmount \n" +
                "NAQD Count                 : NAQDCount \n" +
                "NAQD Amount                : NAQDAmount \n" +
                "C/ADV Count                : CADVCount \n" +
                "C/ADV Amount               : CADVAmount \n" +
                "Auth Count                 : AuthCount \n" +
                "Auth Amount                : AuthAmount \n";
        int k = 6;
        StringBuilder printFinalReport1 = new StringBuilder("");
        String printSettlment1 = printSettlment;
        int totalSchemeLength = Integer.parseInt(receiveDataArray[6]);
        for (int i = 1; i <= totalSchemeLength; i++) {
            if (receiveDataArray[k + 2].equals("0")) {
                String printSettlmentNO = "Transaction Scheme " + i + "\n" +
                        "-------------------- \n" +
                        "Scheme Name                : " + receiveDataArray[k + 1] + "\n" +
                        "<No Transaction> \n";
                k = k + 2;
                printFinalReport1.append(printSettlmentNO);
                printSettlment1 = printSettlmentNO;
            } else {
                printSettlment1 = printSettlment1.replace("transactionNo", String.valueOf(i));
                printSettlment1 = printSettlment1.replace("SchemeName", receiveDataArray[k + 1]);
                printSettlment1 = printSettlment1.replace("TransactionAvailableFlag", receiveDataArray[k + 2]);
                printSettlment1 = printSettlment1.replace("TotalDebitCount", receiveDataArray[k + 3]);
                printSettlment1 = printSettlment1.replace("TotalDebitAmount", receiveDataArray[k + 4]);
                printSettlment1 = printSettlment1.replace("TotalCreditCount", receiveDataArray[k + 5]);
                printSettlment1 = printSettlment1.replace("TotalCreditAmount", receiveDataArray[k + 6]);
                printSettlment1 = printSettlment1.replace("NAQDCount", receiveDataArray[k + 7]);
                printSettlment1 = printSettlment1.replace("NAQDAmount", receiveDataArray[k + 8]);
                printSettlment1 = printSettlment1.replace("CADVCount", receiveDataArray[k + 9]);
                printSettlment1 = printSettlment1.replace("CADVAmount", receiveDataArray[k + 10]);
                printSettlment1 = printSettlment1.replace("AuthCount", receiveDataArray[k + 11]);
                printSettlment1 = printSettlment1.replace("AuthAmount", receiveDataArray[k + 12]);
                k = k + 12;
                printFinalReport1.append(printSettlment1);
                printSettlment1 = printSettlment;
            }
        }
        return "Transaction type            : " + receiveDataArray[1] + "\n" +
                "Response Code              : " + receiveDataArray[2] + "\n" +
                "Response Message           : " + receiveDataArray[3] + "\n" +
                "Date Time Stamp           : " + receiveDataArray[4] + "\n" +
                "Application Version        : " + receiveDataArray[5] + "\n" +
                "Total Scheme Length        : " + receiveDataArray[6] + "\n" +
                printFinalReport1 +
                "ECR Transaction Reference Number: " + receiveDataArray[k + 1] + "\n" +
                "Signature                  : " + receiveDataArray[k + 2] + "\n";

    }

    public String printResponseSummaryReport(String[] receiveDataArray) {

        logger.debug("arrayLength>>"+receiveDataArray.length);

        String printSummaryReportString1 = "Transaction Number transactionNumberHead\n" + "-------------------- \n"
                + "Transaction Type            :TransactionType1  \n" + "Date                        :Date1 \n"
                + "RRN                         :RRN1 \n" + "Transaction Amount          :TransactionAmount1 \n"
                + "Claim                       :Claim1 \n" + "State                       :State1 \n"
                + "Time                        :Time1 \n" + "PAN Number                  :PANNumber1 \n"
                + "authCode                    :authCode1 \n" + "transactionNumber           :transactionNumber1 \n";
        StringBuilder htmlSummaryReport1 ;
        String printSummaryReportString = printSummaryReportString1;
        StringBuilder summaryFinalReport1 = new StringBuilder();
        int k = 7;
        int transactionsLength = Integer.parseInt(receiveDataArray[6]);
        logger.debug("transaction Length>>" + transactionsLength);

        if(transactionsLength > 17) {
            transactionsLength = 17;
        }

        for (int a = 1; a <= transactionsLength; a++) {
            printSummaryReportString = printSummaryReportString.replace("transactionNumberHead", String.valueOf(a));
            printSummaryReportString = printSummaryReportString.replace("TransactionType1", receiveDataArray[k]);
            printSummaryReportString = printSummaryReportString.replace("Date1", receiveDataArray[k + 1]);
            printSummaryReportString = printSummaryReportString.replace("RRN1", receiveDataArray[k + 2]);
            printSummaryReportString = printSummaryReportString.replace("TransactionAmount1", receiveDataArray[k + 3]);
            printSummaryReportString = printSummaryReportString.replace("Claim1", receiveDataArray[k + 4]);
            printSummaryReportString = printSummaryReportString.replace("State1", receiveDataArray[k + 5]);
            printSummaryReportString = printSummaryReportString.replace("Time1", receiveDataArray[k + 6]);
            printSummaryReportString = printSummaryReportString.replace("PANNumber1",  receiveDataArray[k + 7]);
            printSummaryReportString = printSummaryReportString.replace("authCode1", receiveDataArray[k + 8]);
            printSummaryReportString = printSummaryReportString.replace("transactionNumber1", receiveDataArray[k + 9]);
            k = k + 10;
            htmlSummaryReport1 = new StringBuilder(printSummaryReportString);
            summaryFinalReport1.append(htmlSummaryReport1.toString());
            logger.debug("Length" + a + ")  " + htmlSummaryReport1);
            logger.debug("index"+k);
            printSummaryReportString = printSummaryReportString1;
        }
        logger.debug("Final Report>>" + summaryFinalReport1);
        return "Transaction type            : " + receiveDataArray[1] + "\n" + "Response Code               : "
                + receiveDataArray[2] + "\n" + "Response Message            : " + receiveDataArray[3] + "\n"
                + "Date Time Stamp             : " + receiveDataArray[4] + "\n" + "Transaction Requests Count  : "
                + receiveDataArray[5] + "\n" + "Total Transactions Length   : " + receiveDataArray[6] + "\n"
                + summaryFinalReport1;/* + "ECR Transaction Reference Number  : " + receiveDataArray[k] + "\n"
                + "Signature    : " + receiveDataArray[k + 1] + "\n";*/
    }

    public String printResponseReversal(String[] resp) {
        String pan;
        if(resp[4].length() > 14) {
            pan = maskPAn(resp[4]);
        } else {
            pan = resp[4];
        }
        return "Transaction type: " + resp[1] + "\n" +
                "Response Code      : " + resp[2] + "\n" +
                "Response Message   : " + resp[3] + "\n" +
                "PAN Number         : " + pan + "\n" +
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
                "Merchant Category Code : " + resp[20] + "\n" +
                "Transaction Type   : " + resp[21] + "\n" +
                "Scheme Label       : " + resp[22] + "\n" +
                "Store and Cashier Info : " + resp[23] + "\n" +
                "Product Info       : " + resp[24] + "\n" +
                "Application Version: " + resp[25] + "\n" +
                "ECR Transaction Reference Number: " + resp[26] + "\n" +
                "Signature          : " + resp[27] + "\n";
    }

    private String maskPAn(String s) {
        return s.substring(0, 5) + "******" + s.substring(s.length()-4);
    }
}


