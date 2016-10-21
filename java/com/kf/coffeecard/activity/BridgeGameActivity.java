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
import android.widget.ImageView;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;

import com.kf.coffeecard.Game;
import com.kf.coffeecard.R;
import com.kf.coffeecard.fragment.PlayerFragment;
import com.kf.coffeecard.service.BridgeGameService;


public class BridgeGameActivity extends Activity {

    final String TAG = "BridgeGameActivity";
    final int DISPALY_GAME_TIME_DELAY = 2000;
    //final int MAX_CARD_PER_PLAYER = 3;//fake to test
    private ImageView mSplashIv;
    private PlayerFragment[] mPlayerFragments = null;
    private Handler mHandler = new Handler();
    private HandlerThread mThread = null;
    private Game mGame = null;

    private Messenger mClient = null;
    private Messenger mService = null;

    private class ClientHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            Log.d(TAG,"handleMessage msg = "+msg.what);
            switch (msg.what){
                case BridgeGameService.EVENT_SERVICE_REGISTER_DONE:
                    mHandler.postDelayed(displayGame , DISPALY_GAME_TIME_DELAY);
                    break;
                default:
            }
        }

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            mClient = new Messenger(new ClientHandler());
            Message msg = Message.obtain();
            msg.what = BridgeGameService.EVENT_SERVICE_REGISTER;
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

        Intent intent = new Intent(this, BridgeGameService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void initCardImageView(){
        if(mPlayerFragments==null){
            Log.wtf(TAG,"mPlayerFragments is null");
            return;
        }
        Log.d(TAG,"[initCardImageView] new fragment");
        //mPlayerFragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString("kimmy", "A");
        //mPlayerFragment.setArguments(args);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //ft.add(R.id.fragment_container, mPlayerFragment);
        ft.commit();
        Log.d(TAG,"[initCardImageView] new fragment done");
/*
        //PlayerFragment fragment = (PlayerFragment)getFragmentManager().findFragmentById(R.id.playerFagment);
        //PlayerFragment fragment = PlayerFragment.newInstance("A","B");
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString("kimmy", "A");
        fragment.setArguments(args);
*/

        //init local var
/*
        String wt = "iv";
        int cnt = 0;
        int resID = 0;
*/
        /* PLAY 1 (Main player)*/
/*
        for(int i=0; i<mCardImageViews.length; ++i){
            int ivId = i+1;
            resID = getResources().getIdentifier(wt+ivId,"id",getPackageName());
            ImageView iv = (ImageView) findViewById(resID);
            if(iv == null){
                Log.wtf(TAG,"ImageView is null, resID="+resID);
                continue;
            }
            ++cnt;
            mCardImageViews[i] = iv;
        }
        Log.d(TAG,"mCardImageViews size = "+mCardImageViews.length+" , cnt="+cnt);

        /* PLAY 2~4 */
        //int perPlayerCards = mGame.getGameRule().getPerPlayerOfCards();
        /*
        int perPlayerCards = 4;
        for(int i=0;i< perPlayerCards;++i){
            ++cnt;
            resID = getResources().getIdentifier(wt+cnt,"id",getPackageName());
            ImageView iv = (ImageView) findViewById(resID);
            if(iv == null){
                Log.wtf(TAG,"Back ImageView is null, resID="+resID);
                continue;
            }
            mBackImageViews[i] = iv;
        }
        */
    }
    private void setCardImageView(){
/*
        GameRule rule = mGame.getGameRule();
        Vector<Card> cardVector = mGame.getMyCard();
        if(cardVector.size() != rule.getPerPlayerOfCards() ||
           mCardImageViews.length != rule.getPerPlayerOfCards()){//TEST
            Log.wtf(TAG,"The amount of the cards is not correct. " +
                    "(cardVector size="+cardVector.size()+" mCardImageViews size="+mCardImageViews.length);
            return;
        }
*/

        /* PLAY 1 */
/*
        String wt = "c";
        for(int i=0; i<cardVector.size(); ++i){
            Card card = cardVector.elementAt(i);
            String cardName = wt+card.getId();//for test
            int resID = getResources().getIdentifier(cardName,"drawable",getPackageName());
            Log.d(TAG,"card Name/card id/resID = "+cardName+"/"+card.getId()+"/"+resID);
            mCardImageViews[i].setImageResource(resID);
        }
*/
        /* PLAY 2~4 */
        /*
        for(int i=0;i<mBackImageViews.length;++i){
            mBackImageViews[i].setImageResource(R.drawable.back);
            mBackImageViews[i].setRotation(90);
        }
        */
    }

    private Runnable displayGame = new Runnable(){
        @Override
        public void run() {
            Log.d(TAG,"displayGame");
            setContentView(R.layout.activity_bridge_game);
            initCardImageView();
            //setCardImageView();
        }
    };

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
