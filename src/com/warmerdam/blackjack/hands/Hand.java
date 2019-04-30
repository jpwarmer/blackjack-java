package com.warmerdam.blackjack.hands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.warmerdam.blackjack.cards.Card;

public class Hand {
	
	private List<Card> cards = new ArrayList<Card>();
	private Boolean containsAnAce = Boolean.FALSE;
	
	public Hand() {
	}
	
	public void addCard(Card card) {
		if (card.isAnAce()) {
			containsAnAce = Boolean.TRUE;
		}
		cards.add(card);
	}
	
	public Card getCard(int index) {
		return this.getCards().get(index);
	}
	
	@Override
	public String toString() {
		return cards + "";
	}
	
	public int getValue() {
		int sum = cards.stream().mapToInt(Card::getValue).reduce(0, Integer::sum);
		if (containsAnAce) {
			if (sum + 10 <= 21) {
				sum += 10;
			}
		}
		return sum;
	}
	
	public Set<Integer> getUniqueRanks() {
		return this.cards.stream().map(Card::getValue).collect(Collectors.toSet());
	}
	
	public Integer getCardsCount() {
		return this.cards.size();
	}
	
	public List<Card> getCards() {
		return cards;
	}
}
