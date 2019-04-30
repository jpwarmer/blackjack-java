package com.warmerdam.blackjack.game;

public enum Action {
	HIT(1), STAND(2), DOUBLE_DOWN(3), SPLIT(4), SURRENDER(5);
	
	private final int value;
	
	Action(int value) {
		this.value = value;
	}
	
	public static Action valueOf(int option) {
		for (Action action : Action.values()) {
			if (option == action.value) {
				return action;
			}
		}
		return STAND;
	}
}
