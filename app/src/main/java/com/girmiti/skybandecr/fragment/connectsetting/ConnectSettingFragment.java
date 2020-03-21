package com.girmiti.skybandecr.fragment.connectsetting;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.girmiti.skybandecr.R;

public class ConnectSettingFragment extends Fragment {

    private ConnectSettingViewModel mViewModel;

    public static ConnectSettingFragment newInstance() {
        return new ConnectSettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.connect_setting_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ConnectSettingViewModel.class);
        // TODO: Use the ViewModel
    }

}
