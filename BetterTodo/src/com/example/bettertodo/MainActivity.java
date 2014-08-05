/*
 * Main todo activity.
 * This is activity displayed when the app is launched.
 * 
 */

package com.example.bettertodo;

import java.util.ArrayList;
import java.util.Collections;

import com.example.bettertodo.Priority;
import com.example.bettertodo.R;
import com.example.bettertodo.Task;
import com.example.bettertodo.TaskAdapter;
import com.example.bettertodo.PriorityDialogFragment;
import com.example.bettertodo.PriorityDialogFragment.TaskPriorityListener;
import com.example.bettertodo.EditItemActivity;
import com.example.bettertodo.MainActivity;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements TaskPriorityListener {

	
	private String taskName;
	private Priority taskPriority = Priority.INVALID;
	private ListView taskListView;
	private EditText taskEditText;
	private Button	addButton;
	TaskAdapter tAdapter;
	ArrayList<Task> taskList;
	private final int REQUEST_CODE = 101;
	private TaskDataSource taskDS;
	private String TAG = "MAIN_ACTIVITY";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Creating a data source object
		// to handle DB work
		taskDS = new TaskDataSource(this);
		
		// Getting reference to the items in the view
		taskListView = (ListView) findViewById(R.id.taskListView);
		taskEditText = (EditText) findViewById(R.id.addTaskEditText);
		addButton = (Button) findViewById(R.id.addButton);
		
		
		// Getting all taks from the table
		taskList = taskDS.getAllTasks();
		
		// Creating custom task adapter
		tAdapter = new TaskAdapter(this, taskList);
		
		// Attach the adapter to a ListView
		taskListView.setAdapter(tAdapter);
		
		// Sorting the items in the todo list or task list
		// based on the priority. High , Medium and Low
		Collections.sort(taskList);
		tAdapter.notifyDataSetChanged();
		setupOnLongClickListener();
		setupOnClickListener();
	}
	
	// On long click on an item remove it from the task list.
	private void setupOnLongClickListener() {
		taskListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item,
					int pos, long id) {
				Task toRemove = taskList.get(pos);
				// Removing it from task list.
				taskList.remove(pos);
				// Sorting after remove.
				Collections.sort(taskList);
				
				// Making Changes to DB to remove task
				taskDS.removeTask(toRemove.getName());	
				tAdapter.notifyDataSetChanged();
				return true;
			}
    		
    	});
	}

	// On Click  on an item users are taken to edit activity
	private void setupOnClickListener() {
    	taskListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View item,
					int pos, long id) {
				Task itemToEdit = taskList.get(pos);
				int itemIndex = pos;
				taskName = taskList.get(pos).getName();
				taskPriority = taskList.get(pos).getPriority();
				// Passing the selected or clicked item text and the index position to edit activity
				Log.v("TodoActivity", 
							"OnItemClickListerner invoked");
				Log.v("TodoActivity", 
							"Calling  gotoEditActivity");
				gotoEditActivity(pos,itemToEdit.getName(), itemToEdit.getPriorityAsString());
				
			}
			// EditActivity
			private void gotoEditActivity(int itemIndex, String itemText, String itemPriority) {
				Log.v("TodoActivity", 
						"Creating the intent for edit activity");
				Intent i = new Intent(MainActivity.this , EditItemActivity.class);

				// Adding the item text as an extra
				Log.v("TodoActivity", 
						"Adding Extra item text :"+itemText);
				i.putExtra("itemText", itemText);
				i.putExtra("itemPriority", itemPriority);
				Log.v("TodoActivity", 
						"Starting activity for result with request code :"+REQUEST_CODE);
				
				// Starting Activity and waiting for results.
				startActivityForResult(i, REQUEST_CODE); // brings up the second activity
			}
    		
    	});
		
	}
	
	
	// This call back function gets invoked when 
	// Edit item activity is complete
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

	     String newItemText = data.getExtras().getString("updatedItemText");
	     String newPrioirty = data.getExtras().getString("updatedItemPriority");
	     
	     
	     Task oldTask = new Task(taskName,taskPriority);
	     int itemIndexToUpdate = taskList.indexOf(oldTask);
	     Task taskToUpdate  = taskList.get(itemIndexToUpdate);
	     taskToUpdate.setName(newItemText);
	     if(newPrioirty.equalsIgnoreCase("High")) {
	    	 taskToUpdate.setPriority(Priority.HIGH);
	     } else if (newPrioirty.equalsIgnoreCase("Medium")) {
	    	 taskToUpdate.setPriority(Priority.MEDIUM);
	     } else if (newPrioirty.equalsIgnoreCase("Low")) {
	    	 taskToUpdate.setPriority(Priority.LOW);
	     } else {
	    	 throw new IllegalArgumentException("InValid Task Priority");
	     }
	     // Making Changes to DB to update a task
	     taskDS.updateTask(taskName,newItemText, oldTask.getPriorityAsString(),newPrioirty );
	     Collections.sort(taskList);
		 tAdapter.notifyDataSetChanged();
	  }
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// This function is called when the add button is clicked
	public void addTask(View v){
		taskName = taskEditText.getText().toString().trim();
		if(taskName.equalsIgnoreCase("")) {
			Toast.makeText(this, "Cannot add empty task. Please type in some text", Toast.LENGTH_SHORT).show();
		} else {
			Task toAdd = new Task(taskName);
			if(taskList.contains(toAdd)) {
				Toast.makeText(this, "This task is already in the todo list", Toast.LENGTH_SHORT).show();
			} else {
				showPriorityDialog(v);
			}
		}
	}
	
	// This function shows the priority dialog fragment.
	// In which users can choose a priority for the task.
	public void showPriorityDialog(View v) {
		FragmentManager fm = getFragmentManager();
        PriorityDialogFragment pdf = new PriorityDialogFragment();
        pdf.setRetainInstance(true);
        pdf.show(fm, "ChoosePriority");
	}
	
	// Call back function getting invoked when save button is
	// clicked on the dialog fragment
	public void onSave(CharSequence charSequence) {
		String tPriority = charSequence.toString();
		
		// Making Changes to DB to create a task
		Log.v(TAG,"Adding new Task "+taskName+" with priority "+tPriority +" to DB");
		taskDS.createTask(taskName,tPriority);
		
		Task t = new Task(taskName,tPriority);
		taskList.add(t);
		Log.v(TAG,"Adding new Task "+taskName+" with priority "+tPriority +" to ArrayList");
		
		Log.v(TAG,"Sorting ArrayList");
		Collections.sort(taskList);
		Log.v(TAG,"Notifying data changed");
		tAdapter.notifyDataSetChanged();
		taskEditText.setText("");
		Toast.makeText(this, "" +charSequence+" - Priority task added", Toast.LENGTH_SHORT).show();
	}
	
}
