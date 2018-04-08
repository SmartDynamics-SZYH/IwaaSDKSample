package com.szyh.demo;

import android.os.Bundle;

import com.szyh.ewaasdk.websocket.bean.IDCardResponse;
import com.szyh.ewaasdk.websocket.helper.RobotCommHelper;
import com.szyh.ewaasdk.websocket.helper.RobotIDReaderListener;
import com.szyh.ewaasdk.websocket.helper.RobotOtherCardListener;

public class OtherInfoActivity extends BaseActivity {
    private RobotCommHelper robotCommHelper = new RobotCommHelper();
    private RobotIDReaderListenerImpl rirl = new RobotIDReaderListenerImpl();
    private RobotOtherCardListenerImpl rocl = new RobotOtherCardListenerImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("其他信息(身份证、银行卡和条形码等)上报");
        setContentView(R.layout.activity_other_info);
        robotCommHelper.addRobotReadIDListener(rirl);
        robotCommHelper.addRobotOtherCardListener(rocl);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        robotCommHelper.removeRobotReadIDListener(rirl);
        robotCommHelper.removeRobotOtherCardListener(rocl);
    }

    /**
     * 身份证读取监听器
     */
    public class RobotIDReaderListenerImpl implements RobotIDReaderListener {
        @Override
        public void onReadIDResponse(IDCardResponse idCardResponse) {
            //TODO 身份证回调信息
        }
    }

    /**
     * 其他证件读取监听器
     */
    public class RobotOtherCardListenerImpl implements RobotOtherCardListener {
        @Override
        public void onReadOtherCard(int type, String info) {
            //TODO 其他卡片信息 根据type来判断是什么类型卡。
        }
    }
}
