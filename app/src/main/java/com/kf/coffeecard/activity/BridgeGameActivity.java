package com.kf.coffeecard.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Handler;
import android.os.Message;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.view.Gravity;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;

import com.kf.coffeecard.BridgeGame;
import com.kf.coffeecard.Game;
import com.kf.coffeecard.GameConstants;
import com.kf.coffeecard.R;
import com.kf.coffeecard.fragment.PlayerFragment;
import com.kf.coffeecard.service.BridgeGameService;

import java.util.ArrayList;
import java.util.Objects;


public class BridgeGameActivity extends Activity {

    final String TAG = "BridgeGameActivity";
    final int DISPALY_GAME_TIME_DELAY = 1000;

    private ImageView mSplashIv;
    private ArrayList<PlayerFragment> mPlayerFragList = null;
    private Handler mHandler = new ClientHandler();
    private Game mGame = null;

    private Messenger mClient = null;
    private Messenger mService = null;

    private class ClientHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            Log.d(TAG,"handleMessage msg = "+msg.what);
            switch (msg.what){
                case GameConstants.EVENT_SERVICE_REGISTER_DONE:
                    startGame();
                    mHandler.sendMessageDelayed(obtainMessage(GameConstants.EVENT_CLIENT_DISPLAY_GAME), DISPALY_GAME_TIME_DELAY);
                    break;
                case GameConstants.EVENT_CLIENT_DISPLAY_GAME:
                    displayGame();
                    break;
                default:
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            mClient = new Messenger(mHandler);
            Message msg = Message.obtain();
            msg.what = GameConstants.EVENT_SERVICE_REGISTER;
            msg.replyTo = mClient;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_game_splash);
        Log.d(TAG,"onCreate");
        //init game, rule and player
        createGame(getIntent().getExtras());

        Intent intent = new Intent(this, BridgeGameService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void createGame(Bundle bundle){
        mGame = BridgeGame.createGame(bundle);
    }

    private void startGame(){
        sendMessage(GameConstants.EVENT_SERVICE_START_GAME);
    }

    private void displayGame(){
        Log.d(TAG,"displayGame");
        setContentView(R.layout.activity_bridge_game);
        setCardImageView();
    }

    private void setCardImageView(){

        mPlayerFragList = new ArrayList<PlayerFragment>();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        for(int i=0; i< mGame.getNumberOfPlayer(); ++i){
            PlayerFragment frag = new PlayerFragment();
            frag.setPlayer(mGame.getPlayer(i));
            //find framelayout
            int resId = getResources().getIdentifier("fragment_container_"+(i+1), "id", getPackageName());
            Log.d(TAG,"initCardImageView: resId = "+resId);
            fragmentTransaction.add(resId, frag);
            mPlayerFragList.add(frag);
            Log.d(TAG,"[initCardImageView] init player "+i+" done");
        }
        fragmentTransaction.commit();
    }

    protected void onStart(){
        super.onStart();
        Log.d(TAG,"onStart");
        mSplashIv = (ImageView) findViewById(R.id.iv_bridge_game_splash);
        mSplashIv.setImageResource(R.drawable.bridge_game_splash);
    }

    protected void onResume(){
        super.onResume();
        Log.d(TAG,"onResume");
    }

    private void sendMessage(int what){
        Message msg = Message.obtain();
        msg.what = what;
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(int what, Objects obj){
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(int what, int arg1, int arg2, Objects obj){
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bridge_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
