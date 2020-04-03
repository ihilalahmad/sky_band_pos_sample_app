package com.girmiti.skybandecr.fragment.home;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.girmiti.skybandecr.databinding.HomeFragmentBinding;
import com.girmiti.skybandecr.fragment.connectsetting.ConnectSettingViewModel;
import com.girmiti.skybandecr.fragment.transactionsetting.TransactionSettingViewModel;
import com.girmiti.skybandecr.sdk.CLibraryLoad;
import com.girmiti.skybandecr.sdk.logger.Logger;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeViewModel extends ViewModel {

    private HomeFragmentBinding homeFragmentBinding;
    private Logger logger = Logger.getNewLogger(HomeViewModel.class.getName());
    private static String reqData = "";
    private int transactionType;
    private String szSignature = "0000000000000000000000000000000000000000000000000000000000000000";
    private String szEcrBuffer = "";

    private static String terminalNumber = "";
    private String ecrReferenceNo = "";
    private int ecrSelected = TransactionSettingViewModel.getEcr();

    private static String parseData = "";

    public static String getParseData() {
        return parseData;
    }

    public static void setParseData(String parseData) {
        HomeViewModel.parseData = parseData;
    }

    public static String getReqData() {
        return reqData;
    }

    public String getTerminalNumber() {
        return terminalNumber;
    }

    public void resetVisibilityOfViews(HomeFragmentBinding homeFragmentBinding) {
        this.homeFragmentBinding = homeFragmentBinding;

        if (ecrSelected == 0) {
            this.homeFragmentBinding.ecrRefNo.setVisibility(View.GONE);
            this.homeFragmentBinding.ecrRefNoTv.setVisibility(View.GONE);
        } else {
            this.homeFragmentBinding.ecrRefNo.setVisibility(View.VISIBLE);
            this.homeFragmentBinding.ecrRefNoTv.setVisibility(View.VISIBLE);
        }

        this.homeFragmentBinding.payAmt.setVisibility(View.GONE);
        this.homeFragmentBinding.payAmtTv.setVisibility(View.GONE);

        this.homeFragmentBinding.refundAmt.setVisibility(View.GONE);
        this.homeFragmentBinding.refundAmtTv.setVisibility(View.GONE);

        this.homeFragmentBinding.authAmt.setVisibility(View.GONE);
        this.homeFragmentBinding.authAmtTv.setVisibility(View.GONE);

        this.homeFragmentBinding.cashAdvanceAmt.setVisibility(View.GONE);
        this.homeFragmentBinding.cashAdvanceAmtTv.setVisibility(View.GONE);

        this.homeFragmentBinding.cashBackAmt.setVisibility(View.GONE);
        this.homeFragmentBinding.cashBackAmtTv.setVisibility(View.GONE);

        this.homeFragmentBinding.origApproveCode.setVisibility(View.GONE);
        this.homeFragmentBinding.origApproveCodeTv.setVisibility(View.GONE);

        this.homeFragmentBinding.origRefundDate.setVisibility(View.GONE);
        this.homeFragmentBinding.origRefundDateTv.setVisibility(View.GONE);

        this.homeFragmentBinding.samaKeyIndex.setVisibility(View.GONE);
        this.homeFragmentBinding.samaKeyIndexTv.setVisibility(View.GONE);

        this.homeFragmentBinding.trsmId.setVisibility(View.GONE);
        this.homeFragmentBinding.trsmIdTv.setVisibility(View.GONE);

        this.homeFragmentBinding.origTransactionAmt.setVisibility(View.GONE);
        this.homeFragmentBinding.origTransactionAmtTv.setVisibility(View.GONE);

        this.homeFragmentBinding.origTransactionDate.setVisibility(View.GONE);
        this.homeFragmentBinding.origTransactionDateTv.setVisibility(View.GONE);

        this.homeFragmentBinding.vendorKeyIndex.setVisibility(View.GONE);
        this.homeFragmentBinding.vendorKeyIndexTv.setVisibility(View.GONE);

        this.homeFragmentBinding.vendorId.setVisibility(View.GONE);
        this.homeFragmentBinding.vendorIdTv.setVisibility(View.GONE);

        this.homeFragmentBinding.rrnNoEditText.setVisibility(View.GONE);
        this.homeFragmentBinding.rrnNoTextView.setVisibility(View.GONE);

        this.homeFragmentBinding.vendorTerminalType.setVisibility(View.GONE);
        this.homeFragmentBinding.vendorTerminalTypeTv.setVisibility(View.GONE);

        this.homeFragmentBinding.typeOfCompletion.setVisibility(View.GONE);
    }

    public void getVisibilityOfViews(String selectedItem) {


        if (selectedItem.equals("Purchase")) {

            homeFragmentBinding.payAmt.setVisibility(View.VISIBLE);
            homeFragmentBinding.payAmtTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.payAmt.setText("");



        } else if (selectedItem.equals("Purchase CashBack")) {

            homeFragmentBinding.payAmt.setVisibility(View.VISIBLE);
            homeFragmentBinding.payAmtTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.payAmt.setText("");

            homeFragmentBinding.cashBackAmt.setVisibility(View.VISIBLE);
            homeFragmentBinding.cashBackAmtTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.cashBackAmt.setText("");


        } else if (selectedItem.equals("Refund")) {

            homeFragmentBinding.refundAmt.setVisibility(View.VISIBLE);
            homeFragmentBinding.refundAmtTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.refundAmt.setText("");

            homeFragmentBinding.rrnNoTextView.setVisibility(View.VISIBLE);
            homeFragmentBinding.rrnNoEditText.setVisibility(View.VISIBLE);
            homeFragmentBinding.rrnNoEditText.setText("");

            homeFragmentBinding.origRefundDate.setVisibility(View.VISIBLE);
            homeFragmentBinding.origRefundDateTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.origRefundDate.setText("");

        } else if (selectedItem.equals("Pre Authorisation")) {

            homeFragmentBinding.authAmt.setVisibility(View.VISIBLE);
            homeFragmentBinding.authAmtTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.authAmt.setText("");

        } else if (selectedItem.equals("Pre Auth Completion")) {

            homeFragmentBinding.authAmt.setVisibility(View.VISIBLE);
            homeFragmentBinding.authAmtTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.authAmt.setText("");

            homeFragmentBinding.rrnNoEditText.setVisibility(View.VISIBLE);
            homeFragmentBinding.rrnNoTextView.setVisibility(View.VISIBLE);
            homeFragmentBinding.rrnNoEditText.setText("");

            homeFragmentBinding.origTransactionDate.setVisibility(View.VISIBLE);
            homeFragmentBinding.origTransactionDateTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.origTransactionDate.setText("");

            homeFragmentBinding.origApproveCode.setVisibility(View.VISIBLE);
            homeFragmentBinding.origApproveCodeTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.origApproveCode.setText("");

            homeFragmentBinding.typeOfCompletion.setVisibility(View.VISIBLE);

        } else if (selectedItem.equals("Pre Auth Extension")) {

            homeFragmentBinding.rrnNoEditText.setVisibility(View.VISIBLE);
            homeFragmentBinding.rrnNoTextView.setVisibility(View.VISIBLE);
            homeFragmentBinding.rrnNoEditText.setText("");

            homeFragmentBinding.origTransactionDate.setVisibility(View.VISIBLE);
            homeFragmentBinding.origTransactionDateTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.origTransactionDate.setText("");

            homeFragmentBinding.origApproveCode.setVisibility(View.VISIBLE);
            homeFragmentBinding.origApproveCodeTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.origApproveCode.setText("");

        } else if (selectedItem.equals("Pre Auth Void")) {

            homeFragmentBinding.origTransactionAmt.setVisibility(View.VISIBLE);
            homeFragmentBinding.origTransactionAmtTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.origTransactionAmt.setText("");

            homeFragmentBinding.rrnNoEditText.setVisibility(View.VISIBLE);
            homeFragmentBinding.rrnNoTextView.setVisibility(View.VISIBLE);
            homeFragmentBinding.rrnNoEditText.setText("");

            homeFragmentBinding.origTransactionDate.setVisibility(View.VISIBLE);
            homeFragmentBinding.origTransactionDateTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.origTransactionDate.setText("");

            homeFragmentBinding.origApproveCode.setVisibility(View.VISIBLE);
            homeFragmentBinding.origApproveCodeTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.origApproveCode.setText("");

        //    homeFragmentBinding.typeOfCompletion.setVisibility(View.VISIBLE);

        } else if (selectedItem.equals("Cash advance")) {

            homeFragmentBinding.cashAdvanceAmt.setVisibility(View.VISIBLE);
            homeFragmentBinding.cashBackAmtTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.cashAdvanceAmt.setText("");

        } else if (selectedItem.equals("Reversal")) {

            homeFragmentBinding.rrnNoEditText.setVisibility(View.VISIBLE);
            homeFragmentBinding.rrnNoTextView.setVisibility(View.VISIBLE);
            homeFragmentBinding.rrnNoEditText.setText("");

        } else if (selectedItem.equals("Reconciliation")) {

        } else if (selectedItem.equals("Parameter Download")) {

        } else if (selectedItem.equals("Set Parameter")) {

            homeFragmentBinding.vendorId.setVisibility(View.VISIBLE);
            homeFragmentBinding.vendorIdTv.setVisibility(View.VISIBLE);

            homeFragmentBinding.vendorTerminalType.setVisibility(View.VISIBLE);
            homeFragmentBinding.vendorTerminalTypeTv.setVisibility(View.VISIBLE);

            homeFragmentBinding.trsmId.setVisibility(View.VISIBLE);
            homeFragmentBinding.trsmIdTv.setVisibility(View.VISIBLE);

            homeFragmentBinding.vendorKeyIndex.setVisibility(View.VISIBLE);
            homeFragmentBinding.vendorKeyIndexTv.setVisibility(View.VISIBLE);

            homeFragmentBinding.samaKeyIndex.setVisibility(View.VISIBLE);
            homeFragmentBinding.samaKeyIndexTv.setVisibility(View.VISIBLE);

        } else if (selectedItem.equals("Get Parameter")) {

        } else if (selectedItem.equals("Set Terminal Language")) {

        } else if (selectedItem.equals("Terminal Status")) {

        } else if (selectedItem.equals("Previous Transaction Details")) {

        } else if (selectedItem.equals("Register")) {
        }

    }


    public void setReqData(String selectedItem) {

        String date = new SimpleDateFormat("ddMMyyhhmmss", Locale.getDefault()).format(new Date());
        String date1 = new SimpleDateFormat("ddMMyy", Locale.getDefault()).format(new Date());
        int print = TransactionSettingViewModel.getPrint();
        String completion = "0";

        if (ecrSelected == 1) {
            ecrReferenceNo = homeFragmentBinding.ecrRefNo.getText().toString();
        } else
            ecrReferenceNo = date1;

        if (homeFragmentBinding.typeOfCompletion.isChecked()) {
            completion = "1";
        }

        if (selectedItem.equals("Purchase")) {
            transactionType = 0;
            reqData = date + ";" + homeFragmentBinding.payAmt.getText() + ";" + print + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Purchase CashBack")) {

            transactionType = 1;
            reqData = date + ";" + homeFragmentBinding.payAmt.getText() + ";" + homeFragmentBinding.cashBackAmt.getText() + ";" + print + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Refund")) {

            transactionType = 2;
            reqData = date + ";" + homeFragmentBinding.refundAmt.getText() + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + print + ";" + homeFragmentBinding.origRefundDate.getText() + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Pre Authorisation")) {

            transactionType = 3;
            reqData = date + ";" + homeFragmentBinding.authAmt.getText() + ";" + print + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Pre Auth Completion")) {

            transactionType = 4;
            reqData = date + ";" + homeFragmentBinding.authAmt.getText() + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + homeFragmentBinding.origTransactionDate.getText() + ";" + homeFragmentBinding.origApproveCode.getText() + ";" + completion + ";" + print + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Pre Auth Extension")) {
            transactionType = 5;
            reqData = date + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + homeFragmentBinding.origTransactionDate.getText() + ";" + homeFragmentBinding.origApproveCode.getText()+ ";" + print + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Pre Auth Void")) {
            transactionType = 6;
            reqData = date + ";" + homeFragmentBinding.origTransactionAmt.getText() + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + homeFragmentBinding.origTransactionDate.getText() + ";" + homeFragmentBinding.origApproveCode.getText() + ";" + print + ";" + ecrReferenceNo + "!";
         //   reqData = date + ";" + homeFragmentBinding.origTransactionAmt.getText() + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + homeFragmentBinding.origTransactionDate.getText() + ";" + homeFragmentBinding.origApproveCode.getText() + ";" + completion + ";" + print + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("cash advance")) {
            transactionType = 8;
            reqData = date + ";" + homeFragmentBinding.cashAdvanceAmt.getText() + ";" + print + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Reversal")) {
            transactionType = 9;
            reqData = date + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + print + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Reconciliation")) {
            transactionType = 10;
            reqData = date + ";" + print + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Parameter Download")) {
            transactionType = 11;
            reqData = "210320144000;1!";

        } else if (selectedItem.equals("Set Parameter")) {
            transactionType = 12;
            reqData = "210320144000;123;1;123;1;1;1!";

        } else if (selectedItem.equals("Get Parameter")) {
            transactionType = 13;
            reqData = "210320144000;1!";

        } else if (selectedItem.equals("Set Terminal Language")) {
            transactionType = 14;
            reqData = "210320144000;1;1!";

        } else if (selectedItem.equals("Terminal Status")) {
            transactionType = 15;
            reqData = "210320144000;1!";

        } else if (selectedItem.equals("Previous Transaction Details")) {
            transactionType = 16;
            reqData = "210320144000;1!";

        } else if (selectedItem.equals("Register")) {
            transactionType = 17;
            reqData = date + ";" + "0";

        }
    }

    public byte[] packData() throws NoSuchAlgorithmException {

        String combinedValue = "";
        if (transactionType != 17 && !terminalNumber.equals("")) {
            combinedValue = ecrReferenceNo + terminalNumber;
            szSignature = convertSHA(combinedValue);
        }

        logger.debug(":: Request data: " + reqData + ":: Transactiontype: " + transactionType  + ":: Szsignature: " + szSignature);
        CLibraryLoad cLibraryLoad=new CLibraryLoad();
        return cLibraryLoad.getPackData(reqData, transactionType, szSignature, szEcrBuffer);
    }


    public String getTerminalResponse(byte[] packData) throws IOException {

        logger.info("Sending Pack data");
        String terminalResponse = ConnectSettingViewModel.getSocketHostConnector().sendPacketToTerminal(packData);

        if (transactionType == 17) {

            String[] splittedArray = terminalResponse.split("�");

            for (int i = 0; i < splittedArray.length; i++) {
                if (i == 3) {
                    terminalNumber = splittedArray[i];
                }
            }
        }

        System.out.println("Terminal NumBer>>" + terminalNumber);
        return terminalResponse;
    }


    private String convertSHA(String combinedValue) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashInBytes = md.digest(combinedValue.getBytes());

        StringBuilder sb = new StringBuilder();

        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }

        String resultSHA = sb.toString();

        return resultSHA;
    }


    public boolean validateData(HomeFragmentBinding homeFragmentBinding) {

        System.out.println("Transaction type>>" + transactionType);
        System.out.println("Transaction type>>" + homeFragmentBinding.payAmt.getText().toString());

        if ((transactionType == 0) && homeFragmentBinding.payAmt.getText().toString().equals("")) {
            return false;

        } else if (transactionType == 1) {
            if (homeFragmentBinding.payAmt.getText().toString().equals("") || homeFragmentBinding.cashBackAmt.getText().toString().equals("") || ecrReferenceNo.equals(""))
                return false;

        } else if (transactionType == 2) {
            if (homeFragmentBinding.refundAmt.getText().toString().equals("") || homeFragmentBinding.rrnNoEditText.getText().toString().equals("") || homeFragmentBinding.origRefundDate.getText().toString().equals("") || ecrReferenceNo.equals(""))
                return false;
        } else if (transactionType == 3) {
            if (homeFragmentBinding.authAmt.getText().toString().equals("") || ecrReferenceNo.equals(""))
                return false;

        } else if (transactionType == 4) {
            if (homeFragmentBinding.authAmt.getText().toString().equals("") || homeFragmentBinding.rrnNoEditText.getText().toString().equals("") || homeFragmentBinding.origTransactionDate.getText().toString().equals("") || homeFragmentBinding.origApproveCode.getText().toString().equals("") || ecrReferenceNo.equals(""))
                return false;

        } else if (transactionType == 5) {
            if (homeFragmentBinding.rrnNoEditText.getText().toString().equals("") || homeFragmentBinding.origTransactionDate.getText().toString().equals("") || homeFragmentBinding.origApproveCode.getText().toString().equals("") || ecrReferenceNo.equals(""))
                return false;

        } else if (transactionType == 6) {
            if (homeFragmentBinding.origTransactionAmt.getText().toString().equals("") || homeFragmentBinding.rrnNoEditText.getText().toString().equals("") || homeFragmentBinding.origTransactionDate.getText().toString().equals("") || homeFragmentBinding.origApproveCode.getText().toString().equals("") || ecrReferenceNo.equals(""))
                return false;

        } else if (transactionType == 8) {
            if (homeFragmentBinding.cashAdvanceAmt.getText().toString().equals("") || ecrReferenceNo.equals(""))
                return false;

        } else if (transactionType == 9) {
            if (homeFragmentBinding.rrnNoEditText.getText().toString().equals("") || ecrReferenceNo.equals(""))
                return false;

        } else if (transactionType == 10 && ecrReferenceNo.equals(""))
            return false;

        return true;
    }

    public void parse(String terminalResponse) {

        parseData = "";
        String[] splittedArray = terminalResponse.split("�");

        for (int i = 0; i < splittedArray.length; i++) {
            parseData = parseData + "\n" + splittedArray[i];
        }
    }
}


