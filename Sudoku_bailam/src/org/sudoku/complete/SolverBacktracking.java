
package org.sudoku.complete;

import java.util.ArrayList;
import java.util.List;

import org.sudoku.complete.Grid;


public class SolverBacktracking implements Solver {
	private final static int SIZE = 9;
	private int[][] matrix;
	private List<int[][]> results = null;
	private int lastK;
	/**
	 * 
	 */
	public SolverBacktracking() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see control.Solver#solve(model.Grid)
	 */
	@Override
	public Grid solve(int[][] matrixx) {
		matrix = matrixx;
		lastK = getLastK();
		results = new ArrayList<int[][]>();
		trySearch(0);
		if (results.size() > 0) {
			return (new Grid(results.get(0)));

		} else {
			System.out.println("Khong co ket qua...");
			return null;
		}
	}

	@Override
	public Grid solve(final Grid grid) {
		matrix = grid.getMatrix();
		lastK = getLastK();
		results = new ArrayList<int[][]>();
		trySearch(0);
		if (results.size() > 0) {
			return (new Grid(results.get(0)));

		} else {
			System.out.println("Khong co ket qua...");
			return null;
		}
	}

	private void trySearch(int k) {
		// loai bo cac o de bai
		while (k < SIZE * SIZE && matrix[k / SIZE][k % SIZE] != 0) {
			k++;
		}

		if (k == SIZE * SIZE)
			return;

		int row = k / SIZE, col = k % SIZE;

		for (int x = 1; x <= SIZE; x++) { // duyet cac TH
			if (isOK(row, col, x)) {
				matrix[row][col] = x;
				if (k == lastK) {
					int[][] rs = new int[SIZE][SIZE];
					// copy ket qua sang 1 array khac
					for (int i = 0; i < SIZE; i++) {
						for (int j = 0; j < SIZE; j++) {
							rs[i][j] = matrix[i][j];
						}
					}
					results.add(rs); // them vao list
				} else {
					trySearch(k + 1);
				}
				matrix[row][col] = 0;
			}
		}
	}

	private boolean isOK(int row, int col, int x) {

		for (int i = 0; i < SIZE; i++) {
			if (matrix[row][i] == x || // kiem tra hang
					matrix[i][col] == x) { // kiem tra cot
				return false;
			}
		}

		for (int i = row - row % 3; i < row - row % 3 + 3; i++) {
			for (int j = col - col % 3; j < col - col % 3 + 3; j++) {
				if (matrix[i][j] == x) {
					return false;
				}
			}
		}
		return true;
	}

	private int getLastK() {
		for (int row = SIZE - 1; row >= 0; row--) {
			for (int col = SIZE - 1; col >= 0; col--) {
				if (matrix[row][col] == 0) {
					return (row * SIZE + col);
				}
			}
		}
		return 0;
	}

}
