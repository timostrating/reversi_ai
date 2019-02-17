package tic_tac_toe;

import tic_tac_toe.TicTacToeBoard.GameState;

import java.util.Scanner;

public class TicTacToeEngine {

    public static void main(String[] args) {
        TicTacToeBoard board = new TicTacToeBoard();
        Scanner scanner = new Scanner(System.in);

        int playerNr = 1;

        while (true) {
            while (true) {
                System.out.println(board.toString());
                System.out.println("Player "+playerNr+":");
                boolean valid = board.playMove(scanner.nextInt(), scanner.nextInt(), playerNr);

                if (!valid) {
                    System.out.println("Not a valid move please try again");
                    continue;
                }

                GameState state = board.getGameState();
                if (state == GameState.PLAYER_1_WINS) {
                    System.out.println("Player 1 has won");
                    break;
                } else if (state == GameState.PLAYER_2_WINS) {
                    System.out.println("Player 2 has won");
                    break;
                } else if (state == GameState.DRAW) {
                    System.out.println("DRAW");
                    break;
                } else {
                    playerNr = playerNr % 2 + 1;
                }
            }

            System.out.println("\n\nPress any key+enter to play another game");
            scanner.next();
            board.reset();
        }
    }
}
