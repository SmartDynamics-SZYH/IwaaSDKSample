package com.szyh.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.szyh.ewaasdk.websocket.bean.FaceResultResponse;
import com.szyh.ewaasdk.websocket.helper.FaceResultListener;
import com.szyh.ewaasdk.websocket.helper.RobotCommHelper;
import com.szyh.ewaasdk.websocket.robot.FaceRecognitioSwitch;

public class FaceActivity extends AppCompatActivity implements FaceResultListener {
    private RobotCommHelper robotCommHelper = new RobotCommHelper();
    private FaceRecognitioSwitch faceRecognitioSwitch = new FaceRecognitioSwitch();
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("人脸识别测试");
        setContentView(R.layout.face_activity);
        checkBox = (CheckBox) findViewById(R.id.id_face_switch);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(FaceActivity.this, "人脸识别打开", Toast.LENGTH_SHORT).show();
                    faceRecognitioSwitch.open("192.168.5.50");
                    //TODO 需要回调函数的，可以使用下面的方法。
//                    faceRecognitioSwitch.open("192.168.5.50", new RobotCallback() {
//                        @Override
//                        public void onSuccess() {
//
//                        }
//
//                        @Override
//                        public void onFail(Exception e) {
//
//                        }
//                    });
                } else {
                    Toast.makeText(FaceActivity.this, "人脸识别关闭", Toast.LENGTH_SHORT).show();
                    faceRecognitioSwitch.close();
                    //TODO 需要回调函数的，可以使用下列方法
//                    faceRecognitioSwitch.close(new RobotCallback() {
//                        @Override
//                        public void onSuccess() {
//
//                        }
//
//                        @Override
//                        public void onFail(Exception e) {
//
//                        }
//                    });
                }
            }
        });
        robotCommHelper.addFaceResultListener(this);
    }

    @Override
    public void onFaceResult(FaceResultResponse faceResultResponse) {
        //TODO 工控机推送过来的人脸识别数据
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        robotCommHelper.removeFaceResultListener(this);
    }
}
