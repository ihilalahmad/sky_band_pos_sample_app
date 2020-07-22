package com.girmiti.skybandecr.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;
import android.os.Bundle;
import android.view.View;

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.cache.GeneralParamCache;
import com.girmiti.skybandecr.sdk.ConnectionManager;
import com.girmiti.skybandecr.sdk.logger.Logger;
import java.io.IOException;

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

        generalParamCache.clear();
        findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.home_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(findViewById(R.id.nav_host_fragment)).navigate(R.id.settingFragment);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        generalParamCache.clear();
        try {
            if(ConnectionManager.instance() != null){
                ConnectionManager.instance().disconnect();
            }
        } catch (IOException e) {
            logger.severe("Exception on disconnecting socket",e);
        }
    }
}