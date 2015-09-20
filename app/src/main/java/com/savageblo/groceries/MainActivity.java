package com.savageblo.groceries;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    private GroceryAdapter groceryAdapter;
    private ListView groceryListView;
    private Button button;
    private Context context = this;

    NotificationCompat.Builder urgentBuilder =
            new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.abc_ic_menu_paste_mtrl_am_alpha)
                    .setContentTitle("YOUR FOOD EXPIRE")
                    .setContentText("Rite Nao");

    NotificationCompat.Builder moderateBuilder =
            new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.abc_ic_menu_paste_mtrl_am_alpha)
                    .setContentTitle("YOUR FOOD EXPIRE")
                    .setContentText("Soon");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        final Firebase firebaseRef = new Firebase("https://burning-torch-3933.firebaseio.com/items");

        groceryAdapter = new GroceryAdapter(this);

        groceryListView = (ListView)findViewById(R.id.groceryListView);
        groceryListView.setAdapter(groceryAdapter);
        groceryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete this item?");
                builder.setMessage("Are you sure you want to delete " + groceryAdapter.getItem(position).getName() + "?");
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("OK", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Firebase newRef = firebaseRef.child(groceryAdapter.removeItem(position));
                        newRef.removeValue();
                    }
                });
                builder.show();

                return true;
            }

        });

        groceryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Edit the name!");

                LayoutInflater inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
                final View v = inflater.inflate(R.layout.edit_grocery_dialog, null);

                alertDialogBuilder.setView(v);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Save",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText savedText = ((EditText) v.findViewById(R.id.itemName));
                                String itemText = savedText.getText().toString();

                                String newID = groceryAdapter.getKey(position);
                                Firebase newRef = firebaseRef.child(newID);
                                newRef.child("name").setValue(itemText);

                                groceryAdapter.setItemName(position, itemText);
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

        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                groceryAdapter.clearItemList();
                refreshData(snapshot);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        firebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded (DataSnapshot snapshot, String previousChildKey) {
                groceryAdapter.clearItemList();
                refreshData(snapshot);
            }
            @Override
            public void onChildChanged (DataSnapshot snapshot, String previousChildKey) {
                groceryAdapter.clearItemList();
                refreshData(snapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                groceryAdapter.clearItemList();
                refreshData(snapshot);
            }
            @Override
            public void onChildMoved (DataSnapshot snapshot, String previousChildKey) {
                groceryAdapter.clearItemList();
                refreshData(snapshot);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        final Handler handler = new Handler();

        // Define the task to be run here
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.e("Handlers", "Called");
                // Repeat this runnable code again every 2 seconds
                handler.postDelayed(this, 5000);

                Context context = getApplicationContext();
                CharSequence text = "Hello toast!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        };
        // Kick off the first runnable task right away
        handler.post(runnable);

        addListenerOnButton();
    }

    public void refreshData(DataSnapshot snapshot) {
        if (snapshot.hasChildren()) {
            for (DataSnapshot currentItem: snapshot.getChildren()) {
                if (currentItem.hasChild("name")) {
                    groceryAdapter.addItem(currentItem.child("name").getValue().toString(), currentItem.getKey(), stringToDate(currentItem.child("dateBought").getValue().toString()));
                    System.out.println(currentItem.getKey());
                    System.out.println(currentItem.child("name").getValue().toString());
                }
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

                int mNotificationId = 001;
// Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, urgentBuilder.build());


                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText savedText = ((EditText) v.findViewById(R.id.itemName));
                                String itemText = savedText.getText().toString();

                                Date boughtDate = Calendar.getInstance().getTime();

                                Map<String, String> post1 = new HashMap<String, String>();
                                post1.put("name",itemText);
                                post1.put("dateBought", dateToString(boughtDate));

                                Firebase firebaseRef = new Firebase("https://burning-torch-3933.firebaseio.com/");
                                Firebase postRef = firebaseRef.child("items");
                                Firebase newRef = postRef.push();
                                newRef.setValue(post1);

                                int mNotificationId = 001;
// Gets an instance of the NotificationManager service
                                NotificationManager mNotifyMgr =
                                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
                                mNotifyMgr.notify(mNotificationId, moderateBuilder.build());

                                groceryAdapter.addItem(itemText,newRef.getKey(),boughtDate);
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



    public String dateToString(Date date) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        return df.format(date);
    }

    public Date stringToDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
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
