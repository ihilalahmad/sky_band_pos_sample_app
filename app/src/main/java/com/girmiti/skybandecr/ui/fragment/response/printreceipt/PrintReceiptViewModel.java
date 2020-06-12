package com.girmiti.skybandecr.ui.fragment.response.printreceipt;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.girmiti.skybandecr.model.ActiveTxnData;
import com.girmiti.skybandecr.sdk.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PrintReceiptViewModel extends ViewModel {

    private Logger logger = Logger.getNewLogger(PrintReceiptViewModel.class.getName());

    @SuppressLint("DefaultLocale")
    public String printReceiptPurchase(String[] receiveDataArray, Context context) throws IOException {

        InputStream is;
        String htmlString = "";
        String pan;
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        is = context.getResources().getAssets().open("printReceipt/Purchase(customer_copy).html");
        htmlString = getHtmlString(is);
        if (receiveDataArray.length > 27) {
            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[8];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }

            if(receiveDataArray[4].length() > 14) {
                pan = maskPAn(receiveDataArray[4]);
            } else {
                pan = receiveDataArray[4];
            }
            String receiveDataArrayDateTime = receiveDataArray[7];
            logger.debug("DateTime>>" + receiveDataArrayDateTime);
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2) + "/" + date;
            logger.debug("currentDate>>" + currentDate);
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace("currentTime", currentTime);
            htmlString = htmlString.replace("currentDate", currentDate);
            htmlString = htmlString.replace("panNumber", pan);
            htmlString = htmlString.replace("authCode", receiveDataArray[10]);
            htmlString = htmlString.replace("approved", receiveDataArray[3]);
            htmlString = htmlString.replace("CurrentAmount", String.format("%.2f", amount));
            htmlString = htmlString.replace("ExpiryDate", expiryDate);
            htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
            htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
            htmlString = htmlString.replace("AIDaid", receiveDataArray[14]);
            htmlString = htmlString.replace("TVR", receiveDataArray[18]);
            htmlString = htmlString.replace("CVR", receiveDataArray[17]);
            htmlString = htmlString.replace("applicationCryptogram", receiveDataArray[15]);
            htmlString = htmlString.replace("CID", receiveDataArray[16]);
            htmlString = htmlString.replace("MID", receiveDataArray[12]);
            htmlString = htmlString.replace("TID", receiveDataArray[11]);
            htmlString = htmlString.replace("RRN", receiveDataArray[9]);
            htmlString = htmlString.replace("StanNo", receiveDataArray[6]);
            htmlString = htmlString.replace("ApplicationVersion", receiveDataArray[26]);
            htmlString = htmlString.replace("SchemeLabel", receiveDataArray[23]);
            htmlString = htmlString.replace("MerchantCategoryCode", receiveDataArray[21]);
        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptPurchaseCashback(String[] receiveDataArray, Context context) throws IOException {
        InputStream is;
        String htmlString = "";
        String pan;
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        is = context.getResources().getAssets().open("printReceipt/Purchase_Cashback(customer_copy)).html");
        htmlString = getHtmlString(is);
        if (receiveDataArray.length > 29) {
            double transactionAmount = Double.parseDouble(receiveDataArray[5]) / 100;
            transactionAmount = Math.round(transactionAmount * 100.0) / 100.0;
            double cashBackAmount = Double.parseDouble(receiveDataArray[6]) / 100;
            cashBackAmount = Math.round(cashBackAmount * 100.0) / 100.0;
            double totalAmount = Double.parseDouble(receiveDataArray[7]) / 100;
            totalAmount = Math.round(totalAmount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[10];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }

            if(receiveDataArray[4].length() > 14) {
                pan = maskPAn(receiveDataArray[4]);
            } else {
                pan = receiveDataArray[4];
            }

            String receiveDataArrayDateTime = receiveDataArray[9];
            logger.debug("DateTime>>" + receiveDataArrayDateTime);
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            logger.debug("currentDate>>" + currentDate);
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace("currentTime", currentTime);
            htmlString = htmlString.replace("currentDate", currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace("panNumber", pan);
            htmlString = htmlString.replace("authCode", receiveDataArray[12]);
            htmlString = htmlString.replace("approved", receiveDataArray[3]);
            htmlString = htmlString.replace("TransactionAmount", String.format("%.2f", transactionAmount));
            htmlString = htmlString.replace("CashbackAmount",String.format("%.2f",cashBackAmount));
            htmlString = htmlString.replace("TotalAmount", String.format("%.2f",totalAmount));
            htmlString = htmlString.replace("ExpiryDate", expiryDate);
            htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[22]);
            htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
            htmlString = htmlString.replace("AIDaid", receiveDataArray[16]);
            htmlString = htmlString.replace("TVR", receiveDataArray[20]);
            htmlString = htmlString.replace("CVR", receiveDataArray[19]);
            htmlString = htmlString.replace("applicationCryptogram", receiveDataArray[17]);
            htmlString = htmlString.replace("CID", receiveDataArray[18]);
            htmlString = htmlString.replace("MID", receiveDataArray[14]);
            htmlString = htmlString.replace("TID", receiveDataArray[13]);
            htmlString = htmlString.replace("RRN", receiveDataArray[11]);
            htmlString = htmlString.replace("StanNo", receiveDataArray[8]);
            htmlString = htmlString.replace("ApplicationVersion", receiveDataArray[28]);
            htmlString = htmlString.replace("SchemeLabel", receiveDataArray[25]);
            htmlString = htmlString.replace("MerchantCategoryCode", receiveDataArray[23]);
        }
        return htmlString;
    }

    public String printReceiptRefund(String[] receiveDataArray, Context context) throws IOException {
        InputStream is;
        String htmlString = "";
        String pan;
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        is = context.getResources().getAssets().open("printReceipt/Refund(customer_copy).html");
        htmlString = getHtmlString(is);

        if (receiveDataArray.length > 27) {
            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[8];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }

            if(receiveDataArray[4].length() > 14) {
                pan = maskPAn(receiveDataArray[4]);
            } else {
                pan = receiveDataArray[4];
            }
            String receiveDataArrayDateTime = receiveDataArray[7];
            logger.debug("DateTime>>" + receiveDataArrayDateTime);
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            logger.debug("currentDate>>" + currentDate);
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace("currentTime", currentTime);
            htmlString = htmlString.replace("currentDate", currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace("panNumber", pan);
            htmlString = htmlString.replace("authCode", receiveDataArray[10]);
            htmlString = htmlString.replace("approved", receiveDataArray[3]);
            htmlString = htmlString.replace("CurrentAmount", String.format("%.2f",amount));
            htmlString = htmlString.replace("ExpiryDate", expiryDate);
            htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
            htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
            htmlString = htmlString.replace("AIDaid", receiveDataArray[14]);
            htmlString = htmlString.replace("TVR", receiveDataArray[18]);
            htmlString = htmlString.replace("CVR", receiveDataArray[17]);
            htmlString = htmlString.replace("applicationCryptogram", receiveDataArray[15]);
            htmlString = htmlString.replace("CID", receiveDataArray[16]);
            htmlString = htmlString.replace("MID", receiveDataArray[12]);
            htmlString = htmlString.replace("TID", receiveDataArray[11]);
            htmlString = htmlString.replace("RRN", receiveDataArray[9]);
            htmlString = htmlString.replace("StanNo", receiveDataArray[6]);
            htmlString = htmlString.replace("ApplicationVersion", receiveDataArray[26]);
            htmlString = htmlString.replace("SchemeLabel", receiveDataArray[23]);
            htmlString = htmlString.replace("MerchantCategoryCode", receiveDataArray[21]);
        }
        return htmlString;

    }

    public String printReceiptPreAuthorisation(String[] receiveDataArray, Context context) throws IOException {
        InputStream is;
        String htmlString = "";
        String pan;
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        is = context.getResources().getAssets().open("printReceipt/Pre-Auth(Customer_copy).html");
        htmlString = getHtmlString(is);
        if (receiveDataArray.length > 27) {
            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[8];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }

            if(receiveDataArray[4].length() > 14) {
                pan = maskPAn(receiveDataArray[4]);
            } else {
                pan = receiveDataArray[4];
            }
            String receiveDataArrayDateTime = receiveDataArray[7];
            logger.debug("DateTime>>" + receiveDataArrayDateTime);
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            logger.debug("currentDate>>" + currentDate);
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace("currentTime", currentTime);
            htmlString = htmlString.replace("currentDate", currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace("panNumber", pan);
            htmlString = htmlString.replace("authCode", receiveDataArray[10]);
            htmlString = htmlString.replace("approved", receiveDataArray[3]);
            htmlString = htmlString.replace("CurrentAmount", String.format("%.2f",amount));
            htmlString = htmlString.replace("ExpiryDate", expiryDate);
            htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
            htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
            htmlString = htmlString.replace("AIDaid", receiveDataArray[14]);
            htmlString = htmlString.replace("TVR", receiveDataArray[18]);
            htmlString = htmlString.replace("CVR", receiveDataArray[17]);
            htmlString = htmlString.replace("applicationCryptogram", receiveDataArray[15]);
            htmlString = htmlString.replace("CID", receiveDataArray[16]);
            htmlString = htmlString.replace("MID", receiveDataArray[12]);
            htmlString = htmlString.replace("TID", receiveDataArray[11]);
            htmlString = htmlString.replace("RRN", receiveDataArray[9]);
            htmlString = htmlString.replace("StanNo", receiveDataArray[6]);
            htmlString = htmlString.replace("ApplicationVersion", receiveDataArray[26]);
            htmlString = htmlString.replace("SchemeLabel", receiveDataArray[23]);
            htmlString = htmlString.replace("MerchantCategoryCode", receiveDataArray[21]);
        }
        return htmlString;
    }

    public String printReceiptPreAuthCompletion(String[] receiveDataArray, Context context) throws IOException {
        InputStream is;
        String htmlString = "";
        String pan;
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        is = context.getResources().getAssets().open("printReceipt/Purchase_Advice(Customer_copy).html");
        htmlString = getHtmlString(is);
        if (receiveDataArray.length > 27) {

            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[8];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }

            if(receiveDataArray[4].length() > 14) {
                pan = maskPAn(receiveDataArray[4]);
            } else {
                pan = receiveDataArray[4];
            }
            String receiveDataArrayDateTime = receiveDataArray[7];
            logger.debug("DateTime>>" + receiveDataArrayDateTime);
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            logger.debug("currentDate>>" + currentDate);
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace("currentTime", currentTime);
            htmlString = htmlString.replace("currentDate", currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace("panNumber", pan);
            htmlString = htmlString.replace("authCode", receiveDataArray[10]);
            htmlString = htmlString.replace("approved", receiveDataArray[3]);
            htmlString = htmlString.replace("CurrentAmount", String.format("%.2f",amount));;
            htmlString = htmlString.replace("expiryDate", expiryDate);
            htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
            htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
            htmlString = htmlString.replace("AIDaid", receiveDataArray[14]);
            htmlString = htmlString.replace("TVR", receiveDataArray[18]);
            htmlString = htmlString.replace("CVR", receiveDataArray[17]);
            htmlString = htmlString.replace("applicationCryptogram", receiveDataArray[15]);
            htmlString = htmlString.replace("CID", receiveDataArray[16]);
            htmlString = htmlString.replace("MID", receiveDataArray[12]);
            htmlString = htmlString.replace("TID", receiveDataArray[11]);
            htmlString = htmlString.replace("RRN", receiveDataArray[9]);
            htmlString = htmlString.replace("StanNo", receiveDataArray[6]);
            htmlString = htmlString.replace("ApplicationVersion", receiveDataArray[26]);
            htmlString = htmlString.replace("SchemeLabel", receiveDataArray[23]);
            htmlString = htmlString.replace("MerchantCategoryCode", receiveDataArray[21]);
        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptPreAuthExtension(String[] receiveDataArray, Context context) throws IOException {
        InputStream is;
        String htmlString = "";
        String pan;
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        is = context.getResources().getAssets().open("printReceipt/Pre-Extension(Customer_copy).html");
        htmlString = getHtmlString(is);
        if (receiveDataArray.length > 27) {

            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[8];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }
            if(receiveDataArray[4].length() > 14) {
                pan = maskPAn(receiveDataArray[4]);
            } else {
                pan = receiveDataArray[4];
            }
            String receiveDataArrayDateTime = receiveDataArray[7];
            logger.debug("DateTime>>" + receiveDataArrayDateTime);
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            logger.debug("currentDate>>" + currentDate);
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace("currentTime", currentTime);
            htmlString = htmlString.replace("currentDate", currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace("panNumber", pan);
            htmlString = htmlString.replace("authCode", receiveDataArray[10]);
            htmlString = htmlString.replace("approved", receiveDataArray[3]);
            htmlString = htmlString.replace("CurrentAmount", String.format("%.2f",amount));
            htmlString = htmlString.replace("expiryDate", expiryDate);
            htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
            htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
            htmlString = htmlString.replace("AIDaid", receiveDataArray[14]);
            htmlString = htmlString.replace("TVR", receiveDataArray[18]);
            htmlString = htmlString.replace("CVR", receiveDataArray[17]);
            htmlString = htmlString.replace("applicationCryptogram", receiveDataArray[15]);
            htmlString = htmlString.replace("CID", receiveDataArray[16]);
            htmlString = htmlString.replace("MID", receiveDataArray[12]);
            htmlString = htmlString.replace("TID", receiveDataArray[11]);
            htmlString = htmlString.replace("RRN", receiveDataArray[9]);
            htmlString = htmlString.replace("StanNo", receiveDataArray[6]);
            htmlString = htmlString.replace("ApplicationVersion", receiveDataArray[26]);
            htmlString = htmlString.replace("SchemeLabel", receiveDataArray[23]);
            htmlString = htmlString.replace("MerchantCategoryCode", receiveDataArray[21]);
        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptPreAuthVoid(String[] receiveDataArray, Context context) throws IOException {
        InputStream is;
        String htmlString = "";
        String pan;
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        is = context.getResources().getAssets().open("printReceipt/Pre-void(Customer_copy).html");
        htmlString = getHtmlString(is);
        if (receiveDataArray.length > 27) {
            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[8];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }

            if(receiveDataArray[4].length() > 14) {
                pan = maskPAn(receiveDataArray[4]);
            } else {
                pan = receiveDataArray[4];
            }
            String receiveDataArrayDateTime = receiveDataArray[7];
            logger.debug("DateTime>>" + receiveDataArrayDateTime);
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            logger.debug("currentDate>>" + currentDate);
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace("currentTime", currentTime);
            htmlString = htmlString.replace("currentDate", currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace("panNumber", pan);
            htmlString = htmlString.replace("authCode", receiveDataArray[10]);
            htmlString = htmlString.replace("approved", receiveDataArray[3]);
            htmlString = htmlString.replace("CurrentAmount", String.format("%.2f",amount));
            htmlString = htmlString.replace("expiryDate", expiryDate);
            htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
            htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
            htmlString = htmlString.replace("AIDaid", receiveDataArray[14]);
            htmlString = htmlString.replace("TVR", receiveDataArray[18]);
            htmlString = htmlString.replace("CVR", receiveDataArray[17]);
            htmlString = htmlString.replace("applicationCryptogram", receiveDataArray[15]);
            htmlString = htmlString.replace("CID", receiveDataArray[16]);
            htmlString = htmlString.replace("MID", receiveDataArray[12]);
            htmlString = htmlString.replace("TID", receiveDataArray[11]);
            htmlString = htmlString.replace("RRN", receiveDataArray[9]);
            htmlString = htmlString.replace("StanNo", receiveDataArray[6]);
            htmlString = htmlString.replace("ApplicationVersion", receiveDataArray[26]);
            htmlString = htmlString.replace("SchemeLabel", receiveDataArray[23]);
            htmlString = htmlString.replace("MerchantCategoryCode", receiveDataArray[21]);

        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptCashAdvance(String[] receiveDataArray, Context context) throws IOException {
        InputStream is;
        String htmlString = "";
        String pan;
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        is = context.getResources().getAssets().open("printReceipt/Cash_Advance(Customer_copy).html");
        htmlString = getHtmlString(is);
        if (receiveDataArray.length > 27) {
            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[8];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }

            if(receiveDataArray[4].length() > 14) {
                pan = maskPAn(receiveDataArray[4]);
            } else {
                pan = receiveDataArray[4];
            }
            String receiveDataArrayDateTime = receiveDataArray[7];
            logger.debug("DateTime>>" + receiveDataArrayDateTime);
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            logger.debug("currentDate>>" + currentDate);
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace("currentTime", currentTime);
            htmlString = htmlString.replace("currentDate", currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace("panNumber", pan);
            htmlString = htmlString.replace("authCode", receiveDataArray[10]);
            htmlString = htmlString.replace("approved", receiveDataArray[3]);
            htmlString = htmlString.replace("CurrentAmount", String.format("%.2f",amount));
            htmlString = htmlString.replace("ExpiryDate", expiryDate);
            htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
            htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
            htmlString = htmlString.replace("AIDaid", receiveDataArray[14]);
            htmlString = htmlString.replace("TVR", receiveDataArray[18]);
            htmlString = htmlString.replace("CVR", receiveDataArray[17]);
            htmlString = htmlString.replace("applicationCryptogram", receiveDataArray[15]);
            htmlString = htmlString.replace("CID", receiveDataArray[16]);
            htmlString = htmlString.replace("MID", receiveDataArray[12]);
            htmlString = htmlString.replace("TID", receiveDataArray[11]);
            htmlString = htmlString.replace("RRN", receiveDataArray[9]);
            htmlString = htmlString.replace("StanNo", receiveDataArray[6]);
            htmlString = htmlString.replace("ApplicationVersion", receiveDataArray[26]);
            htmlString = htmlString.replace("SchemeLabel", receiveDataArray[23]);
            htmlString = htmlString.replace("MerchantCategoryCode", receiveDataArray[21]);
        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptReversal(String[] receiveDataArray, Context context) throws IOException {
        InputStream is;
        String htmlString = "";
        String pan;
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        is = context.getResources().getAssets().open("printReceipt/Reversal(Customer_copy).html");
        htmlString = getHtmlString(is);

        if (receiveDataArray.length > 27) {
            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[8];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }

            if(receiveDataArray[4].length() > 14) {
                pan = maskPAn(receiveDataArray[4]);
            } else {
                pan = receiveDataArray[4];
            }
            String receiveDataArrayDateTime = receiveDataArray[7];
            logger.debug("DateTime>>" + receiveDataArrayDateTime);
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            logger.debug("currentDate>>" + currentDate);
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace("currentTime", currentTime);
            htmlString = htmlString.replace("currentDate", currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace("panNumber", pan);
            htmlString = htmlString.replace("authCode", receiveDataArray[10]);
            htmlString = htmlString.replace("approved", receiveDataArray[3]);
            htmlString = htmlString.replace("CurrentAmount", String.format("%.2f",amount));
            htmlString = htmlString.replace("expiryDate", expiryDate);
            htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
            htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
            htmlString = htmlString.replace("AIDaid", receiveDataArray[14]);
            htmlString = htmlString.replace("TVR", receiveDataArray[18]);
            htmlString = htmlString.replace("CVR", receiveDataArray[17]);
            htmlString = htmlString.replace("applicationCryptogram", receiveDataArray[15]);
            htmlString = htmlString.replace("CID", receiveDataArray[16]);
            htmlString = htmlString.replace("MID", receiveDataArray[12]);
            htmlString = htmlString.replace("TID", receiveDataArray[11]);
            htmlString = htmlString.replace("RRN", receiveDataArray[9]);
            htmlString = htmlString.replace("StanNo", receiveDataArray[6]);
            htmlString = htmlString.replace("ApplicationVersion", receiveDataArray[26]);
            htmlString = htmlString.replace("SchemeLabel", receiveDataArray[23]);
            htmlString = htmlString.replace("MerchantCategoryCode", receiveDataArray[21]);

        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptReconcilation(String[] receiveDataArray, Context context) throws IOException {

        InputStream is;
        String htmlString = "";
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());

        if (receiveDataArray[2].equals("500") || receiveDataArray[2].equals("501")) {
            is = context.getResources().getAssets().open("printReceipt/Reconcilation.html");
            htmlString = getHtmlString(is);
            is = context.getResources().getAssets().open("printReceipt/PosTable.html");
            String SummaryHtmlString = getHtmlString(is);
            String htmlSummaryReport = SummaryHtmlString;
            String SummaryFinalReport = "";
            String receiveDataArrayDateTime = receiveDataArray[4];
            logger.debug("DateTime>>" + receiveDataArrayDateTime);
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            logger.debug("currentDate>>" + currentDate);
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            int b = 6;
            int totalSchemeLengthL = Integer.parseInt(receiveDataArray[6]);

            for (int j = 1; j <= totalSchemeLengthL; j++) {

                if (receiveDataArray[b + 2].equals("0")) {
                    is = context.getResources().getAssets().open("printReceipt/ReconcilationTable.html");
                    String htmlReconcilationNoTable = getHtmlString(is);
                    String arabi = checkingArabic(receiveDataArray[b + 1]);
                    arabi = arabi.replace("\u08F1", "");
                    htmlReconcilationNoTable = htmlReconcilationNoTable.replace("Scheme", receiveDataArray[b + 1]);
                    htmlReconcilationNoTable = htmlReconcilationNoTable.replace("قَدِيرٞ", arabi);
                    b = b + 2;
                    SummaryFinalReport += htmlReconcilationNoTable;
                } else {
                    htmlSummaryReport = htmlSummaryReport.replace("SchemeName", receiveDataArray[b + 1]);
                    htmlSummaryReport = htmlSummaryReport.replace("totalDBCount", receiveDataArray[b + 3]);
                    htmlSummaryReport = htmlSummaryReport.replace("totalDBAmount", String.format("%.2f",(Double.parseDouble(receiveDataArray[b + 4])) / 100));
                    htmlSummaryReport = htmlSummaryReport.replace("totalCBCount", receiveDataArray[b + 5]);
                    htmlSummaryReport = htmlSummaryReport.replace("totalCBAmount", String.format("%.2f",(Double.parseDouble(receiveDataArray[b + 6])) / 100));
                    htmlSummaryReport = htmlSummaryReport.replace("NAQDCount", receiveDataArray[b + 7]);
                    htmlSummaryReport = htmlSummaryReport.replace("NAQDAmount", String.format("%.2f",(Double.parseDouble(receiveDataArray[b + 8])) / 100));
                    htmlSummaryReport = htmlSummaryReport.replace("CADVCount", receiveDataArray[b + 9]);
                    htmlSummaryReport = htmlSummaryReport.replace("CADVAmount", String.format("%.2f",(Double.parseDouble(receiveDataArray[b + 10])) / 100));
                    htmlSummaryReport = htmlSummaryReport.replace("AUTHCount", receiveDataArray[b + 11]);
                    htmlSummaryReport = htmlSummaryReport.replace("AUTHAmount", String.format("%.2f",(Double.parseDouble(receiveDataArray[b + 12])) / 100));
                    int totalsCount = (int) (Double.parseDouble(receiveDataArray[b + 3]) + Double.parseDouble(receiveDataArray[b + 5]) + Double.parseDouble(receiveDataArray[b + 7]) + Double.parseDouble(receiveDataArray[b + 9]) + Double.parseDouble(receiveDataArray[b + 11]));
                    double totalsAmount = (Double.parseDouble(receiveDataArray[b + 4]) + Double.parseDouble(receiveDataArray[b + 6]) + Double.parseDouble(receiveDataArray[b + 8]) + Double.parseDouble(receiveDataArray[b + 10]) + Double.parseDouble(receiveDataArray[b + 12])) / 100.0;

                    htmlSummaryReport = htmlSummaryReport.replace("TOTALSCount", String.valueOf(totalsCount));
                    htmlSummaryReport = htmlSummaryReport.replace("TOTALSAmount", String.format("%.2f",totalsAmount));
                    b = b + 12;
                    SummaryFinalReport += htmlSummaryReport;
                    htmlSummaryReport = SummaryHtmlString;
                }
                htmlString = htmlString.replace("PosTable", SummaryFinalReport);
                htmlString = htmlString.replace("currentTime", currentTime);
                htmlString = htmlString.replace("currentDate", currentDate);
                htmlString = htmlString.replace("AppVersion", receiveDataArray[5]);
                htmlString = htmlString.replace("TerminalId", ActiveTxnData.getInstance().getTerminalID());

            }
        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptBillPayment(String[] receiveDataArray, String receiveData, Context context) throws IOException {
        InputStream is;
        String htmlString = "";
        String pan;
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        is = context.getResources().getAssets().open("printReceipt/Bill_Payment(Customer_copy).html");
        htmlString = getHtmlString(is);

        if (receiveData.length() > 27) {
            double amount = Double.parseDouble(receiveDataArray[5]) / 100;
            amount = Math.round(amount * 100.0) / 100.0;
            String expiryDate = receiveDataArray[8];
            if (!expiryDate.equals("")) {
                expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2, 4);
            }

            if(receiveDataArray[4].length() > 14) {
                pan = maskPAn(receiveDataArray[4]);
            } else {
                pan = receiveDataArray[4];
            }
            String receiveDataArrayDateTime = receiveDataArray[7];
            logger.debug("DateTime>>" + receiveDataArrayDateTime);
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            logger.debug("currentDate>>" + currentDate);
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace("currentTime", currentTime);
            htmlString = htmlString.replace("currentDate", currentDate);
            if (receiveDataArray[4].length() > 12)
                htmlString = htmlString.replace("panNumber", pan);
            htmlString = htmlString.replace("authCode", receiveDataArray[10]);
            htmlString = htmlString.replace("approved", receiveDataArray[3]);
            htmlString = htmlString.replace("CurrentAmount", String.format("%.2f",amount));
            htmlString = htmlString.replace("expiryDate", expiryDate);
            htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
            htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
            htmlString = htmlString.replace("AIDaid", receiveDataArray[14]);
            htmlString = htmlString.replace("TVR", receiveDataArray[18]);
            htmlString = htmlString.replace("CVR", receiveDataArray[17]);
            htmlString = htmlString.replace("applicationCryptogram", receiveDataArray[15]);
            htmlString = htmlString.replace("CID", receiveDataArray[16]);
            htmlString = htmlString.replace("MID", receiveDataArray[12]);
            htmlString = htmlString.replace("TID", receiveDataArray[11]);
            htmlString = htmlString.replace("RRN", receiveDataArray[9]);
            htmlString = htmlString.replace("StanNo", receiveDataArray[6]);
            htmlString = htmlString.replace("ApplicationVersion", receiveDataArray[26]);
            htmlString = htmlString.replace("SchemeLabel", receiveDataArray[23]);
            htmlString = htmlString.replace("MerchantCategoryCode", receiveDataArray[21]);

        }

        return htmlString;
    }

    public String printReceiptParameterDownload(String[] receiveDataArray, String receiveData, Context context) throws IOException {
        InputStream is;
        String htmlString = "";
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        is = context.getResources().getAssets().open("printReceipt/Parameter_download.html");
        htmlString = getHtmlString(is);

        if (receiveData.length() > 4) {
            String receiveDataArrayDateTime = receiveDataArray[4];
            logger.debug("DateTime>>" + receiveDataArrayDateTime);
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            logger.debug("currentDate>>" + currentDate);
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            htmlString = htmlString.replace("currentTime", currentTime);
            htmlString = htmlString.replace("currentDate", currentDate);
            htmlString = htmlString.replace("terminalId", ActiveTxnData.getInstance().getTerminalID());
            htmlString = htmlString.replace("ResponseCode", receiveDataArray[3]);
        }
        return htmlString;
    }

    @SuppressLint("DefaultLocale")
    public String printReceiptPrintDetail(String[] receiveDataArray, Context context) throws IOException {
        InputStream is;
        String htmlString = "";
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        if (Integer.parseInt(receiveDataArray[2]) == 0) {
            is = context.getResources().getAssets().open("printReceipt/Detail_Report.html");
            htmlString = getHtmlString(is);
            is = context.getResources().getAssets().open("printReceipt/PosTable.html");
            String SummaryHtmlString = getHtmlString(is);
            String htmlSummaryReport = SummaryHtmlString;
            String SummaryFinalReport = "";
            String receiveDataArrayDateTime = receiveDataArray[4];
            logger.debug("DateTime>>" + receiveDataArrayDateTime);
            String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2) + " / " + date;
            logger.debug("currentDate>>" + currentDate);
            String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
            int b = 6;
            int totalSchemeLengthL = Integer.parseInt(receiveDataArray[6]);

            for (int j = 1; j <= totalSchemeLengthL; j++) {

                if (receiveDataArray[b + 2].equals("0")) {
                    is = context.getResources().getAssets().open("printReceipt/ReconcilationTable.html");
                    String htmlReconcilationNoTable = getHtmlString(is);
                    String arabi = checkingArabic(receiveDataArray[b + 1]);
                    arabi = arabi.replace("\u08F1", "");
                    htmlReconcilationNoTable = htmlReconcilationNoTable.replace("Scheme", receiveDataArray[b + 1]);
                    htmlReconcilationNoTable = htmlReconcilationNoTable.replace("قَدِيرٞ", arabi);
                    b = b + 2;
                    SummaryFinalReport += htmlReconcilationNoTable;
                } else {
                    htmlSummaryReport = htmlSummaryReport.replace("SchemeName", receiveDataArray[b + 1]);
                    htmlSummaryReport = htmlSummaryReport.replace("totalDBCount", receiveDataArray[b + 3]);
                    htmlSummaryReport = htmlSummaryReport.replace("totalDBAmount", String.format("%.2f",(Double.parseDouble(receiveDataArray[b + 4])) / 100));
                    htmlSummaryReport = htmlSummaryReport.replace("totalCBCount", receiveDataArray[b + 5]);
                    htmlSummaryReport = htmlSummaryReport.replace("totalCBAmount", String.format("%.2f",(Double.parseDouble(receiveDataArray[b + 6])) / 100));
                    htmlSummaryReport = htmlSummaryReport.replace("NAQDCount", receiveDataArray[b + 7]);
                    htmlSummaryReport = htmlSummaryReport.replace("NAQDAmount", String.format("%.2f",(Double.parseDouble(receiveDataArray[b + 8])) / 100));
                    htmlSummaryReport = htmlSummaryReport.replace("CADVCount", receiveDataArray[b + 9]);
                    htmlSummaryReport = htmlSummaryReport.replace("CADVAmount", String.format("%.2f",(Double.parseDouble(receiveDataArray[b + 10])) / 100));
                    htmlSummaryReport = htmlSummaryReport.replace("AUTHCount", receiveDataArray[b + 11]);
                    htmlSummaryReport = htmlSummaryReport.replace("AUTHAmount", String.format("%.2f",(Double.parseDouble(receiveDataArray[b + 12])) / 100));
                    int totalsCount = (int) (Double.parseDouble(receiveDataArray[b + 3]) + Double.parseDouble(receiveDataArray[b + 5]) + Double.parseDouble(receiveDataArray[b + 7]) + Double.parseDouble(receiveDataArray[b + 9]) + Double.parseDouble(receiveDataArray[b + 11]));
                    double totalsAmount = (Double.parseDouble(receiveDataArray[b + 4]) + Double.parseDouble(receiveDataArray[b + 6]) + Double.parseDouble(receiveDataArray[b + 8]) + Double.parseDouble(receiveDataArray[b + 10]) + Double.parseDouble(receiveDataArray[b + 12])) / 100.0;

                    htmlSummaryReport = htmlSummaryReport.replace("TOTALSCount", String.valueOf(totalsCount));
                    htmlSummaryReport = htmlSummaryReport.replace("TOTALSAmount", String.format("%.2f",totalsAmount));
                    b = b + 12;
                    SummaryFinalReport += htmlSummaryReport;
                    htmlSummaryReport = SummaryHtmlString;
                }
                htmlString = htmlString.replace("PosTable", SummaryFinalReport);
                htmlString = htmlString.replace("currentTime", currentTime);
                htmlString = htmlString.replace("currentDate", currentDate);
                htmlString = htmlString.replace("AppVersion", receiveDataArray[5]);
                htmlString = htmlString.replace("TerminalId", ActiveTxnData.getInstance().getTerminalID());

            }
        }
        return htmlString;
    }

    public String printReceiptPrintSummary(String[] receiveDataArray, Context context) throws IOException {

        InputStream is;
        String htmlString = "";
        String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        String summaryHtmlString = "";
        String SummaryFinalReport = "";


        is = context.getResources().getAssets().open("printReceipt/Summary_Report.html");
        htmlString = getHtmlString(is);

        is = context.getResources().getAssets().open("printReceipt/Summary.html");
        summaryHtmlString = getHtmlString(is);

        String htmlSummaryReport = summaryHtmlString;


        String respDateTime = receiveDataArray[4];

        String currentDate = respDateTime.substring(2, 4) + " / " + respDateTime.substring(0, 2) + " / " + date;
        String currentTime = respDateTime.substring(4, 6) + " :" + respDateTime.substring(6, 8) + " : " + respDateTime.substring(8, 10);

        int j = 7;
        int transactionsLength = Integer.parseInt(receiveDataArray[6]);

        if (transactionsLength > 17) {
            transactionsLength = 17;
        }

        for (int i = 1; i <= transactionsLength; i++) {
            String date1 = receiveDataArray[j + 1];
            date1 = date1.substring(2, 4) + " - " + respDateTime.substring(0, 2) + " - " + date;
            String time = receiveDataArray[j + 6];
            //time = time.substring(4, 6) + " :" + respDateTime.substring(6, 8) + " : " + respDateTime.substring(8, 10);
            htmlSummaryReport = htmlSummaryReport.replace("transactionType", receiveDataArray[j]);
            htmlSummaryReport = htmlSummaryReport.replace("transactionDate", date1);
            htmlSummaryReport = htmlSummaryReport.replace("transactionRRN", receiveDataArray[j + 2]);
            htmlSummaryReport = htmlSummaryReport.replace("transactionAmount", receiveDataArray[j + 3]);
            htmlSummaryReport = htmlSummaryReport.replace("Claim1", receiveDataArray[j + 4]);
            htmlSummaryReport = htmlSummaryReport.replace("transactionState", receiveDataArray[j + 5]);
            htmlSummaryReport = htmlSummaryReport.replace("transactionTime", time);
            htmlSummaryReport = htmlSummaryReport.replace("transactionPANNumber", receiveDataArray[j + 7]);
            htmlSummaryReport = htmlSummaryReport.replace("authCode", receiveDataArray[j + 8]);
            htmlSummaryReport = htmlSummaryReport.replace("transactionNumber", receiveDataArray[j + 9]);
            j = j + 10;
            SummaryFinalReport += htmlSummaryReport;
            htmlSummaryReport = summaryHtmlString;
        }

        htmlString = htmlString.replace("no_Transaction", SummaryFinalReport);
        htmlString = htmlString.replace("currentTime", currentTime);
        htmlString = htmlString.replace("currentDate", currentDate);
        htmlString = htmlString.replace("terminalId", ActiveTxnData.getInstance().getTerminalID());

        return htmlString;
    }

    private String checkingArabic(String command) {
        String arabic = "";
        switch (command) {
            case "ELECTRON":
                arabic = "فيزا";
                break;
            case "MAESTRO":
                arabic = "مايسترو";
                break;
            case "AMEX":
                arabic = "امريكان اكسبرس";
                break;
            case "MASTER":
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
            case "mada":
                arabic = "مدى";
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
        return s.substring(0, 5) + "******" + s.substring(s.length()-4);
    }
}
