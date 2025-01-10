package server.chinesecheckers;

public class ClassicGame implements Game {

	private int players;

	public ClassicGame(int players) throws IllegalArgumentException {
		if(players != 2 && players != 3 && players != 4 && players != 6) {
			throw new IllegalArgumentException("ERROR: Invalid number of players. Try 2, 3, 4 or 6.");
		}
		this.players = players;
	}

	@Override
	public String draw() {
		return "board";
	}

	@Override
	public void move(int player, int xS, int yS, int xF, int yF) throws IllegalArgumentException {

	}
	
}
