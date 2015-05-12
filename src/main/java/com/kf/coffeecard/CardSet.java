package com.kf.coffeecard;

import java.util.Vector;
import android.util.Log;
import java.util.Collections;

/**
 * Created by kimmy on 15/4/6.
 */
public class CardSet {

    private final String TAG = "CardSet";
    final int TOTAL_CARD_POINT = 13;
    final int TOTAL_CARD_SUIT = 4;
    final int TOTAL_CARD = TOTAL_CARD_POINT*TOTAL_CARD_SUIT ;
    private Vector<Card> mCardSet;

    public  CardSet(){
        mCardSet = new Vector<Card>();

        for(int i=0; i<TOTAL_CARD_POINT; ++i){
            for(int j=0; j<TOTAL_CARD_SUIT; ++j){
                Card card = new Card(j,i);
                mCardSet.add(card);
            }

        }
        //printCardSet();
        if(mCardSet.size() != TOTAL_CARD){
            Log.wtf(TAG,"CardSet number error");
        }
    }
    public Card DrawCard(int i ){return mCardSet.elementAt(i);}
    //public Card DrawCard(int i){return mCardSet.remove(i);}
    public void Shuffle(){
        Collections.shuffle(mCardSet);
    }
    public void printCardSet(){
        Log.d(TAG,"printCardSet");
        for(int i=0; i<mCardSet.size();++i){
            Card card = mCardSet.elementAt(i);
            CardPoint point = card.getPoint();
            CardSuit suit = card.getSuit();
            Log.d(TAG,point+" "+suit);
        }
    }
}
