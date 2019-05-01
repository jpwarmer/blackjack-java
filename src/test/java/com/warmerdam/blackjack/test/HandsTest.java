package com.warmerdam.blackjack.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import com.warmerdam.blackjack.cards.Card;
import com.warmerdam.blackjack.cards.CardRank;
import com.warmerdam.blackjack.cards.CardSuit;
import com.warmerdam.blackjack.hands.HandStatus;
import com.warmerdam.blackjack.hands.PlayerHand;

public class HandsTest {
	
	@Test
	public void blackjackCalculationTest() {
		PlayerHand playerHand = new PlayerHand(10.0);
		playerHand.addCard(new Card(CardRank.ACE, CardSuit.CLUB));
		playerHand.addCard(new Card(CardRank.KING, CardSuit.CLUB));
		
		assertEquals(21, playerHand.getValue());
	}
	
	@Test
	public void statusChangesTest() {
		PlayerHand playerHand = new PlayerHand(10.0);
		playerHand.addCard(new Card(CardRank.ACE, CardSuit.CLUB));
		playerHand.addCard(new Card(CardRank.KING, CardSuit.CLUB));
		
		assertEquals(HandStatus.PLAYING, playerHand.getStatus());
		
		assertTrue(playerHand.isPlayable());
		
		playerHand.bust();
		assertTrue(playerHand.isBusted());
		assertFalse(playerHand.isPlayable());
		assertEquals(HandStatus.BUST, playerHand.getStatus());
		
		playerHand.stand();
		assertEquals(HandStatus.STANDING, playerHand.getStatus());
	}
	
	@Test
	public void singleAceTest() {
		PlayerHand playerHand = new PlayerHand(10.0);
		playerHand.addCard(new Card(CardRank.ACE, CardSuit.CLUB));
		
		playerHand.addCard(new Card(CardRank.TWO, CardSuit.CLUB));
		assertEquals(13, playerHand.getValue());
		
		playerHand.addCard(new Card(CardRank.FOUR, CardSuit.CLUB));
		assertEquals(17, playerHand.getValue());
		
		playerHand.addCard(new Card(CardRank.SIX, CardSuit.CLUB));
		assertEquals(13, playerHand.getValue());
	}
	
	@Test
	public void twoAceTest() {
		PlayerHand playerHand = new PlayerHand(10.0);
		playerHand.addCard(new Card(CardRank.ACE, CardSuit.CLUB));
		
		playerHand.addCard(new Card(CardRank.ACE, CardSuit.CLUB));
		assertEquals(12, playerHand.getValue());
		
	}
	
	@Test
	public void splitTest() {
		PlayerHand playerHand = new PlayerHand(10.0);
		playerHand.addCard(new Card(CardRank.SEVEN, CardSuit.CLUB));
		playerHand.addCard(new Card(CardRank.SEVEN, CardSuit.CLUB));
		
		assertEquals(14, playerHand.getValue());
		assertTrue(playerHand.canBeSplit());
		;
		
		PlayerHand newHand = playerHand.split(new Card(CardRank.TWO, CardSuit.CLUB), new Card(CardRank.THREE, CardSuit.CLUB));
		
		assertEquals(10, newHand.getValue());
		assertEquals(9, playerHand.getValue());
		assertFalse(playerHand.canBeSplit());
		
	}
	
	@Test
	public void getCardTest() {
		PlayerHand playerHand = new PlayerHand(10.0);
		playerHand.addCard(new Card(CardRank.SEVEN, CardSuit.CLUB));
		playerHand.addCard(new Card(CardRank.SEVEN, CardSuit.CLUB));
		
		assertNotNull(playerHand.getCard(0));
		assertNotNull(playerHand.getCard(1));
		
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void getCardThrowExceptionTest() {
		PlayerHand playerHand = new PlayerHand(10.0);
		playerHand.getCard(0);
		
	}
}
