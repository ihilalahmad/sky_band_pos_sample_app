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

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import lombok.SneakyThrows;

public class BufferResponseFragment extends Fragment {

    private BufferResponseViewModel bufferResponseViewModel;
    private NavController navController;
    private BufferResponseFragmentBinding bufferResponseFragmentBinding;
    private Logger logger = Logger.getNewLogger(BufferResponseFragment.class.getName());

    private String receiveData = "";

    @SneakyThrows
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        bufferResponseViewModel = ViewModelProviders.of(this).get(BufferResponseViewModel.class);
        bufferResponseFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.buffer_response_fragment, container, false);

        setupListeners();

        return bufferResponseFragmentBinding.getRoot();
    }

    private void setupListeners() throws IOException {
        HomeFragment.setPosition(0);
        bufferResponseFragmentBinding.bufferSend.setText(ActiveTxnData.getInstance().getReqData());
        receiveData = ActiveTxnData.getInstance().getResData();
        logger.debug(getClass() + "::" + "GetRespData>>> " + receiveData);
        receiveData = receiveData.replace(";", "\n");
        bufferResponseFragmentBinding.bufferReceive.setText(receiveData);
        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);

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
}
