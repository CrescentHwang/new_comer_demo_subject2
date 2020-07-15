package com.example.newcomerdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.newcomerdemo.model.PictureItem;
import com.example.newcomerdemo.view.ZoomImageView;

public class PictureBigFragment extends Fragment {

    private ZoomImageView mPictureZoomView;
    private TextView mDescriptionTV;
    private PictureItem mPictureObj;
    public static final String PICTURE_OBJECT_TAG = "PictureBigFragment.PictureObj";

    PictureBigFragment(PictureItem pictureItem) {
        this.mPictureObj = pictureItem;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO 传入的对象为 null
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_big_picture, container, false);
        mPictureZoomView = v.findViewById(R.id.pricture_big_image_view);
        mDescriptionTV = v.findViewById(R.id.picture_description_text_view);
        initUI();
        return v;
    }

    // 初始化 UI 组件
    private void initUI() {
        Glide.with(getActivity())
                .load(mPictureObj.getmUrl())
                .error(R.drawable.picture_err)
                .into(mPictureZoomView);
        mDescriptionTV.setText(mPictureObj.getmDescription());
    }
}
