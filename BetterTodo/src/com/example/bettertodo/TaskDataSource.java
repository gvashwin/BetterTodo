/*
 *  Task Data Source.
 *  Interfaces with DB in getting all the tasks.
 *  Updating a specific task in DB.
 *  Removing a task in DB.
 *  Also takes care of opening and closing DB Connection.
 */


package com.example.bettertodo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class TaskDataSource {
	private static final String TAG = "TASK_DATA_SOURCE";
	private SQLiteDatabase db;
	private DbHelper dbHelper;
	private String allColumns [] = {
			DbHelper.COL_TASK_NAME,
			DbHelper.COL_TASK_PRIORITY
	};
	
	public TaskDataSource(Context ctx){
		dbHelper = new DbHelper(ctx);
	}
	
	// Open helper function to open connection to DB
	public void openDb() throws SQLException {
		Log.v(TAG, "Opening connection to DB");
		db = dbHelper.getWritableDatabase();
	}
	
	// Close helper function to close connectiont to DB
	public void closeDb(){
		Log.v(TAG, "Closing connection to DB");
		dbHelper.close();
	}
	
	/*
	 * Given task name and priority
	 * Creates a task and inserts into the DB
	 */
	
	public void createTask(String name, String priority) {
		openDb();
		ContentValues values = new ContentValues();
		values.put(DbHelper.COL_TASK_NAME, name);
		values.put(DbHelper.COL_TASK_PRIORITY, priority);
		Log.v(TAG, "Inserting Task :"+name+"to DB with priority "+priority );
		db.insert(DbHelper.TABLE_NAME, null, values);
		closeDb();
	}
	
	/*
	 * Connects to the database and get the list of 
	 * all tasks in the database.
	 */
	
	public ArrayList<Task> getAllTasks(){
		Log.v(TAG, "Getting all tasks from Db");
		ArrayList <Task> items = new ArrayList<Task>();
		openDb();
		Cursor c = db.query(DbHelper.TABLE_NAME, allColumns, null, null, null, null, null);
		//Log.v(TAG,"Row count is "+c.getInt(0));
		if( c != null && c.moveToFirst()) {
			do {
				Task item = new Task(c.getString(0).toString(),c.getString(1).toString());
				items.add(item);
			} while(c.moveToNext());
		}
		closeDb();
		return items;
		
	}
	
	/*
	 * Task update function which is called when 
	 * a specific task name or priority needs to be updated
	 * 
	 */
	public int updateTask(String oldText, String newText, String oldPriority, String newPriority) {
		openDb();
		ContentValues values = new ContentValues();
		values.put(DbHelper.COL_TASK_NAME, newText);
		values.put(DbHelper.COL_TASK_PRIORITY, newPriority);
		Log.v(TAG, "Updating task item : "+oldText+" to item :"+newText);
		Log.v(TAG, "Updating task item priority : "+oldPriority+" to item :"+newPriority);
		int rv = db.update(DbHelper.TABLE_NAME, values, DbHelper.COL_TASK_NAME +" =?", new String[]{oldText});
		closeDb();
		return rv;
		
	}
	
	/*
	 * Given a task name, removes the task from the table.
	 */
	
	public void removeTask(String text) {
		openDb();
		Log.v(TAG, "Removing todo item : "+text);
		db.delete(DbHelper.TABLE_NAME, DbHelper.COL_TASK_NAME +" =?", new String[]{text});
		closeDb();
	}
}