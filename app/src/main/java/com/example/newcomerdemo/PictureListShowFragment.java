package com.example.newcomerdemo;

import android.os.Bundle;
import android.os.Handler;
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
import com.example.newcomerdemo.message.show.MyToastShow;
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
    private static final String PHOTO_PAGE_TAG = "PictureListShowFragment.PhotoPage";
    private static final String PHOTO_COUNT_TAG = "PictureListShowFragment.PhotoCount";
    private int mPhotoPage = 1;
    private int mPhotoCount = 40;
    private int mLastPhotoPage = -1;
    private PictListFragmentHanlder mPicUrlGetterHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 销毁 fragment 视图，但不销毁 fragment 本身
        setRetainInstance(true);
        mPicUrlGetterHandler = new PictListFragmentHanlder(this);
        // 首次获取 picture url
        PicturesUrlGetter.getInstance().getPicturesUrl(mPicUrlGetterHandler, getUrl(mPhotoPage, mPhotoCount), mPhotoPage);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_picture_list_show, container, false);
        // 实例化 recyclerview
        mPictureListRecyclerView = v.findViewById(R.id.picture_list_recyclerview);
        mPictureListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        // 实例化 handler
        // 实例化上下拉刷新组件
        mRefreshLayout = (RefreshLayout)v.findViewById(R.id.picture_list_refresh_layout);
        // 初始化组件
        initUI();

        return v;
    }

    // 获取图片列表信息的 url
    private String getUrl(int picturePage, int photoCount) {
        return "https://gank.io/api/v2/data/category/Girl/type/Girl/page/"+picturePage+"/count/"+photoCount;
    }

    // 获取图片列表信息后的处理
    private void dealWithItems(Message msg) {
        switch(msg.arg1) {
            case PicturesUrlGetter.SUCCESSFUL_TAG: // 成功获取图片s Url
                if(isAdded()) {
                    if(msg.arg2 != mLastPhotoPage) { // 因为异步请求，所以需要判断是不是重复请求同一页
//                        Log.i("TEST", "GET ======================== " + msg.arg2);
                        mItems.addAll((ArrayList<PictureItem>)msg.obj);
                        mLastPhotoPage = mPhotoPage;
                        mPhotoPage++; // 更新为下一页的页码
                        // 更新 adapter
                        updateAdapter();
                    }
                }
                break;
            case PicturesUrlGetter.FAILURE_TAG: // 无法获取图片s Url
                mRefreshLayout.finishLoadMore(false);
                switch (msg.arg2) {
                    case PicturesUrlGetter.NO_PICTURE_TAG :
                        MyToastShow.showToast(getActivity(), R.string.pictures_url_no_picture_error);
                        break;
                    case PicturesUrlGetter.REQUEST_ERROR :
                        MyToastShow.showToast(getActivity(), R.string.pictures_url_request_error);
                        break;
                    case PicturesUrlGetter.JSON_OBJECT_PARSE_ERROR :
                        MyToastShow.showToast(getActivity(), R.string.pictures_url_json_object_parsing_error);
                        break;
                    case PicturesUrlGetter.JSON_OBJECT_TRANSLATE_ERROR :
                        MyToastShow.showToast(getActivity(), R.string.pictures_url_json_object_translate_error);
                        break;
                    default:
                        MyToastShow.showToast(getActivity(), R.string.pictures_url_getting_error);
                }
                break;
        }
    }

    // 更新 recyclerview 的 adapter
    private void updateAdapter() {
        if (isAdded()) {
//            Log.i("TEST", "------------------------ mPhotoPage = " + mPhotoPage);
            mPictureListRecyclerView.getAdapter().notifyDataSetChanged();
            mRefreshLayout.finishLoadMore(true);
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
                PicturesUrlGetter.getInstance().getPicturesUrl(mPicUrlGetterHandler, getUrl(mPhotoPage,mPhotoCount),mPhotoPage);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 结束时清空handler的消息
        mPicUrlGetterHandler.removeCallbacksAndMessages(null);
        // handler 置空
        mPicUrlGetterHandler = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PHOTO_PAGE_TAG, mPhotoPage);
        outState.putInt(PHOTO_COUNT_TAG, mPhotoCount);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            this.mPhotoPage = savedInstanceState.getInt(PHOTO_PAGE_TAG);
            this.mPhotoCount = savedInstanceState.getInt(PHOTO_COUNT_TAG);
        }
    }

    // 静态内部类 handler
    private static class PictListFragmentHanlder extends Handler {
        // 外部弱引用
        private final PictureListShowFragment pictListShowFragment;

        private PictListFragmentHanlder(PictureListShowFragment pictureListShowFragment) {
            pictListShowFragment = pictureListShowFragment;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                // 获取图片列表信息 成功
                case PicturesUrlGetter.PICTURE_GETTING_TAG :
                    if(pictListShowFragment != null && pictListShowFragment.isAdded()) {
                        pictListShowFragment.dealWithItems(msg);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
