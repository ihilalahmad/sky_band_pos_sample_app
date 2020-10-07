package com.skyband.ecr.ui.fragment.setting.transaction;

import androidx.lifecycle.ViewModel;

import com.skyband.ecr.cache.GeneralParamCache;
import com.skyband.ecr.constant.Constant;
import com.skyband.ecr.databinding.TransactionSettingFragmentBinding;

public class TransactionSettingViewModel extends ViewModel {
    public static int getPrint() {
        return GeneralParamCache.getInstance().getInt(Constant.ENABLE_PRINT);
    }

    public static int getEcr() {
        return GeneralParamCache.getInstance().getInt(Constant.ENABLE_ECR);
    }

    public void setData(TransactionSettingFragmentBinding transactionSettingFragmentBinding) {

        if (transactionSettingFragmentBinding.ecrNo.isChecked()) {
            GeneralParamCache.getInstance().putInt(Constant.ENABLE_ECR, 1);
        } else {
            GeneralParamCache.getInstance().putInt(Constant.ENABLE_ECR, 0);
        }

        if (transactionSettingFragmentBinding.terminalPrinter.isChecked()) {
            GeneralParamCache.getInstance().putInt(Constant.ENABLE_PRINT, 1);
        } else {
            GeneralParamCache.getInstance().putInt(Constant.ENABLE_PRINT, 0);
        }

    }
}
