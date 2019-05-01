package com.warmerdam.blackjack.players;

public enum UserAction {
	HIT(1), STAND(2), DOUBLE_DOWN(3), SPLIT(4), SURRENDER(5);
	
	private final int value;
	
	UserAction(int value) {
		this.value = value;
	}
	
	public static UserAction valueOf(int option) {
		for (UserAction action : UserAction.values()) {
			if (option == action.value) {
				return action;
			}
		}
		return STAND;
	}
}
