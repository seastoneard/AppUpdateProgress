package com.zhh.appupdateprogress;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.zhh.appupdateprogress.view.AppUpdateProgressDialog;

public class MainActivity extends AppCompatActivity {

    private AppUpdateProgressDialog dialog=null;
    private int mProgress = 0;//下载进度
    private int mMaxProgress;//百分比

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMaxProgress = 100;
        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress=0;
                showDialog();
            }
        });
    }

    private void showDialog() {
        dialog = new AppUpdateProgressDialog(this);
        //正在下载时，不可关闭dialog
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    Toast.makeText(MainActivity.this, "正在下载请稍后", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    return false;
                }
            }
        });
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgress < mMaxProgress) {
                    try {
                        mProgress++;
                        Message msg =myHandler.obtainMessage();
                        msg.what = 100;
                        msg.obj = mProgress;
                        myHandler.sendMessage(msg);
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    int progress = (int) msg.obj;
                    dialog.setProgress(progress);
                    if (100 == progress) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this,"下载完成，跳转到安装界面",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
}
