package com.skyband.ecr.ui.fragment.setting.connnect;

import androidx.databinding.DataBindingUtil;

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

import com.skyband.ecr.R;
import com.skyband.ecr.cache.GeneralParamCache;
import com.skyband.ecr.constant.Constant;
import com.skyband.ecr.databinding.ConnectSettingFragmentBinding;
import com.skyband.ecr.sdk.api.ECRImpl;
import com.skyband.ecr.sdk.api.listener.ECRCore;
import com.skyband.ecr.sdk.logger.Logger;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectSettingFragment extends Fragment {

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

        setupListeners();

        return connectSettingFragmentBinding.getRoot();
    }

    private void setupListeners() {

        ipAddress = generalParamCache.getString(Constant.IP_ADDRESS);
        portNo = generalParamCache.getString(Constant.PORT);
        connectSettingFragmentBinding.ipAddress.setText(ipAddress);
        connectSettingFragmentBinding.portNo.setText(portNo);

        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);

        connectSettingFragmentBinding.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ipAddress = connectSettingFragmentBinding.ipAddress.getText().toString();
                portNo = connectSettingFragmentBinding.portNo.getText().toString();

                if (ipAddress.equals("") && portNo.equals("")) {
                    Toast.makeText(getContext(), R.string.ip_and_port_empty, Toast.LENGTH_LONG).show();
                    return;
                } else if (ipAddress.equals("") || portNo.equals("")) {
                    Toast.makeText(getContext(), R.string.ip_or_port_empty, Toast.LENGTH_LONG).show();
                    return;
                } else if (!validateIp(ipAddress)) {
                    Toast.makeText(getContext(), R.string.ip_invalid, Toast.LENGTH_LONG).show();
                    return;
                } else if (!validatePort(portNo)) {
                    Toast.makeText(getContext(), R.string.port_invalid, Toast.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog dialog = ProgressDialog.show(getActivity(), getString(R.string.connecting), getString(R.string.please_wait), true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        generalParamCache.putString(Constant.IP_ADDRESS, ipAddress);
                        generalParamCache.putString(Constant.PORT, portNo);
                        ecrCore = ECRImpl.getConnectInstance();
                        try {
                            int connectionStatus = ecrCore.doTCPIPConnection(ipAddress, Integer.parseInt(portNo));
                            if (connectionStatus == 0) {
                                logger.info("Socket Connected");
                                dialog.dismiss();
                                generalParamCache.putString(Constant.CONNECTION_STATUS, Constant.CONNECTED);
                                navController.navigate(R.id.action_navigation_connect_setting_to_connectedFragment2);
                            } else {
                                logger.info("Socket Connection failed");
                            }
                        } catch (final IOException e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    logger.severe("Exception on Connecting", e);
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
                GeneralParamCache.getInstance().putString(Constant.IP_ADDRESS, null);
                GeneralParamCache.getInstance().putString(Constant.PORT, null);

                try {
                    connectSettingFragmentBinding.ipAddress.getText().clear();
                    connectSettingFragmentBinding.portNo.getText().clear();
                    if (ecrCore != null && ecrCore.doDisconnection() != 1) {
                        logger.info("Socket Disconnected");
                        Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), R.string.socket_disconnected, Toast.LENGTH_LONG).show();
                        generalParamCache.putString(Constant.CONNECTION_STATUS, Constant.DISCONNECTED);
                    } else {
                        Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), " Not Connected", Toast.LENGTH_LONG).show();
                        logger.info("Socket is Already Disconnected");
                    }
                } catch (final IOException e) {
                    logger.severe("Exception on disconnecting", e);
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