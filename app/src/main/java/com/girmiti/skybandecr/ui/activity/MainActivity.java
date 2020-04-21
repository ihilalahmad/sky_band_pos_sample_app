package com.girmiti.skybandecr.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.girmiti.skybandecr.R;
import com.girmiti.skybandecr.cache.GeneralParamCache;
import com.girmiti.skybandecr.constant.Constant;
import com.girmiti.skybandecr.sdk.ConnectionManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements Constant {

    View view;
    Toolbar toolbar;
    private String activeFragment;
    private GeneralParamCache generalParamCache = GeneralParamCache.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.home_logo);
        view.setVisibility(View.INVISIBLE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
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
            ConnectionManager.Instance().disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

       activeFragment = GeneralParamCache.getInstance().getString(Active_Fragment);

       if(activeFragment!=null) {
           new AlertDialog.Builder(this)
                   .setMessage("Are you sure.. you want to exit?")
                   .setCancelable(false)
                   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           MainActivity.super.onBackPressed();
                       }
                   })
                   .setNegativeButton("No", null)
                   .show();
       }
       else
           super.onBackPressed();
       }
}