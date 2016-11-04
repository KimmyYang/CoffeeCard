/**
 * Created by kimmy on 15/4/6.
 */
package com.kf.coffeecard;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;


public class Card {
    public enum CardSuit{
        CLUB(1),
        DIAMOND(2),
        HEART(3),
        SPADE(4),
        JOKER(5);
        private static Map<Integer,CardSuit> map = new HashMap<Integer,CardSuit>();
        static{
            for(CardSuit suit: CardSuit.values()) {
                map.put(suit.value, suit);
            }
        }
        private int value;
        private CardSuit(int value){
            this.value = value;
        }
        public static CardSuit valueOf(int index){
            return map.get(index);
        }
    }
    public enum CardPoint{
        ACE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(11),
        QUEEN(12),
        KING(13);

        private static Map<Integer,CardPoint> map = new HashMap<Integer,CardPoint>();
        static{
            for(CardPoint point: CardPoint.values()) {
                map.put(point.value, point);
            }
        }
        private int value;
        private CardPoint(int value){
            this.value = value;
        }
        public static CardPoint valueOf(int index){
            return map.get(index);
        }
    }
    private final String TAG = "Card";
    private CardSuit mSuit;
    private CardPoint mPoint;
    private int mId;
    /*
    mId :
    ACE/CLUB : 1
    ACE/DIAMOND : 2
    ...
    KING/HEART : 51
    KING/SPADE : 52
     */
    public Card(int suit , int point , int id){
        mSuit = CardSuit.valueOf(suit);
        mPoint = CardPoint.valueOf(point);
        mId = id;
    }
    public CardSuit getSuit(){return mSuit;}
    public CardPoint getPoint(){return mPoint;}
    public int getId(){return mId;}
    public void sort(){}
}
