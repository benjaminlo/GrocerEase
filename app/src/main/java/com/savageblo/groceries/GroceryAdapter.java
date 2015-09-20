package com.savageblo.groceries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
    }

    public void clearItemList() {
        groceryItemList.clear();

        notifyDataSetChanged();
    }

    public void addItem (String name, String key, Date boughtDate) {
        GroceryItem item = new GroceryItem();
        item.setName(name);
        item.setBought(boughtDate);
        item.setKey(key);
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
            if (groceryItemList.get(i).isExpired(groceryItemList.get(i).timeBeforeExpiry(groceryItemList.get(i).getExpiryDate(groceryItemList.get(i).getBought())))== true && groceryItemList.get(i).wasNotified()== false) {
                groceryItemList.get(i).setNotified();
                expired = true;
                System.out.println(i);
            }
        }
        return expired;
    }

    public boolean almostExpired () {
        boolean expired = false;
        for(int i=0; i<groceryItemList.size(); i++){
            if (groceryItemList.get(i).timeBeforeExpiry(groceryItemList.get(i).getExpiryDate(groceryItemList.get(i).getBought()))< 100000000) {
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
        Date dateBought = current.getBought();
        Date expiry = current.getExpiryDate(dateBought);

        ViewHolder viewHolder = (ViewHolder)currentRow.getTag();
        viewHolder.groceryItemNameTextView.setText(current.getName());
        viewHolder.groceryItemExpireTextView.setText(new SimpleDateFormat().format(expiry));
        viewHolder.groceryItemStatusTextView.setText(current.expired(current.timeBeforeExpiry(expiry)));

        return currentRow;
    }

    public static class ViewHolder {
        public TextView groceryItemNameTextView;
        public TextView groceryItemExpireTextView;
        public TextView groceryItemStatusTextView;
    }
}
