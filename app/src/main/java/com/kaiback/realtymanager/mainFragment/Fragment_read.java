package com.kaiback.realtymanager.mainFragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaiback.realtymanager.R;

/**
 * Created by Administrator on 2017/1/15.
 */

public class Fragment_read extends Fragment{
    private View view;
    private TextView tv_read;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //生成第一个页面的布局
        view = inflater.inflate(R.layout.fragment_read,container,false);
        tv_read = (TextView) view.findViewById(R.id.tv_read);
        tv_read.setText("读故事的Fragment");

        return view;
    }
}
