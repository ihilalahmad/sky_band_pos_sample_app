package com.skyband.ecr.ui.fragment.setting.connnect;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.skyband.ecr.databinding.ConnectSettingFragmentBinding;

public class ConnectSettingViewModel extends ViewModel {

    private ConnectSettingFragmentBinding connectSettingFragmentBinding;

    public void resetVisibilityOfViews(ConnectSettingFragmentBinding connectSettingFragmentBinding) {

        this.connectSettingFragmentBinding = connectSettingFragmentBinding;

        this.connectSettingFragmentBinding.ipAddress.setVisibility(View.INVISIBLE);
        this.connectSettingFragmentBinding.portNo.setVisibility(View.INVISIBLE);

        connectSettingFragmentBinding.ipLabel.setVisibility(View.INVISIBLE);
        connectSettingFragmentBinding.portLabel.setVisibility(View.INVISIBLE);

        connectSettingFragmentBinding.connectButton.setVisibility(View.INVISIBLE);
        connectSettingFragmentBinding.disconnectButton.setVisibility(View.INVISIBLE);
        connectSettingFragmentBinding.btnConnect.setVisibility(View.INVISIBLE);
        connectSettingFragmentBinding.list.setVisibility(View.INVISIBLE);
        connectSettingFragmentBinding.btnAppToAppConnect.setVisibility(View.INVISIBLE);

    }

    public void getVisibilityOfViews(String selectedItem) {

        switch (selectedItem) {
            case "TC/IP":
                connectSettingFragmentBinding.ipAddress.setVisibility(View.VISIBLE);
                connectSettingFragmentBinding.portNo.setVisibility(View.VISIBLE);
                connectSettingFragmentBinding.ipLabel.setVisibility(View.VISIBLE);
                connectSettingFragmentBinding.portNo.setVisibility(View.VISIBLE);
                connectSettingFragmentBinding.connectButton.setVisibility(View.VISIBLE);
                connectSettingFragmentBinding.disconnectButton.setVisibility(View.VISIBLE);
                connectSettingFragmentBinding.ipAddress.setEnabled(true);
                connectSettingFragmentBinding.portNo.setEnabled(true);
                break;
            case "Bluetooth":
                connectSettingFragmentBinding.list.setVisibility(View.VISIBLE);
                connectSettingFragmentBinding.btnConnect.setVisibility(View.VISIBLE);
                break;
            case "AppToApp(Intent)":
                connectSettingFragmentBinding.btnAppToAppConnect.setVisibility(View.VISIBLE);
                break;
            case "AppToApp(LocalHost)":
                connectSettingFragmentBinding.ipAddress.setVisibility(View.VISIBLE);
                connectSettingFragmentBinding.portNo.setVisibility(View.VISIBLE);
                connectSettingFragmentBinding.ipLabel.setVisibility(View.VISIBLE);
                connectSettingFragmentBinding.portNo.setVisibility(View.VISIBLE);
                connectSettingFragmentBinding.connectButton.setVisibility(View.VISIBLE);
                connectSettingFragmentBinding.disconnectButton.setVisibility(View.VISIBLE);
                connectSettingFragmentBinding.ipAddress.setEnabled(false);
                connectSettingFragmentBinding.portNo.setEnabled(true);
            default:
                //For other connection Type
                break;

        }

    }
}
