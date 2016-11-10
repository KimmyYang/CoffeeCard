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
        updatePlayerInfo(ContractInfo.getContractFormat(mContractInfo));
    }

    public ContractInfo getContractInfo(){
        return mContractInfo;
    }
}
