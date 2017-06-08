package com.example.syp.bindserviceinanotherapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.anotherapp.IOnRemoteCountChangeListener;
import com.example.anotherapp.IRemoteBinder;

/**
 * AIDL远程回调
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {
    private Intent myServiceIntent;
    private IRemoteBinder binder = null;
    private TextView outputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputText = (TextView) findViewById(R.id.outputText);

        myServiceIntent = new Intent();
        myServiceIntent.setComponent(new ComponentName("com.example.anotherapp", "com.example.anotherapp.MyService"));

        findViewById(R.id.btnBindRemoteService).setOnClickListener(this);
        findViewById(R.id.btnUnbindRemoteService).setOnClickListener(this);
        findViewById(R.id.btnSetCountTo100).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBindRemoteService:
                bindService(myServiceIntent, this, BIND_AUTO_CREATE);
                break;
            case R.id.btnUnbindRemoteService:
                binder = null;
                unbindService(this);
                break;
            case R.id.btnSetCountTo100:
                if (binder != null) {
                    try {
                        binder.setCount(100);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    //会实现上述第一个工程MyService类中的 onBind方法
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = IRemoteBinder.Stub.asInterface(service);

        try {
            binder.addOnRemoteCountChangeListener(new IOnRemoteCountChangeListener.Stub() {
                @Override
                public void onChange(int count) throws RemoteException {
                    final int c = count;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            outputText.setText("Count " + c);
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}

