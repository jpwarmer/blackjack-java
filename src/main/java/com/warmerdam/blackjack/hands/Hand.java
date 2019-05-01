package com.warmerdam.blackjack.hands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.warmerdam.blackjack.cards.Card;

/**
 * A Hand of cards for a card game.
 */
public class Hand {
	
	private List<Card> cards = new ArrayList<Card>();
	private Boolean containsAnAce = Boolean.FALSE;
	private Double bet = 0.0;
	private HandStatus status = HandStatus.PLAYING;
	
	public Hand() {
	}
	
	public Hand(Double bet) {
		this.bet = bet;
	}
	
	public Double getBet() {
		return bet;
	}
	
	/**
	 * Add a card into the hand
	 *
	 * @param card
	 */
	public void addCard(Card card) {
		if (card.isAnAce()) {
			containsAnAce = Boolean.TRUE;
		}
		cards.add(card);
	}
	
	/**
	 * Get card at index
	 *
	 * @param index
	 */
	public Card getCard(int index) throws IndexOutOfBoundsException {
		return this.getCards().get(index);
	}
	
	@Override
	public String toString() {
		return cards + " = " + getValue();
	}
	
	/**
	 * Return the sum of the ranks in the card according to blackjack rules.
	 */
	public int getValue() {
		int sum = cards.stream().mapToInt(Card::getValue).reduce(0, Integer::sum);
		if (containsAnAce) {
			if (sum + 10 <= 21) {
				sum += 10;
			}
		}
		return sum;
	}
	
	/**
	 * A set with the different values of the cards in the hand
	 *
	 * @return
	 */
	public Set<Integer> getUniqueRanks() {
		return this.cards.stream().map(Card::getValue).collect(Collectors.toSet());
	}
	
	/**
	 * The ammount of cards in the Hand
	 *
	 * @return
	 */
	public Integer getCardsCount() {
		return this.cards.size();
	}
	
	public List<Card> getCards() {
		return cards;
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
		return status.equals(HandStatus.BUSTED);
	}
	
	public void bust() {
		this.status = HandStatus.BUSTED;
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
	public Hand split(Card aNewCard, Card anotherCard) {
		Card aCardFromCurrentHand = this.getCard(0);
		this.getCards().remove(aCardFromCurrentHand);
		this.addCard(aNewCard);
		
		Hand aNewHand = new Hand(this.bet);
		aNewHand.addCard(aCardFromCurrentHand);
		aNewHand.addCard(anotherCard);
		
		return aNewHand;
	}
}
