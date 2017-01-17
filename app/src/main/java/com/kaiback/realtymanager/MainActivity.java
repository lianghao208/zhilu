package com.kaiback.realtymanager;

import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kaiback.realtymanager.ActivityManagerUtils.ActivityManagerUtils;
import com.kaiback.realtymanager.DIYVIew.MyViewPager;
import com.kaiback.realtymanager.DIYVIew.ViewPagerAdapter;
import com.kaiback.realtymanager.mainFragment.Fragment_person;
import com.kaiback.realtymanager.mainFragment.Fragment_read;
import com.kaiback.realtymanager.mainFragment.Fragment_write;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

//    private orderFragment order;
//    private searchFragment search;
//    private personFragment person;
    private RelativeLayout menu_read,menu_write,menu_person;
    private LinearLayout read_selected,write_selected,person_selected;
    private LinearLayout read_normal,write_normal,person_normal;
    private MyViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ArrayList<Fragment> fragmentList;
    //viewPager所在的页面数
    private int state=1;
    private float oldRatio=-1f;
    private boolean direction;
    private static final float OVERSPEED=0.5f;
    private int oracle;
    public static int screenWidth;
    public static int screenHeight;
    private boolean ifExit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //添加activity进入管理队列
        ActivityManagerUtils.getInstance().addActivity(this);

        DisplayMetrics metric=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth=metric.widthPixels;
        screenHeight=metric.heightPixels;
        init_view();

//        order = new orderFragment();
//        search = new searchFragment();
//        person = new personFragment();

        fragmentList = new ArrayList<Fragment>();
//        fragmentList.add(search);
//        fragmentList.add(order);
//        fragmentList.add(person);

        Fragment_read fragment_read = new Fragment_read();
        Fragment_write fragment_write = new Fragment_write();
        Fragment_person fragment_person = new Fragment_person();


        fragmentList.add(fragment_read);
        fragmentList.add(fragment_write);
        fragmentList.add(fragment_person);

        adapter=new ViewPagerAdapter(this.getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(2);
        init_menu();
        viewPager.setCurrentItem(0);
        //finish();
    }

    //把工具栏ActionBar添加到activity中
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_activity_actions, menu);

        //菜单栏搜索按钮显示
        MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_search),MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        //菜单栏写故事按钮显示
        MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_addstory),MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
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
    private void init_view(){
        viewPager = (MyViewPager) findViewById(R.id.main_ViewPager);
        menu_write = (RelativeLayout) findViewById(R.id.menu_write);
        menu_read = (RelativeLayout) findViewById(R.id.menu_read);
        menu_person = (RelativeLayout) findViewById(R.id.menu_person);
        read_selected = (LinearLayout) findViewById(R.id.read_selected);
        write_selected = (LinearLayout) findViewById(R.id.write_selected);
        person_selected = (LinearLayout) findViewById(R.id.person_selected);
        read_normal = (LinearLayout) findViewById(R.id.read_normal);
        write_normal = (LinearLayout) findViewById(R.id.write_normal);
        person_normal = (LinearLayout) findViewById(R.id.person_normal);
    }

    //初始化底栏tab
    private void init_menu(){
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
                    setWriteAlpha(1f-arg1);
                    setPersonAlpha(arg1);
                } else {
                    //往左滑动，arg1是从小到大，往右滑动，arg1是从大到小
                    setWriteAlpha(arg1);
                    setReadAlpha(1f-arg1);
                }
                break;
            case 2:
                setPersonAlpha(arg1);
                setWriteAlpha(1f - arg1);
                break;
        }
    }

    //判断当前所在页面的位置
    private void refreshTab(int state){
        switch (state){
            case 0:onRead();
                break;
            case 1:onWrite();
                break;
            case 2:onPerson();
                break;
        }
    }

    private void cleanMenu(){
        read_selected.setAlpha(0f);
        write_selected.setAlpha(0f);
        person_selected.setAlpha(0f);
        read_normal.setAlpha(1f);
        write_normal.setAlpha(1f);
        person_normal.setAlpha(1f);
    }

    //设置读故事tab透明度，ratio=1时显示，ratio=0时透明
    private void setReadAlpha(float ratio){
        read_selected.setAlpha(ratio);
        read_normal.setAlpha(1f-ratio);
    }

    //设置写故事tab透明度
    private void setWriteAlpha(float ratio){
        write_selected.setAlpha(ratio);
        write_normal.setAlpha(1f-ratio);
    }

    //设置我的tab透明度
    private void setPersonAlpha(float ratio){
        person_selected.setAlpha(ratio);
        person_normal.setAlpha(1f-ratio);
    }

    //显示读故事的Fragment及菜单选中后的界面
    private void onRead(){
        viewPager.setCurrentItem(0);
        read_selected.setAlpha(1f);
        read_normal.setAlpha(0f);
        state=0;
    }

    //显示写故事的Fragment及菜单选中后的界面
    private void onWrite(){
        viewPager.setCurrentItem(1);
        write_selected.setAlpha(1f);
        write_normal.setAlpha(0f);
        state=1;
    }

    //显示我的的Fragment及菜单选中后的界面
    private void onPerson(){
        viewPager.setCurrentItem(2);
        person_selected.setAlpha(1f);
        person_normal.setAlpha(0f);
        state=2;
    }

    @Override
    public void onPageSelected(int i) {
        // TODO Auto-generated method stub
        oracle = i ;

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
        if (ifExit){
            finish();
        }else{
            ifExit = true;
            Toast.makeText(getApplicationContext(),"再按一次退出",Toast.LENGTH_SHORT).show();
            Exit = new Timer();
            //ActivityManagerUtils.getInstance().exit();
            Exit.schedule(new TimerTask() {
                @Override
                public void run() {
                    ifExit = false;
                }
            },1500);
        }
    }


}
