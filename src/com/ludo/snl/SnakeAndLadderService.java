package com.ludo.snl;

import com.ludo.snl.models.*;
import java.util.*;
public class SnakeAndLadderService {

	private SnakeAndLadderBoard snakeAndLadderBoard;
	private int initialNumberOfPlayers;
	//keeping players in game service as they are specific to this game and not the board
	//keeping pieces in the board instead
	private Queue<Player> players;
	private boolean isGameCompleted;
	
	private int noOfDices; //optional rule 1
	private boolean shouldGameContinueTillLastPlayer;  //optional rule 3
	private boolean shouldAllowMultipleDiceRoll;  //optional rule 4
	
	private static final int DEFAULT_BOARD_SIZE = 100;
	private static final int DEFAULT_NO_OF_DICES = 1;
	
	public SnakeAndLadderService(int boardSize) {
		this.snakeAndLadderBoard = new SnakeAndLadderBoard(boardSize); //optional rule no 2
		this.players = new LinkedList<Player>();
		this.noOfDices = SnakeAndLadderService.DEFAULT_NO_OF_DICES;
	}
	
	public SnakeAndLadderService() {
		this(SnakeAndLadderService.DEFAULT_BOARD_SIZE);
	}

	public void setNoOfDices(int noOfDices) {
		this.noOfDices = noOfDices;
	}

	public void setShouldGameContinueTillLastPlayer(boolean shouldGameContinueTillLastPlayer) {
		this.shouldGameContinueTillLastPlayer = shouldGameContinueTillLastPlayer;
	}

	public void setShouldAllowMultipleDiceRoll(boolean shouldAllowMultipleDiceRoll) {
		this.shouldAllowMultipleDiceRoll = shouldAllowMultipleDiceRoll;
	}
	
	// Initialize board
	
	public void setPlayers(List<Player> players) {
		this.players = new LinkedList<Player>();
		this.initialNumberOfPlayers = players.size();
		Map<String, Integer> playerPieces = new HashMap<String, Integer>();
		for(Player player: players) {
			this.players.add(player);
			//each player has a piece which is intially outside of the board(i.e; at position 0)
			playerPieces.put(player.getId(), 0); 
			//Add piece to board
			snakeAndLadderBoard.setPlayerPieces(playerPieces);
		}
	}
	
	public void setSnakes(List<Snake> snakes) {
		snakeAndLadderBoard.setSnakes(snakes);
	}
	public void setLadders(List<Ladder> ladders) {
		snakeAndLadderBoard.setLadders(ladders);
	}
	
	//---------Main core logic for game------------------
	
	private int getNewPositionAfterGoingThroughSnakesAndLadders(int newPosition) {
        int previousPosition;

        do {
            previousPosition = newPosition;
            for (Snake snake : snakeAndLadderBoard.getSnakes()) {
                if (snake.getStart() == newPosition) {
                    newPosition = snake.getEnd(); // Whenever a piece ends up at a position with the head of the snake, the piece should go down to the position of the tail of that snake.
                }
            }

            for (Ladder ladder : snakeAndLadderBoard.getLadders()) {
                if (ladder.getStart() == newPosition) {
                    newPosition = ladder.getEnd(); // Whenever a piece ends up at a position with the start of the ladder, the piece should go up to the position of the end of that ladder.
                }
            }
        } while (newPosition != previousPosition); // There could be another snake/ladder at the tail of the snake or the end position of the ladder and the piece should go up/down accordingly.
        return newPosition;
    }

	private void movePlayer(Player player, int positions) {
        int oldPosition = snakeAndLadderBoard.getPlayerPieces().get(player.getId());
        int newPosition = oldPosition + positions; // Based on the dice value, the player moves their piece forward that number of cells.

        int boardSize = snakeAndLadderBoard.getSize();

        // Can modify this logic to handle side case when there are multiple dices (Optional requirements)
        if (newPosition > boardSize) {
            newPosition = oldPosition; // After the dice roll, if a piece is supposed to move outside position 100, it does not move.
        } else {
            newPosition = getNewPositionAfterGoingThroughSnakesAndLadders(newPosition);
        }

        snakeAndLadderBoard.getPlayerPieces().put(player.getId(), newPosition);

        System.out.println(player.getName() + " rolled a " + positions + " and moved from " + oldPosition +" to " + newPosition);
    }

	private int getTotalValueAfterDiceRolls() {
        // Can use noOfDices and setShouldAllowMultipleDiceRollOnSix here to get total value (Optional requirements)
        return DiceService.roll();
    }
	
	private boolean hasPlayerWon(Player player) {
        // Can change the logic a bit to handle special cases when there are more than one dice (Optional requirements)
        int playerPosition = snakeAndLadderBoard.getPlayerPieces().get(player.getId());
        int winningPosition = snakeAndLadderBoard.getSize();
        return playerPosition == winningPosition; // A player wins if it exactly reaches the position 100 and the game ends there.
    }
	
	private boolean isGameCompleted() {
        // Can use shouldGameContinueTillLastPlayer to change the logic of determining if game is completed (Optional requirements)
        int currentNumberOfPlayers = players.size();
        return currentNumberOfPlayers < initialNumberOfPlayers;
    }
	
	 public void startGame() {
	        while (!isGameCompleted()) {
	        	// Each player rolls the dice when their turn comes.
	            int totalDiceValue = getTotalValueAfterDiceRolls(); 
	            Player currentPlayer = players.poll();
	            movePlayer(currentPlayer, totalDiceValue);
	            if (hasPlayerWon(currentPlayer)) {
	                System.out.println(currentPlayer.getName() + " wins the game");
	                snakeAndLadderBoard.getPlayerPieces().remove(currentPlayer.getId());
	            } else {
	                players.add(currentPlayer);
	            }
	        }
	    }
	 
	
	
}
