package com.example.musicinfor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnStop;
    private boolean isCast; //是否为广播激活

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{"android.permission.RECEIVE_SMS"},1);
        }
        btnStop=findViewById(R.id.btnStop);
        Intent intent = getIntent(); //获取广播意图对象
        isCast = intent.getBooleanExtra("iscast", false);  //默认值为false
        btnStop.setEnabled(isCast);   //设置停止按钮可用和单击监听
        if(isCast) Toast.makeText(this, "正在播放音乐...", Toast.LENGTH_SHORT).show();
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //显式服务调用意图（非绑定式）
                Intent intent=new Intent(MainActivity.this,MyAudioService.class);
                //在Activity组件里，停止音乐播放服务
                stopService(intent);
                finish();  //销毁本活动
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "未授权，无法实现预定的功能！", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(this, "请发一条短信验证...", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
