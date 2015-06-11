/**
 * Created by kimmy on 15/5/12.
 */
package com.kf.coffeecard;


import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public abstract class AbstractService extends Service {

    final String TAG = "AbstractService";
    public static final int MSG_REGISTER_CLIENT = 9991;
    public static final int MSG_REGISTER_CLIENT_RESP = 9992;
    public static final int MSG_UNREGISTER_CLIENT = 9993;

    ArrayList<Messenger> mClients = new ArrayList<Messenger>(); // Keeps track of all current registered clients.
    final Messenger mMessenger = new Messenger(new IncomingHandler()); // Target we publish for clients to send messages to IncomingHandler.

    private class IncomingHandler extends Handler { // Handler of incoming messages from clients.
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    Log.i(TAG, "Client registered: "+msg.replyTo);
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    Log.i(TAG, "Client un-registered: "+msg.replyTo);
                    mClients.remove(msg.replyTo);
                    break;
                case GameProxyService.GPSERVICE_GET_GAME_INFO_REQ:
                    Log.i(TAG,"receive GPSERVICE_GET_GAME_INFO_REQ");
                    mClients.add(msg.replyTo);
                    GetGame();
                     /*
                    try {
                        Log.i(TAG,"send GPSERVICE_GET_GAME_INFO_RESP");
                        Messenger replyMsg = msg.replyTo;
                        replyMsg.send( Message.obtain(null , GameProxyService.GPSERVICE_GET_GAME_INFO_RESP));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    */



                    break;
                default:
                    onReceiveMessage(msg);
            }
        }
    }
    protected void send(Message msg) {

        for (int i=mClients.size()-1; i>=0; i--) {
            try {
                Log.i(TAG, "Sending message to clients: "+msg+" i:"+i+" mClients size:"+mClients.size());
                mClients.get(i).send(msg);
            }
            catch (RemoteException e) {
                // The client is dead. Remove it from the list; we are going through the list from back to front so this is safe to do inside the loop.
                Log.e(TAG, "Client is dead. Removing from list: "+i);
                mClients.remove(i);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service Started.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        onStartService(intent);
        return START_STICKY; // run until explicitly stopped.
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        onStopService();

        Log.i(TAG, "Service Stopped.");
    }



    public abstract void onStartService(Intent intent);
    public abstract void onStopService();
    public abstract void onReceiveMessage(Message msg);
    public abstract void GetGame();

}