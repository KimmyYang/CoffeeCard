package com.kf.coffeecard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.kf.coffeecard.CardSet;
import com.kf.coffeecard.Game;
import com.kf.coffeecard.GameRule;
import com.kf.coffeecard.Card;
import com.kf.coffeecard.GameProxyService;
import com.kf.coffeecard.R;
import com.kf.coffeecard.ServiceManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Vector;


public class BridgeGameActivity extends Activity {

    final String TAG = "BridgeGameActivity";
    final int DISPALY_GAME_TIME_DELAY = 2000;
    //final int MAX_CARD_PER_PLAYER = 3;//fake to test
    private ImageView mSplashIv;
    private ImageView[] mCardImageViews;
    private ImageView[] mBackImageViews;
    private ServiceManager mGameProxyService;
    private Handler mHandler = new Handler();
    private HandlerThread mThread = null;
    private Messenger mReplyMessager = new Messenger(new ReplyHandler());
    private Game mGame = null;

    private Handler mInnerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ServiceManager.SM_SERVICE_ATTACHED:
                    Log.i(TAG, "Received SM_SERVICE_ATTACHED");
                    sendGameInfoMsg();
                    mHandler.postDelayed(displayGame , DISPALY_GAME_TIME_DELAY);
                    break;
                default:
                    break;
            }
        }
    };

    private class ReplyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (mReplyMessager != null) {
                Log.i(TAG, "Incoming message. Passing to handler: "+msg);
                switch (msg.what){
                    case GameProxyService.GPSERVICE_GET_GAME_INFO_RESP:
                        Log.i(TAG,"received GPSERVICE_GET_GAME_INFO_RESP");
                        mGame = (Game)msg.obj;
                        final GameRule rule  = mGame.getGameRule();
                        mCardImageViews = new ImageView[rule.getPerPlayerOfCards()];
                        //mBackImageViews = new ImageView[rule.getTotalCards()-rule.getPerPlayerOfCards()];
                        //mBackImageViews = new ImageView[rule.getPerPlayerOfCards()];//test
                        //mBackImageViews = new ImageView[4];//test
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_game_splash);
        Log.d(TAG,"onCreate");

        mGameProxyService = new ServiceManager(this , GameProxyService.class , mInnerHandler, new Handler(){
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){

                    //case GameProxyService.GPSERVICE_GET_GAME_INFO_RESP:
                     //   Log.i(TAG,"received GPSERVICE_GET_GAME_INFO_RESP");
                       // break;
                    default:
                        break;
                }

            }
        });
        mGameProxyService.bind();//bind service
    }

    private void initCardImageView(){
        if(mCardImageViews==null){
            Log.wtf(TAG,"mCardImageViews is null");
            return;
        }

        //init local var
        String wt = "iv";
        int cnt = 0;
        int resID = 0;

        /* PLAY 1 (Main player)*/
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

        GameRule rule = mGame.getGameRule();
        Vector<Card> cardVector = mGame.getMyCard();
        if(cardVector.size() != rule.getPerPlayerOfCards() ||
           mCardImageViews.length != rule.getPerPlayerOfCards()){//TEST
            Log.wtf(TAG,"The amount of the cards is not correct. " +
                    "(cardVector size="+cardVector.size()+" mCardImageViews size="+mCardImageViews.length);
            return;
        }

        /* PLAY 1 */
        String wt = "c";
        for(int i=0; i<cardVector.size(); ++i){
            Card card = cardVector.elementAt(i);
            String cardName = wt+card.getId();//for test
            int resID = getResources().getIdentifier(cardName,"drawable",getPackageName());
            Log.d(TAG,"card Name/card id/resID = "+cardName+"/"+card.getId()+"/"+resID);
            mCardImageViews[i].setImageResource(resID);
        }

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
            setCardImageView();
        }
    };

    protected void onStart(){
        super.onStart();
        Log.d(TAG,"onStart");
        mSplashIv = (ImageView) findViewById(R.id.iv_bridge_game_splash);
        mSplashIv.setImageResource(R.drawable.bridge_game_splash);
/*
        mThread = new HandlerThread("BGThread");
        mThread.start();
        mThreadHandler = new Handler(mThread.getLooper());
        */
    }
    protected void onResume(){
        super.onResume();
        Log.d(TAG,"onResume");
    }

    private void sendGameInfoMsg(){
        try {
            Log.i(TAG,"send GPSERVICE_GET_GAME_INFO_REQ");
            Message msg = Message.obtain(null , GameProxyService.GPSERVICE_GET_GAME_INFO_REQ);
            msg.replyTo = mReplyMessager;
            mGameProxyService.send(msg);
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
