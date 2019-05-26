package me.happy123.aklotski;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import me.happy123.klotski.ChessPiece;

public class DataBaseHelper extends SQLiteOpenHelper {
	private static DataBaseHelper singleInstance = null;

	// The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/me.happy123.aklotski/databases/";
	private static String DB_NAME = "aklotski.s3db";
	private SQLiteDatabase myDataBase;

	private final Context myContext;

	public static DataBaseHelper getInstance(Context ctx) {
		/**
		 * use the application context as suggested by CommonsWare. this will
		 * ensure that you don't accidentally leak an Activitys context (see
		 * this article for more information:
		 * http://android-developers.blogspot.
		 * nl/2009/01/avoiding-memory-leaks.html)
		 */
		try {
			if (singleInstance == null) {
				singleInstance = new DataBaseHelper(ctx.getApplicationContext());
				singleInstance.createDataBase();
				singleInstance.openDataBase();
			}
		} catch (IOException e) {
			Toast.makeText(ctx, "Err,  can't load database", Toast.LENGTH_SHORT);
		}

		return singleInstance;
	}

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	private DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 **/
	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {
			// database does't exist yet.
		}

		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	// Add your public helper methods to access and get content from the database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
	// to you to create adapters for your views.

	public WarRecord getWar() {
		Cursor cursor = myDataBase.rawQuery(
				"select id, level, status, name, layout, walkthrough from wars where status=0 order by level ASC, id ASC limit 1", null);
		if(cursor==null) {
			cursor = myDataBase.rawQuery(
					"select id, level, status, name, layout, walkthrough from wars where id=13 limit 1", null);
		}
		cursor.moveToFirst();
		return new WarRecord(cursor.getInt(0), cursor.getInt(1),
						 	 cursor.getInt(2), cursor.getString(3),
						 	 cursor.getString(4), cursor.getString(5));

	}

	public WarRecord getWar(int warId) {
		Cursor cursor = myDataBase.rawQuery(
				"select id, level, status, name, layout, walkthrough from wars where id="+warId, null);
		if(cursor==null) {
			cursor = myDataBase.rawQuery(
					"select id, level, status, name, layout, walkthrough from wars where id=13 limit 1", null);
		}
		cursor.moveToFirst();
		return new WarRecord(cursor.getInt(0), cursor.getInt(1),
						 	 cursor.getInt(2), cursor.getString(3),
						 	 cursor.getString(4), cursor.getString(5));

	}
	
	public WarRecord getNextWar(int warId) {
		int level = 0;
		Cursor cursor = myDataBase.rawQuery("select level from wars where id="+warId, null);
		if(cursor != null) {
			cursor.moveToFirst();			
			level = cursor.getInt(0);
		}
		
		cursor = myDataBase.rawQuery(
				"select id, level, status, name, layout, walkthrough from wars where status=0 and level>=" + level + " and id>"+warId, null);
		if(cursor == null) {
			cursor = myDataBase.rawQuery(
					"select id, level, status, name, layout, walkthrough from wars where id=13 limit 1", null);
		}
		cursor.moveToFirst();
		return new WarRecord(cursor.getInt(0), cursor.getInt(1),
						 	 cursor.getInt(2), cursor.getString(3),
						 	 cursor.getString(4), cursor.getString(5));

	}
	
	public ArrayList<WarRecord> getWars(int level) {
		ArrayList<WarRecord> wars = new ArrayList<WarRecord>();
		try {
			Cursor cursor = myDataBase.rawQuery(
					"select id, level, status, name, layout, walkthrough from wars where level="
							+ level, null);
			if (cursor.moveToFirst()) {
				do {
					wars.add(new WarRecord(cursor.getInt(0), cursor.getInt(1),
										   cursor.getInt(2), cursor.getString(3),
										   cursor.getString(4), cursor.getString(5)));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.e("DataBaserHelper",
					"Error, select ... from wars:" + e.toString());
		}
		return wars;
	}
	
	public boolean getBMusic() {
		try {
			Cursor cursor = myDataBase.rawQuery("select bmusic from system", null);
			if (cursor.moveToFirst()) {
				if(cursor.getInt(0) == 1) {
					return true;
				}
			}
		} catch (Exception e) {
			Log.e("DataBaserHelper",
					"Error, select bmusic from system:" + e.toString());
		}		
		return false;
	}
	
	public void setBMusic (boolean bMusic) {

		try {
			ContentValues args = new ContentValues();
			if(bMusic) {
				args.put("bmusic", 1);
			} else {
				args.put("bmusic", 0);
			}			
			
			myDataBase.update("system", args, null, null);			
		} catch (Exception e) {
			Log.e("DataBaserHelper",
					"Error, update system set bmusic:" + e.toString());
		}		
	}		

	public void updateWarStatus(int wid, int status) {
		try {
			String strFilter = "id=" + wid;
			ContentValues args = new ContentValues();
			args.put("status", status);
			myDataBase.update("wars", args, strFilter, null);
			//myDataBase.rawQuery("update wars set status=" + status + " where id=" + wid, null);
			
		} catch (Exception e) {
			Log.e("DataBaserHelper",
					"Error, update wars set status:" + e.toString());
		}		
	}
}
