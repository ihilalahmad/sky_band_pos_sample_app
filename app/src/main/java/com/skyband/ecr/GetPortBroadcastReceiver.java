package com.skyband.ecr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.skyband.ecr.cache.GeneralParamCache;
import com.skyband.ecr.constant.Constant;

public class GetPortBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String portNo = intent.getStringExtra("PortNo");
        System.out.println("Getting Port No" + portNo);
        GeneralParamCache.getInstance().putString(Constant.PORT,portNo);
    }
}