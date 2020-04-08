package com.girmiti.skybandecr.fragment.buffer;

import androidx.lifecycle.ViewModel;

public class BufferResponseViewModel extends ViewModel {

    private static final String RESPONSE_CODE = "Response Code:";
    private static final String RESPONSE_MESSAGE = "Response Message:";
    private static final String PAN_NUMBER = "Pan Number:";
    private static final String TRANSACTION_AMOUNT = "Transaction Amount:";
    private static final String STAN_NO = "Stan Number:";
    private static final String DATE_TIME = "Date And Time:";
    private static final String CARD_EXP_DATE = "Card Expiry Date:";
    private static final String RRN_NO = "RRN:";
    private static final String AUTH_CODE = "Authorization Code:";
    private static final String TERMINAL_ID = "TerminalId:";
    private static final String MERCHANT_ID = "Merchant Id:";
    private static final String BATCH_NO = "Batch Number:";
    private static final String AID_NO = "AID:";
    private static final String APPLN_CRYPTOGRAM = "Appln. Cryptogram:";
    private static final String CRYPTOGRAM_INFO_DATA = "Cryptogram Info Data:";
    private static final String CVR = "CVR:";
    private static final String TVR = "TVR:";
    private static final String TSI = "TSI:";
    private static final String CARD_ENTRY_MODE = "Card Entry Mode:";
    private static final String MERCHANT_CATEGORY_CODE = "Merchant Category Code:";
    private static final String TRANSACTION_TYPE = "Transaction Type:";
    private static final String SCHEME_LABEL = "Scheme Label:";
    private static final String STORE_CASHIER_INFO = "Store & Cashier Info:";
    private static final String PRODUCT_INFO = "Product Info:";
    private static final String VERSION = "Version:";

    public String purchase(String[] response) {
        return RESPONSE_CODE + " " + response[2] + "\n" + RESPONSE_MESSAGE + " " + response[3] + "\n" + PAN_NUMBER + " "
                + response[4] + "\n" + TRANSACTION_AMOUNT + " " + response[5] + "\n" + STAN_NO + " " + response[6]
                + "\n" + DATE_TIME + " " + response[7] + "\n" + CARD_EXP_DATE + " " + response[8] + "\n" + RRN_NO + " "
                + response[9] + "\n" + AUTH_CODE + " " + response[10] + "\n" + TERMINAL_ID + " " + response[11] + "\n"
                + MERCHANT_ID + " " + response[12] + "\n" + BATCH_NO + " " + response[13] + "\n" + AID_NO + " "
                + response[14] + "\n" + APPLN_CRYPTOGRAM + " " + response[15] + "\n" + CRYPTOGRAM_INFO_DATA + " "
                + response[16] + "\n" + CVR + " " + response[17] + "\n" + TVR + " " + response[18] + "\n" + TSI + " "
                + response[19] + "\n" + CARD_ENTRY_MODE + " " + response[20] + "\n" + MERCHANT_CATEGORY_CODE + " "
                + response[21] + "\n" + TRANSACTION_TYPE + " " + response[22] + "\n" + SCHEME_LABEL + response[23]
                + "\n" + STORE_CASHIER_INFO + " " + response[24] + "\n" + PRODUCT_INFO + " " + response[25] + "\n"
                + VERSION + " " + response[27];
    }

    public String cashback(String[] response) {
        return RESPONSE_CODE + " " + response[2] + "\n" + RESPONSE_MESSAGE + " " + response[3] + "\n" + PAN_NUMBER + " "
                + response[4] + "\n" + TRANSACTION_AMOUNT + " " + response[5] + "\n" + STAN_NO + " " + response[6]
                + "\n" + DATE_TIME + " " + response[7] + "\n" + CARD_EXP_DATE + " " + response[8] + "\n" + RRN_NO + " "
                + response[9] + "\n" + AUTH_CODE + " " + response[10] + "\n" + TERMINAL_ID + " " + response[11] + "\n"
                + MERCHANT_ID + " " + response[12] + "\n" + BATCH_NO + " " + response[13] + "\n" + AID_NO + " "
                + response[14] + "\n" + APPLN_CRYPTOGRAM + " " + response[15] + "\n" + CRYPTOGRAM_INFO_DATA + " "
                + response[16] + "\n" + CVR + " " + response[17] + "\n" + TVR + " " + response[18] + "\n" + TSI + " "
                + response[19] + "\n" + CARD_ENTRY_MODE + " " + response[20] + "\n" + MERCHANT_CATEGORY_CODE + " "
                + response[21] + "\n" + TRANSACTION_TYPE + " " + response[22] + "\n" + SCHEME_LABEL + response[23]
                + "\n" + STORE_CASHIER_INFO + " " + response[24] + "\n" + PRODUCT_INFO + " " + response[25] + "\n"
                + VERSION + " " + response[27];
    }

    public String refund(String[] response) {
        return RESPONSE_CODE + " " + response[2] + "\n" + RESPONSE_MESSAGE + " " + response[3] + "\n" + PAN_NUMBER + " "
                + response[4] + "\n" + TRANSACTION_AMOUNT + " " + response[5] + "\n" + STAN_NO + " " + response[6]
                + "\n" + DATE_TIME + " " + response[7] + "\n" + CARD_EXP_DATE + " " + response[8] + "\n" + RRN_NO + " "
                + response[9] + "\n" + AUTH_CODE + " " + response[10] + "\n" + TERMINAL_ID + " " + response[11] + "\n"
                + MERCHANT_ID + " " + response[12] + "\n" + BATCH_NO + " " + response[13] + "\n" + AID_NO + " "
                + response[14] + "\n" + APPLN_CRYPTOGRAM + " " + response[15] + "\n" + CRYPTOGRAM_INFO_DATA + " "
                + response[16] + "\n" + CVR + " " + response[17] + "\n" + TVR + " " + response[18] + "\n" + TSI + " "
                + response[19] + "\n" + CARD_ENTRY_MODE + " " + response[20] + "\n" + MERCHANT_CATEGORY_CODE + " "
                + response[21] + "\n" + TRANSACTION_TYPE + " " + response[22] + "\n" + SCHEME_LABEL + response[23]
                + "\n" + STORE_CASHIER_INFO + " " + response[24] + "\n" + PRODUCT_INFO + " " + response[25] + "\n"
                + VERSION + " " + response[27];
    }

    public String preAuth(String[] response) {
        return RESPONSE_CODE + " " + response[2] + "\n" + RESPONSE_MESSAGE + " " + response[3] + "\n" + PAN_NUMBER + " "
                + response[4] + "\n" + TRANSACTION_AMOUNT + " " + response[5] + "\n" + STAN_NO + " " + response[6]
                + "\n" + DATE_TIME + " " + response[7] + "\n" + CARD_EXP_DATE + " " + response[8] + "\n" + RRN_NO + " "
                + response[9] + "\n" + AUTH_CODE + " " + response[10] + "\n" + TERMINAL_ID + " " + response[11] + "\n"
                + MERCHANT_ID + " " + response[12] + "\n" + BATCH_NO + " " + response[13] + "\n" + AID_NO + " "
                + response[14] + "\n" + APPLN_CRYPTOGRAM + " " + response[15] + "\n" + CRYPTOGRAM_INFO_DATA + " "
                + response[16] + "\n" + CVR + " " + response[17] + "\n" + TVR + " " + response[18] + "\n" + TSI + " "
                + response[19] + "\n" + CARD_ENTRY_MODE + " " + response[20] + "\n" + MERCHANT_CATEGORY_CODE + " "
                + response[21] + "\n" + TRANSACTION_TYPE + " " + response[22] + "\n" + SCHEME_LABEL + response[23]
                + "\n" + STORE_CASHIER_INFO + " " + response[24] + "\n" + PRODUCT_INFO + " " + response[25] + "\n"
                + VERSION + " " + response[27];
    }

    public String preAuthAdvice(String[] response) {
        return RESPONSE_CODE + " " + response[2] + "\n" + RESPONSE_MESSAGE + " " + response[3] + "\n" + PAN_NUMBER + " "
                + response[4] + "\n" + TRANSACTION_AMOUNT + " " + response[5] + "\n" + STAN_NO + " " + response[6]
                + "\n" + DATE_TIME + " " + response[7] + "\n" + CARD_EXP_DATE + " " + response[8] + "\n" + RRN_NO + " "
                + response[9] + "\n" + AUTH_CODE + " " + response[10] + "\n" + TERMINAL_ID + " " + response[11] + "\n"
                + MERCHANT_ID + " " + response[12] + "\n" + BATCH_NO + " " + response[13] + "\n" + AID_NO + " "
                + response[14] + "\n" + APPLN_CRYPTOGRAM + " " + response[15] + "\n" + CRYPTOGRAM_INFO_DATA + " "
                + response[16] + "\n" + CVR + " " + response[17] + "\n" + TVR + " " + response[18] + "\n" + TSI + " "
                + response[19] + "\n" + CARD_ENTRY_MODE + " " + response[20] + "\n" + MERCHANT_CATEGORY_CODE + " "
                + response[21] + "\n" + TRANSACTION_TYPE + " " + response[22] + "\n" + SCHEME_LABEL + response[23]
                + "\n" + STORE_CASHIER_INFO + " " + response[24] + "\n" + PRODUCT_INFO + " " + response[25] + "\n"
                + VERSION + " " + response[27];
    }

    public String preAuthExtn(String[] response) {
        return RESPONSE_CODE + " " + response[2] + "\n" + RESPONSE_MESSAGE + " " + response[3] + "\n" + PAN_NUMBER + " "
                + response[4] + "\n" + TRANSACTION_AMOUNT + " " + response[5] + "\n" + STAN_NO + " " + response[6]
                + "\n" + DATE_TIME + " " + response[7] + "\n" + CARD_EXP_DATE + " " + response[8] + "\n" + RRN_NO + " "
                + response[9] + "\n" + AUTH_CODE + " " + response[10] + "\n" + TERMINAL_ID + " " + response[11] + "\n"
                + MERCHANT_ID + " " + response[12] + "\n" + BATCH_NO + " " + response[13] + "\n" + AID_NO + " "
                + response[14] + "\n" + APPLN_CRYPTOGRAM + " " + response[15] + "\n" + CRYPTOGRAM_INFO_DATA + " "
                + response[16] + "\n" + CVR + " " + response[17] + "\n" + TVR + " " + response[18] + "\n" + TSI + " "
                + response[19] + "\n" + CARD_ENTRY_MODE + " " + response[20] + "\n" + MERCHANT_CATEGORY_CODE + " "
                + response[21] + "\n" + TRANSACTION_TYPE + " " + response[22] + "\n" + SCHEME_LABEL + response[23]
                + "\n" + STORE_CASHIER_INFO + " " + response[24] + "\n" + PRODUCT_INFO + " " + response[25] + "\n"
                + VERSION + " " + response[27];
    }

    public String preAuthVoid(String[] response) {
        return RESPONSE_CODE + " " + response[2] + "\n" + RESPONSE_MESSAGE + " " + response[3] + "\n" + PAN_NUMBER + " "
                + response[4] + "\n" + TRANSACTION_AMOUNT + " " + response[5] + "\n" + STAN_NO + " " + response[6]
                + "\n" + DATE_TIME + " " + response[7] + "\n" + CARD_EXP_DATE + " " + response[8] + "\n" + RRN_NO + " "
                + response[9] + "\n" + AUTH_CODE + " " + response[10] + "\n" + TERMINAL_ID + " " + response[11] + "\n"
                + MERCHANT_ID + " " + response[12] + "\n" + BATCH_NO + " " + response[13] + "\n" + AID_NO + " "
                + response[14] + "\n" + APPLN_CRYPTOGRAM + " " + response[15] + "\n" + CRYPTOGRAM_INFO_DATA + " "
                + response[16] + "\n" + CVR + " " + response[17] + "\n" + TVR + " " + response[18] + "\n" + TSI + " "
                + response[19] + "\n" + CARD_ENTRY_MODE + " " + response[20] + "\n" + MERCHANT_CATEGORY_CODE + " "
                + response[21] + "\n" + TRANSACTION_TYPE + " " + response[22] + "\n" + SCHEME_LABEL + response[23]
                + "\n" + STORE_CASHIER_INFO + " " + response[24] + "\n" + PRODUCT_INFO + " " + response[25] + "\n"
                + VERSION + " " + response[27];
    }

    public String cashAdvance(String[] response) {
        return RESPONSE_CODE + " " + response[2] + "\n" + RESPONSE_MESSAGE + " " + response[3] + "\n" + PAN_NUMBER + " "
                + response[4] + "\n" + TRANSACTION_AMOUNT + " " + response[5] + "\n" + STAN_NO + " " + response[6]
                + "\n" + DATE_TIME + " " + response[7] + "\n" + CARD_EXP_DATE + " " + response[8] + "\n" + RRN_NO + " "
                + response[9] + "\n" + AUTH_CODE + " " + response[10] + "\n" + TERMINAL_ID + " " + response[11] + "\n"
                + MERCHANT_ID + " " + response[12] + "\n" + BATCH_NO + " " + response[13] + "\n" + AID_NO + " "
                + response[14] + "\n" + APPLN_CRYPTOGRAM + " " + response[15] + "\n" + CRYPTOGRAM_INFO_DATA + " "
                + response[16] + "\n" + CVR + " " + response[17] + "\n" + TVR + " " + response[18] + "\n" + TSI + " "
                + response[19] + "\n" + CARD_ENTRY_MODE + " " + response[20] + "\n" + MERCHANT_CATEGORY_CODE + " "
                + response[21] + "\n" + TRANSACTION_TYPE + " " + response[22] + "\n" + SCHEME_LABEL + response[23]
                + "\n" + STORE_CASHIER_INFO + " " + response[24] + "\n" + PRODUCT_INFO + " " + response[25] + "\n"
                + VERSION + " " + response[27];
    }

    public String reversal(String[] response) {
        return RESPONSE_CODE + " " + response[2] + "\n" + RESPONSE_MESSAGE + " " + response[3] + "\n" + PAN_NUMBER + " "
                + response[4] + "\n" + TRANSACTION_AMOUNT + " " + response[5] + "\n" + STAN_NO + " " + response[6]
                + "\n" + DATE_TIME + " " + response[7] + "\n" + CARD_EXP_DATE + " " + response[8] + "\n" + RRN_NO + " "
                + response[9] + "\n" + AUTH_CODE + " " + response[10] + "\n" + TERMINAL_ID + " " + response[11] + "\n"
                + MERCHANT_ID + " " + response[12] + "\n" + BATCH_NO + " " + response[13] + "\n" + AID_NO + " "
                + response[14] + "\n" + APPLN_CRYPTOGRAM + " " + response[15] + "\n" + CRYPTOGRAM_INFO_DATA + " "
                + response[16] + "\n" + CVR + " " + response[17] + "\n" + TVR + " " + response[18] + "\n" + TSI + " "
                + response[19] + "\n" + CARD_ENTRY_MODE + " " + response[20] + "\n" + MERCHANT_CATEGORY_CODE + " "
                + response[21] + "\n" + TRANSACTION_TYPE + " " + response[22] + "\n" + SCHEME_LABEL + response[23]
                + "\n" + STORE_CASHIER_INFO + " " + response[24] + "\n" + PRODUCT_INFO + " " + response[25] + "\n"
                + VERSION + " " + response[27];
    }

    public String reconciliation(String[] response) {
        return RESPONSE_CODE + " " + response[2] + "\n" + RESPONSE_MESSAGE + " " + response[3] + "\n" + PAN_NUMBER + " "
                + response[4] + "\n" + TRANSACTION_AMOUNT + " " + response[5] + "\n" + STAN_NO + " " + response[6]
                + "\n" + DATE_TIME + " " + response[7] + "\n" + CARD_EXP_DATE + " " + response[8] + "\n" + RRN_NO + " "
                + response[9] + "\n" + AUTH_CODE + " " + response[10] + "\n" + TERMINAL_ID + " " + response[11] + "\n"
                + MERCHANT_ID + " " + response[12] + "\n" + BATCH_NO + " " + response[13] + "\n" + AID_NO + " "
                + response[14] + "\n" + APPLN_CRYPTOGRAM + " " + response[15] + "\n" + CRYPTOGRAM_INFO_DATA + " "
                + response[16] + "\n" + CVR + " " + response[17] + "\n" + TVR + " " + response[18] + "\n" + TSI + " "
                + response[19] + "\n" + CARD_ENTRY_MODE + " " + response[20] + "\n" + MERCHANT_CATEGORY_CODE + " "
                + response[21] + "\n" + TRANSACTION_TYPE + " " + response[22] + "\n" + SCHEME_LABEL + response[23]
                + "\n" + STORE_CASHIER_INFO + " " + response[24] + "\n" + PRODUCT_INFO + " " + response[25] + "\n"
                + VERSION + " " + response[27];
    }

    public String parameterDownload(String[] response) {
        return RESPONSE_CODE + " " + response[2] + "\n" + RESPONSE_MESSAGE + " " + response[3] + "\n" + PAN_NUMBER + " "
                + response[4] + "\n" + TRANSACTION_AMOUNT + " " + response[5] + "\n" + STAN_NO + " " + response[6]
                + "\n" + DATE_TIME + " " + response[7] + "\n" + CARD_EXP_DATE + " " + response[8] + "\n" + RRN_NO + " "
                + response[9] + "\n" + AUTH_CODE + " " + response[10] + "\n" + TERMINAL_ID + " " + response[11] + "\n"
                + MERCHANT_ID + " " + response[12] + "\n" + BATCH_NO + " " + response[13] + "\n" + AID_NO + " "
                + response[14] + "\n" + APPLN_CRYPTOGRAM + " " + response[15] + "\n" + CRYPTOGRAM_INFO_DATA + " "
                + response[16] + "\n" + CVR + " " + response[17] + "\n" + TVR + " " + response[18] + "\n" + TSI + " "
                + response[19] + "\n" + CARD_ENTRY_MODE + " " + response[20] + "\n" + MERCHANT_CATEGORY_CODE + " "
                + response[21] + "\n" + TRANSACTION_TYPE + " " + response[22] + "\n" + SCHEME_LABEL + response[23]
                + "\n" + STORE_CASHIER_INFO + " " + response[24] + "\n" + PRODUCT_INFO + " " + response[25] + "\n"
                + VERSION + " " + response[27];
    }

    public String setParameter(String[] response) {
        return RESPONSE_CODE + " " + response[2] + "\n" + RESPONSE_MESSAGE + " " + response[3] + "\n" + PAN_NUMBER + " "
                + response[4] + "\n" + TRANSACTION_AMOUNT + " " + response[5] + "\n" + STAN_NO + " " + response[6]
                + "\n" + DATE_TIME + " " + response[7] + "\n" + CARD_EXP_DATE + " " + response[8] + "\n" + RRN_NO + " "
                + response[9] + "\n" + AUTH_CODE + " " + response[10] + "\n" + TERMINAL_ID + " " + response[11] + "\n"
                + MERCHANT_ID + " " + response[12] + "\n" + BATCH_NO + " " + response[13] + "\n" + AID_NO + " "
                + response[14] + "\n" + APPLN_CRYPTOGRAM + " " + response[15] + "\n" + CRYPTOGRAM_INFO_DATA + " "
                + response[16] + "\n" + CVR + " " + response[17] + "\n" + TVR + " " + response[18] + "\n" + TSI + " "
                + response[19] + "\n" + CARD_ENTRY_MODE + " " + response[20] + "\n" + MERCHANT_CATEGORY_CODE + " "
                + response[21] + "\n" + TRANSACTION_TYPE + " " + response[22] + "\n" + SCHEME_LABEL + response[23]
                + "\n" + STORE_CASHIER_INFO + " " + response[24] + "\n" + PRODUCT_INFO + " " + response[25] + "\n"
                + VERSION + " " + response[27];
    }

    public String getParameter(String[] response) {
        return RESPONSE_CODE + " " + response[2] + "\n" + RESPONSE_MESSAGE + " " + response[3] + "\n" + PAN_NUMBER + " "
                + response[4] + "\n" + TRANSACTION_AMOUNT + " " + response[5] + "\n" + STAN_NO + " " + response[6]
                + "\n" + DATE_TIME + " " + response[7] + "\n" + CARD_EXP_DATE + " " + response[8] + "\n" + RRN_NO + " "
                + response[9] + "\n" + AUTH_CODE + " " + response[10] + "\n" + TERMINAL_ID + " " + response[11] + "\n"
                + MERCHANT_ID + " " + response[12] + "\n" + BATCH_NO + " " + response[13] + "\n" + AID_NO + " "
                + response[14] + "\n" + APPLN_CRYPTOGRAM + " " + response[15] + "\n" + CRYPTOGRAM_INFO_DATA + " "
                + response[16] + "\n" + CVR + " " + response[17] + "\n" + TVR + " " + response[18] + "\n" + TSI + " "
                + response[19] + "\n" + CARD_ENTRY_MODE + " " + response[20] + "\n" + MERCHANT_CATEGORY_CODE + " "
                + response[21] + "\n" + TRANSACTION_TYPE + " " + response[22] + "\n" + SCHEME_LABEL + response[23]
                + "\n" + STORE_CASHIER_INFO + " " + response[24] + "\n" + PRODUCT_INFO + " " + response[25] + "\n"
                + VERSION + " " + response[27];
    }

    public String billPayment(String[] response) {
        return RESPONSE_CODE + " " + response[2] + "\n" + RESPONSE_MESSAGE + " " + response[3] + "\n" + PAN_NUMBER + " "
                + response[4] + "\n" + TRANSACTION_AMOUNT + " " + response[5] + "\n" + STAN_NO + " " + response[6]
                + "\n" + DATE_TIME + " " + response[7] + "\n" + CARD_EXP_DATE + " " + response[8] + "\n" + RRN_NO + " "
                + response[9] + "\n" + AUTH_CODE + " " + response[10] + "\n" + TERMINAL_ID + " " + response[11] + "\n"
                + MERCHANT_ID + " " + response[12] + "\n" + BATCH_NO + " " + response[13] + "\n" + AID_NO + " "
                + response[14] + "\n" + APPLN_CRYPTOGRAM + " " + response[15] + "\n" + CRYPTOGRAM_INFO_DATA + " "
                + response[16] + "\n" + CVR + " " + response[17] + "\n" + TVR + " " + response[18] + "\n" + TSI + " "
                + response[19] + "\n" + CARD_ENTRY_MODE + " " + response[20] + "\n" + MERCHANT_CATEGORY_CODE + " "
                + response[21] + "\n" + TRANSACTION_TYPE + " " + response[22] + "\n" + SCHEME_LABEL + response[23]
                + "\n" + STORE_CASHIER_INFO + " " + response[24] + "\n" + PRODUCT_INFO + " " + response[25] + "\n"
                + VERSION + " " + response[27];
    }
}
