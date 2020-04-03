package com.girmiti.skybandecr.fragment.transactionsetting;

import androidx.lifecycle.ViewModel;

import com.girmiti.skybandecr.databinding.TransactionSettingFragmentBinding;
import com.girmiti.skybandecr.sdk.logger.Logger;

public class TransactionSettingViewModel extends ViewModel {

    private static int print = 0;
    private static int ecr = 0;
    private Logger logger = Logger.getNewLogger(TransactionSettingViewModel.class.getName());

    public static int getPrint() {
        return print;
    }

    public static int getEcr() {
        return ecr;
    }

    public void setData(TransactionSettingFragmentBinding transactionSettingFragmentBinding) {

        if (transactionSettingFragmentBinding.ecrRefNo.isChecked()) {
            ecr = 1;
        } else
            ecr = 0;

        if(transactionSettingFragmentBinding.terminalPrinter.isChecked()){
            print = 1;
        } else
            print = 0;
    }
}
