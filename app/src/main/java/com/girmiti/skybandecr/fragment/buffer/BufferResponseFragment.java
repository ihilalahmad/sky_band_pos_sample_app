package com.girmiti.skybandecr.fragment.buffer;

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
import com.girmiti.skybandecr.fragment.home.HomeViewModel;
import com.girmiti.skybandecr.sdk.logger.Logger;

public class BufferResponseFragment extends Fragment {

    private BufferResponseViewModel bufferResponseViewModel;
    private NavController navController;
    private BufferResponseFragmentBinding bufferResponseFragmentBinding;
    private Logger logger = Logger.getNewLogger(BufferResponseFragment.class.getName());

    private String receiveData = "";
    String parseData = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        bufferResponseViewModel = ViewModelProviders.of(this).get(BufferResponseViewModel.class);
        bufferResponseFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.buffer_response_fragment, container, false);

        setupListeners();

        return bufferResponseFragmentBinding.getRoot();
    }

    private void setupListeners() {

        bufferResponseFragmentBinding.bufferSend.setText(HomeViewModel.getReqData());
        setResponse();
        bufferResponseFragmentBinding.bufferReceive.setText(receiveData);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        bufferResponseFragmentBinding.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();
                navController.navigate(R.id.action_bufferResponseFragment_to_homeFragment, null, options);
            }
        });
    }

    private void setResponse() {

        if (HomeViewModel.getTransactionType() == 0) {
            receiveData = bufferResponseViewModel.purchase(HomeViewModel.getSplittedArray());
        } else if (HomeViewModel.getTransactionType() == 1) {
            receiveData = bufferResponseViewModel.cashback(HomeViewModel.getSplittedArray());
        } else if (HomeViewModel.getTransactionType() == 2) {
            receiveData = bufferResponseViewModel.refund(HomeViewModel.getSplittedArray());
        } else if (HomeViewModel.getTransactionType() == 3) {
            receiveData = bufferResponseViewModel.preAuth(HomeViewModel.getSplittedArray());
        } else if (HomeViewModel.getTransactionType() == 4) {
            receiveData = bufferResponseViewModel.preAuthAdvice(HomeViewModel.getSplittedArray());
        } else if (HomeViewModel.getTransactionType() == 5) {
            receiveData = bufferResponseViewModel.preAuthExtn(HomeViewModel.getSplittedArray());
        } else if (HomeViewModel.getTransactionType() == 6) {
            receiveData = bufferResponseViewModel.preAuthVoid(HomeViewModel.getSplittedArray());
        } else if (HomeViewModel.getTransactionType() == 8) {
            receiveData = bufferResponseViewModel.cashback(HomeViewModel.getSplittedArray());
        } else if (HomeViewModel.getTransactionType() == 9) {
            receiveData = bufferResponseViewModel.reversal(HomeViewModel.getSplittedArray());
        } else if (HomeViewModel.getTransactionType() == 10) {
            receiveData = bufferResponseViewModel.reconciliation(HomeViewModel.getSplittedArray());
        } else if (HomeViewModel.getTransactionType() == 11) {
            receiveData = bufferResponseViewModel.parameterDownload(HomeViewModel.getSplittedArray());
        } else if (HomeViewModel.getTransactionType() == 12) {
            receiveData = bufferResponseViewModel.setParameter(HomeViewModel.getSplittedArray());
        } else if (HomeViewModel.getTransactionType() == 13) {
            receiveData = bufferResponseViewModel.getParameter(HomeViewModel.getSplittedArray());
        } else if (HomeViewModel.getTransactionType() == 20) {
            receiveData = bufferResponseViewModel.billPayment(HomeViewModel.getSplittedArray());
        } else {
            for (String s : HomeViewModel.getSplittedArray()) {
                parseData = parseData + "\n" + s;
            }
            receiveData = parseData;
        }
    }
}
