package com.szyh.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.szyh.ewaasdk.websocket.bean.MotionStatusResponse;
import com.szyh.ewaasdk.websocket.bean.NavigationStatusResponse;
import com.szyh.ewaasdk.websocket.bean.SensorStatusResponse;
import com.szyh.ewaasdk.websocket.helper.RobotCommHelper;
import com.szyh.ewaasdk.websocket.helper.RobotStatusListener;

public class RobotStatusActivity extends AppCompatActivity {
    private RobotCommHelper robotCommHelper = new RobotCommHelper();
    private RobotStatusListenerImpl rsl = new RobotStatusListenerImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("机器人状态信息上报");
        setContentView(R.layout.activity_robot_status);
        robotCommHelper.addRobotStatusListener(rsl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        robotCommHelper.removeRobotStatusListener(rsl);
    }


    class RobotStatusListenerImpl implements RobotStatusListener {

        @Override
        public void onSensorStatusResponse(SensorStatusResponse sensorStatusResponse) {
            //TODO 机器人传感器信息的回调
        }

        @Override
        public void onRobotMotionStatusResponse(MotionStatusResponse motionStatusResponse) {
            //TODO 机器人运动状态信息回调
        }

        @Override
        public void onRobotNavigationStatusResponse(NavigationStatusResponse navigationStatusResponse) {
            //TODO 机器人导航状态新回调
        }
    }
}
