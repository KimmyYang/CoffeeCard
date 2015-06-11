/**
 * Created by kimmy on 15/5/11.
 */
package com.kf.coffeecard;

import android.content.Intent;
import android.os.IBinder;
import android.content.Context;
import com.kf.coffeecard.Game.GameType;

import android.os.Parcelable;
import android.util.Log;
import android.os.Message;
import android.os.Bundle;
import android.os.Parcel;
import java.util.Objects;

public class GameProxyService extends AbstractService {

    private final String TAG = "GameProxyService";
    private Context mContext = null;
    private boolean isInit = true;
    private Game mGame = null;
    /* handle msg id */
    public static final int GPSERVICE_GET_GAME_INFO_REQ = 10;
    public static final int GPSERVICE_GET_GAME_INFO_RESP = 20;

    @Override
    public void onStartService(Intent intent){//init
        Log.d(TAG,"onStartService , isInit "+isInit);

        if(isInit && intent!=null){
            mContext = getBaseContext();
            int index = intent.getIntExtra("gametype",0);
            Log.d(TAG,"index = "+index);
            if(!Game.isVaild(index))throw new RuntimeException("Invaild GameType Index : "+index);
            GameType gameType = GameType.values()[index];
            int playersNum = intent.getIntExtra("players",0);
            String playerName = intent.getStringExtra("name");

            Log.i(TAG,playerName+" Start the game "+gameType+" with "+playersNum+" players");

            switch (gameType){
                case BRIDGE:
                case SEVENS:
                case LIAR:
                    mGame = GameFactory.createGame(new GameRule("",gameType,playersNum,playersNum,1),GetPlayerSet(playerName,playersNum));
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
        return super.onBind(intent);
    }
    /*
    fake player info
    */
    private Player[] GetPlayerSet(String playerName , int playersNum){
        String name;
        Player[] players = new Player[playersNum];
        for(int i=0;i<playersNum;++i){
            if(i == 0)name = playerName;
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
    public void onReceiveMessage(Message msg) {


    }

    @Override
    public void GetGame(){
        if(mGame == null)throw new RuntimeException("Access null pointer : mGame");
        Message msg = new Message();
        msg.what = GPSERVICE_GET_GAME_INFO_RESP;
        msg.obj = mGame;
        //Bundle bundle = new Bundle();
        //Parcel dest = Parcel.obtain();
        //dest.writeParcelable((Parcelable) mGame.getGameRule(), 0);
        //Object objGameRule = (Object)mGame.getGameRule();

        //bundle.putParcelable("gamerule",(Parcelable)dest);
        //bundle.putParcelableArray("players", (Parcelable[]) mGame.getPlayers());
        //msg.setData(bundle);
        send(msg);
    }
}
