package com.kf.coffeecard;
import android.content.Intent;
import android.util.Log;
import com.kf.coffeecard.activity.BridgeGameActivity;
import java.util.Vector;

/**
 * Created by kimmy on 15/4/6.
 */
public class BridgeGame extends Game{

    private final String TAG = "BridgeGame";
    private CardSet mCardSet;

    public  BridgeGame(GameRule rule , Player players[]){
        super(rule , players);
    }

    public void startGame(){
        /*
        start bridge game ...
         */
        Deal();
    }
    private void Deal(){
        Log.d(TAG,"Deal");
        if(!mGameRule.IsGameRuleValid()){
            Log.e(TAG,"Invalid game rule ...");
            return;
        }
        int index = 0;
        int numOfPlayer = mPlayers.length;

        Vector<CardSet> cardSets = new Vector<CardSet>();
        Log.d(TAG,"NumberOfCardSet = "+getGameRule().getNumberOfCardSet());
        for(int i=0; i<getGameRule().getNumberOfCardSet();++i){
            CardSet cardSet = new CardSet();
            cardSet.Shuffle();

            cardSets.add(cardSet);
        }

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
}
