package com.warmerdam.blackjack.players;

import com.warmerdam.blackjack.cards.Card;
import com.warmerdam.blackjack.hands.Hand;

/**
 * A representation of the Player.
 */
public class Player extends BlackjackPlayer {
	
	private double money;
	
	public Player(double money, double bet) {
		super(bet);
		this.money = money;
	}
	
	public double getMoney() {
		return money;
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
	public void pay(Hand hand) {
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
	public void collect(Hand hand) {
		money += hand.getBet();
	}
	
	/**
	 * Bust the current hand. A Bust means that the bet is lost, so the money is adjusted accordingly
	 */
	@Override
	public void bust() {
		super.bust();
		this.money -= this.getHand().getBet();
	}
	
	/**
	 * Double Down – If the player considers they have a favourable hand, generally a total of 9, 10 or 11, they can choose to 'Double Down'.
	 * To do this they place a second wager equal to their first beside their first wager.
	 * A player who doubles down receives exactly one more card face up and is then forced to stand regardless of the total.
	 *
	 * @param card
	 */
	public void doubleDown(Card card) {
		Hand playingHand = this.getHand();
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
	
}
