package com.kf.coffeecard.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Handler;
import android.os.HandlerThread;

import android.os.RemoteException;
import android.util.Log;

import com.kf.coffeecard.BridgeGame;
import com.kf.coffeecard.Game;
import com.kf.coffeecard.GameConstants;

import java.util.Objects;


public class BridgeGameService extends Service {

    public final static String TAG = "BridgeGameService";


    private HandlerThread mThread = null;
    private ServiceHandler mHandler = null;
    private Messenger mMessenger = null;
    private Messenger mClient = null;
    private Game mGame = null;

    //handle by thread
    private class ServiceHandler extends Handler {
        ServiceHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            Log.d(GameConstants.TAG,"BridgeGameService: handleMessage = "+msg.what);

            switch (msg.what){
                case GameConstants.EVENT_SERVICE_REGISTER:
                    mClient = msg.replyTo;
                    if(mClient != null){
                        try {
                            mClient.send(obtainMessage(GameConstants.EVENT_SERVICE_REGISTER_DONE));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case GameConstants.EVENT_SERVICE_INIT_GAME:
                    mGame = Game.getGame();
                    mGame.initGame();
                    break;
                case GameConstants.EVENT_SERVICE_BID_CONTRACT:
                    try {
                        bidContract(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (NullPointerException ex){
                        Log.w(TAG, "ex = " + ex.getMessage());
                    }
                    break;
                case GameConstants.EVENT_SERVICE_START_GAME:
                    //start game
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

    private void bidContract(Message msg) throws RemoteException {
        Bundle bundle = (Bundle)msg.obj;
        if(bundle == null)throw new NullPointerException();

        ((BridgeGame)mGame).bidContract(bundle.getInt(GameConstants.CONTRACT_TRICK),bundle.getInt(GameConstants.CONTRACT_SUIT));
        mClient.send(mHandler.obtainMessage(GameConstants.EVENT_SERVICE_BID_CONTRACT_DONE));
    }
}
