package com.szyh.demo;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.szyh.ewaasdk.websocket.RobotWebSocketClient;
import com.szyh.ewaasdk.websocket.helper.RobotCommHelper;

import org.java_websocket.handshake.ServerHandshake;

/**
 * 重连工控机的例子。
 */
public class WebSocketActivity extends BaseActivity implements RobotWebSocketClient.WebSocketStatusListener {

    private static final String TAG = "WebSocketActivity";

    private RobotCommHelper robotCommHelper = new RobotCommHelper();

    private Button connBtn, disconnBtn;
    private TextView connStatusTv;
    @SuppressLint("HandlerLeak")
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
        connBtn = findViewById(R.id.id_conn);
        disconnBtn = findViewById(R.id.id_dis_conn);
        connStatusTv = findViewById(R.id.id_conn_status);
        if (robotCommHelper.isOpen()) {
            connBtn.setClickable(false);
            disconnBtn.setClickable(true);
            connStatusTv.setText("已连接工控机");
        } else {
            connBtn.setClickable(true);
            disconnBtn.setClickable(false);
            connStatusTv.setText("已断开工控机");
        }
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connBtn.setClickable(false);
                disconnBtn.setClickable(true);
                connStatusTv.setText("已连接工控机");
            }
        });
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        //webSocket关闭就开启重新连接。
        handler.removeCallbacks(disconnectRunnable);
        handler.postDelayed(reconnectRunnable, 100);
        Log.d(TAG, "onClose");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connBtn.setClickable(true);
                disconnBtn.setClickable(false);
                connStatusTv.setText("已断开工控机");
            }
        });
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
