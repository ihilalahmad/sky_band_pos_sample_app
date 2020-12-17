package com.skyband.ecr.ui.fragment.response.printreceipt;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.skyband.ecr.constant.Constant;
import com.skyband.ecr.model.ActiveTxnData;
import com.skyband.ecr.sdk.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PrintReceiptViewModel extends ViewModel {

    private Logger logger = Logger.getNewLogger(PrintReceiptViewModel.class.getName());
    private InputStream is;
    private String htmlString = "";
    private String pan;
    private String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());

    public static String numToArabicConverter(String num) {
        int arabicZeroUnicode = 1632;
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < num.length(); ++i) {
            builder.append((char) ((int) num.charAt(i) - 48 + arabicZeroUnicode));
        }
        return builder.toString();

    }

    @SuppressLint("DefaultLocale")
    public String printReceiptPurchase(String[] receiveDataArray, Context context) throws IOException {

        is = context.getResources().getAssets().open("printReceipt/Purchase(customer_copy).html");
        htmlString = getHtmlString(is);
        if (receiveDataArray.length > 27) {
            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[9];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }
            if (receiveDataArray[4].length() > 14) {
                pan = maskPAn(receiveDataArray[4]);
            } else {
                pan = receiveDataArray[4];
            }

            String receiveDataArrayDateTime = receiveDataArray[8];
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / "
                    + receiveDataArrayDateTime.substring(0, 2) + "/" + date;
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :"
                    + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace(Constant.CURRENT_TIME, currentTime);
            htmlString = htmlString.replace(Constant.CURRENT_DATE, currentDate);
            htmlString = htmlString.replace(Constant.ARABICSAR, checkingArabic("SAR"));
            htmlString = htmlString.replace(Constant.AMOUNT_SAR,
                    numToArabicConverter(String.format("%.2f", amount)));
            htmlString = htmlString.replace(Constant.APPROOVAL_CODE_ARABIC,
                    numToArabicConverter(receiveDataArray[11]));
            htmlString = htmlString.replace(Constant.PAN_NUMBER, pan);
            htmlString = htmlString.replace(Constant.AUTH_CODE, receiveDataArray[11]);
            String arabic = checkingArabic(receiveDataArray[3]);
            arabic = arabic.replace(Constant.ARABIC_WORD, "");
            htmlString = htmlString.replace(Constant.BUSS_CODE, receiveDataArray[6]);
            htmlString = htmlString.replace(Constant.ARABIC_WORD1, arabic);
            htmlString = htmlString.replace(Constant.APPROVED, receiveDataArray[3]);
            htmlString = htmlString.replace(Constant.CURRENT_AMOUNT, String.format("%.2f", amount));
            htmlString = htmlString.replace(Constant.EXPIRY_DATE, expiryDate);
            htmlString = htmlString.replace(Constant.CONTACTLESS, receiveDataArray[21]);
            htmlString = htmlString.replace(Constant.RECEIVED_AT_AARRAY_ONSECODE, receiveDataArray[2]);
            htmlString = htmlString.replace(Constant.AID_AID, receiveDataArray[15]);
            htmlString = htmlString.replace(Constant.TVR, receiveDataArray[19]);
            htmlString = htmlString.replace(Constant.CVR, receiveDataArray[18]);
            htmlString = htmlString.replace(Constant.APPLICATION_CRYPTOGRAM, receiveDataArray[16]);
            htmlString = htmlString.replace(Constant.CID, receiveDataArray[17]);
            htmlString = htmlString.replace(Constant.MID, receiveDataArray[13]);
            htmlString = htmlString.replace(Constant.TID, receiveDataArray[12]);
            htmlString = htmlString.replace(Constant.RRN, receiveDataArray[10]);
            htmlString = htmlString.replace(Constant.STAN_NO, receiveDataArray[7]);
            htmlString = htmlString.replace("TSI", receiveDataArray[20]);
            htmlString = htmlString.replace(Constant.APPLICATION_VERSION, receiveDataArray[26]);
            htmlString = htmlString.replace(Constant.SCHEME_LABEL, receiveDataArray[24]);
            htmlString = htmlString.replace(Constant.MERCHANT_CATEGORY_CODE, receiveDataArray[22]);
            htmlString = htmlString.replace( Constant.VERIFICATION_INFO, receiveDataArray[27]);
            htmlString = htmlString.replace(Constant.ARABIC_VERIFICATION_INFO, checkingArabic(receiveDataArray[27]));
            htmlString = htmlString.replace(Constant.MERCHANT_NAME, receiveDataArray[28]);
            htmlString = htmlString.replace(Constant.MERCHANT_ADDRESS, receiveDataArray[29]);
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_NAME, convertToArabic(receiveDataArray[30]));
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_ADDRESS, convertToArabic(receiveDataArray[31]));
        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptPurchaseCashback(String[] receiveDataArray, Context context) throws IOException {

        is = context.getResources().getAssets().open("printReceipt/Purchase_Cashback(customer_copy)).html");
        htmlString = getHtmlString(is);
        if (receiveDataArray.length > 29) {
            double transactionAmount = Double.parseDouble(receiveDataArray[5]) / 100;
            transactionAmount = Math.round(transactionAmount * 100.0) / 100.0;
            double cashBackAmount = Double.parseDouble(receiveDataArray[6]) / 100;
            cashBackAmount = Math.round(cashBackAmount * 100.0) / 100.0;
            double totalAmount = Double.parseDouble(receiveDataArray[7]) / 100;
            totalAmount = Math.round(totalAmount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[11];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }
            if (receiveDataArray[4].length() > 14) {
                pan = maskPAn(receiveDataArray[4]);
            } else {
                pan = receiveDataArray[4];
            }

            String receiveDataArrayDateTime = receiveDataArray[10];
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / "
                    + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :"
                    + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace(Constant.CURRENT_TIME, currentTime);
            htmlString = htmlString.replace(Constant.CURRENT_DATE, currentDate);
            htmlString = htmlString.replace(Constant.ARABICSAR, checkingArabic("SAR"));
            htmlString = htmlString.replace("amountSARPUR",
                    numToArabicConverter((String.format("%.2f", transactionAmount))));
            htmlString = htmlString.replace("amountSARcashback",
                    numToArabicConverter((String.format("%.2f", cashBackAmount))));
            htmlString = htmlString.replace("amountSARtotal",
                    numToArabicConverter((String.format("{%.2f", totalAmount))));
            htmlString = htmlString.replace(Constant.APPROOVAL_CODE_ARABIC,
                    numToArabicConverter(receiveDataArray[13]));
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace(Constant.PAN_NUMBER, pan);
            htmlString = htmlString.replace(Constant.AUTH_CODE, receiveDataArray[13]);
            String arabic = checkingArabic(receiveDataArray[3]);
            arabic = arabic.replace(Constant.ARABIC_WORD, "");
            htmlString = htmlString.replace(Constant.ARABIC_WORD1, arabic);
            htmlString = htmlString.replace(Constant.APPROVED, receiveDataArray[3]);
            htmlString = htmlString.replace(Constant.BUSS_CODE, receiveDataArray[8]);
            htmlString = htmlString.replace("TransactionAmount", String.format("%.2f", transactionAmount));
            htmlString = htmlString.replace("CashbackAmount", String.format("%.2f", cashBackAmount));
            htmlString = htmlString.replace("TotalAmount", String.format("%.2f", totalAmount));
            htmlString = htmlString.replace(Constant.EXPIRY_DATE, expiryDate);
            htmlString = htmlString.replace(Constant.CONTACTLESS, receiveDataArray[23]);
            htmlString = htmlString.replace(Constant.RECEIVED_AT_AARRAY_ONSECODE, receiveDataArray[2]);
            htmlString = htmlString.replace(Constant.AID_AID, receiveDataArray[17]);
            htmlString = htmlString.replace(Constant.TVR, receiveDataArray[21]);
            htmlString = htmlString.replace(Constant.CVR, receiveDataArray[20]);
            htmlString = htmlString.replace(Constant.APPLICATION_CRYPTOGRAM, receiveDataArray[18]);
            htmlString = htmlString.replace(Constant.CID, receiveDataArray[19]);
            htmlString = htmlString.replace(Constant.MID, receiveDataArray[15]);
            htmlString = htmlString.replace(Constant.TID, receiveDataArray[14]);
            htmlString = htmlString.replace(Constant.RRN, receiveDataArray[12]);
            htmlString = htmlString.replace(Constant.STAN_NO, receiveDataArray[9]);
            htmlString = htmlString.replace("TSI", receiveDataArray[22]);
            htmlString = htmlString.replace(Constant.APPLICATION_VERSION, receiveDataArray[28]);
            htmlString = htmlString.replace(Constant.SCHEME_LABEL, receiveDataArray[26]);
            htmlString = htmlString.replace(Constant.MERCHANT_CATEGORY_CODE, receiveDataArray[24]);
            htmlString = htmlString.replace( Constant.VERIFICATION_INFO, receiveDataArray[29]);
            htmlString = htmlString.replace(Constant.ARABIC_VERIFICATION_INFO, checkingArabic(receiveDataArray[29]));
            htmlString = htmlString.replace(Constant.MERCHANT_NAME, receiveDataArray[30]);
            htmlString = htmlString.replace(Constant.MERCHANT_ADDRESS, receiveDataArray[31]);
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_NAME, convertToArabic(receiveDataArray[32]));
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_ADDRESS, convertToArabic(receiveDataArray[33]));

        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptRefund(String[] receiveDataArray, Context context) throws IOException {

        is = context.getResources().getAssets().open("printReceipt/Refund(customer_copy).html");
        htmlString = getHtmlString(is);

        if (receiveDataArray.length > 27) {
            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[9];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }

            String receiveDataArrayDateTime = receiveDataArray[8];
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / "
                    + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :"
                    + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace(Constant.CURRENT_TIME, currentTime);
            htmlString = htmlString.replace(Constant.CURRENT_DATE, currentDate);
            htmlString = htmlString.replace(Constant.ARABICSAR, checkingArabic("SAR"));
            htmlString = htmlString.replace(Constant.AMOUNT_SAR,
                    numToArabicConverter(String.format("%.2f", amount)));
            htmlString = htmlString.replace(Constant.APPROOVAL_CODE_ARABIC,
                    numToArabicConverter(receiveDataArray[11]));
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace(Constant.PAN_NUMBER, maskPAn(receiveDataArray[4]));
            htmlString = htmlString.replace(Constant.AUTH_CODE, receiveDataArray[11]);
            htmlString = htmlString.replace(Constant.BUSS_CODE, receiveDataArray[6]);
            htmlString = htmlString.replace(Constant.APPROVED, receiveDataArray[3]);
            htmlString = htmlString.replace(Constant.CURRENT_AMOUNT, String.format("%.2f", amount));
            htmlString = htmlString.replace(Constant.EXPIRY_DATE, expiryDate);
            htmlString = htmlString.replace(Constant.CONTACTLESS, receiveDataArray[21]);
            htmlString = htmlString.replace(Constant.RECEIVED_AT_AARRAY_ONSECODE, receiveDataArray[2]);
            htmlString = htmlString.replace(Constant.AID_AID, receiveDataArray[15]);
            htmlString = htmlString.replace(Constant.TVR, receiveDataArray[19]);
            htmlString = htmlString.replace(Constant.CVR, receiveDataArray[18]);
            htmlString = htmlString.replace(Constant.APPLICATION_CRYPTOGRAM, receiveDataArray[16]);
            htmlString = htmlString.replace(Constant.CID, receiveDataArray[17]);
            htmlString = htmlString.replace(Constant.MID, receiveDataArray[13]);
            htmlString = htmlString.replace(Constant.TID, receiveDataArray[12]);
            htmlString = htmlString.replace(Constant.RRN, receiveDataArray[10]);
            htmlString = htmlString.replace(Constant.STAN_NO, receiveDataArray[7]);
            htmlString = htmlString.replace("TSI", receiveDataArray[20]);
            htmlString = htmlString.replace(Constant.APPLICATION_VERSION, receiveDataArray[26]);
            htmlString = htmlString.replace(Constant.SCHEME_LABEL, receiveDataArray[24]);
            htmlString = htmlString.replace(Constant.MERCHANT_CATEGORY_CODE, receiveDataArray[22]);
            htmlString = htmlString.replace( Constant.VERIFICATION_INFO, receiveDataArray[27]);
            htmlString = htmlString.replace(Constant.ARABIC_VERIFICATION_INFO, checkingArabic(receiveDataArray[27]));
            htmlString = htmlString.replace(Constant.MERCHANT_NAME, receiveDataArray[28]);
            htmlString = htmlString.replace(Constant.MERCHANT_ADDRESS, receiveDataArray[29]);
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_NAME, convertToArabic(receiveDataArray[30]));
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_ADDRESS, convertToArabic(receiveDataArray[31]));

        }
        return htmlString;

    }

    @SuppressLint("DefaultLocale")
    public String printReceiptPreAuthorisation(String[] receiveDataArray, Context context) throws IOException {

        is = context.getResources().getAssets().open("printReceipt/Pre-Auth(Customer_copy).html");
        htmlString = getHtmlString(is);
        if (receiveDataArray.length > 27) {
            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[9];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }

            String receiveDataArrayDateTime = receiveDataArray[8];
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / "
                    + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :"
                    + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace(Constant.CURRENT_TIME, currentTime);
            htmlString = htmlString.replace(Constant.CURRENT_DATE, currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace(Constant.PAN_NUMBER, maskPAn(receiveDataArray[4]));
            htmlString = htmlString.replace(Constant.AUTH_CODE, receiveDataArray[11]);
            htmlString = htmlString.replace(Constant.APPROVED, receiveDataArray[3]);
            htmlString = htmlString.replace(Constant.CURRENT_AMOUNT, String.format("%.2f", amount));
            htmlString = htmlString.replace(Constant.EXPIRY_DATE, expiryDate);
            htmlString = htmlString.replace(Constant.ARABICSAR, checkingArabic("SAR"));
            htmlString = htmlString.replace(Constant.AMOUNT_SAR,
                    numToArabicConverter(String.format("%.2f", amount)));
            htmlString = htmlString.replace(Constant.APPROOVAL_CODE_ARABIC,
                    numToArabicConverter(receiveDataArray[11]));
            htmlString = htmlString.replace(Constant.BUSS_CODE, receiveDataArray[6]);
            htmlString = htmlString.replace(Constant.CONTACTLESS, receiveDataArray[21]);
            htmlString = htmlString.replace(Constant.RECEIVED_AT_AARRAY_ONSECODE, receiveDataArray[2]);
            htmlString = htmlString.replace(Constant.AID_AID, receiveDataArray[15]);
            htmlString = htmlString.replace(Constant.TVR, receiveDataArray[19]);
            htmlString = htmlString.replace(Constant.CVR, receiveDataArray[18]);
            htmlString = htmlString.replace(Constant.APPLICATION_CRYPTOGRAM, receiveDataArray[16]);
            htmlString = htmlString.replace(Constant.CID, receiveDataArray[17]);
            htmlString = htmlString.replace(Constant.MID, receiveDataArray[13]);
            htmlString = htmlString.replace(Constant.TID, receiveDataArray[12]);
            htmlString = htmlString.replace(Constant.RRN, receiveDataArray[10]);
            htmlString = htmlString.replace(Constant.STAN_NO, receiveDataArray[7]);
            htmlString = htmlString.replace("TSI", receiveDataArray[20]);
            htmlString = htmlString.replace(Constant.APPLICATION_VERSION, receiveDataArray[26]);
            htmlString = htmlString.replace(Constant.SCHEME_LABEL, receiveDataArray[24]);
            htmlString = htmlString.replace(Constant.MERCHANT_CATEGORY_CODE, receiveDataArray[22]);
            htmlString = htmlString.replace( Constant.VERIFICATION_INFO, receiveDataArray[27]);
            htmlString = htmlString.replace(Constant.ARABIC_VERIFICATION_INFO, checkingArabic(receiveDataArray[27]));
            htmlString = htmlString.replace(Constant.MERCHANT_NAME, receiveDataArray[28]);
            htmlString = htmlString.replace(Constant.MERCHANT_ADDRESS, receiveDataArray[29]);
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_NAME, convertToArabic(receiveDataArray[30]));
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_ADDRESS, convertToArabic(receiveDataArray[31]));
        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptPurchaseAdvice(String[] receiveDataArray, Context context) throws IOException {

        is = context.getResources().getAssets().open("printReceipt/Purchase_Advice(Customer_copy).html");
        htmlString = getHtmlString(is);
        if (receiveDataArray.length > 27) {

            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[9];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }
            String receiveDataArrayDateTime = receiveDataArray[8];
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / "
                    + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :"
                    + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace(Constant.CURRENT_TIME, currentTime);
            htmlString = htmlString.replace(Constant.CURRENT_DATE, currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace(Constant.PAN_NUMBER, maskPAn(receiveDataArray[4]));
            htmlString = htmlString.replace(Constant.ARABICSAR, checkingArabic("SAR"));
            htmlString = htmlString.replace(Constant.AMOUNT_SAR,
                    numToArabicConverter(String.format("%.2f", amount)));
            htmlString = htmlString.replace(Constant.APPROOVAL_CODE_ARABIC,
                    numToArabicConverter(receiveDataArray[11]));
            htmlString = htmlString.replace(Constant.BUSS_CODE, receiveDataArray[6]);
            htmlString = htmlString.replace(Constant.AUTH_CODE, receiveDataArray[11]);
            htmlString = htmlString.replace(Constant.APPROVED, receiveDataArray[3]);
            htmlString = htmlString.replace(Constant.CURRENT_AMOUNT, String.format("%.2f", amount));
            htmlString = htmlString.replace(Constant.EXPIRY_DATE, expiryDate);
            htmlString = htmlString.replace(Constant.CONTACTLESS, receiveDataArray[21]);
            htmlString = htmlString.replace(Constant.RECEIVED_AT_AARRAY_ONSECODE, receiveDataArray[2]);
            htmlString = htmlString.replace(Constant.AID_AID, receiveDataArray[15]);
            htmlString = htmlString.replace(Constant.TVR, receiveDataArray[19]);
            htmlString = htmlString.replace(Constant.CVR, receiveDataArray[18]);
            htmlString = htmlString.replace(Constant.APPLICATION_CRYPTOGRAM, receiveDataArray[16]);
            htmlString = htmlString.replace(Constant.CID, receiveDataArray[17]);
            htmlString = htmlString.replace(Constant.MID, receiveDataArray[13]);
            htmlString = htmlString.replace(Constant.TID, receiveDataArray[12]);
            htmlString = htmlString.replace(Constant.RRN, receiveDataArray[10]);
            htmlString = htmlString.replace(Constant.STAN_NO, receiveDataArray[7]);
            htmlString = htmlString.replace("TSI", receiveDataArray[20]);
            htmlString = htmlString.replace(Constant.APPLICATION_VERSION, receiveDataArray[26]);
            htmlString = htmlString.replace(Constant.SCHEME_LABEL, receiveDataArray[24]);
            htmlString = htmlString.replace(Constant.MERCHANT_CATEGORY_CODE, receiveDataArray[22]);
            htmlString = htmlString.replace( Constant.VERIFICATION_INFO, receiveDataArray[27]);
            htmlString = htmlString.replace(Constant.ARABIC_VERIFICATION_INFO, checkingArabic(receiveDataArray[27]));
            htmlString = htmlString.replace(Constant.MERCHANT_NAME, receiveDataArray[28]);
            htmlString = htmlString.replace(Constant.MERCHANT_ADDRESS, receiveDataArray[29]);
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_NAME, convertToArabic(receiveDataArray[30]));
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_ADDRESS, convertToArabic(receiveDataArray[31]));
        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptPreAuthExtension(String[] receiveDataArray, Context context) throws IOException {

        is = context.getResources().getAssets().open("printReceipt/Pre-Extension(Customer_copy).html");
        htmlString = getHtmlString(is);
        if (receiveDataArray.length > 27) {

            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[9];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }
            String receiveDataArrayDateTime = receiveDataArray[8];
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / "
                    + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :"
                    + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace(Constant.CURRENT_TIME, currentTime);
            htmlString = htmlString.replace(Constant.CURRENT_DATE, currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace(Constant.PAN_NUMBER, maskPAn(receiveDataArray[4]));
            htmlString = htmlString.replace(Constant.ARABICSAR, checkingArabic("SAR"));
            htmlString = htmlString.replace(Constant.AMOUNT_SAR,
                    numToArabicConverter(String.format("%.2f", amount)));
            htmlString = htmlString.replace(Constant.APPROOVAL_CODE_ARABIC,
                    numToArabicConverter(receiveDataArray[11]));
            htmlString = htmlString.replace(Constant.BUSS_CODE, receiveDataArray[6]);
            htmlString = htmlString.replace(Constant.AUTH_CODE, receiveDataArray[11]);
            htmlString = htmlString.replace(Constant.APPROVED, receiveDataArray[3]);
            htmlString = htmlString.replace(Constant.CURRENT_AMOUNT, String.format("%.2f", amount));
            htmlString = htmlString.replace(Constant.EXPIRY_DATE, expiryDate);
            htmlString = htmlString.replace(Constant.CONTACTLESS, receiveDataArray[21]);
            htmlString = htmlString.replace(Constant.RECEIVED_AT_AARRAY_ONSECODE, receiveDataArray[2]);
            htmlString = htmlString.replace(Constant.AID_AID, receiveDataArray[15]);
            htmlString = htmlString.replace(Constant.TVR, receiveDataArray[19]);
            htmlString = htmlString.replace(Constant.CVR, receiveDataArray[18]);
            htmlString = htmlString.replace(Constant.APPLICATION_CRYPTOGRAM, receiveDataArray[16]);
            htmlString = htmlString.replace(Constant.CID, receiveDataArray[17]);
            htmlString = htmlString.replace(Constant.MID, receiveDataArray[13]);
            htmlString = htmlString.replace(Constant.TID, receiveDataArray[12]);
            htmlString = htmlString.replace(Constant.RRN, receiveDataArray[10]);
            htmlString = htmlString.replace(Constant.STAN_NO, receiveDataArray[7]);
            htmlString = htmlString.replace("TSI", receiveDataArray[20]);
            htmlString = htmlString.replace(Constant.APPLICATION_VERSION, receiveDataArray[26]);
            htmlString = htmlString.replace(Constant.SCHEME_LABEL, receiveDataArray[24]);
            htmlString = htmlString.replace(Constant.MERCHANT_CATEGORY_CODE, receiveDataArray[22]);
            htmlString = htmlString.replace( Constant.VERIFICATION_INFO, receiveDataArray[27]);
            htmlString = htmlString.replace(Constant.ARABIC_VERIFICATION_INFO, checkingArabic(receiveDataArray[27]));
            htmlString = htmlString.replace(Constant.MERCHANT_NAME, receiveDataArray[28]);
            htmlString = htmlString.replace(Constant.MERCHANT_ADDRESS, receiveDataArray[29]);
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_NAME, convertToArabic(receiveDataArray[30]));
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_ADDRESS, convertToArabic(receiveDataArray[31]));
        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptPreAuthVoid(String[] receiveDataArray, Context context) throws IOException {

        is = context.getResources().getAssets().open("printReceipt/Pre-void(Customer_copy).html");
        htmlString = getHtmlString(is);
        if (receiveDataArray.length > 27) {
            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[9];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }
            String receiveDataArrayDateTime = receiveDataArray[8];
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / "
                    + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :"
                    + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace(Constant.CURRENT_TIME, currentTime);
            htmlString = htmlString.replace(Constant.CURRENT_DATE, currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace(Constant.PAN_NUMBER, maskPAn(receiveDataArray[4]));
            htmlString = htmlString.replace(Constant.ARABICSAR, checkingArabic("SAR"));
            htmlString = htmlString.replace(Constant.AMOUNT_SAR,
                    numToArabicConverter(String.format("%.2f", amount)));
            htmlString = htmlString.replace(Constant.APPROOVAL_CODE_ARABIC,
                    numToArabicConverter(receiveDataArray[11]));
            htmlString = htmlString.replace(Constant.BUSS_CODE, receiveDataArray[6]);
            htmlString = htmlString.replace(Constant.AUTH_CODE, receiveDataArray[11]);
            htmlString = htmlString.replace(Constant.APPROVED, receiveDataArray[3]);
            htmlString = htmlString.replace(Constant.CURRENT_AMOUNT, String.format("%.2f", amount));
            htmlString = htmlString.replace(Constant.EXPIRY_DATE, expiryDate);
            htmlString = htmlString.replace(Constant.CONTACTLESS, receiveDataArray[21]);
            htmlString = htmlString.replace(Constant.RECEIVED_AT_AARRAY_ONSECODE, receiveDataArray[2]);
            htmlString = htmlString.replace(Constant.AID_AID, receiveDataArray[15]);
            htmlString = htmlString.replace(Constant.TVR, receiveDataArray[19]);
            htmlString = htmlString.replace(Constant.CVR, receiveDataArray[18]);
            htmlString = htmlString.replace(Constant.APPLICATION_CRYPTOGRAM, receiveDataArray[16]);
            htmlString = htmlString.replace(Constant.CID, receiveDataArray[17]);
            htmlString = htmlString.replace(Constant.MID, receiveDataArray[13]);
            htmlString = htmlString.replace(Constant.TID, receiveDataArray[12]);
            htmlString = htmlString.replace(Constant.RRN, receiveDataArray[10]);
            htmlString = htmlString.replace(Constant.STAN_NO, receiveDataArray[7]);
            htmlString = htmlString.replace("TSI", receiveDataArray[20]);
            htmlString = htmlString.replace(Constant.APPLICATION_VERSION, receiveDataArray[26]);
            htmlString = htmlString.replace(Constant.SCHEME_LABEL, receiveDataArray[24]);
            htmlString = htmlString.replace(Constant.MERCHANT_CATEGORY_CODE, receiveDataArray[22]);
            htmlString = htmlString.replace( Constant.VERIFICATION_INFO, receiveDataArray[27]);
            htmlString = htmlString.replace(Constant.ARABIC_VERIFICATION_INFO, checkingArabic(receiveDataArray[27]));
            htmlString = htmlString.replace(Constant.MERCHANT_NAME, receiveDataArray[28]);
            htmlString = htmlString.replace(Constant.MERCHANT_ADDRESS, receiveDataArray[29]);
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_NAME, convertToArabic(receiveDataArray[30]));
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_ADDRESS, convertToArabic(receiveDataArray[31]));
        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptCashAdvance(String[] receiveDataArray, Context context) throws IOException {

        is = context.getResources().getAssets().open("printReceipt/Cash_Advance(Customer_copy).html");
        htmlString = getHtmlString(is);
        if (receiveDataArray.length > 27) {
            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[9];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }
            String receiveDataArrayDateTime = receiveDataArray[8];
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / "
                    + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :"
                    + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace(Constant.ARABICSAR, checkingArabic("SAR"));
            htmlString = htmlString.replace(Constant.AMOUNT_SAR,
                    numToArabicConverter(String.format("%.2f", amount)));
            htmlString = htmlString.replace(Constant.APPROOVAL_CODE_ARABIC,
                    numToArabicConverter(receiveDataArray[11]));
            htmlString = htmlString.replace(Constant.CURRENT_TIME, currentTime);
            htmlString = htmlString.replace(Constant.CURRENT_DATE, currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace(Constant.PAN_NUMBER, maskPAn(receiveDataArray[4]));
            htmlString = htmlString.replace(Constant.AUTH_CODE, receiveDataArray[11]);
            htmlString = htmlString.replace(Constant.BUSS_CODE, receiveDataArray[6]);
            htmlString = htmlString.replace(Constant.APPROVED, receiveDataArray[3]);
            htmlString = htmlString.replace(Constant.CURRENT_AMOUNT, String.format("%.2f", amount));
            htmlString = htmlString.replace(Constant.EXPIRY_DATE, expiryDate);
            htmlString = htmlString.replace(Constant.CONTACTLESS, receiveDataArray[21]);
            htmlString = htmlString.replace(Constant.RECEIVED_AT_AARRAY_ONSECODE, receiveDataArray[2]);
            htmlString = htmlString.replace(Constant.AID_AID, receiveDataArray[15]);
            htmlString = htmlString.replace(Constant.TVR, receiveDataArray[19]);
            htmlString = htmlString.replace(Constant.CVR, receiveDataArray[18]);
            htmlString = htmlString.replace(Constant.APPLICATION_CRYPTOGRAM, receiveDataArray[16]);
            htmlString = htmlString.replace(Constant.CID, receiveDataArray[17]);
            htmlString = htmlString.replace(Constant.MID, receiveDataArray[13]);
            htmlString = htmlString.replace(Constant.TID, receiveDataArray[12]);
            htmlString = htmlString.replace(Constant.RRN, receiveDataArray[10]);
            htmlString = htmlString.replace(Constant.STAN_NO, receiveDataArray[7]);
            htmlString = htmlString.replace("TSI", receiveDataArray[20]);
            htmlString = htmlString.replace(Constant.APPLICATION_VERSION, receiveDataArray[26]);
            htmlString = htmlString.replace(Constant.SCHEME_LABEL, receiveDataArray[24]);
            htmlString = htmlString.replace(Constant.MERCHANT_CATEGORY_CODE, receiveDataArray[22]);
            htmlString = htmlString.replace( Constant.VERIFICATION_INFO, receiveDataArray[27]);
            htmlString = htmlString.replace(Constant.ARABIC_VERIFICATION_INFO, checkingArabic(receiveDataArray[27]));
            htmlString = htmlString.replace(Constant.MERCHANT_NAME, receiveDataArray[28]);
            htmlString = htmlString.replace(Constant.MERCHANT_ADDRESS, receiveDataArray[29]);
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_NAME, convertToArabic(receiveDataArray[30]));
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_ADDRESS, convertToArabic(receiveDataArray[31]));

        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptReversal(String[] receiveDataArray, Context context) throws IOException {

        is = context.getResources().getAssets().open("printReceipt/Reversal(Customer_copy).html");
        htmlString = getHtmlString(is);

        if (receiveDataArray.length > 27) {
            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[9];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }
            String receiveDataArrayDateTime = receiveDataArray[8];
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / "
                    + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :"
                    + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace(Constant.CURRENT_TIME, currentTime);
            htmlString = htmlString.replace(Constant.CURRENT_DATE, currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace(Constant.PAN_NUMBER, maskPAn(receiveDataArray[4]));
            htmlString = htmlString.replace(Constant.ARABICSAR, checkingArabic("SAR"));
            htmlString = htmlString.replace(Constant.AMOUNT_SAR,
                    numToArabicConverter(String.format("%.2f", amount)));
            htmlString = htmlString.replace(Constant.APPROOVAL_CODE_ARABIC,
                    numToArabicConverter(receiveDataArray[11]));
            htmlString = htmlString.replace(Constant.BUSS_CODE, receiveDataArray[6]);
            htmlString = htmlString.replace(Constant.AUTH_CODE, receiveDataArray[11]);
            htmlString = htmlString.replace(Constant.APPROVED, receiveDataArray[3]);
            htmlString = htmlString.replace(Constant.CURRENT_AMOUNT, String.format("%.2f", amount));
            htmlString = htmlString.replace(Constant.EXPIRY_DATE, expiryDate);
            htmlString = htmlString.replace(Constant.CONTACTLESS, receiveDataArray[21]);
            htmlString = htmlString.replace(Constant.RECEIVED_AT_AARRAY_ONSECODE, receiveDataArray[2]);
            htmlString = htmlString.replace(Constant.AID_AID, receiveDataArray[15]);
            htmlString = htmlString.replace(Constant.TVR, receiveDataArray[19]);
            htmlString = htmlString.replace(Constant.CVR, receiveDataArray[18]);
            htmlString = htmlString.replace(Constant.APPLICATION_CRYPTOGRAM, receiveDataArray[16]);
            htmlString = htmlString.replace(Constant.CID, receiveDataArray[17]);
            htmlString = htmlString.replace(Constant.MID, receiveDataArray[13]);
            htmlString = htmlString.replace(Constant.TID, receiveDataArray[12]);
            htmlString = htmlString.replace(Constant.RRN, receiveDataArray[10]);
            htmlString = htmlString.replace("TSI", receiveDataArray[20]);
            htmlString = htmlString.replace(Constant.STAN_NO, receiveDataArray[7]);
            htmlString = htmlString.replace(Constant.APPLICATION_VERSION, receiveDataArray[26]);
            htmlString = htmlString.replace(Constant.SCHEME_LABEL, receiveDataArray[24]);
            htmlString = htmlString.replace(Constant.MERCHANT_CATEGORY_CODE, receiveDataArray[22]);
            htmlString = htmlString.replace( Constant.VERIFICATION_INFO, receiveDataArray[27]);
            htmlString = htmlString.replace(Constant.ARABIC_VERIFICATION_INFO, checkingArabic(receiveDataArray[27]));
            htmlString = htmlString.replace(Constant.MERCHANT_NAME, receiveDataArray[28]);
            htmlString = htmlString.replace(Constant.MERCHANT_ADDRESS, receiveDataArray[29]);
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_NAME, convertToArabic(receiveDataArray[30]));
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_ADDRESS, convertToArabic(receiveDataArray[31]));

        }

        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptReconciliation(String[] receiveDataArray, Context context) throws IOException {

        String summaryHtmlString = "";
        String madaHostTable = "";
        String posTerminalDetails = "";
        StringBuilder summaryFinalReport = new StringBuilder();
        String reconcilationTable = "";
        String reconcilationTable1 = "";

        if (receiveDataArray[2].equals("500") || receiveDataArray[2].equals("501")) {

            is = context.getResources().getAssets().open("printReceipt/Reconcilation.html");
            htmlString = getHtmlString(is);

            String respDateTime = receiveDataArray[4];
            String currentDate = respDateTime.substring(2, 4) + " / "
                    + respDateTime.substring(0, 2) + " / " + date;
            String currentTime = respDateTime.substring(4, 6) + " :"
                    + respDateTime.substring(6, 8) + " : " + respDateTime.substring(8, 10);

            int b = 9;
            int totalSchemeLengthL = Integer.parseInt(receiveDataArray[9]);
            for (int j = 1; j <= totalSchemeLengthL; j++) {

                if (receiveDataArray[b + 2].equalsIgnoreCase("0")) {

                    is = context.getResources().getAssets().open("printReceipt/ReconcilationTable.html");
                    reconcilationTable = getHtmlString(is);

                    String arabi = checkingArabic(receiveDataArray[b + 1]);
                    arabi = arabi.replace("\u08F1", "");
                    reconcilationTable = reconcilationTable.replace("Scheme", receiveDataArray[b + 1]);
                    reconcilationTable = reconcilationTable.replace("قَدِيرٞ", arabi);
                    b = b + 3;
                    summaryFinalReport.append(reconcilationTable);
                } else {
                    is = context.getResources().getAssets().open("printReceipt/MadaHostTable.html");
                    madaHostTable = getHtmlString(is);
                    if (receiveDataArray[b + 3].equalsIgnoreCase("mada Host")) {
                        j = j - 1;
                        String arabic = checkingArabic(receiveDataArray[b + 1]);
                        arabic = arabic.replace("\u08F1", "");
                        madaHostTable = madaHostTable.replace("قَدِيرٞ", arabic);
                        madaHostTable = madaHostTable.replace("schemename", receiveDataArray[b + 1]);
                        madaHostTable = madaHostTable.replace("totalDBCount", receiveDataArray[b + 4]);
                        madaHostTable = madaHostTable.replace("totalDBAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 5])) / 100));
                        madaHostTable = madaHostTable.replace("totalCBCount", receiveDataArray[b + 6]);
                        madaHostTable = madaHostTable.replace("totalCBAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 7])) / 100));
                        madaHostTable = madaHostTable.replace("NAQDCount", receiveDataArray[b + 8]);
                        madaHostTable = madaHostTable.replace("NAQDAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 9])) / 100));
                        madaHostTable = madaHostTable.replace("CADVCount", receiveDataArray[b + 10]);
                        madaHostTable = madaHostTable.replace("CADVAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 11])) / 100));
                        madaHostTable = madaHostTable.replace("AUTHCount", receiveDataArray[b + 12]);
                        madaHostTable = madaHostTable.replace("AUTHAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 13])) / 100));
                        madaHostTable = madaHostTable.replace("TOTALSCount", receiveDataArray[b + 14]);
                        madaHostTable = madaHostTable.replace("TOTALSAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 15])) / 100));
                        b = b + 15;
                        summaryFinalReport.append(madaHostTable);
                    } else if (receiveDataArray[b + 2].equalsIgnoreCase("POS TERMINAL")) {
                        j = j - 1;
                        is = context.getResources().getAssets().open("printReceipt/PosTable.html");
                        summaryHtmlString = getHtmlString(is);

                        summaryHtmlString = summaryHtmlString.replace("totalDBCount", receiveDataArray[b + 3]);
                        summaryHtmlString = summaryHtmlString.replace("totalDBAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 4])) / 100));
                        summaryHtmlString = summaryHtmlString.replace("totalCBCount", receiveDataArray[b + 5]);
                        summaryHtmlString = summaryHtmlString.replace("totalCBAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 6])) / 100));
                        summaryHtmlString = summaryHtmlString.replace("NAQDCount", receiveDataArray[b + 7]);
                        summaryHtmlString = summaryHtmlString.replace("NAQDAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 8])) / 100));
                        summaryHtmlString = summaryHtmlString.replace("CADVCount", receiveDataArray[b + 9]);
                        summaryHtmlString = summaryHtmlString.replace("CADVAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 10])) / 100));
                        summaryHtmlString = summaryHtmlString.replace("AUTHCount", receiveDataArray[b + 11]);
                        summaryHtmlString = summaryHtmlString.replace("AUTHAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 12])) / 100));
                        summaryHtmlString = summaryHtmlString.replace("TOTALSCount", receiveDataArray[b + 13]);
                        summaryHtmlString = summaryHtmlString.replace("TOTALSAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 14])) / 100));
                        b = b + 14;
                        summaryFinalReport.append(summaryHtmlString);
                    } else if (receiveDataArray[b + 2].equalsIgnoreCase("POS TERMINAL DETAILS")) {
                        j = j - 1;
                        is = context.getResources().getAssets().open("printReceipt/PosTerminalDetails.html");
                        posTerminalDetails = getHtmlString(is);

                        posTerminalDetails = posTerminalDetails.replace("totalDBCount", receiveDataArray[b + 3]);
                        posTerminalDetails = posTerminalDetails.replace("totalDBAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 4])) / 100));
                        posTerminalDetails = posTerminalDetails.replace("totalCBCount", receiveDataArray[b + 5]);
                        posTerminalDetails = posTerminalDetails.replace("totalCBAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 6])) / 100));
                        posTerminalDetails = posTerminalDetails.replace("NAQDCount", receiveDataArray[b + 7]);
                        posTerminalDetails = posTerminalDetails.replace("NAQDAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 8])) / 100));
                        posTerminalDetails = posTerminalDetails.replace("CADVCount", receiveDataArray[b + 9]);
                        posTerminalDetails = posTerminalDetails.replace("CADVAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 10])) / 100));
                        posTerminalDetails = posTerminalDetails.replace("AUTHCount", receiveDataArray[b + 11]);
                        posTerminalDetails = posTerminalDetails.replace("AUTHAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 12])) / 100));
                        posTerminalDetails = posTerminalDetails.replace("TOTALSCount", receiveDataArray[b + 13]);
                        posTerminalDetails = posTerminalDetails.replace("TOTALSAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 14])) / 100));
                        b = b + 14;
                        summaryFinalReport.append(posTerminalDetails);
                    } else if (receiveDataArray[b + 1].equalsIgnoreCase("0")) {

                        is = context.getResources().getAssets().open("printReceipt/ReconcilationTable1.html");
                        reconcilationTable1 = getHtmlString(is);

                        summaryFinalReport.append(reconcilationTable1);
                        b = b + 1;
                    }

                }

            }
            htmlString = htmlString.replace("PosTable", summaryFinalReport);
            htmlString = htmlString.replace("merchantId", receiveDataArray[5]);
            htmlString = htmlString.replace(Constant.BUSS_CODE, receiveDataArray[6]);
            htmlString = htmlString.replace("traceNumber", receiveDataArray[7]);
            htmlString = htmlString.replace("currentTime", currentTime);
            htmlString = htmlString.replace("currentDate", currentDate);
            htmlString = htmlString.replace("AppVersion", receiveDataArray[8]);
            htmlString = htmlString.replace("TerminalId", ActiveTxnData.getInstance().getTerminalID());
            htmlString = htmlString.replace(Constant.MERCHANT_NAME, receiveDataArray[b + 1]);
            htmlString = htmlString.replace(Constant.MERCHANT_ADDRESS, receiveDataArray[b + 2]);
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_NAME, convertToArabic(receiveDataArray[b + 3]));
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_ADDRESS, convertToArabic(receiveDataArray[b + 4]));

            return htmlString;
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptBillPayment(String[] receiveDataArray, Context context) throws IOException {

        is = context.getResources().getAssets().open("printReceipt/Bill_Payment(Customer_copy).html");
        htmlString = getHtmlString(is);

        if (receiveDataArray.length > 27) {
            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[9];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }
            String receiveDataArrayDateTime = receiveDataArray[8];
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / "
                    + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :"
                    + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace(Constant.CURRENT_TIME, currentTime);
            htmlString = htmlString.replace(Constant.CURRENT_DATE, currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace(Constant.PAN_NUMBER, maskPAn(receiveDataArray[4]));
            htmlString = htmlString.replace(Constant.ARABICSAR, checkingArabic("SAR"));
            htmlString = htmlString.replace(Constant.AMOUNT_SAR,
                    numToArabicConverter(String.format("%.2f", amount)));
            htmlString = htmlString.replace(Constant.APPROOVAL_CODE_ARABIC,
                    numToArabicConverter(receiveDataArray[11]));
            htmlString = htmlString.replace(Constant.BUSS_CODE, receiveDataArray[6]);
            htmlString = htmlString.replace(Constant.AUTH_CODE, receiveDataArray[11]);
            htmlString = htmlString.replace(Constant.APPROVED, receiveDataArray[3]);
            htmlString = htmlString.replace(Constant.CURRENT_AMOUNT, String.format("%.2f", amount));
            htmlString = htmlString.replace(Constant.EXPIRY_DATE, expiryDate);
            htmlString = htmlString.replace(Constant.CONTACTLESS, receiveDataArray[21]);
            htmlString = htmlString.replace(Constant.RECEIVED_AT_AARRAY_ONSECODE, receiveDataArray[2]);
            htmlString = htmlString.replace(Constant.AID_AID, receiveDataArray[15]);
            htmlString = htmlString.replace(Constant.TVR, receiveDataArray[19]);
            htmlString = htmlString.replace(Constant.CVR, receiveDataArray[18]);
            htmlString = htmlString.replace(Constant.APPLICATION_CRYPTOGRAM, receiveDataArray[16]);
            htmlString = htmlString.replace(Constant.CID, receiveDataArray[17]);
            htmlString = htmlString.replace(Constant.MID, receiveDataArray[13]);
            htmlString = htmlString.replace(Constant.TID, receiveDataArray[12]);
            htmlString = htmlString.replace(Constant.RRN, receiveDataArray[10]);
            htmlString = htmlString.replace(Constant.STAN_NO, receiveDataArray[7]);
            htmlString = htmlString.replace("TSI", receiveDataArray[20]);
            htmlString = htmlString.replace(Constant.APPLICATION_VERSION, receiveDataArray[26]);
            htmlString = htmlString.replace(Constant.SCHEME_LABEL, receiveDataArray[24]);
            htmlString = htmlString.replace(Constant.MERCHANT_CATEGORY_CODE, receiveDataArray[22]);
            htmlString = htmlString.replace( Constant.VERIFICATION_INFO, receiveDataArray[27]);
            htmlString = htmlString.replace(Constant.ARABIC_VERIFICATION_INFO, checkingArabic(receiveDataArray[27]));
            htmlString = htmlString.replace(Constant.MERCHANT_NAME, receiveDataArray[28]);
            htmlString = htmlString.replace(Constant.MERCHANT_ADDRESS, receiveDataArray[29]);
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_NAME, convertToArabic(receiveDataArray[30]));
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_ADDRESS, convertToArabic(receiveDataArray[31]));

        }
        return htmlString;
    }

    public String printReceiptParameterDownload(String[] receiveDataArray, Context context) throws IOException {

        is = context.getResources().getAssets().open("printReceipt/Parameter_download.html");
        htmlString = getHtmlString(is);

        if (receiveDataArray.length > 4) {
            String receiveDataArrayDateTime = receiveDataArray[4];
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / "
                    + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :"
                    + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace(Constant.CURRENT_TIME, currentTime);
            htmlString = htmlString.replace(Constant.CURRENT_DATE, currentDate);
            htmlString = htmlString.replace(Constant.TERMINAL_ID,ActiveTxnData.getInstance().getTerminalID().substring(0, 8));
            htmlString = htmlString.replace("responseCode", receiveDataArray[2]);
        }

        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptRunningTotal(String[] receiveDataArray, Context context) throws IOException {

        String posTableRunning = "";
        String posTerminalDetails = "";
        String reconcilationTable = "";

        if (Integer.parseInt(receiveDataArray[2]) == 0) {
            is = context.getResources().getAssets().open("printReceipt/Detail_Report.html");
            htmlString = getHtmlString(is);

            is = context.getResources().getAssets().open("printReceipt/PosTableRunning.html");
            posTableRunning = getHtmlString(is);

            is = context.getResources().getAssets().open("printReceipt/PosTerminalDetails.html");
            posTerminalDetails = getHtmlString(is);

            StringBuilder summaryFinalReport = new StringBuilder();
            String receiveDataArrayDateTime = receiveDataArray[4];
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / "
                    + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :"
                    + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);

            int b = 8;
            int totalSchemeLengthL = Integer.parseInt(receiveDataArray[8]);

            for (int j = 1; j <= totalSchemeLengthL; j++) {

                if (receiveDataArray[b + 2].equals("0")) {

                    is = context.getResources().getAssets().open("printReceipt/ReconcilationTable.html");
                    reconcilationTable = getHtmlString(is);

                    String arabi = checkingArabic(receiveDataArray[b + 1]);
                    arabi = arabi.replace(Constant.ARABIC_WORD, "");
                    reconcilationTable = reconcilationTable.replace("Scheme", receiveDataArray[b + 1]);
                    reconcilationTable = reconcilationTable.replace(Constant.ARABIC_WORD2, arabi);
                    b = b + 2;
                    summaryFinalReport.append(reconcilationTable);
                } else {
                    if (receiveDataArray[b + 3].equalsIgnoreCase("POS TERMINAL")) {
                        String arabi = checkingArabic(receiveDataArray[b + 1]);
                        arabi = arabi.replace(Constant.ARABIC_WORD, "");
                        posTableRunning = posTableRunning.replace("مدى", arabi);
                        posTableRunning = posTableRunning.replace("schemename", receiveDataArray[b + 1]);
                        posTableRunning = posTableRunning.replace("totalDBCount", receiveDataArray[b + 4]);
                        posTableRunning = posTableRunning.replace("totalDBAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 5])) / 100));
                        posTableRunning = posTableRunning.replace("totalCBCount", receiveDataArray[b + 6]);
                        posTableRunning = posTableRunning.replace("totalCBAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 7])) / 100));
                        posTableRunning = posTableRunning.replace("NAQDCount", receiveDataArray[b + 8]);
                        posTableRunning = posTableRunning.replace("NAQDAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 9])) / 100));
                        posTableRunning = posTableRunning.replace("CADVCount", receiveDataArray[b + 10]);
                        posTableRunning = posTableRunning.replace("CADVAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 11])) / 100));
                        posTableRunning = posTableRunning.replace("AUTHCount", receiveDataArray[b + 12]);
                        posTableRunning = posTableRunning.replace("AUTHAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 13])) / 100));
                        posTableRunning = posTableRunning.replace("TOTALSCount", receiveDataArray[b + 14]);
                        posTableRunning = posTableRunning.replace("TOTALSAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 15])) / 100));
                        b = b + 15;
                        summaryFinalReport.append(posTableRunning);
                    } else if (receiveDataArray[b + 2].equalsIgnoreCase("POS TERMINAL DETAILS")) {
                        j = j - 1;
                        posTerminalDetails = posTerminalDetails.replace("totalDBCount", receiveDataArray[b + 3]);
                        posTerminalDetails = posTerminalDetails.replace("totalDBAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 4])) / 100));
                        posTerminalDetails = posTerminalDetails.replace("totalCBCount", receiveDataArray[b + 5]);
                        posTerminalDetails = posTerminalDetails.replace("totalCBAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 6])) / 100));
                        posTerminalDetails = posTerminalDetails.replace("NAQDCount", receiveDataArray[b + 7]);
                        posTerminalDetails = posTerminalDetails.replace("NAQDAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 8])) / 100));
                        posTerminalDetails = posTerminalDetails.replace("CADVCount", receiveDataArray[b + 9]);
                        posTerminalDetails = posTerminalDetails.replace("CADVAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 10])) / 100));
                        posTerminalDetails = posTerminalDetails.replace("AUTHCount", receiveDataArray[b + 11]);
                        posTerminalDetails = posTerminalDetails.replace("AUTHAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 12])) / 100));
                        posTerminalDetails = posTerminalDetails.replace("TOTALSCount", receiveDataArray[b + 13]);
                        posTerminalDetails = posTerminalDetails.replace("TOTALSAmount",
                                String.format("%.2f", (Double.parseDouble(receiveDataArray[b + 14])) / 100));
                        b = b + 14;
                        summaryFinalReport.append(posTerminalDetails);
                    }
                }
            }
            htmlString = htmlString.replace("PosTable", summaryFinalReport.toString());
            htmlString = htmlString.replace("currentTime", currentTime);
            htmlString = htmlString.replace("currentDate", currentDate);
            htmlString = htmlString.replace("BuzzCode", receiveDataArray[6]);
            htmlString = htmlString.replace("AppVersion", receiveDataArray[7]);
            htmlString = htmlString.replace("TerminalId", ActiveTxnData.getInstance().getTerminalID());
            htmlString = htmlString.replace(Constant.MERCHANT_NAME, receiveDataArray[b + 1]);
            htmlString = htmlString.replace(Constant.MERCHANT_ADDRESS, receiveDataArray[b + 2]);
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_NAME, convertToArabic(receiveDataArray[b + 3]));
            htmlString = htmlString.replace(Constant.ARABIC_MERCHANT_ADDRESS, convertToArabic(receiveDataArray[b + 4]));
        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptPrintSummary(String[] receiveDataArray, Context context) throws IOException {

        String htmlString = "";
        String summaryHtmlString = "";
        String summaryFinalReport = "";

        is = context.getResources().getAssets().open("printReceipt/Summary_Report.html");
        htmlString = getHtmlString(is);

        is = context.getResources().getAssets().open("printReceipt/Summary.html");
        summaryHtmlString = getHtmlString(is);
        String htmlSummaryReport = summaryHtmlString;

        String respDateTime = receiveDataArray[3];
        String currentDate = respDateTime.substring(0, 2) + " / " + respDateTime.substring(2, 4) + "/" + date;
        String currentTime = respDateTime.substring(4, 6) + ":" + respDateTime.substring(6, 8) + ":"
                + respDateTime.substring(8, 10);
        int j = 6;
        int transactionsLength = Integer.parseInt(receiveDataArray[5]);

        for (int i = 1; i <= transactionsLength; i++) {
            String date1 = receiveDataArray[j + 1];
            date1 = date1.substring(0, 2) + "-" + date1.substring(2, 4) + "-" + date1.substring(4, 6);
            String time = receiveDataArray[j + 6];
            time = time.substring(0, 2) + ":" + time.substring(2, 4);
            htmlSummaryReport = htmlSummaryReport.replace("transactionType", receiveDataArray[j]);
            htmlSummaryReport = htmlSummaryReport.replace("transactionDate", date1);
            htmlSummaryReport = htmlSummaryReport.replace("transactionRRN", receiveDataArray[j + 2]);
            htmlSummaryReport = htmlSummaryReport.replace("transactionAmount",
                    String.format("%.2f", (Double.parseDouble(receiveDataArray[j + 3]))));
            htmlSummaryReport = htmlSummaryReport.replace("Claim1", receiveDataArray[j + 4]);
            htmlSummaryReport = htmlSummaryReport.replace("transactionState", receiveDataArray[j + 5]);
            htmlSummaryReport = htmlSummaryReport.replace("transactionTime", time);
            htmlSummaryReport = htmlSummaryReport.replace("transactionPANNumber", receiveDataArray[j + 7]);
            htmlSummaryReport = htmlSummaryReport.replace("authCode", receiveDataArray[j + 8]);
            htmlSummaryReport = htmlSummaryReport.replace("transactionNumber", receiveDataArray[j + 9]);
            j = j + 10;
            summaryFinalReport += htmlSummaryReport;
            htmlSummaryReport = summaryHtmlString;

        }
        htmlString = htmlString.replace("no_Transaction", summaryFinalReport);
        htmlString = htmlString.replace(Constant.CURRENT_TIME, currentTime);
        htmlString = htmlString.replace(Constant.CURRENT_DATE, currentDate);
        htmlString = htmlString.replace(Constant.TERMINAL_ID, ActiveTxnData.getInstance().getTerminalID().substring(0, 8));
        htmlString = htmlString.replace(Constant.MERCHANT_NAME, receiveDataArray[j]);
        htmlString = htmlString.replace(Constant.MERCHANT_ADDRESS, receiveDataArray[j + 1]);

        return htmlString;
    }

    public String checkingArabic(String command) {
        String arabic = "";
        switch (command) {
            case "mada":
                arabic = "مدى";
                break;
            case "ELECTRON":
                arabic = "فيزا";
                break;
            case "MAESTRO":
                arabic = "مايسترو";
                break;
            case "AMEX":
                arabic = "امريكان اكسبرس";
                break;
            case "AMERICAN EXPRESS":
                arabic = "امريكان اكسبرس";
                break;
            case "MASTER":
                arabic = "ماستر كارد";
                break;
            case "MASTERCARD":
                arabic = "ماستر كارد";
                break;
            case "VISA":
                arabic = "فيزا";
                break;
            case "GCCNET":
                arabic = "الشبكة الخليجية";
                break;
            case "JCB":
                arabic = "ج س ب";
                break;
            case "DISCOVER":
                arabic = "ديسكفر";
                break;
            case "SAR":
                arabic = "ريال";
                break;
            case "MADA":
                arabic = "مدى";
                break;
            case "APPROVED":
                arabic = "العملية مقبولة";
                break;
            case "DECLINED":
                arabic = "العملية مرفوضه";
                break;
            case "ACCEPTED":
                arabic = "مستلمة";
                break;
            case "NOT ACCEPTED":
                arabic = "غير مستلمة";
                break;
            case "CARDHOLDER VERIFIED BY SIGNATURE":
                arabic = "تم التحقق بتوقيع العميل";
                break;
            case "CARDHOLDER PIN VERIFIED":
                arabic = "تم التحقق من الرقم السري للعميل";
                break;
            case "DEVICE OWNER IDENTITY VERIFIED":
                arabic = "تم التحقق من هوية حامل الجهاز";
                break;
            case "NO VERIFICATION REQUIRED":
                arabic = "لا يتطلب التحقق";
                break;
            case "INCORRECT PIN":
                arabic = "رقم التعريف الشخصي غير صحيح";
                break;
            default:
                arabic = "";
                break;
        }
        return arabic;
    }

    private String getHtmlString(InputStream is) throws IOException {
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        String str = new String(buffer);
        logger.debug(getClass() + "LoadedHtml>>" + str);
        return str;
    }

    private String maskPAn(String s) {
        return s.substring(0, 5) + "******" + s.substring(s.length() - 4);
    }

    private String convertToArabic(String hex) throws UnsupportedEncodingException {

        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        String decodedToISO88591 = new String(output.toString().getBytes(StandardCharsets.UTF_16), "ISO-8859-6");
        decodedToISO88591 = decodedToISO88591.replace("�", "");

        System.out.println(decodedToISO88591);
        logger.debug("aaaa" + decodedToISO88591);

        return decodedToISO88591;
    }
}
