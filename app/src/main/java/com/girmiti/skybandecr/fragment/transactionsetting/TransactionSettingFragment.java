package com.girmiti.skybandecr.fragment.transactionsetting;

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
import android.widget.Toast;

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.databinding.TransactionSettingFragmentBinding;
import com.girmiti.skybandecr.sdk.logger.Logger;

import lombok.Getter;

public class TransactionSettingFragment extends Fragment {

    private TransactionSettingViewModel transactionSettingViewModel;
    private TransactionSettingFragmentBinding transactionSettingFragmentBinding;
    private NavController navController;
    @Getter
    private static String cashRegisterNO ="";
    private Logger logger = Logger.getNewLogger(TransactionSettingFragment.class.getName());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        transactionSettingViewModel = ViewModelProviders.of(this).get(TransactionSettingViewModel.class);
        transactionSettingFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.transaction_setting_fragment,container,false);

        setupListeners();

        return transactionSettingFragmentBinding.getRoot();
    }

    private void setupListeners() {

        if( TransactionSettingViewModel.getEcr() == 1) {
            transactionSettingFragmentBinding.ecrRefNo.setChecked(true);
        } else {
            transactionSettingFragmentBinding.ecrRefNo.setChecked(false);
        }

        if( TransactionSettingViewModel.getPrint() == 1) {
            transactionSettingFragmentBinding.terminalPrinter.setChecked(true);
        } else {
            transactionSettingFragmentBinding.terminalPrinter.setChecked(false);
        }

        if(!cashRegisterNO.equals("")) {
            transactionSettingFragmentBinding.cashRegisterNo.setText(cashRegisterNO);
        }

        navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        final NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();

        transactionSettingFragmentBinding.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                transactionSettingViewModel.setData(transactionSettingFragmentBinding);
                 boolean isvalid =  transactionSettingViewModel.validCashRegisterno(transactionSettingFragmentBinding);

                 if(isvalid) {
                     cashRegisterNO = transactionSettingFragmentBinding.cashRegisterNo.getText().toString();
                     navController.navigate(R.id.action_transactionSettingFragment_to_homeFragment,null,options);
                 }
                 else {
                     cashRegisterNO="";
                     Toast.makeText(getActivity(), R.string.invalid_input, Toast.LENGTH_LONG).show();
                 }

            }
        });
    }
}
