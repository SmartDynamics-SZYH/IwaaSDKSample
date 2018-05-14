package com.szyh.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.szyh.ewaasdk.websocket.bean.FingerprintInputResponse;
import com.szyh.ewaasdk.websocket.bean.FingerprintResponse;
import com.szyh.ewaasdk.websocket.helper.FingerprintInputListener;
import com.szyh.ewaasdk.websocket.helper.FingerprintRecognizeListener;
import com.szyh.ewaasdk.websocket.helper.RobotCommHelper;
import com.szyh.ewaasdk.websocket.robot.RobotCallback;
import com.szyh.ewaasdk.websocket.robot.RobotOperateFingerprint;
import com.szyh.ewaasdk.websocket.robot.RobotOperationFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/14.
 */

public class FingerprintActivity extends BaseActivity {

    private static final String TAG = FingerprintActivity.class.getSimpleName();

    RobotCommHelper robotCommHelper = new RobotCommHelper();
    private RobotOperationFactory rof = new RobotOperationFactory();
    private List<FingerprintResponse.FingerInfo> fingerInfolList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint_layout);
        setTitle("指纹相关");
        //查询所有指纹
        rof.createRobotOperation(RobotOperateFingerprint.class).queryAll(new RobotOperateFingerprint.QueryFingerprintListener() {
            @Override
            public void onQueryFingerprintSuccess(List<FingerprintResponse.FingerInfo> list) {
                fingerInfolList.clear();
                fingerInfolList.addAll(list);
            }

            @Override
            public void onQueryFingerprintFail() {

            }
        });
    }

    private FingerprintInputListener fingerprintInputListener;

    /**
     * 创建指纹信息和录入
     */
    public void createFingerprint(final String name, final String id) {
        rof.createRobotOperation(RobotOperateFingerprint.class).create(name, id, new RobotCallback() {
            @Override
            public void onSuccess() {
                fingerprintInputListener = new FingerprintInputListenerImpl(name, id);
                robotCommHelper.addFingerprintInputListener(fingerprintInputListener);
            }

            @Override
            public void onFail(Exception e) {

            }
        });
    }

    private class FingerprintInputListenerImpl implements FingerprintInputListener {

        private String name;

        private String id;

        public FingerprintInputListenerImpl(String name, String id) {
            this.name = name;
            this.id = id;
        }

        @Override
        public void onFingerprintInput(FingerprintInputResponse fingerprintInputResponse) {
            int status = fingerprintInputResponse.getStatus();//0：正在录入；1、录入成功；其他表示失败
            int featuresNeeded = fingerprintInputResponse.getFeaturesNeeded();//剩余录入次数
            //TODO 根据 status 和 featuresNeeded判断剩余指纹录入次数
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            if (fingerprintInputListener != null) {
                robotCommHelper.removeFingerprintInputListener(fingerprintInputListener);
            }
        }
    }


    /**
     * 开始指纹识别
     */
    public void openFingerprintRecognize() {
        rof.createRobotOperation(RobotOperateFingerprint.class).recognizeSwitch(true);
        robotCommHelper.addFingerprintRecognizeListener(fingerprintRecognizeListener);
    }

    /**
     * 关闭指纹识别
     */
    public void closeFingerprintRecognize() {
        rof.createRobotOperation(RobotOperateFingerprint.class).recognizeSwitch(false);
        robotCommHelper.addFingerprintRecognizeListener(fingerprintRecognizeListener);
    }

    FingerprintRecognizeListener fingerprintRecognizeListener = new FingerprintRecognizeListener() {
        @Override
        public void onFingerprintRecognize(FingerprintResponse.FingerInfo fingerInfo) {
            if (TextUtils.isEmpty(fingerInfo.getName()) || fingerInfo.getId().equals("-1")) {
                Log.e(TAG, "onFingerprintRecognize: 指纹未录入");
                return;
            }
            Log.d(TAG, "onFingerprintRecognize: " + fingerInfo.getName() + ",验证成功！");
        }
    };
}
