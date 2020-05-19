package com.girmiti.skybandecr.ui.fragment.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.cache.GeneralParamCache;
import com.girmiti.skybandecr.constant.Constant;
import com.girmiti.skybandecr.model.ActiveTxnData;
import com.girmiti.skybandecr.sdk.ConnectionManager;
import com.girmiti.skybandecr.transaction.StartTransaction;
import com.girmiti.skybandecr.databinding.HomeFragmentBinding;
import com.girmiti.skybandecr.transaction.listener.TransactionListener;
import com.girmiti.skybandecr.sdk.ThreadPoolExecutorService;
import com.girmiti.skybandecr.sdk.logger.Logger;

import java.util.Objects;

import lombok.Setter;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener, Constant {
    private GeneralParamCache generalParamCache = GeneralParamCache.getInstance();
    private String selectedItem;
    private HomeViewModel homeViewModel;
    private HomeFragmentBinding homeFragmentBinding;
    private NavController navController;
    private Logger logger = Logger.getNewLogger(HomeFragment.class.getName());
    private Activity activity;
    private ThreadPoolExecutorService threadPoolExecutorService = null;
    @Setter
    public static int position;

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
                new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure.. you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Objects.requireNonNull(getActivity()).finish();
                            }
                        })
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
        homeFragmentBinding.transactionSpinner.setSelection(position);

        if (GeneralParamCache.getInstance().getString(ECR_NUMBER) == null) {
            GeneralParamCache.getInstance().putString(ECR_NUMBER, getEcrNumberString());
        }

        if (ConnectionManager.Instance() != null && ConnectionManager.Instance().isConnected()) {
            homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_780);
        } else {
            homeFragmentBinding.connectionStatus.setImageResource(R.drawable.ic_group_782);
        }
        super.onStart();
    }

    private void setupListeners() {

        if (Objects.equals(generalParamCache.getString(CONNECTION_STATUS), CONNECTED)) {
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
                    validated = homeViewModel.validateData();
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
                if (Objects.equals(generalParamCache.getString(CONNECTION_STATUS), CONNECTED)) {

                    final ProgressDialog dialog = ProgressDialog.show(getContext(), getString(R.string.loading), getString(R.string.please_wait), true);
                    dialog.setCancelable(false);
                    final StartTransaction startTransaction = new StartTransaction(homeViewModel);

                    startTransaction.setTransactionListener(new TransactionListener() {

                        @Override
                        public void onSuccess() {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
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

                                    if (Objects.equals(errorMessage.getMessage(), "0")) {
                                        Toast.makeText(activity, "Time Out..Try Again", Toast.LENGTH_LONG).show();
                                        logger.debug(getClass() + "Exception>>"+errorMessage);
                                    } else if(Objects.equals(errorMessage.getMessage(), "1")){
                                        Toast.makeText(activity, "Exception in disconnection", Toast.LENGTH_LONG).show();
                                        logger.debug(getClass() + "Exception>>"+errorMessage);
                                    } else if(Objects.equals(errorMessage.getMessage(), "3")){
                                        Toast.makeText(activity, "Network Problem..Try Again", Toast.LENGTH_LONG).show();
                                        logger.debug(getClass() + "Exception>>"+errorMessage);
                                    } else if(Objects.equals(errorMessage.getMessage(), "2")){
                                        Toast.makeText(activity, "Socket not Connected", Toast.LENGTH_LONG).show();
                                        logger.debug(getClass() + "Exception>>"+errorMessage);
                                    } else {
                                        Toast.makeText(activity, errorMessage.getMessage(), Toast.LENGTH_LONG).show();
                                        logger.debug(getClass() + "Exception>>"+errorMessage);
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
    }

    @SuppressLint("DefaultLocale")
    public String getEcrNumberString() {
        // Random rnd = new Random();
        // int number = rnd.nextInt(999999);
        int number = 1;
        // this will convert any number sequence into 6 character.
        logger.debug("Ecr No Generated>>>" + String.format("%06d", number));
        return String.format("%06d", number);
    }
}