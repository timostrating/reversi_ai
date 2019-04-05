package game_util;

import java.util.Scanner;

public class HumanPlayer extends Player {
    Scanner scanner;

    public HumanPlayer() {
        scanner = new Scanner(System.in);
    }

    @Override
    public int getInput() {
        System.out.println("Player "+getNr()+": type in a number between 1 to 9 as your input:");
        return scanner.nextInt()-1;
    }
}
