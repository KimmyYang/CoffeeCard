package com.kf.coffeecard;

/**
 * Created by kimmy on 15/4/6.
 */

public class Game {
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
    public void startGame(){};

}
