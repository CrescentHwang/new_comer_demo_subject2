package com.example.newcomerdemo.model;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PicturesUrlGetter {
    private static volatile PicturesUrlGetter mInstance;
    private Gson mGson;

    private PicturesUrlGetter() {}

    public static PicturesUrlGetter getInstance() {
        if(mInstance == null) {
            synchronized (PicturesUrlGetter.class) {
                if(mInstance == null) {
                    mInstance =  new PicturesUrlGetter();
                }
            }
        }
        return mInstance;
    }


    public void getPicturesUrl(final Handler handler, final int whatTag, final String url) {

        OkHttpClient okHttpClient = new OkHttpClient();

        // GET 请求
        final Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);

        // 异步调用
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // TODO 错误处理
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                // 解析 JSON
                try {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    ArrayList<PictureItem> items = new ArrayList<>();
                    mGson = new Gson();
                    for (int i = 0; i < dataArray.length(); i++) {
                        PictureItem item = mGson.fromJson(String.valueOf(dataArray.getJSONObject(i)), PictureItem.class);
                        items.add(item);
                        // 测试
                    }
                    // 数据对象解析完毕，传递给主线程
                    Message message = new Message();
                    message.what = whatTag;
                    message.obj = items;
                    handler.sendMessage(message);
                // TODO 错误处理 JSON 解析错误
                } catch (JSONException e) {
                    e.printStackTrace();
                // TODO 错误处理 JSON 对象转换错误
                } catch (JsonSyntaxException jsonSyncException) {
                    jsonSyncException.printStackTrace();
                }
            }
        });
    }
}
