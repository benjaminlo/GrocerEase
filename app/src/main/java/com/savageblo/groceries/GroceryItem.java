package com.savageblo.groceries;

import java.util.Calendar;
import java.util.Date;

public class GroceryItem {
    private String key;
    private String name;
    private Date bought;
    private Date expiry;
    private boolean notified;
    private boolean expired=false;

    public GroceryItem() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBought() {
        return bought;
    }

    public void setBought(Date bought) {
        this.bought = bought;
    }

    public Date getExpiryDate (Date dateBought) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBought);
        cal.add(Calendar.DAY_OF_YEAR, 7);
        expiry = cal.getTime();
        return expiry;
    }

    public long timeBeforeExpiry (Date expiry) {
        long timeleft = (expiry.getTime() - new Date().getTime());
        System.out.println ("Time Left til Expiry:");
        System.out.println (timeleft);
        return timeleft;
    }

    public String expired (long timeleft) {
        if (timeleft > 0)
            return "YUMMY!";
        return "EXPIRED!";
    }

    public boolean isExpired (long timeleft) {
        if (timeleft > 0)
            return false;
        return true;
    }
    public void setNotified () {
        notified = true;
    }

    public boolean wasNotified () {
        return notified;
    }
}
