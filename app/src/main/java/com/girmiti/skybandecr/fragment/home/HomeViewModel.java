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

import lombok.Getter;
import lombok.Setter;

public class HomeViewModel extends ViewModel {

    private HomeFragmentBinding homeFragmentBinding;
    private Logger logger = Logger.getNewLogger(HomeViewModel.class.getName());

    @Getter
    private static String reqData = "";

    @Getter
    private static int transactionType;

    @Setter
    private static String cashRegisterNo;

    @Getter
    private static String terminalID = "";

    @Getter
    private static String[] splittedArray;

    public static boolean isSessionStarted = false;
    public static boolean isRegistered = false;
    private String szSignature = "";
    private String ecrReferenceNo = "";
    private int ecrSelected = TransactionSettingViewModel.getEcr();

    public void resetVisibilityOfViews(HomeFragmentBinding homeFragmentBinding) {
        this.homeFragmentBinding = homeFragmentBinding;

        if (ecrSelected == 0) {
            this.homeFragmentBinding.ecrRefNo.setVisibility(View.GONE);
            this.homeFragmentBinding.ecrRefNoTv.setVisibility(View.GONE);
        } else {
            this.homeFragmentBinding.ecrRefNo.setVisibility(View.VISIBLE);
            this.homeFragmentBinding.ecrRefNoTv.setVisibility(View.VISIBLE);
        }

        this.homeFragmentBinding.cashRegisterNoTv.setVisibility(View.GONE);
        this.homeFragmentBinding.cashRegisterNo.setVisibility(View.GONE);

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

        this.homeFragmentBinding.billPayAmt.setVisibility(View.GONE);
        this.homeFragmentBinding.billPayAmtTv.setVisibility(View.GONE);

        this.homeFragmentBinding.billerId.setVisibility(View.GONE);
        this.homeFragmentBinding.billerIdTv.setVisibility(View.GONE);

        this.homeFragmentBinding.billerNumber.setVisibility(View.GONE);
        this.homeFragmentBinding.billerNumberTv.setVisibility(View.GONE);

        homeFragmentBinding.terminalLanguage.setVisibility(View.GONE);
        homeFragmentBinding.terminalLanguageTv.setVisibility(View.GONE);

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

        } else if (selectedItem.equals("Cash advance")) {

            homeFragmentBinding.cashAdvanceAmt.setVisibility(View.VISIBLE);
            homeFragmentBinding.cashAdvanceAmtTv.setVisibility(View.VISIBLE);
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
            homeFragmentBinding.terminalLanguage.setVisibility(View.VISIBLE);
            homeFragmentBinding.terminalLanguageTv.setVisibility(View.VISIBLE);

        } else if (selectedItem.equals("Terminal Status")) {

        } else if (selectedItem.equals("Previous Transaction Details")) {

        } else if (selectedItem.equals("Register")) {
            homeFragmentBinding.cashRegisterNoTv.setVisibility(View.VISIBLE);
            homeFragmentBinding.cashRegisterNo.setVisibility(View.VISIBLE);

            homeFragmentBinding.ecrRefNoTv.setVisibility(View.GONE);
            homeFragmentBinding.ecrRefNo.setVisibility(View.GONE);

        } else if (selectedItem.equals("Bill Payment")) {
            homeFragmentBinding.billPayAmt.setVisibility(View.VISIBLE);
            homeFragmentBinding.billPayAmtTv.setVisibility(View.VISIBLE);

            homeFragmentBinding.billerId.setVisibility(View.VISIBLE);
            homeFragmentBinding.billerIdTv.setVisibility(View.VISIBLE);

            homeFragmentBinding.billerNumber.setVisibility(View.VISIBLE);
            homeFragmentBinding.billerNumberTv.setVisibility(View.VISIBLE);

        } else if (selectedItem.equals("Start Session")) {
            homeFragmentBinding.ecrRefNoTv.setVisibility(View.GONE);
            homeFragmentBinding.ecrRefNo.setVisibility(View.GONE);

        } else if (selectedItem.equals("End Session")) {
            homeFragmentBinding.ecrRefNoTv.setVisibility(View.GONE);
            homeFragmentBinding.ecrRefNo.setVisibility(View.GONE);

        }
    }


    public void setReqData(String selectedItem) {

        String date = new SimpleDateFormat("ddMMyyhhmmss", Locale.getDefault()).format(new Date());
        int print = TransactionSettingViewModel.getPrint();
        String completion;

        if (selectedItem.equals("Register")) {
            cashRegisterNo = homeFragmentBinding.cashRegisterNo.getText().toString();
        }

        if (ecrSelected == 1 && !selectedItem.equals("Register")) {
            ecrReferenceNo = homeFragmentBinding.ecrRefNo.getText().toString();
        } else {
            ecrReferenceNo = cashRegisterNo + "000001";
        }

        if (homeFragmentBinding.typeOfCompletion.isChecked()) {
            completion = "1";
        } else {
            completion = "0";
        }

        if (selectedItem.equals("Purchase")) {
            transactionType = 0;
            System.out.println("reqdata"+reqData);
            reqData = date + ";" + homeFragmentBinding.payAmt.getText() + ";" + print + ";" + ecrReferenceNo + "!";
            System.out.println("reqdata"+reqData);
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
            reqData = date + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + homeFragmentBinding.origTransactionDate.getText() + ";" + homeFragmentBinding.origApproveCode.getText() + ";" + print + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Pre Auth Void")) {
            transactionType = 6;
            reqData = date + ";" + homeFragmentBinding.origTransactionAmt.getText() + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + homeFragmentBinding.origTransactionDate.getText() + ";" + homeFragmentBinding.origApproveCode.getText() + ";" + print + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Cash advance")) {
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
            reqData = date + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Set Parameter")) {
            transactionType = 12;
            reqData = date + ";" + homeFragmentBinding.vendorId.getText() + ";" + homeFragmentBinding.vendorTerminalType.getText() + ";" + homeFragmentBinding.trsmId.getText() + ";" + homeFragmentBinding.vendorKeyIndex.getText() + ";" + homeFragmentBinding.samaKeyIndex.getText() + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Get Parameter")) {
            transactionType = 13;
            reqData = date + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Set Terminal Language")) {
            transactionType = 14;
            reqData = date + ";" + homeFragmentBinding.terminalLanguage.getText() + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Terminal Status")) {
            transactionType = 15;
            reqData = date + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Previous Transaction Details")) {
            transactionType = 16;
            reqData = date + ";" + ecrReferenceNo + "!";

        } else if (selectedItem.equals("Register")) {
            transactionType = 17;
            szSignature = "0000000000000000000000000000000000000000000000000000000000000000";
            reqData = date + ";" + cashRegisterNo + "!";

        } else if (selectedItem.equals("Start Session")) {
            szSignature = "0000000000000000000000000000000000000000000000000000000000000000";
            transactionType = 18;
            reqData = date + ";" + cashRegisterNo + "!";

        } else if (selectedItem.equals("End Session")) {
            szSignature = "0000000000000000000000000000000000000000000000000000000000000000";
            transactionType = 19;
            reqData = date + ";" + cashRegisterNo + "!";

        } else if (selectedItem.equals("Bill Payment")) {
            transactionType = 20;
            reqData = date + ";" + homeFragmentBinding.billPayAmt.getText() + ";" + homeFragmentBinding.billerId.getText() + ";" + homeFragmentBinding.billerNumber.getText() + ";" + print + ";" + ecrReferenceNo + "!";

        }
    }

    public byte[] packData() throws NoSuchAlgorithmException {

        String combinedValue = "";
        if (transactionType != 17 && transactionType != 18 && transactionType != 19) {
            combinedValue = "000001" + terminalID;
            logger.debug(":: Terminal No: " + terminalID);
            szSignature = convertSHA(combinedValue);
        }

        logger.debug(":: Request data: " + reqData + ":: Transactiontype: " + transactionType + ":: Szsignature: " + szSignature);
        CLibraryLoad cLibraryLoad = new CLibraryLoad();
        String szEcrBuffer = "";
        return cLibraryLoad.getPackData(reqData, transactionType, szSignature, szEcrBuffer);
    }


    public String getTerminalResponse(byte[] packData) throws IOException {

        logger.info("Sending Pack data");
        String terminalResponse = ConnectSettingViewModel.getSocketHostConnector().sendPacketToTerminal(packData);

        if (transactionType == 17) {
            String[] splittedArray = terminalResponse.split("�");

            for (int i = 0; i < splittedArray.length; i++) {
                if (i == 3) {
                    terminalID = splittedArray[i];
                }
            }
        }

        logger.debug("Terminal ID>>" + terminalID);
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


    public boolean validateData(HomeFragmentBinding homeFragmentBinding) throws Exception {

        if (transactionType != 17 && !isRegistered) {
            throw new Exception("Please Register First");
        } else if (transactionType != 17 && transactionType != 18 && !isSessionStarted) {
            throw new Exception("Please Start Session");
        }
        System.out.println("Cash out length>>" + homeFragmentBinding.cashRegisterNo.getText().length());
        System.out.println("Transaction Type>>" + transactionType);

        if (transactionType == 0) {
            System.out.println("Transaction Type>>" + "0");
            return !homeFragmentBinding.payAmt.getText().toString().equals("") && !homeFragmentBinding.payAmt.getText().toString().equals("0.00") && ecrReferenceNo.length() == 14;

        } else if (transactionType == 1) {
            System.out.println("Transaction Type>>" + "1");
            if (!homeFragmentBinding.payAmt.getText().toString().equals("") && !homeFragmentBinding.payAmt.getText().toString().equals("0.00") && !homeFragmentBinding.cashBackAmt.getText().toString().equals("") && ecrReferenceNo.length() == 14) {
                if (Double.parseDouble(homeFragmentBinding.payAmt.getText().toString()) >= Double.parseDouble(homeFragmentBinding.cashBackAmt.getText().toString())) {
                    return true;
                } else throw new Exception("CashBackAmt cannot be More than Purchase Amt");
            }

        } else if (transactionType == 2) {
            return !homeFragmentBinding.refundAmt.getText().toString().equals("") && !homeFragmentBinding.refundAmt.getText().toString().equals("0.00") && homeFragmentBinding.rrnNoEditText.getText().length() == 12 && homeFragmentBinding.origRefundDate.getText().length() == 6 && ecrReferenceNo.length() == 14;

        } else if (transactionType == 3) {
            return !homeFragmentBinding.authAmt.getText().toString().equals("") && !homeFragmentBinding.authAmt.getText().toString().equals("0.00") && ecrReferenceNo.length() == 14;

        } else if (transactionType == 4) {
            return !homeFragmentBinding.authAmt.getText().toString().equals("") && !homeFragmentBinding.authAmt.getText().toString().equals("0.00") && homeFragmentBinding.rrnNoEditText.getText().length() == 12 && homeFragmentBinding.origTransactionDate.getText().length() == 6 && homeFragmentBinding.origApproveCode.getText().length() == 6 && ecrReferenceNo.length() == 14;

        } else if (transactionType == 5) {
            return homeFragmentBinding.rrnNoEditText.getText().length() == 12 && homeFragmentBinding.origTransactionDate.getText().length() == 6 && homeFragmentBinding.origApproveCode.getText().length() == 6 && ecrReferenceNo.length() == 14;

        } else if (transactionType == 6) {
            return !homeFragmentBinding.origTransactionAmt.getText().toString().equals("") && !homeFragmentBinding.origTransactionAmt.getText().toString().equals("0.00") && homeFragmentBinding.rrnNoEditText.getText().length() == 12 && homeFragmentBinding.origTransactionDate.getText().length() == 6 && homeFragmentBinding.origApproveCode.getText().length() == 6 && ecrReferenceNo.length() == 14;

        } else if (transactionType == 8) {
            return !homeFragmentBinding.cashAdvanceAmt.getText().toString().equals("") && !homeFragmentBinding.cashAdvanceAmt.getText().toString().equals("0.00") && ecrReferenceNo.length() == 14;

        } else if (transactionType == 9) {
            return homeFragmentBinding.rrnNoEditText.getText().length() == 12 && ecrReferenceNo.length() == 14;

        } else if (transactionType == 10 || transactionType == 11) {
            return ecrReferenceNo.length() == 14;

        } else if (transactionType == 12) {
            return homeFragmentBinding.vendorId.getText().length() == 6 && homeFragmentBinding.vendorTerminalType.getText().length() == 1 && homeFragmentBinding.trsmId.getText().length() == 6 && homeFragmentBinding.vendorKeyIndex.getText().length() == 1 && homeFragmentBinding.samaKeyIndex.getText().length() == 1;

        } else if (transactionType == 13 || transactionType == 14 || transactionType == 15 || transactionType == 16) {
            return ecrReferenceNo.length() == 14;

        } else if (transactionType == 17 && homeFragmentBinding.cashRegisterNo.getText().length() == 8) {
            /*// Need to delete
            isRegistered = true;
            //*/
            System.out.println("Lngth of ECR In Register" + ecrReferenceNo.length());
            return ecrReferenceNo.length() == 14;

        } else if (transactionType == 18) {
            System.out.println("Transaction Type>>" + "18");
           /* // Need to delete
            isSessionStarted = true;
            //*/
            return ecrReferenceNo.length() == 14;

        } else if (transactionType == 19) {
            System.out.println("Transaction Type>>" + "19");
          /*  //need to dlt
            isSessionStarted = false;
            //*/
            return ecrReferenceNo.length() == 14;

        } else if (transactionType == 20) {
            System.out.println("Transaction Type>>" + 20);
            return !homeFragmentBinding.billPayAmt.getText().toString().equals("") && !homeFragmentBinding.billPayAmt.getText().toString().equals("0.00") && homeFragmentBinding.billerId.getText().length() == 6 && homeFragmentBinding.billerNumber.getText().length() == 6;

        }
        System.out.println("Transaction Type>>" + "outside");
        return false;
    }

    public void parse(String terminalResponse) {

        splittedArray = terminalResponse.split("�");
    }
}


