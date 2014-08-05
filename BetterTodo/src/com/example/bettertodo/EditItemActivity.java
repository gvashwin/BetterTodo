package com.example.bettertodo;

import com.example.bettertodo.PriorityDialogFragment.TaskPriorityListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class EditItemActivity extends Activity {
	private String itemTextToEdit;
	private int itemIndexPosition;
	private String itemPrioirtyToEdit;
	private EditText editItem; 
	private RadioGroup rgEditPriority;
	
	private String TAG = "EditItemAcitivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		//Getting References to the view
		editItem = (EditText) findViewById(R.id.editItemText);
		rgEditPriority = (RadioGroup) findViewById(R.id.rgEditPriority);
		
		
		Log.v(TAG, "Getting item text from intent");
		itemTextToEdit = getIntent().getStringExtra("itemText");
		Log.v(TAG, 
				"item text to update is :"+itemTextToEdit);
		
		Log.v(TAG, "Getting item priority from intent");
		itemPrioirtyToEdit = getIntent().getStringExtra("itemPriority");
		Log.v(TAG, 
				"item prioirty to update is :"+itemPrioirtyToEdit);
		
		/*
		Log.v(TAG, "Getting item position from intent");
		itemIndexPosition = getIntent().getIntExtra("itemIndex", -1);
		Log.v(TAG, 
				"Item position is :"+itemIndexPosition);
		
		if(itemIndexPosition == -1) {
			Log.e(TAG, 
					"The index position got from the intent can only be 0 " +
					"or some positive value, it cannot be negative");
			throw new IllegalArgumentException("ERROR!!!!! The item Index is -1");
			
		}*/
		
		// Setting task name to edit, in edit activity
		editItem.setText(itemTextToEdit);
		editItem.setSelection(itemTextToEdit.length());
		
		// Setting priority to edit it edit activity
		Log.v(TAG, "Item to edit is :"+itemTextToEdit);
		if(itemPrioirtyToEdit.equalsIgnoreCase("High")){
			rgEditPriority.check(R.id.rbEditHigh);
		} else if(itemPrioirtyToEdit.equalsIgnoreCase("Medium")) {
			rgEditPriority.check(R.id.rbEditMedium);
		} else if(itemPrioirtyToEdit.equalsIgnoreCase("Low")) {
			rgEditPriority.check(R.id.rbEditLow);
		} else {
			throw new IllegalArgumentException("InValid Task Priority");
		}
		
		
	}
	
	public void saveChanges(View v) {
		String updatedItemText = editItem.getText().toString();
		if(updatedItemText.equalsIgnoreCase("")) {
			Toast.makeText(this, "Task name cannot be empty", Toast.LENGTH_SHORT).show();
		} else {
	        int selected = rgEditPriority.getCheckedRadioButtonId();
	        RadioButton priority = (RadioButton) rgEditPriority.findViewById(selected);
	        Log.v(TAG, "Selected Radio button is "+priority.getText());
	        
			Intent responseData = new Intent();
			responseData.putExtra("updatedItemText", updatedItemText);
			responseData.putExtra("updatedItemPriority", priority.getText().toString());
			setResult(RESULT_OK, responseData); 
			Log.v("EditItemAcitivity", "Calling finish");
			finish();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
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
}
