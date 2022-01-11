package com.skyband.ecr.ui.fragment.setting.connnect;

import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skyband.ecr.R;
import com.skyband.ecr.cache.GeneralParamCache;
import com.skyband.ecr.constant.Constant;
import com.skyband.ecr.databinding.ConnectSettingFragmentBinding;
import com.skyband.ecr.model.ActiveTxnData;
import com.skyband.ecr.sdk.api.ECRImpl;
import com.skyband.ecr.sdk.api.listener.ECRCore;
import com.skyband.ecr.sdk.logger.Logger;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.SneakyThrows;

public class ConnectSettingFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ConnectSettingFragmentBinding connectSettingFragmentBinding;
    private NavController navController;

    private String ipAddress = "";
    private String portNo = "";
    private GeneralParamCache generalParamCache = GeneralParamCache.getInstance();

    //Added for bluetooth

    String selectedItem;
    private ConnectSettingViewModel connectSettingViewModel;
    private Dialog dialog;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> discoveredDevicesAdapter;
    private BluetoothDevice connectingDevice;

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    public static ECRCore getEcrCore() {
        return ecrCore;
    }

    public static void setEcrCore() {
        ecrCore = ECRImpl.getConnectInstance();
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

    @SneakyThrows
    @Override
    public void onStart() {
        connectSettingFragmentBinding.connectionTypeSpinner.setSelection(ActiveTxnData.getInstance().getConnectPosition());
        super.onStart();
    }

    private void setupListeners() {

        ipAddress = generalParamCache.getString(Constant.IP_ADDRESS);
        portNo = generalParamCache.getString(Constant.PORT);
        connectSettingFragmentBinding.ipAddress.setText(ipAddress);
        connectSettingFragmentBinding.portNo.setText(portNo);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.connection_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        connectSettingFragmentBinding.connectionTypeSpinner.setAdapter(adapter);

        navController = Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment);

        connectSettingFragmentBinding.connectionTypeSpinner.setOnItemSelectedListener(this);

        connectSettingFragmentBinding.connectButton.setOnClickListener(v -> {

            ipAddress = connectSettingFragmentBinding.ipAddress.getText().toString();
            portNo = connectSettingFragmentBinding.portNo.getText().toString();

            //Disable App To App
            GeneralParamCache.getInstance().putInt(Constant.ENABLE_APP_APP_COMMUNICATION, 0);

            if (!ActiveTxnData.getInstance().isLocalHostConnectionType()) {
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

                new Thread(() -> {
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
                            generalParamCache.putString(Constant.CONNECTION_STATUS, Constant.DISCONNECTED);
                        }
                    } catch (final IOException e) {
                        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                            dialog.dismiss();
                            logger.severe("Exception on Connecting", e);
                            generalParamCache.putString(Constant.CONNECTION_STATUS, Constant.DISCONNECTED);
                            Toast.makeText(getContext(), R.string.unable_to_connect, Toast.LENGTH_LONG).show();
                        });
                    }
                }).start();

            } else {
                navController.navigate(R.id.action_navigation_connect_setting_to_connectedFragment2);
            }

        });

        connectSettingFragmentBinding.disconnectButton.setOnClickListener(v -> {
            GeneralParamCache.getInstance().putString(Constant.IP_ADDRESS, null);
            GeneralParamCache.getInstance().putString(Constant.PORT, null);
            generalParamCache.putString(Constant.CONNECTION_STATUS, Constant.DISCONNECTED);
            try {
                connectSettingFragmentBinding.ipAddress.getText().clear();
                connectSettingFragmentBinding.portNo.getText().clear();
                if (ecrCore != null && ecrCore.doDisconnection() != 1) {
                    logger.info("Socket Disconnected");
                    Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), R.string.socket_disconnected, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), " Not Connected", Toast.LENGTH_LONG).show();
                    logger.info("Socket is Already Disconnected");
                }
            } catch (final IOException e) {
                logger.severe("Exception on disconnecting", e);
                Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "" + e.toString(), Toast.LENGTH_LONG).show();
            }
        });

        //bluetooth connect button
        connectSettingFragmentBinding.btnConnect.setOnClickListener(v -> {
            final ProgressDialog dialog = ProgressDialog.show(getContext(), getString(R.string.searching), getString(R.string.please_wait), true);
            dialog.setCancelable(false);
            new Handler().postDelayed(() -> {
                showPrinterPickDialog();
                dialog.dismiss();
            }, 5000);

        });

        //App To App Connect Button
        connectSettingFragmentBinding.btnAppToAppConnect.setOnClickListener(v -> {
            //Enable App To App
            GeneralParamCache.getInstance().putInt(Constant.ENABLE_APP_APP_COMMUNICATION, 1);

            navController.navigate(R.id.action_navigation_connect_setting_to_connectedFragment2);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ActiveTxnData.getInstance().setConnectPosition(position);
        logger.info("Selected Position" + position);
        selectedItem = parent.getItemAtPosition(position).toString();
        connectSettingViewModel.resetVisibilityOfViews(connectSettingFragmentBinding);
        connectSettingViewModel.getVisibilityOfViews(selectedItem);

        //Enable/Disable Local Host
        if (selectedItem.equals("AppToApp(LocalHost)")) {
            ActiveTxnData.getInstance().setLocalHostConnectionType(true);
            GeneralParamCache.getInstance().putString(Constant.IP_ADDRESS, "localhost");
            GeneralParamCache.getInstance().putString(Constant.PORT, "8888");


        } else if (selectedItem.equals("TC/IP")) {
            ActiveTxnData.getInstance().setLocalHostConnectionType(false);

            if (GeneralParamCache.getInstance().getString(Constant.IP_ADDRESS) != null && GeneralParamCache.getInstance().getString(Constant.IP_ADDRESS).equals("localhost")) {
                GeneralParamCache.getInstance().putString(Constant.IP_ADDRESS, null);
                GeneralParamCache.getInstance().putString(Constant.PORT, null);
                connectSettingFragmentBinding.ipAddress.getText().clear();
                connectSettingFragmentBinding.portNo.getText().clear();
            }
        } else if (selectedItem.equals("Bluetooth")) {
            ActiveTxnData.getInstance().setLocalHostConnectionType(false);
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "Bluetooth is not available!", Toast.LENGTH_SHORT).show();
            }

            if (!bluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
            }

        } else {
            ActiveTxnData.getInstance().setLocalHostConnectionType(false);
        }

    }

    private void showPrinterPickDialog() {
        dialog = new Dialog(getActivity());
        dialog.setCancelable(true);

        View view = Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(R.layout.layout_bluetooth, null);
        dialog.setContentView(view);
        dialog.setTitle("Bluetooth Devices");

        if (bluetoothAdapter.isDiscovering()) {
            Log.e("TAG", "doing");
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();

        //Initializing bluetooth adapters
        ArrayAdapter<String> pairedDevicesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        discoveredDevicesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);

        //locate listviews and attatch the adapters
        ListView listView = (ListView) dialog.findViewById(R.id.pairedDeviceList);
        ListView listView2 = (ListView) dialog.findViewById(R.id.discoveredDeviceList);
        listView.setAdapter(pairedDevicesAdapter);
        listView2.setAdapter(discoveredDevicesAdapter);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        Objects.requireNonNull(getActivity()).registerReceiver(discoveryFinishReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        Objects.requireNonNull(getActivity()).registerReceiver(discoveryFinishReceiver, filter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            pairedDevicesAdapter.add(getString(R.string.none_paired));
        }

        //Handling listview item click event
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            /*bluetoothAdapter.cancelDiscovery();*/
            String info = ((TextView) view1).getText().toString();
            final String address = info.substring(info.length() - 17);
            logger.info("address" + address);
            new Thread(() -> {
                try {
                    final int connectionStatus = connectToDevice(address);

                    requireActivity().runOnUiThread(() -> {
                        if (connectionStatus == 0) {
                            dialog.dismiss();
                            Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "Connected...", Toast.LENGTH_SHORT).show();
                            generalParamCache.putString(Constant.CONNECTION_STATUS, Constant.CONNECTED);
                            navController.navigate(R.id.action_navigation_connect_setting_to_connectedFragment2);
                        } else {
                            dialog.dismiss();
                            generalParamCache.putString(Constant.CONNECTION_STATUS, Constant.DISCONNECTED);
                            Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "Not able to connect", Toast.LENGTH_SHORT).show();
                        }
                    });


                } catch (IOException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }).start();

        });

        listView2.setOnItemClickListener((adapterView, view12, i, l) -> {
            /*bluetoothAdapter.cancelDiscovery();*/
            String info = ((TextView) view12).getText().toString();
            final String address = info.substring(info.length() - 17);
            new Thread(() -> {
                try {
                    final int connectionStatus = connectToDevice(address);
                    requireActivity().runOnUiThread(() -> {
                        if (connectionStatus == 0) {
                            dialog.dismiss();
                            Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "Connected... ", Toast.LENGTH_SHORT).show();
                            generalParamCache.putString(Constant.CONNECTION_STATUS, Constant.CONNECTED);
                            navController.navigate(R.id.action_navigation_connect_setting_to_connectedFragment2);
                        } else {
                            dialog.dismiss();
                            generalParamCache.putString(Constant.CONNECTION_STATUS, Constant.DISCONNECTED);
                            Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "Not able to connect", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (IOException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }).start();
        });

        dialog.findViewById(R.id.cancelButton).setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(false);
        dialog.show();
    }


    private int connectToDevice(String deviceAddress) throws IOException {
        ecrCore = ECRImpl.getConnectInstance();
        bluetoothAdapter.cancelDiscovery();
        connectingDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);
        logger.info("device" + connectingDevice);
        int connectionStatus = ecrCore.doBluetoothConnection(connectingDevice);
        ActiveTxnData.getInstance().setDevice(connectingDevice);
        return connectionStatus;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private final BroadcastReceiver discoveryFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            logger.info("ACtion" + action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                logger.info("bond state" + device.getBondState());
                if (Objects.requireNonNull(device).getBondState() != BluetoothDevice.BOND_BONDED) {
                    discoveredDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (discoveredDevicesAdapter.getCount() == 0) {
                    discoveredDevicesAdapter.add(getString(R.string.none_found));
                }
            }
        }
    };
}
