package com.kf.coffeecard;

/**
 * Created by kimmy on 15/4/6.
 *
 * GameRule value is depend on setting UI
 */
import com.kf.coffeecard.Game.GameType;

public class GameRule {


    private String mName;
    private int mNumberOfPlayerMin;
    private int mNumberOfPlayerMax;
    private int mNumberOfCardSet;
    private GameType mType;

    public GameRule(String name, GameType type, int numPlayerMin, int numPlayerMax, int numCardSet){
        mName = name;
        mType = type;
        mNumberOfPlayerMin = numPlayerMin;
        mNumberOfPlayerMax = numPlayerMax;
        mNumberOfCardSet = numCardSet;
    }
    public boolean IsGameRuleValid(){
        if(mNumberOfPlayerMin<=0 || mNumberOfPlayerMax<=0 || mNumberOfCardSet<=0){
            return false;
        }
        return true;
    }
    public GameType getGameType(){return mType;}
    public int getNumberOfPlayers(){return mNumberOfPlayerMax;}
    public int getNumberOfCardSet(){return mNumberOfCardSet;}


}
