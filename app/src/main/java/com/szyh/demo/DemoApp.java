package com.szyh.demo;

import android.app.Application;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import com.szyh.ewaasdk.audio.AudioEngineProxy;

/**
 * Created by Administrator on 2017/8/2.
 */

public class DemoApp extends Application {

    private static final String TAG = DemoApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        //当时在息屏状态下，亮屏
        screenOnWhenOff();

        //设置当前屏幕显示为永不休眠
        updateScreenTimeoutFromNever();

        //语音语义相关服务，SDK默认科大讯飞语音语义和灵聚语义，但是appid是固定的。需要更换的话用下面的方法
        AudioEngineProxy.getInstance().bindAudioEngineService(this);

        //替换科大讯飞语音语义或者灵聚语义
        //AudioEngineProxy.Builder builder = new AudioEngineProxy.Builder();
        //使用自己生申请的科大讯飞Appid,需要去掉IflytechSDL-release-1.0.aar,加入自己得申请的jar和so文件
        //builder.buildIflytekAppId("xf_appid");
        //替换灵聚的语义appid,dev.lingju.ai
        //builder.buildLingjuAppKey("lingju_appid");
        //AudioEngineProxy.getInstance().bindAudioEngineService(this, builder);
    }


    /**
     * 当时在息屏状态下，亮屏
     * 需要权限：android.permission.WAKE_LOCK
     */
    private void screenOnWhenOff() {
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (pm == null) {
            return;
        }
        //判断当前是否在息屏状态下
        if (!pm.isScreenOn()) {
            Log.e(TAG, "screenOnWhenOff: 息屏状态下，开始亮屏");
            PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP, "screen on");
            mWakeLock.acquire(0L);
            mWakeLock.release();
        }
    }

    /**
     * 设置当前屏幕显示为永不休眠
     */
    private void updateScreenTimeoutFromNever() {
        try {
            int timeout = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
            //判断当前屏幕显示是否是永不休眠
            if (timeout != Integer.MAX_VALUE) {
                //设置屏幕显示为永不休眠
                Log.e(TAG, "updateScreenTimeoutFromNever: 设置屏幕显示为永不休眠");
                Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, Integer.MAX_VALUE);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AudioEngineProxy.getInstance().unbindAudioEngineService(this);
    }
}
