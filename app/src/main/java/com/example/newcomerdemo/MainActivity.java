package com.example.newcomerdemo;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PictureListShowFragment();
    }
}