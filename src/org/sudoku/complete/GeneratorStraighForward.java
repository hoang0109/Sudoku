package org.sudoku.complete;

import java.util.ArrayList;


public class GeneratorStraighForward implements Generator {

	private final static int SIZE = 9;
	private int[][] matrix;
	private ArrayList<Grid> results = null;
	private int lastK;

	@Override
	public Grid generate(int level) {
		return makeGrid();
	}

	private Grid makeGrid() {
		Grid answer = solve(new Grid(), 1).get(0);

		int[][] answerMatrix = answer.getMatrix();
		int[] takeAway = initRandom(SIZE * SIZE);

		for (int k : takeAway) {
			k--;
			int row = k / SIZE, col = k % SIZE;
			int temp = answerMatrix[row][col];

			answerMatrix[row][col] = 0;

			int resultCount = solve(new Grid(answerMatrix), 2).size();
			if (resultCount != 1) {
				answerMatrix[row][col] = temp;
			}

		}

		return new Grid(answerMatrix);
	}
	public ArrayList<Grid> solve(final Grid grid, int needCount) {
		matrix = grid.getMatrix();
		lastK = getLastK();
		results = new ArrayList<Grid>();
		trySearch(0, needCount);
		if (results.size() > 0) {
			return (results);

		} else {
			System.out.println("Khong co ket qua...");
			return null;
		}
	}
	private void trySearch(int k, int needCount) {
		// loai bo cac o de bai
		while (k < SIZE * SIZE && matrix[k / SIZE][k % SIZE] != 0) {
			
			k++;
			
		}

		if (k == SIZE * SIZE)
			return;

		int row = k / SIZE, col = k % SIZE;

		int[] random = initRandom(SIZE);

		for (int x : random) { // duyet cac TH
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
					results.add(new Grid(rs)); // them vao list
				} else {
					if (needCount != 0 && (results.size() == needCount))
						break;
					else
						trySearch(k + 1, needCount);
				}
				matrix[row][col] = 0;
			}
		}
	}

	private int[] initRandom(int size) {
		int[] random = new int[size];

		for (int i = 1; i <= size; i++) {
			random[i - 1] = i;
		}

		for (int i = size; i > 0; i--) {
			int index = (int) (Math.random() * size);
			int temp = random[i - 1];
			random[i - 1] = random[index];
			random[index] = temp;
		}

		return random;
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
	public int[] convert_2d_to_1d(int[][] matrix){
		int[] matrix_1d = new int[matrix.length*matrix.length];
		int count=0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix_1d[count]=matrix[i][j];
				count++;
			}
		}
	
	return matrix_1d;
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