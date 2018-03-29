package com.szyh.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.szyh.demo.helper.UARTHelper;
import com.szyh.demo.listener.AngleChangeListener;
import com.szyh.ewaasdk.serialport.SerialPortDataListener;
import com.szyh.ewaasdk.serialport.SerialPortStateListener;
import com.szyh.ewaasdk.serialport.SerialPortTools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SerialPortActivity extends AppCompatActivity implements AngleChangeListener {

    private static final String TAG = "SerialPortActivity";
    private TextView serialPortText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_port);
        setTitle("五麦串口通讯框架测试");
        UARTHelper.createAgent(this);
        serialPortText = (TextView) findViewById(R.id.id_serial_port_text);
    }

    @Override
    public void onAngleChange(final int angle) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                serialPortText.setText("唤醒角度：" + angle);
                Toast.makeText(SerialPortActivity.this, "唤醒角度：" + angle, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UARTHelper.destroy();
    }

    /**
     * 增强0号咪头拾音
     *
     * @param view
     */
    public void beam00(View view) {
        try {
            SerialPortTools.sendCmd("BEAM 0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void beam01(View view) {
        try {
            SerialPortTools.sendCmd("BEAM 1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void beam02(View view) {
        try {
            SerialPortTools.sendCmd("BEAM 2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void beam03(View view) {
        try {
            SerialPortTools.sendCmd("BEAM 3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
