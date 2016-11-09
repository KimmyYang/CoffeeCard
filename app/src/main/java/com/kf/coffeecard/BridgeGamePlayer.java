package com.kf.coffeecard;

/**
 * Created by KimmyYang on 2016/11/7.
 */
public class BridgeGamePlayer extends Player {
    private ContractInfo mContractInfo = new ContractInfo();

    public BridgeGamePlayer(String name, int id) {
        super(name, id);
    }

    //update self contract
    public void updateContract(int trick , int suit, boolean isPass){
        mContractInfo.updateContract(trick, suit, isPass);
        updatePlayerInfo(getContractFormat());
    }
    private String getContractFormat(){
        if(mContractInfo.isPass)return GameConstants.CONTRACT_PASS;
        String suit = Card.CardSuit.IndexToString(mContractInfo.Suit);
        return Integer.toString(mContractInfo.Trick)+":"+ (suit!=null?suit:GameConstants.NO_KING);
    }
    public ContractInfo getContractInfo(){
        return mContractInfo;
    }
}
