
package org.sudoku.complete;

import org.sudoku.complete.Grid;


public interface Solver {
	/**
	 * 
	 * @param grid: grid to be solved
	 * @return: grid that has been solved
	 * null if can not be solved
	 */
	public Grid solve(final Grid grid);

	Grid solve(int[][] matrixx); 
}
