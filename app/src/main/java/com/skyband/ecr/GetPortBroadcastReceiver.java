package com.skyband.ecr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class GetPortBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String portNo = intent.getStringExtra("PortNo");
        System.out.println("Getting Port No" + portNo);
        Toast.makeText(context, "Port from AppB"+"  "+portNo, Toast.LENGTH_SHORT).show();
    }
}