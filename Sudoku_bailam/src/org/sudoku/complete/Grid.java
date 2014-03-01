package org.sudoku.complete;
import java.util.Arrays;


public class Grid {

	private final static int SIZE = 9;
	private int[][] matrix;
	/**
	 * 
	 */
	public Grid() {
		// TODO Auto-generated constructor stub
		matrix = new int[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			Arrays.fill(matrix[0], 0);
		}
	}
	
	/**
	 * 
	 * @param mt: 2D array
	 */
	public Grid(int[][] mt) {
		matrix = new int[SIZE][SIZE];
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				matrix[row][col] = mt[row][col];
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int[][] getMatrix() {
		if (matrix == null) {
			matrix = new int[SIZE][SIZE];
		}
		return matrix;
	}

}
