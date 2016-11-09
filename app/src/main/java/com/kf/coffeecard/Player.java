package com.kf.coffeecard;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by kimmy on 15/4/6.
 */
public class Player {
    private String mName;
    private int mID;
    private Vector<Card> mCards = new Vector<Card>();
    private TextView mPlayerInfo = null;

    public Player(String name, int id){
        mName = name;
        mID = id;
    }
    public String getName(){return mName;}
    public Integer getID(){return mID;}
    public void setCard(Card card){mCards.add(card);}
    public Vector getCards(){return mCards;}
    public int getNumberOfCards(){return mCards.size();}
    public void initPlayerInfo(TextView view){
        mPlayerInfo = view;
        //mPlayerInfo.setText(mName);
        //mPlayerInfo.setText("1:No King / 20000");
    }
}
