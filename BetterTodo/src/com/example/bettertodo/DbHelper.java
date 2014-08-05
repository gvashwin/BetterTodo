/*
 * Database Helper Class.
 *
 * Creates the database and tables.
 * Drops the tables on upgrade.
 */

package com.example.bettertodo;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "DB_HELPER";
	
	// DB Version
	private static final int DATABASE_VERSION = 1;
	
	// DB Name
	private static final String DATABASE_NAME = "Tasks.db";
	
	// Table Name
	public static final String TABLE_NAME = "TaskItems";
	
	// Column Names
	public static final String COL_TASK_NAME = "taskName";
	public static final String COL_TASK_PRIORITY = "taskPriority";
	
	
	
	
	/*
	 * Create table sql query
	 */
	private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( " 
					+COL_TASK_NAME + " TEXT" +" ," 
					+COL_TASK_PRIORITY +" TEXT" 
					+")";
	
	public DbHelper(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		Log.v(TAG, "Creating Table : "+TABLE_NAME );
		db.execSQL(CREATE_TABLE);
		Log.v(TAG, TABLE_NAME+" Table Created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {	
		db.execSQL("DROP TABLE IF EXIST" + TABLE_NAME);
		onCreate(db);
		
	}

}
