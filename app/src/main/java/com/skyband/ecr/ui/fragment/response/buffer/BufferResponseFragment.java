package com.skyband.ecr.ui.fragment.response.buffer;

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

import com.skyband.ecr.R;
import com.skyband.ecr.databinding.BufferResponseFragmentBinding;
import com.skyband.ecr.model.ActiveTxnData;
import com.skyband.ecr.sdk.logger.Logger;
import com.skyband.ecr.transaction.TransactionType;

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
        ActiveTxnData.getInstance().setPosition(0);
        bufferResponseFragmentBinding.bufferSend.setText(ActiveTxnData.getInstance().getReqData());
        receiveData = ActiveTxnData.getInstance().getResData();
        String[] receiveDataArray = receiveData.split(";");
        logger.debug(getClass() + "::" + "GetRespData>>> " + receiveData);
        String bufferData;
        if (ActiveTxnData.getInstance().getTransactionType() != TransactionType.PRINT_SUMMARY_REPORT) {
            bufferData = setResponse(receiveDataArray);
        } else {
            bufferData = bufferResponseViewModel.printResponseSummaryReport(ActiveTxnData.getInstance().getSummaryReportArray());
        }
        String encodedHtml = Base64.encodeToString(bufferData.getBytes(), Base64.NO_PADDING);
        bufferResponseFragmentBinding.bufferReceive.loadData(encodedHtml, "text/html", "base64");
        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);

        if (ActiveTxnData.getInstance().getTransactionType() != TransactionType.PRINT_SUMMARY_REPORT) {
            switch (receiveDataArray[1]) {
                case "17":
                case "18":
                case "19":
                case "15":
                case "16":
                case "12":
                case "13":
                case "30":
                case "40":
                case "24":
                    bufferResponseFragmentBinding.printReceipt.setVisibility(View.GONE);
                    break;
                case "10":
                    if (Integer.parseInt(receiveDataArray[2]) == 500 || Integer.parseInt(receiveDataArray[2]) == 501) {
                        bufferResponseFragmentBinding.printReceipt.setVisibility(View.VISIBLE);
                    } else {
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
                case "23":
                    if (receiveDataArray.length > 11 && !receiveDataArray[5].equals("22")) {
                        bufferResponseFragmentBinding.printReceipt.setVisibility(View.VISIBLE);
                    } else {
                        bufferResponseFragmentBinding.printReceipt.setVisibility(View.GONE);
                    }
                    break;
                default:
                    if (receiveDataArray[3].equals("APPROVED") || receiveDataArray[3].equals("DECLINED")) {
                        bufferResponseFragmentBinding.printReceipt.setVisibility(View.VISIBLE);
                    } else {
                        bufferResponseFragmentBinding.printReceipt.setVisibility(View.GONE);
                    }
            }
        } else {
            bufferResponseFragmentBinding.printReceipt.setVisibility(View.VISIBLE);
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

            case "5":
            case "6":
            case "20":
                if (receiveDataArray.length > 27) {
                    return bufferResponseViewModel.printResponsePurchase(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }
            case "1":
                if (receiveDataArray.length > 29) {
                    return bufferResponseViewModel.printResponsePurchaseCashBack(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }
            case "9":
                if (receiveDataArray.length > 26) {
                    return bufferResponseViewModel.printResponseReversal(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }
            case "10":
                if (receiveDataArray.length > 18) {
                    return bufferResponseViewModel.printResponseReconcilation(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }

            case "11":
            case "12":
                if (receiveDataArray.length > 4) {
                    return bufferResponseViewModel.printResponseParameterDownload(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }

            case "13":
                if (receiveDataArray.length > 9) {
                    return bufferResponseViewModel.printResponseGetSetting(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }

            case "17":
                if (receiveDataArray.length > 2) {
                    return bufferResponseViewModel.printResponseRegister(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
                }

            case "18":
            case "19":
                return bufferResponseViewModel.printResponseStartSession(receiveDataArray);
            case "21":

                if (Integer.parseInt(receiveDataArray[2]) == 0) {
                    return bufferResponseViewModel.printResponseRunningTotal(receiveDataArray);
                } else {
                    return bufferResponseViewModel.printResponseRunningTotalDefault(receiveDataArray);
                }

            case "22":
                return bufferResponseViewModel.printResponseSummaryReport(receiveDataArray);

            case "23":
                if (receiveDataArray.length > 11) {
                    String[] separateResponse = new String[receiveData.length()];
                    int j = 1;
                    for (int i = 5; i < receiveDataArray.length - 3; i++) {
                        separateResponse[j] = receiveDataArray[i];
                        j = j + 1;
                    }
                    String[] a = changeToTransactionType(separateResponse);
                    ActiveTxnData.getInstance().setReplacedArray(a);
                    return setResponse(a);
                } else {
                    return bufferResponseViewModel.printResponseRepeat(receiveDataArray);
                }

            case "24":
                if (receiveDataArray.length > 5)
                    return bufferResponseViewModel.printResponseCheckStatus(receiveDataArray);
                else
                    return bufferResponseViewModel.printResponseDefault(receiveDataArray);
            default:
                return bufferResponseViewModel.printResponseOtherTransaction(receiveDataArray);
        }
    }

    private String[] changeToTransactionType(String[] terminalResponse) {

        switch (terminalResponse[1]) {
            case "A0":
                terminalResponse[1] = "17";
                break;
            case "B6":
                terminalResponse[1] = "18";
                break;
            case "B7":
                terminalResponse[1] = "19";
                break;
            case "A1":
                terminalResponse[1] = "0";
                break;
            case "A2":
                terminalResponse[1] = "1";
                break;
            case "A3":
                terminalResponse[1] = "8";
                break;
            case "A4":
                terminalResponse[1] = "3";
                break;
            case "A5":
                terminalResponse[1] = "9";
                break;
            case "A6":
                terminalResponse[1] = "2";
                break;
            case "A7":
                terminalResponse[1] = "4";
                break;
            case "A8":
                terminalResponse[1] = "5";
                break;
            case "A9":
                terminalResponse[1] = "6";
                break;
            case "B1":
                terminalResponse[1] = "10";
                break;
            case "B2":
                terminalResponse[1] = "11";
                break;
            case "B3":
                terminalResponse[1] = "12";
                break;
            case "B4":
                terminalResponse[1] = "13";
                break;
            case "B5":
                terminalResponse[1] = "14";
                break;
            case "B8":
                terminalResponse[1] = "20";
                break;
            case "B9":
                terminalResponse[1] = "21";
                break;
            case "C1":
                terminalResponse[1] = "22";
                break;
            case "C2":
                terminalResponse[1] = "23";
                break;
            case "C3":
                terminalResponse[1] = "24";
                break;
            case "D1":
                terminalResponse[1] = "30";
                break;
            default:
                terminalResponse[1] = "40";
                break;
        }
        return terminalResponse;
    }
}
