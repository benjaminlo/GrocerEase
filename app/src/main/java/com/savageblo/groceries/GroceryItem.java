package com.savageblo.groceries;

import java.util.Calendar;
import java.util.Date;

public class GroceryItem {
    private String key;
    private String name;
    private Date bought;

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
        return cal.getTime();
    }
}
