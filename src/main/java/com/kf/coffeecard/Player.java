package com.kf.coffeecard;

import java.util.Vector;

/**
 * Created by kimmy on 15/4/6.
 */
public class Player {
    private String mName;
    private int mID;
    private Vector<Card> mCards = new Vector<Card>();
    public Player(String name, int id){
        mName = name;
        mID = id;
    }
    public String getName(){return mName;}
    public Integer getID(){return mID;}
    public void setCard(Card card){mCards.add(card);}
    public Vector getCards(){return mCards;}
}
