package com.test.hexun.mytest.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.test.hexun.mytest.utils.LogBiz;


public class MyIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService() {
        super("hxx");
    }
    public MyIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LogBiz.i("MyIntentService  onHandleIntent" + android.os.Process.myTid());
        test();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogBiz.i("MyIntentService  onBind" + android.os.Process.myTid());
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogBiz.i("MyIntentService  onUnbind" + android.os.Process.myTid());
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogBiz.i("MyIntentService  onStartCommand" + android.os.Process.myTid());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        LogBiz.i("MyIntentService  onCreate" + android.os.Process.myTid());
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        LogBiz.i("MyIntentService  onDestroy" + android.os.Process.myTid());
        super.onDestroy();
    }


    @Override
    public void onRebind(Intent intent) {
        LogBiz.i("MyIntentService  onRebind" + android.os.Process.myTid());
        super.onRebind(intent);
    }

    private void test() {
        LogBiz.i("MyIntentService  我在的线程" + android.os.Process.myTid());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LogBiz.i("MyIntentService  我新起的线程" + android.os.Process.myTid());
                synchronized (Thread.currentThread()) {
                    try {
                        Thread.currentThread().wait(1000); //wait线程挂起 释放锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    LogBiz.i("MyIntentService  我新起的线程" + android.os.Process.myTid() + "等了两秒结束");
                }
            }
        };
        new Thread(runnable).start();
    }
}
