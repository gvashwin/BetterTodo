/*
 * This class creates the priority dialog fragment.
 */

package com.example.bettertodo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

@SuppressLint("NewApi")
public class PriorityDialogFragment extends DialogFragment {
	private static final String TAG = "PriorityDialogFragment";
	private RadioGroup rgPriority;
	public PriorityDialogFragment() {
		
	}
	interface TaskPriorityListener {
        public void onSave(CharSequence charSequence);
    }
	
	@Override
	@SuppressLint("NewApi")
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		getDialog().setTitle("Priority ?");
		setCancelable(false);
        final View view = inflater.inflate(R.layout.priority_dialog, container);
        Button saveBtn = (Button) view.findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	getSelection(view);
            }
        });
        return view;
    }
    
	public void getSelection(View view) {
		rgPriority = (RadioGroup) view.findViewById(R.id.rgPriority);
        int selected = rgPriority.getCheckedRadioButtonId();
        RadioButton priority = (RadioButton) rgPriority.findViewById(selected);
        Log.v(TAG, "Selected Radio button is "+priority.getText());
        TaskPriorityListener mainActivity = (TaskPriorityListener) getActivity();
        mainActivity.onSave(priority.getText().toString());
		getDialog().dismiss();
	}
	

}