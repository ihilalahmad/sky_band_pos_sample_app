package com.girmiti.skybandecr.ui.fragment.home;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.lifecycle.ViewModel;

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

        transactionTypeString = TransactionType.valueOf(selectedItem.toUpperCase().replace(" ", "_"));
        prevEcrNo = String.format("%06d", Integer.parseInt(GeneralParamCache.getInstance().getString(Ecr_Number)) - 1);

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
                homeFragmentBinding.ecrNoTv.setVisibility(View.GONE);
                homeFragmentBinding.ecrNo.setVisibility(View.GONE);
                homeFragmentBinding.prevEcrNo.setVisibility(View.VISIBLE);
                homeFragmentBinding.prevEcrNoTv.setVisibility(View.VISIBLE);
                homeFragmentBinding.prevEcrNo.setText(prevEcrNo);
                break;
        }
    }

    public void setReqData(String selectedItem) {

        String date = new SimpleDateFormat("ddMMyyhhmmss", Locale.getDefault()).format(new Date());
        int print = TransactionSettingViewModel.getPrint();
        String completion;

        if (ecrSelected == ONE && !(selectedItem.equals(TransactionType.REGISTER.getTransactionType()) || selectedItem.equals(TransactionType.START_SESSION.getTransactionType()) || selectedItem.equals(TransactionType.END_SESSION.getTransactionType()))) {
            ecrReferenceNo = GeneralParamCache.getInstance().getString(CASH_REGISTER_NO) + homeFragmentBinding.ecrNo.getText().toString();
        } else {
            ecrReferenceNo = GeneralParamCache.getInstance().getString(CASH_REGISTER_NO) + GeneralParamCache.getInstance().getString(Ecr_Number);
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
            case PARAMETER_DOWNLOAD:
            case GET_PARAMETER:
            case TERMINAL_STATUS:
            case PREVIOUS_TRANSACTION_DETAILS:
            case PRINT_DETAIL_REPORT:
            case PRINT_SUMMARY_REPORT:
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

        if (transactionTypeString != TransactionType.START_SESSION && transactionTypeString != TransactionType.END_SESSION && transactionTypeString != TransactionType.REGISTER) {
            combinedValue = ecrReferenceNo.substring(ecrReferenceNo.length() - SIX) + ActiveTxnData.getInstance().getTerminalID();
            logger.debug(getClass() + "::Combined value>>" + combinedValue);
            logger.debug(getClass() + "::ECR Ref No. Length>>" + ecrReferenceNo.length());
            szSignature = convertSHA(combinedValue);
        }

        logger.debug(getClass() + ":: Request data: " + reqData + ":: TransactionType: " + transactionType + ":: Szsignature: " + szSignature);

        if (ecrSelected != ONE && transactionTypeString != TransactionType.REGISTER && transactionTypeString != TransactionType.START_SESSION && transactionTypeString != TransactionType.END_SESSION) {
            int ecrNumber = Integer.parseInt(GeneralParamCache.getInstance().getString(Ecr_Number)) + 1;
            GeneralParamCache.getInstance().putString(Ecr_Number, String.format("%06d", ecrNumber));
        }

        String terminalResponse = ConnectSettingFragment.getEcrCore().doTCPIPTransaction(ipAddress, portNumber, reqData, transactionType, szSignature);

        if (transactionTypeString == TransactionType.REGISTER) {
            String[] splittedArray = terminalResponse.split(";");

            for (int i = 0; i < splittedArray.length; i++) {
                if (i == 3) {
                    terminalID = splittedArray[i];
                    ActiveTxnData.getInstance().setTerminalID(terminalID);
                }
            }
        }

        logger.debug(getClass() + "::Terminal ID>>" + terminalID);
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

        if (transactionTypeString != TransactionType.REGISTER && !ActiveTxnData.getInstance().isRegistered()) {
            throw new Exception("Please Register First");
        } else if (transactionTypeString != TransactionType.REGISTER && transactionTypeString != TransactionType.START_SESSION && transactionTypeString != TransactionType.END_SESSION && !ActiveTxnData.getInstance().isSessionStarted()) {
            throw new Exception("Please Start Session");
        } else if (GeneralParamCache.getInstance().getString(CASH_REGISTER_NO) == null) {
            logger.info("Cash Register No. not entered");
            throw new Exception("Please Enter CashRegister Number in ECR Transaction Settings");
        }
        System.out.println("TransactiontypeString>>>" + transactionTypeString);
        if (transactionTypeString == TransactionType.PURCHASE) {
            return !homeFragmentBinding.payAmt.getText().toString().equals("") && !String.valueOf(Long.parseLong(homeFragmentBinding.payAmt.getText().toString())).equals("0") && ecrReferenceNo.length() == FOURTEEN;

        } else if (transactionTypeString == TransactionType.PURCHASE_CASHBACK) {
            if (!homeFragmentBinding.payAmt.getText().toString().equals("") && !String.valueOf(Long.parseLong(homeFragmentBinding.payAmt.getText().toString())).equals("0") && !homeFragmentBinding.cashBackAmt.getText().toString().equals("") && !String.valueOf(Long.parseLong(homeFragmentBinding.cashBackAmt.getText().toString())).equals("0") && ecrReferenceNo.length() == FOURTEEN) {
                if (Long.parseLong(String.valueOf(homeFragmentBinding.payAmt.getText())) > Long.parseLong(String.valueOf(homeFragmentBinding.cashBackAmt.getText()))) {
                    return true;
                } else throw new Exception("CashBackAmt cannot be More than or equal to Purchase Amt");
            }

        } else if (transactionTypeString == TransactionType.REFUND) {
            return !homeFragmentBinding.refundAmt.getText().toString().equals("") && !String.valueOf(Long.parseLong(homeFragmentBinding.refundAmt.getText().toString())).equals("0") && homeFragmentBinding.rrnNoEditText.getText().length() == TWELVE && homeFragmentBinding.origRefundDate.getText().length() == SIX && ecrReferenceNo.length() == FOURTEEN;

        } else if (transactionTypeString == TransactionType.PRE_AUTHORISATION) {
            return !homeFragmentBinding.authAmt.getText().toString().equals("") && !String.valueOf(Long.parseLong(homeFragmentBinding.authAmt.getText().toString())).equals("0") && ecrReferenceNo.length() == FOURTEEN;

        } else if (transactionTypeString == TransactionType.PRE_AUTH_COMPLETION) {
            return !homeFragmentBinding.authAmt.getText().toString().equals("") && !String.valueOf(Long.parseLong(homeFragmentBinding.authAmt.getText().toString())).equals("0") && homeFragmentBinding.rrnNoEditText.getText().length() == TWELVE && homeFragmentBinding.origTransactionDate.getText().length() == SIX && homeFragmentBinding.origApproveCode.getText().length() == SIX && ecrReferenceNo.length() == FOURTEEN;

        } else if (transactionTypeString == TransactionType.PRE_AUTH_EXTENSION) {
            return homeFragmentBinding.rrnNoEditText.getText().length() == TWELVE && homeFragmentBinding.origTransactionDate.getText().length() == SIX && homeFragmentBinding.origApproveCode.getText().length() == SIX && ecrReferenceNo.length() == FOURTEEN;

        } else if (transactionTypeString == TransactionType.PRE_AUTH_VOID) {
            return !homeFragmentBinding.origTransactionAmt.getText().toString().equals("") && !String.valueOf(Long.parseLong(homeFragmentBinding.origTransactionAmt.getText().toString())).equals("0") && homeFragmentBinding.rrnNoEditText.getText().length() == TWELVE && homeFragmentBinding.origTransactionDate.getText().length() == SIX && homeFragmentBinding.origApproveCode.getText().length() == SIX && ecrReferenceNo.length() == FOURTEEN;

        } else if (transactionTypeString == TransactionType.CASH_ADVANCE) {
            return !homeFragmentBinding.cashAdvanceAmt.getText().toString().equals("") && !String.valueOf(Long.parseLong(homeFragmentBinding.cashAdvanceAmt.getText().toString())).equals("0") && ecrReferenceNo.length() == FOURTEEN;

        } else if (transactionTypeString == TransactionType.REVERSAL) {
            return homeFragmentBinding.rrnNoEditText.getText().length() == TWELVE && ecrReferenceNo.length() == FOURTEEN;

        } else if (transactionTypeString == TransactionType.RECONCILIATION || transactionTypeString == TransactionType.PARAMETER_DOWNLOAD) {
            return ecrReferenceNo.length() == FOURTEEN;

        } else if (transactionTypeString == TransactionType.SET_PARAMETER) {
            return homeFragmentBinding.vendorId.getText().length() == SIX && homeFragmentBinding.vendorTerminalType.getText().length() == ONE && homeFragmentBinding.trsmId.getText().length() == SIX && homeFragmentBinding.vendorKeyIndex.getText().length() == ONE && homeFragmentBinding.samaKeyIndex.getText().length() == ONE;

        } else if (transactionTypeString == TransactionType.GET_PARAMETER || transactionTypeString == TransactionType.SET_TERMINAL_LANGUAGE || transactionTypeString == TransactionType.TERMINAL_STATUS || transactionTypeString == TransactionType.PREVIOUS_TRANSACTION_DETAILS || transactionTypeString == TransactionType.PRINT_DETAIL_REPORT || transactionTypeString == TransactionType.PRINT_SUMMARY_REPORT) {
            return ecrReferenceNo.length() == FOURTEEN;

        } else if (transactionTypeString == TransactionType.REGISTER) {
            if (GeneralParamCache.getInstance().getString(CASH_REGISTER_NO).length() == EIGHT) {
                return ecrReferenceNo.length() == FOURTEEN;
            } else
                throw new Exception("Please Enter CashRegister Number in ECR Transaction Settings");

        } else if (transactionTypeString == TransactionType.START_SESSION) {
            return ecrReferenceNo.length() == FOURTEEN;

        } else if (transactionTypeString == TransactionType.END_SESSION) {
            return ecrReferenceNo.length() == FOURTEEN;

        } else if (transactionTypeString == TransactionType.BILL_PAYMENT) {
            return !homeFragmentBinding.billPayAmt.getText().toString().equals("") && !String.valueOf(Long.parseLong(homeFragmentBinding.billPayAmt.getText().toString())).equals("0") && homeFragmentBinding.billerId.getText().length() == SIX && homeFragmentBinding.billerNumber.getText().length() == SIX;

        } else if (transactionTypeString == TransactionType.CHECK_STATUS) {
            return ecrReferenceNo.length() == FOURTEEN;
        }
        return false;
    }

    public void setParseResponse(String terminalResponse) {
        ActiveTxnData.getInstance().setResData(terminalResponse);
    }
}


