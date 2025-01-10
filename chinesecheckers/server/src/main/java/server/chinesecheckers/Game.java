package server.chinesecheckers;

public interface Game {
    public String draw();
    public void move(int player, int xS, int yS, int xF, int yF) throws IllegalArgumentException;
}
