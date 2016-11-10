package com.kf.coffeecard;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Objects;
import java.util.Vector;

/**
 * Created by kimmy on 15/4/6.
 */

public abstract class Game {

    public enum GameType{
        BRIDGE,
        BLACK_JACK,
        SEVENS,
        LIAR;
    }

    public class GameState{
        public final static int IDLE = 0;
        public final static int BID_CONTRACT = 1;
        public final static int GAME_START = 2;
        public final static int GAME_END = 3;
        public int state = IDLE;
    }

    private static final String TAG = "Game";
    protected static Game mInstance = null;
    protected Player mPlayers[];
    protected GameRule mGameRule;
    private GameState mState = null;

    public abstract void initGame();
    protected abstract void Deal();
    protected abstract void ArrangeCard();
    protected abstract void WeightCalculated();
    public abstract Player getMainPlayer();

    public Game(GameRule rule , Player player[]) {
        mGameRule = rule;
        mPlayers = player;
        mState = new GameState();
    }

    public static Game getGame(){
        if(mInstance == null){
            throw new NullPointerException("Hasn't created the game");
        }
        return mInstance;
    }

    public static boolean isVaild(int index) {
        if (index > GameType.values().length) return false;
        return true;
    }
    public GameRule getGameRule(){return mGameRule;}
    public Player[] getPlayers(){return mPlayers;}
    public int getNumberOfPlayer(){
        return mGameRule.getNumberOfPlayers();
    }

    public Player getPlayer(int index){
        if(index>=0 && index < mPlayers.length){
            return mPlayers[index];
        }
        Log.e(TAG,"getPlayer: Player can't find, index = "+index);
        return null;
    }

    public String getPlayerName(int index){
        if(index>=0 && index < mPlayers.length){
            return mPlayers[index].getName();
        }
        return null;
    }

    public int getPerPlayerOfCards(){
        return mGameRule.getPerPlayerOfCards();
    }

    public void setState(int state){
        mState.state = state;
    }

    public int getState(){
        return mState.state;
    }

    public Vector<Card> getMyCard(){
        if(mPlayers.length > 0) {
            Player player = mPlayers[0];
            if(player!=null){
                return player.getCards();
            }
        }
        return null;
    }

}
