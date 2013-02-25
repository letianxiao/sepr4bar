package dab.engine.persistence;

import dab.engine.simulator.FailureModel;
import dab.engine.simulator.PhysicalModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Saveable/Loadable game state
 * 
 * @author David
 */
public class SaveGame {

    @JsonProperty
    private PhysicalModel physicalModel;
    @JsonProperty
    private String userName;

    private SaveGame() {
    }

    /**
     * Load a specified saved game
     *  @param String
     *  @return SaveGame
     */
    public static SaveGame load(String filename) throws JsonParseException, IOException {
        Persistence p = new Persistence();
        return p.deserializeSaveGame(FileSystem.readString(filename));
    }

    /**
     *  Save the state of the game
     *  @param PhysicalModel
     *  @param FailureModel
     *  @param String
     */
    public SaveGame(PhysicalModel physicalModel, FailureModel failureModel, String userName) {
        this.physicalModel = physicalModel;
        this.userName = userName;
    }
    /**
     * Serialize the saved game
     * @throws JsonProcessingException, FileNotFoundException, IOException
     */
    public void save() throws JsonProcessingException, FileNotFoundException, IOException {
        Persistence p = new Persistence();
        String data = p.serialize(this);
        FileSystem.createSavePath();
        FileSystem.writeString(fileName(), data);
    }

    /**
     * Get the name of the user
     *  @return String
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     *  Get the physical mode
     *  @return PhysicalModel
     */
    public PhysicalModel getPhysicalModel() {
        return this.physicalModel;
    }

    /**
     * generateFileName generates a new unique file name using getTimeInMillis
     *
     * @return The newly generated random file name
     */
    private String fileName() {
        Calendar cal = Calendar.getInstance();
        return "sepr.teameel." + userName + "." + cal.getTimeInMillis() + ".nuke";
    }
}
