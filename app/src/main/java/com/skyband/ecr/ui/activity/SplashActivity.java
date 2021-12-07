package com.skyband.ecr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.skyband.ecr.R;
import com.skyband.ecr.model.ActiveTxnData;
import com.skyband.ecr.sdk.logger.Logger;

public class SplashActivity extends AppCompatActivity {

    private Logger logger = Logger.getNewLogger(SplashActivity.class.getName());

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        byte[] receivedDataByte = getIntent().getByteArrayExtra("app-to-app-response");

        if (receivedDataByte != null && receivedDataByte.length > 0) {
            String receivedIntentData = new String(receivedDataByte);
            logger.info("Received ECR Response >>" + receivedIntentData);
            receivedIntentData = receivedIntentData.replace("�", ";");
            ActiveTxnData.getInstance().setReceivedIntentData(receivedIntentData);

        }

        handler.postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            SplashActivity.this.finish();
        }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logger.info("Inside Splash Activity onActivityResult");
    }
}
