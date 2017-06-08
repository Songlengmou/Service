package com.example.syp.startservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;

/**
 * Service
 * <p>
 * <p>
 * onStartCommand()方法：
 * 启动service的时候，onCreate方法只有第一次会调用，onStartCommand和onStart每次都被调用。onStartCommand会告诉系统如何重启服务，如判断是否异常终止后重新启动，在何种情况下异常终止
 * onStartComand使用时，返回的是一个(int)整形。
 * 这个整形可以有四个返回值：start_sticky、start_no_sticky、START_REDELIVER_INTENT、START_STICKY_COMPATIBILITY。
 */
public class MyService extends Service {

    public interface OnCountChangeListener{
        void onChange(int count);
    }


    private boolean running = false;
    private int count = 0;


    private OnCountChangeListener onCountChangeListener;


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public void SetCount(int count) {
            MyService.this.setCount(count);
        }

        public int getCount() {
            return MyService.this.getCount();
        }

        public void setOnCountChangeListener(OnCountChangeListener listener){
            MyService.this.setOnCountChangeListener(listener);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        running = true;

        new Thread() {
            @Override
            public void run() {
                super.run();

                while (running) {
                    try {
                        sleep(1000);

                        count++;

                        if (getOnCountChangeListener() != null) {
                            getOnCountChangeListener().onChange(count);
                        }

                        System.out.println(count);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        System.out.println("MyService.onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("MyService.onStartCommand");
        int count = intent.getIntExtra("count", 0);
        if (count != 0) {
            this.count = count;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    //自己写的一个get set方法
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public void setOnCountChangeListener(OnCountChangeListener onCountChangeListener) {
        this.onCountChangeListener = onCountChangeListener;
    }

    public OnCountChangeListener getOnCountChangeListener() {
        return onCountChangeListener;
    }

    @Override
    public void onDestroy() {
        System.out.println("MyService.onDestroy");
        super.onDestroy();

        running = false;
    }

}
