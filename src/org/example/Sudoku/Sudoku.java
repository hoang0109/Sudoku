package org.example.Sudoku;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
public class Sudoku extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private static final String TAG = "Sudoku" ;
	int level=0;
	 public static Handler finishedHandler = new Handler() {
	      @Override public void handleMessage(Message msg) {
	          pd.dismiss();
	          //start new activity
	      }
	  };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Set up clicks listener all button
        View newButton = findViewById(R.id.new_button);
        	 newButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        	 aboutButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        	 exitButton.setOnClickListener(this);
       }
   
    public void onClick(View v)
    {
    	switch(v.getId()){
    	case R.id.about_button:
    		Intent  i = new Intent(this, About.class);
    		startActivity(i);
    		break ;
    	case R.id.new_button:
    		openNewGameDialog();
    		break ;
    	case R.id.exit_button:
    		finish();
    		// More Button
    	}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case R.id.settings:
    		startActivity(new Intent(this, Prefs.class));
    		return true ;
    	//More item go here
    	}
    	return false ;
    }
    
    private void openNewGameDialog(){
    	int dif =0;
    	final AlertDialog myAlertDialog = new AlertDialog.Builder(this).setTitle(R.string.start_game).setItems(R.array.difficulty, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int i) {
				// TODO Auto-generated method stub
				
				startGame(level,i);
			}

			private void startGame(int level,int i) {
				Log.v("TAG","level = "+level);
				// TODO Auto-generated method stub
				if(level==0){
				Intent intent = new Intent(Sudoku.this,Game6.class);
				intent.putExtra(Game6.KEY_DIFFICULTY,i);
				startActivity(intent);}
				if(level==1) {
					Intent intent = new Intent(Sudoku.this,Game.class);
					intent.putExtra(Game.KEY_DIFFICULTY,i);
					startActivity(intent);
				}
			}
		}).create();
    	new AlertDialog.Builder(this).setTitle(R.string.start_game).setItems(R.array.level, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int i) {
				// TODO Auto-generated method stub
				level = i;
				myAlertDialog.show();
			}

		}).show();
    	    	
    }	
	// Phuong thuc khoi dong Game theo do kho
    private void startGame(int i){
    	Log.d(TAG, "click on..."+  i);
    	// Start game here
    	Intent intent = new Intent(Sudoku.this, Game6.class);
    	//intent.putExtra(Game.KEY_DIFFICULTY, i);
    	startActivity(intent);
    }
    private static ProgressDialog pd;
    
   // phuong thuc khoi dong game khong can do kho
    private void startGame(){
    	
    	Log.d(TAG, "click on play...");
    	// Start game here
    	Intent intent = new Intent(Sudoku.this, Game6.class);
    	startActivity(intent);
  
    	
    }
    
}
