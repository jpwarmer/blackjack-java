package com.warmerdam.blackjack.players;

import com.warmerdam.blackjack.cards.Card;

public class Dealer extends BlackjackPlayer {
	
	public Dealer() {
		super(0.0);
	}
	
	public Card getFirstCard() {
		return getHand().getCard(0);
	}
	
	/**
	 * @return
	 */
	public UserAction getNextAction() {
		if (getValue() <= 16) {
			return UserAction.HIT;
		}
		if (getValue() > 21) {
			return UserAction.DONE;
		}
		return UserAction.STAND;
	}
	
	public boolean isBusted() {
		return getHand().isBusted();
	}
	
}
