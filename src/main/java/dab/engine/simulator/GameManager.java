package dab.engine.simulator;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 *
 * Interface which manages Player name, save and load implementations.
 *
 * @author David
 */
public interface GameManager {

    String[] listGames();

    void loadGame(int gameNumber);

    void saveGame() throws JsonProcessingException;

    void setUsername(String userName);
    
    String getUsername();
}
