package com.xtwy.mobilemedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * 日期：2018/3/17-10:19
 * 作者：侯建军
 * 功能：电话多媒体使用
 */
public class MainActivity extends AppCompatActivity {
    private Button btnStartRecord;
    private Button btnStopRecord;
    private String TAG="Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取按钮对象
        btnStartRecord=(Button)findViewById(R.id.btnStartRecord);
        btnStopRecord=(Button)findViewById(R.id.btnStopRecord);
        //开始录音
        btnStartRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"录音权限已经获取");
                //定义服务的意图对象
                Intent intent = new Intent(MainActivity.this,PhoneStatusService.class);
                startService(intent);
            }
        });
        //停止录音
        btnStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PhoneStatusService.class);
                stopService(intent);
            }
        });
    }
}
