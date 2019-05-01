package com.warmerdam.blackjack.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.warmerdam.blackjack.cards.Card;
import com.warmerdam.blackjack.hands.Hand;

/**
 * An parent class for all BlackjackPlayers. Basic functionality and properties can be found here.
 * <p>
 * Actions default implementations.
 */
public class BlackjackPlayer {
	
	private double defaultBet;
	private Hand currentHand;
	private List<Hand> hands = new ArrayList<>();
	
	public BlackjackPlayer(double defaultBet) {
		this.defaultBet = defaultBet;
	}
	
	/**
	 * Used to prepare the player for a game.
	 */
	public void getReady() {
		this.hands.clear();
		this.currentHand = null;
	}
	
	/**
	 * @return a List with all hands of cards
	 */
	public List<Hand> getHands() {
		return this.hands;
	}
	
	/**
	 * @return a list of playable hands of cards
	 */
	public List<Hand> getNonBustedHands() {
		return this.hands.stream().filter(h -> !h.isBusted()).collect(Collectors.toList());
	}
	
	/**
	 * @return the value of the default hand
	 */
	public int getValue() {
		return getHand().getValue();
	}
	
	/**
	 * @return The current hand of cards. Return the first one if no hand has been selected.
	 */
	public Hand getHand() {
		if (currentHand == null) {
			currentHand = hands.get(0);
		}
		return currentHand;
	}
	
	/**
	 * Add a card to the currentHand
	 *
	 * @param card
	 */
	public void addCard(Card card) {
		if (hands.isEmpty()) {
			hands.add(new Hand(defaultBet));
		}
		getHand().addCard(card);
	}
	
	/**
	 * @return An Optional with the possible next playable hand.
	 */
	public Optional<Hand> getNextPlayableHand() {
		return this.getNonBustedHands().stream().filter(h -> h.isPlayable()).findAny();
		
	}
	
	/*
	
		ACTIONS
	
	 */
	
	/**
	 * Bust the current hand. A Bust means that the bet is lost, so the money is adjusted accordingly
	 */
	public void bust() {
		this.getHand().bust();
	}
	
	/**
	 * Stand – If the player is happy with the total they’ve been dealt they can stand, taking no further action and passing to the dealer.
	 * The player can take this action after any of the other player actions as long as their hand total is not more than 21.
	 */
	public void stand() {
		this.getHand().stand();
	}
	
	/**
	 * Hit – Add another card. If the hand total is less than 21 the player can choose to Hit again or Stand.
	 * If the total is 21 the hand automatically stands. If the total is over 21 the hand is bust.
	 *
	 * @param aCard to add to the hand
	 */
	public void hit(Card aCard) {
		addCard(aCard);
		int value = getValue();
		if (value >= 21) {
			if (value > 21) {
				bust();
				return;  //This hand is done.
			}
			stand();
		}
	}
	
	/**
	 * Split – If the player’s first two cards are of matching rank they can choose to place an additional bet equal to their original bet and split the cards into two hands.
	 * Where the player chooses to do this the cards are separated and an additional card is dealt to complete each hand.
	 * If either hand receives a second card of matching rank the player may be offered the option to split again.
	 * The split hands are played one at a time. The player has all the usual options: stand, hit, split or double down.
	 */
	public boolean split(Card card, Card cardToNew) {
		if (canSplitHand()) {
			this.getHands().add(this.getHand().split(card, cardToNew));
			return true;
		}
		return false;
	}
	
	private boolean canSplitHand() {
		return this.getHand().canBeSplit();
	}
	
	public void setCurrentHand(Hand currentHand) {
		this.currentHand = currentHand;
	}
	
	public boolean hasBlackjack() {
		return this.getValue() == 21;
	}
	
}
