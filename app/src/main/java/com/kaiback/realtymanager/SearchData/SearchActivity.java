package com.kaiback.realtymanager.SearchData;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kaiback.realtymanager.R;

public class SearchActivity extends AppCompatActivity {

    private TextView searchResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchResult = (TextView) findViewById(R.id.searchResult);
        //显示用户搜索的字符串
        //String queryString = intent.getStringExtra(SearchManager.QUERY); //获取搜索内容
        //searchResult.setText(queryString);
        //doSearchQuery(getIntent());
        searchResult.setText(getIntent().getStringExtra(SearchManager.QUERY));
    }


    @Override
    protected void onNewIntent(Intent intent) {  //activity重新置顶
        super.onNewIntent(intent);
        //setIntent(intent);
        doSearchQuery(intent);
    }


    private void doSearchQuery(Intent intent){
        searchResult = (TextView) findViewById(R.id.searchResult);
        if(intent == null)
            return;
        if( Intent.ACTION_SEARCH.equals( intent.getAction())){  //如果是通过ACTION_SEARCH来调用，即如果通过搜索调用

            String queryString = intent.getStringExtra(SearchManager.QUERY); //获取搜索内容
            searchResult.setText(queryString);
        }

    }
}
