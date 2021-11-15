package com.szyh.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.szyh.ewaasdk.audio.AudioEngineProxy;
import com.szyh.ewaasdk.audio.RecognizeListener;
import com.szyh.ewaasdk.audio.ResponseListener;
import com.szyh.ewaasdk.audio.SongEntity;
import com.szyh.ewaasdk.audio.SynthesizerListener;

import java.util.List;

public class AudioActivity extends BaseActivity
        implements View.OnClickListener, RecognizeListener, SynthesizerListener, ResponseListener {
    private String TAG = "MainActivity";
    private Button startRe, stopRe, startTTS, stopTTS, startPlay, stopPlay, clearBtn;
    private TextView tvReco;
    private Handler handler;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("语音语义测试");
        setContentView(R.layout.activity_audio);
        startRe = (Button) findViewById(R.id.id_start_recognize);
        stopRe = (Button) findViewById(R.id.id_stop_recognize);
        startTTS = (Button) findViewById(R.id.id_start_tts);
        stopTTS = (Button) findViewById(R.id.id_stop_tts);
        startPlay = (Button) findViewById(R.id.id_start_play);
        stopPlay = (Button) findViewById(R.id.id_stop_play);
        clearBtn = (Button) findViewById(R.id.id_clear);
        scrollView = (ScrollView) findViewById(R.id.scroll_reco);
        tvReco = (TextView) findViewById(R.id.tv_reco_result);

        startRe.setOnClickListener(this);
        stopRe.setOnClickListener(this);
        startTTS.setOnClickListener(this);
        stopTTS.setOnClickListener(this);
        startPlay.setOnClickListener(this);
        stopPlay.setOnClickListener(this);
        clearBtn.setOnClickListener(this);

        if (AudioEngineProxy.getInstance().isMediaPlaying()) {
            startPlay.setText("暂停播放");
        } else {
            startPlay.setText("开始播放");
        }
        handler = new Handler();
        setLister();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setLister() {
        if (AudioEngineProxy.getInstance().isInited()) {
            AudioEngineProxy.getInstance().setRecognizeLister(this);//语音识别监听器
            AudioEngineProxy.getInstance().setResponseListener(this);//语义回调监听器
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setLister();
                }
            }, 1000);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_start_recognize:
                AudioEngineProxy.getInstance().startRecognize();
                break;
            case R.id.id_stop_recognize:
                AudioEngineProxy.getInstance().stopRecognize();
                break;
            case R.id.id_start_tts:
                AudioEngineProxy.getInstance().startSpeakAbsolute("您好，我是艾娃");
                break;
            case R.id.id_stop_tts:
                AudioEngineProxy.getInstance().stopSpeakingAbsolte();
                break;
            case R.id.id_start_play:
                AudioEngineProxy.getInstance().playPause();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setPlayStatus();
                    }
                }, 500);
                break;
            case R.id.id_stop_play:
                AudioEngineProxy.getInstance().stopMedia();
                break;
            case R.id.id_clear:
                tvReco.setText("");
                break;
            default:
                break;

        }
    }

    private void setPlayStatus() {
        if (AudioEngineProxy.getInstance().isMediaPlaying()) {
            startPlay.setText("暂停播放");
        } else {
            startPlay.setText("开始播放");
        }
    }

    @Override
    public void onRecoginzeBegin() {

    }

    @Override
    public void onRecoginzeVolumeChanged(int vol) {

    }

    @Override
    public void onRecoginzeEnd() {

    }

    @Override
    public void onRecognizeStop() throws RemoteException {

    }

    @Override
    public boolean onRecoginzeResult(String result) {
        tvReco.append("  user: ");
        tvReco.append(result);
        tvReco.append("\n");
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        return false;
    }

    @Override
    public void onRobotResponse(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvReco.append("robot: ");
                tvReco.append(text);
                tvReco.append("\n");
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                AudioEngineProxy.getInstance().startSpeakAbsolute(text);
            }
        });
    }

    @Override
    public void onRecognizeError(int e) {

    }

    @Override
    public void onRecognizeTimeout() {
        //2分钟后超时回调
    }

    @Override
    public void onPlayResponse(List<SongEntity> songEntity) {
        AudioEngineProxy.getInstance().prepareOnlineMusic(songEntity);
    }

    @Override
    public void onCustomResponse(String reqText, String respText, String s2) throws RemoteException {

    }

    @Override
    public IBinder asBinder() {
        return null;
    }

    @Override
    public void onSynthesizerInited(int i) {

    }

    @Override
    public void onSynthersizeCompleted(int i) {

    }

    @Override
    public void onSynthersizeSpeakBegin() {

    }

    @Override
    public void onSynthersizeError(int i) {

    }

    @Override
    public void onSpeakProgress(int i) {

    }
}
