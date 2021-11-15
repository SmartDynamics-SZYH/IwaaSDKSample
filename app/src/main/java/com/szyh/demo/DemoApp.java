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

    //TODO 将此处的讯飞 appid修改为在科大讯飞官网申请的appid
    private static final String XUNFEI_ID = "";
    //TODO 将此处的灵聚 appid修改为在灵聚申请的appid
    private static final String XLINGJU_ID = "";

    @Override
    public void onCreate() {
        super.onCreate();

        //当时在息屏状态下，亮屏
        screenOnWhenOff();

        //设置当前屏幕显示为永不休眠
        updateScreenTimeoutFromNever();

        //替换科大讯飞语音id
        AudioEngineProxy.Builder builder = new AudioEngineProxy.Builder();
        //使用自己生申请的科大讯飞Appid,需要去掉IflytechSDL-release-1.0.aar,加入自己得申请的msc.jar和libmsc.so文件
        builder.buildIflytekAppId(XUNFEI_ID);
        builder.buildLingjuAppKey(XLINGJU_ID);
        AudioEngineProxy.getInstance().bindAudioEngineService(this, builder);
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
