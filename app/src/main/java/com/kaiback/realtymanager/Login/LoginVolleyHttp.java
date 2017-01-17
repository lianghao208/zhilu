package com.kaiback.realtymanager.Login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.kaiback.realtymanager.ActivityManagerUtils.ActivityManagerUtils;
import com.kaiback.realtymanager.MainActivity;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

//import static android.support.v4.content.ContextCompat.startActivity;

//import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by Administrator on 2017/1/15.
 */

public class LoginVolleyHttp {
    private String mUrl;
    private Context mContext;
    private String mUsername;
    private String mPassword;
    private int comfirm;

    public LoginVolleyHttp(Context context, String url, String username, String password) {
        mContext = context;
        mUrl = url;
        mUsername = username;
        mPassword = password;
    }

    //public RequestQueue mQueue = Volley.newRequestQueue(null);
    public void stringRequestWithPost(final Context context) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    try {
                        response=new String(response.trim().getBytes("iso-8859-1"),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
// TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String resultbak=null;
                    try {
                        resultbak=new String("ok".getBytes("iso-8859-1"),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
// TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if(response.equals(resultbak)){
                        Log.e("loginagain","ok");
                        RsSharedUtil.putString(context,"user",resultbak);
                        //跳转页面
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        ActivityManagerUtils.getInstance().finishActivityclass(LoginActivity.class);


                    }else{
                        Log.e("loginagain","error");
                        RsSharedUtil.putString(context,"user","error");
                        Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();

                    }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "VolleyError:" + error.toString());
                try {
                    byte[] htmlBodyBytes = error.networkResponse.data;
                    Log.e(TAG, new String(htmlBodyBytes), error);
                    Toast.makeText(ApplicationController.getInstance(), "网络连接失败,请检查您的网络", Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", mUsername);
                map.put("password", mPassword);
                return map;
            }

        };
        stringRequest.setTag("String");
        //加入队列
        ApplicationController.getRequestQueue().add(stringRequest);
        //Log.e("isComfirm",String.valueOf(comfirm));

    }

    public void JsonRequestWithPost() {
        //mcontext = context;
        //String url = AppConfig.URL_LOGIN;
        JSONObject json = new JSONObject();
        /*
        try{
            json.put("account",RsSharedUtil.getString(mcontext, "account"));
            json.put("password",RsSharedUtil.getString(mcontext, "password"));
            //获取手机物理识别码
            TelephonyManager tm = (TelephonyManager)mcontext.getSystemService(Context.TELEPHONY_SERVICE);
            String DEVICE_ID = tm.getDeviceId();
            json.put("recognizeCode",DEVICE_ID);
        }catch(Exception e){
            e.printStackTrace();
        }*/
        JsonObjectRequest jsonObjectRequest
                = new JsonObjectRequest(mUrl, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("loginagain", response.toString());
                try {
                    String result = response.getString("result");
                    switch (result) {
                    /*
                    case "success":
                        RsSharedUtil.putString(mcontext, "code", response.getString("code"));
                        RsSharedUtil.putString(mcontext, "account", response.getString("account"));
                        RsSharedUtil.putString(mcontext, "stuPhoto", response.getString("stuPhoto"));
                        RsSharedUtil.putInt(mcontext, "area", response.getInt("area"));
                        RsSharedUtil.putString(mcontext, "phoneNumber", response.getString("phoneNumber"));
                        RsSharedUtil.putString(mcontext, "stuName", response.getString("stuName"));
                        RsSharedUtil.putInt(mcontext, "stuId", response.getInt("stuId"));
                        RsSharedUtil.putBoolean(mcontext, "online", true);
                        RsSharedUtil.putInt(mcontext, "identity", response.getInt("identity"));

                        Toast.makeText(mcontext, "重新登录成功", Toast.LENGTH_LONG).show();

                        break;
                    case "no such a student":
                    case "wrongformat":
                        Toast.makeText(mcontext, "用户名或密码错误！", Toast.LENGTH_LONG).show();
                        RsSharedUtil.putString(mcontext, "account", "");
                        RsSharedUtil.putString(mcontext, "password", "");
                        break;
                    case "alreadylogined":
                    case "failedsave":
                    case "duplicate":
                        Toast.makeText(mcontext, "账户已经登录！请稍后重试！", Toast.LENGTH_SHORT).show();
                        break;
                        */
                    }

                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    //Toast.makeText(mcontext, "数据解析失败", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    //Toast.makeText(mcontext, "服务器异常，稍等片刻后重试", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        );
        ApplicationController.getRequestQueue().add(jsonObjectRequest);
    }


}
