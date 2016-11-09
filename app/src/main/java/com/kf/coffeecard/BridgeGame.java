/**
 * Created by kimmy on 15/4/6.
 */
package com.kf.coffeecard;
import android.util.Log;
import com.kf.coffeecard.Card.CardSuit;
import com.kf.coffeecard.Card.CardPoint;
import java.util.ArrayList;
import java.util.Vector;
import android.os.Bundle;
import android.widget.TextView;

public class BridgeGame extends Game{

    final boolean DBG = true;
    final boolean VDBG = false;
    private static final String TAG = "BridgeGame";

    //using in call king
    public static final int TOTAL_TRICKS  = 7;
    public static final int TOTAL_CONTRACT_SUIT  = 5;
    ArrayList<String> mContractTrickList = null;
    ArrayList<String> mContractSuitList = null;
    private int mContractSuit;
    private int mContractTrick;

    //create game , rule and player
    public static Game createGame(Bundle bundle){
        Log.d(TAG, "createGame");
        if(mInstance == null){
            String name = bundle.getString(GameConstants.PLAYER_NAME);
            int numPlayers = bundle.getInt(GameConstants.NUMBER_OF_PLAYER);
            GameType type = GameType.values()[bundle.getInt(GameConstants.GAME_TYPE)];
            mInstance = new BridgeGame(new BridgeGameRule(type, numPlayers), createPlayers(numPlayers, name));
        }
        return mInstance;
    }

    private static Player[] createPlayers(int numPlayers, String name){
        Player[] players = new BridgeGamePlayer[numPlayers];
        //1st player
        players[0] = new BridgeGamePlayer(name, 0);
        //other players
        for(int i=1; i<numPlayers ; ++i){//1, 2, 3, ..numPlayers
            players[i] = new BridgeGamePlayer(Integer.toString(i), i);
        }
        return players;
    }

    private BridgeGame(GameRule rule , Player players[]){
        super(rule , players);
        Log.i(TAG, "Created BridgeGame");
        initContractList();
    }

    private void initContractList(){
        mContractTrickList = new ArrayList<String>();
        mContractSuitList = new ArrayList<String>();
        //suit(club, diamond, heart, spade, no king)
        for(int i=0; i < TOTAL_CONTRACT_SUIT; ++i){
            String suit = CardSuit.IndexToString(i);
            mContractSuitList.add(suit!=null?suit:"No King");
        }
        //trick(1~7)
        for(int i=1; i<= TOTAL_TRICKS; ++i){
            mContractTrickList.add(Integer.toString(i));
        }
    }

    public void initGame(){
        /*
        start bridge game ...
         */
        Log.i(TAG,"Start Bridge Game ..");
        Deal();
        ArrangeCard();
        WeightCalculated();
    }

    //init textView for each player
    public void initPlayerInfo(int index,TextView view){
        if(index < mPlayers.length){
            mPlayers[index].initPlayerInfo(view);
        }
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
        Log.d(TAG, "Deal: NumberOfCardSet = " + getGameRule().getNumberOfCardSet());
        for(int i=0; i<getGameRule().getNumberOfCardSet();++i){
            CardSet cardSet = new CardSet();
            cardSet.Shuffle();
            cardSets.add(cardSet);
        }
        Log.d(TAG, "Deal: cardSetsSize = " + cardSets.size());
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
            //printCard(cards);
        }
    }

    protected void WeightCalculated(){
        Log.d(GameConstants.TAG, "WeightCalculated");
        for(Player player:mPlayers){
            ((BridgeGameRule)mGameRule).weightCalculated(player);
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

    public void bidContract(){
        if(DBG)Log.d(GameConstants.TAG,"bidContract: original contract = ["+mContractTrick+","+mContractSuit+"]");
        ((BridgeGamePlayer)getMainPlayer()).updateContract(mContractTrick, mContractSuit);
        for(Player player:mPlayers){
            Bundle bundle = ((BridgeGameRule) mGameRule).bidContract(player, mContractTrick, mContractSuit);
            if(player.getID() >0 && bundle != null){//just need to update player 2~4
                mContractTrick = bundle.getInt(GameConstants.CONTRACT_TRICK,mContractTrick);
                mContractSuit = bundle.getInt(GameConstants.CONTRACT_SUIT,mContractSuit);
                ((BridgeGamePlayer) player).updateContract(mContractTrick, mContractSuit);//update self contract
                Log.d(GameConstants.TAG,"bidContract: play["+player.getID()+"] contract = ["+mContractTrick+","+mContractSuit+"]");
            }
        }
    }

    protected Player getMainPlayer(){
        //id:0 is the main player
        if(mPlayers.length > 0){
            return mPlayers[0];
        }
        return new Player(Integer.toString(0),0);
    }

    //using in call king
    public ArrayList<String> getContractTrickList(){
        return mContractTrickList;
    }

    public ArrayList<String> getContractSuitList(){
        return mContractSuitList;
    }

    public void setContractSuit(int suit){
        mContractSuit = suit;
    }

    public void setmContractTrick(int trick){
        mContractTrick = trick;
    }

    //test
    public static void printCard(Vector<Card> cards){
        if(cards.size()==0)return;
        Log.d(TAG, "printCard : ");
        for(int i=0;i<cards.size();++i){
            Card card = cards.elementAt(i);
            Log.d(GameConstants.TAG,"point/suit = "+card.getPoint()+"/"+card.getSuit());
        }
    }
}
