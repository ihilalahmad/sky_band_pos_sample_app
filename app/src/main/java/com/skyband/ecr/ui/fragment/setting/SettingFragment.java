package com.skyband.ecr.ui.fragment.setting;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skyband.ecr.R;
import com.skyband.ecr.databinding.SettingFragmentBinding;

public class SettingFragment extends Fragment {

    private SettingFragmentBinding settingFragmentBinding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        settingFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.setting_fragment, container, false);

        getActivity().findViewById(R.id.home_logo).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.left).setVisibility(View.VISIBLE);

        setupListeners();

        return settingFragmentBinding.getRoot();
    }

    private void setupListeners() {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        settingFragmentBinding.connectionSetting.setOnClickListener(v -> navController.navigate(R.id.action_settingFragment_to_navigation_connect_setting));

        settingFragmentBinding.ecrTransactionSetting.setOnClickListener(v -> navController.navigate(R.id.action_settingFragment_to_transactionSettingFragment));
    }
}

