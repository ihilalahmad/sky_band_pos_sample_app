package com.girmiti.skybandecr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.girmiti.skybandecr.sdk.SocketHostConnector;
import com.girmiti.skybandecr.sdk.logger.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Logger logger = Logger.getNewLogger(MainActivity.class.getName());

    private SocketHostConnector socketHostConnector;
    private Button transactionButton;
    private Button connectButton;
    private Button disconnectButton;
    private Spinner transactionTypeSpinner;
    private Spinner connectionTypeSpinner;
    private EditText ipEditText;
    private EditText portNoEditText;
    private EditText payAmtEditText;
    private EditText cashBackAmtEditText;
    private CheckBox printCheckBox;
    private TextView responseTextView;
    private EditText ecrRefNoEditText;

    private String socketConnectionResponse;
    private String ipAddress;
    private int portNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transactionButton = findViewById(R.id.transaction_btn);
        connectButton = findViewById(R.id.connect_button);
        disconnectButton = findViewById(R.id.disconnect_button);
        transactionTypeSpinner = findViewById(R.id.transaction_spinner);
        connectionTypeSpinner = findViewById(R.id.connection_type);
        ipEditText = findViewById(R.id.ip_address);
        portNoEditText = findViewById(R.id.port_no);
        payAmtEditText = findViewById(R.id.pay_amt);
        cashBackAmtEditText = findViewById(R.id.cash_back_amt);
        printCheckBox = findViewById(R.id.check_box_print);
        responseTextView = findViewById(R.id.response);
        ecrRefNoEditText = findViewById(R.id.ecr_ref_no);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.transaction_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transactionTypeSpinner.setAdapter(adapter);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String connectionTyp = connectionTypeSpinner.getSelectedItem().toString();

                if (connectionTyp.equals(getString(R.string.socket))) {

                    ipAddress = ipEditText.getText().toString();
                    String portNumber=portNoEditText.getText().toString();

                     final Pattern IP_ADDRESS
                            = Pattern.compile(
                            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                                    + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                                    + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                                    + "|[1-9][0-9]|[0-9]))");
                    Matcher matcher = IP_ADDRESS.matcher(ipAddress);

                    if (!matcher.matches()) {
                        Toast.makeText(getApplicationContext(), R.string.enter_valid_ip, Toast.LENGTH_LONG).show();

                        return;
                    }

                    if(portNumber.trim().equals("")){
                        Toast.makeText(getApplicationContext(), R.string.enter_port_no, Toast.LENGTH_LONG).show();

                        return;
                    }
                    portNo = Integer.parseInt(portNoEditText.getText().toString());


                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            socketHostConnector = new SocketHostConnector(ipAddress, portNo);

                            try {
                                socketConnectionResponse = socketHostConnector.createConnection();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        connectButton.setText(R.string.connected);
                                        Toast.makeText(getApplicationContext(), "" + socketConnectionResponse, Toast.LENGTH_LONG).show();
                                    }
                                });
                            } catch (final IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_LONG).show();
                                    }
                                });
                                logger.severe("IOException:Socket Connection",e);
                                e.printStackTrace();
                            }

                        }
                    }).start();
                } else
                    Toast.makeText(getApplicationContext(), R.string.select_connect_type_as_socket, Toast.LENGTH_LONG).show();
            }
        });

        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (socketHostConnector != null) {
                    try {
                        socketHostConnector.cleanup();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    connectButton.setText(R.string.connect);
                    Toast.makeText(getApplicationContext(), R.string.disconnected, Toast.LENGTH_LONG).show();
                    socketHostConnector = null;

                } else
                    Toast.makeText(getApplicationContext(), R.string.socket_disconnected, Toast.LENGTH_LONG).show();
            }
        });
        transactionTypeSpinner.setOnItemSelectedListener(this);
        connectionTypeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(final AdapterView<?> parent, View view, final int position, long id) {
        final String selectedItem = parent.getItemAtPosition(position).toString();

        transactionButton.setOnClickListener(new View.OnClickListener() {

            int tranType;
            int print = 0;
            String szEcrBuffer = "";
            String unpack = "";
            String reqData;
            String payAmount;
            String cashBackAmt;
            String ecrReferenceNo = new SimpleDateFormat(getString(R.string.date_pattern), Locale.getDefault()).format(new Date());
            String finalResponse;

            @Override
            public void onClick(View v) {
                payAmount = payAmtEditText.getText().toString();
                cashBackAmt = cashBackAmtEditText.getText().toString();
                System.out.println(ecrReferenceNo);
                if (printCheckBox.isSelected()) {
                    print = 1;
                }

                if (selectedItem.equals(getString(R.string.purchase))) {

                    if (payAmount.trim().equals("")) {
                        Toast.makeText(getApplicationContext(), R.string.pay_amt_not_entered, Toast.LENGTH_LONG).show();
                        return;
                    }
                    tranType = 0;
                    reqData = payAmount + ";" + print + ";" + ecrReferenceNo + "!";

                } else if (selectedItem.equals(getString(R.string.purchase_cashback))) {

                    if (payAmount.trim().equals("") || cashBackAmt.trim().equals("")) {
                        Toast.makeText(getApplicationContext(), R.string.payamt_cashback_not_entered, Toast.LENGTH_LONG).show();

                        return;
                    }
                    tranType = 1;
                    reqData = payAmount + ";" + cashBackAmt + print + ";" + ecrReferenceNo + "!";

                } else if (selectedItem.equals(getString(R.string.settlement))) {
                    tranType = 10;
                    reqData = print + ";" + ecrReferenceNo + "!";

                } else {
                    Toast.makeText(getApplicationContext(), R.string.transaction_type_not_selected, Toast.LENGTH_LONG).show();

                    return;
                }

                if (socketHostConnector != null && socketHostConnector.socket.isConnected()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                finalResponse = socketHostConnector.tcpIpSend(reqData, tranType, szEcrBuffer);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (finalResponse.isEmpty())
                                            Toast.makeText(getApplicationContext(), R.string.soccet_not_connected, Toast.LENGTH_LONG).show();
                                        else
                                            Toast.makeText(getApplicationContext(), getString(R.string.response) + finalResponse, Toast.LENGTH_LONG).show();
                                    }
                                });

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (finalResponse.contains("ü")) {
                                            String[] array = finalResponse.split("ü");

                                            for (int i = 0; i < array.length; i++)
                                                unpack = "" + (array[i]);
                                            responseTextView.setText(unpack);

                                        } else
                                            responseTextView.setText(finalResponse);
                                    }
                                });
                            } catch (IOException e) {
                                logger.severe("IOException:Sending Data >>>",e);
                            }
                        }
                    }).start();

                } else
                    Toast.makeText(getApplicationContext(), R.string.soccet_not_connected, Toast.LENGTH_LONG).show();
               ///////////////TO DO//////////////////
                //  Intent intent = new Intent(MainActivity.this, TransactionStatus.class);
                //startActivity(intent);
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}