package dab.engine.simulator;

import dab.engine.utilities.Energy;
import dab.engine.utilities.Percentage;
import dab.engine.utilities.Pressure;
import dab.engine.utilities.Temperature;

import java.util.ArrayList;

/**
 * Interface concerning Plant component status.
 * @author David
 */
public interface PlantStatus {

    public Percentage controlRodPosition();
    
    public Pressure reactorPressure();
    
    public Temperature reactorTemperature();
    
    public Percentage reactorWaterLevel();

    public Energy energyGenerated();

    
    public boolean getReactorToTurbine();

    public Temperature condenserTemperature();

    public Pressure condenserPressure();

    public Percentage condenserWaterLevel();

    public Percentage reactorMinimumWaterLevel();

    public String[] listFailedComponents();

    public boolean turbineHasFailed();

    public ArrayList<FailableComponent> components();
    
    public boolean pumpIsActive(int pumpNum) throws KeyNotFoundException;
    
    public boolean valveIsOpen(int valveNum) throws KeyNotFoundException;
}
