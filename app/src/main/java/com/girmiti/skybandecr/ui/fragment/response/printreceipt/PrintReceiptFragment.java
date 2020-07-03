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
import java.util.Objects;

import lombok.SneakyThrows;

public class PrintReceiptFragment extends Fragment {

    private PrintReceiptViewModel printReceiptViewModel;
    private PrintReceiptFragmentBinding printReceiptFragmentBinding;
    private Logger logger = Logger.getNewLogger(PrintReceiptFragment.class.getName());
    private String receiveData;
    private NavController navController;

    @SneakyThrows
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        printReceiptFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.print_receipt_fragment, container, false);
        printReceiptViewModel = ViewModelProviders.of(this).get(PrintReceiptViewModel.class);

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
        if(receiveDataArray[1].equals("23") && (receiveDataArray.length > 15)){
            receiveDataArray = ActiveTxnData.getInstance().getReplacedArray();
        }
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

        switch (receiveDataArray[1]) {

            case "0":
                return printReceiptViewModel.printReceiptPurchase(receiveDataArray, Objects.requireNonNull(getContext()));

            case "1":
                return printReceiptViewModel.printReceiptPurchaseCashback(receiveDataArray, Objects.requireNonNull(getContext()));

            case "2":
                return printReceiptViewModel.printReceiptRefund(receiveDataArray, Objects.requireNonNull(getContext()));

            case "3":
                return printReceiptViewModel.printReceiptPreAuthorisation(receiveDataArray, Objects.requireNonNull(getContext()));

            case "4":
                return printReceiptViewModel.printReceiptPreAuthCompletion(receiveDataArray, Objects.requireNonNull(getContext()));

            case "5":
                return printReceiptViewModel.printReceiptPreAuthExtension(receiveDataArray, Objects.requireNonNull(getContext()));

            case "6":
                return printReceiptViewModel.printReceiptPreAuthVoid(receiveDataArray, Objects.requireNonNull(getContext()));

            case "8":
                return printReceiptViewModel.printReceiptCashAdvance(receiveDataArray, Objects.requireNonNull(getContext()));

            case "9":
                return printReceiptViewModel.printReceiptReversal(receiveDataArray, Objects.requireNonNull(getContext()));

            case "10":
                return printReceiptViewModel.printReceiptReconciliation(receiveDataArray, Objects.requireNonNull(getContext()));

            case "20":
                return printReceiptViewModel.printReceiptBillPayment(receiveDataArray,Objects.requireNonNull(getContext()));

            case "11":
                return printReceiptViewModel.printReceiptParameterDownload(receiveDataArray,Objects.requireNonNull(getContext()));

            case "21":
                return printReceiptViewModel.printReceiptPrintDetail(receiveDataArray,Objects.requireNonNull(getContext()));

            case "22":
                return printReceiptViewModel.printReceiptPrintSummary(receiveDataArray,Objects.requireNonNull(getContext()));

            case "12":
            case "14":
            case "17":
            case "23":
            case "18":
            case "19":
            case "13":
            case "15":
            case "16":
            default:
                return null;
        }
    }
}

