package com.example.newcomerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.newcomerdemo.model.PictureItem;

public class PictureBigActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context, PictureItem pictureItem) {
        Intent intent = new Intent(context, PictureBigActivity.class);
        intent.putExtra(PictureBigFragment.PICTURE_OBJECT_TAG, pictureItem);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new PictureBigFragment((PictureItem)getIntent().getParcelableExtra(PictureBigFragment.PICTURE_OBJECT_TAG));
    }


}