package com.kf.coffeecard;

/**
 * Created by kimmy on 16/11/9.
 */
public class ContractInfo {
    public int Trick = 0;
    public int Suit = 0;
    public boolean isContractChange = false;
    public boolean isPass = false;

    public void updateContract(int trick, int suit, boolean isPass){
        this.isPass = isPass;
        this.Trick = trick;
        this.Suit = suit;
        this.isContractChange = true;
    }
}
