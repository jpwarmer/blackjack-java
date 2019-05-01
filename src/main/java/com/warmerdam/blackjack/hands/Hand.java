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
	
	public Hand() {
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
		return cards + "";
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
}
