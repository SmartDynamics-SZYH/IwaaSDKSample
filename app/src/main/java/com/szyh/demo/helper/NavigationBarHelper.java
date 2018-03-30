package com.szyh.demo.helper;

import android.view.View;

/**
 * Created by Administrator on 2018/3/30.
 */

public class NavigationBarHelper {

    public static void hideNavigationBar(View view) {
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
