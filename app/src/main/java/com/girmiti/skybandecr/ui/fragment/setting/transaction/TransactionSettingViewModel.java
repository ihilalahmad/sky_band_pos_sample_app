package com.girmiti.skybandecr.ui.fragment.setting.transaction;

import androidx.lifecycle.ViewModel;

import com.girmiti.skybandecr.cache.GeneralParamCache;
import com.girmiti.skybandecr.constant.Constant;
import com.girmiti.skybandecr.databinding.TransactionSettingFragmentBinding;

public class TransactionSettingViewModel extends ViewModel implements Constant {

    public static int getPrint() {
        return GeneralParamCache.getInstance().getInt(ENABLE_PRINT);
    }

    public static int getEcr() {
        return GeneralParamCache.getInstance().getInt(ENABLE_ECR);
    }

    public void setData(TransactionSettingFragmentBinding transactionSettingFragmentBinding) {

        if (transactionSettingFragmentBinding.ecrNo.isChecked()) {
            GeneralParamCache.getInstance().putInt(ENABLE_ECR, 1);
        } else {
            GeneralParamCache.getInstance().putInt(ENABLE_ECR, 0);
        }

        if (transactionSettingFragmentBinding.terminalPrinter.isChecked()) {
            GeneralParamCache.getInstance().putInt(ENABLE_PRINT, 1);
        } else {
            GeneralParamCache.getInstance().putInt(ENABLE_PRINT, 0);
        }

    }
}
