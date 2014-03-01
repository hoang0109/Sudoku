package org.example.Sudoku;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class MyDatabase extends SQLiteOpenHelper{
    private static String DB_PATH = "/data/data/com.myapp.sqllite/databases/";
    private static String DB_NAME = "msqllite.db";
    private String TAG="TAG";
    private SQLiteDatabase myDataBase; 
    private final Context myContext;
    public MyDatabase(Context context) {
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
    @Override
	public void onCreate(SQLiteDatabase db) {
    	
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
	@Override
	public synchronized void close() {
    	    if(myDataBase != null)
    		    myDataBase.close();
    	    super.close();
	}
	//kiem tra database neu ton tai thi thong bao database san dang su dung
	//neu khong thi copy database vao duong dan "DB_PATH+DB_NAME"
	public void checkAndCopyDatabase(){
		boolean dbExist = checkDataBase();
    	if(dbExist){
    		Log.d(TAG, "database already exist!");
    	}else{
        	this.getReadableDatabase();
        	try {
    			copyDataBase();
    		} catch (IOException e) {
    			Log.d(TAG,"Error copying database");
        	}
    	}
	}
	private boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    	}catch(SQLiteException e){
    	}
    	if(checkDB != null) checkDB.close();
    	return checkDB != null ? true : false;
    }
	
	private void copyDataBase() throws IOException{
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
    	String outFileName = DB_PATH + DB_NAME;
    	OutputStream myOutput = new FileOutputStream(outFileName);
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    }

    public void openDataBase() throws SQLException{
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
    
    public void ExeSQLData(String sql) throws SQLException{
    	myDataBase.execSQL(sql);
    }
    
    public Cursor QueryData(String query) throws SQLException{
    	return myDataBase.rawQuery(query, null);
    }
}