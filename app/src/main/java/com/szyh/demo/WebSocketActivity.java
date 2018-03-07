package com.szyh.demo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.szyh.ewaasdk.websocket.RobotWebSocketClient;
import com.szyh.ewaasdk.websocket.helper.RobotCommHelper;

import org.java_websocket.handshake.ServerHandshake;

/**
 * 重连工控机的例子。
 */
public class WebSocketActivity extends AppCompatActivity implements RobotWebSocketClient.WebSocketStatusListener {

    private static final String TAG = "WebSocketActivity";

    private RobotCommHelper robotCommHelper = new RobotCommHelper();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    /**
     * 断开连接任务
     */
    private Runnable disconnectRunnable = new Runnable() {
        @Override
        public void run() {
            robotCommHelper.disconnect();
        }
    };

    /**
     * 重新连接任务
     */
    private Runnable reconnectRunnable = new Runnable() {
        @Override
        public void run() {
            createAndConnect();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("工控机控制");
        setContentView(R.layout.activity_web_socket);
    }

    public void connectWindow(View view) {
        createAndConnect();
    }

    public void disconnectWindow(View view) {
        robotCommHelper.disconnect();
    }

    private void createAndConnect() {
        robotCommHelper.createWebSocketClient();

        robotCommHelper.removeConnectStatusListener(this);
        robotCommHelper.addConnectStatusListener(this);

        robotCommHelper.connect();
        handler.removeCallbacks(disconnectRunnable);
        handler.postDelayed(disconnectRunnable, 30 * 1000);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        //连接成功之后移除断开连接任务和重新连接的任务。
        handler.removeCallbacks(disconnectRunnable);
        handler.removeCallbacks(reconnectRunnable);
        Log.d(TAG, "onOpen");
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        //webSocket关闭就开启重新连接。
        handler.removeCallbacks(disconnectRunnable);
        handler.postDelayed(reconnectRunnable, 100);
        Log.d(TAG, "onClose");
    }

    @Override
    public void onError(Exception e) {
        //如果webSocket没有打开，且报错则就要重新去连接。
        if (!robotCommHelper.isOpen()) {
            handler.removeCallbacks(disconnectRunnable);
            handler.postDelayed(reconnectRunnable, 100);
        }
    }
}
