package com.girmiti.skybandecr.ui.fragment.setting.transaction;

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
import com.girmiti.skybandecr.cache.GeneralParamCache;
import com.girmiti.skybandecr.constant.Constant;
import com.girmiti.skybandecr.databinding.TransactionSettingFragmentBinding;
import com.girmiti.skybandecr.model.ActiveTxnData;
import com.girmiti.skybandecr.sdk.logger.Logger;

import java.util.Objects;

public class TransactionSettingFragment extends Fragment implements Constant {

    private TransactionSettingViewModel transactionSettingViewModel;
    private TransactionSettingFragmentBinding transactionSettingFragmentBinding;
    private NavController navController;

    private Logger logger = Logger.getNewLogger(TransactionSettingFragment.class.getName());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        transactionSettingViewModel = ViewModelProviders.of(this).get(TransactionSettingViewModel.class);
        transactionSettingFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.transaction_setting_fragment, container, false);

        setupListeners();

        return transactionSettingFragmentBinding.getRoot();
    }

    private void setupListeners() {

        if(ActiveTxnData.getInstance().isRegistered()) {
            transactionSettingFragmentBinding.cashRegisterNo2.setText(GeneralParamCache.getInstance().getString(CASH_REGISTER_NO));
        }

        if (TransactionSettingViewModel.getEcr() == 1) {
            transactionSettingFragmentBinding.ecrNo.setChecked(true);
        } else {
            transactionSettingFragmentBinding.ecrNo.setChecked(false);
        }

        if (TransactionSettingViewModel.getPrint() == 1) {
            transactionSettingFragmentBinding.terminalPrinter.setChecked(true);
        } else {
            transactionSettingFragmentBinding.terminalPrinter.setChecked(false);
        }

        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);
        final NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();

        transactionSettingFragmentBinding.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                transactionSettingViewModel.setData(transactionSettingFragmentBinding);
                    navController.navigate(R.id.action_transactionSettingFragment_to_homeFragment, null, options);
            }
        });
    }
}
