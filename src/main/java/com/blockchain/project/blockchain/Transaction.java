package com.blockchain.project.blockchain;

import java.util.Date;

public class Transaction {
    private String sender;
    private String recipient;
    private int amount;
    private String projName;
    private Integer summa;
    private Date dateEnd;
    private String description;

    public Transaction(String sender, String recipient, int amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }
    public Transaction(String projName, Integer summa, Date dateEnd, String description) {
        this.projName = projName;
        this.summa = summa;
        this.dateEnd = dateEnd;
        this.description = description;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public int getAmount() {
        return amount;
    }

    public String getProjName() {
        return projName;
    }

    public Integer getSumma() {
        return summa;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public String getDescription() {
        return description;
    }
}
