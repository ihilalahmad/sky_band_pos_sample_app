package com.raj.skybandecr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    SocketHostConnector socketHostConnector;
    Button transactionButton;
    Button connectButton;
    Button disconnectButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transactionButton=findViewById(R.id.transaction_btn);
        connectButton=findViewById(R.id.connect_button);
        disconnectButton=findViewById(R.id.disconnect_button);

        transactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TransactionStatus.class);
                startActivity(intent);
            }
        });

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketHostConnector= new SocketHostConnector();
                try {
                    socketHostConnector.createConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketHostConnector.cleanup();
            }
        });

    }
}