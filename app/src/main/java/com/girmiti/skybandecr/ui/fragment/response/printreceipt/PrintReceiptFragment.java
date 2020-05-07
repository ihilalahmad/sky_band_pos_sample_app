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
        logger.debug(getClass()+"Received Data Array>>" + receiveDataArray);
        String replacedHtmlString = getPrintReceipt(receiveDataArray);
        logger.debug(getClass() +"Replaced Html>>" + replacedHtmlString);
        if(replacedHtmlString != null) {
            String encodedHtml = Base64.encodeToString(replacedHtmlString.getBytes(), Base64.NO_PADDING);
            printReceiptFragmentBinding.webview.loadData(encodedHtml,"text/html", "base64");
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
        String htmlString;

        switch (ActiveTxnData.getInstance().getTransactionType()) {
            case PURCHASE:
                is = getResources().getAssets().open("printReceipt/Purchase(customer_copy).html");
                htmlString = getHtmlString(is);
                if(receiveDataArray.length >= 26) {
                    htmlString = htmlString.replace("CurrentAmount",receiveDataArray[5]);
                    htmlString = htmlString.replace("currentDate",receiveDataArray[7].substring(0, 6));
                    htmlString = htmlString.replace("currentTime",receiveDataArray[7].substring(6));
                    htmlString = htmlString.replace("panNumber",receiveDataArray[4]);
                    htmlString = htmlString.replace("authCode",receiveDataArray[10]);
                    htmlString = htmlString.replace("ExpiryDate",receiveDataArray[8]);
                    htmlString = htmlString.replace("TerminalVersion",receiveDataArray[26]);
                    htmlString = htmlString.replace("CONTACTLESS",receiveDataArray[20]);
                    htmlString = htmlString.replace("ResponseCode",receiveDataArray[2]);
                    htmlString = htmlString.replace("AID",receiveDataArray[14]);
                    htmlString = htmlString.replace("TVR",receiveDataArray[18]);
                    htmlString = htmlString.replace("CVR",receiveDataArray[17]);
                    htmlString = htmlString.replace("applicationCryptogram",receiveDataArray[15]);
                    htmlString = htmlString.replace("CID",receiveDataArray[16]);
                    htmlString = htmlString.replace("MID",receiveDataArray[12]);
                    htmlString = htmlString.replace("TID",receiveDataArray[11]);
                    htmlString = htmlString.replace("RRN",receiveDataArray[9]);
                    htmlString = htmlString.replace("StanNo",receiveDataArray[6]);
                    htmlString = htmlString.replace("ApplicationVersion",receiveDataArray[26]);
                    htmlString = htmlString.replace("SchemeLabel",receiveDataArray[23]);
                    htmlString = htmlString.replace("MerchantCategoryCode",receiveDataArray[21]);

                }
                return htmlString;
            case PURCHASE_CASHBACK:
                is = getResources().getAssets().open("printReceipt/Purchase_Cashback(customer copy)).html");
                htmlString = getHtmlString(is);
                if(receiveDataArray.length >= 29) {
                    htmlString = htmlString.replace("CurrentAmount",receiveDataArray[5]);
                    htmlString = htmlString.replace("currentDate",receiveDataArray[9].substring(0, 6));
                    htmlString = htmlString.replace("currentTime",receiveDataArray[9].substring(6));
                    htmlString = htmlString.replace("panNumber",receiveDataArray[4]);
                    htmlString = htmlString.replace("CashBackAmount",receiveDataArray[6]);
                    int amount = Integer.parseInt(receiveDataArray[5]);
                    int cashBack = Integer.parseInt(receiveDataArray[6]);
                    int totalAmt = amount + cashBack;
                    htmlString = htmlString.replace("TotalAmount",String.valueOf(totalAmt));
                    htmlString = htmlString.replace("ExpiryDate",receiveDataArray[10]);
                    htmlString = htmlString.replace("authCode",receiveDataArray[12]);
                    htmlString = htmlString.replace("approved",receiveDataArray[3]);

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
                if(receiveDataArray.length >= 26) {

                    htmlString = htmlString.replace("currentDate",receiveDataArray[7].substring(0, 6));
                    htmlString = htmlString.replace("currentTime",receiveDataArray[7].substring(6));
                    htmlString = htmlString.replace("panNumber",receiveDataArray[4]);
                    htmlString = htmlString.replace("authCode", receiveDataArray[10]);
                    htmlString = htmlString.replace("approved", receiveDataArray[3]);
                    htmlString = htmlString.replace("CurrentAmount",receiveDataArray[5]);
                    htmlString = htmlString.replace("ExpiryDate",receiveDataArray[8]);
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
                if(receiveDataArray.length >= 26) {

                    htmlString = htmlString.replace("currentTime",receiveDataArray[7].substring(6));
                    htmlString = htmlString.replace("currentDate",receiveDataArray[7].substring(0, 6));
                    htmlString = htmlString.replace("panNumber",receiveDataArray[4]);
                    htmlString = htmlString.replace("authCode", receiveDataArray[10]);
                    htmlString = htmlString.replace("approved", receiveDataArray[3]);
                    htmlString = htmlString.replace("CurrentAmount",receiveDataArray[5]);
                    htmlString = htmlString.replace("ExpiryDate",receiveDataArray[8]);
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
                if(receiveDataArray.length >= 27)

                htmlString = htmlString.replace("currentTime",receiveDataArray[7].substring(6));
                htmlString = htmlString.replace("currentDate",receiveDataArray[7].substring(0, 6));
                htmlString = htmlString.replace("panNumber",receiveDataArray[4]);
                htmlString = htmlString.replace("authCode",  receiveDataArray[10]);
                htmlString = htmlString.replace("approved",  receiveDataArray[3]);
                htmlString = htmlString.replace("CurrentAmount",receiveDataArray[5]);
                htmlString = htmlString.replace("ExpiryDate",receiveDataArray[8]);
                htmlString = htmlString.replace("CONTACTLESS",  receiveDataArray[20]);
                htmlString = htmlString.replace("ResponseCode",  receiveDataArray[2]);
                htmlString = htmlString.replace("AID",  receiveDataArray[14]);
                htmlString = htmlString.replace("TVR",  receiveDataArray[18]);
                htmlString = htmlString.replace("CVR",  receiveDataArray[17]);
                htmlString = htmlString.replace("applicationCryptogram",  receiveDataArray[15]);
                htmlString = htmlString.replace("CID",  receiveDataArray[16]);
                htmlString = htmlString.replace("MID",  receiveDataArray[12]);
                htmlString = htmlString.replace("TID",  receiveDataArray[11]);
                htmlString = htmlString.replace("RRN",  receiveDataArray[9]);
                htmlString = htmlString.replace("StanNo",  receiveDataArray[6]);
                htmlString = htmlString.replace("ApplicationVersion",  receiveDataArray[26]);
                htmlString = htmlString.replace("SchemeLabel",  receiveDataArray[23]);
                htmlString = htmlString.replace("MerchantCategoryCode",  receiveDataArray[21]);

                return htmlString;
            case PRE_AUTH_EXTENSION:
                is = getResources().getAssets().open("printReceipt/Pre-Extension(Customer_copy).html");
                htmlString = getHtmlString(is);

                htmlString = htmlString.replace("currentTime",receiveDataArray[7].substring(6));
                htmlString = htmlString.replace("currentDate",receiveDataArray[7].substring(0, 6));
                htmlString = htmlString.replace("panNumber",receiveDataArray[4]);
                htmlString = htmlString.replace("authCode",  receiveDataArray[10]);
                htmlString = htmlString.replace("approved",  receiveDataArray[3]);
                htmlString = htmlString.replace("CurrentAmount",receiveDataArray[5]);
                htmlString = htmlString.replace("ExpiryDate",receiveDataArray[8]);
                htmlString = htmlString.replace("CONTACTLESS",  receiveDataArray[20]);
                htmlString = htmlString.replace("ResponseCode",  receiveDataArray[2]);
                htmlString = htmlString.replace("AID",  receiveDataArray[14]);
                htmlString = htmlString.replace("TVR",  receiveDataArray[18]);
                htmlString = htmlString.replace("CVR",  receiveDataArray[17]);
                htmlString = htmlString.replace("applicationCryptogram",  receiveDataArray[15]);
                htmlString = htmlString.replace("CID",  receiveDataArray[16]);
                htmlString = htmlString.replace("MID",  receiveDataArray[12]);
                htmlString = htmlString.replace("TID",  receiveDataArray[11]);
                htmlString = htmlString.replace("RRN",  receiveDataArray[9]);
                htmlString = htmlString.replace("StanNo",  receiveDataArray[6]);
                htmlString = htmlString.replace("ApplicationVersion",  receiveDataArray[26]);
                htmlString = htmlString.replace("SchemeLabel",  receiveDataArray[23]);
                htmlString = htmlString.replace("MerchantCategoryCode",  receiveDataArray[21]);

                return htmlString;
            case PRE_AUTH_VOID:
                is = getResources().getAssets().open("printReceipt/Pre-void(Customer_copy).html");
                htmlString = getHtmlString(is);

                htmlString = htmlString.replace("currentTime",receiveDataArray[7].substring(6));
                htmlString = htmlString.replace("currentDate",receiveDataArray[7].substring(0, 6));
                htmlString = htmlString.replace("panNumber",receiveDataArray[4]);
                htmlString = htmlString.replace("authCode", receiveDataArray[10]);
                htmlString = htmlString.replace("approved", receiveDataArray[3]);
                htmlString = htmlString.replace("CurrentAmount",receiveDataArray[5]);
                htmlString = htmlString.replace("ExpiryDate",receiveDataArray[8]);
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
                return htmlString;
            case CASH_ADVANCE:
                is = getResources().getAssets().open("printReceipt/Cash_Advance(Customer_copy).html");
                htmlString = getHtmlString(is);

                htmlString = htmlString.replace("currentDate",receiveDataArray[7].substring(0, 6));
                htmlString = htmlString.replace("currentTime",receiveDataArray[7].substring(6));
                htmlString = htmlString.replace("panNumber",receiveDataArray[4]);
                htmlString = htmlString.replace("authCode", receiveDataArray[10]);
                htmlString = htmlString.replace("approved", receiveDataArray[3]);
                htmlString = htmlString.replace("CurrentAmount",receiveDataArray[5]);
                htmlString = htmlString.replace("ExpiryDate",receiveDataArray[8]);
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
                return htmlString;
            case REVERSAL:
                is = getResources().getAssets().open("printReceipt/Reversal(Customer_copy).html");
                htmlString = getHtmlString(is);

                htmlString = htmlString.replace("currentDate",receiveDataArray[7].substring(0, 6));
                htmlString = htmlString.replace("currentTime",receiveDataArray[7].substring(6));
                htmlString = htmlString.replace("panNumber",receiveDataArray[4]);
                htmlString = htmlString.replace("authCode", receiveDataArray[10]);
                htmlString = htmlString.replace("approved", receiveDataArray[3]);
                htmlString = htmlString.replace("CurrentAmount",receiveDataArray[5]);
                htmlString = htmlString.replace("ExpiryDate",receiveDataArray[8]);
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

                return htmlString;
            case RECONCILIATION:
                is = getResources().getAssets().open("printReceipt/Reconcilation.html");
                htmlString = getHtmlString(is);

                htmlString = htmlString.replace("currentDate",receiveDataArray[4].substring(0, 6));
                htmlString = htmlString.replace("currentTime",receiveDataArray[4].substring(6));
               /* htmlString = htmlString.replace("totalDBCount", (Convert.ToDecimal( receiveDataArray[7]) / 100).ToString());
                htmlString = htmlString.replace("totalDBAmount", (Convert.ToDecimal( receiveDataArray[8]) / 100).ToString());
                htmlString = htmlString.replace("totalCBCount", (Convert.ToDecimal( receiveDataArray[9]) / 100).ToString());
                htmlString = htmlString.replace("totalCBAmount", (Convert.ToDecimal( receiveDataArray[10]) / 100).ToString());
                htmlString = htmlString.replace("NAQDCount", (Convert.ToDecimal( receiveDataArray[11]) / 100).ToString());
                htmlString = htmlString.replace("NAQDAmount", (Convert.ToDecimal( receiveDataArray[12]) / 100).ToString());
                htmlString = htmlString.replace("CADVCount", (Convert.ToDecimal( receiveDataArray[13]) / 100).ToString());
                htmlString = htmlString.replace("CADVAmount", (Convert.ToDecimal( receiveDataArray[14]) / 100).ToString());
                htmlString = htmlString.replace("AUTHCount", (Convert.ToDecimal( receiveDataArray[15]) / 100).ToString());
                htmlString = htmlString.replace("AUTHAmount", (Convert.ToDecimal( receiveDataArray[16]) / 100).ToString());*/
                htmlString = htmlString.replace("maestroTransactions",  receiveDataArray[20]);
                htmlString = htmlString.replace("americanTransactions",  receiveDataArray[22]);
                htmlString = htmlString.replace("masterTransactions",  receiveDataArray[24]);
                htmlString = htmlString.replace("visaTransactions",  receiveDataArray[26]);

               /* decimal TOTALSCount = (Convert.ToDecimal( receiveDataArray[7]) + Convert.ToDecimal( receiveDataArray[9]) + Convert.ToDecimal( receiveDataArray[11]) + Convert.ToDecimal( receiveDataArray[13]) + Convert.ToDecimal( receiveDataArray[15])) / 100;
                decimal TOTALSAmount = (Convert.ToDecimal( receiveDataArray[8]) + Convert.ToDecimal( receiveDataArray[10]) + Convert.ToDecimal( receiveDataArray[12]) + Convert.ToDecimal( receiveDataArray[14]) + Convert.ToDecimal( receiveDataArray[16])) / 100;
                htmlString = htmlString.replace("TOTALSCount", (string.Format("{0:0.00##}", TOTALSCount)));
                htmlString = htmlString.replace("TOTALSAmount", (string.Format("{0:0.00##}", TOTALSAmount)));
*/
                return htmlString;
            case BILL_PAYMENT:
                is = getResources().getAssets().open("printReceipt/Bill_Payment(Customer_copy).html");
                htmlString = getHtmlString(is);

                htmlString = htmlString.replace("currentDate",receiveDataArray[7].substring(0, 6));
                htmlString = htmlString.replace("currentTime",receiveDataArray[7].substring(6));
                htmlString = htmlString.replace("panNumber",receiveDataArray[4]);
                htmlString = htmlString.replace("authCode",  receiveDataArray[10]);
                htmlString = htmlString.replace("approved",  receiveDataArray[3]);
                htmlString = htmlString.replace("CurrentAmount",receiveDataArray[5]);
                htmlString = htmlString.replace("ExpiryDate",receiveDataArray[8]);
                htmlString = htmlString.replace("CONTACTLESS",  receiveDataArray[20]);
                htmlString = htmlString.replace("ResponseCode",  receiveDataArray[2]);
                htmlString = htmlString.replace("AID",  receiveDataArray[14]);
                htmlString = htmlString.replace("TVR",  receiveDataArray[18]);
                htmlString = htmlString.replace("CVR",  receiveDataArray[17]);
                htmlString = htmlString.replace("applicationCryptogram",  receiveDataArray[15]);
                htmlString = htmlString.replace("CID",  receiveDataArray[16]);
                htmlString = htmlString.replace("MID",  receiveDataArray[12]);
                htmlString = htmlString.replace("TID",  receiveDataArray[11]);
                htmlString = htmlString.replace("RRN",  receiveDataArray[9]);
                htmlString = htmlString.replace("StanNo",  receiveDataArray[6]);
                htmlString = htmlString.replace("ApplicationVersion",  receiveDataArray[26]);
                htmlString = htmlString.replace("SchemeLabel",  receiveDataArray[23]);
                htmlString = htmlString.replace("MerchantCategoryCode", receiveDataArray[21]);

                return htmlString;
            case PARAMETER_DOWNLOAD:
                is = getResources().getAssets().open("printReceipt/Parameter_download.html");
                htmlString = getHtmlString(is);

                htmlString = htmlString.replace("currentDate",receiveDataArray[4].substring(0, 6));
                htmlString = htmlString.replace("currentTime",receiveDataArray[4].substring(6));
                htmlString = htmlString.replace("responseCode", receiveDataArray[3]);

                return htmlString;
            case PRINT_DETAIL_REPORT:
                is = getResources().getAssets().open("printReceipt/Detail_Report.html");
                htmlString = getHtmlString(is);

                htmlString = htmlString.replace("currentDate",receiveDataArray[4].substring(0, 6));
                htmlString = htmlString.replace("currentTime",receiveDataArray[4].substring(6));
               /* htmlString = htmlString.replace("totalDBCount", (Convert.ToDecimal(receiveDataArray[7]) / 100).ToString());
                htmlString = htmlString.replace("totalDBAmount", (Convert.ToDecimal(receiveDataArray[8]) / 100).ToString());
                htmlString = htmlString.replace("totalCBCount", (Convert.ToDecimal(receiveDataArray[9]) / 100).ToString());
                htmlString = htmlString.replace("totalCBAmount", (Convert.ToDecimal(receiveDataArray[10]) / 100).ToString());
                htmlString = htmlString.replace("NAQDCount", (Convert.ToDecimal(receiveDataArray[11]) / 100).ToString());
                htmlString = htmlString.replace("NAQDAmount", (Convert.ToDecimal(receiveDataArray[12]) / 100).ToString());
                htmlString = htmlString.replace("CADVCount", (Convert.ToDecimal(receiveDataArray[13]) / 100).ToString());
                htmlString = htmlString.replace("CADVAmount", (Convert.ToDecimal(receiveDataArray[14]) / 100).ToString());
                htmlString = htmlString.replace("AUTHCount", (Convert.ToDecimal(receiveDataArray[15]) / 100).ToString());
                htmlString = htmlString.replace("AUTHAmount", (Convert.ToDecimal(receiveDataArray[16]) / 100).ToString());*/
                htmlString = htmlString.replace("maestroTransactions", receiveDataArray[20]);
                htmlString = htmlString.replace("americanTransactions", receiveDataArray[22]);
                htmlString = htmlString.replace("masterTransactions", receiveDataArray[24]);
                htmlString = htmlString.replace("visaTransactions", receiveDataArray[26]);

               /* decimal TOTALSCount = (Convert.ToDecimal(receiveDataArray[7]) + Convert.ToDecimal(receiveDataArray[9]) + Convert.ToDecimal(receiveDataArray[11]) + Convert.ToDecimal(receiveDataArray[13]) + Convert.ToDecimal(receiveDataArray[15])) / 100;
                decimal TOTALSAmount = (Convert.ToDecimal(receiveDataArray[8]) + Convert.ToDecimal(receiveDataArray[10]) + Convert.ToDecimal(receiveDataArray[12]) + Convert.ToDecimal(receiveDataArray[14]) + Convert.ToDecimal(receiveDataArray[16])) / 100;

                htmlString = htmlString.replace("TOTALSCount", (string.Format("{0:0.00##}", TOTALSCount)));
                htmlString = htmlString.replace("TOTALSAmount", (string.Format("{0:0.00##}", TOTALSAmount)));*/

                return htmlString;
            case PRINT_SUMMARY_REPORT:
                is = getResources().getAssets().open("printReceipt/Summary_Report.html");
                htmlString = getHtmlString(is);
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
        logger.debug(getClass() + "LoadedHtml>>"+ str);
        return str;
    }
}

