package com.kf.coffeecard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import android.os.Bundle;
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
        ArrayList<Card> cards =  player.getCards();
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
            if(orgSuit > suit && isBidSuit(trick,weight)){
                bidSuit = orgSuit;
                isChange = true;
                if(bidTrick==0)++bidTrick;
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
        bundle.putBoolean(GameConstants.CONTRACT_PASS,!isChange);
        bundle.putInt(GameConstants.CONTRACT_TRICK,bidTrick);
        bundle.putInt(GameConstants.CONTRACT_SUIT, bidSuit);
        return bundle;
    }

    private boolean isBidSuit(int trick, int weight){
        if(weight >= 8 && weight<15 && trick<=2)return true;
        else if(weight >= 15 && trick<=3)return true;
        else if(weight >= 20)return true;
        return false;
    }
    private boolean isBidTrick(int trick, int weight){
        /*if(weight >= 10 && weight < 13 ){
            return true;
        }*/
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

    public Card.CardSuit getMinSuitWeightByID(int playerId){
        ArrayList<Integer> weightList = mWeightMap.get(playerId);
        int minSuit = 0; int weight = weightList.get(0);
        for(int i=1; i<weightList.size();++i){
            int _weight = weightList.get(i);
            if(_weight == 0){
                continue;
            }
            else if(_weight < weight){
                minSuit = i;
                weight = weightList.get(i);
            }
        }
        return Card.CardSuit.valueOf(minSuit);
    }

    public Card getMaxCardBySuit(Card.CardSuit suit, Player player){
        ArrayList<Card> cards = player.getCards();
        int maxPoint = 0;
        Card maxCard = null;
        for(Card card: cards){
            if(card.getSuit().compare(suit) == 0){
                if(card.getPoint().compare(Card.CardPoint.ACE) == 0){//ace is the biggest
                    maxCard = card;
                    break;
                }
                else if(maxCard==null || card.getPoint().getValue() > maxPoint ){
                    maxCard = card;
                    maxPoint = card.getPoint().getValue();
                }
            }
        }
        return maxCard;
    }

    public Card getMinCardBySuit(Card.CardSuit suit, Player player){
        ArrayList<Card> cards = player.getCards();
        int minPoint = 0;
        Card minCard = null;
        for(Card card: cards){
            if(card.getSuit().compare(suit) == 0){
                if(minCard==null ||
                 (card.getPoint().getValue() < minPoint && card.getPoint().compare(Card.CardPoint.ACE)!=0)){//not ace
                    minCard = card;
                    minPoint = card.getPoint().getValue();
                }
            }
        }
        return minCard;
    }

    /*
    get the biggest card
    ex. cards = 3,7,1,5  , _card = 4 , biggerCard = 5
     */
    public Card getBiggerCard(Card _card, Player player){
        ArrayList<Card> cards = player.getCards();
        int biggerPoint = 0;
        Card biggerCard = null;
        if(_card.getPoint().compare(Card.CardPoint.ACE) == 0)return null;//no bigger card

        for(Card card: cards){
            if(card.getSuit().compare(_card.getSuit()) == 0){
                if(biggerCard == null){
                    biggerCard = card;
                    biggerPoint = card.getPoint().getValue();
                    continue;
                }
                if(card.getPoint().compare(_card.getPoint()) > 0){
                    //just get the bigger card, not biggest card
                    if(card.getPoint().getValue() < biggerPoint ){
                        biggerCard = card;
                        biggerPoint = card.getPoint().getValue();
                    }
                }
            }
        }
        return biggerCard;
    }

    private void printWeight(ArrayList<Integer> weightList){
        for(int i=0; i<weightList.size(); ++i){
            Log.d(GameConstants.TAG,"weightCalculated: "+i+" : "+weightList.get(i));
        }
    }
}
