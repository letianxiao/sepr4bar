/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dab.engine.persistence;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import dab.engine.persistence.Persistence;
import dab.engine.simulator.GameOverException;
import dab.engine.simulator.FuelPile;
import dab.engine.simulator.PhysicalModel;
import dab.engine.simulator.Port;
import dab.engine.simulator.Reactor;
import dab.engine.utilities.Percentage;
import static dab.engine.utilities.Units.joules;
import static dab.engine.utilities.Units.kelvin;
import static dab.engine.utilities.Units.kilograms;
import static dab.engine.utilities.Units.kilogramsPerCubicMetre;
import static dab.engine.utilities.Units.percent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author David
 */
public class PersistenceTest {

	Persistence persistence;
	PhysicalModel before;
	PhysicalModel after;

    @Before
    public void setUp() {
        persistence = new Persistence();
        before = new PhysicalModel();
    }

    @Test
    public void shouldSerializePercentage() throws JsonProcessingException {
        String result = persistence.serialize(percent(50));
        assertEquals("{\"@class\":\"Percentage\",\"percentagePoints\":50.0}", result);
    }

    @Test
    public void shouldSerializeDensity() throws JsonProcessingException {
        String result = persistence.serialize(kilogramsPerCubicMetre(10));
        assertEquals("{\"@class\":\"Density\",\"kilogramsPerCubicMetre\":10.0}", result);
    }

    @Test
    public void shouldSerializeTemperature() throws JsonProcessingException {
        String result = persistence.serialize(kelvin(300));
        assertEquals("{\"@class\":\"Temperature\",\"degreesKelvin\":300.0}", result);
    }

    @Test
    public void shouldSerializeMass() throws JsonProcessingException {
        String result = persistence.serialize(kilograms(40));
        assertEquals("{\"@class\":\"Mass\",\"kilograms\":40.0}", result);
    }

    @Test
    public void shouldSerializeEnergy() throws JsonProcessingException {
        String result = persistence.serialize(joules(5));
        assertEquals("{\"@class\":\"Energy\",\"joules\":5.0}", result);
    }

    @Test
    public void shouldSerializePort() throws JsonProcessingException {
        String result = persistence.serialize(new Port());
        assertNotSame("", result);
    }

    @Test
    public void shouldSerializeFuelPile() throws JsonProcessingException {
        String result = persistence.serialize(new FuelPile());
        assertEquals(
                "{\"@class\":\"FuelPile\",\"controlRodPosition\":{\"@class\":\"Percentage\",\"percentagePoints\":0.0}}",
                result);
    }

    @Test
    public void shouldSerializeReactor() throws JsonProcessingException {
        String result = persistence.serialize(new Reactor());
        assertNotSame("", result);
    }

    @Test
    public void shouldSerializePhysicalModel() throws JsonProcessingException {
        String result = persistence.serialize(new PhysicalModel());
        assertNotSame("", result);
    }

    @Test
    public void shouldPersistPercentage() throws JsonProcessingException, IOException {
        Percentage before = percent(50);
        String result = persistence.serialize(before);
        Percentage after = persistence.deserializePercentage(result);
        assertEquals(before, after);
        assertNotSame(before, after);
    }

    @Test
    public void shouldPersistPhysicalModel() throws JsonProcessingException, IOException {
        shouldPersistConsistently();
    }

    @Test
    public void shouldPersistChangesToControlRodPosition() throws IOException, JsonProcessingException {
        before.moveControlRods(percent(57));
        shouldPersistConsistently();
    }

    @Test
    public void shouldPersistConditions() throws GameOverException {
        before.moveControlRods(percent(67));
        before.step(100);
        shouldPersistConsistently();
    }

    private void backAndForthPersistence() {
        try {
            String representation = persistence.serialize(before);
            after = persistence.deserializePhysicalModel(representation);
        } catch (JsonParseException ex) {
            Logger.getLogger(PersistenceTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JsonMappingException ex) {
            Logger.getLogger(PersistenceTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PersistenceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void shouldPersistConsistently() {
        backAndForthPersistence();
        assertNotNull(after);
        assertNotSame(before, after);
        assertEquals(before.controlRodPosition(), after.controlRodPosition());
        assertEquals(before.energyGenerated(), after.energyGenerated());
        assertEquals(before.reactorPressure(), after.reactorPressure());
        assertEquals(before.reactorTemperature(), after.reactorTemperature());
        assertEquals(before.reactorWaterLevel(), after.reactorWaterLevel());
    }
}
