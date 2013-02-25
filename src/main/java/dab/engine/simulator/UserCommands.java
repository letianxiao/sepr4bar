package dab.engine.simulator;

import java.util.Random;

/**
 * Enum class for the list of user commands.
 * 
 * @author Team Eel
 *
 */

public enum UserCommands {
    TURNON,
    TURNOFF,
    //REPAIR,
    MOVE,
    OPEN,
    CLOSE;

    /**
     *
     * @return a random User Command
     */
    public static UserCommands randomCommand() {
        int pick = new Random().nextInt(UserCommands.values().length);
        return UserCommands.values()[pick];
    }
}
