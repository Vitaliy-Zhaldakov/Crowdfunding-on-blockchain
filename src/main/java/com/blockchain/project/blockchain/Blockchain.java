package com.blockchain.project.blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class Blockchain {
    private ArrayList<Block> chain;
    private ArrayList<Transaction> current_transaction;

    public Blockchain() {
        this.current_transaction = new ArrayList<>();
        this.chain = new ArrayList<>();
        new_block(100,null,0);
    }

    public Block new_block(int proof, ArrayList<Transaction> transactions, int previous_hash) {
        Block block;
        try {
            block = new Block(chain.size(), System.currentTimeMillis(), transactions, proof, 0, previous_hash);
        } catch (NullPointerException e) {
            block = new Block(0, System.currentTimeMillis(), transactions, proof, 0,previous_hash);
        }
        block.setHash(hash(block));
        this.chain.add(block);
        return block;
    }

    @Override
    public String toString() {
        StringBuilder output;
        output = new StringBuilder();
        for (Block b:this.chain) {
            output.append("=========================\n");
            if (b.getIndex() == 0) {
                output.append("INDEX: GENESIS\n");
            } else {
                output.append("INDEX: ").append(b.getIndex()).append("\n");
            }
            output.append("HASH: ").append(b.getHash()).append("\n");
            output.append("PREVIOUS-HASH: ").append(b.getPrevious_hash()).append("\n");
            output.append("TIMESTAMP: ").append(new Timestamp(b.getTime())).append("\n");
            try {
                for (Transaction t : b.getTransactions()) {
                    output.append("-----\n");
                    output.append("***TRANSACTION***\n");
                    output.append("SENDER: ").append(t.getSender()).append("\n");
                    output.append("RECIPIENT: ").append(t.getRecipient()).append("\n");
                    output.append("AMOUNT: ").append(t.getAmount()).append("\n");
                    output.append("-----\n");
                }
            } catch (NullPointerException e) {
                output.append("-----\n");
                output.append("***NO TRANSACTIONS***\n");
                output.append("-----\n");
            }
            output.append("=========================\n");
        }
        return String.valueOf(output);
    }

    public void new_transaction(String sender, String recipient, int amount) {
        Transaction transaction = new Transaction(sender, recipient, amount);
        current_transaction.add(transaction);
    }
    public void new_transaction(String projName, Integer summa, Date dateEnd, String description) {
        Transaction transaction = new Transaction(projName, summa, dateEnd, description);
        current_transaction.add(transaction);
    }

    public int hash(Block block) {
        return block.hashCode();
    }


    public Block last_block() {
        return chain.get(chain.size() -1);
    }

    public int proof_of_work(int last_proof) {
        int proof = 0;
        while (!valid_proof(last_proof, proof)) {
            proof++;
        }
        return proof;
    }

    public boolean valid_proof(int last_proof, int proof) {
        String str = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String guess = String.valueOf(last_proof + proof);
            byte[] guess_hash = digest.digest(guess.getBytes(StandardCharsets.UTF_8));
            str = Base64.getEncoder().encodeToString(guess_hash);
            str = str.substring(0,1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return str.equals("0");
    }

    public ArrayList<Transaction> getCurrent_transaction() {
        ArrayList<Transaction> transactions = (ArrayList<Transaction>) this.current_transaction.clone();
        this.current_transaction.clear();
        return transactions;
    }
}
