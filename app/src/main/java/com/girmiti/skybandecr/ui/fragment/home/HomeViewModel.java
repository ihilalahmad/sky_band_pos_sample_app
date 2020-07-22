package com.girmiti.skybandecr.ui.fragment.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.cache.GeneralParamCache;
import com.girmiti.skybandecr.constant.Constant;
import com.girmiti.skybandecr.databinding.HomeFragmentBinding;
import com.girmiti.skybandecr.model.ActiveTxnData;
import com.girmiti.skybandecr.transaction.TransactionType;
import com.girmiti.skybandecr.ui.fragment.setting.connnect.ConnectSettingFragment;
import com.girmiti.skybandecr.ui.fragment.setting.transaction.TransactionSettingViewModel;
import com.girmiti.skybandecr.sdk.logger.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeViewModel extends ViewModel {

    private TransactionType transactionTypeString = TransactionType.PURCHASE;
    private HomeFragmentBinding homeFragmentBinding;
    private Logger logger = Logger.getNewLogger(HomeViewModel.class.getName());
    private int ecrSelected = TransactionSettingViewModel.getEcr();
    private String reqData = "";
    private String terminalID = "";
    private String szSignature = "";
    private String ecrReferenceNo = "";
    private String prevEcrNo = "";
    private String date = "";

    @SuppressLint("DefaultLocale")
    public void resetVisibilityOfViews(HomeFragmentBinding homeFragmentBinding) {

        this.homeFragmentBinding = homeFragmentBinding;
        if (ecrSelected != Constant.ONE) {
            this.homeFragmentBinding.ecrNo.setVisibility(View.GONE);
            this.homeFragmentBinding.ecrNoTv.setVisibility(View.GONE);
        } else {
            this.homeFragmentBinding.ecrNo.setVisibility(View.VISIBLE);
            this.homeFragmentBinding.ecrNoTv.setVisibility(View.VISIBLE);
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

        this.homeFragmentBinding.billPayAmt.setVisibility(View.GONE);
        this.homeFragmentBinding.billPayAmtTv.setVisibility(View.GONE);

        this.homeFragmentBinding.billerId.setVisibility(View.GONE);
        this.homeFragmentBinding.billerIdTv.setVisibility(View.GONE);

        this.homeFragmentBinding.billerNumber.setVisibility(View.GONE);
        this.homeFragmentBinding.billerNumberTv.setVisibility(View.GONE);

        homeFragmentBinding.terminalLanguage.setVisibility(View.GONE);
        homeFragmentBinding.terminalLanguageTv.setVisibility(View.GONE);

        this.homeFragmentBinding.prevEcrNo.setVisibility(View.GONE);
        this.homeFragmentBinding.prevEcrNoTv.setVisibility(View.GONE);
    }

    @SuppressLint("DefaultLocale")
    public void getVisibilityOfViews(String selectedItem) {

        transactionTypeString = TransactionType.valueOf(selectedItem.toUpperCase().replace(" ", "_"));
        prevEcrNo = GeneralParamCache.getInstance().getString(Constant.PREV_ECR_NO);

        switch (transactionTypeString) {
            case PURCHASE:
                homeFragmentBinding.payAmt.setVisibility(View.VISIBLE);
                homeFragmentBinding.payAmtTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.payAmt.setText("");
                break;
            case PURCHASE_CASHBACK:
                homeFragmentBinding.payAmt.setVisibility(View.VISIBLE);
                homeFragmentBinding.payAmtTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.payAmt.setText("");

                homeFragmentBinding.cashBackAmt.setVisibility(View.VISIBLE);
                homeFragmentBinding.cashBackAmtTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.cashBackAmt.setText("");
                break;
            case REFUND:
                homeFragmentBinding.refundAmt.setVisibility(View.VISIBLE);
                homeFragmentBinding.refundAmtTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.refundAmt.setText("");

                homeFragmentBinding.rrnNoTextView.setVisibility(View.VISIBLE);
                homeFragmentBinding.rrnNoEditText.setVisibility(View.VISIBLE);
                homeFragmentBinding.rrnNoEditText.setText("");

                homeFragmentBinding.origRefundDate.setVisibility(View.VISIBLE);
                homeFragmentBinding.origRefundDateTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.origRefundDate.setText("");
                break;
            case PRE_AUTHORISATION:
                homeFragmentBinding.authAmt.setVisibility(View.VISIBLE);
                homeFragmentBinding.authAmtTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.authAmt.setText("");
                break;
            case PRE_AUTH_COMPLETION:
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
                break;
            case PRE_AUTH_EXTENSION:
                homeFragmentBinding.rrnNoEditText.setVisibility(View.VISIBLE);
                homeFragmentBinding.rrnNoTextView.setVisibility(View.VISIBLE);
                homeFragmentBinding.rrnNoEditText.setText("");

                homeFragmentBinding.origTransactionDate.setVisibility(View.VISIBLE);
                homeFragmentBinding.origTransactionDateTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.origTransactionDate.setText("");

                homeFragmentBinding.origApproveCode.setVisibility(View.VISIBLE);
                homeFragmentBinding.origApproveCodeTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.origApproveCode.setText("");
                break;
            case PRE_AUTH_VOID:
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
                break;
            case CASH_ADVANCE:
                homeFragmentBinding.cashAdvanceAmt.setVisibility(View.VISIBLE);
                homeFragmentBinding.cashAdvanceAmtTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.cashAdvanceAmt.setText("");
                break;
            case REVERSAL:
                homeFragmentBinding.rrnNoEditText.setVisibility(View.VISIBLE);
                homeFragmentBinding.rrnNoTextView.setVisibility(View.VISIBLE);
                homeFragmentBinding.rrnNoEditText.setText("");
                break;
            case SET_SETTINGS:
                homeFragmentBinding.vendorId.setVisibility(View.VISIBLE);
                homeFragmentBinding.vendorIdTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.vendorId.setText("");

                homeFragmentBinding.vendorTerminalType.setVisibility(View.VISIBLE);
                homeFragmentBinding.vendorTerminalTypeTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.vendorTerminalType.setText("");

                homeFragmentBinding.trsmId.setVisibility(View.VISIBLE);
                homeFragmentBinding.trsmIdTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.trsmId.setText("");

                homeFragmentBinding.vendorKeyIndex.setVisibility(View.VISIBLE);
                homeFragmentBinding.vendorKeyIndexTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.vendorKeyIndex.setText("");

                homeFragmentBinding.samaKeyIndex.setVisibility(View.VISIBLE);
                homeFragmentBinding.samaKeyIndexTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.samaKeyIndex.setText("");
                break;
            case SET_TERMINAL_LANGUAGE:
                homeFragmentBinding.terminalLanguage.setVisibility(View.VISIBLE);
                homeFragmentBinding.terminalLanguageTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.terminalLanguage.setText("");
                break;
            case REGISTER:
            case END_SESSION:
            case START_SESSION:
                homeFragmentBinding.ecrNoTv.setVisibility(View.GONE);
                homeFragmentBinding.ecrNo.setVisibility(View.GONE);
                break;
            case BILL_PAYMENT:
                homeFragmentBinding.billPayAmt.setVisibility(View.VISIBLE);
                homeFragmentBinding.billPayAmtTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.billPayAmt.setText("");

                homeFragmentBinding.billerId.setVisibility(View.VISIBLE);
                homeFragmentBinding.billerIdTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.billerId.setText("");

                homeFragmentBinding.billerNumber.setVisibility(View.VISIBLE);
                homeFragmentBinding.billerNumberTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.billerNumber.setText("");
                break;
            case REPEAT:
                homeFragmentBinding.prevEcrNo.setVisibility(View.VISIBLE);
                homeFragmentBinding.prevEcrNoTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.prevEcrNo.setText(prevEcrNo);
                break;
        }
    }

    public void setReqData(String selectedItem, Context context) {

        date = new SimpleDateFormat("ddMMyyhhmmss", Locale.getDefault()).format(new Date());
        int print = TransactionSettingViewModel.getPrint();
        if (print != 1) {
            print = 0;
        }
        String completion = "1";

        switch (transactionTypeString) {
            case PURCHASE:
                reqData = date + ";" + (int) (Double.parseDouble(String.valueOf(homeFragmentBinding.payAmt.getText())) * 100) + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case PURCHASE_CASHBACK:
                reqData = date + ";" + (int) (Double.parseDouble(String.valueOf(homeFragmentBinding.payAmt.getText())) * 100) + ";" + (int) (Double.parseDouble(String.valueOf(homeFragmentBinding.cashBackAmt.getText())) * 100) + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case REFUND:
                reqData = date + ";" + (int) (Double.parseDouble(String.valueOf(homeFragmentBinding.refundAmt.getText())) * 100) + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + print + ";" + homeFragmentBinding.origRefundDate.getText() + ";" + ecrReferenceNo + "!";
                break;
            case PRE_AUTHORISATION:
                reqData = date + ";" + (int) (Double.parseDouble(String.valueOf(homeFragmentBinding.authAmt.getText())) * 100) + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case PRE_AUTH_COMPLETION:
                reqData = date + ";" + (int) (Double.parseDouble(String.valueOf(homeFragmentBinding.authAmt.getText())) * 100) + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + homeFragmentBinding.origTransactionDate.getText() + ";" + homeFragmentBinding.origApproveCode.getText() + ";" + completion + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case PRE_AUTH_EXTENSION:
                reqData = date + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + homeFragmentBinding.origTransactionDate.getText() + ";" + homeFragmentBinding.origApproveCode.getText() + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case PRE_AUTH_VOID:
                reqData = date + ";" + (int) (Double.parseDouble(String.valueOf(homeFragmentBinding.origTransactionAmt.getText())) * 100) + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + homeFragmentBinding.origTransactionDate.getText() + ";" + homeFragmentBinding.origApproveCode.getText() + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case CASH_ADVANCE:
                reqData = date + ";" + (int) (Double.parseDouble(String.valueOf(homeFragmentBinding.cashAdvanceAmt.getText())) * 100) + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case REVERSAL:
                reqData = date + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case RECONCILIATION:
                reqData = date + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case SET_SETTINGS:
                reqData = date + ";" + homeFragmentBinding.vendorId.getText() + ";" + homeFragmentBinding.vendorTerminalType.getText() + ";" + homeFragmentBinding.trsmId.getText() + ";" + homeFragmentBinding.vendorKeyIndex.getText() + ";" + homeFragmentBinding.samaKeyIndex.getText() + ";" + ecrReferenceNo + "!";
                break;
            case SET_TERMINAL_LANGUAGE:
                reqData = date + ";" + homeFragmentBinding.terminalLanguage.getText() + ";" + ecrReferenceNo + "!";
                break;
            case BILL_PAYMENT:
                reqData = date + ";" + (int) (Double.parseDouble(String.valueOf(homeFragmentBinding.billPayAmt.getText())) * 100) + ";" + homeFragmentBinding.billerId.getText() + ";" + homeFragmentBinding.billerNumber.getText() + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case REGISTER:
            case START_SESSION:
            case END_SESSION:
                szSignature = "0000000000000000000000000000000000000000000000000000000000000000";
                reqData = date + ";" + GeneralParamCache.getInstance().getString(Constant.CASH_REGISTER_NO) + "!";
                break;
            case REPEAT:
                reqData = date + ";" + prevEcrNo + ";" + ecrReferenceNo + "!";
                break;
            case PRINT_SUMMARY_REPORT:
                reqData = date + ";" + "0" + ";" + ecrReferenceNo + "!";
                break;
            case PARAMETER_DOWNLOAD:
            case GET_SETTINGS:
            case TERMINAL_STATUS:
            case PREVIOUS_TRANSACTION_DETAILS:
            case RUNNING_TOTAL:
            case CHECK_STATUS:

            default:
                reqData = date + ";" + ecrReferenceNo + "!";
                break;
        }
        logger.debug("req data" + reqData);
        // save to active txn cache
        ActiveTxnData.getInstance().setReqData(reqData);
        ActiveTxnData.getInstance().setTransactionType(transactionTypeString);
        ActiveTxnData.getInstance().setEcrReferenceNo(ecrReferenceNo);
        ActiveTxnData.getInstance().setSzSignature(szSignature);

    }

    @SuppressLint("DefaultLocale")
    public String getTerminalResponse() throws Exception {

        String ipAddress = GeneralParamCache.getInstance().getString(Constant.IP_ADDRESS);
        int portNumber = Integer.parseInt(GeneralParamCache.getInstance().getString(Constant.PORT));
        int transactionType = transactionTypeString.ordinal();
        String combinedValue = "";
        transactionTypeString = ActiveTxnData.getInstance().getTransactionType();

        GeneralParamCache.getInstance().putString(Constant.PREV_ECR_NO, ecrReferenceNo.substring(ecrReferenceNo.length() - Constant.SIX));

        if (transactionTypeString != TransactionType.START_SESSION && transactionTypeString != TransactionType.END_SESSION && transactionTypeString != TransactionType.REGISTER) {
            combinedValue = ecrReferenceNo.substring(ecrReferenceNo.length() - Constant.SIX) + ActiveTxnData.getInstance().getTerminalID();
            logger.debug(getClass() + "::Combined value>>" + combinedValue);
            logger.debug(getClass() + "::ECR Ref No. Length>>" + ecrReferenceNo.length());
            szSignature = convertSHA(combinedValue);
        }

        logger.debug(getClass() + ":: Request data: " + reqData + ":: TransactionType: " + transactionType + ":: Szsignature: " + szSignature);

        if (ecrSelected != Constant.ONE && transactionTypeString != TransactionType.REGISTER && transactionTypeString != TransactionType.START_SESSION && transactionTypeString != TransactionType.END_SESSION) {
            int ecrNumber = Integer.parseInt(GeneralParamCache.getInstance().getString(Constant.ECR_NUMBER)) + 1;
            GeneralParamCache.getInstance().putString(Constant.ECR_NUMBER, String.format("%06d", ecrNumber));
        }

        StringBuilder terminalResponse = new StringBuilder(ConnectSettingFragment.getEcrCore().doTCPIPTransaction(ipAddress, portNumber, reqData, transactionType, szSignature));
        String terminalResponseString = terminalResponse.toString();
        String[] responseArray = terminalResponseString.split(";");
        String[] splittedResponse;
        String thirdResponse;
        logger.debug("FirstApicall length>> " + responseArray.length);

        if (ActiveTxnData.getInstance().getTransactionType() == TransactionType.PRINT_SUMMARY_REPORT) {

            int count = Integer.parseInt(responseArray[4]);

            for (int i = 1; i <= count; i++) {
                String reqData = date + ";" + i + ";" + ecrReferenceNo + "!";
                String resp = ConnectSettingFragment.getEcrCore().doTCPIPTransaction(ipAddress, portNumber, reqData, transactionType, szSignature);
                String[] secondResponse = resp.split(";");
                System.out.println(secondResponse.length);
                String[] respTemp = new String[secondResponse.length - 2];
                System.out.println(respTemp.length);
                int j = 0;
                for (int k = 3; k < secondResponse.length; k++) {
                    respTemp[j] = secondResponse[k];
                    j = j + 1;
                }
                thirdResponse = terminalResponseString + arrayIntoString(respTemp);
                System.out.println("ThirdResponse" + thirdResponse);

                splittedResponse = thirdResponse.split(";");
                splittedResponse[0] = "22";
                terminalResponseString = thirdResponse;
                ActiveTxnData.getInstance().setSummaryReportArray(splittedResponse);
            }

            if(count == 0) {
                splittedResponse = terminalResponseString.split(";");
                splittedResponse[0] = "22";
                ActiveTxnData.getInstance().setSummaryReportArray(splittedResponse);
            }
        }

        if (transactionTypeString == TransactionType.REGISTER) {
            String[] splittedArray = terminalResponseString.split(";");

            for (int i = 0; i < splittedArray.length; i++) {
                if (i == 3) {
                    terminalID = splittedArray[i];
                    ActiveTxnData.getInstance().setTerminalID(terminalID);
                }
            }
        }

        logger.debug(getClass() + "::Terminal ID>>" + terminalID);

        return terminalResponseString;
    }


    public String arrayIntoString(String[] resp) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < resp.length; i++) {
            sb.append(resp[i]);
            sb.append(";");
        }
        return sb.toString();
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

    public boolean validateData(String selectedItem, Context context) throws Exception {

        if (transactionTypeString != TransactionType.REGISTER && !ActiveTxnData.getInstance().isRegistered()) {
            throw new Exception("Please Register First");
        } else if (transactionTypeString != TransactionType.REGISTER && transactionTypeString != TransactionType.START_SESSION && transactionTypeString != TransactionType.END_SESSION && !ActiveTxnData.getInstance().isSessionStarted()) {
            throw new Exception("Please Start Session");
        }

        if (GeneralParamCache.getInstance().getString(Constant.CASH_REGISTER_NO) == null) {
            GeneralParamCache.getInstance().putString(Constant.CASH_REGISTER_NO, context.getString(R.string.cash_register_no));
        }

        if (ecrSelected == Constant.ONE && !(selectedItem.equals(TransactionType.REGISTER.getTransactionType()) || selectedItem.equals(TransactionType.START_SESSION.getTransactionType()) || selectedItem.equals(TransactionType.END_SESSION.getTransactionType()))) {
            ecrReferenceNo = GeneralParamCache.getInstance().getString(Constant.CASH_REGISTER_NO) + homeFragmentBinding.ecrNo.getText().toString();
        } else {
            ecrReferenceNo = GeneralParamCache.getInstance().getString(Constant.CASH_REGISTER_NO) + GeneralParamCache.getInstance().getString(Constant.ECR_NUMBER);
        }

        logger.debug(getClass() + ":: Ecr refrence no>>" + ecrReferenceNo);
        logger.debug(getClass() + ":: Cash Register no>>" + GeneralParamCache.getInstance().getString(Constant.CASH_REGISTER_NO));


        switch (transactionTypeString) {
            case PURCHASE:
                return validatePurchase();
            case PURCHASE_CASHBACK:
                return validatePurchaseCashBack();
            case REFUND:
                return validateRefund();
            case PRE_AUTHORISATION:
                return validatePreAuthorisation();
            case PRE_AUTH_COMPLETION:
                return validatePreAuthCompletion();
            case PRE_AUTH_EXTENSION:
                return validatePreAuthExtension();
            case PRE_AUTH_VOID:
                return validatePreAuthVoid();
            case CASH_ADVANCE:
                return validateCashAdvance();
            case REVERSAL:
                return validateReversal();
            case SET_SETTINGS:
                return validateSetParameter();
            case REGISTER:
                return validateRegister();
            case END_SESSION:
                return validateEndSession();
            case BILL_PAYMENT:
                return validateBillPayment();
            case SET_TERMINAL_LANGUAGE:
                return validateTerminalLanguage();
            case RECONCILIATION:
            case PARAMETER_DOWNLOAD:
            case GET_SETTINGS:
            case TERMINAL_STATUS:
            case PREVIOUS_TRANSACTION_DETAILS:
            case RUNNING_TOTAL:
            case PRINT_SUMMARY_REPORT:
            case REPEAT:
            case CHECK_STATUS:
            case START_SESSION:
                return validateEcrNo();
        }
        return false;
    }

    private boolean validatePurchase() throws Exception {

        if (ecrReferenceNo.length() == Constant.EIGHT) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != Constant.FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.payAmt.getText().toString().equals("")) {
            throw new Exception("Amount should not be empty");
        } else if (String.valueOf(Double.parseDouble(homeFragmentBinding.payAmt.getText().toString())).equals("0.0")) {
            throw new Exception("Amount should not be 0");
        } else {
            return true;
        }
    }

    private boolean validatePurchaseCashBack() throws Exception {

        if (ecrReferenceNo.length() == Constant.EIGHT) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != Constant.FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.payAmt.getText().toString().equals("")) {
            throw new Exception("Purchase Amount should not be empty");
        } else if (String.valueOf(Double.parseDouble(homeFragmentBinding.payAmt.getText().toString())).equals("0.0")) {
            throw new Exception("Purchase Amount should not be 0");
        } else if (homeFragmentBinding.cashBackAmt.getText().toString().equals("")) {
            throw new Exception("CashBack Amount should not be empty");
        } else if (String.valueOf(Double.parseDouble(homeFragmentBinding.cashBackAmt.getText().toString())).equals("0.0")) {
            throw new Exception("CashBack Amount should not be 0");
        } else if (!(Double.parseDouble(String.valueOf(homeFragmentBinding.payAmt.getText())) > Double.parseDouble(String.valueOf(homeFragmentBinding.cashBackAmt.getText())))) {
            throw new Exception("CashBackAmt should not be More than or equal to Purchase Amt");
        } else return true;
    }

    private boolean validateRefund() throws Exception {

        if (ecrReferenceNo.length() == Constant.EIGHT) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != Constant.FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.refundAmt.getText().toString().equals("")) {
            throw new Exception("Refund Amount should not be empty");
        } else if (String.valueOf(Double.parseDouble(homeFragmentBinding.refundAmt.getText().toString())).equals("0.0")) {
            throw new Exception("Refund Amount should not be 0");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() == Constant.ZERO) {
            throw new Exception("RRN no. should not be empty");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() != Constant.TWELVE) {
            throw new Exception("RRN no. length should be 12 digits");
        } else if (homeFragmentBinding.origRefundDate.getText().length() == Constant.ZERO) {
            throw new Exception("Date should not be empty");
        } else if (homeFragmentBinding.origRefundDate.getText().length() != Constant.SIX) {
            throw new Exception("Date length should be 6 digits");
        } else {
            return true;
        }
    }

    private boolean validatePreAuthorisation() throws Exception {

        if (ecrReferenceNo.length() == Constant.EIGHT) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != Constant.FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.authAmt.getText().toString().equals("")) {
            throw new Exception("Auth Amount should not be empty");
        } else if (String.valueOf(Double.parseDouble(homeFragmentBinding.authAmt.getText().toString())).equals("0.0")) {
            throw new Exception("Auth Amount should not be 0");
        } else {
            return true;
        }
    }

    private boolean validatePreAuthCompletion() throws Exception {

        if (ecrReferenceNo.length() == Constant.EIGHT) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != Constant.FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() == Constant.ZERO) {
            throw new Exception("RRN no. should not be empty");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() != Constant.TWELVE) {
            throw new Exception("RRN no. length should be 12 digits");
        } else if (homeFragmentBinding.authAmt.getText().toString().equals("")) {
            throw new Exception("Auth Amount should not be empty");
        } else if (String.valueOf(Double.parseDouble(homeFragmentBinding.authAmt.getText().toString())).equals("0.0")) {
            throw new Exception("Auth Amount should not be 0");
        } else if (homeFragmentBinding.origTransactionDate.getText().length() == Constant.ZERO) {
            throw new Exception("Date should not be 0");
        } else if (homeFragmentBinding.origTransactionDate.getText().length() != Constant.SIX) {
            throw new Exception("Date length should be 6 digits");
        } else if (homeFragmentBinding.origApproveCode.getText().length() == Constant.ZERO) {
            throw new Exception("Approve code should not be empty");
        } else if (homeFragmentBinding.origApproveCode.getText().length() != Constant.SIX) {
            throw new Exception("Approve code length should be 6 digits");
        } else {
            return true;
        }
    }

    private boolean validatePreAuthExtension() throws Exception {

        if (ecrReferenceNo.length() == Constant.EIGHT) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != Constant.FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() == Constant.ZERO) {
            throw new Exception("RRN no. should not be empty");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() != Constant.TWELVE) {
            throw new Exception("RRN no. length should be 12 digits");
        } else if (homeFragmentBinding.origTransactionDate.getText().length() == Constant.ZERO) {
            throw new Exception("Date should not be 0");
        } else if (homeFragmentBinding.origTransactionDate.getText().length() != Constant.SIX) {
            throw new Exception("Date length should be 6 digits");
        } else if (homeFragmentBinding.origApproveCode.getText().length() == Constant.ZERO) {
            throw new Exception("Approve code should not be empty");
        } else if (homeFragmentBinding.origApproveCode.getText().length() != Constant.SIX) {
            throw new Exception("Approve code length should be 6 digits");
        } else {
            return true;
        }
    }

    private boolean validatePreAuthVoid() throws Exception {

        if (ecrReferenceNo.length() == Constant.EIGHT) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != Constant.FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() == Constant.ZERO) {
            throw new Exception("RRN no. should not be empty");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() != Constant.TWELVE) {
            throw new Exception("RRN no. length should be 12 digits");
        } else if (homeFragmentBinding.origTransactionAmt.getText().toString().equals("")) {
            throw new Exception("Original Transaction Amount should not be empty");
        } else if (String.valueOf(Double.parseDouble(homeFragmentBinding.origTransactionAmt.getText().toString())).equals("0.0")) {
            throw new Exception("Original Transaction Amount should not be 0");
        } else if (homeFragmentBinding.origTransactionDate.getText().length() == Constant.ZERO) {
            throw new Exception("Date should not be 0");
        } else if (homeFragmentBinding.origTransactionDate.getText().length() != Constant.SIX) {
            throw new Exception("Date length should be 6 digits");
        } else if (homeFragmentBinding.origApproveCode.getText().length() == Constant.ZERO) {
            throw new Exception("Approve code should not be empty");
        } else if (homeFragmentBinding.origApproveCode.getText().length() != Constant.SIX) {
            throw new Exception("Approve code length should be 6 digits");
        } else {
            return true;
        }
    }

    private boolean validateCashAdvance() throws Exception {

        if (ecrReferenceNo.length() == Constant.EIGHT) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != Constant.FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.cashAdvanceAmt.getText().toString().equals("")) {
            throw new Exception("Cash Advance Amount should not be empty");
        } else if (String.valueOf(Double.parseDouble(homeFragmentBinding.cashAdvanceAmt.getText().toString())).equals("0.0")) {
            throw new Exception("Cash Advance Amount should not be 0");
        } else {
            return true;
        }
    }

    private boolean validateReversal() throws Exception {

        if (ecrReferenceNo.length() == Constant.EIGHT) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != Constant.FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() == Constant.ZERO) {
            throw new Exception("RRN no. should not be empty");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() != Constant.TWELVE) {
            throw new Exception("RRN no. length should be 12 digit");
        } else {
            return true;
        }
    }

    private boolean validateSetParameter() throws Exception {

        if (ecrReferenceNo.length() == Constant.EIGHT) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != Constant.FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.vendorId.getText().length() == Constant.ZERO) {
            throw new Exception("Vendor Id should not be empty");
        } else if (homeFragmentBinding.vendorId.getText().length() != Constant.TWO) {
            throw new Exception("Vendor Id length should be 2 digits");
        } else if (homeFragmentBinding.vendorTerminalType.getText().length() == Constant.ZERO) {
            throw new Exception("Vendor Terminal Type should not be empty");
        } else if (homeFragmentBinding.vendorTerminalType.getText().length() != Constant.TWO) {
            throw new Exception("Vendor Terminal Type length should be 2 digits");
        } else if (homeFragmentBinding.trsmId.getText().length() == Constant.ZERO) {
            throw new Exception("Trsm Id should not be empty");
        } else if (homeFragmentBinding.trsmId.getText().length() != Constant.SIX) {
            throw new Exception("Trsm Id length should be 6 digits");
        } else if (homeFragmentBinding.vendorKeyIndex.getText().length() == Constant.ZERO) {
            throw new Exception("Vendor key Index should not be empty");
        } else if (homeFragmentBinding.vendorKeyIndex.getText().length() != Constant.TWO) {
            throw new Exception("Vendor key Index length should be 2 digits");
        } else if (homeFragmentBinding.samaKeyIndex.getText().length() == Constant.ZERO) {
            throw new Exception("Sama key Index should not be empty");
        } else if (homeFragmentBinding.samaKeyIndex.getText().length() != Constant.TWO) {
            throw new Exception("Sama key Index length should be 2 digits");
        } else {
            return true;
        }
    }

    private boolean validateTerminalLanguage() throws Exception {

        if (ecrReferenceNo.length() == Constant.EIGHT) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != Constant.FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.terminalLanguage.getText().toString().equals("")) {
            throw new Exception("Terminal language should not be empty");
        } else {
            return true;
        }
    }

    private boolean validateRegister() throws Exception {

        if (GeneralParamCache.getInstance().getString(Constant.CASH_REGISTER_NO).length() != Constant.EIGHT) {
            throw new Exception("Please Enter CashRegister Number in ECR Transaction Settings");
        } else if (ecrReferenceNo.length() == Constant.EIGHT) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != Constant.FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else {
            return true;
        }
    }

    private boolean validateEndSession() throws Exception {

        if (ecrReferenceNo.length() == Constant.EIGHT) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != Constant.FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (!ActiveTxnData.getInstance().isSessionStarted()) {
            throw new Exception("Session not Started");
        } else {
            return true;
        }
    }

    private boolean validateBillPayment() throws Exception {

        if (ecrReferenceNo.length() == Constant.EIGHT) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != Constant.FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.billPayAmt.getText().toString().equals("")) {
            throw new Exception("Bill Amount should not be empty");
        } else if (String.valueOf(Double.parseDouble(homeFragmentBinding.billPayAmt.getText().toString())).equals("0.0")) {
            throw new Exception("Bill Amount should not be 0");
        } else if (homeFragmentBinding.billerId.getText().length() == Constant.ZERO) {
            throw new Exception("Bill Id should not be empty");
        } else if (homeFragmentBinding.billerId.getText().length() != Constant.SIX) {
            throw new Exception("Bill Id length should be 6 digits");
        } else if (homeFragmentBinding.billerNumber.getText().length() == Constant.ZERO) {
            throw new Exception("Bill Number should not be empty");
        } else if (homeFragmentBinding.billerNumber.getText().length() != Constant.SIX) {
            throw new Exception("Bill Number length should be 6 digits");
        } else {
            return true;
        }
    }

    private boolean validateEcrNo() throws Exception {

        if (ecrReferenceNo.length() == Constant.EIGHT) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != Constant.FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else {
            return true;
        }
    }

    public void setParseResponse(String terminalResponse) {
        ActiveTxnData.getInstance().setResData(terminalResponse);
    }
}