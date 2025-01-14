/*
 * ConnectFour
 *
 * This class represents a Connect Four
 * game, which allows two players to drop
 * checkers into a grid until one achieves
 * four checkers in a straight line.
 *
 * Made by Paul Peng
 *
 * Last Modified 1/14/2024
 */

import java.io.*;

public class ConnectFour {
	// declare necessary classes
	ConnectFourGUI gui;
	BufferedReader fileIn;
	BufferedWriter fileOut;

	// create self-explanatory constants
	final int ROWS = 6;
	final int COLUMNS = 7;
	final int P1 = 1;
	final int P2 = 2;
	final int EMPTY = 0;

	// create the board array and an int representing whose turn it is
	int[][] board;
	int currPlayer;

	// constructor, call start immediately
	public ConnectFour(ConnectFourGUI gui) {
		this.gui = gui;
		start();
	}

	/*
	 * void start()
	 * begins the game by creating board, resetting it, and initializing player 1's
	 * turn
	 */
	public void start() {
		board = new int[ROWS][COLUMNS];
		resetBoard();
		gui.resetGameBoard();

		currPlayer = P1;
		gui.setNextPlayer(P1);
	}

	/*
	 * int checkWin()
	 * returns int - the player that won the game. if no one won, returns -1
	 * checks if there are 4 pieces in a row by the same player on the board, AKA a
	 * win
	 */
	public int checkWin() {
		// set winner to -1 first, if someone won change it accordingly
		int winner = -1;

		/*
		 * iterate through each horizontal in each vertical until the 4th vertical line
		 * from the left
		 * at each column iteration, check it, and the 3 columns directly to the right
		 * if all of these held pieces of the same player, that player has won
		 * this checks all possible horizontal 4-in-a-rows
		 */
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS - 3; col++) {
				if (board[row][col] == P1 &&
						board[row][col + 1] == P1 &&
						board[row][col + 2] == P1 &&
						board[row][col + 3] == P1) {
					winner = P1;
				}
				if (board[row][col] == P2 &&
						board[row][col + 1] == P2 &&
						board[row][col + 2] == P2 &&
						board[row][col + 3] == P2) {
					winner = P2;
				}
			}
		}

		/*
		 * iterate through each vertical in each horizontal until the 3rd horizontal
		 * line from the top
		 * at each row iteration, check it, and the 3 rows directly down from it
		 * if all of these held pieces of the same player, that player has won
		 * this checks all possible vertical 4-in-a-rows
		 */
		for (int row = 0; row < ROWS - 3; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				if (board[row][col] == P1 &&
						board[row + 1][col] == P1 &&
						board[row + 2][col] == P1 &&
						board[row + 3][col] == P1) {
					winner = P1;
				}
				if (board[row][col] == P2 &&
						board[row + 1][col] == P2 &&
						board[row + 2][col] == P2 &&
						board[row + 3][col] == P2) {
					winner = P2;
				}
			}
		}

		/*
		 * iterate through each diagonal using a similar strategy
		 * begin on the first row, going right by each column, and check the diagonal
		 * down from there
		 * go until the 4th column and 3rd row as otherwise it would go out of bounds
		 * note that this checks all the diagonals on the board that go from TOP LEFT to
		 * BOTTOM RIGHT
		 */
		for (int row = 0; row < ROWS - 3; row++) {
			for (int col = 0; col < COLUMNS - 3; col++) {
				if (board[row][col] == P1 &&
						board[row + 1][col + 1] == P1 &&
						board[row + 2][col + 2] == P1 &&
						board[row + 3][col + 3] == P1) {
					winner = P1;
				}
				if (board[row][col] == P2 &&
						board[row + 1][col + 1] == P2 &&
						board[row + 2][col + 2] == P2 &&
						board[row + 3][col + 3] == P2) {
					winner = P2;
				}
			}
		}

		/*
		 * iterate through each diagonal using a similar strategy as the other diagonal
		 * check
		 * begin on the first row, but this time start on the last column and go left
		 * note that this checks all the diagonals on the board that go from TOP RIGHT
		 * to BOTTOM LEFT
		 */
		for (int row = 0; row < ROWS - 3; row++) {
			for (int col = COLUMNS - 1; col >= COLUMNS - 4; col--) {
				if (board[row][col] == P1 &&
						board[row + 1][col - 1] == P1 &&
						board[row + 2][col - 2] == P1 &&
						board[row + 3][col - 3] == P1) {
					winner = P1;
				}
				if (board[row][col] == P2 &&
						board[row + 1][col - 1] == P2 &&
						board[row + 2][col - 2] == P2 &&
						board[row + 3][col - 3] == P2) {
					winner = P2;
				}
			}
		}

		// return winner at the end
		return winner;
	}

	/*
	 * boolean boardFull()
	 * returns boolean - true if the board is full, false if not
	 * checks if the board is full, indicating a draw
	 */
	public boolean boardFull() {
		// create flag for fullness
		boolean full = true;

		// iterate through the board, if any slot is empty, the board is not full
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (board[i][j] == EMPTY)
					full = false;
			}
		}

		return full;
	}

	/*
	 * void play(int column)
	 * int column - the column that the player placed the piece onto
	 * the central method of the program. runs when a player clicks to place a piece
	 */
	public void play(int column) {
		// create flag for if a piece was successfully placed
		boolean placed = false;

		// check each slot on the column the player clicked on bottom to top, placing
		// the piece at the first empty slot
		for (int i = ROWS - 1; i >= 0; i--) {
			if (board[i][column] == EMPTY && !placed) {
				board[i][column] = currPlayer;
				gui.setPiece(i, column, currPlayer);
				placed = true;
			}
		}

		// check if someone won, if they did, show winner message and reset the game
		// after
		if (checkWin() == P1) {
			gui.showWinnerMessage(P1);
			start();
		} else if (checkWin() == P2) {
			gui.showWinnerMessage(P2);
			start();
			// if the board is full, show tie message and reset the game after
		} else if (boardFull()) {
			gui.showTieGameMessage();
			start();
			// otherwise, continue the game as usual
		} else {
			// if a piece was successfully placed, alternate turns
			if (placed) {
				if (currPlayer == P1) {
					currPlayer = P2;
				} else {
					currPlayer = P1;
				}
				gui.setNextPlayer(currPlayer);
			}
		}
	}

	/*
	 * void resetBoard()
	 * sets all the slots on the board to empty, also resets player turn
	 */
	public void resetBoard() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				board[i][j] = EMPTY;
			}
		}

		currPlayer = P1;
		gui.setNextPlayer(P1);
	}

	/*
	 * boolean saveToFile
	 * returns boolean - true if the file was successfully saved, false if not
	 * String file - the name of the file to save to
	 * saves the current board to a file
	 */
	public boolean saveToFile(String file) {
		// the success flag
		boolean success = false;

		try {
			fileOut = new BufferedWriter(new FileWriter(file));

			// write each integer on the board to the file
			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLUMNS; j++) {
					fileOut.write(board[i][j] + ""); // make sure to convert to String
				}
			}

			// make sure to save the current player turn
			fileOut.write(currPlayer + "");

			success = true;
			fileOut.close();
		} catch (IOException e) {
			System.out.println("IO Exception occured: " + e);
		}

		return success;
	}

	/*
	 * boolean loadFromFile
	 * returns boolean - true if the file was successfully loaded, false if not
	 * String file - the name of the file to load
	 * loads a board from a saved board file
	 */
	public boolean loadFromFile(String file) {
		// the success flag
		boolean success = false;

		try {
			fileIn = new BufferedReader(new FileReader(file));

			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLUMNS; j++) {
					// since 0's ascii code is 48, subtracting 48 from each read ascii code will
					// give the actual integer
					board[i][j] = fileIn.read() - 48;
				}
			}

			// make sure to read the current player turn and set it accordingly
			currPlayer = fileIn.read() - 48;
			gui.setNextPlayer(currPlayer);

			success = true;
			fileIn.close();
		} catch (IOException e) {
			System.out.println("IO Exception occured: " + e);
		}

		return success;
	}

	/*
	 * void updateGameBoard()
	 * updates the GUI board to match the internal board array
	 */
	public void updateGameBoard() {
		// iterate through the board array, if a slot is not empty, set the GUI slot to
		// that piece
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (board[i][j] != EMPTY) {
					gui.setPiece(i, j, board[i][j]);
				}
			}
		}
	}
}
