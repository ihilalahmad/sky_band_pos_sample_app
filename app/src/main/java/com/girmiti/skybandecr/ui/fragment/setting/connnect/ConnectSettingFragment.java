package com.girmiti.skybandecr.ui.fragment.setting.connnect;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.cache.GeneralParamCache;
import com.girmiti.skybandecr.constant.Constant;
import com.girmiti.skybandecr.databinding.ConnectSettingFragmentBinding;
import com.girmiti.skybandecr.sdk.api.ECRImpl;
import com.girmiti.skybandecr.sdk.api.listener.ECRCore;
import com.girmiti.skybandecr.sdk.logger.Logger;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectSettingFragment extends Fragment implements Constant {

    public ConnectSettingViewModel connectSettingViewModel;
    private ConnectSettingFragmentBinding connectSettingFragmentBinding;
    private NavController navController;

    private String ipAddress = "";
    private String portNo = "";
    private GeneralParamCache generalParamCache = GeneralParamCache.getInstance();

    public static ECRCore getEcrCore() {
        return ecrCore;
    }

    private static ECRCore ecrCore;
    private Logger logger = Logger.getNewLogger(ConnectSettingFragment.class.getName());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Objects.requireNonNull(getActivity()).findViewById(R.id.home_logo).setVisibility(View.INVISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.left).setVisibility(View.VISIBLE);

        connectSettingFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.connect_setting_fragment, container, false);
        connectSettingViewModel = ViewModelProviders.of(this).get(ConnectSettingViewModel.class);

        setupListeners();

        return connectSettingFragmentBinding.getRoot();
    }

    private void setupListeners() {

        ipAddress = generalParamCache.getString(IP_ADDRESS);
        portNo = generalParamCache.getString(PORT);
        connectSettingFragmentBinding.ipAddress.setText(ipAddress);
        connectSettingFragmentBinding.portNo.setText(portNo);

        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);

        connectSettingFragmentBinding.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ipAddress = connectSettingFragmentBinding.ipAddress.getText().toString();
                portNo = connectSettingFragmentBinding.portNo.getText().toString();

                if (!validateIp(ipAddress) || !validatePort(portNo) || ipAddress.equals("") || portNo.equals("")) {
                    Toast.makeText(getContext(), R.string.ip_port_invalid, Toast.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog dialog = ProgressDialog.show(getActivity(), getString(R.string.connecting), getString(R.string.please_wait), true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        generalParamCache.putString(IP_ADDRESS, ipAddress);
                        generalParamCache.putString(PORT, portNo);
                        ecrCore = ECRImpl.getConnectInstance();
                        try {
                            int connectionStatus = ecrCore.doTCPIPConnection(ipAddress, Integer.parseInt(portNo));
                            if (connectionStatus == 0) {
                                logger.info("Socket Connected");
                                dialog.dismiss();
                                navController.navigate(R.id.action_navigation_connect_setting_to_connectedFragment2);
                            } else {
                                logger.info("Socket Connection failed");
                            }
                        } catch (final IOException e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    logger.severe("Exception on Connecting",e);
                                    Toast.makeText(getContext(), R.string.unable_to_connect, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        connectSettingFragmentBinding.disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralParamCache.getInstance().putString(IP_ADDRESS, null);
                GeneralParamCache.getInstance().putString(PORT, null);

                try {
                    connectSettingFragmentBinding.ipAddress.getText().clear();
                    connectSettingFragmentBinding.portNo.getText().clear();
                    if (ecrCore != null && ecrCore.doDisconnection() != 1) {
                        logger.info("Socket Disconnected");
                  //      GeneralParamCache.getInstance().putString(CASH_REGISTER_NO, null);
                        Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), R.string.socket_disconnected, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), " Not Connected", Toast.LENGTH_LONG).show();
                        logger.info("Socket is Already Disconnected");
                    }
                } catch (final IOException e) {
                    logger.severe("Exception on disconnecting",e);
                    Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "" + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validateIp(String ipAddress) {

        final Pattern IP_ADDRESS
                = Pattern.compile(
                "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                        + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                        + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                        + "|[1-9][0-9]|[0-9]))");
        Matcher matcher = IP_ADDRESS.matcher(ipAddress);

        return matcher.matches();
    }

    private boolean validatePort(String portNo) {

        Pattern pattern;
        Matcher matcher;
        String PORT = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
        pattern = Pattern.compile(PORT);
        matcher = pattern.matcher(portNo);

        return matcher.matches();
    }
}
