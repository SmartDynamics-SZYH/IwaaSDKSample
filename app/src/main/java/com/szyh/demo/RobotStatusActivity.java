package com.szyh.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.szyh.ewaasdk.websocket.bean.MotionStatusResponse;
import com.szyh.ewaasdk.websocket.bean.NavigationStatusResponse;
import com.szyh.ewaasdk.websocket.bean.SensorStatusResponse;
import com.szyh.ewaasdk.websocket.helper.RobotCommHelper;
import com.szyh.ewaasdk.websocket.helper.RobotStatusListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RobotStatusActivity extends BaseActivity {
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


    private int minLaserDistance = -1;

    class RobotStatusListenerImpl implements RobotStatusListener {

        @Override
        public void onSensorStatusResponse(SensorStatusResponse ssr) {
            //TODO 机器人传感器信息的回调
            //激光束 0-189（共计180个）
            ArrayList<SensorStatusResponse.Laser> lasers = ssr.getLaserList();
            if (lasers != null) {
                List<Integer> distances = new ArrayList<>();
                int size = lasers.size();//激光束数目
                int index = (size - 180) / 2 + 44;//机器人正前方开始位置
                int indexMax = index + 90;
                for (int i = index; i < indexMax; i++) {
                    int distance = lasers.get(i).getDistance();
                    distances.add(distance);
                    Collections.sort(distances, new Comparator<Integer>() {
                        @Override
                        public int compare(Integer o1, Integer o2) {
                            if (o1 > o2) {//升序排序
                                return 1;
                            } else if (o1 == o2) {
                                return 0;
                            } else {
                                return -1;
                            }
                        }
                    });
                }
                minLaserDistance = distances.get(0);//获取最小距离，单位毫米
                //TODO 判断 minLaserDistance 大小来做具体的业务。比如1、小于1000毫米以内机器人唤醒，2、大于2000毫米之外机器人休眠
            }
        }

        @Override
        public void onRobotMotionStatusResponse(MotionStatusResponse motionStatusResponse) {
            //TODO 机器人运动状态信息回调
            //获取头部水平角度
            int headHorizontalAngle = motionStatusResponse.getHeadHorizontal();
        }

        @Override
        public void onRobotNavigationStatusResponse(NavigationStatusResponse navigationStatusResponse) {
            //TODO 机器人导航状态新回调
        }
    }
}
