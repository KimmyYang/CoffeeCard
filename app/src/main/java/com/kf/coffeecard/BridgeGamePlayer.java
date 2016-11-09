package com.kf.coffeecard;

/**
 * Created by KimmyYang on 2016/11/7.
 */
public class BridgeGamePlayer extends Player {
    public BridgeGamePlayer(String name, int id) {
        super(name, id);
    }

    //self contract
    private int mContractSuit = 0;
    private int mContractTrick = 0;

    //update self contract
    public void updateContract(int trick , int suit){
        mContractSuit = suit;
        mContractTrick = trick;
        updatePlayerInfo(getContractFormat());
    }
    private String getContractFormat(){
        if(mContractTrick==0 && mContractSuit==0)return GameConstants.CONTRACT_PASS;
        String suit = Card.CardSuit.IndexToString(mContractSuit);
        return Integer.toString(mContractTrick)+":"+ (suit!=null?suit:GameConstants.NO_KING);
    }
}
