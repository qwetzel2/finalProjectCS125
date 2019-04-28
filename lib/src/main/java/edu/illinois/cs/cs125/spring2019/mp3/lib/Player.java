package edu.illinois.cs.cs125.spring2019.mp3.lib;

/**
 * The class that represents the two players in the game.
 */
public class Player {
    /**
     * Constructor.
     * @param setPileName the name for this player's pile.
     */
    public Player(final String setPileName) {
        pileName = setPileName;
    }
    /**
     * The name of the pile accessed by the api of this player's cards.
     */
    public String pileName;


}
