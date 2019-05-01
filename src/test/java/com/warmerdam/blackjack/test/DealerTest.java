package com.warmerdam.blackjack.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import com.warmerdam.blackjack.cards.Card;
import com.warmerdam.blackjack.cards.CardRank;
import com.warmerdam.blackjack.cards.CardSuit;
import com.warmerdam.blackjack.players.Dealer;

public class DealerTest {
	private Dealer dealer;
	
	@Before
	public void setup() {
		dealer = new Dealer();
		dealer.getReady();
	}
	
	@Test
	public void getFirstCardTest() {
		dealer.addCard(new Card(CardRank.ACE, CardSuit.CLUB));
		dealer.addCard(new Card(CardRank.THREE, CardSuit.CLUB));
		assertTrue(dealer.getFirstCard().isAnAce());
	}
	
	@Test
	public void getAnotherFirstCardTest() {
		dealer.addCard(new Card(CardRank.THREE, CardSuit.CLUB));
		dealer.addCard(new Card(CardRank.ACE, CardSuit.CLUB));
		assertFalse(dealer.getFirstCard().isAnAce());
	}
	
	@Test
	public void needAnotherCardTest() {
		dealer.addCard(new Card(CardRank.THREE, CardSuit.CLUB));
		dealer.addCard(new Card(CardRank.JACK, CardSuit.CLUB));
		assertTrue(dealer.needAnotherCard(new Card(CardRank.ACE, CardSuit.CLUB)));
		assertFalse(dealer.needAnotherCard(new Card(CardRank.KING, CardSuit.CLUB)));
	}
	
	@Test
	public void needAnotherCardWithBlackjackTest() {
		dealer.addCard(new Card(CardRank.JACK, CardSuit.CLUB));
		assertTrue(dealer.needAnotherCard(new Card(CardRank.ACE, CardSuit.CLUB)));
		assertFalse(dealer.needAnotherCard(new Card(CardRank.ACE, CardSuit.CLUB)));
	}
	
}
