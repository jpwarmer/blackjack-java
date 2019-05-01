package com.warmerdam.blackjack;

import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;

import com.warmerdam.blackjack.cards.Deck;
import com.warmerdam.blackjack.hands.Hand;
import com.warmerdam.blackjack.players.Dealer;
import com.warmerdam.blackjack.players.Player;
import com.warmerdam.blackjack.players.UserAction;

/**
 * Blackjack game.
 * <p>
 * This is the main class of the Blackjack game.
 * Is basically the board, the orchestrator.
 * <p>
 * A lot of the code is related to console interactivity.
 */
public class Blackjack {
	
	private static PrintStream console = System.out;
	private static Scanner scanner = new Scanner(System.in);
	private static Deck deck = new Deck();
	
	public static void main(String[] args) {
		
		//Initialize default values for the player bank and bet
		double initialMoney = 100.0;
		double bet = 10.0;
		
		//Intro
		console.println("Blackjack!");
		console.println("Dealer must HIT on 16 and STAND on 17");
		console.println("Let's play!");
		
		//Create a new player.
		Player player = new Player(initialMoney, bet);
		
		//The games will loop until the player wants to end the players.
		while (true) {
			
			playBlackjack(player);
			
			scanner.nextLine();
			
			console.println(String.format("Your money so far: $%s", player.getMoney()));
			console.println("Continue? (Enter e or E to exit, any other character to continue)");
			String exit = scanner.nextLine();
			if (exit.equalsIgnoreCase("e")) {
				break;
			}
			console.println("\n\n\n");
		}
		console.println(String.format("Your final amount: $%s", player.getMoney()));
		console.println("Hope to see you soon again");
	}
	
	/**
	 * The player will play against the dealer. Using an infinite deck of cards.
	 * Here is the game. How is played.
	 */
	private static void playBlackjack(Player player) {
		//Delete hands of previous games.
		player.getReady();
		
		//Create the dealer.
		Dealer dealer = new Dealer();
		
		//Deal two cards to each player.
		player.addCard(deck.dealCard());
		player.addCard(deck.dealCard());
		
		dealer.addCard(deck.dealCard());
		dealer.addCard(deck.dealCard());
		
		//Check if anyone has Blackjack.
		if (dealer.hasBlackjack()) {
			console.println("Your hand:" + player.getHand());
			console.println("Dealer hand:" + dealer.getHand());
			console.println("Blackjack! You lose!!");
			console.println("\nPress <Enter> to continue.");
			player.pay();
			return;
		}
		if (player.hasBlackjack()) {
			console.println("Dealer hand:" + dealer.getHand());
			console.println("Your hand:" + player.getHand());
			console.println("Blackjack! You Win!!");
			console.println("\nPress <Enter> to continue.");
			player.collect();
			return;
			
		}
		
		//Now the real game begins. Player play first.
		//Must play all the hands until dealer's turn.
		boolean dealerTurn = false;
		
		console.println(String.format("\n\nDealer first card: %s", dealer.getFirstCard()));
		
		while (!dealerTurn) { //Player keep playing as long as she can/wants
			
			//Player can have multiple hands (after split). Should play all of them independently.
			Optional<Hand> optionalPlayingHand = player.getNextPlayableHand();
			
			if (optionalPlayingHand.isPresent()) {
				
				player.setCurrentHand(optionalPlayingHand.get());
				
				console.println("Your playing hand:" + player.getHand());
				console.print("Please, select an option: ");
				console.println("(1) Hit (2) Stand (3) Double Down (4) Split (5) Surrender");
				int option = scanner.nextInt();
				
				switch (UserAction.valueOf(option)) {
					case HIT:
						console.println("You've chosen: HIT");
						player.hit(deck.dealCard());
						console.println("Your resulting hand:" + player.getHand());
						break;
					case STAND:
						console.println("You've chosen: STAND");
						player.stand();
						break;
					case SPLIT:
						console.println("You've chosen: SPLIT");
						if (player.split(deck.dealCard(), deck.dealCard())) {
							console.println("Your hand has been split");
							console.println("Your resulting hand:" + player.getHand());
						} else {
							console.println("Your hand can not be split. Try another option");
						}
						break;
					case DOUBLE_DOWN:
						console.println("You've chosen: DOUBLE DOWN");
						player.doubleDown(deck.dealCard());
						console.println("Your resulting hand:" + player.getHand());
						break;
					case SURRENDER:
						console.println("You've chosen: SURRENDER");
						if (player.surrender()) {
							console.println("Lose half of your bet.");
							console.println("Dealer hand:" + dealer.getHand());
							return;
						} else {
							console.println("You cannot surrender. Try another option");
						}
						break;
				}
				
			} else {
				dealerTurn = true;
			}
		}
		
		//All player's hands are standing or busted
		for (Hand hand : player.getHands()) {
			console.println("Your hand: " + hand + " Status: " + hand.getStatus());
		}
		
		console.println("\nDealer hand:" + dealer.getHand());
		console.println("\nDealer turn.\n");
		//Dealer's turn.
		while (dealerTurn) {
			UserAction dealerAction = dealer.getNextAction();
			switch (dealerAction) {
				case HIT:
					console.println("Dealer HIT");
					dealer.hit(deck.dealCard());
					console.println("Dealer resulting hand:" + dealer.getHand());
					break;
				case STAND:
					console.println("Dealer STAND");
					dealer.stand();
					dealerTurn = false;
					break;
				case DONE:
					dealerTurn = false;
					break;
			}
		}
		
		if (dealer.isBusted()) {
			//Dealer is busted. Player wins!
			console.println("Dealer hand:" + dealer.getHand());
			console.println("You win!!");
			player.collect();
			return;
		}
		
		//Check if dealer won
		if (dealer.hasBlackjack()) {
			console.println("Dealer hand:" + dealer.getHand());
			console.println("There is a tie!. Dealer wins. Its a casino after all");
			player.pay();
			return;
		}
		console.println("Dealer hand:" + dealer.getHand());
		for (Hand hand : player.getNonBustedHands()) {
			if (hand.getValue() > dealer.getValue()) {
				//Player has a better hand. Hand wins.
				console.println("You have a winner hand :" + hand);
				player.collect(hand);
			} else {
				//Player's hand lose
				console.println("You have a losing hand :" + hand);
				if (hand.getValue() == dealer.getValue()) {
					console.println("Remember: in a tie, you lose!");
				}
				player.pay(hand);
			}
		}
	}
	
}
