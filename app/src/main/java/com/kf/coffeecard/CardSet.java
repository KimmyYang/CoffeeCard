/**
 * Created by kimmy on 15/4/6.
 */
package com.kf.coffeecard;

import java.util.ArrayList;
import java.util.Vector;
import android.util.Log;
import java.util.Collections;
import com.kf.coffeecard.Card.CardSuit;
import com.kf.coffeecard.Card.CardPoint;


public class CardSet {

    private final String TAG = "CardSet";
    public static final int TOTAL_CARD_POINT = 13;
    public static final int TOTAL_CARD_SUIT = 4;
    public final static int TOTAL_CARD = TOTAL_CARD_POINT*TOTAL_CARD_SUIT ;
    private ArrayList<Card> mCardSet;

    public  CardSet(){
        mCardSet = new ArrayList<Card>();
        int id = 1;
        for(int i=1; i<=TOTAL_CARD_POINT; ++i){
            for(int j=1; j<=TOTAL_CARD_SUIT; ++j,++id){
                Card card = new Card(j,i,id);
                mCardSet.add(card);
            }
        }
        //printCardSet();
        if(mCardSet.size() != TOTAL_CARD){
            Log.e(TAG,"CardSet number error");
        }
    }
    public Card DrawCard(int index ){return mCardSet.get(index);}
    //public Card DrawCard(int i){return mCardSet.remove(i);}
    public void Shuffle(){
        Collections.shuffle(mCardSet);
    }

    public Card getCard(int suit, int point){
        CardSuit _suit = CardSuit.valueOf(suit);
        CardPoint _point = CardPoint.valueOf(point);
        for(Card card: mCardSet){
            if(card.isEquals(_suit, _point)){
                return card;
            }
        }
        Log.d(GameConstants.TAG,"getCard: new card "+suit+" , "+point);
        return new Card(suit, point);
    }

    public void printCardSet(){
        Log.d(TAG,"printCardSet");
        for(int i=0; i<mCardSet.size();++i){
            Card card = mCardSet.get(i);
            CardPoint point = card.getPoint();
            CardSuit suit = card.getSuit();
            Log.d(TAG,point+" "+suit);
        }
    }
}
