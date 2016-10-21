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

    public GameFactory(){
        Log.i(TAG,"Created GameFactory");
    }
    public static Game createGame(GameRule rule , Player players[]){

        if(mInstance != null)throw new RuntimeException("GameFactory.createGame should be call once.");

        mInstance = new GameFactory();

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
        return mGame;
    }
    public static GameFactory getInstance(){
        if(mInstance == null)throw new RuntimeException("GameFactory instance is null");
        return mInstance;
    }
}
