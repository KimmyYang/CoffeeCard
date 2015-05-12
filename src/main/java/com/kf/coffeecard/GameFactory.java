package com.kf.coffeecard;

/**
 * Created by kimmy on 15/4/8.
 */
import com.kf.coffeecard.Game.GameType;
import android.util.Log;

public class GameFactory {

    private static final String TAG = "GameFactory";
    private static GameFactory mInstance;
    private static Game mGame;
    private GameType mGameType;
    private Player[] mPlayers;
    private GameRule mRule;

    public GameFactory(GameRule rule, Player players[]){
        mRule = rule;
        mGameType = rule.getGameType();
        mPlayers = players;
    }
    public static void createGame(GameRule rule , Player players[]){
        Log.i(TAG,"createGame");
        if(mInstance != null)throw new RuntimeException("GameFactory.createGame should be call once.");

        mInstance = new GameFactory(rule, players);

        switch (rule.getGameType()){
            case BRIDGE:
                mGame = new BridgeGame(rule, players);
                mGame.startGame();
                break;
            case BLACK_JACK:
            case SEVENS:
            case LIAR:
            default:
                break;
        }
        Log.i(TAG,"createGame end");
    }
}
