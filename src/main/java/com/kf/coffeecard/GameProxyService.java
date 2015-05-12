/**
 * Created by kimmy on 15/5/11.
 */
package com.kf.coffeecard;

import android.content.Intent;
import android.os.IBinder;
import android.content.Context;
import com.kf.coffeecard.Game.GameType;
import android.util.Log;
import android.os.Message;

public class GameProxyService extends AbstractService {

    private final String TAG = "GameProxyService";
    private Context mContext = null;
    private GameType mGameType;
    private int mPlayers;
    private String mName;
    private static boolean isInit = true;
/*
    public void onCreate(){
        Log.d(TAG,"onCreate");
        isInit = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand , isInit "+isInit);

        //if(intent == null)return START_STICKY;

        if(isInit){
            mContext = getBaseContext();
            int index = intent.getIntExtra("gametype",0);
            Log.d(TAG,"index = "+index);
            if(!Game.isVaild(index))throw new RuntimeException("Invaild GameType Index : "+index);
            mGameType = GameType.values()[index];
            mPlayers = intent.getIntExtra("players",0);
            mName = intent.getStringExtra("name");

            Log.i(TAG,mName+" Start the game "+mGameType+" with "+mPlayers+" players");

            switch (mGameType){
                case BRIDGE:
                case SEVENS:
                case LIAR:
                    GameFactory.createGame(new GameRule("",mGameType,mPlayers,mPlayers,1),GetPlayerSet());
                    break;
                case BLACK_JACK:
                default:
                    break;
            }

        }
        return START_STICKY;
    }
*/
    @Override
    public void onStartService(Intent intent){
        Log.d(TAG,"onStartService , isInit "+isInit);

        if(isInit){
            mContext = getBaseContext();
            int index = intent.getIntExtra("gametype",0);
            Log.d(TAG,"index = "+index);
            if(!Game.isVaild(index))throw new RuntimeException("Invaild GameType Index : "+index);
            mGameType = GameType.values()[index];
            mPlayers = intent.getIntExtra("players",0);
            mName = intent.getStringExtra("name");

            Log.i(TAG,mName+" Start the game "+mGameType+" with "+mPlayers+" players");

            switch (mGameType){
                case BRIDGE:
                case SEVENS:
                case LIAR:
                    GameFactory.createGame(new GameRule("",mGameType,mPlayers,mPlayers,1),GetPlayerSet());
                    break;
                case BLACK_JACK:
                default:
                    break;
            }
            isInit = false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /*
    fake player info
    */
    private Player[] GetPlayerSet(){
        String name;
        Player[] players = new Player[mPlayers];
        for(int i=0;i<mPlayers;++i){
            if(i == 0)name = mName;
            else name = Integer.toString(i);
            Player player = new Player(name , i);
            players[i] = player;
        }
        return players;
    }

    @Override
    public void onStopService(){
        Log.i(TAG,"onStopService");
    }
    @Override
    public void onReceiveMessage(Message msg){


    }

}
