package com.kaiback.realtymanager.mainFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kaiback.realtymanager.ActivityManagerUtils.ActivityManagerUtils;
import com.kaiback.realtymanager.Login.ApplicationController;
import com.kaiback.realtymanager.Login.LoginActivity;
import com.kaiback.realtymanager.Login.RsSharedUtil;
import com.kaiback.realtymanager.MainActivity;
import com.kaiback.realtymanager.R;

/**
 * Created by Administrator on 2017/1/15.
 */

public class Fragment_person extends Fragment{
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //生成第一个页面的布局
        view = inflater.inflate(R.layout.fragment_person,container,false);
        TextView tv_person;
        tv_person = (TextView) view.findViewById(R.id.tv_person);
        tv_person.setText("我的Fragment");

        Button logout;
        logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RsSharedUtil.clear(ApplicationController.getInstance());
                Toast.makeText(ApplicationController.getInstance(),"注销成功",Toast.LENGTH_SHORT);
                Intent intent = new Intent();
                intent.setClass(ApplicationController.getInstance(), LoginActivity.class);
                startActivity(intent);
                ActivityManagerUtils.getInstance().finishActivityclass(MainActivity.class);

            }
        });
        return view;
    }
}
