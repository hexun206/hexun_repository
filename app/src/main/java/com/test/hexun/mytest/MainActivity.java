package com.test.hexun.mytest;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.test.hexun.mytest.service.MyIntentService;
import com.test.hexun.mytest.service.MyService;
import com.test.hexun.mytest.utils.LogBiz;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {
    TextView txCount;
    ToggleButton toggleButton;
    MyService myService;
    ServiceConnection serviceConnection;
    IntentFilter intentFilter;
    MyLocalReciver myLocalReciver;
    LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogBiz.i("我在的线程：" + android.os.Process.myTid());
        txCount = (TextView) findViewById(R.id.textview);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.test.hexun.mytest.MY_LOCAL_BOADCASE");
        myLocalReciver = new MyLocalReciver();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(myLocalReciver, intentFilter);
        initService();
        toggleButton.setChecked(false);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                localBroadcastManager.sendBroadcast(new Intent("com.test.hexun.mytest.MY_LOCAL_BOADCASE").putExtra("isLocked", isChecked));
                Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {

                    }
                }).subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {

                    }
                });
            }
        });

    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    txCount.setText("" + msg.obj);
                    break;
                case 2:
                    if ((boolean) msg.obj) {
                        bindService(new Intent(MainActivity.this, MyService.class), serviceConnection, BIND_AUTO_CREATE);
                        LogBiz.i("我要绑定线程：" + android.os.Process.myTid());
                    } else {
                        unbindService(serviceConnection);
                        LogBiz.i("我要解绑线程：" + android.os.Process.myTid());
                    }
                    break;
            }
            return false;
        }
    });


    void initService() {
//        startService(new Intent(this, MyService.class));
        startService(new Intent(this, MyIntentService.class).putExtra("name", "lala"));
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                myService = ((MyService.MyBinder) service).getService();
                myService.setDataListener(new MyService.DataListener() {
                    @Override
                    public void dataChanged(String data) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        msg.obj = data;
                        msg.sendToTarget();
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(new Intent(this, MyService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbindService(serviceConnection);
//        stopService(new Intent(this, MyService.class));
        localBroadcastManager.unregisterReceiver(myLocalReciver);
    }

    class MyLocalReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogBiz.i("收到广播消息的线程：" + android.os.Process.myTid());
            Message msg = mHandler.obtainMessage();
            msg.what = 2;
            msg.obj = intent.getBooleanExtra("isLocked", false);
            msg.sendToTarget();
            
        }
    }

}
