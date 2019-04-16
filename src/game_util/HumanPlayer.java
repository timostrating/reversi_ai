package game_util;

import tic_tac_toe.TicTacToe;

import java.util.Scanner;

/**
 * Player that plays by providing numbers in the console
 */
public class HumanPlayer extends Player {
    Scanner scanner;
    GameRules game;

    public HumanPlayer(GameRules game) {
        scanner = new Scanner(System.in);
        this.game = game;
    }

    @Override
    public Move getInput() {
        System.out.println("Player " + getNr() + ": type in a number between 1 to "+ ((game instanceof TicTacToe)? "9" : "64") +" as your input:");
        return game.getMove(scanner.nextInt() - 1, getNr());
    }
}
