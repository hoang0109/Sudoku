package org.example.Sudoku;
import org.example.Sudoku.*;
import org.sudoku.complete.*;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

public class Game extends Activity {
	private static final String TAG = "Sudoku" ;
	public static final String KEY_DIFFICULTY = "difficulty";
	public static int diff=1;
	private GeneratorStraighForward Generator = new GeneratorStraighForward();
	private Grid grid = new Grid();
	private int puzzle[] = new int[9 * 9] ; 
	public int[][] mypuzzle = new int[9][9];
	public DataBaseHelper dbManager =  new DataBaseHelper(this);
	/*Khao bao 1 mang 2 chieu 9x9*/
	private PuzzleView puzzleView ;
	private  ProgressDialog pd;
	 public  Handler finishedHandler = new Handler() {
	      @Override public void handleMessage(Message msg) {
	          pd.dismiss();
	          calculateUsedTiles();
	          puzzleView = new PuzzleView(Game.this);
	  		setContentView(puzzleView);
	  		puzzleView.requestFocus();
	  		/*for (int x = 0; x < 9; x++) {
	  			for (int y = 0; y < 9; y++) {
	  			 Log.d(TAG, "mypuzzle[" + x + "][" + y + "] = "
	  			 + mypuzzle[x][y]);
	  			}
	  		}*/
	          //start new activity
	      }
	  };
	 private boolean getHints(){
		 SharedPreferences pre =  PreferenceManager.getDefaultSharedPreferences(this);
		 return pre.getBoolean("hints", true);
		 
	 }
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		diff = getIntent().getIntExtra(KEY_DIFFICULTY, 1);
		pd = ProgressDialog.show(Game.this, "Đang khởi tạo", "Đang khởi tạo ma trân sudoku", true, false);
        new Thread(new Runnable() {
        	

				public void run() {
					Log.d(TAG, "onCreate");
					
					//puzzle = getPuzzle();
					puzzle = dbManager.getPuzzle("puzzle81",diff);
					Log.d(TAG, "diff = "+diff);
					mypuzzle=conver_Puzzle(puzzle);
					finishedHandler.sendEmptyMessage(0);
                }
            }).start();
        calculateUsedTiles();
        puzzleView = new PuzzleView(this);
		setContentView(puzzleView);
		puzzleView.requestFocus();
	}

	// hien keypad voi x ,y la toa do
	protected void showKeypadOrError(int x, int y) {
		int tiles[] = getUsedTiles(x, y);
		if (tiles.length == 9) {
		Toast toast = Toast.makeText(this,R.string.no_moves_lable, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		} else {
		Log.d(TAG, "showKeypad: used=" + toPuzzleString(tiles));
		Dialog v = new Keypad(this, tiles, puzzleView,getHints());
		v.show();
		}
		}
	
	protected boolean setTileIfValid(int x, int y, int value) {
		int tiles[] = getUsedTiles(x, y);
		if (value != 0) {
		for (int tile : tiles) {
		if (tile == value)
		return false;
		}
		}
		setTile(x, y, value);
		calculateUsedTiles();
		return true;
		}
	// ma tran chua so da nhap
	private final int[][][] used = new int[9][9][];
	// ghi ra gia tri khong the nhap tai vi tri x,y
	protected int[] getUsedTiles(int x, int y) {
	return used[x][y];
	}
	// them so da nhap vap user
	private void calculateUsedTiles() {
		for (int x = 0; x < 9; x++) {
		for (int y = 0; y < 9; y++) {
		used[x][y] = calculateUsedTiles(x, y);
		// Log.d(TAG, "used[" + x + "][" + y + "] = "
		// + toPuzzleString(used[x][y]));
		}
	}
}
	// kiem tra hang ngang hang doc neu so nao da su dung them vao
	// c sau do loai bo so 0 roi dua ra ket qua
	private int[] calculateUsedTiles(int x, int y) {
		int c[] = new int[9];
		// horizontal
		for (int i = 0; i < 9; i++) {
			if (i == y)
				continue;
			int t = getTile(x, i);
			if (t != 0)
				c[t - 1] = t;
		}
		// vertical
		for (int i = 0; i < 9; i++) {
			if (i == x)
				continue;
				int t = getTile(i, y);
				if (t != 0)
				c[t - 1] = t;
		}
		// same cell block
		int startx = (x / 3) * 3;
		int starty = (y / 3) * 3;
		for (int i = startx; i < startx + 3; i++) {
		for (int j = starty; j < starty + 3; j++) {
			if (i == x && j == y)
				continue;
			int t = getTile(i, j);
				if (t != 0)
					c[t - 1] = t;
				}
				}
				// compress
			int nused = 0;
				for (int t : c) {
					if (t != 0)
						nused++;
				}
			int c1[] = new int[nused];
				nused = 0;
				for (int t : c) {
				if (t != 0)
					c1[nused++] = t;
				}
				return c1;
				}
	// cover Matrix to array
	private int[] Matrix_toArray(int[][] matrix){
		int[] ketqua = new int[ matrix.length*matrix.length];
		int t=0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				ketqua[t] = matrix[i][j];
				t++;
			}
		}
		return ketqua;
	}
	// lấy ma trận bài toán
	private int[] getPuzzle() {
		String puz;
		
    	grid = Generator.generate(0);
    	int[][] matrix = grid.getMatrix();
    	return Generator.convert_2d_to_1d(matrix);
    	
	//	return fromPuzzleString(puz);
		}
	private int[][] getPuzzle1() {
		String puz;
		GeneratorStraighForward Generator = new GeneratorStraighForward();
    	Grid grid = new Grid();
    	grid = Generator.generate(0);
    	int[][] matrix = grid.getMatrix();
    	return matrix;
    	
	//	return fromPuzzleString(puz);
		}
	//xoa 0
	private int[][] removeZero(int[][] matrix){
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j]==0) {
					matrix[i][j]=(Integer) null;
				}
				
			}
		}
		return matrix;
	}
	// chuyển mảng số thành String
	static private String toPuzzleString(int[] puz) {
		StringBuilder buf = new StringBuilder();
		for (int element : puz) {
		buf.append(element);
		}
		return buf.toString();
		}
	// chuyển chuỗi String xang mảng số
	static protected int[] fromPuzzleString(String string) {
		int[] puz = new int[string.length()];
		for (int i = 0; i < puz.length; i++) {
		puz[i] = string.charAt(i) - '0';
		}
		return puz;
		}
	// chuyen int[] thành int[][]
	static protected int[][] conver_Puzzle(int[] puz){
		int dem=0;
		int[][] ketqua = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				ketqua[i][j]=puz[dem];
				dem++;
			}
			
		}
		return ketqua;
				
	}
	// lấy số tại tọa độ x y
	private int getTile(int x, int y) {
		return puzzle[y * 9 + x];
		}
	private int getTile_old(int[] puzzle_,int x, int y) {
		return puzzle_[y * 9 + x];
		}
	
		private void setTile(int x, int y, int value) {
		puzzle[y * 9 + x] = value;
		}
		
	protected String getTileString(int x, int y) {
		int v = getTile(x, y);
		if (v == 0)
			return "";
		else
			return String.valueOf(v);
		}
	protected String getTileString(int x) {
		if (x == 0)
			return "";
		else
			return String.valueOf(x);
		}


	  @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	            getMenuInflater().inflate(R.menu.game, menu);
	            return true;
	    }
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
        case R.id.new_game:
        	finish();
        	startActivity(getIntent());
            return true;
 
        case R.id.Solver:
        	SolverBacktracking sol =  new SolverBacktracking();
        	grid = new Grid(mypuzzle);
        	grid = sol.solve(grid);
			puzzle = Matrix_toArray(grid.getMatrix());
			mypuzzle=conver_Puzzle(puzzle);
			calculateUsedTiles();
			puzzleView = new PuzzleView(this);
			setContentView(puzzleView);
			puzzleView.requestFocus();
            return true;
 
        case R.id.Exit:
        	finish();
            return true;
 
        default:
            return super.onOptionsItemSelected(item);
        }
    }
		
	
	
}
