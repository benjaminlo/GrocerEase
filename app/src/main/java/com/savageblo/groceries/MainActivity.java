package com.savageblo.groceries;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    private GroceryAdapter groceryAdapter;
    private ListView groceryListView;
    private Button button;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        final Firebase firebaseRef = new Firebase("https://burning-torch-3933.firebaseio.com/");

        groceryAdapter = new GroceryAdapter(this);

        groceryListView = (ListView)findViewById(R.id.groceryListView);
        groceryListView.setAdapter(groceryAdapter);
        groceryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        firebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                refreshData(snapshot);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        addListenerOnButton();
    }

    public void refreshData(DataSnapshot snapshot) {
        DataSnapshot currentLevel = snapshot;
        if (currentLevel.hasChild("items")) {
            currentLevel = currentLevel.child("items");
        }

        for (DataSnapshot currentItem: currentLevel.getChildren()) {
            if (currentItem.hasChild("name")) {
                groceryAdapter.addItem(currentItem.child("name").getValue().toString());
            }
        }
    }

    public void addListenerOnButton() {
        button = (Button) findViewById(R.id.addItembutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Add a new item!");
                LayoutInflater inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
                final View v = inflater.inflate(R.layout.add_grocery_dialog, null);
                alertDialogBuilder.setView(v);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Save",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText savedText = ((EditText) v.findViewById(R.id.itemName));
                                String itemText = savedText.getText().toString();
                                groceryAdapter.addItem(itemText);

                                Map<String, String> post1 = new HashMap<String, String>();
                                post1.put("name", itemText);
                                post1.put("date", "DATE");

                                Firebase firebaseRef = new Firebase("https://burning-torch-3933.firebaseio.com/");
                                Firebase postRef = firebaseRef.child("items");
                                postRef.push().setValue(post1);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
