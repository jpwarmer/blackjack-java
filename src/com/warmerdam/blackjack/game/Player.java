package com.warmerdam.blackjack.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.warmerdam.blackjack.cards.Card;
import com.warmerdam.blackjack.hands.Hand;
import com.warmerdam.blackjack.hands.PlayerHand;

public class Player {
	
	private String name;
	private double money;
	private double defaultBet;
	
	private List<PlayerHand> hands = new ArrayList<>();
	
	public Player(String name, double money, double defaultBet) {
		this.name = name;
		this.money = money;
		this.defaultBet = defaultBet;
	}
	
	public void resetHands() {
		this.hands.clear();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public List<PlayerHand> getHands() {
		return this.hands;
	}
	
	public List<PlayerHand> getNonBustedHands() {
		return this.hands.stream().filter(h -> !h.isBusted()).collect(Collectors.toList());
	}
	
	public int getValue() {
		return getDefaultHand().getValue();
	}
	
	public int getHandValue(Hand hand) {
		return hand.getValue();
	}
	
	public PlayerHand getDefaultHand() {
		return hands.get(0);
	}
	
	public void addCard(Card card) {
		if (hands.isEmpty()) {
			hands.add(new PlayerHand(defaultBet));
		}
		getDefaultHand().addCard(card);
	}
	
	public void addCardToHand(Hand hand, Card card) {
		hand.addCard(card);
	}
	
	public void pay() {
		this.getNonBustedHands().stream().forEach(h -> money -= h.getBet());
	}
	
	public void pay(PlayerHand hand) {
		money -= hand.getBet();
	}
	
	public void collect() {
		this.getNonBustedHands().stream().forEach(h -> money += h.getBet());
	}
	
	public void collect(PlayerHand hand) {
		money += hand.getBet();
	}
	
	public Optional<PlayerHand> getNextPlayableHand() {
		return this.getNonBustedHands().stream().filter(h -> h.isPlayable()).findAny();
		
	}
	
	public void bustHand(PlayerHand playerHand) {
		playerHand.bust();
		this.money -= playerHand.getBet();
	}
	
	public void standHand(PlayerHand playerHand) {
		playerHand.stand();
	}
	
	public boolean canSplitHand(PlayerHand playingHand) {
		return playingHand.canBeSplited();
	}
	
	public void doubleDown(PlayerHand playingHand, Card card) {
		playingHand.addCard(card);
		playingHand.doubleBet();
		if (playingHand.getValue() > 21) {
			this.bustHand(playingHand);
		} else {
			this.standHand(playingHand);
		}
		
	}
	
	public boolean canSurrender() {
		return (hands.size() == 1 && getDefaultHand().getCardsCount() == 2);
	}
	
	public void surrender() {
		getDefaultHand().splitBet();
		this.bustHand(getDefaultHand());
	}
	
	public void split(PlayerHand playingHand, Card card, Card cardToNew) {
		PlayerHand newHand = playingHand.split();
		playingHand.addCard(card);
		newHand.addCard(cardToNew);
		this.hands.add(newHand);
		
	}
	
	public double getMoney() {
		return money;
	}
}
