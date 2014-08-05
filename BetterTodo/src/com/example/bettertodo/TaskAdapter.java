/*
 * Custom ArrayAdapter to display tasks in the todo list.
 */

package com.example.bettertodo;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskAdapter extends ArrayAdapter<Task> {
	private final Context context;
    private final ArrayList<Task> taskList;

    public TaskAdapter(Context context, ArrayList<Task> list) {

        super(context, 0, list);

        this.context = context;
        this.taskList = list;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // Get the data item for this position
       Task task = getItem(position);    
       // Check if an existing view is being reused, otherwise inflate the view
       if (convertView == null) {
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_layout, parent, false);
       }
       // Lookup view for data population
       TextView name = (TextView) convertView.findViewById(R.id.taskName);
       TextView priority = (TextView) convertView.findViewById(R.id.taskPriority);
       // Populate the data into the template view using the data object
       name.setText(task.getName());
       priority.setText(task.getPriorityAsString());
       Priority taskPriority = task.getPriority();
       priority.setTextColor(Color.WHITE);
       switch(taskPriority){
    	   case HIGH:
    		   priority.setBackgroundColor(Color.RED);
    		   break;
    	   case MEDIUM:
    		   priority.setBackgroundColor(Color.parseColor("#FFA500"));
    		   break;
    	   case  LOW:
    		   priority.setBackgroundColor(Color.BLUE);
    		   break;
    	default:
    		
    		   
       }
       // Return the completed view to render on screen
       return convertView;
   }
}
