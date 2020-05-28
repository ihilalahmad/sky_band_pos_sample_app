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
        return "TransactionType:" + resp[1] + "\n"
                + "ResponseCode:" + resp[2] + "\n"
                + "TerminalId:" + resp[3] + "\n";
    }

    String printResponseStartSession(String[] resp) {
        return "TransactionType:" + resp[1] + "\n"
                + "ResponseCode:" + resp[2] + "\n";
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
        int k = 5;
        StringBuilder printFinalReport1 = new StringBuilder("");
        int totalSchemeLength = Integer.parseInt(receiveDataArray[5]);
        for (int i = 1; i <= totalSchemeLength; i++)
        {
            if (receiveDataArray[k + 2].equals("0"))
            {
                String printSettlmentNO = "Transaction Scheme " + i + "\n" +
                        "-------------------- \n" +
                        "Scheme Name                : " + receiveDataArray[k + 1] + "\n" +
                        "<No Transaction> \n";
                k = k + 2;
                printFinalReport1.append(printSettlmentNO);
            }
            else
            {
                printSettlment = printSettlment.replace("transactionNo", String.valueOf(i));
                printSettlment = printSettlment.replace("SchemeName", receiveDataArray[k + 1]);
                printSettlment = printSettlment.replace("TransactionAvailableFlag", receiveDataArray[k + 2]);
                printSettlment = printSettlment.replace("TotalDebitCount", receiveDataArray[k + 3]);
                printSettlment = printSettlment.replace("TotalDebitAmount", receiveDataArray[k + 4]);
                printSettlment = printSettlment.replace("TotalCreditCount", receiveDataArray[k + 5]);
                printSettlment = printSettlment.replace("TotalCreditAmount", receiveDataArray[k + 6]);
                printSettlment = printSettlment.replace("NAQDCount", receiveDataArray[k + 7]);
                printSettlment = printSettlment.replace("NAQDAmount", receiveDataArray[k + 8]);
                printSettlment = printSettlment.replace("CADVCount", receiveDataArray[k + 9]);
                printSettlment = printSettlment.replace("CADVAmount", receiveDataArray[k + 10]);
                printSettlment = printSettlment.replace("AuthCount", receiveDataArray[k + 11]);
                printSettlment = printSettlment.replace("AuthAmount", receiveDataArray[k + 12]);
                k = k + 12;
                printFinalReport1.append(printSettlment);
            }
        }
        return "Transaction type            : " + receiveDataArray[1] + "\n" +
                "Response Code              : " + receiveDataArray[2] + "\n" +
                "Response Message           : " + receiveDataArray[3] + "\n" +
                "Date Time Stamp           : " + receiveDataArray[4] + "\n" +
                "Total Scheme Length        : " + receiveDataArray[5] + "\n" +
                printFinalReport1 +
                "ECR Transaction Reference Number: " + receiveDataArray[k + 1] + "\n" +
                "Signature                  : " + receiveDataArray[k + 2] + "\n";

    }

    public String printResponseSummaryReport(String[] receiveDataArray) {

        StringBuilder SummaryFinalReport1 = new StringBuilder();
        String printSummaryReportString1 = "Transaction Number transactionNumberHead\n" +
                "-------------------- \n" +
                "Transaction Type            :TransactionType1  \n" +
                "Date                        :Date1 \n" +
                "RRN                         :RRN1 \n" +
                "Transaction Amount          :TransactionAmount1 \n" +
                "State                       :State1 \n" +
                "Time                        :Time1 \n" +
                "PAN Number                  :PANNumber1 \n" +
                "authCode                    :authCode1 \n" +
                "transactionNumber           :transactionNumber1 \n";
        String printSummaryReportString = printSummaryReportString1;

        int k = 7;
        for (int a = 1; a <= receiveDataArray[6].length(); a++)
        {
            printSummaryReportString = printSummaryReportString.replace("transactionNumberHead", String.valueOf(a));
            printSummaryReportString = printSummaryReportString.replace("TransactionType1", receiveDataArray[k]);
            printSummaryReportString = printSummaryReportString.replace("Date1", receiveDataArray[k + 1]);
            printSummaryReportString = printSummaryReportString.replace("RRN1", receiveDataArray[k + 2]);
            printSummaryReportString = printSummaryReportString.replace("TransactionAmount1", receiveDataArray[k + 3]);
            printSummaryReportString = printSummaryReportString.replace("State1", receiveDataArray[k + 4]);
            printSummaryReportString = printSummaryReportString.replace("Time1", receiveDataArray[k + 5]);
            printSummaryReportString = printSummaryReportString.replace("PANNumber1", receiveDataArray[k + 6]);
            printSummaryReportString = printSummaryReportString.replace("authCode1", receiveDataArray[k + 7]);
            printSummaryReportString = printSummaryReportString.replace("transactionNumber1", receiveDataArray[k + 8]);
            k = k + 9;
            SummaryFinalReport1.append(printSummaryReportString);
            printSummaryReportString = printSummaryReportString1;
        }

        return "Transaction type            : " + receiveDataArray[1] + "\n" +
                "Response Code               : " + receiveDataArray[2] + "\n" +
                "Response Message            : " + receiveDataArray[3] + "\n" +
                "Date Time Stamp             : " + receiveDataArray[4] + "\n" +
                "Transaction Requests Count  : " + receiveDataArray[5] + "\n" +
                "Total Transactions Length   : " + receiveDataArray[6] + "\n" +
                SummaryFinalReport1 +
                "ECR Transaction Reference Number  : " + receiveDataArray[k] + "\n" +
                "Signature    : " + receiveDataArray[k+1] + "\n";
    }
}


