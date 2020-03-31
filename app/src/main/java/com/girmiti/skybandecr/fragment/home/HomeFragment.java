package com.girmiti.skybandecr.fragment.home;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.databinding.HomeFragmentBinding;
import com.girmiti.skybandecr.fragment.connectsetting.ConnectSettingViewModel;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private HomeViewModel homeViewModel;
    private HomeFragmentBinding homeFragmentBinding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        homeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
       homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);


        getActivity().findViewById(R.id.home_logo).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.left).setVisibility(View.INVISIBLE);

        setupListeners();

        return homeFragmentBinding.getRoot();
    }

    private void setupListeners() {

        if ( ConnectSettingViewModel.getSocketHostConnector() != null && ConnectSettingViewModel.getSocketHostConnector().socket != null) {

            if(ConnectSettingViewModel.getSocketHostConnector().socket.isConnected()){
                homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_780);
            }
        }
        else
            homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_782);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.transaction_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        homeFragmentBinding.transactionSpinner.setAdapter(adapter);

        homeFragmentBinding.transactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String selectedItem = homeFragmentBinding.transactionSpinner.getSelectedItem().toString();

                if(selectedItem.equals("Select Transaction")){
                    Toast.makeText(getActivity(), "Please Select Transaction Type", Toast.LENGTH_LONG).show();
                    return;
                }

                 homeViewModel.setReqData(selectedItem);
                boolean a= homeViewModel.validateData(homeFragmentBinding);
                System.out.println("boolean>>"+a);

                 if( !a){
                     Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_LONG).show();
                     return;
                 }
                if ( ConnectSettingViewModel.getSocketHostConnector()!= null && ConnectSettingViewModel.getSocketHostConnector().socket.isConnected()) {

                    final ProgressDialog dialog = ProgressDialog.show(getContext(), "Loading", "Please wait...", true);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                final String terminalResponse = homeViewModel.sendData();

                                homeViewModel.parse(terminalResponse);

                                Log.e("msg", "Got Response");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        // Toast.makeText(getActivity(), "Response is: " + terminalResponse, Toast.LENGTH_LONG).show();
                                        if (selectedItem.equals("Register")) {

                                            if(homeViewModel.getTerminalNumber().equals(""))
                                                Toast.makeText(getActivity(), "Not Registered", Toast.LENGTH_LONG).show();
                                            else
                                                 Toast.makeText(getActivity(), "Registered Successful", Toast.LENGTH_LONG).show();

                                        } else
                                            navController.navigate(R.id.action_homeFragment_to_bufferResponseFragment);
                                    }
                                });

                            } catch (final IOException | NoSuchAlgorithmException e) {
                                e.printStackTrace();
                                dialog.dismiss();

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), "" + e, Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }).start();
                }
                else
                    Toast.makeText(getContext(), "Socket is not connected", Toast.LENGTH_LONG).show();
            }
        });

        homeFragmentBinding.transactionSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        final String selectedItem = parent.getItemAtPosition(position).toString();

        homeViewModel.resetVisibilityOfViews(homeFragmentBinding);
        homeViewModel.getVisibilityOfViews(selectedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
