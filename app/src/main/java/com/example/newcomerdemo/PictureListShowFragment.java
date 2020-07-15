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

public class PictureListShowFragment extends Fragment {
    private RecyclerView mPictureListRecyclerView;
    private ArrayList<PictureItem> items = new ArrayList<>();
    private RefreshLayout mRefreshLayout;
    private static final int SUCCEED_GETTING_TAG = 1000;
    private int mPhotoPage = 1;
    private int mPhontCount = 40;
    // TODO 结束时清理消息队列
    private static Handler mPicUrlGetterHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 销毁 fragment 视图，但不销毁 fragment 本身
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_picture_list_show, container, false);
        // 实例化 recyclerview
        mPictureListRecyclerView = v.findViewById(R.id.picture_list_recyclerview);
        mPictureListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        // 实例化 handler
        mPicUrlGetterHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    // 获取图片列表信息 成功
                    case SUCCEED_GETTING_TAG :
                        dealWithItems(msg);
                        break;
                    default:
                        break;
                }
            }
        };
        // 实例化上下拉刷新组件
        mRefreshLayout = (RefreshLayout)v.findViewById(R.id.picture_list_refresh_layout);
        // 初始化组件
        initUI();
        // 首次获取 picture url
        PicturesUrlGetter.getInstance().getPicturesUrl(mPicUrlGetterHandler, SUCCEED_GETTING_TAG, getUrl());
        return v;
    }

    // 获取图片列表信息的 url
    private String getUrl() {
        String url = "https://gank.io/api/v2/data/category/Girl/type/Girl/page/"+mPhotoPage+"/count/"+mPhontCount;
        mPhotoPage++;
        return url;
    }

    // 获取图片列表信息成功后的处理
    private void dealWithItems(Message msg) {
        items.addAll((ArrayList<PictureItem>)msg.obj);
        // 更新 adapter
        updateAdapter();
    }

    // 更新 recyclerview 的 adapter
    private void updateAdapter() {
        if (isAdded()) {
            mPictureListRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    // 初始化UI组件
    private void initUI() {
        PictureListShowRecyclerViewAdapter adapter = new PictureListShowRecyclerViewAdapter(getContext(), items);
        // adapter 设置点击事件
        adapter.setOnItemClickListener(new PictureListShowRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(PictureBigActivity.newIntent(getActivity(), items, position));
            }
        });
        mPictureListRecyclerView.setAdapter(adapter);
        // 禁止下拉刷新
        mRefreshLayout.setEnableRefresh(false);
        // 设置上拉加载
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                PicturesUrlGetter.getInstance().getPicturesUrl(mPicUrlGetterHandler, SUCCEED_GETTING_TAG, getUrl());
                refreshlayout.finishLoadMore(1000);
            }
        });
    }
}
