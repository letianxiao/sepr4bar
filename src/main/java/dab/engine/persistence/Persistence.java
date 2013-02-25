package dab.engine.persistence;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dab.engine.simulator.PhysicalModel;
import dab.engine.utilities.Percentage;

import java.io.IOException;

/**
 * 
 * 
 * The game's persistence is managed by the Simulator class. This uses the Jackson JSON
 * serialization library to serialize the FailureModel object to a text file that is saved to disk.
 * Conversely, it also deserializes a file to the FailureModel when loading an existing game.
 * Files are stored on disk associated with the player's username which they enter at the start of
 * the game. The listSavedGames method reads the game save directory for game names that
 * contain the players username and returns a String[] of files in this saved games folder.
 * 
 * @author David
 */
public class Persistence {

    private ObjectMapper mapper = new ObjectMapper();

    /**
     *
     * @param obj
     *
     * @return
     *
     * @throws JsonProcessingException
     */
    public String serialize(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    /**
     *
     * @param representation
     *
     * @return
     *
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public SaveGame deserializeSaveGame(String representation) throws JsonParseException, JsonMappingException,
                                                                      IOException {
        return mapper.readValue(representation, SaveGame.class);
    }

    /**
     *
     * @param representation
     *
     * @return
     *
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public PhysicalModel deserializePhysicalModel(String representation) throws JsonParseException, JsonMappingException,
                                                                                IOException {
        return mapper.readValue(representation, PhysicalModel.class);
    }

    /**
     *
     * @param representation
     *
     * @return
     *
     * @throws IOException
     */
    public Percentage deserializePercentage(String representation) throws IOException {
        return mapper.readValue(representation, Percentage.class);
    }
}
