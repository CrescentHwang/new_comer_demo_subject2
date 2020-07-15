package com.example.newcomerdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.newcomerdemo.model.PictureItem;

import java.util.ArrayList;


public class PictureBigActivity extends AppCompatActivity {
    public static final String PICTURES_ARRAY_LIST = "PictureBigActivity.PicturesArrList";
    public static final String POSITION_TAG = "PictureBigActivity.Position";
    private ViewPager2 mViewPager;
    private ArrayList<PictureItem> mPictureItemsArrayList;
    private int mPosition;
    private FragmentManager mFm;
    private CanBeDraggedOutListener mCanBeDraggedOutListener;
    public interface CanBeDraggedOutListener {
        boolean canBeDraggedOut();
    }
    public void setCanBeDraggedOutListener(CanBeDraggedOutListener canBeDraggedOutListener) {
        this.mCanBeDraggedOutListener = canBeDraggedOutListener;
    }

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
        mPosition = getIntent().getIntExtra(POSITION_TAG,0);
        mViewPager = findViewById(R.id.picture_big_view_pager);
        // 设置 fragment
        mFm = getSupportFragmentManager();
        // 设置 adapter
        mViewPager.setAdapter(new FragmentStateAdapter(mFm, getLifecycle()) {
            @Override
            public int getItemCount() {
                return mPictureItemsArrayList.size();
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                final PictureBigItemFragment fragment = new PictureBigItemFragment(mPictureItemsArrayList.get(position));
                setCanBeDraggedOutListener(new CanBeDraggedOutListener() {
                    @Override
                    public boolean canBeDraggedOut() {
                        return fragment.canBeDraggedOut();
                    }
                });
                return new PictureBigItemFragment(mPictureItemsArrayList.get(position));
            }
        });
        mViewPager.setCurrentItem(mPosition, false);
        mViewPager.setUserInputEnabled(false);
    }
}