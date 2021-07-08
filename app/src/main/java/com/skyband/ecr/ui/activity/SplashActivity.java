package com.skyband.ecr.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.skyband.ecr.R;
import com.skyband.ecr.YourBroadcastReceiver;
import com.skyband.ecr.sdk.logger.Logger;
public class SplashActivity extends AppCompatActivity {

    private Logger logger = Logger.getNewLogger(SplashActivity.class.getName());

    private Handler handler = new Handler();
    private YourBroadcastReceiver MyReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            }
        }, 1000);
    }

}
