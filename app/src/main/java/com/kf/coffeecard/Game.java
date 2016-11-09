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

    private static final String TAG = "Game";
    protected static Game mInstance = null;
    protected Player mPlayers[];
    protected GameRule mGameRule;

    public abstract void initGame();
    protected abstract void Deal();
    protected abstract void ArrangeCard();
    protected abstract void WeightCalculated();
    protected abstract Player getMainPlayer();

    public Game(GameRule rule , Player player[]) {
        mGameRule = rule;
        mPlayers = player;
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
