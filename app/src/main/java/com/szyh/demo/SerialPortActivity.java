package com.szyh.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.szyh.ewaasdk.serialport.SerialPortDataListener;
import com.szyh.ewaasdk.serialport.SerialPortStateListener;
import com.szyh.ewaasdk.serialport.SerialPortTools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SerialPortActivity extends AppCompatActivity implements
        SerialPortDataListener, SerialPortStateListener {

    private static final String TAG = "SerialPortActivity";
    private TextView serialPortText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_port);
        setTitle("五麦串口通讯框架测试");
        serialPortText = (TextView) findViewById(R.id.id_serial_port_text);
        if (!SerialPortTools.isStarted()) {
            try {
                SerialPortTools.init("/dev/ttyS3", 115200, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SerialPortTools.addDataListener(this);
        SerialPortTools.addStateListener(this);
    }


    private static final String ANGLE_HEAD = "angle:";
    private static final String ANGLE_INFO = "angle:360";
    private StringBuffer serialPortSb = new StringBuffer();


    @Override
    public void onSerialPortDataReceived(byte[] data) {
        final String receive = new String(data).trim();
        if (!TextUtils.isEmpty(receive)) {
            serialPortSb.append(receive);
        }
        if (serialPortSb.toString().contains(ANGLE_HEAD)) {
            int angleIndex = serialPortSb.indexOf(ANGLE_HEAD);
            serialPortSb.delete(0, angleIndex);
            if (serialPortSb.length() < ANGLE_INFO.length()) {
                return;
            }
            serialPortSb.delete(ANGLE_INFO.length(), serialPortSb.length());
            Matcher matcher = Pattern.compile("(\\d+)").matcher(serialPortSb.toString().trim());
            if (matcher.find()) {
                //该角度就是五麦或者六麦被唤醒的角度，
                // 根据该角度和工控机上报的机器人头部角度，再计算艾娃的头部需要转动的角度。
                final int angle = Integer.parseInt(matcher.group(1)) % 360;
                Log.d(TAG, "onSerialPortDataReceived: angle = " + angle);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        serialPortText.setText("angle:" + angle);
                        Toast.makeText(SerialPortActivity.this, "angle：" + angle, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public void onSerialPortDataSend(byte[] data) {
        String send = new String(data).trim();
    }

    @Override
    public void onSerialPortOpened() {
        Log.d(TAG, "onSerialPortOpened: ");
    }

    @Override
    public void onSerialPortClosed() {
        Log.d(TAG, "onSerialPortClosed: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SerialPortTools.removeStateListener(this);
        SerialPortTools.removeDataListener(this);
        //SerialPortTools.close();
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
