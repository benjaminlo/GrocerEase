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

    public void addItem (String name, String key) {
        GroceryItem item = new GroceryItem();
        item.setName(name);
        item.setExpired(new Date());
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

            currentRow.setTag(viewHolder);
        }

        GroceryItem currentGroceryItem = groceryItemList.get(position);

        ViewHolder viewHolder = (ViewHolder)currentRow.getTag();
        viewHolder.groceryItemNameTextView.setText(currentGroceryItem.getName());
        viewHolder.groceryItemExpireTextView.setText(new SimpleDateFormat().format(currentGroceryItem.getExpired()));

        return currentRow;
    }

    public void add(GroceryItem item) {
        groceryItemList.add(item);

        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView groceryItemNameTextView;
        public TextView groceryItemExpireTextView;
    }
}
