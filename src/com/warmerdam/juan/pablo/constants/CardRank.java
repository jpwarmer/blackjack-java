package com.warmerdam.juan.pablo.constants;

public enum CardRank {
	ACE(1, "A"),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	EIGHT(8),
	NINE(9),
	TEN(10),
	JACK(10, "J"),
	QUEEN(10, "Q"),
	KING(10, "K");
	
	private final int value;
	private final String face;
	
	CardRank(int value) {
		this.value = value;
		this.face = value +"";
	}
	
	CardRank(int value, String face) {
		this.value = value;
		this.face = face;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public String getFace() {
		return face;
	}
}
