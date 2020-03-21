package com.girmiti.skybandecr.fragment.transactionsetting;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.girmiti.skybandecr.R;

public class TransactionSettingFragment extends Fragment {

    private TransactionSettingViewModel mViewModel;

    public static TransactionSettingFragment newInstance() {
        return new TransactionSettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.transaction_setting_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TransactionSettingViewModel.class);
        // TODO: Use the ViewModel
    }

}
