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
    private int mPerPlayerOfCards;
    private int mTotalCards;
    final int BASE_PER_PLAYER_CARDS = 13;

    public GameRule(String name, GameType type, int numPlayerMin, int numPlayerMax, int numCardSet){
        mName = name;
        mType = type;
        mNumberOfPlayerMin = numPlayerMin;
        mNumberOfPlayerMax = numPlayerMax;
        mNumberOfCardSet = numCardSet;
        createRule();
    }

    public GameRule(GameType type, int numPlayers){
        mType = type;
        mNumberOfPlayerMax = mNumberOfPlayerMin = numPlayers;
        createRule();
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

    private void createRule(){
        switch (mType){
            case SEVENS:
            case BRIDGE:
                mNumberOfCardSet = 1;
                mPerPlayerOfCards = BASE_PER_PLAYER_CARDS;
                mTotalCards = BASE_PER_PLAYER_CARDS*mNumberOfPlayerMax;
                break;
            case LIAR:
                mPerPlayerOfCards = BASE_PER_PLAYER_CARDS*mNumberOfCardSet;
            default:
                mPerPlayerOfCards = BASE_PER_PLAYER_CARDS;
                break;
        }
    }
    public int getPerPlayerOfCards(){
        return mPerPlayerOfCards;
    }
    public int getTotalCards(){
        return mTotalCards;
    }
}
