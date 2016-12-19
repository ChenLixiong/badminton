package com.ymq.badminton_rank.utils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.ymq.badminton_rank.activity.MyApplication;

import org.json.JSONObject;

/**
 * Created by chenlixiong on 2016/12/12.
 */

public class HttpUtil {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static void postJson(String url, JSONObject json , Callback callback) {
        OkHttpClient okHttpClient = MyApplication.getOkHttpInstance();
        RequestBody requestBody = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
