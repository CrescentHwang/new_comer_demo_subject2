package com.example.newcomerdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.newcomerdemo.message.show.MyToastShow;
import com.example.newcomerdemo.model.PictureItem;

import java.util.ArrayList;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;


public class PictureBigActivity extends AppCompatActivity {
    public static final String PICTURES_ARRAY_LIST = "PictureBigActivity.PicturesArrList";
    public static final String POSITION_TAG = "PictureBigActivity.Position";
    private ViewPager mViewPager;
    private ArrayList<PictureItem> mPictureItemsArrayList;
    private int mPosition;

    public static Intent newIntent(Context context,
                                   ArrayList<PictureItem> pictureItemArrList, int position) {
        Intent intent = new Intent(context, PictureBigActivity.class);
        intent.putExtra(PICTURES_ARRAY_LIST, pictureItemArrList);
        intent.putExtra(POSITION_TAG, position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_picture);
        mPictureItemsArrayList = getIntent().getParcelableArrayListExtra(PICTURES_ARRAY_LIST);
        if(mPictureItemsArrayList == null) {
            MyToastShow.showToast(this, "图片加载失败，请返回重试");
            return;
        }
        mPosition = getIntent().getIntExtra(POSITION_TAG,0);
        mViewPager = findViewById(R.id.picture_big_view_pager);
        // 设置 fragment
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return new PictureBigItemFragment(mPictureItemsArrayList.get(position));
            }

            @Override
            public int getCount() {
                return mPictureItemsArrayList.size();
            }
        });
        mViewPager.setCurrentItem(mPosition, false);
    }
}