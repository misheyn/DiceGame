package com.company;

public class Player {
    public Player() {}
    public Player(String playerName, PlayerType _type) {
        name = playerName;
        score = 0;
        winStatus = false;
        type = _type;
    }

    public static Player getInstance() {
        Player localInstance = instance;
        if (localInstance == null) {
            synchronized (Player.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Player();
                }
            }
        }
        return localInstance;
    }

    private static volatile Player instance;
    public int score;
    public boolean winStatus;
    public String name;
    public PlayerType type;

}
