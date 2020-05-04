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
        String replacedHtmlString = getPrintReceipt(receiveDataArray);
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
                return htmlString.replace("purchase amount",receiveDataArray[5]);
            case PURCHASE_CASHBACK:
                is = getResources().getAssets().open("printReceipt/Purchase cashback(customer copy)).html");
                htmlString = getHtmlString(is);
                return htmlString;
            case REFUND:
                is = getResources().getAssets().open("printReceipt/Refund(customer_copy).html");
                htmlString = getHtmlString(is);
                return htmlString;
            case PRE_AUTHORISATION:
                is = getResources().getAssets().open("printReceipt/Pre-Auth(Customer_copy).html");
                htmlString = getHtmlString(is);
                return htmlString;
            case PRE_AUTH_EXTENSION:
                is = getResources().getAssets().open("printReceipt/Pre-Extension(Customer_copy).html");
                htmlString = getHtmlString(is);
                return htmlString;
            case PRE_AUTH_VOID:
                is = getResources().getAssets().open("printReceipt/Pre-void(Customer_copy).html");
                htmlString = getHtmlString(is);
                return htmlString;
            case CASH_ADVANCE:
                is = getResources().getAssets().open("printReceipt/Cash_Advance(Customer_copy).html");
                htmlString = getHtmlString(is);
                return htmlString;
            case REVERSAL:
                is = getResources().getAssets().open("printReceipt/Reversal(Customer_copy).html");
                htmlString = getHtmlString(is);
                return htmlString;
            case RECONCILIATION:
                is = getResources().getAssets().open("printReceipt/Reconcilation.html");
                htmlString = getHtmlString(is);
                return htmlString;
            case BILL_PAYMENT:
                is = getResources().getAssets().open("printReceipt/Bill Pyment(Customer_copy).html");
                htmlString = getHtmlString(is);
                return htmlString;
            case PARAMETER_DOWNLOAD:
                is = getResources().getAssets().open("printReceipt/Parameter download.html");
                htmlString = getHtmlString(is);
                return htmlString;
            case PRINT_DETAIL_REPORT:
                is = getResources().getAssets().open("printReceipt/Detail_Report.html");
                htmlString = getHtmlString(is);
                return htmlString;
            case PRINT_SUMMARY_REPORT:
                is = getResources().getAssets().open("printReceipt/Summary_Report.html");
                htmlString = getHtmlString(is);
                return htmlString;
            case PRE_AUTH_COMPLETION:
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

