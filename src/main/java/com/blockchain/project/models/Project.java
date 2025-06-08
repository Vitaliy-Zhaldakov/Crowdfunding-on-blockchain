package com.blockchain.project.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String projName;
    @Column(nullable = false)
    private Integer target_amount;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Integer collected_amount;
    @Column(nullable = false)
    private Date dateEnd;
    @Column(nullable = false)
    private Boolean active;
    @Column(name = "tx_hash", unique = true)
    private String transactionHash;

    public Project() {}
    public Project(String projName, Integer target_amount, String description,
                   Integer collected_amount, Date dateEnd, Boolean active, String transactionHash) {
        this.projName = projName;
        this.target_amount = target_amount;
        this.description = description;
        this.collected_amount = 0;
        this.dateEnd = dateEnd;
        this.active = true;
        this.transactionHash = transactionHash;
    }

    public String getProjName() {
        return projName;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Integer getTarget_amount() {
        return target_amount;
    }

    public Integer getCollected_amount() {
        return collected_amount;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public Boolean getActive() {
        return active;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTarget_amount(Integer target_amount) {
        this.target_amount = target_amount;
    }

    public void setCollected_amount(Integer collected_amount) {
        this.collected_amount = collected_amount;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }
}
