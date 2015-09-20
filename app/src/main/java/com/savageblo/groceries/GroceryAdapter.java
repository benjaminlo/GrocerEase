package com.savageblo.groceries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroceryAdapter extends BaseAdapter {
    private List<GroceryItem> groceryItemList;

    private Context context;

    public GroceryAdapter(Context ctx) {
        groceryItemList = new ArrayList<>();

        context = ctx;

        Firebase.setAndroidContext(ctx);
    }

    public void clearItemList() {
        groceryItemList.clear();

        notifyDataSetChanged();
    }

    public void addItem (String name, String key, Date boughtDate, Date expiryDate, boolean expired,
                         boolean notified) {
        GroceryItem item = new GroceryItem();
        item.setName(name);
        item.setKey(key);
        item.setBought(boughtDate);
        item.setExpiryDate(expiryDate);
        item.setIsExpired(expired);
        item.setWasNotified(notified);
        groceryItemList.add(item);

        notifyDataSetChanged();
    }

    public void setItemName (int position, String newName) {
        groceryItemList.get(position).setName(newName);

        notifyDataSetChanged();
    }

    public String removeItem (int position) {
        String key = groceryItemList.get(position).getKey();
        groceryItemList.remove(position);

        notifyDataSetChanged();

        return key;
    }

    public String getKey (int position) {
        return groceryItemList.get(position).getKey();
    }

    public boolean checkExpired () {
        boolean expired = false;
        for(int i=0; i<groceryItemList.size(); i++){
            if (groceryItemList.get(i).getIsExpired()== true && groceryItemList.get(i).getWasNotified()== false) {
                groceryItemList.get(i).setWasNotified(true);
                expired = true;
                System.out.println(i);
            }
        }
        return expired;
    }

    public boolean almostExpired () {
        boolean expired = false;
        for(int i=0; i<groceryItemList.size(); i++){
            if (groceryItemList.get(i).remainingTime()< 86400000) {
                expired = true;
            }
        }
        return expired;
    }


    @Override
    public int getCount() {
        return groceryItemList.size();
    }

    @Override
    public GroceryItem getItem(int position) {
        return groceryItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View currentRow = convertView;

        if (currentRow == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            currentRow = inflater.inflate(R.layout.grocery_item, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.groceryItemNameTextView = (TextView)currentRow.findViewById(R.id.groceryItemName);
            viewHolder.groceryItemExpireTextView = (TextView)currentRow.findViewById(R.id.groceryItemExpire);
            viewHolder.groceryItemStatusTextView = (TextView)currentRow.findViewById(R.id.groceryItemStatus);

            currentRow.setTag(viewHolder);
        }

        GroceryItem current = groceryItemList.get(position);
        //Date dateBought = current.getBought();
        Date expiry = current.getExpiryDate();

        String newText = "";
        boolean isExpired = current.validateExpired();

        if (isExpired==true) {
            newText = "YUCKY";
            final Firebase firebaseRef = new Firebase("https://burning-torch-3933.firebaseio.com/items");
            String newID = getKey(position);
            Firebase newRef = firebaseRef.child(newID);
            newRef.child("isExpired").setValue("true");
            newRef.child("wasNotified").setValue("true");
        }
        else
            newText = "YUMMY";

        ViewHolder viewHolder = (ViewHolder)currentRow.getTag();
        viewHolder.groceryItemNameTextView.setText(current.getName());
        viewHolder.groceryItemExpireTextView.setText(new SimpleDateFormat().format(expiry));
        viewHolder.groceryItemStatusTextView.setText(newText);

        return currentRow;
    }

    public static class ViewHolder {
        public TextView groceryItemNameTextView;
        public TextView groceryItemExpireTextView;
        public TextView groceryItemStatusTextView;
    }
}
