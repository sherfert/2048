package p2048;

/**
 * A set of static methods to handle two-dimensional arrays in the way
 * necessary.
 * 
 * @author Satia
 */
public class ArrayUtil {

	/**
	 * Swaps the positions horizontally.
	 * 
	 * @param input
	 *            the array to modify
	 */
	public static void swapHorizontally(int[][] input) {
		for (int i = 0; i < input.length; i++) {
			// In each row only go to the middle
			for (int j = 0; j < input[i].length / 2; j++) {
				int swap = input[i][j];
				input[i][j] = input[i][input[i].length - 1 - j];
				input[i][input[i].length - 1 - j] = swap;
			}
		}
	}

	/**
	 * Swaps the positions diagonally.
	 * 
	 * @param input
	 *            the array to modify
	 */
	public static void swapDiagonally(int[][] input) {
		for (int i = 0; i < input.length; i++) {
			// In each row only go up to i
			for (int j = 0; j < i; j++) {
				int swap = input[i][j];
				input[i][j] = input[j][i];
				input[j][i] = swap;
			}
		}
	}
}
