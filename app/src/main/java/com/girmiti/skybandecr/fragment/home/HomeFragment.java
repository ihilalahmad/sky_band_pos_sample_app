package com.girmiti.skybandecr.fragment.home;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.databinding.HomeFragmentBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private HomeFragmentBinding homeFragmentBinding;
    protected NavController navController;
    private TextView connectSetting;
    private TextView ecrTransactionSetting;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        homeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        getActivity().findViewById(R.id.home_logo).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.left).setVisibility(View.INVISIBLE);

        setupListeners();

        return homeFragmentBinding.getRoot();
    }

    private void setupListeners() {

        connectSetting = homeFragmentBinding.connectionSetting;
        ecrTransactionSetting = homeFragmentBinding.ecrTransactionSetting;
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        connectSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_navigation_connect_setting);
            }
        });
        ecrTransactionSetting= homeFragmentBinding.ecrTransactionSetting;
        ecrTransactionSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_homeFragment_to_transactionSettingFragment);
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel

    }

}

