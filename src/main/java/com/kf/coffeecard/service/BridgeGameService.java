package com.kf.coffeecard.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Handler;
import android.os.HandlerThread;

import android.os.RemoteException;
import android.util.Log;

public class BridgeGameService extends Service {

    public final static String TAG = "BridgeGameService";
    public final static int EVENT_SERVICE_REGISTER = 1;
    public final static int EVENT_SERVICE_REGISTER_DONE = 2;

    private HandlerThread mThread = null;
    private ServiceHandler mHandler = null;
    private Messenger mMessenger = null;
    private Messenger mClient = null;

    //handle by thread
    private class ServiceHandler extends Handler {
        ServiceHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG,"handleMessage msg = "+msg.what);

            switch (msg.what){
                case EVENT_SERVICE_REGISTER:
                    mClient = msg.replyTo;
                    if(mClient != null){
                        try {
                            mClient.send(obtainMessage(EVENT_SERVICE_REGISTER_DONE));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                 break;
                default:
            }
        }
    }

    public BridgeGameService() {
    }

    @Override
    public void onCreate() {
        mThread = new HandlerThread("service_thread");
        mThread.start();
        mHandler = new ServiceHandler(mThread.getLooper());
        mMessenger = new Messenger(mHandler);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mMessenger.getBinder();
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
