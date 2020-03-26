package com.girmiti.skybandecr.fragment.connectsetting;

import androidx.appcompat.widget.Toolbar;
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

import java.io.IOException;

public class ConnectSettingFragment extends Fragment {

    private ConnectSettingViewModel connectSettingViewModel;
    private ConnectSettingFragmentBinding connectSettingFragmentBinding;
    protected NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        getActivity().findViewById(R.id.home_logo).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.left).setVisibility(View.VISIBLE);

        connectSettingFragmentBinding= DataBindingUtil.inflate(inflater,R.layout.connect_setting_fragment,container,false);
        connectSettingViewModel = ViewModelProviders.of(this).get(ConnectSettingViewModel.class);

        setupListeners();

        return connectSettingFragmentBinding.getRoot();
    }

    private void setupListeners() {


        navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);

       connectSettingFragmentBinding.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog dialog = ProgressDialog.show(getActivity(), "connecting", "Please wait...", true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                try {

                    if(connectSettingViewModel.getConnectResponse(connectSettingFragmentBinding.ipAddress.getText().toString(), Integer.parseInt(connectSettingFragmentBinding.portNo.getText().toString()))){
                        dialog.dismiss();
                        navController.navigate(R.id.action_navigation_connect_setting_to_connectedFragment2);
                    }

                } catch (final IOException e) {
                    e.printStackTrace();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(getContext().getApplicationContext(), "Unable to Connect", Toast.LENGTH_LONG).show();
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
                try {
                    connectSettingViewModel.disConnectSocket();
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

}
