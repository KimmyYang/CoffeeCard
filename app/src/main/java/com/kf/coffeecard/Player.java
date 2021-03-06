package com.kf.coffeecard;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by kimmy on 15/4/6.
 */
public class Player {
    protected String mName;
    protected int mID;
    protected ArrayList<Card> mCards = new ArrayList<Card>();
    protected String mPlayerInfo;
    protected int mScore = GameRule.getGameScore();
    public final int NAME_INFO_FORMAT = 0;
    public final int SCORE_INFO_FORMAT = 1;
    private Player mPartner = null;
    protected ArrayList<Card> mPlayedList = null;//record the played card

    public Player(String name, int id){
        mName = name;
        mID = id;
        mPlayedList = new ArrayList<Card>();
        updatePlayerInfo(NAME_INFO_FORMAT);
    }
    public String getName(){return mName;}
    public Integer getID(){return mID;}
    public void setCard(Card card){mCards.add(card);}
    public ArrayList getCards(){return mCards;}
    public int getNumberOfCards(){return mCards.size();}
    public String getPlayerInfo(){return mPlayerInfo;}
    protected void updatePlayerInfo(int format){
        switch (format){
            case NAME_INFO_FORMAT:
                mPlayerInfo = "Player : "+mName;
                break;
            default:
            case SCORE_INFO_FORMAT:
                mPlayerInfo = mName+"/"+Integer.toString(mScore);
                break;
        }
    }
    protected void updatePlayerInfo(String info){
        mPlayerInfo = info;
    }

    public void setPartner(Player player){
        mPartner = player;
    }
}
