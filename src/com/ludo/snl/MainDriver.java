package com.ludo.snl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.ludo.snl.models.*;
public class MainDriver {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter no. of Snakes");
		int noOfSnakes = scanner.nextInt();
		List<Snake> snakes = new ArrayList<Snake>();
		System.out.println("Enter start and end pos. of all Snakes");
        for(int i=0;i<noOfSnakes;i++) {
	       snakes.add(new Snake(scanner.nextInt(), scanner.nextInt()));
         }
        System.out.println("Enter no. of Ladders");
        int noOfLadders = scanner.nextInt();
        List<Ladder> ladders = new ArrayList<Ladder>();
        System.out.println("Enter start and end pos. of all Ladders");
        for(int i=0;i<noOfLadders;i++) {
        	ladders.add(new Ladder(scanner.nextInt(), scanner.nextInt()));
        }
        System.out.println("Enter no. of players:-");
        int noOfPlayers = scanner.nextInt();
        System.out.println("Enter name of Players:-");
        List<Player> players = new ArrayList<Player>();
        for(int i=0;i<noOfPlayers;i++) {
        	players.add(new Player(scanner.next()));
        }
        
        SnakeAndLadderService snakeAndLadderService = new SnakeAndLadderService();
        snakeAndLadderService.setPlayers(players);
        snakeAndLadderService.setSnakes(snakes);
        snakeAndLadderService.setLadders(ladders);
        
        snakeAndLadderService.startGame();
	}

}
