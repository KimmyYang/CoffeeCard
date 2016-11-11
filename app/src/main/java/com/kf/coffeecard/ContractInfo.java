package com.kf.coffeecard;

/**
 * Created by kimmy on 16/11/9.
 */
public class ContractInfo {
    public int Trick = 0;
    public int Suit = 0;
    public boolean isContractChange = false;
    public boolean isPass = false;
    public int CallerID = 0;//who decide the final contract

    public void updateContract(int trick, int suit, boolean isPass){
        this.isPass = isPass;
        this.Trick = trick;
        this.Suit = suit;
        this.isContractChange = true;
    }

    public static String getContractFormat(ContractInfo contract){
        if(contract.isPass)return GameConstants.CONTRACT_PASS;
        String suit = Card.CardSuit.IndexToString(contract.Suit);
        return Integer.toString(contract.Trick)+":"+ (suit!=null?suit:GameConstants.NO_KING);
    }

    public static String getMainContractFormat(ContractInfo contract){
        String suit = Card.CardSuit.IndexToString(contract.Suit);
        return Integer.toString(contract.Trick)+":"+ (suit!=null?suit:GameConstants.NO_KING);
    }
}
