package com.warmerdam.blackjack;

import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;

import com.warmerdam.blackjack.cards.Deck;
import com.warmerdam.blackjack.game.Action;
import com.warmerdam.blackjack.game.Player;
import com.warmerdam.blackjack.hands.Hand;
import com.warmerdam.blackjack.hands.PlayerHand;

/**
 * We would like for you to build the game of Blackjack using the command line in Java. The game has some interesting rules and is a great parallel for various business rules we encounter in real projects. Our objective here is to see how you organize your thoughts and implement major OO principles into your work. We will also assess your overall coding skills in the Java language. Your objective it to produce a PLAYABLE GAME. This does not mean all the rules need to be perfect, but the general game play should be implemented.
 * <p>
 * This should be a command line program, you will be playing against a very rudimentary dealer robot that you will write.THE ROBOT DOES NOT NEED TO BE GOOD AT BLACKJACK. It can randomly select moves,, but it must make those move within the allowed rules of the game..
 * <p>
 * The game should start with $100, and each hand is $10. There are many good guides for how to play Blackjack, and here is a recap of the game
 * <p>
 * http://www.pagat.com/banking/blackjack.html
 * <p>
 * Though the rules can get involved, the available actions available are quite simple. The command line should show whose move it is and allow you to select one of these:
 * <p>
 * (1) Hit (2) Stand (3) Double Down (4) Split (5) Surrender
 * <p>
 * After the game ends, the program should declare a winner and ask for a new game allocating the winnings to the bank account of your user.
 * <p>
 * You will have 48 hours to complete this challenge. This is not a lot of time, and we would like to see how you chose to spend it. The part of this game we care most about is the infrastructure of the game, not the way that the bots play, or how the visuals look. Please make your code easy to read, well commented, and all zombie code cleaned up.
 * <p>
 * Once completed please create an account on BitBucket, upload this private repo, and send us a link. This assignment should be completed by Thursday morning. Please let me know if there is an issue with timing as I know this was delayed on our end.
 * <p>
 * Note: We have seen many of the solutions out on the web. If we see a striking similarity to what is out there, we will not be able to proceed with the interview. Please use this to express your personal coding style
 */
public class Blackjack {
	private static PrintStream console = System.out;
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		double initialMoney = 100.0;
		double bet = 10.0;
		console.println("Blackjack!");
		console.println("Dealer must HIT on 16 and STAND on 17");
		console.println("Let's play!");
		console.println("Your name?: ");
		String playerName = scanner.nextLine();
		if (playerName == "") {
			playerName = "You!";
		}
		
		Player player = new Player(playerName, initialMoney, bet);
		Deck deck = new Deck();
		while (true) {
			
			playBlackjack(player, deck);
			console.println(String.format("Your money so far: $%s", player.getMoney()));
			console.println("Do you want to exit? (Y)");
			String exit = scanner.nextLine();
			if (exit.equalsIgnoreCase("y")) {
				break;
			}
		}
		console.println(String.format("Your final amount: $%s", player.getMoney()));
	}
	
	/**
	 * The player will play against the dealer. Using a deck of cards.
	 */
	private static void playBlackjack(Player player, Deck deck) {
		//Delete hands of previous games.
		player.resetHands();
		
		//The dealer is just a Hand of cards.
		Hand dealer = new Hand();
		
		//Beginning of the game. Two cards each.
		player.addCard(deck.dealCard());
		player.addCard(deck.dealCard());
		
		dealer.addCard(deck.dealCard());
		dealer.addCard(deck.dealCard());
		
		//Check if anyone has Blackjack.
		if (dealer.getValue() == 21) {
			console.println("Your hand:" + player.getDefaultHand());
			console.println("Dealer hand:" + dealer);
			console.println("Blackjack! You lose!!");
			player.pay();
			return;
		}
		if (player.getValue() == 21) {
			console.println("Dealer hand:" + dealer);
			console.println("Your hand:" + player.getDefaultHand());
			console.println("Blackjack! You Win!!");
			player.collect();
			return;
			
		}
		
		//Now the real game begins. Player play first.
		boolean dealerTurn = false;
		console.println("Dealer first card:" + dealer.getCard(0));
		while (!dealerTurn) { //Player keep playing as long as it can
			
			Optional<PlayerHand> optionalPlayingHand = player.getNextPlayableHand();
			//Player can have multiple hands (after split). Should play all of them independently.
			if (optionalPlayingHand.isPresent()) {
				PlayerHand playingHand = optionalPlayingHand.get();
				console.println("Your playing hand:" + playingHand);
				console.print("Please, select an option: ");
				console.println("(1) Hit (2) Stand (3) Double Down (4) Split (5) Surrender");
				int option = scanner.nextInt();
				int playingHandCurrentValue = 0;
				switch (Action.valueOf(option)) {
					case HIT:
						//Hit – Add another card. If the hand total is less than 21 the player can choose to Hit again or Stand.
						// If the total is 21 the hand automatically stands. If the total is over 21 the hand is bust.
						player.addCardToHand(playingHand, deck.dealCard());
						playingHandCurrentValue = player.getHandValue(playingHand);
						if (playingHandCurrentValue >= 21) {
							if (playingHandCurrentValue > 21) {
								player.bustHand(playingHand);
								break;  //This hand is done.
							}
							player.standHand(playingHand);
						}
						break;
					case STAND:
						//Stand – If the player is happy with the total they’ve been dealt they can stand, taking no further action and passing to the dealer.
						// The player can take this action after any of the other player actions as long as their hand total is not more than 21.
						player.standHand(playingHand);
						break;
					case SPLIT:
						//Split – If the player’s first two cards are of matching rank they can choose to place an additional bet equal to their original bet and split the cards into two hands.
						// Where the player chooses to do this the cards are separated and an additional card is dealt to complete each hand.
						// If either hand receives a second card of matching rank the player may be offered the option to split again.
						// The split hands are played one at a time. The player has all the usual options: stand, hit, split or double down.
						
						if (player.canSplitHand(playingHand)) {
							player.split(playingHand, deck.dealCard(), deck.dealCard());
						}
						break;
					case DOUBLE_DOWN:
						//Double Down – If the player considers they have a favourable hand, generally a total of 9, 10 or 11, they can choose to 'Double Down'.
						// To do this they place a second wager equal to their first beside their first wager.
						// A player who doubles down receives exactly one more card face up and is then forced to stand regardless of the total.
						player.doubleDown(playingHand, deck.dealCard());
						break;
					case SURRENDER:
						//Surrender – Taking back half their bet and giving up their hand. Surrender must be the player's first and only action on the hand.
						if (player.canSurrender()) {
							player.surrender();
							console.println("Your choose to surrender. Lose half your bet.");
							console.println("Dealer hand:" + dealer);
							return;
						}
						break;
				}
			} else {
				dealerTurn = true;
			}
		}
		for (PlayerHand hand : player.getHands()) {
			console.println("Your hand: " + hand + " Status: " + hand.getStatus() + " Total: " + hand.getValue());
		}
		
		//Player done. Stand or 21.
		while (dealer.getValue() <= 16) {
			//Less than 16, dealer hit.
			dealer.addCard(deck.dealCard());
			if (dealer.getValue() > 21) {
				//Dealer is bust. Player wins!
				console.println("Dealer hand:" + dealer + " Total: " + dealer.getValue());
				console.println("You win!!");
				player.collect();
				return;
			}
		}
		if (dealer.getValue() == 21) {
			console.println("Dealer hand:" + dealer + " Total: " + dealer.getValue());
			console.println("There is a tie!. Dealer wins. Its a casino after all");
			player.pay();
			return;
		}
		console.println("Dealer hand:" + dealer + " Total: " + dealer.getValue());
		for (PlayerHand hand : player.getNonBustedHands()) {
			if (hand.getValue() > dealer.getValue()) {
				//Player has a better hand. Hand wins.
				console.println("Winner hand :" + hand);
				player.collect(hand);
			} else {
				//Player hand lose
				console.println("Loser hand :" + hand);
				player.pay(hand);
			}
		}
		
		scanner.nextLine();
	}
	
}
