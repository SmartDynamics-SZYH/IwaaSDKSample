package com.szyh.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.szyh.ewaasdk.websocket.robot.RobotOperationArm;
import com.szyh.ewaasdk.websocket.robot.RobotOperationFactory;
import com.szyh.ewaasdk.websocket.robot.RobotOperationHead;
import com.szyh.ewaasdk.websocket.robot.RobotOperationLight;
import com.szyh.ewaasdk.websocket.robot.RobotOperationLight2;
import com.szyh.ewaasdk.websocket.robot.RobotOperationMap;
import com.szyh.ewaasdk.websocket.robot.RobotOperationMode;
import com.szyh.ewaasdk.websocket.robot.RobotOperationMotion;
import com.szyh.ewaasdk.websocket.robot.RobotOperationNavi;
import com.szyh.ewaasdk.websocket.robot.RobotOperationPrint;
import com.szyh.ewaasdk.websocket.robot.RobotOperationRotate;
import com.szyh.ewaasdk.websocket.robot.RobotOperationShutdown;

/**
 * 机器人控制流程是
 * 1、连接sebsocketServer
 * 2、客户端登录,登陆成功可以对机器人进行控制
 */
public class RobotOperateActivity extends AppCompatActivity {

    private static final String TAG = "RobotOperateActivity";

    private RobotOperationFactory rof = new RobotOperationFactory();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("机器人控制");
        setContentView(R.layout.activity_robot);
    }

    /**
     * 运动控制（前进为例子，其余由后退、左转、右转）
     * 遇到障碍物会躲避
     *
     * @param view
     */
    public void robotMotionRequestAhead(View view) {
        rof.createRobotOperation(RobotOperationMotion.class).goAhead();
        new RobotOperationMotion().goAhead();
        // TODO 或者 new RobotOperationMotion().goAhead();
    }

    /**
     * 二代机器人灯光(眼睛、耳朵和身体)控制（以打开眼睛灯光为例子，其他有打开耳朵、身体灯光和所有灯光）
     *
     * @param view
     */
    public void robotLight2(View view) {
        rof.createRobotOperation(RobotOperationLight.class).eyeLightOn(8, 80);
        //TODO 或者new RobotOperationLight().eyeLightOn(8, 80);其中参数的含义请参看javadoc文档。
    }

    /**
     * 三代机器人灯光（上嘴唇和下嘴唇）控制（以打开上嘴唇为例子，其他有打开下嘴唇和所有灯光）
     *
     * @param view
     */
    public void robotLight3(View view) {
        rof.createRobotOperation(RobotOperationLight2.class).upperLipLightOn(8, 80);
        //TODO 或者new RobotOperationLight2().upperLipLightOn(8, 80);其中参数的含义请参看javadoc文档。
    }

    /**
     * 获取地图
     * 从机器人内部获取扫描的地图
     *
     * @param view
     */
    public void robotGetMap(View view) {
        RobotOperationMap robotOperationMap = rof.createRobotOperation(RobotOperationMap.class);    //地图操控类
        robotOperationMap.setMapListener(new RobotOperationMap.MapListener() {  //设置地图监听器
            @Override
            public void base64(String mapData) {
                //TODO 获取base地图图片数据
            }
        });
        robotOperationMap.getDefaultMapInfo();
    }


    /**
     * 切换到自由模式
     * 随意走，避障
     *
     * @param view
     */
    public void robotModeRequest(View view) {
        rof.createRobotOperation(RobotOperationMode.class).freeMode();
    }

    /**
     * 头部控制，以头部水平转动到120度为例子。
     * 二代有头部垂直方向运动，三代没有。
     * 头回正后的头部所在的角度：水平角度180，垂直角度180。
     * 其余的都不操作，请查看javadoc文档。
     *
     * @param view
     */
    public void robotHeadRequest(View view) {
        rof.createRobotOperation(RobotOperationHead.class).horizontalRotateTo(120); //固定速度 8度/秒 水平方向转到指定角度。
    }

    /**
     * 身体转动，即身体转动多少角度。
     * 左转：向左90度；
     * 右转：向右90度；
     * 转身：向后180度；
     * 转圈：转360度；
     * 也可以转动到指定度数（比如30度等等）。
     *
     * @param view
     */
    public void robotTurnAround(View view) {
        rof.createRobotOperation(RobotOperationRotate.class).turnAround();
    }


    /**
     * 导航到指定位置，遇到障碍物避障
     * 235-X
     * 125-Y
     * 60-到定位点之后，机器人的角度
     * 50-速度，单位 cm/s
     *
     * @param view
     */
    public void robotNavigationRequest(View view) {
        rof.createRobotOperation(RobotOperationNavi.class).naviObstacleAvoid(235, 125, 60, 50);
    }

    /**
     * 关机
     *
     * @param view
     */
    public void robotShutdown(View view) {
        //默认延迟10秒，参数30表示30秒后关机。
        rof.createRobotOperation(RobotOperationShutdown.class).sendShutdown(30);
    }

    /**
     * 三代艾娃客服才有手臂
     * 手臂控制，手臂垂直方向为180度（下面以左手手臂向前转动60度为例）
     *
     * @param view
     */
    public void robotOperateArm(View view) {
        rof.createRobotOperation(RobotOperationArm.class).leftArm(4, 180 + 60);
    }

    /**
     * 打印数据
     *
     * @param view
     */
    public void robotOperatePrint(View view) {
        rof.createRobotOperation(RobotOperationPrint.class).sendPrint("Hello World");
    }
}
