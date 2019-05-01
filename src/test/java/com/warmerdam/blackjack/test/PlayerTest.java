package com.warmerdam.blackjack.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import com.warmerdam.blackjack.cards.Card;
import com.warmerdam.blackjack.cards.CardRank;
import com.warmerdam.blackjack.cards.CardSuit;
import com.warmerdam.blackjack.players.Player;

public class PlayerTest {
	private Player player;
	
	@Before
	public void setup() {
		player = new Player(100.0, 10.0);
		player.getReady();
	}
	
	@Test
	public void addCardTest() {
		player.addCard(new Card(CardRank.ACE, CardSuit.CLUB));
		assertEquals(11, player.getValue());
	}
	
	@Test
	public void surrenderFailureTest() {
		player.addCard(new Card(CardRank.ACE, CardSuit.CLUB));
		assertFalse(player.surrender());
	}
	
	@Test
	public void surrenderTrueTest() {
		player.addCard(new Card(CardRank.ACE, CardSuit.CLUB));
		player.addCard(new Card(CardRank.THREE, CardSuit.CLUB));
		assertTrue(player.surrender());
		assertEquals(95.0, player.getMoney(), 0);
	}
	
	@Test
	public void splitTrueTest() {
		player.addCard(new Card(CardRank.THREE, CardSuit.CLUB));
		player.addCard(new Card(CardRank.THREE, CardSuit.DIAMOND));
		assertTrue(player.split(new Card(CardRank.ACE, CardSuit.SPADE), new Card(CardRank.FIVE, CardSuit.HEART)));
		
	}
	
}
