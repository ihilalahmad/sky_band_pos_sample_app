package com.girmiti.skybandecr.ui.fragment.response.buffer;

import androidx.lifecycle.ViewModel;

import com.girmiti.skybandecr.sdk.logger.Logger;

public class BufferResponseViewModel extends ViewModel {

    private static String htmlStart = "<html><head></head><body><table style='border-collapse: collapse; width: 100%;'>";
    private static String htmlSeperator1 = "<tr style = 'border: 1px solid #ddd; border: 1px solid #ddd; padding: 8px;'><td>";
    private static String htmlSeperator2 = "</td><td>:</td><td>";
    private static String htmlSeperator3 = "</td></tr>";
    private static String htmlEnd = "</table></body></html>";
    private Logger logger = Logger.getNewLogger(BufferResponseViewModel.class.getName());

    String printResponsePurchase(String[] resp) {

        String pan;
        String htmlString = "";
        if (resp[4].length() > 14) {
            pan = maskPAn(resp[4]);
        } else {
            pan = resp[4];
        }
        String[] purchaseArray = {"", "TransactionType", "ResponseCode", "ResponseMessage", "PanNumber",
                "TransactionAmount", "BuzzCode", "StanNo", "Date&Time", "CardExpriyDate", "RRN", "AuthCode", "TID",
                "MID", "BatchNo", "AID", "ApplicationCryptogram", "CID", "CVR", "TVR", "TSI", "CardEntryMode",
                "MerchantCategoryCode", "Terminal TransactionType", "Schemelabel", "Store&CashierInfo", "ProductInfo",
                "ApplicationVersion", "EcrTransactionReferenceNumber", "Signature"};

        for (int i = 1; i < purchaseArray.length; i++) {
            if (i != 4)
                htmlString = htmlString + htmlSeperator1 + purchaseArray[i] + htmlSeperator2 + resp[i] + htmlSeperator3;
            else
                htmlString = htmlString + htmlSeperator1 + purchaseArray[i] + htmlSeperator2 + pan + htmlSeperator3;
        }
        htmlString = htmlStart + htmlString + htmlEnd;
        return htmlString;
    }

    String printResponseRegister(String[] resp) {

        String[] register = {"", "Transaction type", "Response Code", "Terminal id"};
        String htmlString = "";
        for (int i = 1; i < register.length; i++) {
            htmlString = htmlString + htmlSeperator1 + register[i] + htmlSeperator2 + resp[i] + htmlSeperator3;

        }
        htmlString = htmlStart + htmlString + htmlEnd;
        return htmlString;
    }

    String printResponseStartSession(String[] resp) {
        String htmlString = "";
        String[] sessionArray = {"", "TransactionType", "ResponseCode"};
        for (int i = 1; i < sessionArray.length; i++) {
            htmlString = htmlString + htmlSeperator1 + sessionArray[i] + htmlSeperator2 + resp[i] + htmlSeperator3;
        }
        htmlString = htmlStart + htmlString + htmlEnd;
        return htmlString;
    }

    String printResponsePurchaseCashBack(String[] resp) {
        String pan;
        String htmlString = "";
        if (resp[4].length() > 14) {
            pan = maskPAn(resp[4]);
        } else {
            pan = resp[4];
        }
        String[] purchaseCashBackArray = {"", "TransactionType", "ResponseCode", "ResponseMessage", "PanNumber",
                "TransactionAmount", "CashBackAmount", "TotalAmount", "BuzzCode", "StanNo", "Date&Time",
                "CardExpriyDate", "RRN", "AuthCode", "TID", "MID", "BatchNo", "AID", "ApplicationCryptogram", "CID",
                "CVR", "TVR", "TSI", "CardEntryMode", "MerchantCategoryCode", "Terminal TransactionType", "Schemelabel",
                "Store&CashierInfo", "ProductInfo", "ApplicationVersion", "EcrTransactionReferenceNumber",
                "Signature"};

        for (int i = 1; i < purchaseCashBackArray.length; i++) {
            if (i != 4)
                htmlString = htmlString + htmlSeperator1 + purchaseCashBackArray[i] + htmlSeperator2 + resp[i]
                        + htmlSeperator3;
            else
                htmlString = htmlString + htmlSeperator1 + purchaseCashBackArray[i] + htmlSeperator2 + pan
                        + htmlSeperator3;

        }
        htmlString = htmlStart + htmlString + htmlEnd;
        return htmlString;

    }

    String printResponseReconcilation(String[] resp) {

        StringBuilder htmlString = new StringBuilder();
        String[] posTerminalDetails = {"", "TransactionAvailableFlag", "SchemeName", "POFFCount", "POFFAmount",
                "PONCount", "PONAmount", "NAQDCount", "NAQDAmount", "ReversalCount", "ReversalAmount", "RefundCount",
                "RefundAmount", "CompCount", "CompAmount"};
        String[] posTerminal = {"", "TransactionAvailableFlag", "SchemeName", "TotalDebitCount", "TotalDebitAmount",
                "TotalCreditCount", "TotalCreditAmount", "NAQDCount", "NAQDAmount", "CADVCount", "CADVAmount",
                "AuthCount", "AuthAmount", "TotalCount", "TotalAmount"};
        String[] printSettlement = {"", "SchemeName", "Transaction Available Flag", "SchemeHost", "Total Debit Count",
                "Total Debit Amount", "Total Credit Count", "Total Credit Amount", "NAQD Count", "NAQD Amount",
                "C/ADV Count", "C/ADV Amount", "Auth Count", "Auth Amount", "Total Count", "Total Amount"};
        String[] reconcilation = {"", "TransactionType", "ResponseCode", "ResponseMessage", "Date&Time", "MerchantId",
                "BuzzCode", "TraceNumber", "ApplicationVersion", "TotalSchemeLength"};
        int k = 9;
        int totalSchemeLength = Integer.parseInt(resp[9]);
        for (int i = 1; i <= 9; i++)
            htmlString.append(htmlSeperator1).append(reconcilation[i]).append(htmlSeperator2).append(resp[i]).append(htmlSeperator3);
        for (int i = 1; i <= totalSchemeLength; i++) {

            if (resp[k + 2].equals("0")) {
                htmlString.append(htmlSeperator1).append("SchemeName").append(htmlSeperator2).append(resp[k + 1]).append(htmlSeperator3);
                htmlString.append(htmlSeperator1).append("NoTransaction").append(" ").append(htmlSeperator3);
                k = k + 3;
            } else {
                if (resp[k + 3].equalsIgnoreCase("mada HOST")) {
                    i = i - 1;
                    for (int j = 1; j < printSettlement.length; j++) {
                        htmlString.append(htmlSeperator1).append(printSettlement[j]).append(htmlSeperator2).append(resp[k + j]).append(htmlSeperator3);
                    }
                    k = k + 15;
                } else if (resp[k + 2].equalsIgnoreCase("POS TERMINAL")) {
                    i = i - 1;
                    for (int j = 1; j < posTerminal.length; j++) {
                        htmlString.append(htmlSeperator1).append(posTerminal[j]).append(htmlSeperator2).append(resp[k + j]).append(htmlSeperator3);
                    }
                    k = k + 14;
                } else if (resp[k + 2].equalsIgnoreCase("POS TERMINAL DETAILS")) {
                    i = i - 1;
                    for (int j = 1; j < posTerminalDetails.length; j++) {
                        htmlString.append(htmlSeperator1).append(posTerminalDetails[j]).append(htmlSeperator2).append(resp[k + j]).append(htmlSeperator3);
                    }
                    k = k + 14;
                } else if (resp[k + 1].equals("0")) {
                    htmlString.append(htmlSeperator1).append("SchemeName").append(htmlSeperator2).append("PosTerminal").append(htmlSeperator3);
                    htmlString.append(htmlSeperator1).append("NoTransaction").append(htmlSeperator3);
                    htmlString.append(htmlSeperator1).append("SchemeName").append(htmlSeperator2).append("PosTerminalDetails").append(htmlSeperator3);
                    htmlString.append(htmlSeperator1).append("NoTransaction").append(htmlSeperator3);
                    k = k + 1;
                }
            }
        }
        htmlString.append(htmlSeperator1).append("ECRTransactionReferenceNumber").append(htmlSeperator2).append(resp[k + 1]).append(htmlSeperator3);
        htmlString.append(htmlSeperator1).append("Signature").append(htmlSeperator2).append(resp[k + 2]).append(htmlSeperator3);
        htmlString = new StringBuilder(htmlStart + htmlString + htmlEnd);

        return htmlString.toString();
    }

    String printResponseParameterDownload(String[] resp) {
        String htmlString = "";
        String[] parameterArray = {"", "TransactionType", "ResponseCode", "ResponseMessage", "DateTimeStamp",
                "EcrTransactionRefNo", "Signature"};
        for (int i = 1; i < parameterArray.length; i++) {
            htmlString = htmlString + htmlSeperator1 + parameterArray[i] + htmlSeperator2 + resp[i] + htmlSeperator3;
        }
        htmlString = htmlStart + htmlString + htmlEnd;
        return htmlString;
    }

    String printResponseGetSetting(String[] resp) {
        String htmlString = "";
        String[] getParameter = {"", "TransactionType", "ResponseCode", "ResponseMessage", "DateTimeStamp", "VendorId",
                "VendorTerminalType", "TrsmId", "VendorKeyIndex", "SamaKeyIndex", "EcrTransactionReferenceNumber",
                "Signature"};
        for (int i = 1; i < getParameter.length; i++) {
            htmlString = htmlString + htmlSeperator1 + getParameter[i] + htmlSeperator2 + resp[i] + htmlSeperator3;
        }
        htmlString = htmlStart + htmlString + htmlEnd;
        return htmlString;
    }


    String printResponseRepeat(String[] resp) {
        String htmlString = "";
        String[] repeatArray = {"", "TransactionType", "ResponseCode", "ResponseMessage", "Date Time Stamp",
                "Previous Transaction Response", "Previous ECR Number", "ECR Transaction Reference Number",
                "Signature"};
        for (int i = 1; i < repeatArray.length; i++) {
            htmlString = htmlString + htmlSeperator1 + repeatArray[i] + htmlSeperator2 + resp[i] + htmlSeperator3;
        }
        htmlString = htmlStart + htmlString + htmlEnd;
        return htmlString;
    }

    public String printResponseDefault(String[] resp) {
        String htmlString = "";

        String[] otherResponse = {"", "TransactionType", "ResponseCode", "ResponseMessage"};

        for (int i = 1; i < otherResponse.length; i++)
            htmlString = htmlString + htmlSeperator1 + otherResponse[i] + htmlSeperator2 + resp[i] + htmlSeperator3;
        htmlString = htmlStart + htmlString + htmlEnd;
        return htmlString;
    }

    public String printResponseOtherTransaction(String[] resp) {

        String htmlString = "";

        String[] otherResponse = {"", "ResponseCode", "ResponseMessage"};

        for (int i = 1; i < otherResponse.length; i++)
            htmlString = htmlString + htmlSeperator1 + otherResponse[i] + htmlSeperator2 + resp[i] + htmlSeperator3;
        htmlString = htmlStart + htmlString + htmlEnd;
        return htmlString;
    }

    public String printResponseRunningTotal(String[] resp) {

        StringBuilder htmlString = new StringBuilder();
        String[] runningTotal = {"", "TransactionType", "ResponseCode", "ResponseMessage", "DateTimeStamp",
                "TraceNumber", "BuzzCode", "ApplicationVersion", "TotalSchemeLength"};
        String[] printSettlementPos = {"", "Scheme Name", "Transaction Available Flag", "Scheme Host",
                "Total Debit Count", "Total Debit Amount", "Total Credit Count", "Total Credit Amount", "NAQD Count",
                "NAQD Amount", "C/ADV Count", "C/ADV Amount", "Auth Count", "Auth Amount", "Total Count",
                "Total Amount"};
        String[] printSettlmentPosDetails = {"", "Transaction Available Flag", "Scheme Name", "P/OFF Count",
                "P/OFF Amount", "P/ON Count", "P/ON Amount", "NAQD Count", "NAQD Amount", "REVERSAL Count",
                "REVERSAL Amount", "Refund Count", "Refund Amount", "Comp Count", "Comp Amount"};
        int k = 8;
        int totalSchemeLength = Integer.parseInt(resp[8]);
        for (int i = 1; i <= 8; i++)
            htmlString.append(htmlSeperator1).append(runningTotal[i]).append(htmlSeperator2).append(resp[i]).append(htmlSeperator3);
        for (int i = 1; i <= totalSchemeLength; i++) {

            if (resp[k + 2].equalsIgnoreCase("0")) {
                htmlString.append(htmlSeperator1).append("SchemeName").append(htmlSeperator2).append(resp[k + 1]).append(htmlSeperator3);
                htmlString.append(htmlSeperator1).append("NoTransaction").append(" ").append(htmlSeperator3);
                k = k + 2;
            } else {
                if (resp[k + 3].equalsIgnoreCase("POS TERMINAL")) {
                    i = i - 1;
                    for (int j = 1; j < printSettlementPos.length; j++) {
                        htmlString.append(htmlSeperator1).append(printSettlementPos[j]).append(htmlSeperator2).append(resp[k + j]).append(htmlSeperator3);
                    }
                    k = k + 15;
                } else if (resp[k + 2].equalsIgnoreCase("POS TERMINAL DETAILS")) {
                    i = i - 1;
                    for (int j = 1; j < printSettlmentPosDetails.length; j++) {
                        htmlString.append(htmlSeperator1).append(printSettlmentPosDetails[j]).append(htmlSeperator2).append(resp[k + j]).append(htmlSeperator3);
                    }
                    k = k + 14;
                }
            }
        }
        htmlString.append(htmlSeperator1).append("ECRTransactionReferenceNumber").append(htmlSeperator2).append(resp[k + 1]).append(htmlSeperator3);
        htmlString.append(htmlSeperator1).append("Signature").append(htmlSeperator2).append(resp[k + 2]).append(htmlSeperator3);
        htmlString = new StringBuilder(htmlStart + htmlString + htmlEnd);

        return htmlString.toString();
    }

    public String printResponseRunningTotalDefault(String[] resp) {
        String htmlString = "";
        String[] otherResponse = {"", "TransactionType", "ResponseCode", "ResponseMessage", "ECR Transaction Reference Number", "Signature"};

        for (int i = 1; i < otherResponse.length; i++)
            htmlString = htmlString + htmlSeperator1 + otherResponse[i] + htmlSeperator2 + resp[i] + htmlSeperator3;
        htmlString = htmlStart + htmlString + htmlEnd;
        return htmlString;
    }

    public String printResponseSummaryReport(String[] resp) {

        String htmlString = "";

        String[] printSummaryReport = {
                "Transaction Type", "Date", "RRN", "Transaction Amount", "Claim", "State", "Time", "PAN Number",
                "authCode", "transactionNumber"};
        String[] printSummaryReturn = {"Transaction type", "Response Code", "Response Message", "Date Time Stamp",
                "Transaction Requests Count", "Total Transactions Length"};


        int k = 6;
        int transactionsLength = Integer.parseInt(resp[5]);

        for (int i = 1; i <= 5; i++)
            htmlString = htmlString + htmlSeperator1 + printSummaryReturn[i] + htmlSeperator2 + resp[i] + htmlSeperator3;
        for (int j = 1; j <= transactionsLength; j++) {
            htmlString = htmlString + htmlSeperator1 + "Transaction" + htmlSeperator2 + j + htmlSeperator3;
            for (int m = 0; m <= 9; m++) {
                htmlString = htmlString + htmlSeperator1 + printSummaryReport[m] + htmlSeperator2 + resp[k + m] + htmlSeperator3;
            }
            k = k + 10;
        }

        htmlString = htmlString + htmlSeperator1 + "ECRTransactionReferenceNumber" + htmlSeperator2 + resp[k]
                + htmlSeperator3;
        htmlString = htmlString + htmlSeperator1 + "Signature" + htmlSeperator2 + resp[k + 1] + htmlSeperator3;
        htmlString = htmlStart + htmlString + htmlEnd;

        return htmlString;
    }

    public String printResponseReversal(String[] resp) {

        String pan;
        String htmlString = "";
        if (resp[4].length() > 14) {
            pan = maskPAn(resp[4]);
        } else {
            pan = resp[4];
        }
        String[] reverseArray = {"", "TransactionType", "ResponseCode", "ResponseMessage", "PanNumber",
                "TransactionAmount", "BuzzCode", "StanNo", "Date&Time", "CardExpriyDate", "RRN", "AuthCode", "TID",
                "MID", "BatchNo", "AID", "ApplicationCryptogram", "CID", "CVR", "TVR", "TSI", "CardEntryMode",
                "MerchantCategoryCode", "Terminal TransactionType", "Schemelabel", "Store&CashierInfo", "ProductInfo",
                "ApplicationVersion", "EcrTransactionReferenceNumber", "Signature"};

        for (int i = 1; i < reverseArray.length; i++) {
            if (i != 4)
                htmlString = htmlString + htmlSeperator1 + reverseArray[i] + htmlSeperator2 + resp[i] + htmlSeperator3;
            else
                htmlString = htmlString + htmlSeperator1 + reverseArray[i] + htmlSeperator2 + pan + htmlSeperator3;
        }
        htmlString = htmlStart + htmlString + htmlEnd;

        return htmlString;
    }

    public String printResponseCheckStatus(String[] resp) {

        String htmlString = "";
        String[] checkStatusArray = {"", "TransactionType", "ResponseCode", "Response Message", "Date Time Stamp", "ECRTransactionReferenceNo", "Signature"};
        for (int i = 1; i < checkStatusArray.length; i++) {
            htmlString = htmlString + htmlSeperator1 + checkStatusArray[i] + htmlSeperator2 + resp[i] + htmlSeperator3;
        }
        htmlString = htmlStart + htmlString + htmlEnd;
        return htmlString;
    }

    private String maskPAn(String s) {
        return s.substring(0, 5) + "******" + s.substring(s.length() - 4);
    }
}