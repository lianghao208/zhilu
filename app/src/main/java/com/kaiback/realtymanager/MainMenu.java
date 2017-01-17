package com.kaiback.realtymanager;

import java.util.UUID;

/**
 * Created by Administrator on 2017/1/15.
 */

public class MainMenu {
    private UUID mId;
    private String mTitle;

    public MainMenu(){
        //生成唯一标识符
        this.mId = UUID.randomUUID();
    }

    public UUID getmId() {
        return mId;
    }

    public void setmId(UUID mId) {
        this.mId = mId;
    }

    public String getTitle(){
        return mTitle;
    }
    public void setmTitle(String title){
        this.mTitle = title;
    }
}
