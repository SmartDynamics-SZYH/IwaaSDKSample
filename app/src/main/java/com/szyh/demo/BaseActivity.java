package com.szyh.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.szyh.demo.helper.NavigationBarHelper;

/**
 * Created by Administrator on 2018/3/30.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationBarHelper.hideNavigationBar(getWindow().getDecorView());
    }
}
