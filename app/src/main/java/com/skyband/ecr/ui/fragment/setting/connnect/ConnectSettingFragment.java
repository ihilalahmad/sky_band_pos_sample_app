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
import android.os.Message;
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
import com.skyband.ecr.sdk.BluetoothConnectionManager;
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
    public static final String DEVICE_OBJECT = "device_name";
    private ArrayAdapter<String> discoveredDevicesAdapter;
    public static BluetoothConnectionManager bluetoothConnectionManager;
    private BluetoothDevice connectingDevice;

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_DEVICE_OBJECT = 4;
    public static final int MESSAGE_TOAST = 5;

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    //

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

        connectSettingViewModel = ViewModelProviders.of(this).get(ConnectSettingViewModel.class);

        setupListeners();

        return connectSettingFragmentBinding.getRoot();
    }

    @Override
    public void onStart() {
        connectSettingFragmentBinding.connectionTypeSpinner.setSelection(ActiveTxnData.getInstance().getConnectPosition());
        bluetoothConnectionManager = BluetoothConnectionManager.instance(handler);
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

        //bluetooth connect button
        connectSettingFragmentBinding.btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrinterPickDialog();
            }
        });

        connectSettingFragmentBinding.sendButton.setOnClickListener(new View.OnClickListener() {
            @SneakyThrows
            @Override
            public void onClick(View v) {
                sendMessage("fjhfehfhf");
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ActiveTxnData.getInstance().setConnectPosition(position);
        logger.info("Selected Position" + position);
        selectedItem = parent.getItemAtPosition(position).toString();
        connectSettingViewModel.resetVisibilityOfViews(connectSettingFragmentBinding);
        connectSettingViewModel.getVisibilityOfViews(selectedItem);

        if (selectedItem.equals("Bluetooth")) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "Bluetooth is not available!", Toast.LENGTH_SHORT).show();
            }

            btnEnableDisable_Discoverable();
        }

    }

    private void btnEnableDisable_Discoverable() {
        Log.d("TAG", "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            logger.info(">>handler msg:" + msg);
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case 3:
                            Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "Connected to: " + connectingDevice.getName(), Toast.LENGTH_SHORT).show();
                            generalParamCache.putString(Constant.CONNECTION_STATUS, Constant.CONNECTED);
                            break;
                        case 2:
                            Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "Connecting..." , Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                        case 0:
                            Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "Not connected" + connectingDevice.getName(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case MESSAGE_DEVICE_OBJECT:
                    connectingDevice = msg.getData().getParcelable(DEVICE_OBJECT);
                    logger.info("Connecting Device" + connectingDevice);
                    Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), "Connected to " + connectingDevice.getName(),
                            Toast.LENGTH_SHORT).show();
                    //Added for navigating after connection
                    navController.navigate(R.id.action_navigation_connect_setting_to_connectedFragment2);
                    break;

                case MESSAGE_TOAST:
                    Toast.makeText(Objects.requireNonNull(getContext()).getApplicationContext(), msg.getData().getString("toast"),
                            Toast.LENGTH_SHORT).show();
                    break;

            }
            return false;
        }
    });

    public static void sendMessage(String message) throws IOException {

        if (message.length() > 0) {
            byte[] send = message.getBytes();
            bluetoothConnectionManager.write(send);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bluetoothAdapter.cancelDiscovery();
                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);
                logger.info("address" + address);
                connectToDevice(address);
                dialog.dismiss();
            }

        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bluetoothAdapter.cancelDiscovery();
                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);

                connectToDevice(address);
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }


    private void connectToDevice(String deviceAddress) {
        ecrCore = ECRImpl.getConnectInstance();
        bluetoothAdapter.cancelDiscovery();
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        logger.info("device" + device);
        bluetoothConnectionManager.connect(device);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private final BroadcastReceiver discoveryFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
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
