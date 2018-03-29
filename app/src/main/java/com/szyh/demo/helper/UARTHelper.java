package com.szyh.demo.helper;

import android.text.TextUtils;

import com.szyh.demo.listener.AngleChangeListener;
import com.szyh.ewaasdk.serialport.SerialPortDataListener;
import com.szyh.ewaasdk.serialport.SerialPortTools;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @项目名： IwaaKF-III
 * @包名： com.szyh.ikf3.helper
 * @文件名: UARTHelper
 * @创建者: ruanhouli
 * @创建时间: 2017/5/8 17:34
 * @描述： 串口操作帮助类
 */

public class UARTHelper {

    private static final String FILE_NAME = "/dev/ttyS3";

    private static final int BAUD_RATE = 115200;

    private static AngleChangeListener angleChangeListener;

    private static SerialPortDataListener serialPortDataListener;

    /**
     * 打开UART,并设置唤醒角度改变监听器
     *
     * @param acl 唤醒角度改变监听器
     */
    public static void createAgent(AngleChangeListener acl) {
        if (!SerialPortTools.isStarted()) {
            try {
                SerialPortTools.init(FILE_NAME, BAUD_RATE, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            serialPortDataListener = new SerialPortDataListenerImpl();
            SerialPortTools.addDataListener(serialPortDataListener);
        }
        angleChangeListener = acl;
    }


    /**
     * 关闭UART
     */
    public static void destroy() {
        SerialPortTools.removeDataListener(serialPortDataListener);
        SerialPortTools.close();
        serialPortDataListener = null;
        angleChangeListener = null;
    }

    /**
     * 增强正前方拾音
     */
    public static void enhanceAhead() {
        try {
            SerialPortTools.sendCmd("BEAM 0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class SerialPortDataListenerImpl implements SerialPortDataListener {

        private StringBuffer serialPortSb = new StringBuffer();
        private static final String ANGLE_HEAD = "angle:";
        private static final String ANGLE_INFO = "angle:360";

        @Override
        public void onSerialPortDataReceived(byte[] data) {
            String receive = new String(data).trim();
            if (!TextUtils.isEmpty(receive)) {
                serialPortSb.append(receive);
            }
            if (serialPortSb.toString()
                    .contains(ANGLE_HEAD)) {
                int angleIndex = serialPortSb.indexOf(ANGLE_HEAD);
                serialPortSb.delete(0, angleIndex);//删除angle:的前部分
                if (serialPortSb.length() < ANGLE_INFO.length()) {
                    return;
                }
                serialPortSb.delete(ANGLE_INFO.length(), serialPortSb.length());//删除angle:360的后部分
                Matcher matcher = Pattern.compile("(\\d+)").matcher(serialPortSb.toString().trim());
                if (matcher.find()) {
                    int angle = Integer.parseInt(matcher.group(1)) % 360;
                    serialPortSb.delete(0, serialPortSb.length());
                    if (angleChangeListener != null) {
                        angleChangeListener.onAngleChange(angle);
                    }
                }
            }

        }

        @Override
        public void onSerialPortDataSend(byte[] bytes) {

        }
    }
}
