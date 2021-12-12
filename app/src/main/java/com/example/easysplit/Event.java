package com.example.easysplit;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;

public class Event implements Serializable{
    String name;
    String createdBy;
    String distributionType;
    String billAmount;
    HashMap<String,String> paidBy = new HashMap<String,String>();
    HashMap<String,String> participants = new HashMap<String,String>();
    HashMap<String,String> mailIdsNames = new HashMap<String,String>();
    ArrayList<String> membersMails= new ArrayList<String>();

    public Event(){

    }
    public Event(String name, String cb,String distributionType, String billAmount, HashMap paidBy, HashMap participants) {
        this.name = name;
        this.createdBy = cb;
        this.distributionType=distributionType;
        this.billAmount=billAmount;
        this.paidBy=paidBy;
        this.participants=participants;
    }

    public String getDistributionType() {
        return distributionType;
    }

    public void setDistributionType(String distributionType) {
        this.distributionType = distributionType;
    }

    public String getBillAmount() {
        return this.billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }



    public HashMap<String,String> getParticipants() {
        return participants;
    }

    public void setParticipants(HashMap<String,String> participants) {
        this.participants = participants;
    }

    public HashMap<String,String> getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(HashMap<String,String>  paidBy) {
        this.paidBy = paidBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}