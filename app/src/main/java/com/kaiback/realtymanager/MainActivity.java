package com.kaiback.realtymanager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kaiback.realtymanager.ActivityManagerUtils.ActivityManagerUtils;
import com.kaiback.realtymanager.DIYVIew.MyViewPager;
import com.kaiback.realtymanager.DIYVIew.ViewPagerAdapter;
import com.kaiback.realtymanager.Login.ApplicationController;
import com.kaiback.realtymanager.Login.RsSharedUtil;
import com.kaiback.realtymanager.SearchData.QuerySuggestionsAdapter;
import com.kaiback.realtymanager.SearchData.SearchActivity;
import com.kaiback.realtymanager.mainFragment.Fragment_favorite;
import com.kaiback.realtymanager.mainFragment.Fragment_person;
import com.kaiback.realtymanager.mainFragment.Fragment_read;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


//import android.widget.CursorAdapter;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener,SearchView.OnSuggestionListener,SearchView.OnQueryTextListener {

    //    private orderFragment order;
//    private searchFragment search;
//    private personFragment person;
    private RelativeLayout menu_read, menu_write, menu_person;
    private LinearLayout read_selected, write_selected, person_selected;
    private LinearLayout read_normal, write_normal, person_normal;
    private MyViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ArrayList<Fragment> fragmentList;
    //viewPager所在的页面数
    private int state = 1;
    private float oldRatio = -1f;
    private boolean direction;
    private static final float OVERSPEED = 0.5f;
    private int oracle;
    public static int screenWidth;
    public static int screenHeight;
    private boolean ifExit = false;

    private CursorAdapter mSuggestionsAdapter;
    private static final String[] COLUMNS = { BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1, };
    MatrixCursor cursor = new MatrixCursor(COLUMNS);
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //添加activity进入管理队列
        ActivityManagerUtils.getInstance().addActivity(this);

        //显示菜单栏
        setOverflowShowingAlways();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;
        screenHeight = metric.heightPixels;
        init_view();
        //显示主界面的Fragment和ViewPager
        setFragment();
        //对用户在搜索框输入的字符串进行处理
        handleIntent(getIntent());

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void setFragment() {
        fragmentList = new ArrayList<Fragment>();

        Fragment_read fragment_read = new Fragment_read();
        Fragment_favorite fragment_favorite = new Fragment_favorite();
        Fragment_person fragment_person = new Fragment_person();


        fragmentList.add(fragment_read);
        fragmentList.add(fragment_favorite);
        fragmentList.add(fragment_person);

        adapter = new ViewPagerAdapter(this.getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(2);
        init_menu();
        viewPager.setCurrentItem(0);

    }

    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    //用户点击搜索后的操作
    private void doMySearch(String query) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.putExtra(SearchManager.QUERY, query);
        RsSharedUtil.putString(this, "searchHistory", query);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }
    //添加suggestion需要的数据
    public Cursor getTestCursor() {

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
                this.getFilesDir() + "/my.db3", null);

        Cursor cursor = null;
        try {

            String insertSql = "insert into tb_test values (null,?,?)";
            String deleteSql = "delete from tb_test where name=?";
            //db.execSQL(deleteSql, new Object[]{"ab"});
            db.execSQL(insertSql, new Object[]{RsSharedUtil.getString(this, "searchHistory"), null});

            String querySql = "select * from tb_test";

            cursor = db.rawQuery(querySql, null);

        } catch (Exception e) {

            String sql = "create table tb_test (_id integer primary key autoincrement,tb_name varchar(20),tb_age integer)";

            db.execSQL(sql);

            String insertSql = "insert into tb_test values (null,?,?)";

            String deleteSql = "delete from tb_test where name=?";

            //db.execSQL(deleteSql, new Object[]{"ab"});
            //db.delete("tb_test",null,null);
            db.execSQL(insertSql, new Object[]{RsSharedUtil.getString(this, "searchHistory"), null});



            String querySql = "select * from tb_test";

            cursor = db.rawQuery(querySql, null);
        }

        return cursor;
    }

    //把工具栏ActionBar添加到activity中
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_activity_actions, menu);
        //菜单栏搜索按钮显示
        MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_search), MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        //菜单栏写故事按钮显示
        MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_addstory), MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

        /*
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchView == null) {
            Log.e("SearchView", "Fail to get Search View.");
            return true;
        }
        searchView.setIconifiedByDefault(true); // 缺省值就是true，可能不专门进行设置，false和true的效果图如下，true的输入框更大

        searchView.setIconified(true);//添加下面一句,防止数据两次加载
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView();
        searchView.setQueryHint("请输入要搜索的文章");
        // 获取搜索服务管理器
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //获取用户输入的搜索数据，在新的activity中onCreat方法中通过intent.getStringExtra(SearchManager.QUERY); 就能得到用户输入的搜索字符。
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //显示自动填充搜索框
        searchView.setQueryRefinementEnabled(true);
        //自定义搜索提示框
        Cursor cursor = this.getTestCursor();

        */
        //@SuppressWarnings("deprecation")
        // SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.searchview_item, cursor, new String[]{"tb_name"}, new int[]{R.id.search_list});


        //searchView.setSuggestionsAdapter(adapter);
        createSearchItem(menu);
        return super.onCreateOptionsMenu(menu);
    }



    private void createSearchItem(Menu menu) {


        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconifiedByDefault(true); // 缺省值就是true，可能不专门进行设置，false和true的效果图如下，true的输入框更大
        searchView.setIconified(true);//添加下面一句,防止数据两次加载
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView();
        searchView.setQueryHint("请输入要搜索的文章");
        // 获取搜索服务管理器
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //获取用户输入的搜索数据，在新的activity中onCreat方法中通过intent.getStringExtra(SearchManager.QUERY); 就能得到用户输入的搜索字符。
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //显示自动填充搜索框
        searchView.setQueryRefinementEnabled(true);
        //自定义搜索提示框
        //Cursor cursor = this.getTestCursor();
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);


        if (mSuggestionsAdapter == null) {

            cursor.addRow(new String[] { "1", "Murica" });
            cursor.addRow(new String[] { "2", "Canada" });
            cursor.addRow(new String[] { "3", "Denmark" });
            //cursor.addRow(new String[] { "4",RsSharedUtil.getString(this, "searchHistory")});
            mSuggestionsAdapter = new QuerySuggestionsAdapter(ApplicationController.getInstance(), cursor);
        }

        searchView.setSuggestionsAdapter(mSuggestionsAdapter);

        //MenuItem searchItem = menu.add(0, 0, 0, "search");
        //searchItem.setIcon(R.drawable.abs__ic_search);
        //searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        //searchItem.setActionView(searchView);
    }
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //对菜单的点击实现响应
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //若点击搜索菜单则调用搜索函数
                //openSearch();
                return true;
            case R.id.action_addstory:
                //若点击写故事菜单则调用设置函数
                //openAddstory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //对页面控件的初始化
    private void init_view() {
        viewPager = (MyViewPager) findViewById(R.id.main_ViewPager);
        menu_write = (RelativeLayout) findViewById(R.id.menu_favorite);
        menu_read = (RelativeLayout) findViewById(R.id.menu_read);
        menu_person = (RelativeLayout) findViewById(R.id.menu_person);
        read_selected = (LinearLayout) findViewById(R.id.read_selected);
        write_selected = (LinearLayout) findViewById(R.id.favorite_selected);
        person_selected = (LinearLayout) findViewById(R.id.person_selected);
        read_normal = (LinearLayout) findViewById(R.id.read_normal);
        write_normal = (LinearLayout) findViewById(R.id.favorite_normal);
        person_normal = (LinearLayout) findViewById(R.id.person_normal);
    }

    //初始化底栏tab
    private void init_menu() {
        read_selected.setAlpha(1f);

        menu_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRead();
            }
        });

        menu_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWrite();
            }
        });

        menu_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPerson();
            }
        });
    }

    //arg0 :当前页面，及你点击滑动的页面，=0什么都没做，=1正在滑动，=2完成滑动。
    //arg1:当前页面偏移的百分比。
    // arg2:当前页面偏移的像素位置。
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
        //若未初始化，则跳过超速判断
        if (oldRatio != -1f) {

            //超速判断，若超，则根据方向纠正state
            if (
                    (Math.abs((arg1 - oldRatio)) > OVERSPEED)
                            && (oldRatio != 0f)) {
                if (direction) {
                    state = arg0;
                } else state = arg0 + 1;

            }
        }
        //记录历史方向，放在超速判断之后，这个位置很重要
        direction = state == arg0;
        if (arg1 == 0f) {
            cleanMenu();
            refreshTab(oracle);
            return;
        }
        oldRatio = arg1;
        //判断是第几个页面，设置菜单栏透明度
        switch (state) {
            case 0:
                setReadAlpha(1f - arg1);
                setWriteAlpha(arg1);
                break;
            case 1:
                if (arg0 > 0) {
                    setWriteAlpha(1f - arg1);
                    setPersonAlpha(arg1);
                } else {
                    //往左滑动，arg1是从小到大，往右滑动，arg1是从大到小
                    setWriteAlpha(arg1);
                    setReadAlpha(1f - arg1);
                }
                break;
            case 2:
                setPersonAlpha(arg1);
                setWriteAlpha(1f - arg1);
                break;
        }
    }

    //判断当前所在页面的位置
    private void refreshTab(int state) {
        switch (state) {
            case 0:
                onRead();
                break;
            case 1:
                onWrite();
                break;
            case 2:
                onPerson();
                break;
        }
    }

    private void cleanMenu() {
        read_selected.setAlpha(0f);
        write_selected.setAlpha(0f);
        person_selected.setAlpha(0f);
        read_normal.setAlpha(1f);
        write_normal.setAlpha(1f);
        person_normal.setAlpha(1f);
    }

    //设置读故事tab透明度，ratio=1时显示，ratio=0时透明
    private void setReadAlpha(float ratio) {
        read_selected.setAlpha(ratio);
        read_normal.setAlpha(1f - ratio);
    }

    //设置写故事tab透明度
    private void setWriteAlpha(float ratio) {
        write_selected.setAlpha(ratio);
        write_normal.setAlpha(1f - ratio);
    }

    //设置我的tab透明度
    private void setPersonAlpha(float ratio) {
        person_selected.setAlpha(ratio);
        person_normal.setAlpha(1f - ratio);
    }

    //显示读故事的Fragment及菜单选中后的界面
    private void onRead() {
        viewPager.setCurrentItem(0);
        read_selected.setAlpha(1f);
        read_normal.setAlpha(0f);
        state = 0;
    }

    //显示写故事的Fragment及菜单选中后的界面
    private void onWrite() {
        viewPager.setCurrentItem(1);
        write_selected.setAlpha(1f);
        write_normal.setAlpha(0f);
        state = 1;
    }

    //显示我的的Fragment及菜单选中后的界面
    private void onPerson() {
        viewPager.setCurrentItem(2);
        person_selected.setAlpha(1f);
        person_normal.setAlpha(0f);
        state = 2;
    }

    @Override
    public void onPageSelected(int i) {
        // TODO Auto-generated method stub
        oracle = i;

    }

    @Override
    public void onPageScrollStateChanged(int i) {
        // TODO Auto-generated method stub
    }

    //检测手机返回键是否按下，按下两次后退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByTwoClick();
        }
        return false;
    }

    //按下两次后退出程序
    private void exitByTwoClick() {
        Timer Exit = null;
        if (ifExit) {
            finish();
        } else {
            ifExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
            Exit = new Timer();
            //ActivityManagerUtils.getInstance().exit();
            Exit.schedule(new TimerTask() {
                @Override
                public void run() {
                    ifExit = false;
                }
            }, 1500);
        }
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    //搜索栏提示下拉菜单选择接口
    @Override
    public boolean onSuggestionSelect(int position) {
        //Toast.makeText(this,position,Toast.LENGTH_LONG).show();
        return false;
    }

    //搜索栏提示下拉提示菜单选择接口
    @Override
    public boolean onSuggestionClick(int position) {
        Cursor c = (Cursor) mSuggestionsAdapter.getItem(position);
        String query = c.getString(c.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
        doMySearch(query);
        /*
        if(query.equals("清除历史搜索记录")){
            RsSharedUtil.putString(this,"searchHistory",null);
            cursor.addRow(new String[] {"1" ,"清除历史搜索记录" });
            Toast.makeText(this,query,Toast.LENGTH_LONG).show();
        }else{
            //对用户搜索的内容进行处理
            doMySearch(query);
        }*/

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //对用户在搜索框输入的字符串进行处理

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
