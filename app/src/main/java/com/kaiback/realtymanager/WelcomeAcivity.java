package com.kaiback.realtymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.kaiback.realtymanager.ActivityManagerUtils.ActivityManagerUtils;
import com.kaiback.realtymanager.Login.ApplicationController;
import com.kaiback.realtymanager.Login.LoginActivity;
import com.kaiback.realtymanager.Login.RsSharedUtil;

public class WelcomeAcivity extends AppCompatActivity {

    private ImageView welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_acivity);
        //添加activity进入管理队列
        ActivityManagerUtils.getInstance().addActivity(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        welcome = (ImageView) findViewById(R.id.welcome);
        String userData = RsSharedUtil.getString(ApplicationController.getInstance(),"user");
        if(!userData.equals("ok")){
            Intent intent = new Intent();
            intent.setClass(WelcomeAcivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        }else{
            Intent intent = new Intent();
            intent.setClass(WelcomeAcivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        }
    }

}
