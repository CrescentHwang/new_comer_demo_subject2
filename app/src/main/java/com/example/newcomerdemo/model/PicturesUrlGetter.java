package com.example.newcomerdemo.model;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.bumptech.glide.util.LogTime;
import com.example.newcomerdemo.PictureBigItemFragment;
import com.example.newcomerdemo.PictureListShowFragment;
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
    public static final int PICTURE_GETTING_TAG = 100;
    public static final int SUCCESSFUL_TAG = 101;
    public static final int FAILURE_TAG = 102;
    public static final int NO_PICTURE_TAG = 103;
    public static final int JSON_OBJECT_PARSE_ERROR = 104;
    public static final int JSON_OBJECT_TRANSLATE_ERROR = 105;
    public static final int REQUEST_ERROR = 106;


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


    public void getPicturesUrl(final Handler handler, final String url) {

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
                // 链接失败，传递给主线程
                failedGettingUrls(handler, PICTURE_GETTING_TAG, REQUEST_ERROR);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.i("TEST", "call() ----------------------------------------");
                Log.i("TEST", "request url = " + url);
                String responseBody = response.body().string();
                // 解析 JSON
                try {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    if(dataArray.length() > 0) {
                        ArrayList<PictureItem> items = new ArrayList<>();
                        mGson = new Gson();
                        for (int i = 0; i < dataArray.length(); i++) {
                            PictureItem item = mGson.fromJson(String.valueOf(dataArray.getJSONObject(i)), PictureItem.class);
                            items.add(item);
                            Log.i("TEST", item.toString());
                        }
                        // 数据对象解析成功，传递给主线程
                        Message message = new Message();
                        message.what = PICTURE_GETTING_TAG;
                        message.arg1 = SUCCESSFUL_TAG;
                        message.obj = items;
                        handler.sendMessage(message);
                        // 测试
                    } else {
                            failedGettingUrls(handler, PICTURE_GETTING_TAG, NO_PICTURE_TAG);
                    }
                    // 错误处理 JSON 解析错误
                } catch (JSONException e) {
                    failedGettingUrls(handler, PICTURE_GETTING_TAG, JSON_OBJECT_PARSE_ERROR);
                // 错误处理 JSON 对象转换错误
                } catch (JsonSyntaxException jsonSyncException) {
                    failedGettingUrls(handler, PICTURE_GETTING_TAG, JSON_OBJECT_TRANSLATE_ERROR);
                }
            }
        });
    }

    private void failedGettingUrls(Handler handler, int whatTag, int errInfoTag) {
        // 链接失败，传递给主线程
        Message message = new Message();
        message.what = whatTag;
        message.arg1 = FAILURE_TAG;
        message.arg2 = errInfoTag;
        handler.sendMessage(message);
    }
}
