package icarus.Simulator;

import static icarus.Utilities.Units.percent;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import icarus.seprphase2.GameOverException;

import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Marius
 */
public class PhysicalModelTest {

    @Test
    public void runningStepIncreasesReactorTemperature() throws GameOverException {
        PhysicalModel model = new PhysicalModel();
        model.moveControlRods(percent(100));
        model.step(10);
        assertThat(model.reactorTemperature().inKelvin(), greaterThan(350.0));
    }

    @Test
    public void reactorMovesTurbine() throws GameOverException {
        PhysicalModel model = new PhysicalModel();
        model.moveControlRods(percent(100));
        model.step(100);
        assertThat(model.energyGenerated().inJoules(), greaterThan(0.0));
    }

    
    @Test
    public void shouldSetControlRodPosition() throws GameOverException {
        PhysicalModel model = new PhysicalModel();
        model.moveControlRods(percent(100));
        assertTrue(model.controlRodPosition().equals(percent(100)));
    }
    
    @Test
    public void shouldSetConnectionToOpena() {
        PhysicalModel model = new PhysicalModel();
        model.setReactorToTurbine(true);
        assertEquals(true, model.getReactorToTurbine());
    }
    
    @Test
    public void shouldSetConnectionToClosed() {
        PhysicalModel model = new PhysicalModel();
        model.setReactorToTurbine(false);
        assertEquals(false, model.getReactorToTurbine());
    }

    @Test
    public void shouldSetConnectionToOpen() {
        PhysicalModel model = new PhysicalModel();
        model.setReactorToTurbine(true);
        assertEquals(true, model.getReactorToTurbine());

    }
     
    @Test
    public void shouldSetCondenserBackToNormalFailureState() {
        PhysicalModel model = new PhysicalModel();
        model.failCondenser();
        try {
            model.repairCondenser();
        } catch (CannotRepairException e) {
            fail(e.getMessage());
        }
        assertFalse(model.components().get(2).hasFailed());
    }

    @Test
    public void shouldSetTurbineBackToNormalFailureState() {
        PhysicalModel model = new PhysicalModel();
        model.components().get(0).fail();
        try {
            model.repairTurbine();
        } catch (CannotRepairException e) {
            fail(e.getMessage());
        }
        assertFalse(model.components().get(0).hasFailed());
    }

    @Ignore
    @Test(expected = CannotRepairException.class)
    public void shouldNotSetCondenserBackToNormalFailureState() throws CannotRepairException {
        PhysicalModel model = new PhysicalModel();


        model.repairCondenser();

    }
    
    @Ignore
    @Test(expected = CannotRepairException.class)
    public void shouldNotSetTurbineBackToNormalFailureState() throws CannotRepairException {
        PhysicalModel model = new PhysicalModel();


        model.repairCondenser();

    }

    /*@Test
     public void shouldSetPumpBackToPumping() {
     PhysicalModel model = new PhysicalModel();
     model.changePumpState(1, false);
     model.repairPump(1);
     assertEquals(true, model.);
     }*/
    @Test
    public void listNoFailures() {
        PhysicalModel pm = new PhysicalModel();
        assertEquals(0, pm.listFailedComponents().length);
    }

    @Test
    public void listSeveralFailures() {
        PhysicalModel pm = new PhysicalModel();
        pm.failCondenser();
        pm.failReactor();
        String[] expected = {"Reactor", "Condenser"};
        assertArrayEquals(expected, pm.listFailedComponents());
    }
    
    @Ignore
    @Test(expected = CannotRepairException.class)
    public void shouldNotSetPumpBackToNormalFailureState() throws CannotRepairException, KeyNotFoundException {
        PhysicalModel model = new PhysicalModel();
        model.repairPump(1);
    }

    @Test
    public void shouldInitializePump2ToPumping() {
        PhysicalModel model = new PhysicalModel();
        assertTrue(model.getPumpStatus(2));
    }

    @Test
    public void shouldInitializePump1ToPumping() {
        PhysicalModel model = new PhysicalModel();
        assertTrue(model.getPumpStatus(1));

    }

    @Test
    public void shouldSetPumpStateToOff() throws CannotControlException, KeyNotFoundException {
        PhysicalModel model = new PhysicalModel();
        assertTrue(model.getPumpStatus(1));
        model.changePumpState(1, false);
        assertFalse(model.getPumpStatus(1));
    }

    @Test
    public void shouldSetPumpStateToOn() throws CannotControlException, KeyNotFoundException {
        PhysicalModel model = new PhysicalModel();
        assertTrue(model.getPumpStatus(1));
        model.changePumpState(1, false);
        assertFalse(model.getPumpStatus(1));
        model.changePumpState(1, true);
        assertTrue(model.getPumpStatus(1));
    }
    
    @Test(expected = KeyNotFoundException.class)
    public void shouldRefuseToRepairInvalidPump() throws KeyNotFoundException, CannotRepairException {
        PhysicalModel model = new PhysicalModel();
        model.repairPump(100);
    }

    @Test(expected = KeyNotFoundException.class)
    public void shouldRefuseToControlInvalidPump() throws CannotControlException, KeyNotFoundException {
        PhysicalModel model = new PhysicalModel();
        model.changePumpState(100, true);
    }

    @Test(expected = KeyNotFoundException.class)
    public void shouldRefuseToControlInvalidValve() throws KeyNotFoundException {
        PhysicalModel model = new PhysicalModel();
        model.changeValveState(100, true);
    }
}
