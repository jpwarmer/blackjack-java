package com.warmerdam.blackjack.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.warmerdam.blackjack.cards.Card;
import com.warmerdam.blackjack.hands.Hand;
import com.warmerdam.blackjack.hands.PlayerHand;

/**
 * A representation of the Player.
 * <p>
 * Keep an account of the money, hands and bets.
 * <p>
 * Perform actions over the hands of cards.
 */
public class Player {
	
	private String name;
	private double money;
	private double defaultBet;
	private PlayerHand currentHand;
	private List<PlayerHand> hands = new ArrayList<>();
	
	public Player(String name, double money, double defaultBet) {
		this.name = name;
		this.money = money;
		this.defaultBet = defaultBet;
	}
	
	/**
	 * Used to prepare the player for a game.
	 */
	public void getReady() {
		this.hands.clear();
		this.currentHand = null;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * @return a List with all hands of cards
	 */
	public List<PlayerHand> getHands() {
		return this.hands;
	}
	
	/**
	 * @return a list of playable hands of cards
	 */
	public List<PlayerHand> getNonBustedHands() {
		return this.hands.stream().filter(h -> !h.isBusted()).collect(Collectors.toList());
	}
	
	/**
	 * @return the value of the default hand
	 */
	public int getValue() {
		return getHand().getValue();
	}
	
	/**
	 * Return the value of the selected hand.
	 *
	 * @param hand
	 * @return
	 */
	public int getHandValue(Hand hand) {
		return hand.getValue();
	}
	
	/**
	 * @return The current hand of cards. Return the first one if no hand has been selected.
	 */
	public PlayerHand getHand() {
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
			hands.add(new PlayerHand(defaultBet));
		}
		getHand().addCard(card);
	}
	
	/**
	 * Pay all the bets lost.
	 */
	public void pay() {
		this.getNonBustedHands().stream().forEach(h -> money -= h.getBet());
	}
	
	/**
	 * Pay the hand's bet lost .
	 *
	 * @param hand
	 */
	public void pay(PlayerHand hand) {
		money -= hand.getBet();
	}
	
	/**
	 * Collect all the earnings
	 */
	public void collect() {
		this.getNonBustedHands().stream().forEach(h -> money += h.getBet());
	}
	
	/**
	 * Collect the hand's bet earnings
	 *
	 * @param hand
	 */
	public void collect(PlayerHand hand) {
		money += hand.getBet();
	}
	
	/**
	 * @return An Optional with the possible next playable hand.
	 */
	public Optional<PlayerHand> getNextPlayableHand() {
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
		this.money -= this.getHand().getBet();
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
	
	/**
	 * Double Down – If the player considers they have a favourable hand, generally a total of 9, 10 or 11, they can choose to 'Double Down'.
	 * To do this they place a second wager equal to their first beside their first wager.
	 * A player who doubles down receives exactly one more card face up and is then forced to stand regardless of the total.
	 *
	 * @param card
	 */
	public void doubleDown(Card card) {
		PlayerHand playingHand = this.getHand();
		playingHand.addCard(card);
		playingHand.doubleBet();
		
		if (playingHand.getValue() > 21) {
			this.bust();
		} else {
			this.stand();
		}
		
	}
	
	/**
	 * Surrender – Taking back half their bet and giving up their hand. Surrender must be the player's first and only action on the hand.
	 *
	 * @return
	 */
	public boolean surrender() {
		if (canSurrender()) {
			getHand().splitBetInHalf();
			this.bust();
			return true;
		}
		return false;
	}
	
	private boolean canSurrender() {
		return (this.getHands().size() == 1 && getHand().getCardsCount() == 2);
	}
	
	public double getMoney() {
		return money;
	}
	
	public void setCurrentHand(PlayerHand currentHand) {
		this.currentHand = currentHand;
	}
}
