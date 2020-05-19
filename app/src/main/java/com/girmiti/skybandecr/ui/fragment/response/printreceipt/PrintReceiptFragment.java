package com.girmiti.skybandecr.ui.fragment.response.printreceipt;

import androidx.activity.OnBackPressedCallback;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.databinding.PrintReceiptFragmentBinding;
import com.girmiti.skybandecr.model.ActiveTxnData;
import com.girmiti.skybandecr.sdk.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import lombok.SneakyThrows;

public class PrintReceiptFragment extends Fragment {

    private PrintReceiptViewModel mViewModel;
    private PrintReceiptFragmentBinding printReceiptFragmentBinding;
    private Logger logger = Logger.getNewLogger(PrintReceiptFragment.class.getName());
    private String receiveData;
    private NavController navController;

    @SneakyThrows
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        printReceiptFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.print_receipt_fragment, container, false);
        mViewModel = ViewModelProviders.of(this).get(PrintReceiptViewModel.class);

        Objects.requireNonNull(getActivity()).findViewById(R.id.home_logo).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.left).setVisibility(View.INVISIBLE);

        setupListeners();

        return printReceiptFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });
    }

    private void setupListeners() throws IOException {

        receiveData = ActiveTxnData.getInstance().getResData();
        logger.debug(getClass() + "::" + "GetRespData>>> " + receiveData);
        String[] receiveDataArray = receiveData.split(";");
        logger.debug(getClass() + "Received Data Array>>" + receiveDataArray);
        String replacedHtmlString = getPrintReceipt(receiveDataArray);
        logger.debug(getClass() + "Replaced Html>>" + replacedHtmlString);
        if (replacedHtmlString != null) {
            String encodedHtml = Base64.encodeToString(replacedHtmlString.getBytes(), Base64.NO_PADDING);
            printReceiptFragmentBinding.webview.loadData(encodedHtml, "text/html", "base64");
        }

        final NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();
        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);

        printReceiptFragmentBinding.buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_printReceiptFragment_to_homeFragment, null, options);
            }
        });

    }

    private String getPrintReceipt(String[] receiveDataArray) throws IOException {
        InputStream is;
        String htmlString = "";
        switch (ActiveTxnData.getInstance().getTransactionType()) {
            case PURCHASE:
                is = getResources().getAssets().open("printReceipt/Purchase(customer_copy).html");
                htmlString = getHtmlString(is);
                if (receiveDataArray.length > 27) {
                    double amount = Double.parseDouble(receiveDataArray[5]) / 100;
                    amount = Math.round(amount * 100.0) / 100.0;
                    String expiryDate = receiveDataArray[8];
                    if (!expiryDate.equals("")) {
                        expiryDate = expiryDate.substring(2, 4) + "/" + expiryDate.substring(0, 2);
                    }
                    String receiveDataArrayDateTime = receiveDataArray[7];
                    logger.debug("DateTime>>" + receiveDataArrayDateTime);
                    String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2);
                    logger.debug("currentDate>>" + currentDate);
                    String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
                    htmlString = htmlString.replace("currentTime", currentTime);
                    htmlString = htmlString.replace("currentDate", currentDate);
                    htmlString = htmlString.replace("panNumber", receiveDataArray[4]);
                    htmlString = htmlString.replace("authCode", receiveDataArray[10]);
                    htmlString = htmlString.replace("approved", receiveDataArray[3]);
                    htmlString = htmlString.replace("CurrentAmount", String.valueOf(amount));
                    htmlString = htmlString.replace("ExpiryDate", expiryDate);
                    htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
                    htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
                    htmlString = htmlString.replace("AID", receiveDataArray[14]);
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
            case PURCHASE_CASHBACK:
                is = getResources().getAssets().open("printReceipt/Purchase_Cashback(customer_copy)).html");
                htmlString = getHtmlString(is);
                if (receiveDataArray.length > 29) {
                    double transactionAmount = Double.parseDouble(receiveDataArray[5]) / 100;
                    transactionAmount = Math.round(transactionAmount * 100.0) / 100.0;
                    double cashBackAmount = Double.parseDouble(receiveDataArray[6]) / 100;
                    double totalAmount = Double.parseDouble(receiveDataArray[7]) / 100;
                    String expiryDate = receiveDataArray[10];
                    if (!expiryDate.equals("")) {
                        expiryDate = expiryDate.substring(2, 4) + "/" + expiryDate.substring(0, 2);
                    }
                    String receiveDataArrayDateTime = receiveDataArray[9];
                    logger.debug("DateTime>>" + receiveDataArrayDateTime);
                    String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2);
                    logger.debug("currentDate>>" + currentDate);
                    String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
                    htmlString = htmlString.replace("currentTime", currentTime);
                    htmlString = htmlString.replace("currentDate", currentDate);
                    //    htmlString = htmlString.replace("panNumber", pan);
                    htmlString = htmlString.replace("authCode", receiveDataArray[12]);
                    htmlString = htmlString.replace("approved", receiveDataArray[3]);
                    htmlString = htmlString.replace("TransactionAmount", String.valueOf(transactionAmount));
                    htmlString = htmlString.replace("CashbackAmount", String.valueOf(cashBackAmount));
                    htmlString = htmlString.replace("TotalAmount", String.valueOf(totalAmount));
                    htmlString = htmlString.replace("ExpiryDate", expiryDate);
                    htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[22]);
                    htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
                    htmlString = htmlString.replace("AID", receiveDataArray[16]);
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
            case REFUND:
                is = getResources().getAssets().open("printReceipt/Refund(customer_copy).html");
                htmlString = getHtmlString(is);

                if (receiveDataArray.length > 27) {
                    double amount = Double.parseDouble(receiveDataArray[5]) / 100;
                    amount = Math.round(amount * 100.0) / 100.0;
                    String expiryDate = receiveDataArray[8];
                    if (!expiryDate.equals("")) {
                        expiryDate = expiryDate.substring(2, 4) + "/" + expiryDate.substring(0, 2);
                    }
                    String receiveDataArrayDateTime = receiveDataArray[7];
                    logger.debug("DateTime>>" + receiveDataArrayDateTime);
                    String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2);
                    logger.debug("currentDate>>" + currentDate);
                    String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
                    htmlString = htmlString.replace("currentTime", currentTime);
                    htmlString = htmlString.replace("currentDate", currentDate);
                    //  htmlString = htmlString.replace("panNumber", pan);
                    htmlString = htmlString.replace("authCode", receiveDataArray[10]);
                    htmlString = htmlString.replace("approved", receiveDataArray[3]);
                    htmlString = htmlString.replace("CurrentAmount", String.valueOf(amount));
                    htmlString = htmlString.replace("ExpiryDate", expiryDate);
                    htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
                    htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
                    htmlString = htmlString.replace("AID", receiveDataArray[14]);
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
            case PRE_AUTHORISATION:
                is = getResources().getAssets().open("printReceipt/Pre-Auth(Customer_copy).html");
                htmlString = getHtmlString(is);
                if (receiveDataArray.length > 27) {
                    double amount = Double.parseDouble(receiveDataArray[5]) / 100;
                    amount = Math.round(amount * 100.0) / 100.0;
                    String expiryDate = receiveDataArray[8];
                    if (expiryDate != "") {
                        expiryDate = expiryDate.substring(2, 4) + "/" + expiryDate.substring(0, 2);
                    }
                    String receiveDataArrayDateTime = receiveDataArray[7];
                    logger.debug("DateTime>>" + receiveDataArrayDateTime);
                    String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2);
                    logger.debug("currentDate>>" + currentDate);
                    String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
                    htmlString = htmlString.replace("currentTime", currentTime);
                    htmlString = htmlString.replace("currentDate", currentDate);
                    //   htmlString = htmlString.replace("panNumber", pan);
                    htmlString = htmlString.replace("authCode", receiveDataArray[10]);
                    htmlString = htmlString.replace("approved", receiveDataArray[3]);
                    htmlString = htmlString.replace("CurrentAmount", String.valueOf(amount));
                    htmlString = htmlString.replace("expiryDate", expiryDate);
                    htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
                    htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
                    htmlString = htmlString.replace("AID", receiveDataArray[14]);
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
            case PRE_AUTH_COMPLETION:
                is = getResources().getAssets().open("printReceipt/Purchase_Advice(Customer_copy).html");
                htmlString = getHtmlString(is);
                if (receiveDataArray.length > 27) {

                    double amount = Double.parseDouble(receiveDataArray[5]) / 100;
                    amount = Math.round(amount * 100.0) / 100.0;
                    String expiryDate = receiveDataArray[8];
                    if (!expiryDate.equals("")) {
                        expiryDate = expiryDate.substring(2, 4) + "/" + expiryDate.substring(0, 2);
                    }
                    String receiveDataArrayDateTime = receiveDataArray[7];
                    logger.debug("DateTime>>" + receiveDataArrayDateTime);
                    String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2);
                    logger.debug("currentDate>>" + currentDate);
                    String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
                    htmlString = htmlString.replace("currentTime", currentTime);
                    htmlString = htmlString.replace("currentDate", currentDate);
                    //   htmlString = htmlString.replace("panNumber", pan);
                    htmlString = htmlString.replace("authCode", receiveDataArray[10]);
                    htmlString = htmlString.replace("approved", receiveDataArray[3]);
                    htmlString = htmlString.replace("CurrentAmount", String.valueOf(amount));
                    htmlString = htmlString.replace("expiryDate", expiryDate);
                    htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
                    htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
                    htmlString = htmlString.replace("AID", receiveDataArray[14]);
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
            case PRE_AUTH_EXTENSION:
                is = getResources().getAssets().open("printReceipt/Pre-Extension(Customer_copy).html");
                htmlString = getHtmlString(is);
                if (receiveDataArray.length > 27) {

                    double amount = Double.parseDouble(receiveDataArray[5]) / 100;
                    amount = Math.round(amount * 100.0) / 100.0;
                    String expiryDate = receiveDataArray[8];
                    if (expiryDate != "") {
                        expiryDate = expiryDate.substring(2, 4) + "/" + expiryDate.substring(0, 2);
                    }
                    String receiveDataArrayDateTime = receiveDataArray[7];
                    logger.debug("DateTime>>" + receiveDataArrayDateTime);
                    String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2);
                    logger.debug("currentDate>>" + currentDate);
                    String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
                    htmlString = htmlString.replace("currentTime", currentTime);
                    htmlString = htmlString.replace("currentDate", currentDate);
                    //  htmlString = htmlString.replace("panNumber", pan);
                    htmlString = htmlString.replace("authCode", receiveDataArray[10]);
                    htmlString = htmlString.replace("approved", receiveDataArray[3]);
                    htmlString = htmlString.replace("CurrentAmount", String.valueOf(amount));
                    htmlString = htmlString.replace("expiryDate", expiryDate);
                    htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
                    htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
                    htmlString = htmlString.replace("AID", receiveDataArray[14]);
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
            case PRE_AUTH_VOID:
                is = getResources().getAssets().open("printReceipt/Pre-void(Customer_copy).html");
                htmlString = getHtmlString(is);
                if (receiveDataArray.length > 27) {
                    double amount = Double.parseDouble(receiveDataArray[5]) / 100;
                    amount = Math.round(amount * 100.0) / 100.0;
                    String expiryDate = receiveDataArray[8];
                    if (expiryDate != "") {
                        expiryDate = expiryDate.substring(2, 4) + "/" + expiryDate.substring(0, 2);
                    }
                    String receiveDataArrayDateTime = receiveDataArray[7];
                    logger.debug("DateTime>>" + receiveDataArrayDateTime);
                    String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2);
                    logger.debug("currentDate>>" + currentDate);
                    String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
                    htmlString = htmlString.replace("currentTime", currentTime);
                    htmlString = htmlString.replace("currentDate", currentDate);
                    //   htmlString = htmlString.replace("panNumber", pan);
                    htmlString = htmlString.replace("authCode", receiveDataArray[10]);
                    htmlString = htmlString.replace("approved", receiveDataArray[3]);
                    htmlString = htmlString.replace("CurrentAmount", String.valueOf(amount));
                    htmlString = htmlString.replace("expiryDate", expiryDate);
                    htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
                    htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
                    htmlString = htmlString.replace("AID", receiveDataArray[14]);
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
            case CASH_ADVANCE:
                is = getResources().getAssets().open("printReceipt/Cash_Advance(Customer_copy).html");
                htmlString = getHtmlString(is);
                if (receiveDataArray.length > 27) {
                    double amount = Double.parseDouble(receiveDataArray[5]) / 100;
                    amount = Math.round(amount * 100.0) / 100.0;
                    String expiryDate = receiveDataArray[8];
                    if (!expiryDate.equals("")) {
                        expiryDate = expiryDate.substring(2, 4) + "/" + expiryDate.substring(0, 2);
                    }
                    String receiveDataArrayDateTime = receiveDataArray[7];
                    logger.debug("DateTime>>" + receiveDataArrayDateTime);
                    String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2);
                    logger.debug("currentDate>>" + currentDate);
                    String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
                    htmlString = htmlString.replace("currentTime", currentTime);
                    htmlString = htmlString.replace("currentDate", currentDate);
                    //  htmlString = htmlString.replace("panNumber", pan);
                    htmlString = htmlString.replace("authCode", receiveDataArray[10]);
                    htmlString = htmlString.replace("approved", receiveDataArray[3]);
                    htmlString = htmlString.replace("CurrentAmount", String.valueOf(amount));
                    htmlString = htmlString.replace("ExpiryDate", expiryDate);
                    htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
                    htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
                    htmlString = htmlString.replace("AID", receiveDataArray[14]);
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
            case REVERSAL:
                is = getResources().getAssets().open("printReceipt/Reversal(Customer_copy).html");
                htmlString = getHtmlString(is);

                if (receiveDataArray.length > 27) {
                    double amount = Double.parseDouble(receiveDataArray[5]) / 100;
                    amount = Math.round(amount * 100.0) / 100.0;
                    String expiryDate = receiveDataArray[8];
                    if (expiryDate != "") {
                        expiryDate = expiryDate.substring(2, 4) + "/" + expiryDate.substring(0, 2);
                    }
                    String receiveDataArrayDateTime = receiveDataArray[7];
                    logger.debug("DateTime>>" + receiveDataArrayDateTime);
                    String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2);
                    logger.debug("currentDate>>" + currentDate);
                    String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
                    htmlString = htmlString.replace("currentTime", currentTime);
                    htmlString = htmlString.replace("currentDate", currentDate);
                    //   htmlString = htmlString.replace("panNumber", pan);
                    htmlString = htmlString.replace("authCode", receiveDataArray[10]);
                    htmlString = htmlString.replace("approved", receiveDataArray[3]);
                    htmlString = htmlString.replace("CurrentAmount", String.valueOf(amount));
                    htmlString = htmlString.replace("expiryDate", expiryDate);
                    htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
                    htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
                    htmlString = htmlString.replace("AID", receiveDataArray[14]);
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
            case RECONCILIATION:

                if (receiveDataArray[2].equals("500") || receiveDataArray[2].equals("501"))
                {
                    is = getResources().getAssets().open("printReceipt/Reconcilation.html");
                    htmlString = getHtmlString(is);
                    is = getResources().getAssets().open("printReceipt/PosTable.html");
                    String SummaryHtmlString = getHtmlString(is);
                    String htmlSummaryReport = SummaryHtmlString;
                    String SummaryFinalReport = "";
                    String receiveDataArrayDateTime = receiveDataArray[4];
                    logger.debug("DateTime>>" + receiveDataArrayDateTime);
                    String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2);
                    logger.debug("currentDate>>" + currentDate);
                    String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
                    int b = 5;
                    int totalSchemeLengthL =Integer.parseInt(receiveDataArray[5]);

                    for (int j = 1; j <= totalSchemeLengthL; j++)
                    {

                        if (receiveDataArray[b + 2].equals("0"))
                        {
                            is = getResources().getAssets().open("printReceipt/PosTable.html");
                            String reconcilationNoTable = getHtmlString(is);
                            String htmlreconcilationNoTable =reconcilationNoTable;
                            htmlreconcilationNoTable = htmlreconcilationNoTable.replace("SchemeNo", receiveDataArray[b + 1]);
                            b = b + 2;

                            SummaryFinalReport += htmlreconcilationNoTable;
                        }
                        else
                        {
                            htmlSummaryReport = htmlSummaryReport.replace("SchemeName", receiveDataArray[b + 1]);
                            htmlSummaryReport = htmlSummaryReport.replace("totalDBCount", receiveDataArray[b + 3]);
                            htmlSummaryReport = htmlSummaryReport.replace("totalDBamount", String.valueOf((Integer.parseInt(receiveDataArray[b + 4])) / 100));
                            htmlSummaryReport = htmlSummaryReport.replace("totalCBCount",  receiveDataArray[b + 5]);
                            htmlSummaryReport = htmlSummaryReport.replace("totalCBamount",  String.valueOf((Integer.parseInt(receiveDataArray[b + 6])) / 100));
                            htmlSummaryReport = htmlSummaryReport.replace("NAQDCount", receiveDataArray[b + 7]);
                            htmlSummaryReport = htmlSummaryReport.replace("NAQDamount",  String.valueOf((Integer.parseInt(receiveDataArray[b + 8])) / 100));
                            htmlSummaryReport = htmlSummaryReport.replace("CADVCount",  receiveDataArray[b + 9]);
                            htmlSummaryReport = htmlSummaryReport.replace("CADVamount", String.valueOf((Integer.parseInt(receiveDataArray[b + 10])) / 100));
                            htmlSummaryReport = htmlSummaryReport.replace("AUTHCount", receiveDataArray[b + 11]);
                            htmlSummaryReport = htmlSummaryReport.replace("AUTHamount", String.valueOf((Integer.parseInt(receiveDataArray[b + 12])) / 100));
                            double TOTALSCount = (Integer.parseInt(receiveDataArray[b + 3]) + Integer.parseInt(receiveDataArray[b + 5]) + Integer.parseInt(receiveDataArray[b + 7]) + Integer.parseInt(receiveDataArray[b + 9]) + Integer.parseInt(receiveDataArray[b + 11]));
                            double TOTALSamount = (Integer.parseInt(receiveDataArray[b + 4]) + Integer.parseInt(receiveDataArray[b + 6]) + Integer.parseInt(receiveDataArray[b + 8]) + Integer.parseInt(receiveDataArray[b + 10]) + Integer.parseInt(receiveDataArray[b + 12])) / 100.0;

                            htmlSummaryReport.replace("TOTALSCount", String.valueOf(TOTALSCount));
                            htmlSummaryReport.replace("TOTALSamount", String.valueOf(TOTALSamount));
                            b = b + 12;
                            SummaryFinalReport += htmlSummaryReport;
                            htmlSummaryReport =SummaryHtmlString;
                        }
                        htmlString = htmlString.replace("PosTable", SummaryFinalReport);
                        htmlString = htmlString.replace("currentTime", currentTime);
                        htmlString = htmlString.replace("currentDate", currentDate);
                    }
                }
                return htmlString;

            case BILL_PAYMENT:
                is = getResources().getAssets().open("printReceipt/Bill_Payment(Customer_copy).html");
                htmlString = getHtmlString(is);

                if (receiveData.length() > 27) {
                    double amount = Double.parseDouble(receiveDataArray[5]) / 100;
                    amount = Math.round(amount * 100.0) / 100.0;
                    String expiryDate = receiveDataArray[8];
                    if (expiryDate != "") {
                        expiryDate = expiryDate.substring(2, 4) + "/" + expiryDate.substring(0, 2);
                    }
                    String receiveDataArrayDateTime = receiveDataArray[7];
                    logger.debug("DateTime>>" + receiveDataArrayDateTime);
                    String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2);
                    logger.debug("currentDate>>" + currentDate);
                    String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
                    htmlString = htmlString.replace("currentTime", currentTime);
                    htmlString = htmlString.replace("currentDate", currentDate);
                    //   htmlString = htmlString.replace("panNumber", pan);
                    htmlString = htmlString.replace("authCode", receiveDataArray[10]);
                    htmlString = htmlString.replace("approved", receiveDataArray[3]);
                    htmlString = htmlString.replace("CurrentAmount", String.valueOf(amount));
                    htmlString = htmlString.replace("expiryDate", expiryDate);
                    htmlString = htmlString.replace("CONTACTLESS", receiveDataArray[20]);
                    htmlString = htmlString.replace("ResponseCode", receiveDataArray[2]);
                    htmlString = htmlString.replace("AID", receiveDataArray[14]);
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
            case PARAMETER_DOWNLOAD:
                is = getResources().getAssets().open("printReceipt/Parameter_download.html");
                htmlString = getHtmlString(is);

                if (receiveData.length() > 4) {
                    String receiveDataArrayDateTime = receiveDataArray[4];
                    logger.debug("DateTime>>" + receiveDataArrayDateTime);
                    String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2);
                    logger.debug("currentDate>>" + currentDate);
                    String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
                    htmlString = htmlString.replace("currentTime", currentTime);
                    htmlString = htmlString.replace("currentDate", currentDate);
                    htmlString = htmlString.replace("terminalId", ActiveTxnData.getInstance().getTerminalID());
                    htmlString = htmlString.replace("ResponseCode", receiveDataArray[3]);
                }
                return htmlString;

            case PRINT_DETAIL_REPORT:
                is = getResources().getAssets().open("printReceipt/Detail_Report.html");
                htmlString = getHtmlString(is);


                return htmlString;
            case PRINT_SUMMARY_REPORT:
                is = getResources().getAssets().open("printReceipt/Summary_Report.html");
                htmlString = getHtmlString(is);

                String receiveDataArrayDateTime = receiveDataArray[4];
                logger.debug("DateTime>>" + receiveDataArrayDateTime);
                String currentDate = receiveDataArrayDateTime.substring(2, 4) + " / " + receiveDataArrayDateTime.substring(0, 2);
                logger.debug("currentDate>>" + currentDate);
                String currentTime = receiveDataArrayDateTime.substring(4, 6) + " :" + receiveDataArrayDateTime.substring(6, 8) + " : " + receiveDataArrayDateTime.substring(8, 10);
                String htmlSummaryReport = htmlString;
                StringBuilder SummaryFinalReport = new StringBuilder();
                int j = 7;
                int transactionsLength = Integer.parseInt(receiveDataArray[6]);
                for (int i = 1; i <= transactionsLength; i++) {

                    htmlSummaryReport = htmlSummaryReport.replace("transactionType", receiveDataArray[j]);
                    htmlSummaryReport = htmlSummaryReport.replace("transactionDate", receiveDataArray[j + 1]);
                    htmlSummaryReport = htmlSummaryReport.replace("transactionRRN", receiveDataArray[j + 2]);
                    htmlSummaryReport = htmlSummaryReport.replace("transactionamount", receiveDataArray[j + 3]);
                    htmlSummaryReport = htmlSummaryReport.replace("transactionState", receiveDataArray[j + 4]);
                    htmlSummaryReport = htmlSummaryReport.replace("transactionTime", receiveDataArray[j + 5]);
                    htmlSummaryReport = htmlSummaryReport.replace("transactionPANNumber", receiveDataArray[j + 6]);
                    htmlSummaryReport = htmlSummaryReport.replace("authCode", receiveDataArray[j + 7]);
                    htmlSummaryReport = htmlSummaryReport.replace("transactionNumber", receiveDataArray[j + 8]);
                    j = j + 9;
                    SummaryFinalReport.append(htmlSummaryReport);
                    htmlSummaryReport = htmlString;
                }

                htmlString = htmlString.replace("no_Transaction", SummaryFinalReport);
                htmlString = htmlString.replace("currentTime", currentTime);
                htmlString = htmlString.replace("currentDate", currentDate);
                htmlString = htmlString.replace("terminalId", ActiveTxnData.getInstance().getTerminalID());

                return htmlString;

            case SET_PARAMETER:
            case SET_TERMINAL_LANGUAGE:
            case REGISTER:
            case START_SESSION:
            case END_SESSION:
            case CHECK_STATUS:
            case GET_PARAMETER:
            case TERMINAL_STATUS:
            case PREVIOUS_TRANSACTION_DETAILS:
            default:
                is = null;
        }
        return null;
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
}

