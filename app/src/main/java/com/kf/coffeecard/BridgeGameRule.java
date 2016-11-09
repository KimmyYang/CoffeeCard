package com.kf.coffeecard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import android.os.Bundle;
import android.os.DropBoxManager;
import android.util.Log;

/**
 * Created by KimmyYang on 2016/11/7.
 */
public class BridgeGameRule extends GameRule {

    final boolean DBG = true;
    final boolean VDBG = false;
    private final int HIGH_WEIGHT = 3;
    private final int MEDIUM_WEIGHT = 2;
    private final int BASIC_WEIGHT = 1;
    public static final int BRIDGE_GAME_SCORE = 20000;

    HashMap<Integer, ArrayList<Integer>> mWeightMap = new HashMap<Integer, ArrayList<Integer>>();

    public BridgeGameRule(Game.GameType type, int numPlayers) {
        super(type, numPlayers);
    }

    public void weightCalculated(Player player){
        ArrayList<Integer> weightList = new ArrayList<Integer>(Collections.nCopies(Card.CARD_SUIT_COUNT, 0));//init to 0
        Vector<Card> cards =  player.getCards();
        for(Card card:cards) {
            int index = card.getSuit().getValue()-1;
            weightList.set(index, weightList.get(index)+getCardWeight(card.getPoint()));
        }
        if(player.getID() == 0 && VDBG){
            for(int i=0; i<weightList.size(); ++i){
                Log.d(GameConstants.TAG,"weightCalculated: "+i+" : "+weightList.get(i));
            }
        }
        mWeightMap.put(player.getID(), weightList);
    }

    public Bundle bidContract(Player player, int trick, int suit){
        boolean isChange = false;
        int bidTrick=trick, bidSuit=suit;
        ArrayList<Integer> weightList = mWeightMap.get(player.getID());
        if(DBG)printWeight(weightList);
        Log.d(GameConstants.TAG,"bidContract: before id["+player.getID()+"] = trick = "+trick+", suit = "+suit);
        for(int i=0; i< weightList.size(); ++i){
            int weight =  weightList.get(i);
            int orgSuit = i+1;
            if(orgSuit > suit && isBidSuit(weight)){
                bidSuit = orgSuit;
                isChange = true;
            }
            else if(orgSuit < suit && isBidTrick(trick, weight)){
                bidTrick++;
                bidSuit = orgSuit;
                isChange = true;
            }
        }
        if(DBG && isChange){
            BridgeGame.printCard(player.getCards());
        }
        Log.d(GameConstants.TAG,"bidContract: after id["+player.getID()+"] = "+bidTrick+", bidSuit = "+bidSuit);
        Bundle bundle = new Bundle();
        bundle.putInt(GameConstants.CONTRACT_TRICK,bidTrick);
        bundle.putInt(GameConstants.CONTRACT_SUIT, bidSuit);
        return bundle;
    }

    private boolean isBidSuit(int weight){
        if(weight >= 8 )return true;
        return false;
    }
    private boolean isBidTrick(int trick, int weight){
        if(weight >= 10 && weight < 13 ){
            return true;
        }
        if(weight < 13){
            if(weight>=10 && trick==1 )return true;//bid 2
            return false;//bid 1
        }else if(weight >= 13 && weight < 18){
            if(trick == 1)return true;//bid 2
            else if(weight>15 && trick==2)return true;//bid 3
            return false;
        }else if(weight >= 18){
            if(trick <= 2)return true;//bid 3
            if(weight>20 && trick<=3)return true;//bid 1,2,3,4
            return false;
        }
        return false;
    }

    private int getCardWeight(Card.CardPoint value){
        if(value == Card.CardPoint.ACE || value == Card.CardPoint.KING){
            return HIGH_WEIGHT;
        }
        else if(value == Card.CardPoint.QUEEN || value == Card.CardPoint.JACK || value ==  Card.CardPoint.TEN){
            return MEDIUM_WEIGHT;
        }else{
            return BASIC_WEIGHT;
        }
    }

    private void printWeight(ArrayList<Integer> weightList){
        for(int i=0; i<weightList.size(); ++i){
            Log.d(GameConstants.TAG,"weightCalculated: "+i+" : "+weightList.get(i));
        }
    }
}
