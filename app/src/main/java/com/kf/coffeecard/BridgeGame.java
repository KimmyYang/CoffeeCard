/**
 * Created by kimmy on 15/4/6.
 */
package com.kf.coffeecard;
import android.util.Log;
import com.kf.coffeecard.Card.CardSuit;
import com.kf.coffeecard.Card.CardPoint;
import java.util.ArrayList;
import java.util.Vector;
import android.util.Pair;
import java.util.TreeMap;
import android.os.Bundle;

public class BridgeGame extends Game{

    private TreeMap<Integer,Pair<CardPoint,CardSuit>> mWieghtMap = null;
    private static final String TAG = "BridgeGame";


    private BridgeGame(GameRule rule , Player players[]){
        super(rule , players);
        Log.i(TAG,"Created BridgeGame");
    }

    public static Game createGame(Bundle bundle){
        Log.d(TAG,"createGame");
        if(mInstance == null){
            String name = bundle.getString(GameConstants.PLAYER_NAME);
            int numPlayers = bundle.getInt(GameConstants.NUMBER_OF_PLAYER);
            GameType type = GameType.values()[bundle.getInt(GameConstants.GAME_TYPE)];
            mInstance = new BridgeGame(new GameRule(type, numPlayers), createPlayers(numPlayers, name));
        }
        return mInstance;
    }

    private static Player[] createPlayers(int numPlayers, String name){
        Player[] players = new Player[numPlayers];
        //1st player
        players[0] = new Player(name, 0);
        //other players
        for(int i=1; i<numPlayers ; ++i){//1, 2, 3, ..numPlayers
            players[i] = new Player(Integer.toString(i), i);
        }
        return players;
    }

    public void startGame(){
        /*
        start bridge game ...
         */
        Log.i(TAG,"Start Bridge Game ..");
        Deal();
        ArrangeCard();
    }

    protected void Deal(){
        Log.i(TAG, "Dealing Cards ..");
        if(!mGameRule.IsGameRuleValid()){
            Log.e(TAG,"Invalid game rule ...");
            return;
        }
        int index = 0;
        int numOfPlayer = mPlayers.length;

        Vector<CardSet> cardSets = new Vector<CardSet>();
        Log.d(TAG,"Deal: NumberOfCardSet = "+getGameRule().getNumberOfCardSet());
        for(int i=0; i<getGameRule().getNumberOfCardSet();++i){
            CardSet cardSet = new CardSet();
            cardSet.Shuffle();
            cardSets.add(cardSet);
        }
        Log.d(TAG,"Deal: cardSetsSize = "+cardSets.size());
        for(int i=0;i<cardSets.size();++i){
            CardSet cardSet = cardSets.get(i);//tmp

            for(int j=0;j<cardSet.TOTAL_CARD;++j){
                index = j % numOfPlayer;
                Card card = cardSet.DrawCard(j);
                if(index==0)Log.d(TAG,"player "+index+": "+card.getPoint()+" , "+card.getSuit());
                mPlayers[index].setCard(card);
            }
        }
    }

    protected void ArrangeCard(){
        Log.i(TAG,"Arranging Cards ..");
        //TreeMap<Integer,Card> treeMap = new TreeMap<Integer,Card>();
        ArrayList<Card> cardList = new ArrayList<>();
        boolean matchSuit = false;
        for(int i=0;i < mPlayers.length; ++i){
        //for(int i=0;i < 1; ++i){
            Vector<Card> cards = mPlayers[i].getCards();
            //Log.d(TAG,"vector size = "+cards.size());
            cardList.clear();//init
            //printCard(cards);
            for(int j=0;j<cards.size();++j){

                matchSuit = false;
                Card card = cards.elementAt(j);

                if(cardList.size() == 0)cardList.add(card);
                else{
                    //Log.d(TAG,"list size = "+cardList.size());
                    for(int index=0;index<cardList.size();++index){
                        Card _card = cardList.get(index);

                        if(_card.getSuit().compareTo(card.getSuit()) < 0){
                            cardList.add(index,card);break;
                        }
                        else if(_card.getSuit().compareTo(card.getSuit()) == 0){
                            if(card.getPoint()==CardPoint.ACE){
                                cardList.add(index,card);break;
                            }
                            else if(_card.getPoint().compareTo(card.getPoint()) < 0 && _card.getPoint()!=CardPoint.ACE){
                                cardList.add(index,card);break;
                            }
                            matchSuit = true;
                        }
                        else if(_card.getSuit().compareTo(card.getSuit()) > 0 && matchSuit && index<cardList.size()-1){
                            cardList.add(index,card);break;
                        }

                        if(index==cardList.size()-1){
                            cardList.add(index+1,card);
                            break;
                        }
                    }
                }
            }
            if(cards.size() != cardList.size()){
                Log.wtf(TAG,"ArrangeCard : Vector size not equal to List size");
            }
            ArrangeByColor(cardList);
            copyListToVector(cards, cardList);
            printCard(cards);
        }
    }

    private void copyListToVector(Vector<Card> cardsVector, ArrayList<Card> cardsList){

        cardsVector.clear();
        for(int i=0;i<cardsList.size();++i){
            Card card = cardsList.get(i);
            cardsVector.add(card);
        }
    }

    private void ArrangeByColor(ArrayList<Card> cardList){

        for(int index=0;index<cardList.size();++index){

            if(cardList.get(index).getSuit() == CardSuit.DIAMOND){
                Card card = cardList.remove(index);
                cardList.add(card);
                if(index != cardList.size()-1){
                    --index;
                }
            }
            else if(cardList.get(index).getSuit() == CardSuit.CLUB){
                break;
            }

        }
    }
    /*
    create the card arrange weight map,
    TreeMap will sort the value by key (small -> big)
    12=KING,11=QUEEN,...1=TWO,0=ACE
    3=SPADE, 2=HEART, 1=DIAMOND , 0=CLUB
     */
    private void initCardWeightMap(){

        int weight = CardSet.TOTAL_CARD - CardSet.TOTAL_CARD_SUIT;//48
        if(mWieghtMap==null)mWieghtMap = new TreeMap<Integer,Pair<CardPoint,CardSuit>>();
        mWieghtMap.clear();

        for(int i=CardSet.TOTAL_CARD_POINT;i>1;--i){
            CardPoint point = CardPoint.values()[i-1];//13
            for(int j=CardSet.TOTAL_CARD_SUIT;j>0;--j,--weight){
                CardSuit suit = CardSuit.values()[j-1];
                mWieghtMap.put(weight,new Pair<CardPoint,CardSuit>(point,suit));
                //mWieghtMap.equals()
            }
        }
    }

    private void printCard(Vector<Card> cards){
        if(cards.size()==0)return;
        Log.d(TAG,"printCard : ");
        for(int i=0;i<cards.size();++i){
            Card card = cards.elementAt(i);
            Log.d(TAG,"point/suit = "+card.getPoint()+"/"+card.getSuit());
        }
    }

    public void test(){

    }
/*
    public Vector<Card> getMyCard(){
        if(mPlayers.length > 0) {
            Player player = mPlayers[0];
            if(player!=null){
                return player.getCards();
            }
        }
        return null;
    }
    */

}
