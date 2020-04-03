package com.girmiti.skybandecr.fragment.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.StartTransaction;
import com.girmiti.skybandecr.databinding.HomeFragmentBinding;
import com.girmiti.skybandecr.fragment.connectsetting.ConnectSettingViewModel;
import com.girmiti.skybandecr.listner.TransactionListener;
import com.girmiti.skybandecr.sdk.ThreadPoolExecutorService;
import com.girmiti.skybandecr.sdk.logger.Logger;

import java.util.Objects;

public class HomeFragment extends Fragment  implements AdapterView.OnItemSelectedListener {
   private String selectedItem;
    private HomeViewModel homeViewModel;
    private HomeFragmentBinding homeFragmentBinding;
    private NavController navController;
    private Logger logger = Logger.getNewLogger(HomeFragment.class.getName());
    private Activity activity;
    private ThreadPoolExecutorService threadPoolExecutorService = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        homeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);


        getActivity().findViewById(R.id.home_logo).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.left).setVisibility(View.INVISIBLE);

        return homeFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        setupListeners();
        activity = getActivity();
        threadPoolExecutorService = ThreadPoolExecutorService.getInstance();
    }

    private void setupListeners() {

        if (ConnectSettingViewModel.getSocketHostConnector() != null && ConnectSettingViewModel.getSocketHostConnector().getSocket() != null) {

            if (ConnectSettingViewModel.getSocketHostConnector().getSocket().isConnected()) {
                homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_780);
            }
        } else {
            homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_782);
        }

        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.transaction_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        homeFragmentBinding.transactionSpinner.setAdapter(adapter);

        homeFragmentBinding.transactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                homeViewModel.setReqData(selectedItem);
                logger.info(getClass() + "Data request is set");
                boolean validated = homeViewModel.validateData(homeFragmentBinding);
                logger.info(getClass() + "Data validated");

                if (!validated) {
                    Toast.makeText(getActivity(), "Invalid Input", Toast.LENGTH_LONG).show();
                    return;
                }

                if (ConnectSettingViewModel.getSocketHostConnector() != null && ConnectSettingViewModel.getSocketHostConnector().getSocket().isConnected()) {

                    final ProgressDialog dialog = ProgressDialog.show(getContext(), "Loading", "Please wait...", true);
                    final StartTransaction startTransaction = new StartTransaction(homeViewModel);
                    startTransaction.setTransactionListener(new TransactionListener() {
                        @Override
                        public void onSuccess() {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    if (selectedItem.equals("Register")) {

                                        if (homeViewModel.getTerminalNumber().equals("")) {
                                            Toast.makeText(activity, "Not Registered", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(activity, "Registered Successful", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        navController.navigate(R.id.action_homeFragment_to_bufferResponseFragment);
                                    }
                                }
                            });

                            threadPoolExecutorService.remove(startTransaction);
                        }

                        @Override
                        public void onError(final String errorMessage) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                                }
                            });
                            threadPoolExecutorService.remove(startTransaction);
                        }
                    });

                    threadPoolExecutorService.execute(startTransaction);
                } else {
                    Toast.makeText(getActivity(), "Socket is not connected", Toast.LENGTH_LONG).show();
                }
            }
        });

        homeFragmentBinding.transactionSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        selectedItem = parent.getItemAtPosition(position).toString();

        homeViewModel.resetVisibilityOfViews(homeFragmentBinding);
        homeViewModel.getVisibilityOfViews(selectedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getActivity(), "Transaction type not selected", Toast.LENGTH_LONG).show();
    }
}