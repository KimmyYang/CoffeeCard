package com.kf.coffeecard;

/**
 * Created by kimmy on 15/5/12.
 */
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


public class ServiceManager {
    final String TAG = "ServiceManager";
    private Class<? extends AbstractService> mServiceClass;
    private Context mActivity;
    private boolean mIsBound;
    private Messenger mService = null;
    private Handler mIncomingHandler = null;
    private final Messenger mMessenger = new Messenger(new IncomingHandler());



    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (mIncomingHandler != null) {
                Log.i(TAG, "Incoming message. Passing to handler: "+msg);
                mIncomingHandler.handleMessage(msg);
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            Log.i(TAG, "Attached.");
            /*
            try {
                Message msg = Message.obtain(null, AbstractService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even do anything with it
            }
            */
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
            mService = null;

            Log.i(TAG, "Disconnected.");
        }
    };

    public ServiceManager(Context context, Class<? extends AbstractService> serviceClass, Handler incomingHandler) {
        this.mActivity = context;
        this.mServiceClass = serviceClass;
        this.mIncomingHandler = incomingHandler;

        if (isRunning()) {
            Log.d(TAG,"isRunning");
            doBindService();
        }
    }

    public void start(Intent intent) {
        doStartService(intent);
        doBindService(intent);
        //doBindService();
    }

    public void stop() {
        doUnbindService();
        doStopService();
    }

    /**
     * Use with caution (only in Activity.onDestroy())!
     */
    public void unbind() {
        doUnbindService();
    }

    public boolean isRunning() {
        ActivityManager manager = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);

        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (mServiceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

    public void send(Message msg) throws RemoteException {
        if (mIsBound) {
            if (mService != null) {
                mService.send(msg);
            }
        }
    }

    private void doStartService(Intent intent) {
        mActivity.startService(intent);
    }

    private void doStopService() {
        mActivity.stopService(new Intent(mActivity, mServiceClass));
    }

    private void doBindService(Intent intent) {
        Log.d(TAG,"doBindService , intent :"+intent);
        mActivity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        //mActivity.bindService(intent, mConnection, 0);
        mIsBound = true;
    }
    private void doBindService() {
        mActivity.bindService(new Intent(mActivity, mServiceClass), mConnection, Context.BIND_AUTO_CREATE);
        //mActivity.bindService(new Intent(mActivity, mServiceClass), mConnection, 0);
        mIsBound = true;
    }

    private void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, AbstractService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service has crashed.
                }
            }

            // Detach our existing connection.
            mActivity.unbindService(mConnection);
            mIsBound = false;
            Log.i(TAG, "Unbinding.");
        }
    }
}