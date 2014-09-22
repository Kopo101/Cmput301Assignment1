package com.example.cmput301todoapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class ItemArrayAdapter extends ArrayAdapter<toDo> implements Filterable{

    HashMap<toDo, Integer> mIdMap = new HashMap<toDo, Integer>();
    private MainActivity databaseAccess;
    public List<toDo> items;
    private ToDoFilter filter;
    
    public ItemArrayAdapter(Context context, int textViewResourceId,
        List<toDo> objects) {
      super(context, textViewResourceId, objects);
      items = objects;
      filter = new ToDoFilter();
      for (int i = 0; i < objects.size(); ++i) {
        mIdMap.put(objects.get(i), i);
      }
      databaseAccess = (MainActivity) context;
    }
    private class ViewHolder {
    	   TextView code;
    	   CheckBox name;
    	  }
    	 
    	  @SuppressLint("InflateParams") @Override
    	  public View getView(int position, View convertView, ViewGroup parent) {
    	 
    	   ViewHolder holder = null;
    	   Log.v("ConvertView", String.valueOf(position));
    	 
    	   if (convertView == null) {
    	   LayoutInflater vi = (LayoutInflater)getContext().getSystemService(
    	     Context.LAYOUT_INFLATER_SERVICE);
    	   convertView = vi.inflate(R.layout.todo_information, null);
    	 
    	   holder = new ViewHolder();
    	   holder.code = (TextView) convertView.findViewById(R.id.code);
    	   holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
    	   convertView.setTag(holder);

    	    holder.name.setOnClickListener( new View.OnClickListener() {  
    	     public void onClick(View v) {  
    	      CheckBox cb = (CheckBox) v ;  
    	      toDo item = (toDo) cb.getTag();
    	      item.setSelected(!item.getChecked());
    	      databaseAccess.saveObject(item);
    	     }  
    	    });  
    	   } 
    	   else {
    	    holder = (ViewHolder) convertView.getTag();
    	   }
    	 
    	   toDo item = getItem(position);
    	   holder.code.setText(item.getText());
    	   holder.name.setChecked(item.getChecked());
    	   holder.name.setTag(item);
    	   return convertView;
    	 
    	  }


	@Override
    public long getItemId(int position) {
      toDo item = getItem(position);
      return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }
    private class ToDoFilter extends Filter {
    	
    	@Override
    	protected FilterResults performFiltering(CharSequence constraint) {
    	    FilterResults results = new FilterResults();
    	    // We implement here the filter logic
    	    if (constraint == null || constraint.length() == 0) {
    	        // No filter implemented we return all the list
    	        results.values = items;
    	        results.count = items.size();
    	    }
    	    else {
    	        // We perform filtering operation
    	        List<toDo> nToDo = new ArrayList<toDo>();
    	         
    	        for (toDo t : items) {
    	            if (t.getArchived())
    	                nToDo.add(t);
    	        }
    	         
    	        results.values = nToDo;
    	        results.count = nToDo.size();
    	 
    	    }
    	    return results;
    	}

    	@Override
    	protected void publishResults(CharSequence constraint, FilterResults results) {
    	    if (results.count == 0)
    	        notifyDataSetInvalidated();
    	    else {
    	        items = (List<toDo>) results.values;
    	        notifyDataSetChanged();
    	    }
    		
    	}

    	
    }
    
    public Filter getFilter() {
    	if (filter == null) {
    		filter = new ToDoFilter();
    	}
    	return filter;
    }


  }
