package com.blockchain.project.blockchain;

import java.util.ArrayList;

public class Block {
    private int index;
    private long time;
    private ArrayList<Transaction> transactions;
    private int proof;
    private int hash;
    private int previous_hash;

    public Block(int index, long time, ArrayList<Transaction> transactions, int proof, int hash, int previous_hash) {
        this.index = index;
        this.time = time;
        this.transactions = transactions;
        this.proof = proof;
        this.hash = hash;
        this.previous_hash = previous_hash;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public int getIndex() {
        return index;
    }

    public long getTime() {
        return time;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public int getProof() {
        return this.proof;
    }

    public int getPrevious_hash() {
        return previous_hash;
    }
}
