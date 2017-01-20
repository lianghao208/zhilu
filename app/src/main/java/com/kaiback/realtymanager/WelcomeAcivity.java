package com.kaiback.realtymanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.kaiback.realtymanager.ActivityManagerUtils.ActivityManagerUtils;
import com.kaiback.realtymanager.Login.ApplicationController;
import com.kaiback.realtymanager.Login.LoginActivity;
import com.kaiback.realtymanager.Login.RsSharedUtil;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeAcivity extends AppCompatActivity {

    private ImageView welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_activity);
        //添加activity进入管理队列
        ActivityManagerUtils.getInstance().addActivity(this);

        /*
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

       */
        //设置欢迎页面，两秒后记进入主界面
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                String userData = RsSharedUtil.getString(ApplicationController.getInstance(),"user");
                //欢迎页面，从userData里读取用户登录信息，若读取正确，则直接进入主菜单，不用再登陆
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
        };
        timer.schedule(task,1000*2);

    }

}
