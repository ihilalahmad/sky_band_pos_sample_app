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

public class BufferResponseFragment extends Fragment {

    private BufferResponseViewModel bufferResponseViewModel;
    protected NavController navController;
    private BufferResponseFragmentBinding bufferResponseFragmentBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        bufferResponseViewModel = ViewModelProviders.of(this).get(BufferResponseViewModel.class);
        bufferResponseFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.buffer_response_fragment, container, false);

        bufferResponseFragmentBinding.bufferSend.setText(HomeViewModel.getReqData());

        String receiveData = HomeViewModel.getParseData();
        bufferResponseFragmentBinding.bufferReceive.setText(receiveData);
        HomeViewModel.setParseData("");

        setupListeners();

        return bufferResponseFragmentBinding.getRoot();
    }

    private void setupListeners() {

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        bufferResponseFragmentBinding.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();
                navController.navigate(R.id.action_bufferResponseFragment_to_homeFragment, null, options);

            }
        });
    }
}
