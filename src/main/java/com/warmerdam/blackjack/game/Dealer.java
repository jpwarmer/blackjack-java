package com.warmerdam.blackjack.game;

import com.warmerdam.blackjack.cards.Card;

public class Dealer extends BlackjackPlayer {
	
	public Dealer() {
		super(0.0);
	}
	
	public Card getFirstCard() {
		return getHand().getCard(0);
	}
	
	/**
	 * Add the card to the hand and return true if is able to receive more cards.
	 *
	 * @param card
	 * @return
	 */
	public boolean needAnotherCard(Card card) {
		if (getValue() <= 16) {
			addCard(card);
			if (getValue() > 21) {
				bust();
				return false;
			}
			return true;
		}
		return false;
	}
	
	public boolean isBusted() {
		return getHand().isBusted();
	}
	
}
