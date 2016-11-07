package com.kf.coffeecard;

/**
 * Created by kimmy on 16/10/26.
 */
public class GameConstants {
    public final static String TAG = "kimmy";
    public final static String PLAYER_NAME = "name";
    public final static String NUMBER_OF_PLAYER = "players";
    public final static String GAME_TYPE = "gametype";
    public final static String CONTRACT_TRICK = "contract_trick";
    public final static String CONTRACT_SUIT = "contract_suit";

    //service
    public final static int EVENT_SERVICE_REGISTER = 1;
    public final static int EVENT_SERVICE_REGISTER_DONE = 2;
    public final static int EVENT_SERVICE_INIT_GAME = 3;
    public final static int EVENT_SERVICE_BID_CONTRACT = 4;

    //client
    public final static int EVENT_CLIENT_DISPLAY_GAME = 100;
    public final static int EVENT_CLIENT_DISPLAY_GAME_DONE = 101;

}
