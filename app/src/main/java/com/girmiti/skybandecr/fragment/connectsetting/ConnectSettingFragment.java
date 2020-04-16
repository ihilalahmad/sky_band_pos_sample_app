package com.girmiti.skybandecr.fragment.connectsetting;

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
import com.girmiti.skybandecr.databinding.ConnectSettingFragmentBinding;
import com.girmiti.skybandecr.fragment.home.HomeViewModel;
import com.girmiti.skybandecr.sdk.logger.Logger;

import java.io.IOException;
import java.util.Objects;

public class ConnectSettingFragment extends Fragment {

    public static ConnectSettingViewModel connectSettingViewModel;
    private ConnectSettingFragmentBinding connectSettingFragmentBinding;
    private NavController navController;
    private static String ipAddress = "";
    private static String portNo = "";
    private static boolean saveData = false;
    private Logger logger = Logger.getNewLogger(ConnectSettingFragment.class.getName());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        getActivity().findViewById(R.id.home_logo).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.left).setVisibility(View.VISIBLE);

        connectSettingFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.connect_setting_fragment, container, false);
        connectSettingViewModel = ViewModelProviders.of(this).get(ConnectSettingViewModel.class);

        setupListeners();

        return connectSettingFragmentBinding.getRoot();
    }

    private void setupListeners() {

        if(saveData) {
            connectSettingFragmentBinding.ipAddress.setText(ipAddress);
            connectSettingFragmentBinding.portNo.setText(portNo);
        }
        else {
            connectSettingFragmentBinding.ipAddress.setText("");
            connectSettingFragmentBinding.portNo.setText("");
        }

        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);

        connectSettingFragmentBinding.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ipAddress = connectSettingFragmentBinding.ipAddress.getText().toString();
                portNo = connectSettingFragmentBinding.portNo.getText().toString();

                if (!connectSettingViewModel.validateIp(ipAddress) || !connectSettingViewModel.validatePort(portNo) || ipAddress.equals("") || portNo.equals("")) {
                    Toast.makeText(getContext(), R.string.ip_port_invalid, Toast.LENGTH_LONG).show();

                    return;
                }

                final ProgressDialog dialog = ProgressDialog.show(getActivity(), getString(R.string.connecting), getString(R.string.please_wait), true);
                dialog.setCancelable(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            if (connectSettingViewModel.getConnection(ipAddress, Integer.parseInt(portNo))) {
                                dialog.dismiss();
                                saveData = true;
                                navController.navigate(R.id.action_navigation_connect_setting_to_connectedFragment2);
                            }

                        } catch (final IOException e) {
                            logger.severe(getString(R.string.Exception_during_connection), e);

                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), R.string.unable_to_connect, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                }).start();
            }
        });

        connectSettingFragmentBinding.disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    connectSettingViewModel.disConnectSocket();
                    saveData = false;
                    Toast.makeText(getContext().getApplicationContext(), R.string.socket_disconnected, Toast.LENGTH_LONG).show();

                } catch (final IOException e) {
                    e.printStackTrace();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext().getApplicationContext(), "" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
