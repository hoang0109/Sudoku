package org.example.Sudoku;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper{
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/org.example.Sudoku/databases/";
    private static String DB_NAME = "Database.s3db";
    private SQLiteDatabase myDataBase; 
    private final Context myContext;
 
    public DataBaseHelper(Context context) {
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
    public void createDataBase() throws IOException{
    	boolean dbExist = checkDataBase();
    	if(dbExist){
    	}else{
        	this.getReadableDatabase();
        	try {
    			copyDataBase();
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	} finally {
                this.close();
            }
    	}
 
    }
 
    private boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(Exception e){
    		//database does't exist yet.
    	}
    	if(checkDB != null){
    		checkDB.close();
    	}
    	return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException{
    	try {
	    	//Open your local db as the input stream
	    	InputStream myInput = myContext.getAssets().open(DB_NAME);
	    	// Path to the just created empty db
	    	String outFileName = DB_PATH + DB_NAME;
	    	//Open the empty db as the output stream
	    	OutputStream myOutput = new FileOutputStream(outFileName);
	    	//transfer bytes from the inputfile to the outputfile
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = myInput.read(buffer))>0){
	    		myOutput.write(buffer, 0, length);
	    	}
	    	//Close the streams
	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
    	 } catch (Exception e) {
             e.printStackTrace();
         }
 
    }

    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }
    public int[] getPuzzle(String db_,int diff){
    	String matrix2 = showDB(db_,diff);
    	int[] matrix =  StringToInt(matrix2);
    	return matrix;
    }
    public int[] StringToInt(String matrix){
		int[] ketqua = new int[matrix.length()];
		for (int i = 0; i < ketqua.length; i++) {
			ketqua[i]=Integer.parseInt(matrix.charAt(i)+"");
		}
		return ketqua;
				
	}
	public int[] Matrix_ToLv(int level,int[] Matrix){
		 Random rand = new Random();
		 
		for (int i = 0; i < level; i++) {
			int randomNum = rand.nextInt((level - 0) + 1) + 0;
			Matrix[randomNum]=0;
		}
		return Matrix;
	}
	public String showDB(String db_,int diff){
		DataBaseHelper dbManager =  this;
        String sql = "SELECT * FROM "+db_+" where dif="+diff+"  ORDER BY Random() LIMIT 1";
        
        // Chep File MYPTITDEMO.sqlite vao "/data/data/com.example.democonnectdbfromasset/databases/";
        try {
	        createDataBase();
        } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }

        SQLiteDatabase db = dbManager.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        String str="";
        while(!cursor.isAfterLast()){
        	String MaTrix = cursor.getString(cursor.getColumnIndex("puzzle"));
        	str+=MaTrix;
        	cursor.moveToNext();
        }
        
        cursor.moveToLast();
//        showTxt.setText(cursor.getString(cursor.getColumnIndex("QCONTENT")));
        cursor.close();
        db.close();
        dbManager.close();
        return str;
	}
    @Override
	public synchronized void close() {
    	    if(myDataBase != null)
    		    myDataBase.close();
            SQLiteDatabase.releaseMemory();
    	    super.close();
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
}