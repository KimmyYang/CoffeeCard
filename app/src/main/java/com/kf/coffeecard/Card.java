/**
 * Created by kimmy on 15/4/6.
 */
package com.kf.coffeecard;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;


public class Card {

    public enum CardSuit{
        INVALID(-1),
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
        public int getValue(){
            return value;
        }
        public int compare(CardSuit suit){
            if(value > suit.getValue()){
                return 1;
            }else if(value < suit.getValue()){
                return -1;
            }else {
                return 0;
            }
        }
        public static CardSuit valueOf(int index){
            return map.get(index);
        }
        public static String IndexToString(int index){
            CardSuit suit = valueOf(index);
            if(suit == CLUB){
                return "Club";
            }else if(suit == DIAMOND){
                return "Diamond";
            }else if(suit == HEART){
                return "Heart";
            }else if(suit == SPADE) {
                return "Spade";
            }else {
                return null;
            }
        }
    }
    public enum CardPoint{
        INVALID(-1),
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
        public int getValue(){
            return value;
        }
        public int compare(CardPoint point){
            if(value > point.getValue()){
                return 1;
            }else if(value < point.getValue()){
                return -1;
            }else {
                return 0;
            }
        }
        public static CardPoint valueOf(int index){
            return map.get(index);
        }
    }
    private final String TAG = "Card";
    public static final int CARD_SUIT_COUNT = 4;
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

    public Card(int suit , int point){
        mId = 0;
        mSuit = CardSuit.valueOf(suit);
        mPoint = CardPoint.valueOf(point);
    }

    public Card(){
        resetCard();
    }

    public void resetCard(){
        mId = 0;
        mSuit = CardSuit.INVALID;
        mPoint = CardPoint.INVALID;
    }
    public boolean isValid(){
        if(mId ==0 || mSuit==CardSuit.INVALID ||
                mPoint==CardPoint.INVALID)return false;
        return true;
    }
    public CardSuit getSuit(){return mSuit;}
    public CardPoint getPoint(){return mPoint;}
    public int getId(){return mId;}
    public boolean isEquals(CardSuit suit, CardPoint point){
        if(suit == mSuit && point == mPoint)return true;
        return false;
    }
    public void sort(){}
}
