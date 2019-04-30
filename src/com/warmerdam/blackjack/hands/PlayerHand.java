package com.warmerdam.blackjack.hands;

import com.warmerdam.blackjack.cards.Card;

public class PlayerHand extends Hand {
	
	private Double bet;
	private HandStatus status;
	
	public PlayerHand(Double bet) {
		super();
		this.status = HandStatus.PLAYING;
		this.bet = bet;
	}
	
	public Double getBet() {
		return bet;
	}
	
	public boolean isPlayable() {
		return status.equals(HandStatus.PLAYING);
	}
	
	public boolean isBusted() {
		return status.equals(HandStatus.BUST);
	}
	
	public void bust() {
		this.status = HandStatus.BUST;
	}
	
	public void stand() {
		this.status = HandStatus.STANDING;
	}
	
	public boolean canBeSplited() {
		return this.getCardsCount() == 2 && this.getUniqueRanks().size() == 1 && this.isPlayable();
	}
	
	public void doubleBet() {
		this.bet *= 2;
	}
	
	public void splitBet() {
		this.bet /= 2;
	}
	
	public PlayerHand split() {
		PlayerHand hand = new PlayerHand(this.bet);
		Card card = this.getCards().get(0);
		this.getCards().remove(card);
		hand.addCard(card);
		return hand;
	}
}
