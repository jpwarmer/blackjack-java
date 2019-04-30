package com.warmerdam.blackjack.cards;

public class Card {
	
	private CardRank rank;
	private CardSuit suit;
	
	public Card(CardRank rank, CardSuit suit) {
		this.rank = rank;
		this.suit = suit;
	}
	
	@Override
	public String toString() {
		return String.format("%s of %s", rank.getFace(), suit.name().toLowerCase());
	}
	
	public int getValue() {
		return rank.getValue();
	}
	
	public boolean isAnAce() {
		return rank.equals(CardRank.ACE);
	}
}
