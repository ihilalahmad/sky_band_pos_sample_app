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
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class HomeViewModel extends ViewModel implements Constant {




    private HomeFragmentBinding homeFragmentBinding;
    private Logger logger = Logger.getNewLogger(HomeViewModel.class.getName());
    private int ecrSelected = TransactionSettingViewModel.getEcr();

    private String reqData = "";
    public TransactionType transactionTypeString = TransactionType.PURCHASE;
    private String terminalID = "";
    private String szSignature = "";
    private String ecrReferenceNo = "";
    private String prevEcrNo = "";
    private String date = "";

    @SuppressLint("DefaultLocale")
    public void resetVisibilityOfViews(HomeFragmentBinding homeFragmentBinding) {

        this.homeFragmentBinding = homeFragmentBinding;
        if (ecrSelected != ONE) {
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

        this.homeFragmentBinding.typeOfCompletion.setVisibility(View.GONE);

        this.homeFragmentBinding.prevEcrNo.setVisibility(View.GONE);
        this.homeFragmentBinding.prevEcrNoTv.setVisibility(View.GONE);
    }

    @SuppressLint("DefaultLocale")
    public void getVisibilityOfViews(String selectedItem) {

        String a = "\u0002�C1�00�APPROVED�080620183836�0�73�AUTH�060620183836�015813000045�0000000001.11�Yes�APP�1356�-VC-MSR�010028�000057�ADV�060620�015814000046�0000000001.11�Yes�APP�1400�-VC-MSR�010028�000057�AUTH�060620�015814000048�0000000001.00�Yes�APP�1404�-VC-MSR�010028�000057�ADV�060620�015814000049�0000000001.00�Yes�APP�1406�-VC-MSR�010028�000057�AUTH�060620�015814000050�0000000001.99�Yes�APP�1423�-P1-ICC�010859�000057�ADV�060620�015814000052�0000000001.99�Yes�APP�1424�-P1-ICC�010859�000057�ADV�060620�015814000053�0000000001.99�Yes�APP�1426�-P1-ICC�010859�000057�AUTH�060620�015814000054�0000000004.44�Yes�APP�1432�-VC-MSR�010028�000057�ADV�060620�015814000055�0000000004.44�Yes�APP�1434�-VC-MSR�010028�000057�AUTH�060620�015815000056�0000000002.99�Yes�APP�1517�-P1-KEY�010859�000057�AUTH�060620�015815000058�0000000002.44�Yes�APP�1518�-P1-KEY�010859�000070�ADV�060620�015815000059�0000000002.44�Yes�APP�1522�-P1-KEY�010859�000070�AUTH�060620�015815000060�0000000001.23�Yes�APP�1524�-P1-ICC�010859�000070�ADV�060620�015815000062�0000000001.23�Yes�APP�1526�-P1-KEY�010859�000070�AUTH�060620�015815000063�0000000001.00�Yes�APP�1529�-VC-MSR�010028�000070�ADV�060620�015815000064�0000000001.00�Yes�APP�1531�-P1-KEY�010028�000070�ADV�060620�015815000065�0000000001.00�Yes�APP�1534�-VC-MSR�010028�000070�AUTH�060620�015815000066�0000000002.00�Yes�APP�1535�-VC-MSR�010028�000070�ADV�060620�015815000067�0000000002.00�Yes�APP�1537�-VC-MSR�010028�000070�AUTH�060620";
        logger.debug("length>>" + a.length());
        transactionTypeString = TransactionType.valueOf(selectedItem.toUpperCase().replace(" ", "_"));
        prevEcrNo = GeneralParamCache.getInstance().getString(PREV_ECR_NO);

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

                homeFragmentBinding.typeOfCompletion.setVisibility(View.VISIBLE);
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
            case SET_PARAMETER:
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
            case CHECK_STATUS:
                homeFragmentBinding.prevEcrNo.setVisibility(View.VISIBLE);
                homeFragmentBinding.prevEcrNoTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.prevEcrNo.setText(prevEcrNo);
                break;
        }
    }

    public void setReqData(String selectedItem, Context context) {

        date = new SimpleDateFormat("ddMMyyhhmmss", Locale.getDefault()).format(new Date());
        int print = TransactionSettingViewModel.getPrint();
        String completion;

        if (GeneralParamCache.getInstance().getString(CASH_REGISTER_NO) == null) {
            GeneralParamCache.getInstance().putString(CASH_REGISTER_NO, context.getString(R.string.cash_register_no));
        }

        if (ecrSelected == ONE && !(selectedItem.equals(TransactionType.REGISTER.getTransactionType()) || selectedItem.equals(TransactionType.START_SESSION.getTransactionType()) || selectedItem.equals(TransactionType.END_SESSION.getTransactionType()))) {
            ecrReferenceNo = GeneralParamCache.getInstance().getString(CASH_REGISTER_NO) + homeFragmentBinding.ecrNo.getText().toString();
        } else {
            ecrReferenceNo = GeneralParamCache.getInstance().getString(CASH_REGISTER_NO) + GeneralParamCache.getInstance().getString(ECR_NUMBER);
        }

        logger.debug(getClass() + ":: Ecr refrence no>>" + ecrReferenceNo);
        logger.debug(getClass() + ":: Cash Register no>>" + GeneralParamCache.getInstance().getString(CASH_REGISTER_NO));

        if (homeFragmentBinding.typeOfCompletion.isChecked()) {
            completion = "1";
        } else {
            completion = "0";
        }

        switch (transactionTypeString) {
            case PURCHASE:
                reqData = date + ";" + homeFragmentBinding.payAmt.getText() + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case PURCHASE_CASHBACK:
                reqData = date + ";" + homeFragmentBinding.payAmt.getText() + ";" + homeFragmentBinding.cashBackAmt.getText() + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case REFUND:
                reqData = date + ";" + homeFragmentBinding.refundAmt.getText() + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + print + ";" + homeFragmentBinding.origRefundDate.getText() + ";" + ecrReferenceNo + "!";
                break;
            case PRE_AUTHORISATION:
                reqData = date + ";" + homeFragmentBinding.authAmt.getText() + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case PRE_AUTH_COMPLETION:
                reqData = date + ";" + homeFragmentBinding.authAmt.getText() + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + homeFragmentBinding.origTransactionDate.getText() + ";" + homeFragmentBinding.origApproveCode.getText() + ";" + completion + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case PRE_AUTH_EXTENSION:
                reqData = date + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + homeFragmentBinding.origTransactionDate.getText() + ";" + homeFragmentBinding.origApproveCode.getText() + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case PRE_AUTH_VOID:
                reqData = date + ";" + homeFragmentBinding.origTransactionAmt.getText() + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + homeFragmentBinding.origTransactionDate.getText() + ";" + homeFragmentBinding.origApproveCode.getText() + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case CASH_ADVANCE:
                reqData = date + ";" + homeFragmentBinding.cashAdvanceAmt.getText() + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case REVERSAL:
                reqData = date + ";" + homeFragmentBinding.rrnNoEditText.getText() + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case RECONCILIATION:
                reqData = date + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case SET_PARAMETER:
                reqData = date + ";" + homeFragmentBinding.vendorId.getText() + ";" + homeFragmentBinding.vendorTerminalType.getText() + ";" + homeFragmentBinding.trsmId.getText() + ";" + homeFragmentBinding.vendorKeyIndex.getText() + ";" + homeFragmentBinding.samaKeyIndex.getText() + ";" + ecrReferenceNo + "!";
                break;
            case SET_TERMINAL_LANGUAGE:
                reqData = date + ";" + homeFragmentBinding.terminalLanguage.getText() + ";" + ecrReferenceNo + "!";
                break;
            case BILL_PAYMENT:
                reqData = date + ";" + homeFragmentBinding.billPayAmt.getText() + ";" + homeFragmentBinding.billerId.getText() + ";" + homeFragmentBinding.billerNumber.getText() + ";" + print + ";" + ecrReferenceNo + "!";
                break;
            case REGISTER:
            case START_SESSION:
            case END_SESSION:
                szSignature = "0000000000000000000000000000000000000000000000000000000000000000";
                reqData = date + ";" + GeneralParamCache.getInstance().getString(CASH_REGISTER_NO) + "!";
                break;
            case CHECK_STATUS:
                reqData = date + ";" + prevEcrNo + ";" + ecrReferenceNo + "!";
                break;
            case PRINT_SUMMARY_REPORT:
                reqData = date + ";" + "0" + ";" + ecrReferenceNo + "!";
                break;
            case PARAMETER_DOWNLOAD:
            case GET_PARAMETER:
            case TERMINAL_STATUS:
            case PREVIOUS_TRANSACTION_DETAILS:
            case PRINT_DETAIL_REPORT:

            default:
                reqData = date + ";" + ecrReferenceNo + "!";
                break;
        }

        // save to active txn cache
        ActiveTxnData.getInstance().setReqData(reqData);
        ActiveTxnData.getInstance().setTransactionType(transactionTypeString);
        ActiveTxnData.getInstance().setEcrReferenceNo(ecrReferenceNo);
        ActiveTxnData.getInstance().setSzSignature(szSignature);

    }

    @SuppressLint("DefaultLocale")
    public String getTerminalResponse() throws Exception {

        String ipAddress = GeneralParamCache.getInstance().getString(IP_ADDRESS);
        int portNumber = Integer.parseInt(GeneralParamCache.getInstance().getString(PORT));
        int transactionType = transactionTypeString.ordinal();
        String combinedValue = "";
        transactionTypeString = ActiveTxnData.getInstance().getTransactionType();

        GeneralParamCache.getInstance().putString(PREV_ECR_NO, ecrReferenceNo.substring(ecrReferenceNo.length() - SIX));

        if (transactionTypeString != TransactionType.START_SESSION && transactionTypeString != TransactionType.END_SESSION && transactionTypeString != TransactionType.REGISTER) {
            combinedValue = ecrReferenceNo.substring(ecrReferenceNo.length() - SIX) + ActiveTxnData.getInstance().getTerminalID();
            logger.debug(getClass() + "::Combined value>>" + combinedValue);
            logger.debug(getClass() + "::ECR Ref No. Length>>" + ecrReferenceNo.length());
            szSignature = convertSHA(combinedValue);
        }

        logger.debug(getClass() + ":: Request data: " + reqData + ":: TransactionType: " + transactionType + ":: Szsignature: " + szSignature);

        if (ecrSelected != ONE && transactionTypeString != TransactionType.REGISTER && transactionTypeString != TransactionType.START_SESSION && transactionTypeString != TransactionType.END_SESSION) {
            int ecrNumber = Integer.parseInt(GeneralParamCache.getInstance().getString(ECR_NUMBER)) + 1;
            GeneralParamCache.getInstance().putString(ECR_NUMBER, String.format("%06d", ecrNumber));
        }

        StringBuilder terminalResponse = new StringBuilder(ConnectSettingFragment.getEcrCore().doTCPIPTransaction(ipAddress, portNumber, reqData, transactionType, szSignature));
        String terminalResponseString = terminalResponse.toString();
        String[] responseArray = terminalResponseString.split(";");
        String[] responseTemp;

        logger.debug("FirstApicall length>> " + responseArray.length);

        if (Integer.parseInt(responseArray[1]) == 22 && Integer.parseInt(responseArray[5]) != 0) {

            responseTemp = new String[terminalResponseString.length() - 2];
            int count = Integer.parseInt(responseArray[5]);
            int m = 1;
            for (int n = 1; n < terminalResponseString.length() - 2; n++) {
                responseTemp[m] = responseArray[n];
                m = m + 1;
            }

            if (count > 0) {

                for (int i = 1; i <= count; i++) {

                    reqData = date + ";" + i + ";" + ecrReferenceNo + "!";
                    String terminalResponseString1 = ConnectSettingFragment.getEcrCore().doTCPIPTransaction(ipAddress, portNumber, reqData, transactionType, szSignature);
                    logger.debug("terminal Response Sumarry Report>> " + i + terminalResponseString1);
                    String[] responseArray1 = terminalResponseString1.split(";");
                    responseTemp = new String[responseArray1.length - 6];
                    int j = 0;
                    for (int k = 4; k < responseArray1.length - 2; k++) {
                        responseTemp[j] = responseArray1[k];
                        j = j + 1;
                    }
                    terminalResponseString = Arrays.toString(responseTemp);
                    terminalResponse.append(terminalResponseString);
                }
            }
        }

        if (transactionTypeString == TransactionType.REGISTER) {
            String[] splittedArray = terminalResponse.toString().split(";");

            for (int i = 0; i < splittedArray.length; i++) {
                if (i == 3) {
                    terminalID = splittedArray[i];
                    ActiveTxnData.getInstance().setTerminalID(terminalID);
                }
            }
        }

        logger.debug(getClass() + "::Terminal ID>>" + terminalID);

        return terminalResponse.toString();
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

    public boolean validateData() throws Exception {

        if (transactionTypeString != TransactionType.REGISTER && !ActiveTxnData.getInstance().isRegistered()) {
            throw new Exception("Please Register First");
        } else if (transactionTypeString != TransactionType.REGISTER && transactionTypeString != TransactionType.START_SESSION && transactionTypeString != TransactionType.END_SESSION && !ActiveTxnData.getInstance().isSessionStarted()) {
            throw new Exception("Please Start Session");
        }

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
            case SET_PARAMETER:
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
            case GET_PARAMETER:
            case TERMINAL_STATUS:
            case PREVIOUS_TRANSACTION_DETAILS:
            case PRINT_DETAIL_REPORT:
            case PRINT_SUMMARY_REPORT:
            case CHECK_STATUS:
            case START_SESSION:
                return validateEcrNo();
        }
        return false;
    }

    private boolean validatePurchase() throws Exception {

        if (ecrReferenceNo.length() == ZERO) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.payAmt.getText().toString().equals("")) {
            throw new Exception("Amount should not be empty");
        } else if (String.valueOf(Long.parseLong(homeFragmentBinding.payAmt.getText().toString())).equals("0")) {
            throw new Exception("Amount should not be 0");
        } else {
            return true;
        }
    }

    private boolean validatePurchaseCashBack() throws Exception {

        if (ecrReferenceNo.length() == ZERO) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.payAmt.getText().toString().equals("")) {
            throw new Exception("Purchase Amount should not be empty");
        } else if (String.valueOf(Long.parseLong(homeFragmentBinding.payAmt.getText().toString())).equals("0")) {
            throw new Exception("Purchase Amount should not be 0");
        } else if (homeFragmentBinding.cashBackAmt.getText().toString().equals("")) {
            throw new Exception("CashBack Amount should not be empty");
        } else if (String.valueOf(Long.parseLong(homeFragmentBinding.cashBackAmt.getText().toString())).equals("0")) {
            throw new Exception("CashBack Amount should not be 0");
        } else if (!(Long.parseLong(String.valueOf(homeFragmentBinding.payAmt.getText())) > Long.parseLong(String.valueOf(homeFragmentBinding.cashBackAmt.getText())))) {
            throw new Exception("CashBackAmt should not be More than or equal to Purchase Amt");
        } else return true;
    }

    private boolean validateRefund() throws Exception {

        if (ecrReferenceNo.length() == ZERO) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.refundAmt.getText().toString().equals("")) {
            throw new Exception("Refund Amount should not be empty");
        } else if (String.valueOf(Long.parseLong(homeFragmentBinding.refundAmt.getText().toString())).equals("0")) {
            throw new Exception("Refund Amount should not be 0");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() == ZERO) {
            throw new Exception("RRN no. should not be empty");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() != TWELVE) {
            throw new Exception("RRN no. length should be 12 digits");
        } else if (homeFragmentBinding.origRefundDate.getText().length() == ZERO) {
            throw new Exception("Date should not be empty");
        } else if (homeFragmentBinding.origRefundDate.getText().length() != SIX) {
            throw new Exception("Date length should be 6 digits");
        } else {
            return true;
        }
    }

    private boolean validatePreAuthorisation() throws Exception {

        if (ecrReferenceNo.length() == ZERO) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.authAmt.getText().toString().equals("")) {
            throw new Exception("Auth Amount should not be empty");
        } else if (String.valueOf(Long.parseLong(homeFragmentBinding.authAmt.getText().toString())).equals("0")) {
            throw new Exception("Auth Amount should not be 0");
        } else {
            return true;
        }
    }

    private boolean validatePreAuthCompletion() throws Exception {

        if (ecrReferenceNo.length() == ZERO) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() == ZERO) {
            throw new Exception("RRN no. should not be empty");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() != TWELVE) {
            throw new Exception("RRN no. length should be 12 digits");
        } else if (homeFragmentBinding.authAmt.getText().toString().equals("")) {
            throw new Exception("Auth Amount should not be empty");
        } else if (String.valueOf(Long.parseLong(homeFragmentBinding.authAmt.getText().toString())).equals("0")) {
            throw new Exception("Auth Amount should not be 0");
        } else if (homeFragmentBinding.origTransactionDate.getText().length() == ZERO) {
            throw new Exception("Date should not be 0");
        } else if (homeFragmentBinding.origTransactionDate.getText().length() != SIX) {
            throw new Exception("Date length should be 6 digits");
        } else if (homeFragmentBinding.origApproveCode.getText().length() == ZERO) {
            throw new Exception("Approve code should not be empty");
        } else if (homeFragmentBinding.origApproveCode.getText().length() != SIX) {
            throw new Exception("Approve code length should be 6 digits");
        } else {
            return true;
        }
    }

    private boolean validatePreAuthExtension() throws Exception {

        if (ecrReferenceNo.length() == ZERO) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() == ZERO) {
            throw new Exception("RRN no. should not be empty");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() != TWELVE) {
            throw new Exception("RRN no. length should be 12 digits");
        } else if (homeFragmentBinding.origTransactionDate.getText().length() == ZERO) {
            throw new Exception("Date should not be 0");
        } else if (homeFragmentBinding.origTransactionDate.getText().length() != SIX) {
            throw new Exception("Date length should be 6 digits");
        } else if (homeFragmentBinding.origApproveCode.getText().length() == ZERO) {
            throw new Exception("Approve code should not be empty");
        } else if (homeFragmentBinding.origApproveCode.getText().length() != SIX) {
            throw new Exception("Approve code length should be 6 digits");
        } else {
            return true;
        }
    }

    private boolean validatePreAuthVoid() throws Exception {

        if (ecrReferenceNo.length() == ZERO) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() == ZERO) {
            throw new Exception("RRN no. should not be empty");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() != TWELVE) {
            throw new Exception("RRN no. length should be 12 digits");
        } else if (homeFragmentBinding.origTransactionAmt.getText().toString().equals("")) {
            throw new Exception("Original Transaction Amount should not be empty");
        } else if (String.valueOf(Long.parseLong(homeFragmentBinding.origTransactionAmt.getText().toString())).equals("0")) {
            throw new Exception("Original Transaction Amount should not be 0");
        } else if (homeFragmentBinding.origTransactionDate.getText().length() == ZERO) {
            throw new Exception("Date should not be 0");
        } else if (homeFragmentBinding.origTransactionDate.getText().length() != SIX) {
            throw new Exception("Date length should be 6 digits");
        } else if (homeFragmentBinding.origApproveCode.getText().length() == ZERO) {
            throw new Exception("Approve code should not be empty");
        } else if (homeFragmentBinding.origApproveCode.getText().length() != SIX) {
            throw new Exception("Approve code length should be 6 digits");
        } else {
            return true;
        }
    }

    private boolean validateCashAdvance() throws Exception {

        if (ecrReferenceNo.length() == ZERO) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.cashAdvanceAmt.getText().toString().equals("")) {
            throw new Exception("Cash Advance Amount should not be empty");
        } else if (String.valueOf(Long.parseLong(homeFragmentBinding.cashAdvanceAmt.getText().toString())).equals("0")) {
            throw new Exception("Cash Advance Amount should not be 0");
        } else {
            return true;
        }
    }

    private boolean validateReversal() throws Exception {

        if (ecrReferenceNo.length() == ZERO) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() == ZERO) {
            throw new Exception("RRN no. should not be empty");
        } else if (homeFragmentBinding.rrnNoEditText.getText().length() != TWELVE) {
            throw new Exception("RRN no. length should be 12 digit");
        } else {
            return true;
        }
    }

    private boolean validateSetParameter() throws Exception {

        if (ecrReferenceNo.length() == ZERO) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.vendorId.getText().length() == ZERO) {
            throw new Exception("Vendor Id should not be empty");
        } else if (homeFragmentBinding.vendorId.getText().length() != TWO) {
            throw new Exception("Vendor Id length should be 2 digits");
        } else if (homeFragmentBinding.vendorTerminalType.getText().length() == ZERO) {
            throw new Exception("Vendor Terminal Type should not be empty");
        } else if (homeFragmentBinding.vendorTerminalType.getText().length() != TWO) {
            throw new Exception("Vendor Terminal Type length should be 2 digits");
        } else if (homeFragmentBinding.trsmId.getText().length() == ZERO) {
            throw new Exception("Trsm Id should not be empty");
        } else if (homeFragmentBinding.trsmId.getText().length() != SIX) {
            throw new Exception("Trsm Id length should be 6 digits");
        } else if (homeFragmentBinding.vendorKeyIndex.getText().length() == ZERO) {
            throw new Exception("Vendor key Index should not be empty");
        } else if (homeFragmentBinding.vendorKeyIndex.getText().length() != TWO) {
            throw new Exception("Vendor key Index length should be 2 digits");
        } else if (homeFragmentBinding.samaKeyIndex.getText().length() == ZERO) {
            throw new Exception("Sama key Index should not be empty");
        } else if (homeFragmentBinding.samaKeyIndex.getText().length() != TWO) {
            throw new Exception("Sama key Index length should be 2 digits");
        } else {
            return true;
        }
    }

    private boolean validateTerminalLanguage() throws Exception {

        if (ecrReferenceNo.length() == ZERO) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.terminalLanguage.getText().toString().equals("")) {
            throw new Exception("Terminal language should not be empty");
        } else {
            return true;
        }
    }

    private boolean validateRegister() throws Exception {

        if (GeneralParamCache.getInstance().getString(CASH_REGISTER_NO).length() != EIGHT) {
            throw new Exception("Please Enter CashRegister Number in ECR Transaction Settings");
        } else if (ecrReferenceNo.length() == ZERO) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else {
            return true;
        }
    }

    private boolean validateEndSession() throws Exception {

        if (ecrReferenceNo.length() == ZERO) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (!ActiveTxnData.getInstance().isSessionStarted()) {
            throw new Exception("Session not Started");
        } else {
            return true;
        }
    }

    private boolean validateBillPayment() throws Exception {

        if (ecrReferenceNo.length() == ZERO) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else if (homeFragmentBinding.billPayAmt.getText().toString().equals("")) {
            throw new Exception("Bill Amount should not be empty");
        } else if (String.valueOf(Long.parseLong(homeFragmentBinding.billPayAmt.getText().toString())).equals("0")) {
            throw new Exception("Bill Amount should not be 0");
        } else if (homeFragmentBinding.billerId.getText().length() == ZERO) {
            throw new Exception("Bill Id should not be empty");
        } else if (homeFragmentBinding.billerId.getText().length() != SIX) {
            throw new Exception("Bill Id length should be 6 digits");
        } else if (homeFragmentBinding.billerNumber.getText().length() == ZERO) {
            throw new Exception("Bill Number should not be empty");
        } else if (homeFragmentBinding.billerNumber.getText().length() != SIX) {
            throw new Exception("Bill Number length should be 6 digits");
        } else {
            return true;
        }
    }

    private boolean validateEcrNo() throws Exception {

        if (ecrReferenceNo.length() == ZERO) {
            throw new Exception("Ecr no. should not be empty");
        } else if (ecrReferenceNo.length() != FOURTEEN) {
            throw new Exception("Ecr no. length should be 6 digits");
        } else {
            return true;
        }
    }

    public void setParseResponse(String terminalResponse) {
        ActiveTxnData.getInstance().setResData(terminalResponse);
    }
}