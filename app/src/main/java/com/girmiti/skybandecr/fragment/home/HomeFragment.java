package com.girmiti.skybandecr.fragment.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.girmiti.skybandecr.fragment.connectsetting.ConnectSettingFragment;
import com.girmiti.skybandecr.fragment.connectsetting.ConnectSettingViewModel;
import com.girmiti.skybandecr.listner.TransactionListener;
import com.girmiti.skybandecr.sdk.ThreadPoolExecutorService;
import com.girmiti.skybandecr.sdk.logger.Logger;

import java.io.IOException;
import java.math.BigDecimal;
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

    @Override
    public void onStart() {
        super.onStart();
        homeFragmentBinding.transactionSpinner.setSelection(position);
    }

    private void setupListeners() {

        addTextChanged();

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
                logger.info(getClass() + getString(R.string.request_data_set));
                boolean validated = false;

                try {
                    validated = homeViewModel.validateData(homeFragmentBinding);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage()+"", Toast.LENGTH_LONG).show();
                    return;
                }

                logger.info(getClass() + getString(R.string.req_data_validated));
                if (!validated) {
                    Toast.makeText(getActivity(), R.string.invalid_input, Toast.LENGTH_LONG).show();
                    return;
                }

                if (ConnectSettingViewModel.getSocketHostConnector() != null && ConnectSettingViewModel.getSocketHostConnector().getSocket().isConnected()) {

                    final ProgressDialog dialog = ProgressDialog.show(getContext(), getString(R.string.loading), getString(R.string.please_wait), true);
                    final StartTransaction startTransaction = new StartTransaction(homeViewModel);
                    startTransaction.setTransactionListener(new TransactionListener() {
                        @Override
                        public void onSuccess() {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    if (selectedItem.equals(getString(R.string.register)) || selectedItem.equals(getString(R.string.start_session)) ||  selectedItem.equals(getString(R.string.end_session)) ) {

                                        if (selectedItem.equals(getString(R.string.register))) {
                                            if(HomeViewModel.getTerminalID().equals("")){
                                                Toast.makeText(activity, "Not Registered", Toast.LENGTH_LONG).show();
                                            } else {
                                                HomeViewModel.isRegistered=true;
                                                Toast.makeText(activity, R.string.registered_success, Toast.LENGTH_LONG).show();
                                            }
                                        } else if(selectedItem.equals(getString(R.string.start_session))) {
                                         HomeViewModel.isSessionStarted= true;
                                            Toast.makeText(activity, R.string.session_started, Toast.LENGTH_LONG).show();
                                        } else if(selectedItem.equals(getString(R.string.end_session))) {
                                            HomeViewModel.isSessionStarted = false;
                                            Toast.makeText(activity, R.string.session_ended, Toast.LENGTH_LONG).show();
                                        }

                                    } else {
                                        navController.navigate(R.id.action_homeFragment_to_bufferResponseFragment);
                                    }
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
                                        ConnectSettingFragment.connectSettingViewModel.disConnectSocket();
                                        homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_782);
                                        Toast.makeText(getActivity(), ""+errorMessage, Toast.LENGTH_LONG).show();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_782);
                                        Toast.makeText(getActivity(), ""+e, Toast.LENGTH_LONG).show();
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

    private void addTextChanged() {

        homeFragmentBinding.payAmt.addTextChangedListener(new CustomTextWatcher());
        homeFragmentBinding.cashAdvanceAmt.addTextChangedListener(new CustomTextWatcher());
        homeFragmentBinding.cashBackAmt.addTextChangedListener(new CustomTextWatcher());
        homeFragmentBinding.refundAmt.addTextChangedListener(new CustomTextWatcher());
        homeFragmentBinding.authAmt.addTextChangedListener(new CustomTextWatcher());
        homeFragmentBinding.origTransactionAmt.addTextChangedListener(new CustomTextWatcher());
        homeFragmentBinding.billPayAmt.addTextChangedListener(new CustomTextWatcher());
    }


    private class CustomTextWatcher implements TextWatcher {
        private String current = "";

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals( current )) {

                String currencySymbol = "$";
                String replaceable = String.format( "[%s,.\\s]", currencySymbol);
                String cleanString = s.toString().replaceAll( replaceable, "" );

                double parsed;
                try {
                    parsed = Double.parseDouble( cleanString );
                } catch (NumberFormatException e) {
                    parsed = 0.00;
                }
                int divider = 100;
                double da = parsed / divider;
                String formatted = getFormattedAmount( BigDecimal.valueOf(da) );
                current = formatted;

                try {
                    if(homeFragmentBinding.payAmt.getText().hashCode() == s.hashCode()){
                        homeFragmentBinding.payAmt.setText( formatted );
                        homeFragmentBinding.payAmt.setSelection( formatted.length() );

                    } else if (homeFragmentBinding.cashBackAmt.getText().hashCode() == s.hashCode()){
                        homeFragmentBinding.cashBackAmt.setText( formatted );
                        homeFragmentBinding.cashBackAmt.setSelection( formatted.length() );

                    } else if (homeFragmentBinding.cashAdvanceAmt.getText().hashCode() == s.hashCode()){
                        homeFragmentBinding.cashAdvanceAmt.setText( formatted );
                        homeFragmentBinding.cashAdvanceAmt.setSelection( formatted.length() );

                    } else if (homeFragmentBinding.refundAmt.getText().hashCode() == s.hashCode()){
                        homeFragmentBinding.refundAmt.setText( formatted );
                        homeFragmentBinding.refundAmt.setSelection( formatted.length() );

                    } else if (homeFragmentBinding.authAmt.getText().hashCode() == s.hashCode()){
                        homeFragmentBinding.authAmt.setText( formatted );
                        homeFragmentBinding.authAmt.setSelection( formatted.length() );

                    } else if (homeFragmentBinding.origTransactionAmt.getText().hashCode() == s.hashCode()){
                        homeFragmentBinding.origTransactionAmt.setText( formatted );
                        homeFragmentBinding.origTransactionAmt.setSelection( formatted.length() );

                    } else if (homeFragmentBinding.billPayAmt.getText().hashCode() == s.hashCode()){
                        homeFragmentBinding.billPayAmt.setText( formatted );
                        homeFragmentBinding.billPayAmt.setSelection( formatted.length() );
                    }

                } catch (StackOverflowError e) {
                    logger.info(HomeFragment.this.getClass() + "::" + e.getMessage());
                }
            }
        }

        private String getFormattedAmount(BigDecimal amount) {
            String amountFormat = "%.2f";
            return String.format(amountFormat, amount).replace(",",".");
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        this.position=position;
        selectedItem = parent.getItemAtPosition(position).toString();

        homeViewModel.resetVisibilityOfViews(homeFragmentBinding);
        homeViewModel.getVisibilityOfViews(selectedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getActivity(), R.string.transactn_type_not_selected, Toast.LENGTH_LONG).show();
    }
}