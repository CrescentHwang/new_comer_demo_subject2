package com.example.newcomerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newcomerdemo.adapter.PictureListShowRecyclerViewAdapter;
import com.example.newcomerdemo.message.dialog.MessageDialog;
import com.example.newcomerdemo.model.PictureItem;
import com.example.newcomerdemo.model.PicturesUrlGetter;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;

public class PictureListShowFragment extends Fragment {
    private RecyclerView mPictureListRecyclerView;
    private ArrayList<PictureItem> mItems = new ArrayList<>();;
    private RefreshLayout mRefreshLayout;
    private int mPhotoPage = 0;
    private int mPhontCount = 40;
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
                    case PicturesUrlGetter.PICTURE_GETTING_TAG :
                        if(isAdded()) {
                            dealWithItems(msg);
                        }
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
        PicturesUrlGetter.getInstance().getPicturesUrl(mPicUrlGetterHandler, getUrl());
        return v;
    }

    // 获取图片列表信息的 url
    private String getUrl() {
        String url = "https://gank.io/api/v2/data/category/Girl/type/Girl/page/"+mPhotoPage+"/count/"+mPhontCount;
        return url;
    }

    // 获取图片列表信息后的处理
    private void dealWithItems(Message msg) {
        switch(msg.arg1) {
            case PicturesUrlGetter.SUCCESSFUL_TAG: // 成功获取图片s Url
                mItems.addAll((ArrayList<PictureItem>)msg.obj);
                mPhotoPage++;
                // 更新 adapter
                updateAdapter();
                break;
            case PicturesUrlGetter.FAILURE_TAG: // 无法获取图片s Url
                switch (msg.arg2) {
                    case PicturesUrlGetter.NO_PICTURE_TAG :
                        MessageDialog.showToast(getActivity(), R.string.pictures_url_no_picture_error);
                        break;
                    case PicturesUrlGetter.REQUEST_ERROR :
                        MessageDialog.showToast(getActivity(), R.string.pictures_url_request_error);
                        break;
                    case PicturesUrlGetter.JSON_OBJECT_PARSE_ERROR :
                        MessageDialog.showToast(getActivity(), R.string.pictures_url_json_object_parsing_error);
                        break;
                    case PicturesUrlGetter.JSON_OBJECT_TRANSLATE_ERROR :
                        MessageDialog.showToast(getActivity(), R.string.pictures_url_json_object_translate_error);
                        break;
                    default:
                        MessageDialog.showToast(getActivity(), R.string.pictures_url_getting_error);
                }
                break;
        }
    }

    // 更新 recyclerview 的 adapter
    private void updateAdapter() {
        if (isAdded()) {
            mPictureListRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    // 初始化UI组件
    private void initUI() {
        PictureListShowRecyclerViewAdapter adapter = new PictureListShowRecyclerViewAdapter(getContext(), mItems);
        // adapter 设置点击事件
        adapter.setOnItemClickListener(new PictureListShowRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(PictureBigActivity.newIntent(getActivity(), mItems, position));
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
                PicturesUrlGetter.getInstance().getPicturesUrl(mPicUrlGetterHandler, getUrl());
                refreshlayout.finishLoadMore(500);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 结束时清空handler的消息
        mPicUrlGetterHandler.removeCallbacksAndMessages(null);
    }
}
