package com.example.newcomerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newcomerdemo.adapter.PictureListShowRecyclerViewAdapter;
import com.example.newcomerdemo.model.PictureItem;
import com.example.newcomerdemo.model.PicturesUrlGetter;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class PictureListShowFragment extends Fragment
        implements PictureListShowRecyclerViewAdapter.OnItemClickListener{
    private RecyclerView mPictureListRecyclerView;
    private ArrayList<PictureItem> items = new ArrayList<>();
    private static final int GET_PICTURE_URL_TAG = 1000;
    private int mPhotoPage = 1;
    private int mPhontCount = 40;
//    private static final String URL = "https://gank.io/api/v2/data/category/Girl/type/Girl/page/1/count/40";

    private Handler mPicUrlGetterHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                // 获取 picture url 结束
                case GET_PICTURE_URL_TAG :
                    items.addAll((ArrayList<PictureItem>)msg.obj);
                    // 更新 UI
                    updateUI();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 销毁 fragment 视图，但不销毁 fragment 本身
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_picture_list_show, container, false);
        // 初始化 recyclerview
        mPictureListRecyclerView = v.findViewById(R.id.picture_list_recyclerview);
        mPictureListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        // 首次获取 picture url
        PicturesUrlGetter.getInstance().getPicturesUrl(mPicUrlGetterHandler, GET_PICTURE_URL_TAG, getUrl());

        mPictureListRecyclerView.setAdapter(new PictureListShowRecyclerViewAdapter(getContext(), items));

        // 初始化下拉刷新，上拉加载组件
        RefreshLayout refreshLayout = (RefreshLayout)v.findViewById(R.id.picture_list_refresh_layout);
        // 禁止下拉刷新
        refreshLayout.setEnableRefresh(false);
        // 设置上拉加载
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                PicturesUrlGetter.getInstance().getPicturesUrl(mPicUrlGetterHandler, GET_PICTURE_URL_TAG, getUrl());
                refreshlayout.finishLoadMore(1000);
            }
        });

        return v;
    }

    private void updateUI() {
        if(isAdded()) {
            mPictureListRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private String getUrl() {
        String url = "https://gank.io/api/v2/data/category/Girl/type/Girl/page/"+mPhotoPage+"/count/"+mPhontCount;
        mPhotoPage++;
        return url;
    }

    @Override
    public void onItemClick(View view, int position) {
        // todo! 点击跳转到图片放大页。
    }
}
