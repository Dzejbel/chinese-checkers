package server.chinesecheckers;

public class Game {
    
    //private Board board;

    public Game(int gameType, int players) {
        //board = new Board(gameType, players);
    }

    public String draw() {
        return "draw board";
    }

    public String move(int player, int xS, int yS, int xK, int yK) {
        return "player " + player + " moved from (" + xS + ", " + yS + ") to (" + xK + ", " + yK + ")";
    }
}
