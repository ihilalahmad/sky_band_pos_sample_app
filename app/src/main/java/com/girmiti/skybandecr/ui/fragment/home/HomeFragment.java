package com.girmiti.skybandecr.ui.fragment.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
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
import com.girmiti.skybandecr.model.ActiveTxnData;
import com.girmiti.skybandecr.sdk.ConnectionManager;
import com.girmiti.skybandecr.transaction.StartTransaction;
import com.girmiti.skybandecr.databinding.HomeFragmentBinding;
import com.girmiti.skybandecr.transaction.listener.TransactionListener;
import com.girmiti.skybandecr.sdk.ThreadPoolExecutorService;
import com.girmiti.skybandecr.sdk.logger.Logger;

import java.io.IOException;
import java.util.Objects;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private String selectedItem;
    private HomeViewModel homeViewModel;
    private HomeFragmentBinding homeFragmentBinding;
    private NavController navController;
    private Logger logger = Logger.getNewLogger(HomeFragment.class.getName());
    private Activity activity;
    private ThreadPoolExecutorService threadPoolExecutorService = null;
    private static int position;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        homeFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);

        Objects.requireNonNull(getActivity()).findViewById(R.id.home_logo).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.left).setVisibility(View.INVISIBLE);

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

    @Override
    public void onStart() {
        super.onStart();
        homeFragmentBinding.transactionSpinner.setSelection(position);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ConnectionManager.Instance() != null && ConnectionManager.Instance().isConnected()) {
            homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_780);
        } else {
            homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_782);
        }
    }

    private void setupListeners() {

        if (ConnectionManager.Instance() != null && ConnectionManager.Instance().isConnected()) {
            homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_780);
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
                logger.info(getClass() + getString(R.string.request_data_set));

                boolean validated = false;

                try {
                    validated = homeViewModel.validateData(homeFragmentBinding);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage() + "", Toast.LENGTH_LONG).show();
                    return;
                }

                logger.info(getClass() + getString(R.string.req_data_validated));
                if (!validated) {
                    Toast.makeText(getActivity(), R.string.invalid_input, Toast.LENGTH_LONG).show();
                    return;
                }
                if ( ConnectionManager.Instance() != null && ConnectionManager.Instance().isConnected()) {

                    final ProgressDialog dialog = ProgressDialog.show(getContext(), getString(R.string.loading), getString(R.string.please_wait), true);
                    final StartTransaction startTransaction = new StartTransaction(homeViewModel);
                    startTransaction.setTransactionListener(new TransactionListener() {
                        @Override
                        public void onSuccess() {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    if (selectedItem.equals(getString(R.string.register))) {
                                        if (ActiveTxnData.getInstance().getTerminalID().equals("")) {
                                            Toast.makeText(activity, R.string.not_registered, Toast.LENGTH_LONG).show();
                                            return;
                                        } else {
                                            ActiveTxnData.getInstance().setRegistered(true);
                                        }
                                    } else if (selectedItem.equals(getString(R.string.start_session))) {
                                        ActiveTxnData.getInstance().setSessionStarted(true);

                                    } else if (selectedItem.equals(getString(R.string.end_session))) {
                                        ActiveTxnData.getInstance().setSessionStarted(false);

                                    }
                                    navController.navigate(R.id.action_homeFragment_to_bufferResponseFragment);
                                }
                            });
                            threadPoolExecutorService.remove(startTransaction);
                        }

                        @Override
                        public void onError(final Exception errorMessage) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    try {
                                        ConnectionManager.Instance().disconnect();
                                        homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_782);
                                        Toast.makeText(getActivity(), "Connection Reset..Please Connect Again", Toast.LENGTH_LONG).show();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_782);
                                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                            threadPoolExecutorService.remove(startTransaction);
                        }
                    });

                    threadPoolExecutorService.execute(startTransaction);
                } else {
                    Toast.makeText(getActivity(), R.string.soccet_not_connected, Toast.LENGTH_LONG).show();
                }
            }
        });

        homeFragmentBinding.transactionSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        HomeFragment.position = position;
        selectedItem = parent.getItemAtPosition(position).toString();

        homeViewModel.resetVisibilityOfViews(homeFragmentBinding);
        homeViewModel.getVisibilityOfViews(selectedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getActivity(), R.string.transactn_type_not_selected, Toast.LENGTH_LONG).show();
    }
}