package com.kaiback.realtymanager.Login;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by dell on 2016/7/26.
 */
public class UserControl {

    private static Context mcontext;

    public static void LoginAgain(Context context){

            mcontext = context;
            //用户配置文件绝对路径
            String url = "";
            JSONObject json=new JSONObject();

            try{
                json.put("account",RsSharedUtil.getString(mcontext, "account"));
                json.put("password",RsSharedUtil.getString(mcontext, "password"));
                //获取手机物理识别码
                TelephonyManager tm = (TelephonyManager)mcontext.getSystemService(Context.TELEPHONY_SERVICE);
                String DEVICE_ID = tm.getDeviceId();
                json.put("recognizeCode",DEVICE_ID);
            }catch(Exception e){
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest
                    =new JsonObjectRequest(url, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("loginagain",response.toString());
                    try {
                        String result = response.getString("result");
                        switch (result){
                            case "success":
                                RsSharedUtil.putString(mcontext, "code", response.getString("code"));
                                RsSharedUtil.putString(mcontext, "account", response.getString("account"));
                                RsSharedUtil.putString(mcontext,"stuPhoto",response.getString("stuPhoto"));
                                RsSharedUtil.putInt(mcontext, "area", response.getInt("area"));
                                RsSharedUtil.putString(mcontext, "phoneNumber", response.getString("phoneNumber"));
                                RsSharedUtil.putString(mcontext,"stuName",response.getString("stuName"));
                                RsSharedUtil.putInt(mcontext,"stuId",response.getInt("stuId"));
                                RsSharedUtil.putBoolean(mcontext, "online", true);
                                RsSharedUtil.putInt(mcontext,"identity",response.getInt("identity"));

                                Toast.makeText(mcontext, "重新登录成功",Toast.LENGTH_LONG).show();

                                break;
                            case "no such a student":
                            case "wrongformat":
                                Toast.makeText(mcontext,"用户名或密码错误！",Toast.LENGTH_LONG).show();
                                RsSharedUtil.putString(mcontext, "account", "");
                                RsSharedUtil.putString(mcontext,"password", "");
                                break;
                            case "alreadylogined":
                            case "failedsave":
                            case "duplicate":
                                Toast.makeText(mcontext,"账户已经登录！请稍后重试！",Toast.LENGTH_SHORT).show();
                                break;                        }

                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                        Toast.makeText(mcontext, "数据解析失败",Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        Toast.makeText(mcontext, "服务器异常，稍等片刻后重试",Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }}
            );
            ApplicationController.getRequestQueue().add(jsonObjectRequest);
        }
    }

