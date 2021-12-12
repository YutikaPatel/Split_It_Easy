package com.example.easysplit;

import java.util.HashMap;

public class EvenTester {
    String name;
    String bill;
    int nbill;

    //HashMap<String,String> paidBy= new HashMap<String,String>();
    EvenTester(){

    }



   /* public HashMap<String, String> getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(HashMap<String, String> paidBy) {
        this.paidBy = paidBy;
    }
*/
    EvenTester(String name, String bill){
        this.name=name;
        this.bill=bill;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBill() {
        return bill;
//String.valueOf(new Integer(bill));
    }



    public void setBill(String bill) {
        this.bill = bill;
        //Integer.parseInt(bill);
    }
}
