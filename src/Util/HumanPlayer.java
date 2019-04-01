package Util;

import java.util.Scanner;

public class HumanPlayer extends Player {
    int nr;
    Scanner scanner;

    public HumanPlayer() {
        scanner = new Scanner(System.in);
    }

    @Override
    public int getInput() {
        System.out.println("Player "+nr+": type in a number between 1 to 9 as your input:");
        return scanner.nextInt()-1;
    }
}
