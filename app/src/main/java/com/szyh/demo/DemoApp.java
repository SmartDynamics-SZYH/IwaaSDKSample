package com.szyh.demo;

import android.app.Application;

import com.szyh.ewaasdk.audio.AudioEngineProxy;

/**
 * Created by Administrator on 2017/8/2.
 */

public class DemoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
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

    @Override
    public void onTerminate() {
        super.onTerminate();
        AudioEngineProxy.getInstance().unbindAudioEngineService(this);
    }
}
