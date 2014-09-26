package com.example.cmput301todoapplication;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class ArchivedActivity extends Activity implements DialogFragmentListener{

	private ItemArrayAdapter adapter;
	public AccessData databaseAccess;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_archived);
        App.setContext(this);
        databaseAccess = new AccessData();
        updateList();
	}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks
	        int id = item.getItemId();
	        if (id == R.id.summary_button) {
	        	summaryDialog(this.findViewById(MODE_PRIVATE));
	        }
	        if (id == R.id.add_item_button) {
	        	addItemDialog(this.findViewById(MODE_PRIVATE));
	        }
	        if (id == R.id.seeAll) {
	            Intent intent = new Intent(ArchivedActivity.this, MainActivity.class);
	            startActivity(intent);      
	            finish();
	        }
	        if (id == R.id.email_button) {
	        	emailDialog(this.findViewById(MODE_PRIVATE));

	        }

	        return super.onOptionsItemSelected(item);
	    }
	    
	    public void addItemDialog(View view) {
	    	DialogFragment dialog = new AddItemDialog();
	    	dialog.show(getFragmentManager(), INPUT_SERVICE);
	    }
	    
	    public void emailDialog(View view) {
	    	DialogFragment dialog = new EmailItemDialog();
	    	dialog.show(getFragmentManager(), INPUT_SERVICE);
	    }
	    
	    public void summaryDialog(View view) {
	    	DialogFragment dialog = new SummaryDialog();
	    	dialog.show(getFragmentManager(), INPUT_SERVICE);
	    }
	    
	
    public void updateList()
    {
    	final ListView listView = (ListView) findViewById(R.id.itemListView);
        final ArrayList<toDo> items = new ArrayList<toDo>();
        SharedPreferences savedItems = getSharedPreferences("Items", Context.MODE_PRIVATE);
        
        Gson gson = new Gson();
        Map<String,?> entries = savedItems.getAll();
        Set<String> keys = entries.keySet();
        
        for (String key : keys) {
            String json = savedItems.getString(key, "");
            toDo item = gson.fromJson(json, toDo.class);
            if (item != null && item.getArchived() == true) {
            	items.add(item);
            }
        }
        adapter = new ItemArrayAdapter(this,
                		R.layout.todo_information, items);
            			listView.setAdapter(adapter);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
					View view, int position, long id) {
					// Remove or archive item
		            AlertDialog.Builder dialog = new AlertDialog.Builder(ArchivedActivity.this);
		            dialog.setTitle(R.string.dialog_remove_item);
		            dialog.setMessage("Delete or archive this item?");
		            final toDo itemToRemove = adapter.getItem(position);

		            dialog.setNegativeButton("Cancel", null);
		            if(itemToRemove.getArchived() == false) {
		            dialog.setNeutralButton("Archive", new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							itemToRemove.Archived = !itemToRemove.getArchived();
							databaseAccess.modifyObject(App.getContext(), itemToRemove);
							updateList();
						}
		            
		            });
		            }
		            else {
			            dialog.setNeutralButton("Unarchive", new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								itemToRemove.Archived = !itemToRemove.getArchived();
								databaseAccess.modifyObject(App.getContext(), itemToRemove);
								updateList();
							}
			            
			            });
		            }
		            dialog.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {        
		                items.remove(itemToRemove);
		                databaseAccess.deleteObject(App.getContext(), itemToRemove);
		                adapter.notifyDataSetChanged(); 
		             }
		            });
		             dialog.show();  
		             return true;
		        }
				

			});
    }

	@Override
	public void onReturnValue(String text) {
        Random rand = new Random();
        int id = rand.nextInt(100000);
		toDo item = new toDo(id, text);
		if (databaseAccess.saveObject(App.getContext(), item)) {
			//refresh list to show changes
			updateList();
			final ListView listView = (ListView) findViewById(R.id.itemListView);
			((BaseAdapter) listView.getAdapter()).notifyDataSetChanged(); 
		}	
	}
}
