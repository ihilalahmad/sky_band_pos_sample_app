package com.girmiti.skybandecr.fragment.transactionsetting;

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
import android.widget.Button;

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.databinding.TransactionSettingFragmentBinding;

public class TransactionSettingFragment extends Fragment {

    private TransactionSettingViewModel mViewModel;
    private TransactionSettingFragmentBinding transactionSettingFragmentBinding;
    private NavController navController;

    private Button okButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        transactionSettingFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.transaction_setting_fragment,container,false);
        setupListeners();
        return transactionSettingFragmentBinding.getRoot();
    }

    private void setupListeners() {

        navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        okButton= transactionSettingFragmentBinding.okButton;
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_transactionSettingFragment_to_homeFragment);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TransactionSettingViewModel.class);
        // TODO: Use the ViewModel
    }

}
