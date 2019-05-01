package com.warmerdam.blackjack.hands;

import com.warmerdam.blackjack.cards.Card;

/**
 * A Hand of cards for a Player. Keep bet information and a status.
 */
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
	
	/**
	 * Can be played? (Blackjack rule)
	 *
	 * @return
	 */
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
	
	public boolean canBeSplit() {
		return this.getCardsCount() == 2 && this.getUniqueRanks().size() == 1 && this.isPlayable();
	}
	
	public void doubleBet() {
		this.bet *= 2;
	}
	
	public void splitBetInHalf() {
		this.bet /= 2;
	}
	
	public HandStatus getStatus() {
		return status;
	}
	
	/**
	 * Split the current hand.
	 *
	 * @param aNewCard    A card to be added to the current hand after the split
	 * @param anotherCard A card to be added to the new hand after the split
	 * @return A new hand.
	 */
	public PlayerHand split(Card aNewCard, Card anotherCard) {
		Card aCardFromCurrentHand = this.getCard(0);
		this.getCards().remove(aCardFromCurrentHand);
		this.addCard(aNewCard);
		
		PlayerHand aNewHand = new PlayerHand(this.bet);
		aNewHand.addCard(aCardFromCurrentHand);
		aNewHand.addCard(anotherCard);
		
		return aNewHand;
	}
}
