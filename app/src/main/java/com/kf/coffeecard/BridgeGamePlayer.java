package com.kf.coffeecard;

/**
 * Created by KimmyYang on 2016/11/7.
 */
public class BridgeGamePlayer extends Player {
    public BridgeGamePlayer(String name, int id) {
        super(name, id);
    }

    //self contract
    private int mContractSuit;
    private int mContractTrick;

    //update self contract
    public void updateContract(int trick , int suit){
        mContractSuit = suit;
        mContractTrick = trick;
    }
}
