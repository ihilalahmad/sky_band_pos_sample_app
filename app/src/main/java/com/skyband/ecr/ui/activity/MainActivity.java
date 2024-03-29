package com.skyband.ecr.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;
import android.os.Bundle;
import android.view.View;

import com.skyband.ecr.R;
import com.skyband.ecr.cache.GeneralParamCache;
import com.skyband.ecr.model.ActiveTxnData;
import com.skyband.ecr.sdk.ConnectionManager;
import com.skyband.ecr.sdk.logger.Logger;
import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    View view;
    Toolbar toolbar;
    private GeneralParamCache generalParamCache = GeneralParamCache.getInstance();
    private Logger logger = Logger.getNewLogger(MainActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.home_logo);
        view.setVisibility(View.INVISIBLE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ActiveTxnData.getInstance().getReceivedIntentData() == null) {
            generalParamCache.clear();
        }

        findViewById(R.id.left).setOnClickListener(v -> onBackPressed());

        findViewById(R.id.home_logo).setOnClickListener(v -> Navigation.findNavController(findViewById(R.id.nav_host_fragment)).navigate(R.id.settingFragment));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if(ConnectionManager.instance() != null){
                Objects.requireNonNull(ConnectionManager.instance()).disconnect();
            }
        } catch (IOException e) {
            logger.severe("Exception on disconnecting socket",e);
        }
    }
}