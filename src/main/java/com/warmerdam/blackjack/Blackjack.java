package com.warmerdam.blackjack;

import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;

import com.warmerdam.blackjack.cards.Deck;
import com.warmerdam.blackjack.players.Dealer;
import com.warmerdam.blackjack.players.Player;
import com.warmerdam.blackjack.players.UserAction;
import com.warmerdam.blackjack.hands.Hand;

/**
 * We would like for you to build the players of Blackjack using the command line in Java.
 * The players has some interesting rules and is a great parallel for various business rules we encounter in real projects.
 * Our objective here is to see how you organize your thoughts and implement major OO principles into your work.
 * We will also assess your overall coding skills in the Java language.
 * <p>
 * Your objective it to produce a PLAYABLE GAME. This does not mean all the rules need to be perfect, but the general players play should be implemented. * <p>
 * This should be a command line program, you will be playing against a very rudimentary dealer robot that you will write.THE ROBOT DOES NOT NEED TO BE GOOD AT BLACKJACK.
 * It can randomly select moves,, but it must make those move within the allowed rules of the players..
 * <p>
 * The players should start with $100, and each hand is $10. There are many good guides for how to play Blackjack, and here is a recap of the players
 * >
 * http://www.pagat.com/banking/blackjack.html
 * <p>
 * Though the rules can get involved, the available actions available are quite simple. The command line should show whose move it is and allow you to select one of these:
 * <p>
 * (1) Hit (2) Stand (3) Double Down (4) Split (5) Surrender
 * >
 * After the players ends, the program should declare a winner and ask for a new players allocating the winnings to the bank account of your user.
 * <p>
 * You will have 48 hours to complete this challenge.
 * This is not a lot of time, and we would like to see how you chose to spend it. The part of this players we care most about is the infrastructure of the players, not the way that the bots play, or how the visuals look.
 * <p>
 * Please make your code easy to read, well commented, and all zombie code cleaned up.
 * Once completed please create an account on BitBucket, upload this private repo, and send us a link. This assignment should be completed by Thursday morning. Please let me know if there is an issue with timing as I know this was delayed on our end.
 * Note: We have seen many of the solutions out on the web. If we see a striking similarity to what is out there, we will not be able to proceed with the interview. Please use this to express your personal coding style
 */

/**
 * Blackjack players.
 * <p>
 * This is the main class of the Blackjack players.
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
			console.println("Do you want to exit? (Y)");
			String exit = scanner.nextLine();
			if (exit.equalsIgnoreCase("y")) {
				break;
			}
			console.println("\n\n\n");
		}
		console.println(String.format("Your final amount: $%s", player.getMoney()));
		console.println("Hope to see you soon again");
	}
	
	/**
	 * The player will play against the dealer. Using an infinite deck of cards.
	 */
	private static void playBlackjack(Player player) {
		//Delete hands of previous games.
		player.getReady();
		
		//The dealer is just a Hand of cards.
		Dealer dealer = new Dealer();
		
		//Beginning of the players. Two cards each.
		player.addCard(deck.dealCard());
		player.addCard(deck.dealCard());
		
		dealer.addCard(deck.dealCard());
		dealer.addCard(deck.dealCard());
		
		//Check if anyone has Blackjack.
		if (dealer.hasBlackjack()) {
			console.println("Your hand:" + player.getHand());
			console.println("Dealer hand:" + dealer.getHand());
			console.println("Blackjack! You lose!!");
			player.pay();
			return;
		}
		if (player.hasBlackjack()) {
			console.println("Dealer hand:" + dealer.getHand());
			console.println("Your hand:" + player.getHand());
			console.println("Blackjack! You Win!!");
			player.collect();
			return;
			
		}
		
		//Now the real players begins. Player play first.
		//Must play all the hands until is dealer turn.
		boolean dealerTurn = false;
		
		console.println(String.format("\n\nDealer first card: %s", dealer.getFirstCard()));
		
		while (!dealerTurn) { //Player keep playing as long as it can/wants
			
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
		
		//All player hands are standing or busted
		for (Hand hand : player.getHands()) {
			console.println("Your hand: " + hand + " Status: " + hand.getStatus());
		}
		
		//Dealer turn.
		while (true) {
			if (!dealer.needAnotherCard(deck.dealCard())) {
				break;
			}
		}
		
		if (dealer.isBusted()) {
			//Dealer is bust. Player wins!
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
				//Player hand lose
				console.println("You have a losing hand :" + hand);
				player.pay(hand);
			}
		}
	}
	
}