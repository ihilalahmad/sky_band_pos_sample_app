package com.girmiti.skybandecr.ui.fragment.response.buffer;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.databinding.BufferResponseFragmentBinding;
import com.girmiti.skybandecr.model.ActiveTxnData;
import com.girmiti.skybandecr.sdk.logger.Logger;
import com.girmiti.skybandecr.ui.fragment.home.HomeFragment;

import java.util.Objects;

import lombok.SneakyThrows;

public class BufferResponseFragment extends Fragment {

    private BufferResponseViewModel bufferResponseViewModel;
    private NavController navController;
    private BufferResponseFragmentBinding bufferResponseFragmentBinding;
    private Logger logger = Logger.getNewLogger(BufferResponseFragment.class.getName());
    private String receiveData;

    @SneakyThrows
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        bufferResponseViewModel = ViewModelProviders.of(this).get(BufferResponseViewModel.class);
        bufferResponseFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.buffer_response_fragment, container, false);
        setupListeners();

        return bufferResponseFragmentBinding.getRoot();
    }

    private void setupListeners() {

        HomeFragment.setPosition(0);
        bufferResponseFragmentBinding.bufferSend.setText(ActiveTxnData.getInstance().getReqData());
        receiveData = ActiveTxnData.getInstance().getResData();
        String[] receiveDataArray = receiveData.split(";");
        logger.debug(getClass() + "::" + "GetRespData>>> " + receiveData);
        String BufferData = setResponse(receiveDataArray);
        bufferResponseFragmentBinding.bufferReceive.setText(BufferData);
        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);

        switch (receiveDataArray[1]) {
            case "17":
            case "18":
            case "19":
            case "15":
            case "16":
            case "12":
            case "13":
            case "23":
            case "30":
            case "40":
                bufferResponseFragmentBinding.printReceipt.setVisibility(View.GONE);
                break;
            case "10":
                logger.debug("inside Reconsiliation print");
                if (Integer.parseInt(receiveDataArray[2]) == 500 || Integer.parseInt(receiveDataArray[2]) == 501) {
                    logger.debug("inside Reconsiliation print1");
                    bufferResponseFragmentBinding.printReceipt.setVisibility(View.VISIBLE);
                } else {
                    logger.debug("inside Reconsiliation print2");
                    bufferResponseFragmentBinding.printReceipt.setVisibility(View.GONE);
                }
                break;
            case "9":
                if (Integer.parseInt(receiveDataArray[2]) != 400) {
                    bufferResponseFragmentBinding.printReceipt.setVisibility(View.GONE);
                } else {
                    bufferResponseFragmentBinding.printReceipt.setVisibility(View.VISIBLE);
                }
                break;
            case "11":
                if (Integer.parseInt(receiveDataArray[2]) == 300 && !receiveDataArray[3].equals("DECLINED")) {
                    bufferResponseFragmentBinding.printReceipt.setVisibility(View.VISIBLE);
                } else {
                    bufferResponseFragmentBinding.printReceipt.setVisibility(View.GONE);
                }
                break;
            case "21":
                if (Integer.parseInt(receiveDataArray[2]) != 0) {
                    bufferResponseFragmentBinding.printReceipt.setVisibility(View.GONE);
                } else {
                    bufferResponseFragmentBinding.printReceipt.setVisibility(View.VISIBLE);
                }
                break;
            case "22":
                bufferResponseFragmentBinding.printReceipt.setVisibility(View.VISIBLE);
                break;
            default:
                if (receiveDataArray[3].equals("APPROVED") || receiveDataArray[3].equals("DECLINED")) {
                    bufferResponseFragmentBinding.printReceipt.setVisibility(View.VISIBLE);
                } else {
                    bufferResponseFragmentBinding.printReceipt.setVisibility(View.GONE);
                }
        }

        bufferResponseFragmentBinding.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();
                navController.navigate(R.id.action_bufferResponseFragment_to_homeFragment, null, options);
            }
        });

        bufferResponseFragmentBinding.printReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.printReceiptFragment, true).build();
                navController.navigate(R.id.action_bufferResponseFragment_to_printReceiptFragment, null, options);
            }
        });
    }

    private String setResponse(String[] receiveDataArray) {

        switch (receiveDataArray[1]) {
            case "0":
            case "2":
            case "3":
            case "4":
            case "8":
            case "9":
                if (receiveData.length() > 27) {
                    return bufferResponseViewModel.printResponsePurchase(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }
            case "1":
                if (receiveData.length() > 29) {
                    return bufferResponseViewModel.printResponsePurchaseCashBack(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }
            case "17":
                if (receiveData.length() > 2) {
                    return bufferResponseViewModel.printResponseRegister(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }
            case "18":
            case "19":
                if (receiveData.length() > 1) {
                    return bufferResponseViewModel.printResponseStartSession(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }
            case "10":
                if (receiveData.length() > 18) {
                    return bufferResponseViewModel.printResponseReconcilation(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }
            case "11":
            case "12":
                if (receiveData.length() > 5) {
                    return bufferResponseViewModel.printResponseParameterDownload(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }
            case "13":
                if (receiveData.length() > 10) {
                    return bufferResponseViewModel.printResponseGetParameter(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }
            case "23":
                if (receiveData.length() > 7) {
                    return bufferResponseViewModel.printResponseCheckStatus(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }
            case "20":
            case "5":
            case "6":
                if (receiveData.length() > 2) {
                    return bufferResponseViewModel.printResponseBillPayment(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }
            case "21":
                return bufferResponseViewModel.printResponsePrintDetailReport(receiveDataArray);
            case "22":
                return bufferResponseViewModel.printResponseSummaryReport(receiveDataArray);

            default:
                return bufferResponseViewModel.printResponseDefault(receiveDataArray);
        }
    }
}
