
/*****************************************************************************
 * File: MatrixFill.java
 * Author: Keith Schwarz (htiek@cs.stanford.edu)
 *
 * An algorithm that solves a creative Microsoft job interview question.
 *
 * The question in this problem is the following: you are given a boolean
 * matrix of dimensions m x n. You want to transform the matrix to produce a
 * new matrix according to the following rule: entry (i, j) should be true
 * (we'll denote it '1') if there was a 1 anywhere on row i or column j, and
 * should be 0 otherwise. For example, given this input matrix:
 *
 *                                0 0 0 1 0
 *                                1 0 0 0 0
 *                                0 0 0 0 0
 *                                0 0 0 1 0
 *
 * the output would be
 *
 *                                1 1 1 1 1
 *                                1 1 1 1 1
 *                                1 0 0 1 0
 *                                1 1 1 1 1
 *
 * Similarly, given matrix
 *
 *                                  0 0 0
 *                                  0 1 0
 *                                  0 0 0
 *
 * The output would be
 *
 *                                  0 1 0
 *                                  1 1 1
 *                                  0 1 0
 *
 * The challenge is the following: is it possible to do this in time O(mn)
 * and space only O(1)? That is, can you solve this problem in constant space
 * and linear time? Amazingly, the answer is yes!
 *
 * The initial problem with trying to solve this in O(1) space is that if we
 * start changing entries in the matrix from 0 to 1 as we begin filling in the
 * matrix, we might end up confusing ourselves later between the case where the
 * element was originally 1 (meaning we should fill its row and column with 1s)
 * and the case where the element was originally 0 (meaning that we shouldn't.)
 * I recommend trying to work through this problem before reading on - it's a
 * great challenge
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * To motivate the correct solution, let's begin with a solution that uses
 * O(n) space rather than O(1) space.
 *
 * If you think about it, for each row and each column, we only need to store
 * one bit of information: should that column be filled or not? Therefore, one
 * simple solution would be the following:
 *
 * 1. Create an array of size m storing which rows contain 1s. This can be
 *    filled in in time O(m) and uses O(m) space.
 * 2. Create an array of size n storing which columns contain 1s. This can be
 *    filled in time O(n) and uses O(n) space.
 * 3. For each (i, j), set position (i, j) to 1 if entry i in the row array or
 *    entry j in the column array is a 1. This also takes only O(mn) time.
 *
 * This overall approach will use time O(mn), but needs space O(m + n), which
 * is too much for what we're trying to do.
 *
 * However, we can reduce the space usage down to O(n) by using a clever
 * observation. Start as before by filling in an array that marks which
 * columns contain 1s. However, instead of creating a secondary array to store
 * this information for rows, instead just iterate across each row, filling it
 * with 1s if any of its entries are 1. After doing this, use the auxiliary
 * array with information about the marked columns to fill in the columns that
 * contain 1s. Why does this work? Well, if an element is a 0 in the result,
 * it means that there were no 1s in its row and no 1s in its column, so it
 * should indeed be a 0. If the element is a 1, then there was either a 1 in
 * its row or a 1 in its column, so it should indeed be a 1.
 *
 * As an example, let's trace this algorithm on this input array:
 *
 *                                0 0 0 1 0
 *                                1 0 0 0 0
 *                                0 0 0 0 0
 *                                0 0 0 1 0
 *
 * We start off by making an auxiliary array for the columns and filling it in:
 *
 *                                0 0 0 1 0
 *                                1 0 0 0 0
 *                                0 0 0 0 0
 *                                0 0 0 1 0
 *
 *                                1 0 0 1 0 (aux array)
 *
 * Next, we fill in each row containing a 1:
 *
 *                                1 1 1 1 1
 *                                1 1 1 1 1
 *                                0 0 0 0 0
 *                                1 1 1 1 1
 *
 *                                1 0 0 1 0 (aux array)
 *
 * Finally, we fill each column with a 1 in the aux array:
 *
 *                                1 1 1 1 1
 *                                1 1 1 1 1
 *                                1 0 0 1 0
 *                                1 1 1 1 1
 *
 *                                1 0 0 1 0 (aux array)
 *
 * and we're done!
 *
 * This approach is more memory-efficient than before, but it's still using
 * too much memory. We need to get it down to O(1) memory, not O(n).
 *
 * This is where we can use a really clever trick. Let's begin with an
 * observation: if every row in the matrix contains a 1, then the resulting
 * matrix will all be 1's. Therefore, we can start off our search by checking
 * if all the rows have a 1 in them and, if so, filling in the entire matrix.
 *
 * If, however, some row is all zeros, we know that in the "fill in each row
 * that contains a 1" step, the row won't be touched. In fact, the only way
 * that any entries on this row will get set to 1 is if those entries are in
 * columns that contain 1s. But wait - that sounds a lot like our auxiliary
 * array, which should only have 1s in columns containing 1s! This leads us to
 * the most important insight of the algorithm: we can treat one of the rows of
 * zeros as our auxiliary array, meaning that we don't need to allocate a new
 * auxiliary array!
 *
 * The new algorithm is pretty much the same as before, but with the auxiliary
 * array packed into the matrix itself. Let's do an example:
 *
 *                                1 0 0 1 0 1
 *                                0 0 0 0 0 0
 *                                1 0 0 0 0 0
 *                                0 0 0 0 0 0
 *                                0 0 0 1 0 1
 *
 * We start by scanning to find a row of all 0s to use as our auxiliary array.
 * This is shown here:
 *
 *                                1 0 0 1 0 1
 *                                0 0 0 0 0 0 -- (aux)
 *                                1 0 0 0 0 0
 *                                0 0 0 0 0 0
 *                                0 0 0 1 0 1
 *
 * Now, we fill in the auxiliary array, as before:
 *
 *                                1 0 0 1 0 1
 *                                1 0 0 1 0 1 -- (aux)
 *                                1 0 0 0 0 0
 *                                0 0 0 0 0 0
 *                                0 0 0 1 0 1
 *
 * Next, we fill in each row in the matrix containing at least one 1, *except*
 * for the auxiliary array row (after all, it's really all 0's. We're just
 * appropriating the bits for other purposes). This is shown here:
 *
 *                                1 1 1 1 1 1
 *                                1 0 0 1 0 1 -- (aux)
 *                                1 1 1 1 1 1
 *                                0 0 0 0 0 0
 *                                1 1 1 1 1 1
 *
 * Finally, using the auxiliary array, we fill in each column that contained a
 * 1 in the initial array:
 *
 *                                1 1 1 1 1 1
 *                                1 0 0 1 0 1 -- (aux)
 *                                1 1 1 1 1 1
 *                                1 0 0 1 0 1
 *                                1 1 1 1 1 1
 *
 * And we're done! This whole process takes time O(mn) because we're making a
 * constant number of passes over the original matrix and only uses O(1) space
 * (enough to hold the index of the auxiliary array and incidental loop
 * variables).
 */
public class MatrixFill {
    /* This class isn't meant to be instantiated. */
    private MatrixFill() {
        // Do nothing
    }

    /**
     * Given a boolean matrix of 0s and 1s, transforms the grid by setting each
     * entry that was in the same row or column as a 1 into a 1. The
     * algorithm used here uses only O(1) space and runs in linear time.
     *
     * @param matrix The matrix, which is modified by the algorithm.
     */
    public static void fill(boolean[][] matrix) {
        /* Start by finding a row to use as an auxiliary array. If one can't
         * be found, that's great! The whole matrix should be 1s and we're
         * done. Note that if the matrix has no rows, there will be no aux row.
         */
        int auxRow = findAuxiliaryRow(matrix);
        if (auxRow == -1) {
            fillMatrixWithOnes(matrix);
            return;
        }

        /* Now, populate the auxiliary array by scanning through the columns
         * and marking which ones have 1s in them.
         */
        populateAuxiliaryRow(matrix, auxRow);

        /* Next, fill in each row (except the auxiliary row) that contains a 1
         * with 1s.
         */
        fillRowsWithOnes(matrix, auxRow);

        /* Finally, fill in each column that originally contained a 1 with
         * 1s.
         */
        fillColumnsWithOnes(matrix, auxRow);
    }

    /**
     * Given a boolean matrix, finds a row of all 0s in that matrix, returning
     * -1 as a sentinel if one is not found. This algorithm runs in time O(mn) and
     * uses only O(1) space.
     *
     * @param matrix The matrix in question.
     * @return The index of a row of all 0s, or -1 if none exists.
     */
    private static int findAuxiliaryRow(boolean[][] matrix) {
        for (int row = 0; row < matrix.length; row++) {
            if (isZeroRow(matrix, row)) return row;
        }
        return -1;
    }

    /**
     * Given a boolean matrix and a row in that matrix, returns whether every
     * entry in that row is 0. This method runs in O(n) time and uses O(1) space.
     *
     * @param matrix The matrix in question.
     * @param row The row index.
     * @return Whether every entry in that row is 0.
     */
    private static boolean isZeroRow(boolean[][] matrix, int row) {
        for (int col = 0; col < matrix[row].length; col++) {
            if (matrix[row][col]) return false;
        }
        return true;
    }

    /**
     * Given a boolean matrix and a column in that matrix, returns whether every
     * entry in that column is 0. This method runs in O(m) time and uses O(1) 
     * space.
     *
     * @param matrix The matrix in question.
     * @param col The column index.
     * @return Whether every entry in that column is 0.
     */
    private static boolean isZeroColumn(boolean[][] matrix, int col) {
        for (int row = 0; row < matrix.length; row++) {
            if (matrix[row][col]) return false;
        }
        return true;
    }

    /**
     * Fills a boolean matrix with the value 1's (true's). This method runs in
     * O(mn) time and uses O(1) space.
     *
     * @param matrix The matrix to fill with 1's.
     */
    private static void fillMatrixWithOnes(boolean[][] matrix) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                matrix[row][col] = true;
            }
        }
    }

    /**
     * Given a matrix and an auxiliary row (a row of all 0's), fills in the
     * auxiliary row with information about which columns contain 1's. This method
     * runs in time O(mn) and uses O(1) space.
     *
     * @param matrix The matrix in question.
     * @param auxRow The row number of the auxiliary row.
     */
    private static void populateAuxiliaryRow(boolean[][] matrix, int auxRow) {
        for (int col = 0; col < matrix[auxRow].length; col++) {
            if (!isZeroColumn(matrix, col)) matrix[auxRow][col] = true;
        }
    }

    /**
     * Given a matrix, fills each row in the matrix (except for the auxiliary row)
     * with 1's if any of the entries in that row are 1's. This methods runs in
     * time O(mn) and uses O(1) space.
     *
     * @param matrix The matrix to process.
     * @param auxRow The index of the auxiliary row.
     */
    private static void fillRowsWithOnes(boolean[][] matrix, int auxRow) {
        for (int row = 0; row < matrix.length; row++) {
            /* Skip the auxiliary row; the 1's in it are spurious. */
            if (row == auxRow) continue;

            if (!isZeroRow(matrix, row)) fillRow(matrix, row);
        }
    }

    /**
     * Given a matrix and a row in the matrix, fills that row of the matrix with
     * 1's. This method runs im time O(n) and uses O(1) space.
     *
     * @param matrix The matrix to process.
     * @param row The row number.
     */
    private static void fillRow(boolean[][] matrix, int row) {
        for (int col = 0; col < matrix[row].length; col++) {
            matrix[row][col] = true;
        }
    }

    /**
     * Given a matrix and a column in the matrix, fills that column of the matrix
     * with 1's. This method runs im time O(m) and uses O(1) space.
     *
     * @param matrix The matrix to process.
     * @param col The column number.
     */
    private static void fillColumn(boolean[][] matrix, int col) {
        for (int row = 0; row < matrix.length; row++) {
            matrix[row][col] = true;
        }
    }

    /**
     * Given a matrix and the row number of the auxiliary row, fills in every
     * column in the matrix with a 1 in the auxiliary row with 1's.
     *
     * @param matrix The matrix to process.
     * @param auxRow The index of the auxiliary row.
     */
    private static void fillColumnsWithOnes(boolean[][] matrix, int auxRow) {
        for (int col = 0; col < matrix[auxRow].length; col++) {
            if (matrix[auxRow][col]) fillColumn(matrix, col);
        }
    }
}
