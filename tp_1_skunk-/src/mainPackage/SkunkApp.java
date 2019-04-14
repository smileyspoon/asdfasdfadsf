package mainPackage;

import java.util.InputMismatchException;
import java.util.LinkedList;

import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.StdOut;

import java.io.FileReader;
import java.io.BufferedReader;

public class SkunkApp {
	private static int numberOfPlayer = 0;

	private static Game game = new Game();
	
	public static void main(String[] args) throws Exception {

		StdOut.println("**********************************");
		StdOut.println("** Welcome to the Skunk game!!! **");
		StdOut.println("**********************************\n");

		// Query user for amount of players
		do {
			StdOut.println("How many players?");

			try {
				numberOfPlayer = StdIn.readInt();
			} catch (InputMismatchException e) {
				StdOut.println("Invalid input");
			}
			
			if (numberOfPlayer < 2) {
				StdOut.println("At least 2 players are required to start a game.\n");
			}
		} while (numberOfPlayer < 2);

		// Enter name for each player
		for (int i = 0; i < numberOfPlayer; i++) {
			game.newPlayer(i+1);
		}

		// Query players for complete listing of rules
		rules();
		
		// Play one round of Skunk
		playRound();
		
	}
	
	private static void playRound() throws Exception {

		boolean roundCompleted = false;
		boolean scored100 = false;
		
		// This block is for 1 round of multiple turns
		// After 1 player gets to 100 every other player gets one more turn to pass them
		while (!roundCompleted) {
			
			StdOut.println("A new round is starting.");
			StdOut.println("Press any key to continue....\n");
			System.in.read();
			
			// if someone has scored 100 these are the last turns
			if (scored100 == true) {
				roundCompleted = true;
			}
			
			// Players take their turns
			for (int i = 0; i < numberOfPlayer; i++) {
				// taking turns and only allowing to take turn if starting with < 100 points in round
				if (game.getPlayer(i).getCurrentRound().getRoundTotal() < 100) {
					StdOut.println("\n\n\n******************************");
					
					StdOut.println("Hello " + game.getPlayer(i).getName());
					StdOut.println("It's turn " + game.getPlayer(i).getCurrentRound().getTurnNumber() + ".\n");
					TurnController turnController = new TurnController();
					turnController.startTurn(game.getPlayer(i).getCurrentTurn());
					game.getPlayer(i).getCurrentRound().addTurnToRoundTotal();
					game.getPlayer(i).getCurrentRound().newTurn();
					
					// these conditions are only true for first to score 100 points in the round
					if ((game.getPlayer(i).getCurrentRound().getRoundTotal() >= 100) && (scored100 == false)) {
						StdOut.println(game.getPlayer(i).getName() + " has scored " + game.getPlayer(i).getCurrentRound().getRoundTotal() + " points!");
						StdOut.println("All players get one more turn to score more than " + game.getPlayer(i).getName());
						scored100 = true;
						break;
					}
				}
			}	
 
			// Print round summary for all players
			roundSummary();	
		}
		
		StdOut.println("We have a round winner!");
	}

	public static void roundSummary() throws Exception {

		for (int i = 0; i < numberOfPlayer; i++) {
			StdOut.println("\n******************************");
			StdOut.println("\n********Round Summary*********");
			StdOut.println("\n******************************");
			StdOut.println(game.getPlayer(i).getName() + "'s Round score is:  "
					+ game.getPlayer(i).getCurrentRound().getRoundTotal());
			StdOut.println("\n");
		}
	}
	
	private static void rules() throws Exception {
		StdOut.println("\nWould you like to view the rules for the game of Skunk?");
		
		if (StdIn.readString().matches("Y|y")) {
			BufferedReader br = new BufferedReader(new FileReader("resources/rules.txt"));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		}
	}
}