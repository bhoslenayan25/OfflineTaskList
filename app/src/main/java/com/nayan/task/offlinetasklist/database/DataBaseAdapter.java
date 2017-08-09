package com.nayan.task.offlinetasklist.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nayan on 9/8/17.
 */

public class DataBaseAdapter {

	private static final String DATABASE_NAME = "offlinetasklist";
	private static final int DATABASE_VERSION = 1;
	private DBHelper ourHelper;
	private final Context ourContext;
	public static SQLiteDatabase ourDatabase;

	private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
			db.execSQL(TaskList.CREATE_TABLE);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
			db.execSQL("DROP TABLE IF EXISTS " + TaskList.DATABASE_TABLE);
			onCreate(db);
		}

	}

	public DataBaseAdapter(Context c) {
		ourContext = c;
	}

	public DataBaseAdapter open() throws SQLException {
		ourHelper = new DBHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

}
