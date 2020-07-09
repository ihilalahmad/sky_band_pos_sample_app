package com.girmiti.skybandecr.ui.fragment.response.buffer;

import androidx.lifecycle.ViewModel;

import com.girmiti.skybandecr.sdk.logger.Logger;

public class BufferResponseViewModel extends ViewModel {

    private Logger logger = Logger.getNewLogger(BufferResponseViewModel.class.getName());

    String printResponsePurchase(String[] resp) {
        String pan;
        if (resp[4].length() > 14) {
            pan = maskPAn(resp[4]);
        } else {
            pan = resp[4];
        }

        return "Transaction type" + "\t" + "\t" + "\t" + ": " + resp[1] + "\n" +
                "Response Code" + "\t" + "\t" + "\t" + ": " + resp[2] + "\n" +
                "Response Message" + "\t" + "\t" + "\t" + ": " + resp[3] + "\n" +
                "PanNumber" + "\t" + "\t" + "\t" + "\t" + ": " + pan + "\n" +
                "Transaction Amount" + "\t" + "\t" + "\t" + ": " + resp[5] + "\n" +
                "Buss Code" + "\t" + "\t" + "\t" + "\t" + ": " + resp[6] + "\n" +
                "StanNo" + "\t" + "\t" + "\t" + "\t" + "\t" + ": " + resp[7] + "\n" +
                "Date&Time " + "\t" + "\t" + "\t" + ": " + resp[8] + "\n" +
                "Card Exp Date" + "\t" + "\t" + "\t" + "\t" + ": " + resp[9] + "\n" +
                "RRN" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + ": " + resp[10] + "\n" +
                "Auth Code" + "\t" + "\t" + "\t" + "\t" + ": " + resp[11] + "\n" +
                "TID" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + ": " + resp[12] + "\n" +
                "MID" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + ": " + resp[13] + "\n" +
                "Batch No" + "\t" + "\t" + "\t" + "\t" + "\t" + ": " + resp[14] + "\n" +
                "AID" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + ": " + resp[15] + "\n" +
                "Application Cryptogram" + "\t" + "\t" + ": " + resp[16] + "\n" +
                "CID" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + ": " + resp[17] + "\n" +
                "CVR" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + ": " + resp[18] + "\n" +
                "TVR" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + ": " + resp[19] + "\n" +
                "TSI" + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + ": " + resp[20] + "\n" +
                "Card Entry Mode" + "\t" + "\t" + "\t" + ": " + resp[21] + "\n" +
                "Merchant Category Code" + "\t" + "\t" + ": " + resp[22] + "\n" +
                "Transaction Type" + "\t" + "\t" + "\t" + ": " + resp[23] + "\n" +
                "Scheme Label" + "\t" + "\t" + "\t" + "\t" + ": " + resp[24] + "\n" +
                "Store and Cashier Info" + "\t" + "\t" + ": " + resp[25] + "\n" +
                "Product Info" + "\t" + "\t" + "\t" + "\t" + ": " + resp[26] + "\n" +
                "Application Version" + "\t" + "\t" + "\t" + ": " + resp[27] + "\n" +
                "ECR Transaction Reference No" + ": " + resp[28] + "\n" +
                "Signature" + "\t" + "\t" + "\t" + "\t" + "\t" + ": " + resp[29] + "\n";
    }

    String printResponseRegister(String[] resp) {
        return "Transaction type" + "\t" + "\t" + "\t" + ": " + resp[1] + "\n" +
                "Response Code" + "\t" + "\t" + "\t" + ": " + resp[2] + "\n" +
                "Terminal id" + "\t" + "\t" + "\t" + ": " + resp[3] + "\n";
    }

    String printResponseStartSession(String[] resp) {
        return "Transaction type" + "\t" + "\t" + "\t" + ": " + resp[1] + "\n" +
                "Response Code" + "\t" + "\t" + "\t" + ": " + resp[2] + "\n";
    }

    String printResponsePurchaseCashBack(String[] resp) {
        String pan;
        if (resp[4].length() > 14) {
            pan = maskPAn(resp[4]);
        } else {
            pan = resp[4];
        }
        return "Transaction type" + "\t" + "\t" + "\t" + ": " + resp[1] + "\n" +
                "Response Code" + "\t" + "\t" + "\t" + ": " + resp[2] + "\n" +
                "Response Message" + "\t" + "\t" + ": " + resp[3] + "\n" +
                "PAN Number" + "\t" + "\t" + "\t" + ": " + pan + "\n" +
                "Transaction Amount" + "\t" + "\t" + ": " + resp[5] + "\n" +
                "Cash Back Amount" + "\t" + "\t" + ": " + resp[6] + "\n" +
                "Total Amount" + "\t" + "\t" + "\t" + ": " + resp[7] + "\n" +
                "Buss Code" + "\t" + "\t" + "\t" + ": " + resp[8] + "\n" +
                "Stan No" + "\t" + "\t" + "\t" + "\t" + ": " + resp[9] + "\n" +
                "Date & Time" + "\t" + "\t" + "\t" + ": " + resp[10] + "\n" +
                "Card Exp Date" + "\t" + "\t" + "\t" + ": " + resp[11] + "\n" +
                "RRN" + "\t" + "\t" + "\t" + "\t" + ": " + resp[12] + "\n" +
                "Auth Code" + "\t" + "\t" + "\t" + ": " + resp[13] + "\n" +
                "TID" + "\t" + "\t" + "\t" + "\t" + ": " + resp[14] + "\n" +
                "MID" + "\t" + "\t" + "\t" + "\t" + ": " + resp[15] + "\n" +
                "Batch No" + "\t" + "\t" + "\t" + ": " + resp[16] + "\n" +
                "AID" + "\t" + "\t" + "\t" + "\t" + ": " + resp[17] + "\n" +
                "Application Cryptogram" + "\t" + "\t" + ": " + resp[18] + "\n" +
                "CID" + "\t" + "\t" + "\t" + "\t" + ": " + resp[19] + "\n" +
                "CVR" + "\t" + "\t" + "\t" + "\t" + ": " + resp[20] + "\n" +
                "TVR" + "\t" + "\t" + "\t" + "\t" + ": " + resp[21] + "\n" +
                "TSI" + "\t" + "\t" + "\t" + "\t" + ": " + resp[22] + "\n" +
                "Card Entry Mode" + "\t" + "\t" + "\t" + ": " + resp[23] + "\n" +
                "Merchant Category Code" + "\t" + "\t" + ": " + resp[24] + "\n" +
                "Scheme Label" + "\t" + "\t" + "\t" + ": " + resp[26] + "\n" +
                "Store and Cashier Info" + "\t" + "\t" + ": " + resp[27] + "\n" +
                "Product Info" + "\t" + "\t" + "\t" + ": " + resp[28] + "\n" +
                "Application Version" + "\t" + "\t" + ": " + resp[29] + "\n" +
                "ECR Transaction Reference Number" + "\t" + ": " + resp[30] + "\n" +
                "Signature" + "\t" + "\t" + "\t" + ": " + resp[31] + "\n";
    }

    String printResponseReconcilation(String[] resp) {

        String printSettlment1 =
                "Scheme Name" + "\t" + "\t" + "\t" + ": SchemeName \n" +
                        "Scheme HOST" + "\t" + "\t" + "\t" + ": SchemeHOST \n" +
                        "Transaction Available Flag" + "\t" + "\t" + ": TransactionAvailableFlag \n" +
                        "Total Debit Count" + "\t" + "\t" + "\t" + ": TotalDebitCount \n" +
                        "Total Debit Amount" + "\t" + "\t" + ": TotalDebitAmount \n" +
                        "Total Credit Count" + "\t" + "\t" + "\t" + ": TotalCreditCount \n" +
                        "Total Credit Amount" + "\t" + "\t" + ": TotalCreditAmount \n" +
                        "NAQD Count" + "\t" + "\t" + "\t" + ": NAQDCount \n" +
                        "NAQD Amount" + "\t" + "\t" + "\t" + ": NAQDAmount \n" +
                        "C/ADV Count" + "\t" + "\t" + "\t" + ": CADVCount \n" +
                        "C/ADV Amount" + "\t" + "\t" + "\t" + ": CADVAmount \n" +
                        "Auth Count" + "\t" + "\t" + "\t" + ": AuthCount \n" +
                        "Auth Amount" + "\t" + "\t" + "\t" + ": AuthAmount \n" +
                        "Total Count" + "\t" + "\t" + "\t" + ": TotalCount \n" +
                        "Total Amount" + "\t" + "\t" + "\t" + ": TotalAmount \n";
        String printSettlmentPos1 =
                "Transaction Available Flag" + "\t" + "\t" + ": TransactionAvailableFlag \n" +
                        "Scheme Name" + "\t" + "\t" + "\t" + ": SchemeName \n" +
                        "Total Debit Count" + "\t" + "\t" + "\t" + ": TotalDebitCount \n" +
                        "Total Debit Amount" + "\t" + "\t" + ": TotalDebitAmount \n" +
                        "Total Credit Count" + "\t" + "\t" + "\t" + ": TotalCreditCount \n" +
                        "Total Credit Amount" + "\t" + "\t" + ": TotalCreditAmount \n" +
                        "NAQD Count" + "\t" + "\t" + "\t" + ": NAQDCount \n" +
                        "NAQD Amount" + "\t" + "\t" + "\t" + ": NAQDAmount \n" +
                        "C/ADV Count" + "\t" + "\t" + "\t" + ": CADVCount \n" +
                        "C/ADV Amount" + "\t" + "\t" + "\t" + ": CADVAmount \n" +
                        "Auth Count" + "\t" + "\t" + "\t" + ": TotalCount \n" +
                        "Auth Amount" + "\t" + "\t" + "\t" + ": AuthAmount \n" +
                        "Total Count" + "\t" + "\t" + "\t" + ": TotalCount \n" +
                        "Total Amount" + "\t" + "\t" + "\t" + ": TotalAmount \n";
        String printSettlmentPosDetails1 =
                "Transaction Available Flag" + "\t" + "\t" + ": TransactionAvailableFlag \n" +
                        "Scheme Name" + "\t" + "\t" + "\t" + ": SchemeName \n" +
                        "P/OFF Count" + "\t" + "\t" + "\t" + ": POFFCount \n" +
                        "P/OFF Amount" + "\t" + "\t" + "\t" + ": POFFAmount \n" +
                        "P/ON Count" + "\t" + "\t" + "\t" + ": PONCount \n" +
                        "P/ON Amount" + "\t" + "\t" + "\t" + ": PONAmount \n" +
                        "NAQD Count" + "\t" + "\t" + "\t" + ": NAQDCount \n" +
                        "NAQD Amount" + "\t" + "\t" + "\t" + ": NAQDAmount \n" +
                        "REVERSAL Count" + "\t" + "\t" + "\t" + ": REVERSALCount \n" +
                        "REVERSAL Amount" + "\t" + "\t" + ": REVERSALAmount \n" +
                        "REFUND Count" + "\t" + "\t" + "\t" + ": REFUNDCount \n" +
                        "REFUND Amount" + "\t" + "\t" + "\t" + ": REFUNDAmount \n" +
                        "COMP Count" + "\t" + "\t" + "\t" + ": COMPCount \n" +
                        "COMP Amount" + "\t" + "\t" + "\t" + ": COMPAmount \n";

        StringBuilder printFinalReport1 = new StringBuilder("");
        int k = 9;
        int totalSchemeLength = Integer.parseInt(resp[9]);
        logger.debug("Sceme length   :" + totalSchemeLength);
        for (int i = 1; i <= totalSchemeLength; i++) {

            if (resp[k + 2].equals("0")) {
                String printSettlmentNO = "Scheme Name" + "\t" + "\t" + "\t" + ": " + resp[k + 1] + "\n" +
                        "<No Transaction> \n";
                k = k + 3;
                printFinalReport1.append(printSettlmentNO);
            } else {
                String printSettlment = printSettlment1;
                if (resp[k + 3].equalsIgnoreCase("mada HOST")) {
                    i = i - 1;
                    printSettlment = printSettlment.replace("SchemeName", resp[k + 1]);
                    printSettlment = printSettlment.replace("TransactionAvailableFlag", resp[k + 2]);
                    printSettlment = printSettlment.replace("SchemeHOST", resp[k + 3]);
                    printSettlment = printSettlment.replace("TotalDebitCount", resp[k + 4]);
                    printSettlment = printSettlment.replace("TotalDebitAmount", resp[k + 5]);
                    printSettlment = printSettlment.replace("TotalCreditCount", resp[k + 6]);
                    printSettlment = printSettlment.replace("TotalCreditAmount", resp[k + 7]);
                    printSettlment = printSettlment.replace("NAQDCount", resp[k + 8]);
                    printSettlment = printSettlment.replace("NAQDAmount", resp[k + 9]);
                    printSettlment = printSettlment.replace("CADVCount", resp[k + 10]);
                    printSettlment = printSettlment.replace("CADVAmount", resp[k + 11]);
                    printSettlment = printSettlment.replace("AuthCount", resp[k + 12]);
                    printSettlment = printSettlment.replace("AuthAmount", resp[k + 13]);
                    printSettlment = printSettlment.replace("TotalCount", resp[k + 14]);
                    printSettlment = printSettlment.replace("TotalAmount", resp[k + 15]);
                    k = k + 15;
                    printFinalReport1.append(printSettlment);

                } else if (resp[k + 2].equalsIgnoreCase("POS TERMINAL")) {
                   String printSettlmentPos = printSettlmentPos1;
                    i = i - 1;
                    printSettlmentPos = printSettlmentPos.replace("TransactionAvailableFlag", resp[k + 1]);
                    printSettlmentPos = printSettlmentPos.replace("SchemeName", resp[k + 2]);
                    printSettlmentPos = printSettlmentPos.replace("TotalDebitCount", resp[k + 3]);
                    printSettlmentPos = printSettlmentPos.replace("TotalDebitAmount", resp[k + 4]);
                    printSettlmentPos = printSettlmentPos.replace("TotalCreditCount", resp[k + 5]);
                    printSettlmentPos = printSettlmentPos.replace("TotalCreditAmount", resp[k + 6]);
                    printSettlmentPos = printSettlmentPos.replace("NAQDCount", resp[k + 7]);
                    printSettlmentPos = printSettlmentPos.replace("NAQDAmount", resp[k + 8]);
                    printSettlmentPos = printSettlmentPos.replace("CADVCount", resp[k + 9]);
                    printSettlmentPos = printSettlmentPos.replace("CADVAmount", resp[k + 10]);
                    printSettlmentPos = printSettlmentPos.replace("AuthCount", resp[k + 11]);
                    printSettlmentPos = printSettlmentPos.replace("AuthAmount", resp[k + 12]);
                    printSettlmentPos = printSettlmentPos.replace("TotalCount", resp[k + 13]);
                    printSettlmentPos = printSettlmentPos.replace("TotalAmount", resp[k + 14]);
                    k = k + 14;
                    printFinalReport1.append(printSettlmentPos);
                } else if (resp[k + 2].equalsIgnoreCase("POS TERMINAL DETAILS")) {
                    String printSettlmentPosDetails = printSettlmentPosDetails1;
                    i = i - 1;
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("TransactionAvailableFlag", resp[k + 1]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("SchemeName", resp[k + 2]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("POFFCount", resp[k + 3]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("POFFAmount", resp[k + 4]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("PONCount", resp[k + 5]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("PONAmount", resp[k + 6]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("NAQDCount", resp[k + 7]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("NAQDAmount", resp[k + 8]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("REVERSALCount", resp[k + 9]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("REVERSALAmount", resp[k + 10]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("REFUNDCount", resp[k + 11]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("REFUNDAmount", resp[k + 12]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("COMPCount", resp[k + 13]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("COMPAmount", resp[k + 14]);
                    k = k + 14;
                    printFinalReport1.append(printSettlmentPosDetails);
                } else if (resp[k + 1].equals("0")) {
                    String printSettlmentNO1 = "Scheme Name" + "\t" + "\t" + "\t" + ": POS TERMINAL \n" +
                            "<No Transaction> \n" +
                            "Scheme Name" + "\t" + "\t" + "\t" + ": POS TERMINAL Details\n" +
                            "<No Transaction> \n";
                    k = k + 1;
                    printFinalReport1.append(printSettlmentNO1);
                }
            }
        }

        return "Transaction type" + "\t" + "\t" + "\t" + ": " + resp[1] + "\n" +
                "Response Code" + "\t" + "\t" + "\t" + ": " + resp[2] + "\n" +
                "Response Message" + "\t" + "\t" + ": " + resp[3] + "\n" +
                "Date Time Stamp" + "\t" + "\t" + "\t" + ": " + resp[4] + "\n" +
                "Merchent ID" + "\t" + "\t" + "\t" + ": " + resp[5] + "\n" +
                "Buss Code" + "\t" + "\t" + "\t" + ": " + resp[6] + "\n" +
                "Trace Number" + "\t" + "\t" + "\t" + ": " + resp[7] + "\n" +
                "Application Version" + "\t" + "\t" + ": " + resp[8] + "\n" +
                "Total Scheme Length" + "\t" + "\t" + ": " + resp[9] + "\n" +
                printFinalReport1 +
                "ECR Transaction Reference Number : " + resp[k + 1] + "\n" +
                "Signature" + "\t" + "\t" + "\t" + ": " + resp[k + 2] + "\n";

    }

    String printResponseParameterDownload(String[] resp) {
        return "Transaction type" + "\t" + "\t" + "\t" + ": " + resp[1] + "\n" +
                "Response Code" + "\t" + "\t" + "\t" + ": " + resp[2] + "\n" +
                "Response Message" + "\t" + "\t" + ": " + resp[3] + "\n" +
                "Date Time Stamp" + "\t" + "\t" + "\t" + ": " + resp[4] + "\n" +
                "ECR Transaction Reference Number : " + resp[5] + "\n" +
                "Signature" + "\t" + "\t" + "\t" + ": " + resp[6] + "\n";
    }

    String printResponseGetSetting(String[] resp) {
        return "Transaction type" + "\t" + "\t" + "\t" + ": " + resp[1] + "\n" +
                "Response Code" + "\t" + "\t" + "\t" + ": " + resp[2] + "\n" +
                "Response Message" + "\t" + "\t" + ": " + resp[3] + "\n" +
                "Date Time Stamp" + "\t" + "\t" + "\t" + ": " + resp[4] + "\n" +
                "Vendor ID" + "\t" + "\t" + "\t" + ": " + resp[5] + "\n" +
                "Vendor Terminal type" + "\t" + "\t" + ": " + resp[6] + "\n" +
                "TRSM ID" + "\t" + "\t" + "\t" + "\t" + ": " + resp[7] + "\n" +
                "Vendor Key Index" + "\t" + "\t" + "\t" + ": " + resp[8] + "\n" +
                "SAMA Key Index" + "\t" + "\t" + "\t" + ": " + resp[9] + "\n" +
                "ECR Transaction Reference Number : " + resp[10] + "\n" +
                "Signature" + "\t" + "\t" + "\t" + ": " + resp[11] + "\n";
    }


    String printResponseRepeat(String[] resp) {
        return "Transaction type" + resp[1] + "\n" + "Response Code" + resp[2] + "\n"
                + "Response Message" + resp[3] + "\n" + "Date Time Stamp" + resp[4] + "\n"
                + "PreviousTransactionResponse:" + resp[5] + "\n" + "PreviousEcrNo		:" + resp[6] + "\n"
                + "ECR Transaction Reference Number : " + resp[7] + "\n" + "Signature" + resp[8] + "\n";
    }

    public String printResponseDefault(String[] resp) {
        return "Transaction type: " + resp[1] + "\n" +
                "Response Code      : " + resp[2] + "\n" +
                "Response Message   : " + resp[3] + "\n";
    }

    public String printResponseRunningTotal(String[] resp) {

        String printSettlmentPos1 = "Scheme Name" + "\t" + "\t" + "\t" + "\t" + ": SchemeName \n" + "Scheme HOST" + "\t" + "\t" + "\t"
                + "\t" + ": SchemeHOST \n" + "Transaction Available Flag" + "\t"
                + ": TransactionAvailableFlag \n" + "Total Debit Count" + "\t" + "\t" + "\t" + ": TotalDebitCount \n"
                + "Total Debit Amount" + "\t" + "\t" + "\t" + ": TotalDebitAmount \n" + "Total Credit Count" + "\t" + "\t"
                + "\t" + ": TotalCreditCount \n" + "Total Credit Amount" + "\t" + "\t" + ": TotalCreditAmount \n"
                + "NAQD Count" + "\t" + "\t" + "\t" + "\t" + ": NAQDCount \n" + "NAQD Amount" + "\t" + "\t" + "\t" + "\t"
                + ": NAQDAmount \n" + "C/ADV Count" + "\t" + "\t" + "\t" + "\t" + ": CADVCount \n" + "C/ADV Amount" + "\t"
                + "\t" + "\t" + "\t" + ": CADVAmount \n" + "Auth Count" + "\t" + "\t" + "\t" + "\t" + ": AuthCount \n" + "Auth Amount"
                + "\t" + "\t" + "\t" + "\t" + ": AuthAmount \n" + "Total Count" + "\t" + "\t" + "\t" + "\t" + ": TotalCount \n"
                + "Total Amount" + "\t" + "\t" + "\t" + "\t" + ": TotalAmount \n";
        String printSettlmentPosDetails1 = "Transaction Available Flag" + "\t" + ": TransactionAvailableFlag \n"
                + "Scheme Name" + "\t" + "\t" + "\t" + "\t" + ": SchemeName \n" + "P/OFF Count" + "\t" + "\t" + "\t" + "\t"
                + ": POFFCount \n" + "P/OFF Amount" + "\t" + "\t" + "\t" + "\t" + ": POFFAmount \n" + "P/ON Count" + "\t"
                + "\t" + "\t" + "\t" + ": PONCount \n" + "P/ON Amount" + "\t" + "\t" + "\t" + "\t" + ": PONAmount \n" + "NAQD Count"
                + "\t" + "\t" + "\t" + "\t" + ": NAQDCount \n" + "NAQD Amount" + "\t" + "\t" + "\t" + "\t" + ": NAQDAmount \n"
                + "REVERSAL Count" + "\t" + "\t" + "\t" + ": REVERSALCount \n" + "REVERSAL Amount" + "\t" + "\t" + "\t"
                + ": REVERSALAmount \n" + "REFUND Count" + "\t" + "\t" + "\t" + "\t" + ": REFUNDCount \n" + "REFUND Amount"
                + "\t" + "\t" + "\t" + ": REFUNDAmount \n" + "COMP Count" + "\t" + "\t" + "\t" + "\t" + ": COMPCount \n"
                + "COMP Amount" + "\t" + "\t" + "\t" + "\t" + ": COMPAmount \n";

        StringBuilder printFinalReport1 = new StringBuilder("");
        int k = 8;
        int totalSchemeLength = Integer.parseInt(resp[8]);
        logger.debug("Sceme length   :" + totalSchemeLength);
        for (int i = 1; i <= totalSchemeLength; i++) {

            if (resp[k + 2].equalsIgnoreCase("0")) {
                String printSettlmentNO = "Scheme Name" + "\t" + "\t" + "\t" + "\t" + ": " + resp[k + 1] + "\n"
                        + "<No Transaction> \n";
                k = k + 2;
                StringBuilder printSettlment2 = new StringBuilder(printSettlmentNO);
                printFinalReport1.append(printSettlment2);
            } else {
                if (resp[k + 3].equalsIgnoreCase("POS TERMINAL")) {
                    String printSettlmentPos = printSettlmentPos1;
                    printSettlmentPos = printSettlmentPos.replace("SchemeName", resp[k + 1]);
                    printSettlmentPos = printSettlmentPos.replace("TransactionAvailableFlag", resp[k + 2]);
                    printSettlmentPos = printSettlmentPos.replace("SchemeHOST", resp[k + 3]);
                    printSettlmentPos = printSettlmentPos.replace("TotalDebitCount", resp[k + 4]);
                    printSettlmentPos = printSettlmentPos.replace("TotalDebitAmount", resp[k + 5]);
                    printSettlmentPos = printSettlmentPos.replace("TotalCreditCount", resp[k + 6]);
                    printSettlmentPos = printSettlmentPos.replace("TotalCreditAmount", resp[k + 7]);
                    printSettlmentPos = printSettlmentPos.replace("NAQDCount", resp[k + 8]);
                    printSettlmentPos = printSettlmentPos.replace("NAQDAmount", resp[k + 9]);
                    printSettlmentPos = printSettlmentPos.replace("CADVCount", resp[k + 10]);
                    printSettlmentPos = printSettlmentPos.replace("CADVAmount", resp[k + 11]);
                    printSettlmentPos = printSettlmentPos.replace("AuthCount", resp[k + 12]);
                    printSettlmentPos = printSettlmentPos.replace("AuthAmount", resp[k + 13]);
                    printSettlmentPos = printSettlmentPos.replace("TotalCount", resp[k + 14]);
                    printSettlmentPos = printSettlmentPos.replace("TotalAmount", resp[k + 15]);
                    k = k + 15;
                    printFinalReport1.append(printSettlmentPos);
                } else if (resp[k + 2].equalsIgnoreCase("POS TERMINAL DETAILS")) {
                    String printSettlmentPosDetails = printSettlmentPosDetails1;
                    i = i - 1;
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("TransactionAvailableFlag",
                            resp[k + 1]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("SchemeName", resp[k + 2]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("POFFCount", resp[k + 3]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("POFFAmount", resp[k + 4]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("PONCount", resp[k + 5]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("PONAmount", resp[k + 6]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("NAQDCount", resp[k + 7]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("NAQDAmount", resp[k + 8]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("REVERSALCount", resp[k + 9]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("REVERSALAmount", resp[k + 10]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("REFUNDCount", resp[k + 11]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("REFUNDAmount", resp[k + 12]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("COMPCount", resp[k + 13]);
                    printSettlmentPosDetails = printSettlmentPosDetails.replace("COMPAmount", resp[k + 14]);
                    k = k + 14;
                    printFinalReport1.append(printSettlmentPosDetails);
                }
            }
        }
        return "Transaction type" + "\t" + "\t" + "\t" + ": " + resp[1] + "\n" + "Response Code" + "\t" + "\t" + "\t"
                + ": " + resp[2] + "\n" + "Response Message" + "\t" + "\t" + "\t" + ": " + resp[3] + "\n" + "Date Time Stamp"
                + "\t" + "\t" + "\t" + ": " + resp[4] + "\n" + "Trace Number" + "\t" + "\t" + "\t" + "\t" + ": " + resp[5]
                + "\n" + "Buss Code" + "\t" + "\t" + "\t" + "\t" + ": " + resp[6] + "\n" + "Application Version" + "\t" + "\t" + "\t"
                + ": " + resp[7] + "\n" + "Total Scheme Length" + "\t" + "\t" + ": " + resp[8] + "\n"
                + printFinalReport1 + "ECRTransaction ReferenceNo: " + resp[k + 1] + "\n" + "Signature" + "\t"
                + "\t" + "\t" + "\t" + "\t" + ": " + resp[k + 2] + "\n";
    }

    public String printResponseSummaryReport(String[] resp) {

        logger.debug("arrayLength>>" + resp.length);

        String printSummaryReportString1 = "Transaction transactionNumberHead\n"
                + "----------------------------------------------------------\n" + "Transaction Type" + "\t" + "\t"
                + "\t" + ": TransactionType1  \n" + "Date" + "\t" + "\t" + "\t" + "\t" + ": Date1 \n" + "RRN" + "\t"
                + "\t" + "\t" + "\t" + ": RRN1 \n" + "Transaction Amount" + "\t" + "\t" + ": TransactionAmount1 \n"
                + "Claim " + "\t" + "\t" + "\t" + "\t" + ": Claim1 \n" + "State" + "\t" + "\t" + "\t" + "\t"
                + ": State1 \n" + "Time" + "\t" + "\t" + "\t" + "\t" + ": Time1 \n" + "PAN Number" + "\t" + "\t" + "\t"
                + ": PANNumber1 \n" + "authCode" + "\t" + "\t" + "\t" + ": authCode1 \n" + "transactionNumber" + "\t"
                + "\t" + ": transactionNumber1 \n" + "----------------------------------------------------------\n";
        StringBuilder summaryFinalReport1 = new StringBuilder("");
        int k = 7;
        int transactionsLength = Integer.parseInt(resp[6]);
        if (transactionsLength > 17) {
            transactionsLength = 17;
        }
        for (int a = 1; a <= transactionsLength; a++) {
            String printSummaryReportString = printSummaryReportString1;
            printSummaryReportString = printSummaryReportString.replace("transactionNumberHead", String.valueOf(a));
            printSummaryReportString = printSummaryReportString.replace("TransactionType1", resp[k]);
            printSummaryReportString = printSummaryReportString.replace("Date1", resp[k + 1]);
            printSummaryReportString = printSummaryReportString.replace("RRN1", resp[k + 2]);
            printSummaryReportString = printSummaryReportString.replace("TransactionAmount1", resp[k + 3]);
            printSummaryReportString = printSummaryReportString.replace("Claim1", resp[k + 4]);
            printSummaryReportString = printSummaryReportString.replace("State1", resp[k + 5]);
            printSummaryReportString = printSummaryReportString.replace("Time1", resp[k + 6]);
            printSummaryReportString = printSummaryReportString.replace("PANNumber1", resp[k + 7]);
            printSummaryReportString = printSummaryReportString.replace("authCode1", resp[k + 8]);
            printSummaryReportString = printSummaryReportString.replace("transactionNumber1", resp[k + 9]);
            k = k + 10;
            summaryFinalReport1.append(printSummaryReportString);
        }

        return "Transaction type" + "\t" + "\t" + "\t" + ": " + resp[1] + "\n" + "Response Code" + "\t" + "\t" + "\t"
                + ": " + resp[2] + "\n" + "Response Message" + "\t" + "\t" + ": " + resp[3] + "\n" + "Date Time Stamp"
                + "\t" + "\t" + "\t" + ": " + resp[4] + "\n" + "Transaction Requests Count" + "\t" + ": " + resp[5]
                + "\n" + "Total Transactions Length" + "\t" + ": " + resp[6] + "\n" + summaryFinalReport1
                + "ECR Transaction Reference Number  : " + resp[k] + "\n" + "Signature" + "\t" + "\t" + "\t" + ": "
                + resp[k + 1] + "\n";
    }

    public String printResponseReversal(String[] resp) {
        String pan;
        if (resp[4].length() > 14) {
            pan = maskPAn(resp[4]);
        } else {
            pan = resp[4];
        }
        return "Transaction type" + "\t" + "\t" + "\t" + ": " + resp[1] + "\n" +
                "Response Code" + "\t" + "\t" + "\t" + ": " + resp[2] + "\n" +
                "Response Message" + "\t" + "\t" + ": " + resp[3] + "\n" +
                "PAN Number" + "\t" + "\t" + "\t" + ": " + pan + "\n" +
                "Transaction Amount" + "\t" + "\t" + ": " + resp[5] + "\n" +
                "Buss Code" + "\t" + "\t" + "\t" + ": " + resp[6] + "\n" +
                "Stan No" + "\t" + "\t" + "\t" + "\t" + ": " + resp[7] + "\n" +
                "Date & Time" + "\t" + "\t" + "\t" + ": " + resp[8] + "\n" +
                "Card Exp Date" + "\t" + "\t" + "\t" + ": " + resp[9] + "\n" +
                "RRN" + "\t" + "\t" + "\t" + "\t" + ": " + resp[10] + "\n" +
                "Auth Code" + "\t" + "\t" + "\t" + ": " + resp[11] + "\n" +
                "TID" + "\t" + "\t" + "\t" + "\t" + ": " + resp[12] + "\n" +
                "MID" + "\t" + "\t" + "\t" + "\t" + ": " + resp[13] + "\n" +
                "Batch No" + "\t" + "\t" + "\t" + ": " + resp[14] + "\n" +
                "AID" + "\t" + "\t" + "\t" + "\t" + ": " + resp[15] + "\n" +
                "Application Cryptogram" + "\t" + "\t" + ": " + resp[16] + "\n" +
                "CID" + "\t" + "\t" + "\t" + "\t" + ": " + resp[17] + "\n" +
                "CVR" + "\t" + "\t" + "\t" + "\t" + ": " + resp[18] + "\n" +
                "TVR" + "\t" + "\t" + "\t" + "\t" + ": " + resp[19] + "\n" +
                "TSI" + "\t" + "\t" + "\t" + "\t" + ": " + resp[20] + "\n" +
                "Card Entry Mode" + "\t" + "\t" + "\t" + ": " + resp[21] + "\n" +
                "Merchant Category Code" + "\t" + "\t" + ": " + resp[22] + "\n" +
                "Scheme Label" + "\t" + "\t" + "\t" + ": " + resp[24] + "\n" +
                "Store and Cashier Info" + "\t" + "\t" + ": " + resp[25] + "\n" +
                "Product Info" + "\t" + "\t" + "\t" + ": " + resp[26] + "\n" +
                "Application Version" + "\t" + "\t" + ": " + resp[27] + "\n" +
                "ECR Transaction Reference Number" + "\t" + ": " + resp[28] + "\n" +
                "Signature" + "\t" + "\t" + "\t" + ": " + resp[29] + "\n";
    }

    public String printResponseCheckStatus(String[] resp) {
        return "Transaction type" + "\t" + "\t" + "\t" + ": " + resp[1] + "\n" +
                "Response Code" + "\t" + "\t" + "\t" + ": " + resp[2] + "\n" +
                "Response Message" + "\t" + "\t" + "\t" + ": " + resp[3] + "\n" +
                "Date Time Stamp" + "\t" + "\t" + "\t" + ": " + resp[4] + "\n" +
                "ECRTransactionReferenceNo: " + resp[5] + "\n" +
                "Signature" + "\t" + "\t" + "\t" + "\t" + "\t" + ": " + resp[6] + "\n";
    }

    private String maskPAn(String s) {
        return s.substring(0, 5) + "******" + s.substring(s.length() - 4);
    }
}


