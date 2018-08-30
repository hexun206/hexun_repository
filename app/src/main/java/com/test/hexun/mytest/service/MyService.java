package com.test.hexun.mytest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.test.hexun.mytest.utils.LogBiz;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends Service {

    MyBinder binder = new MyBinder();

    public MyService() {
        LogBiz.i("MyService 我在的线程：" + android.os.Process.myTid());
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogBiz.i("MyService  onBind" + android.os.Process.myTid());
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogBiz.i("MyService  onUnbind" + android.os.Process.myTid());
//        stop = true;
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogBiz.i("MyService  onStartCommand" + android.os.Process.myTid());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        LogBiz.i("MyService  onCreate" + android.os.Process.myTid());
        test();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        LogBiz.i("MyService  onDestroy" + android.os.Process.myTid());
        stop = true;
        super.onDestroy();
    }


    @Override
    public void onRebind(Intent intent) {
        LogBiz.i("MyService  onRebind" + android.os.Process.myTid());
        super.onRebind(intent);
    }

    boolean stop = false;
    long lastTime = 0;

    private void test() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LogBiz.i("MyService  我新起的线程" + android.os.Process.myTid());
                synchronized (Thread.currentThread()) {
                    stop = false;
                    while (!stop) {
                        long currentTime = System.currentTimeMillis();
                        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTime));
                        if (dataListener != null) {
                            dataListener.dataChanged(time);
                        }
                        try {
//                            Thread.currentThread().wait(1000); //wait线程挂起 释放锁
                            Thread.currentThread().sleep(1000); //sleep线程休眠 不释放锁
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (currentTime - lastTime > 15000) {
                            lastTime = currentTime;
                            LogBiz.i("MyService  我正在运行" + time);
                        }
                    }
//                    LogBiz.i("MyService  我新起的线程" + android.os.Process.myTid() + "等了两秒结束");
                }
            }
        };
        new Thread(runnable).start();
    }

    DataListener dataListener;

    public void setDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }

    public interface DataListener {
        void dataChanged(String data);
    }


    public class MyBinder extends android.os.Binder {

        public MyService getService() {
            return MyService.this;
        }


    }
}
