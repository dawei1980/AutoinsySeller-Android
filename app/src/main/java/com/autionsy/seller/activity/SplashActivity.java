package com.autionsy.seller.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.autionsy.seller.R;

public class SplashActivity extends BaseActivity {

    private SharedPreferences preferences;
    private boolean isFirstIn = true;//是否是第一次进入App

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    setPage();
                    break;
                case 1://第一次进入app
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);

        initView();
    }

    @SuppressLint("ApplySharedPref")
    private void initView() {

        //判断是否是第一次进入app
        preferences = getSharedPreferences("login", MODE_PRIVATE);
        isFirstIn = preferences.getBoolean("isFirstIn", true);
        if (isFirstIn) {
            handler.sendEmptyMessageDelayed(1, 1000);
            preferences.edit().putBoolean("isFirstIn", false).commit();
        } else {
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    }

    private void setPage(){
        preferences = getSharedPreferences("login_date", MODE_PRIVATE);
        String mUsername = preferences.getString("USERNAME","");
        String mPassword = preferences.getString("PASSWORD","");

        if(!"".equals(mUsername) && !"".equals(mPassword)){
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }
}
