package com.warmerdam.blackjack.cards;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * An infinite deck, is always able to deal a card
 */
public class Deck {
	private List<Card> cards;
	private int index;
	
	/**
	 * Creates and shuffle a deck of cards.
	 */
	public Deck() {
		cards = new LinkedList<Card>();
		for (CardSuit suit : CardSuit.values()) {
			for (CardRank rank : CardRank.values()) {
				cards.add(new Card(rank, suit));
			}
		}
		shuffle();
	}
	
	private void shuffle() {
		Collections.shuffle(cards);
		index = 0;
	}
	
	/**
	 * Return the next card in the Deck.
	 *
	 * @return A Card
	 */
	public Card dealCard() {
		if (index == cards.size()) {
			shuffle();
		}
		return cards.get(index++);
	}
	
}
