package com.warmerdam.juan.pablo;

import com.warmerdam.juan.pablo.cards.Card;

public class PlayerHand extends Hand {
	
	private Double bet;
	private Status status;
	
	public PlayerHand(Double bet) {
		super();
		this.status = Status.PLAYING;
		this.bet = bet;
	}
	
	public Double getBet() {
		return bet;
	}
	
	public boolean isPlayable() {
		return status.equals(Status.PLAYING);
	}
	
	public boolean isBusted() {
		return status.equals(Status.BUST);
	}
	
	public void bust() {
		this.status = Status.BUST;
	}
	
	public void stand() {
		this.status = Status.STANDING;
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
