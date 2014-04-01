package org.example.Sudoku;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class Game6 extends Activity {
	private PuzzleView6 puzzleView;
	public static final String KEY_DIFFICULTY = "difficulty";
	protected static final String TAG = "sudoku";
	public DataBaseHelper dbManager =  new DataBaseHelper(this);
	public static int diff=1;
	private int[] puzzle = new int[6*6] ; 
	public int[][] mypuzzle = new int[6][6];
	private  ProgressDialog pd;
	 public  Handler finishedHandler = new Handler() {
	      @Override public void handleMessage(Message msg) {
	          pd.dismiss();
	          calculateUsedTiles();
	          puzzleView = new PuzzleView6(Game6.this);
	  		setContentView(puzzleView);
	  		puzzleView.requestFocus();
	      }
	  };
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		diff = getIntent().getIntExtra(KEY_DIFFICULTY, 1);
		pd = ProgressDialog.show(Game6.this, "Đang khởi tạo", "Đang khởi tạo ma trân sudoku", true, false);
        new Thread(new Runnable() {
        	

				public void run() {
					Log.d(TAG, "onCreate");
					
					//puzzle = getPuzzle();
					puzzle = dbManager.getPuzzle("puzzle36",diff);
					mypuzzle=conver_Puzzle(puzzle);
					finishedHandler.sendEmptyMessage(0);
                }
            }).start();
            
        calculateUsedTiles();
        puzzleView = new PuzzleView6(this);
		setContentView(puzzleView);
		puzzleView.requestFocus();

		
	}
	// chuyen int[] thành int[][]
		static protected int[][] conver_Puzzle(int[] puz){
			int dem=0;
			int[][] ketqua = new int[6][6];
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 6; j++) {
					ketqua[i][j]=puz[dem];
					dem++;
				}
				
			}
			return ketqua;
					
		}
	// ma tran chua so da nhap
	private final int[][][] used = new int[6][6][];
	// ghi ra gia tri khong the nhap tai vi tri x,y
	protected int[] getUsedTiles(int x, int y) {
	return used[x][y];
	}
	// them so da nhap vap user
	private void calculateUsedTiles() {
		for (int x = 0; x < 6; x++) {
		for (int y = 0; y < 6; y++) {
		used[x][y] = calculateUsedTiles(x, y);
		}
	}
}
	// kiem tra hang ngang hang doc neu so nao da su dung them vao
		// c sau do loai bo so 0 roi dua ra ket qua
		private int[] calculateUsedTiles(int x, int y) {
			int c[] = new int[6];
			// horizontal
			for (int i = 0; i < 6; i++) {
				if (i == y)
					continue;
				int t = getTile(x, i);
				if (t != 0)
					c[t - 1] = t;
			}
			// vertical
			for (int i = 0; i < 6; i++) {
				if (i == x)
					continue;
					int t = getTile(i, y);
					if (t != 0)
					c[t - 1] = t;
			}
			// same cell block
			int startx = (x / 3) * 3;
			int starty = (y / 2) * 2;
			for (int i = startx; i < startx + 3; i++) {
			for (int j = starty; j < starty + 2; j++) {
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
		// chuyển mảng số thành String
		static private String toPuzzleString(int[] puz) {
			StringBuilder buf = new StringBuilder();
			for (int element : puz) {
			buf.append(element);
			}
			return buf.toString();
			}
		 private boolean getHints(){
			 SharedPreferences pre =  PreferenceManager.getDefaultSharedPreferences(this);
			 return pre.getBoolean("hints", true);
			 
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
	// lay gia tri so tai vi tri x,y
	protected String getTileString(int x, int y) {
		int v = getTile(x, y);
		if (v == 0)
			return "";
		else
			return String.valueOf(v);
	}

	private int getTile(int x, int y) {
		return puzzle[y*6+x];
	}
	private void setTile(int x, int y, int value) {
		puzzle[y * 6 + x] = value;
		}
	
	protected String getTileString(int x) {
		if (x == 0)
			return "";
		else
			return String.valueOf(x);
		}
	
	protected String getTileString1(int x, int y) {
		int v = getTile(x, y);
		if (v == 0)
			return "";
		else
			return String.valueOf(v);
		}

}
