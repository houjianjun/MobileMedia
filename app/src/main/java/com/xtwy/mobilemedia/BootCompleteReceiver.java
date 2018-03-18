package com.xtwy.mobilemedia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 创建时间： 2018/3/17.
 * 作    者： 侯建军
 * 功能描述： 启动完成广播服务
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "BootCompleteReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"手机启动完毕了，监视到了手机启动的广播事件，开启后台监听的服务");
        Intent i = new Intent(context,PhoneStatusService.class);
        //开始监听服务
        context.startService(i);
    }
}
