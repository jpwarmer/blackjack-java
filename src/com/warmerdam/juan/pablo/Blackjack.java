package com.warmerdam.juan.pablo;

import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;

import com.warmerdam.juan.pablo.cards.Deck;
import com.warmerdam.juan.pablo.constants.Action;

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
			
			console.println("Do you want to exit? (Y)");
			String exit = scanner.nextLine();
			if (exit.equalsIgnoreCase("y")) {
				break;
			}
		}
		
	}
	
	/**
	 * The player will play against the dealer. Using a deck of cards.
	 */
	private static void playBlackjack(Player player, Deck deck) {
		//Delete hands of previous games.
		player.resetHands();
		
		//The dealer is just a Hand of cards.
		Hand dealer = new Hand();
		
		//Beginning of the game. Two cards each
		player.addCard(deck.dealCard());
		player.addCard(deck.dealCard());
		
		dealer.addCard(deck.dealCard());
		dealer.addCard(deck.dealCard());
		
		//Check if anyone has Blackjack.
		if (dealer.getValue() == 21) {
			player.pay();
			return;
		}
		if (player.getValue() == 21) {
			player.collect();
			return;
			
		}
		
		//Now the real game begins. Player play first.
		boolean dealerTurn = false;
		
		while (!dealerTurn) {
			
			Optional<PlayerHand> optionalPlayingHand = player.getNextPlayableHand();
			if (optionalPlayingHand.isPresent()) {
				PlayerHand playingHand = optionalPlayingHand.get();
				
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
								break;  //Player lose
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
						//Split – If the player’s first two cards are of matching rank they can choose to place an additional bet equal to their original bet and split the cards into two hands. Where the player chooses to do this the cards are separated and an additional card is dealt to complete each hand. If either hand receives a second card of matching rank the player may be offered the option to split again, though this depends on the rules in the casino. Generally the player is allowed a maximum of 4 hands after which no further splits are allowed. The split hands are played one at a time in the order in which they were dealt, from the dealer's left to the delaer's right. The player has all the usual options: stand, hit or double down.
						//
						//A player who splits Aces is usually only allowed to receive a single additional card on each hand. Normally players are allowed to split two non-matching 10-value cards, for example a King and a Jack. If Aces are split and the player draws a Ten or if Tens are split and the player draws an Ace, the resulting hand does not count as a Blackjack but only as an ordinary 21. In this case the player's two-card 21 will push (tie with) dealer's 21 in three or more cards.
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
						//Surrender – Most casinos allow a player to surrender, taking back half their bet and giving up their hand. Surrender must be the player's first and only action on the hand. In the most usual version, known as Late Surrender, it is after the dealer has checked the hole card and does not have a Blackjack.
						//
						//After all players have completed their actions the dealer plays their hand according to fixed rules. First they will reveal their down card. The dealer will then continue to take cards until they have a total of 17 or higher. The rules regarding Soft 17 (a total of 17 with an Ace counted as 11 such as A+6) vary from casino to casino. Some require the dealer to stand while others require additional cards to be taken until a total of hard 17 or 18+ is reached. This rule will be clearly printed on the felt of the table.
						//
						//If the dealer busts all non-busted player hands are automatically winners.
						if (player.canSurrender()) {
							player.surrender();
						}
						break;
				}
			} else {
				dealerTurn = true;
			}
			scanner.nextLine();
			break;
		}
		
		//Player done. Stand or 21.
		if (dealer.getValue() <= 16) {
			//Less than 16, dealer hit.
			dealer.addCard(deck.dealCard());
			if (dealer.getValue() > 21) {
				//Dealer is bust. Player wins!
				player.collect();
				return;
			}
		}
		if (dealer.getValue() == 21) {
			//At least tie. Dealer wins!
			player.pay();
			return;
		}
		for (Hand hand : player.getHands()) {
			if (hand.getValue() > dealer.getValue()) {
				//Player has a better hand. Player wins.
				player.collect();
				return;
			}
		}
		//Player lose
		player.pay();
		
	}
	
	private static void printDealerHand(String s) {
		console.println(s);
	}
	
	private static void printPlayerHands(Player player) {
		for (Hand hand : player.getHands()) {
			console.println("Your cards are: " + hand);
		}
	}
}
