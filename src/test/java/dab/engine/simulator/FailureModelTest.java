package dab.engine.simulator;

import dab.engine.simulator.FailureModel;
import dab.engine.simulator.PlantController;
import dab.engine.simulator.PlantStatus;
import dab.engine.simulator.UserCommands;
import static dab.engine.utilities.Units.*;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;


/**
 *
 * @author david
 */
public class FailureModelTest {

    @Rule
    public final JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock
    private PlantController plantController;
    @Mock
    private PlantStatus plantStatus;
    
    private FailureModel model;

    public FailureModelTest() {
    }

    @Before
    public void setup() {
        model = new FailureModel(plantController, plantStatus);
    }
    
    @Test
    public void testTrue() {
        assertTrue(true);
        
    }
    

    
    @Ignore @Test
    public void testStep_0args() throws Exception {
    }

    @Ignore @Test
    public void testFailStateCheck() {
    }

    @Ignore
    @Test
    public void listNoFailedComponents() {
        final String[] componentList = {};
        String[] expected = {};
        context.checking(new Expectations() {
            {
                allowing(plantStatus).listFailedComponents();
                will(returnValue(componentList));
            }
        });
        assertArrayEquals(expected, model.listFailedComponents());
    }

    @Test
    public void listFailedCondenser() {
        final String[] componentList = {"Condenser"};
        String[] expected = {"Condenser"};
        context.checking(new Expectations() {
            {
                allowing(plantStatus).listFailedComponents();
                will(returnValue(componentList));
            }
        });
        assertArrayEquals(expected, model.listFailedComponents());
    }

    @Test
    public void testMoveControlRods() {
        context.checking(new Expectations() {
            {
                oneOf(plantController).moveControlRods(percent(57));
            }
        });
        model.moveControlRods(percent(57));
    }

    @Test
    public void changeValveState() throws Exception {
        context.checking(new Expectations() {
            {
                oneOf(plantController).changeValveState(1, true);
            }
        });
        model.changeValveState(1, true);
    }

    @Test
    public void changePumpState() throws Exception {
        context.checking(new Expectations() {
            {
                oneOf(plantController).changePumpState(1, true);
            }
        });
        model.changePumpState(1, true);
    }

    @Test
    public void repairPump() throws Exception {
        context.checking(new Expectations() {
            {
                oneOf(plantController).repairPump(1);
            }
        });
        model.repairPump(1);
    }

    @Test
    public void repairCondenser() throws Exception {
        context.checking(new Expectations() {{
                oneOf(plantController).repairCondenser();
            }});
        model.repairCondenser();
        
        context.assertIsSatisfied();
    }

    @Test
    public void repairTurbine() throws Exception {
        context.checking(new Expectations() {
            {
                oneOf(plantController).repairTurbine();
            }
        });
        model.repairTurbine();
    }

    @Test
    public void shouldGetControlRodPosition() {
        context.checking(new Expectations() {
            {
                allowing(plantStatus).controlRodPosition();
                will(returnValue(percent(28)));
            }
        });
        assertEquals(percent(28), model.controlRodPosition());
    }

    @Test
    public void shouldGetReactorPressure() {
        context.checking(new Expectations() {
            {
                allowing(plantStatus).reactorPressure();
                will(returnValue(pascals(10)));
            }
        });
        assertEquals(pascals(10), model.reactorPressure());
    }

    @Test
    public void shouldGetReactorTemperature() {
        context.checking(new Expectations() {
            {
                allowing(plantStatus).reactorTemperature();
                will(returnValue(kelvin(1000)));
            }
        });
        assertEquals(kelvin(1000), model.reactorTemperature());
    }

    @Test
    public void shouldGetReactorWaterLevel() {
        context.checking(new Expectations() {
            {
                allowing(plantStatus).reactorWaterLevel();
                will(returnValue(percent(89)));
            }
        });
        assertEquals(percent(89), model.reactorWaterLevel());
    }

    @Test
    public void shouldGetEnergyGenerated() {
        context.checking(new Expectations() {
            {
                allowing(plantStatus).energyGenerated();
                will(returnValue(joules(100000)));
            }
        });
        assertEquals(joules(100000), model.energyGenerated());
    }

    @Test
    public void shouldSetReactorToTurbine() {
        context.checking(new Expectations() {
            {
                oneOf(plantController).setReactorToTurbine(true);
            }
        });
        model.setReactorToTurbine(true);
    }

    @Test
    public void shouldGetReactorToTurbine() {
        context.checking(new Expectations() {
            {
                allowing(plantStatus).getReactorToTurbine();
                will(returnValue(false));
            }
        });
        assertEquals(false, model.getReactorToTurbine());
    }

    @Test
    public void shouldGetCondenserTemperature() {
        context.checking(new Expectations() {
            {
                allowing(plantStatus).condenserTemperature();
                will(returnValue(kelvin(100)));
            }
        });
        assertEquals(kelvin(100), model.condenserTemperature());
    }

    @Test
    public void shouldGetCondenserPressure() {
        context.checking(new Expectations() {
            {
                allowing(plantStatus).condenserPressure();
                will(returnValue(pascals(1000)));
            }
        });
        assertEquals(pascals(1000), plantStatus.condenserPressure());
    }

    @Test
    public void shouldGetCondenserWaterLevel() {
        context.checking(new Expectations() {
            {
                allowing(plantStatus).condenserWaterLevel();
                will(returnValue(percent(56)));
            }
        });
        assertEquals(percent(56), model.condenserWaterLevel());
    }

    @Test
    public void shouldGetReactorMinimumWaterLevel() {
        context.checking(new Expectations() {
            {
                allowing(plantStatus).reactorMinimumWaterLevel();
                will(returnValue(percent(50)));
            }
        });
        assertEquals(percent(50), model.reactorMinimumWaterLevel());
    }

    @Test
    public void failCondenser() {
        context.checking(new Expectations() {
            {
                oneOf(plantController).failCondenser();
            }
        });
        model.failCondenser();
    }

    @Test
    public void failReactor() {
        context.checking(new Expectations() {
            {
                oneOf(plantController).failReactor();
            }
        });
        model.failReactor();
    }

    @Ignore @Test
    public void testStep_int() throws Exception {
    }

    @Test
    public void shouldGetTurbineHasFailed() {
        context.checking(new Expectations() {
            {
                oneOf(plantStatus).turbineHasFailed();
                will(returnValue(true));
            }
        });
        assertEquals(true, model.turbineHasFailed());
    }

    @Ignore @Test
    public void testComponents() {
    }

    
    /*
     * dunno why this failed
     */
    
    @Test
    public void testSoftFailCheck(){
        context.checking(new Expectations() {
            {
                model.softFailCheck(UserCommands.TURNOFF, 2);
            }
        });
    	assertEquals(true, model.softFailCheck(UserCommands.TURNOFF, 2));
    }
     
    
    @Test
    public void testGenerateFailedParameterMove() {
        context.checking(new Expectations() {
            {
              //  model.softFailCheck(UserCommands.TURNOFF, 2);
            }
        });
    	double par = model.generateFailedParameter(UserCommands.MOVE);
		assertTrue((par>=0) && (par<101));
    }
    
    @Test
    public void testGenerateFailedParameterTurnOn() {
        
    	double par = model.generateFailedParameter(UserCommands.TURNON);
		assertTrue((par>0) && (par<3));
    }
    
    @Test
    public void testGenerateFailedParameterTurnOff() {
        
    	double par = model.generateFailedParameter(UserCommands.TURNOFF);
		assertTrue((par>0) && (par<3));
    }
    
    @Test
    public void testGenerateFailedParameterClose() {
        
    	double par = model.generateFailedParameter(UserCommands.TURNOFF);
		assertTrue((par>0) && (par<3));
    }
    
    @Test
    public void testGenerateFailedParameterOpen() {
        context.checking(new Expectations() {{}});
    	double par = model.generateFailedParameter(UserCommands.TURNOFF);
		assertTrue((par>0) && (par<3));
    }

}
