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

public class Fragment_write extends Fragment{
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //生成第一个页面的布局
        view = inflater.inflate(R.layout.fragment_write,container,false);
        TextView tv_write;
        tv_write = (TextView) view.findViewById(R.id.tv_write);
        tv_write.setText("写故事的Fragment");

        return view;
    }
}
