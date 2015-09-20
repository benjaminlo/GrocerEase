package com.savageblo.groceries;

import java.util.Calendar;
import java.util.Date;

public class GroceryItem {
    private String key;
    private String name;
    private Date bought;
    private Date expiry;
    private boolean wasNotified;
    private boolean isExpired;

    public GroceryItem() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getBought() {
        return bought;
    }

    public void setBought(Date bought) {
        this.bought = bought;
    }

    public void setExpiryDate (Date exp) {
        this.expiry = exp;
    }

    public Date getExpiryDate () {
        return expiry;
    }

    public boolean getIsExpired () {
        return isExpired;
    }

    public void setIsExpired (boolean x) {
        this.isExpired = x;
    }

    public boolean getWasNotified () {
        return wasNotified;
    }

    public void setWasNotified (boolean x) {
        this.wasNotified = x;
    }

    public boolean validateExpired () {
        long timeleft = (expiry.getTime() - new Date().getTime());
        if (timeleft < 0)
            isExpired = true;
        return isExpired;
    }

    public long remainingTime () {
        long timeleft = (expiry.getTime() - new Date().getTime());
        return timeleft;
    }



}
