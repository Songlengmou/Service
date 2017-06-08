package com.example.anotherapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList

/**
 * Created by syp on 17-5-19.
 * 2.AIDL进程间通信
 */
open class MyService : Service() {

    private var count = 0
    private var running = false
    private var countChangeListeners = RemoteCallbackList<ICountChangeListener>()


    override fun onBind(intent: Intent?): IBinder {
        return MyBinder()
    }

    inner class MyBinder : IRemoteBinder.Stub() {
        override fun registerCountChangeListener(listener: ICountChangeListener?) {
            this@MyService.registerCountChangeListener(listener)
        }

        //ICountChangeListener?  问号是防止空指针
        override fun unregisterCountChangeListener(listener: ICountChangeListener?) {
            this@MyService.unregisterCountChangeListener(listener)
        }


        override fun getCount(): Int {
            return this@MyService.count
        }

        override fun setCount(count: Int) {
            this@MyService.count = count
        }
    }

    override fun onCreate() {
        super.onCreate()

        running = true
        println("====================MyService.onCreate")

        Thread(Runnable {

            while (running) {
                count++

                countChangeListeners.beginBroadcast()

                var index = 0
                while (index < countChangeListeners.registeredCallbackCount) {
                    countChangeListeners.getBroadcastItem(index).onChange(count)
                    index++
                }

                countChangeListeners.finishBroadcast()

                println(count)

                Thread.sleep(1000)
            }
        }).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        running = false

        println("====================MyService.onDestroy")
    }

    fun registerCountChangeListener(listener: ICountChangeListener?) {
        countChangeListeners.register(listener)
    }

    fun unregisterCountChangeListener(listener: ICountChangeListener?) {
        countChangeListeners.unregister(listener)
    }
}