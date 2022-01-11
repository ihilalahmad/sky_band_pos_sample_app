package com.skyband.ecr.ui.fragment.setting.transaction;

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

import com.skyband.ecr.R;
import com.skyband.ecr.constant.Constant;
import com.skyband.ecr.databinding.TransactionSettingFragmentBinding;
import com.skyband.ecr.model.ActiveTxnData;

import java.util.Objects;

public class TransactionSettingFragment extends Fragment {

    private TransactionSettingViewModel transactionSettingViewModel;
    private TransactionSettingFragmentBinding transactionSettingFragmentBinding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        transactionSettingViewModel = ViewModelProviders.of(this).get(TransactionSettingViewModel.class);
        transactionSettingFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.transaction_setting_fragment, container, false);

        setupListeners();

        return transactionSettingFragmentBinding.getRoot();
    }

    private void setupListeners() {

        if (ActiveTxnData.getInstance().getCashRegisterNo() != null) {
            transactionSettingFragmentBinding.cashRegisterNoEt.setText(ActiveTxnData.getInstance().getCashRegisterNo());
        }

        transactionSettingFragmentBinding.ecrNo.setChecked(TransactionSettingViewModel.getEcr() == 1);

        transactionSettingFragmentBinding.terminalPrinter.setChecked(TransactionSettingViewModel.getPrint() == 1);

        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);
        final NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();

        transactionSettingFragmentBinding.okButton.setOnClickListener(v -> {

            transactionSettingViewModel.setData(transactionSettingFragmentBinding);

            if (!(transactionSettingFragmentBinding.cashRegisterNoEt.length() > Constant.ZERO)) {
                Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "Cash Register No. should not be empty", Toast.LENGTH_LONG).show();
            }
            if (!(transactionSettingFragmentBinding.cashRegisterNoEt.length() >= Constant.EIGHT)) {
                Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "Cash Register No. length should be 8 digits", Toast.LENGTH_LONG).show();
            } else {

                if (ActiveTxnData.getInstance().getCashRegisterNo() != null && transactionSettingFragmentBinding.cashRegisterNoEt.getText().toString().equals(ActiveTxnData.getInstance().getCashRegisterNo())) {
                    ActiveTxnData.getInstance().setRegistered(false);
                    ActiveTxnData.getInstance().setSessionStarted(false);
                }

                ActiveTxnData.getInstance().setCashRegisterNo(transactionSettingFragmentBinding.cashRegisterNoEt.getText().toString());
                navController.navigate(R.id.action_transactionSettingFragment_to_homeFragment, null, options);
            }

        });
    }
}
