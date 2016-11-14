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

public class BridgeGame extends Game{

    final boolean DBG = true;
    final boolean VDBG = false;
    private static final String TAG = "BridgeGame";

    //using in call king
    public static final int TOTAL_TRICKS  = 7;
    public static final int TOTAL_CONTRACT_SUIT  = 5;
    ArrayList<String> mContractTrickList = null;
    ArrayList<String> mContractSuitList = null;
    //final contract
    private ContractInfo mContractInfo = new ContractInfo();
    private int mPassCnt = 0;

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
        setPartner(players);
        return players;
    }

    /*
    partner : player 0, 2  , partner : player 1, 3
     */
    private static void setPartner(Player[] players){
        int partnerIndex = 0;
        for(int i=0; i< players.length; ++i){
            if(i < 2){//0,1
                partnerIndex = i+2;
            }else{//2,3
                partnerIndex = i-2;
            }
            players[i].setPartner(players[partnerIndex]);
        }
    }

    private BridgeGame(GameRule rule , Player players[]){
        super(rule, players);
        Log.i(TAG, "Created BridgeGame");
        initContractList();
    }

    private void initContractList(){
        mContractTrickList = new ArrayList<String>();
        mContractSuitList = new ArrayList<String>();
        //suit(club, diamond, heart, spade, no king)
        for(int i=1; i <= TOTAL_CONTRACT_SUIT; ++i){
            String suit = CardSuit.IndexToString(i);
            mContractSuitList.add(suit!=null?suit:GameConstants.NO_KING);
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

        Log.d(GameConstants.TAG, "Deal: cardSetsSize = " + cardSets.size() + ", numOfPlayer = " + numOfPlayer);
        for(int i=0;i<cardSets.size();++i){
            CardSet cardSet = cardSets.get(i);//tmp

            for(int j=0;j<cardSet.TOTAL_CARD;++j){
                index = j % numOfPlayer;
                Card card = cardSet.DrawCard(j);
                if(index==0)Log.d(TAG,"player "+index+": "+card.getPoint()+" , "+card.getSuit());
                mPlayers[index].setCard(card);
            }
        }
        mCardSet = cardSets.get(0);//just one card set only in bridge game
    }

    protected void ArrangeCard(){
        Log.i(TAG, "Arranging Cards ..");
        //TreeMap<Integer,Card> treeMap = new TreeMap<Integer,Card>();
        ArrayList<Card> cardList = new ArrayList<>();
        boolean matchSuit = false;
        for(int i=0;i < mPlayers.length; ++i){
        //for(int i=0;i < 1; ++i){
            ArrayList<Card> cards = mPlayers[i].getCards();
            //Log.d(TAG,"vector size = "+cards.size());
            cardList.clear();//init
            //printCard(cards);
            for(int j=0;j<cards.size();++j){

                matchSuit = false;
                Card card = cards.get(j);

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
                Log.e(TAG,"ArrangeCard : Vector size not equal to List size");
            }
            ArrangeByColor(cardList);
            //copyListToVector(cards, cardList);
            cards = cardList;
            //printCard(cards);
        }
    }

    protected void WeightCalculated(){
        Log.d(GameConstants.TAG, "WeightCalculated");
        for(Player player:mPlayers){
            ((BridgeGameRule)mGameRule).weightCalculated(player);
        }
    }
/*
    private void copyListToVector(Vector<Card> cardsVector, ArrayList<Card> cardsList){
        cardsVector.clear();
        for(int i=0;i<cardsList.size();++i){
            Card card = cardsList.get(i);
            cardsVector.add(card);
        }
    }
*/
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

    public void bidContract(int trick, int suit){
        if(DBG)Log.d(GameConstants.TAG,"bidContract: main player contract = ["+trick+","+suit+"]");
        //update main player contract
        if(trick!=0 && suit!=0){
            ((BridgeGamePlayer)getMainPlayer()).updateContract(trick, suit, false);
            mContractInfo.CallerID = getMainPlayer().getID();
            resetPassCnt();
        }
        else {
            ((BridgeGamePlayer)getMainPlayer()).updateContract(trick, suit, true);
            increasePassCnt();
        }
        //update final contract
        if(!mContractInfo.isContractChange || (trick!=0 && suit!=0)){
            mContractInfo.updateContract(trick, suit, false);
        }
        //update other player contract
        for(Player player:mPlayers){
            if(mContractInfo.isPass && ((BridgeGamePlayer) player).getContractInfo().isContractChange){
                Log.d(GameConstants.TAG,"bidContract: Pass");
                return;
            }
            Bundle bundle = ((BridgeGameRule) mGameRule).bidContract(player,mContractInfo.Trick,mContractInfo.Suit);

            if(player.getID() > 0 && bundle != null){//just need to update player 2~4
                if(!bundle.getBoolean(GameConstants.CONTRACT_PASS)){//new contract
                    mContractInfo.updateContract(bundle.getInt(GameConstants.CONTRACT_TRICK),
                                                 bundle.getInt(GameConstants.CONTRACT_SUIT),false);
                    mContractInfo.CallerID = player.getID();
                    resetPassCnt();
                }else{
                    increasePassCnt();
                }
                //update self contract
                ((BridgeGamePlayer) player).updateContract(bundle.getInt(GameConstants.CONTRACT_TRICK),
                        bundle.getInt(GameConstants.CONTRACT_SUIT),
                        bundle.getBoolean(GameConstants.CONTRACT_PASS));

                if(VDBG)Log.d(GameConstants.TAG,"bidContract: play["+player.getID()+"] contract = ["+bundle.getInt(GameConstants.CONTRACT_TRICK)+","+bundle.getInt(GameConstants.CONTRACT_SUIT)+"]"+
                ", Pass = "+bundle.getBoolean(GameConstants.CONTRACT_PASS));
            }
            mContractInfo.isPass = (mPassCnt >= (mGameRule.getNumberOfPlayers()-1));
            Log.d(GameConstants.TAG,"bidContract: isPass = "+mContractInfo.isPass+", "+mContractInfo.Trick+":"+mContractInfo.Suit);
        }
        //Log.d(GameConstants.TAG,"mPlayers size = "+mPlayers.length);
    }

    private void resetPassCnt(){
        mPassCnt = 0;
    }

    private void increasePassCnt(){
        ++mPassCnt;
    }

    public ContractInfo getContractInfo(){
        return mContractInfo;
    }

    /*
    playerId : last player's id
    card : playerId's play card
     */
    public Card playCard(int lastPlayerId, Card lastCard){
        int playedCnt = 0;
        //check in which case first ...
        for(Card _card: mPlayList){
            if(_card == null)continue;
            ++playedCnt;
        }
        Card playCard = null;
        //get the current player's cards
        int currentPlayerId = lastPlayerId+1;
        if(lastPlayerId == 3) currentPlayerId = 0;

        if(playedCnt == 0){//playerId is the first play in this round
            playCard = firstPlay(currentPlayerId);
        }else if(playedCnt == 1){//..
            playCard = secondPlay(currentPlayerId, lastCard);
        }else if(playedCnt == 2){//..

        }else if(playedCnt == 3){//playerId is the last play in this round

        }else {

        }
        return playCard;
    }

    private Card firstPlay(int currentPlayerId){
        ArrayList<Card> cards = mPlayers[currentPlayerId].getCards();

        CardSuit minSuit = ((BridgeGameRule) mGameRule).getMinSuitWeightByID(currentPlayerId);
        CardPoint _point = CardPoint.INVALID;//init
        Card playCard = null;
        for(Card card: cards){
            if(card.getSuit() == minSuit){
                if(card.getPoint().compare(CardPoint.ACE) == 0){//win one trick first
                    playCard = card;
                    break;
                }
                //get the smallest point
                if(_point.compare(CardPoint.INVALID)==0 || _point.compare(card.getPoint()) == 1){
                    _point = card.getPoint();
                    playCard = card;
                }
            }
        }
        return playCard;
    }

    private Card secondPlay(int currentPlayerId, Card lastCard){
        Card playCard = null;
        if(mContractInfo.Suit == lastCard.getSuit().getValue()){//king..

        }else{
            Card maxCard = mGameRule.getMaxCardBySuit(lastCard.getSuit(), mPlayers[currentPlayerId]);
            Card minCard = mGameRule.getMinCardBySuit(lastCard.getSuit(), mPlayers[currentPlayerId]);
            if(maxCard!=null && maxCard.getPoint().compare(lastCard.getPoint()) == -1){
                playCard = minCard;
            }else{
                playCard = mGameRule.getBiggerCard(lastCard, mPlayers[currentPlayerId]);
            }
        }
        if(playCard == null){//no any same suit card
            CardSuit minSuit = ((BridgeGameRule) mGameRule).getMinSuitWeightByID(currentPlayerId);
            playCard = mGameRule.getMinCardBySuit(minSuit,mPlayers[currentPlayerId]);
        }
        return playCard;
    }

    /*
    input : contract caller id
    playId : contract caller id's next player
    ex. id=0, playerId=1 ..
     */
    public void setPlayID(int id){
        int playId = id+1;
        if(playId >= getPlayers().length){
            playId = 0;
        }
        mPlayID = playId;
    }

    public Player getMainPlayer(){
        //id:0 is the main player
        if(mPlayers.length > 0){
            return mPlayers[0];
        }
        return new Player(Integer.toString(0),0);
    }

    //update contract spinner list
    public boolean isValidContract(int trick, int suit){
        if(suit < mContractInfo.Suit && trick < mContractInfo.Trick){
            return false;
        }
        return true;
    }

    //using in call king
    public ArrayList<String> getContractTrickList(){
        return mContractTrickList;
    }

    public ArrayList<String> getContractSuitList(){
        return mContractSuitList;
    }

    //test
    public static void printCard(ArrayList<Card> cards){
        if(cards.size()==0)return;
        Log.d(TAG, "printCard : ");
        for(int i=0;i<cards.size();++i){
            Card card = cards.get(i);
            Log.d(GameConstants.TAG,"point/suit = "+card.getPoint()+"/"+card.getSuit());
        }
    }
}
