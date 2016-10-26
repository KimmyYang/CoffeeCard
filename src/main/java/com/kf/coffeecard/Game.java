package com.kf.coffeecard;

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

    protected Player mPlayers[];
    protected GameRule mGameRule;
    public Game(GameRule rule , Player player[]){
        mGameRule = rule;
        mPlayers = player;
    }
    public static boolean isVaild(int index) {
        if (index > GameType.values().length) return false;
        return true;
    }

    public GameRule getGameRule(){return mGameRule;}
    public Player[] getPlayers(){return mPlayers;}
    public abstract void startGame();

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
