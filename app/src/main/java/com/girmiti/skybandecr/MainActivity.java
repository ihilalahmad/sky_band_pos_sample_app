package com.girmiti.skybandecr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SocketHostConnector socketHostConnector;
    Button transactionButton;
    Button connectButton;
    Button disconnectButton;
    Spinner transactionTypeSpinner;
    Spinner connectionTypeSpinner;
    EditText ipEditText;
    EditText portNoEditText;
    EditText payAmtEditText;
    EditText cashBackAmtEditText;
    CheckBox printCheckBox;
    TextView responseTextView;
    EditText ecrRefNoEditText;

    String socketConnectionResponse;
    String ipAddress;
    int portNo;

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
                if (connectionTyp.equals("Socket")) {
                    ipAddress = ipEditText.getText().toString();
                    portNo = Integer.parseInt(portNoEditText.getText().toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("msg", "Entered");
                            socketHostConnector = new SocketHostConnector(ipAddress, portNo);
                            try {
                                socketConnectionResponse = socketHostConnector.createConnection();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        connectButton.setText("Connected");
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
                                Log.d("error>>", String.valueOf(e));
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else
                    Toast.makeText(getApplicationContext(), "Please select connection type as Socket", Toast.LENGTH_LONG).show();
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
                    connectButton.setText("connect");
                    Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_LONG).show();
                    socketHostConnector = null;

                } else
                    Toast.makeText(getApplicationContext(), "Socket is already Disconnected", Toast.LENGTH_LONG).show();
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
            String ecrReferenceNo = new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault()).format(new Date());
            String finalResponse;

            @Override
            public void onClick(View v) {
                payAmount = payAmtEditText.getText().toString();
                cashBackAmt = cashBackAmtEditText.getText().toString();
                System.out.println(ecrReferenceNo);
                if (printCheckBox.isSelected()) {
                    print = 1;
                }

                if (selectedItem.equals("purchase")) {

                    if (payAmount.trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "PayAmount not entered", Toast.LENGTH_LONG).show();
                        return;
                    }
                    tranType = 0;
                    reqData = payAmount + ";" + print + ";" + ecrReferenceNo + "!";

                } else if (selectedItem.equals("Purchase Cashback")) {
                    if (payAmount.trim().equals("") || cashBackAmt.trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "PayAmount or CashBack not Entered", Toast.LENGTH_LONG).show();
                        return;
                    }
                    tranType = 1;
                    reqData = payAmount + ";" + cashBackAmt + print + ";" + ecrReferenceNo + "!";

                } else if (selectedItem.equals("Settlement")) {
                    tranType = 10;
                    reqData = print + ";" + ecrReferenceNo + "!";

                } else {
                    Toast.makeText(getApplicationContext(), "Transaction Type Not Selected", Toast.LENGTH_LONG).show();
                    return;
                }

                if (socketHostConnector != null && socketHostConnector.socket.isConnected()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("msg", "Entered");
                            try {
                                finalResponse = socketHostConnector.tcpIpSend(reqData, tranType, szEcrBuffer);
                                Log.e("msg", "Got Response");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (finalResponse.isEmpty())
                                            Toast.makeText(getApplicationContext(), "Socket is not connected", Toast.LENGTH_LONG).show();
                                        else
                                            Toast.makeText(getApplicationContext(), "Response is: " + finalResponse, Toast.LENGTH_LONG).show();
                                    }
                                });
                                System.out.println(finalResponse);
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
                                e.printStackTrace();
                            }
                        }
                    }).start();

                } else
                    Toast.makeText(getApplicationContext(), "Socket is not connected ", Toast.LENGTH_LONG).show();
                //  Intent intent = new Intent(MainActivity.this, TransactionStatus.class);
                //startActivity(intent);
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}