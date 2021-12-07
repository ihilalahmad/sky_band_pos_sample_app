package com.skyband.ecr.ui.fragment.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.skyband.ecr.R;
import com.skyband.ecr.GetPortBroadcastReceiver;
import com.skyband.ecr.cache.GeneralParamCache;
import com.skyband.ecr.constant.Constant;
import com.skyband.ecr.model.ActiveTxnData;
import com.skyband.ecr.sdk.BluetoothConnectionManager;
import com.skyband.ecr.sdk.ConnectionManager;
import com.skyband.ecr.transaction.StartTransaction;
import com.skyband.ecr.databinding.HomeFragmentBinding;
import com.skyband.ecr.transaction.TransactionType;
import com.skyband.ecr.transaction.listener.TransactionListener;
import com.skyband.ecr.sdk.ThreadPoolExecutorService;
import com.skyband.ecr.sdk.logger.Logger;
import com.skyband.ecr.ui.fragment.setting.connnect.ConnectSettingFragment;
import com.skyband.ecr.ui.fragment.setting.transaction.TransactionSettingViewModel;

import java.math.BigDecimal;
import java.util.Objects;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private GeneralParamCache generalParamCache = GeneralParamCache.getInstance();
    private String selectedItem;
    private HomeViewModel homeViewModel;
    private HomeFragmentBinding homeFragmentBinding;
    private NavController navController;
    private Logger logger = Logger.getNewLogger(HomeFragment.class.getName());
    private Activity activity;
    private ThreadPoolExecutorService threadPoolExecutorService = null;
    private String currencySymbol = "$";
    private int divider = 100;
    private String amountFormat = "%.2f";
    private GetPortBroadcastReceiver getPortBroadcastReceiver;

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

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                        .setMessage("Are you sure.. you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> Objects.requireNonNull(getActivity()).finish())
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        setupListeners();
        activity = getActivity();
        threadPoolExecutorService = ThreadPoolExecutorService.getInstance();
    }

    @Override
    public void onStart() {

        homeFragmentBinding.transactionSpinner.setSelection(ActiveTxnData.getInstance().getPosition());

        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);

        if (GeneralParamCache.getInstance().getString(Constant.ECR_NUMBER) == null) {
            GeneralParamCache.getInstance().putString(Constant.ECR_NUMBER, getEcrNumberString());
        }

        // checking connection type
        if (ActiveTxnData.getInstance().getConnectPosition() == 0) {
            if (ConnectionManager.instance() != null && Objects.equals(generalParamCache.getString(Constant.CONNECTION_STATUS), Constant.CONNECTED)) {
                homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_780);
            } else {
                homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_782);
            }
        } else if (BluetoothConnectionManager.instance() != null && Objects.equals(generalParamCache.getString(Constant.CONNECTION_STATUS), Constant.CONNECTED)) {
            homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_780);
        } else {
            homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_782);
        }

        if (TransactionSettingViewModel.getAppToAPPCommunication() == 1) {
            homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_780);
            if (ConnectSettingFragment.getEcrCore() == null) {
                ConnectSettingFragment.setEcrCore();
            }
            generalParamCache.putString(Constant.IP_ADDRESS, "localhost");

        }

        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ActiveTxnData.getInstance().getReceivedIntentData() != null) {

            String terminalResponse;

            try {
                terminalResponse = homeViewModel.changeToTransactionType(ActiveTxnData.getInstance().getReceivedIntentData());
                homeViewModel.handleTerminalResponse(terminalResponse);
                ActiveTxnData.getInstance().setResData(terminalResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (ActiveTxnData.getInstance().getTransactionType() == TransactionType.REGISTER) {
                if (ActiveTxnData.getInstance().getTerminalID() == null) {
                    Toast.makeText(activity, R.string.id_not_received, Toast.LENGTH_LONG).show();
                    return;
                } else
                    ActiveTxnData.getInstance().setRegistered(true);
            } else if (ActiveTxnData.getInstance().getTransactionType() == TransactionType.START_SESSION) {
                ActiveTxnData.getInstance().setSessionStarted(true);

            } else if (ActiveTxnData.getInstance().getTransactionType() == TransactionType.END_SESSION) {
                ActiveTxnData.getInstance().setSessionStarted(false);
            }

            ActiveTxnData.getInstance().setReceivedIntentData(null);
            navController.navigate(R.id.action_homeFragment_to_bufferResponseFragment);
        }

        if (TransactionSettingViewModel.getAppToAPPCommunication() == 1) {
            sendAndReceiveBroadcast();
        }

        logger.info("Inside onResume Home");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (TransactionSettingViewModel.getAppToAPPCommunication() == 1) {
            unRegisterBroadcast();
        }
    }

    private void setupListeners() {

        addTextChanged();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.transaction_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        homeFragmentBinding.transactionSpinner.setAdapter(adapter);

        homeFragmentBinding.transactionBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean validated;

                try {
                    validated = homeViewModel.validateData(selectedItem, getContext());
                } catch (Exception e) {
                    logger.severe("Exception on validating", e);
                    Toast.makeText(getActivity(), e.getMessage() + "", Toast.LENGTH_LONG).show();
                    return;
                }

                logger.info(getClass() + getString(R.string.req_data_validated));
                if (!validated) {
                    Toast.makeText(getActivity(), R.string.invalid_input, Toast.LENGTH_LONG).show();
                    return;
                }

                homeViewModel.setReqData(selectedItem, getContext());
                logger.info(getClass() + getString(R.string.request_data_set));

                int appToAppCommunication = TransactionSettingViewModel.getAppToAPPCommunication();

                if (appToAppCommunication == 1) {

                    if (!appInstalledOrNot()) {
                        Toast.makeText(activity, "Mada App  is not installed...please install and try again", Toast.LENGTH_LONG).show();
                    } else {

                        try {
                            byte[] packData = homeViewModel.getPackData();
                            Intent intent = requireActivity().getPackageManager().getLaunchIntentForPackage("com.skyband.pos.app");
                            intent.putExtra("message", "exr-txn-event");
                            intent.putExtra("request", packData);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                    if (Objects.equals(generalParamCache.getString(Constant.CONNECTION_STATUS), Constant.CONNECTED)) {

                        final ProgressDialog dialog = ProgressDialog.show(getContext(), getString(R.string.loading), getString(R.string.please_wait), true);
                        dialog.setCancelable(false);
                        final StartTransaction startTransaction = new StartTransaction(homeViewModel);

                        startTransaction.setTransactionListener(new TransactionListener() {

                            @Override
                            public void onSuccess() {
                                activity.runOnUiThread(() -> {
                                    dialog.dismiss();

                                    if (selectedItem.equals(getString(R.string.register))) {
                                        if (ActiveTxnData.getInstance().getTerminalID() == null) {
                                            Toast.makeText(activity, R.string.id_not_received, Toast.LENGTH_LONG).show();
                                            return;
                                        } else
                                            ActiveTxnData.getInstance().setRegistered(true);
                                    } else if (selectedItem.equals(getString(R.string.start_session))) {
                                        ActiveTxnData.getInstance().setSessionStarted(true);

                                    } else if (selectedItem.equals(getString(R.string.end_session))) {
                                        ActiveTxnData.getInstance().setSessionStarted(false);
                                    }
                                    navController.navigate(R.id.action_homeFragment_to_bufferResponseFragment);
                                });
                                threadPoolExecutorService.remove(startTransaction);
                            }

                            @Override
                            public void onError(final Exception errorMessage) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();

                                        if (Objects.equals(errorMessage.getMessage(), "0")) {
                                            Toast.makeText(activity, "Time Out..Try Again", Toast.LENGTH_LONG).show();
                                            logger.debug(getClass() + "Exception>>" + errorMessage);
                                        } else if (Objects.equals(errorMessage.getMessage(), "1")) {
                                            Toast.makeText(activity, "Exception in disconnection", Toast.LENGTH_LONG).show();
                                            logger.debug(getClass() + "Exception>>" + errorMessage);
                                        } else if (Objects.equals(errorMessage.getMessage(), "3")) {
                                            Toast.makeText(activity, "Network Problem..Try Again", Toast.LENGTH_LONG).show();
                                            logger.debug(getClass() + "Exception>>" + errorMessage);
                                        } else if (Objects.equals(errorMessage.getMessage(), "2")) {
                                            Toast.makeText(activity, "Socket not Connected", Toast.LENGTH_LONG).show();
                                            logger.debug(getClass() + "Exception>>" + errorMessage);
                                        } else if (Objects.equals(errorMessage.getMessage(), "4")) {
                                            Toast.makeText(activity, "Sending incorrect parameter", Toast.LENGTH_LONG).show();
                                            logger.debug(getClass() + "Exception>>" + errorMessage);
                                        } else {
                                            Toast.makeText(activity, errorMessage.getMessage(), Toast.LENGTH_LONG).show();
                                            logger.debug(getClass() + "Exception>>" + errorMessage);
                                        }
                                    }
                                });
                                threadPoolExecutorService.remove(startTransaction);
                            }
                        });
                        threadPoolExecutorService.execute(startTransaction);
                    } else {
                        homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_782);
                        Toast.makeText(getActivity(), R.string.soccet_not_connected, Toast.LENGTH_LONG).show();
                    }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        ActiveTxnData.getInstance().setPosition(position);
        selectedItem = parent.getItemAtPosition(position).toString();
        logger.debug("Dropdown Selected Item>>" + selectedItem);
        if (selectedItem.equals(TransactionType.PURCHASE_ADVICE_FULL.getTransactionType())) {
            ActiveTxnData.getInstance().setPartialCapture(false);
            selectedItem = "Purchase Advice Full";
        } else if (selectedItem.equals("Purchase Advice(Partial Capture)")) {
            ActiveTxnData.getInstance().setPartialCapture(true);
            selectedItem = "Purchase Advice Full";
        }
        homeViewModel.resetVisibilityOfViews(homeFragmentBinding);
        homeViewModel.getVisibilityOfViews(selectedItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @SuppressLint("DefaultLocale")
    private String getEcrNumberString() {
        // Random rnd = new Random();
        // int number = rnd.nextInt(999999);
        int number = 1;
        // this will convert any number sequence into 6 character.
        logger.debug("Ecr No Generated>>>" + String.format("%06d", number));
        return String.format("%06d", number);
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
            if (!s.toString().equals(current)) {

                String replaceable = String.format("[%s,.\\s]", currencySymbol);
                String cleanString = s.toString().replaceAll(replaceable, "");

                double parsed;
                try {
                    parsed = Double.parseDouble(cleanString);
                } catch (NumberFormatException e) {
                    parsed = 0.00;
                }
                double da = parsed / divider;
                String formatted = getFormattedAmount(BigDecimal.valueOf(da));
                current = formatted;

                try {
                    if (homeFragmentBinding.payAmt.getText().hashCode() == s.hashCode()) {
                        homeFragmentBinding.payAmt.setText(formatted);
                        homeFragmentBinding.payAmt.setSelection(formatted.length());

                    } else if (homeFragmentBinding.cashBackAmt.getText().hashCode() == s.hashCode()) {
                        homeFragmentBinding.cashBackAmt.setText(formatted);
                        homeFragmentBinding.cashBackAmt.setSelection(formatted.length());

                    } else if (homeFragmentBinding.cashAdvanceAmt.getText().hashCode() == s.hashCode()) {
                        homeFragmentBinding.cashAdvanceAmt.setText(formatted);
                        homeFragmentBinding.cashAdvanceAmt.setSelection(formatted.length());

                    } else if (homeFragmentBinding.refundAmt.getText().hashCode() == s.hashCode()) {
                        homeFragmentBinding.refundAmt.setText(formatted);
                        homeFragmentBinding.refundAmt.setSelection(formatted.length());

                    } else if (homeFragmentBinding.authAmt.getText().hashCode() == s.hashCode()) {
                        homeFragmentBinding.authAmt.setText(formatted);
                        homeFragmentBinding.authAmt.setSelection(formatted.length());

                    } else if (homeFragmentBinding.origTransactionAmt.getText().hashCode() == s.hashCode()) {
                        homeFragmentBinding.origTransactionAmt.setText(formatted);
                        homeFragmentBinding.origTransactionAmt.setSelection(formatted.length());

                    } else if (homeFragmentBinding.billPayAmt.getText().hashCode() == s.hashCode()) {
                        homeFragmentBinding.billPayAmt.setText(formatted);
                        homeFragmentBinding.billPayAmt.setSelection(formatted.length());
                    }

                } catch (StackOverflowError e) {
                    logger.info(HomeFragment.this.getClass() + "::" + e.getMessage());
                }
            }
        }

        private String getFormattedAmount(BigDecimal amount) {
            return String.format(amountFormat, amount).replace(",", ".");
        }
    }

    private boolean appInstalledOrNot() {
        PackageManager pm = requireActivity().getPackageManager();
        try {
            pm.getPackageInfo("com.skyband.pos.app", PackageManager.GET_ACTIVITIES);
            logger.info("App Installed");
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void sendAndReceiveBroadcast() {
        Intent intent = new Intent();
        intent.setAction("com.skyband.pos.app.send.ecr.port");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setComponent(
                new ComponentName("com.skyband.pos.app", "com.skyband.pos.app.ui.ecr.ECRPortBroadcastReceiver"));
        getContext().sendBroadcast(intent);
        getPortBroadcastReceiver = new GetPortBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("com.skyband.pos.perform.port");
        getContext().registerReceiver(getPortBroadcastReceiver, intentFilter);
    }

    private void unRegisterBroadcast() {
        if (getPortBroadcastReceiver != null) {
            getContext().unregisterReceiver(getPortBroadcastReceiver);
        }

    }
}