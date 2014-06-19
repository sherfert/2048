package p2048;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The logic for the 2048 game. A set of static methods.
 * 
 * @author Satia
 */
public class Logic2048 {

	/**
	 * @return a random initial game configuration.
	 */
	public static int[][] newGame() {
		// make list with (NUMBER_OF_TILES_IN_A_ROW^2 - 2) x 0 and 2 x 2.
		List<Integer> numbers = new ArrayList<Integer>();
		// Add the 0s
		final int numZeros = MainPanel.NUMBER_OF_TILES_IN_A_ROW
				* MainPanel.NUMBER_OF_TILES_IN_A_ROW - 2;
		for (int i = 0; i < numZeros; i++) {
			numbers.add(0);
		}
		// And (2) 2s
		numbers.add(2);
		numbers.add(2);
		// shuffle
		Collections.shuffle(numbers);
		// put list content into array
		int[][] result = new int[MainPanel.NUMBER_OF_TILES_IN_A_ROW][MainPanel.NUMBER_OF_TILES_IN_A_ROW];
		for (int i = 0; i < MainPanel.NUMBER_OF_TILES_IN_A_ROW; i++) {
			for (int j = 0; j < MainPanel.NUMBER_OF_TILES_IN_A_ROW; j++) {
				result[i][j] = numbers.get(i
						* MainPanel.NUMBER_OF_TILES_IN_A_ROW + j);
			}
		}
		return result;
	}

	/**
	 * The game is over, when there are no 0s and no two same numbers touch
	 * anywhere.
	 * 
	 * @param current
	 *            the configuration
	 * @return if the game is over
	 */
	public static boolean isGameOver(int[][] current) {
		for (int i = 0; i < MainPanel.NUMBER_OF_TILES_IN_A_ROW; i++) {
			for (int j = 0; j < MainPanel.NUMBER_OF_TILES_IN_A_ROW; j++) {
				// Check for 0s
				if (current[i][j] == 0) {
					return false;
				}
				// Check for each position if right or below is the same number
				if ((i != MainPanel.NUMBER_OF_TILES_IN_A_ROW - 1 && current[i + 1][j] == current[i][j])
						|| (j != MainPanel.NUMBER_OF_TILES_IN_A_ROW - 1 && current[i][j + 1] == current[i][j])) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * The right arrow was pressed. The parameter will be changed accordingly.
	 * 
	 * @param current
	 *            the current configuration
	 * @return the score bonus
	 */
	public static int rightArrowPressed(int[][] current) {
		synchronized (current) {
			// Swap horizontally
			ArrayUtil.swapHorizontally(current);
			// Use leftArrowPressed
			int result = leftArrowPressed0(current);
			// Swap back
			ArrayUtil.swapHorizontally(current);
			return result;
		}
	}

	/**
	 * The up arrow was pressed. The parameter will be changed accordingly.
	 * 
	 * @param current
	 *            the current configuration
	 * @return the score bonus
	 */
	public static int upArrowPressed(int[][] current) {
		synchronized (current) {
			// Swap diagonally
			ArrayUtil.swapDiagonally(current);
			// Use leftArrowPressed
			int result = leftArrowPressed0(current);
			// Swap back
			ArrayUtil.swapDiagonally(current);
			return result;
		}
	}

	/**
	 * The down arrow was pressed. The parameter will be changed accordingly.
	 * 
	 * @param current
	 *            the current configuration
	 * @return the score bonus
	 */
	public static int downArrowPressed(int[][] current) {
		synchronized (current) {
			// Swap diagonally and horizontally
			ArrayUtil.swapDiagonally(current);
			ArrayUtil.swapHorizontally(current);
			// Use leftArrowPressed
			int result = leftArrowPressed0(current);
			// Swap back
			ArrayUtil.swapHorizontally(current);
			ArrayUtil.swapDiagonally(current);
			return result;
		}
	}

	/**
	 * The left arrow was pressed. The parameter will be changed accordingly.
	 * 
	 * @param current
	 *            the current configuration
	 * @return the score bonus
	 */
	public static int leftArrowPressed(int[][] current) {
		synchronized (current) {
			return leftArrowPressed0(current);
		}
	}

	/**
	 * @param current
	 *            the current configuration
	 * @return the score bonus
	 */
	private static int leftArrowPressed0(int[][] current) {
		boolean changed = false;
		int score = 0;

		// Go row for row
		for (int i = 0; i < MainPanel.NUMBER_OF_TILES_IN_A_ROW; i++) {
			// First deal with 0s
			for (int j = 0; j < MainPanel.NUMBER_OF_TILES_IN_A_ROW; j++) {
				// If only 0s remain, we can continue with the next row
				boolean cont = true;
				for (int k = j; k < MainPanel.NUMBER_OF_TILES_IN_A_ROW; k++) {
					if (current[i][k] != 0) {
						cont = false;
						break;
					}
				}
				if (cont) {
					break;
				}

				// while it is a zero, move the remaining left
				while (current[i][j] == 0) {
					// Move left
					changed = true;
					moveLeft(current[i], j);
				}
			}

			// Now deal with touching numbers, except 0s
			for (int j = 0; j < MainPanel.NUMBER_OF_TILES_IN_A_ROW; j++) {
				if (current[i][j] == 0) {
					continue;
				}
				// if the two touching numbers are the same, merge them
				if (j != MainPanel.NUMBER_OF_TILES_IN_A_ROW - 1
						&& current[i][j] == current[i][j + 1]) {
					changed = true;
					current[i][j] *= 2;
					score += current[i][j];
					// Move the rest left again
					moveLeft(current[i], j + 1);
				}
			}
		}

		// Finally, if sth. changed, spawn in 2 in a random 0's position if
		// there is any.
		if (changed) {
			// collect all 0 positions
			List<Integer> zeros = new ArrayList<Integer>(
					MainPanel.NUMBER_OF_TILES_IN_A_ROW
							* MainPanel.NUMBER_OF_TILES_IN_A_ROW);
			for (int i = 0; i < MainPanel.NUMBER_OF_TILES_IN_A_ROW; i++) {
				for (int j = 0; j < MainPanel.NUMBER_OF_TILES_IN_A_ROW; j++) {
					if (current[i][j] == 0) {
						// Save position
						zeros.add(i * MainPanel.NUMBER_OF_TILES_IN_A_ROW + j);
					}
				}
			}
			// Select one randomly
			int listPos = (int) (Math.random() * zeros.size());
			// Spawn the two
			current[zeros.get(listPos) / MainPanel.NUMBER_OF_TILES_IN_A_ROW][zeros
					.get(listPos) % MainPanel.NUMBER_OF_TILES_IN_A_ROW] = 2;
		}

		return score;
	}

	/**
	 * Moves the remainder of a row left.
	 * 
	 * @param row
	 *            the row
	 * @param fromPos
	 *            from which position to start, inclusive
	 */
	private static void moveLeft(int[] row, int fromPos) {
		for (int i = fromPos; i < row.length - 1; i++) {
			row[i] = row[i + 1];
		}
		// 0 in the last position
		row[row.length - 1] = 0;
	}
}
