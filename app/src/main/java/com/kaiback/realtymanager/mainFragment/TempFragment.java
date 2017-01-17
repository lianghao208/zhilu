package com.kaiback.realtymanager.mainFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaiback.realtymanager.R;

/**
 * Created by 595056078 on 2016/11/20.
 */

public class TempFragment extends Fragment {

    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_temp, container, false);


        TextView textView;
        textView = (TextView) view.findViewById(R.id.text);
        textView.setText("测试用Fragment");

        return view;
    }
}
