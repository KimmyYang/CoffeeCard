/**
 * Created by kimmy on 15/4/6.
 */
package com.kf.coffeecard;
import android.util.Log;

enum CardSuit{
    SPADE,
    HEART,
    DIAMOND,
    CLUB,
    JOKER
}
enum CardPoint{
    ACE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING
}
public class Card {
    private final String TAG = "Card";
    private CardSuit mSuit;
    private CardPoint mPoint;

    public Card(int suit , int point){
        mSuit = CardSuit.values()[suit];
        mPoint = CardPoint.values()[point];
        //Log.d(TAG,mSuit+" "+mPoint);
    }
    public CardSuit getSuit(){return mSuit;}
    public CardPoint getPoint(){return mPoint;}
}
