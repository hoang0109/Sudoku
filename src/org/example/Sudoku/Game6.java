package org.example.Sudoku;

import android.app.Activity;
import android.os.Bundle;

public class Game6 extends Activity {
	private PuzzleView6 puzzleView;
	private int[][] puzzle = new int[6][6] ; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getPuzzle();
		puzzleView = new PuzzleView6(this);
		setContentView(puzzleView);
		puzzleView.requestFocus();
		
	}
	private void getPuzzle(){
		for (int i = 0; i < puzzle.length; i++) {
			for (int j = 0; j < puzzle.length; j++) {
				puzzle[i][j]=1;
			}
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
		return puzzle[x][y];
	}
}
