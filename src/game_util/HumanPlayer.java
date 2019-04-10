package game_util;

import java.util.Scanner;

public class HumanPlayer extends Player {
    Scanner scanner;
    GameRules game;

    public HumanPlayer(GameRules game) {
        scanner = new Scanner(System.in);
        this.game = game;
    }

    @Override
    public Move getInput() {
        System.out.println("Player " + getNr() + ": type in a number between 1 to 9 as your input:");
        return game.getMove(scanner.nextInt() - 1, getNr());
    }
}
