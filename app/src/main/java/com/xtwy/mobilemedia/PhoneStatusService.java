package com.xtwy.mobilemedia;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 创建时间： 2018/3/17.
 * 作    者： 侯建军
 * 功能描述：电话录音服务程序
 */
public class PhoneStatusService extends Service {
    public static final String TAG = "PhoneStatusService";
    //电话状态的监听器
    private PhoneListener phoneListener;
    //声明手机电话系统状态管理的服务类
    private TelephonyManager tm;
    /**
     * 功能：实出方抽象方法
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //当服务第一次被创建的时候执行，由系统执行的。
    @Override
    public void onCreate() {
        //获取手机电话状态管理的服务。
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        Log.e(TAG,"服务开始");
        //创建一个监听器，监听电话呼叫状态的变化。
        phoneListener = new PhoneListener();
        //开始监听用户的通话状态
        tm.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }
    //当服务被停止的时候调用。
    @Override
    public void onDestroy() {
        //服务停止取消电话的监听器
        tm.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
        phoneListener = null;
        super.onDestroy();
    }
}
