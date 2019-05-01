package com.warmerdam.blackjack.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import com.warmerdam.blackjack.cards.Card;
import com.warmerdam.blackjack.cards.CardRank;
import com.warmerdam.blackjack.cards.CardSuit;
import com.warmerdam.blackjack.players.Dealer;
import com.warmerdam.blackjack.players.UserAction;

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
	public void nextActionHitTest() {
		dealer.addCard(new Card(CardRank.THREE, CardSuit.CLUB));
		dealer.addCard(new Card(CardRank.JACK, CardSuit.CLUB));
		assertEquals(UserAction.HIT, dealer.getNextAction());
	}
	
	@Test
	public void nextActionStandTest() {
		dealer.addCard(new Card(CardRank.QUEEN, CardSuit.CLUB));
		dealer.addCard(new Card(CardRank.JACK, CardSuit.CLUB));
		assertEquals(UserAction.STAND, dealer.getNextAction());
	}
	
	@Test
	public void nextActionDoneTest() {
		dealer.addCard(new Card(CardRank.QUEEN, CardSuit.CLUB));
		dealer.addCard(new Card(CardRank.JACK, CardSuit.CLUB));
		dealer.addCard(new Card(CardRank.FIVE, CardSuit.CLUB));
		assertEquals(UserAction.DONE, dealer.getNextAction());
	}
}
