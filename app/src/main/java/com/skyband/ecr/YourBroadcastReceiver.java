package com.skyband.ecr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.skyband.ecr.cache.GeneralParamCache;
import com.skyband.ecr.constant.Constant;

public class YourBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String portNo = intent.getStringExtra("PortNo");
        Toast.makeText(context, "Data from AppB"+"  "+portNo, Toast.LENGTH_SHORT).show();
       /* String portNo = intent.getExtras().getString("port");
        System.out.println("Getting Port No" + portNo);
        GeneralParamCache.getInstance().putString(Constant.PORT, portNo);*/
    }
}