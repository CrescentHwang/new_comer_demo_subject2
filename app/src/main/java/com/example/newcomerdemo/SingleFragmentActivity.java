package com.example.newcomerdemo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

// 管理单一 fragment 的 Activity，抽象出来用以复用
public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_single);

        // 获取 fragmentManager
        FragmentManager fm = getSupportFragmentManager();
        // 通过 fragment 容器 ID 来获取fragment
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
