package dab.engine.simulator;

import dab.engine.utilities.*;
import static dab.engine.utilities.Units.joules;
import dab.engine.seprphase2.GameOverException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 *
 * Physical simulation of the the nuclear plant is encapsulated within the
 * PhysicalModel class. The components within the PhysicalModel use simplified
 * but generally physically accurate models to calculate the temperature,
 * pressure, flow rate and energy transfer accordingly. The PhysicalModel is
 * also designed with the Pipes and Filters architectural style in mind and
 * implements the same PlantController and PlantStatus interfaces as the
 * Simulator and FailureModel. Each component has a step() method which
 * simulates the properties of that component over a short time.
 *
 * Water and steam is transferred between the Reactor, Turbine and Condenser
 * through Pumps and Connections.
 *
 * TODO: refactor turbine to reactor, heatsink to blabla code TODO2: bitch to
 * David about using json and failing massively. TODO3: it's still buggy, find
 * what that is all about...
 *
 * @author Marius
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class PhysicalModel implements PlantController, PlantStatus {

    @JsonProperty
    private Reactor reactor = new Reactor();
    @JsonProperty
    private Turbine turbine = new Turbine();
    @JsonProperty
    private Condenser condenser = new Condenser();
    @JsonProperty
    private Energy energyGenerated = joules(0);
    @JsonProperty
    private String username;
    @JsonProperty
    private ArrayList<Pump> allPumps;
    @JsonProperty
    private ArrayList<Connection> allConnections;
    @JsonProperty
    private HeatSink heatSink;

    /**
     * @param heatSink Intialising the Heat sink
     * @param allPumps Intialising all pumps by maping them by using a Hash map
     * @param allConnections Intialising the connections by mapping them by
     * using a Hash Map
     *
     */
    public PhysicalModel() {

        Connection reactorToTurbine;
        Connection turbineToCondenser;
        Pump condenserToReactor;
        Pump heatsinkToCondenser;

        heatSink = new HeatSink();

        allPumps = new ArrayList<>();
        allConnections = new ArrayList<>();

        reactorToTurbine = new Connection(reactor.outputPort(), turbine.inputPort(), 0.05);
        turbineToCondenser = new Connection(turbine.outputPort(), condenser.inputPort(), 0.05);


        condenserToReactor = new Pump(condenser.outputPort(), reactor.inputPort());
        heatsinkToCondenser = new Pump(heatSink.outputPort(), condenser.coolantInputPort());


        allConnections.add(reactorToTurbine);
        allConnections.add(turbineToCondenser);

        allPumps.add(condenserToReactor);
        allPumps.add(heatsinkToCondenser);

    }

    public Reactor getReactor() {
        return reactor;
    }

    public Condenser getCondenser() {
        return condenser;
    }

    public Turbine getTurbine() {
        return turbine;
    }

    public Pump getPump(int pumpNumber) {
        int actualIndex = pumpNumber - 1; // internally valves are stored from 0
        return allPumps.get(actualIndex);
    }

    public ArrayList<Pump> getPumps() {
        return allPumps;
    }

    public Valve getValve(int valveNumber) throws KeyNotFoundException {
        int actualIndex = valveNumber - 1; // internally valves are stored from 0
        return allConnections.get(actualIndex);
    }

    @Override
    public String[] listFailedComponents() {
        ArrayList<String> out = new ArrayList<>();

        /*
         * Iterate through all pumps to get their IDs
         */
        for (int i = 0; i < allPumps.size(); ++i) {
            Pump current = allPumps.get(i);
            if (current.hasFailed()) {
                out.add("Pump " + (i + 1));
            }
        }

        /*
         * Check if reactor failed
         */
        if (reactor.hasFailed()) {
            out.add("Reactor");
        }

        /*
         * Check if turbine failed
         */
        if (turbine.hasFailed()) {
            out.add("Turbine");
        }

        /*
         * Check if condenser failed
         */
        if (condenser.hasFailed()) {
            out.add("Condenser");
        }

        return out.toArray(new String[out.size()]);

    }

    /**
     *
     * @param steps Time step of the game
     */
    @Override
    public void step(int steps) throws GameOverException {
        for (int i = 0; i < steps; i++) {
            reactor.step();
            turbine.step();
            condenser.step();
            energyGenerated = joules(energyGenerated.inJoules() + turbine.outputPower());

            getReactorToTurbineConnection().step();
            getTurbineToCondenser().step();
            getCondenserToReactor().step();
            getHeatsinkToCondenser().step();
        }
    }

    protected Connection getReactorToTurbineConnection() {
        return allConnections.get(0);
    }

    protected Connection getTurbineToCondenser() {
        return allConnections.get(1);
    }

    /**
     *
     * @param percent % to move control rods
     */
    @Override
    public void moveControlRods(Percentage percent) {
        reactor.moveControlRods(percent);
    }

    /**
     *
     * @return Temperature
     */
    @Override
    public Temperature reactorTemperature() {
        return reactor.temperature();
    }

    /**
     *
     * @return minimum water mass for the reactor
     */
    public Mass reactorMinimumWaterMass() {
        return reactor.minimumWaterMass();
    }

    /**
     *
     * @return maximum water mass for the reactor
     */
    public Mass reactorMaximumWaterMass() {
        return reactor.maximumWaterMass();
    }

    @Override
    public Percentage reactorMinimumWaterLevel() {
        return reactor.minimumWaterLevel();
    }

    @Override
    public void failReactor() {
        reactor.fail();
    }

    @Override
    public void failCondenser() {
        condenser.fail();
    }

    /**
     *
     * @return the energy generated
     */
    @Override
    public Energy energyGenerated() {
        return energyGenerated;
    }

    /**
     *
     * @return control rods position
     */
    @Override
    public Percentage controlRodPosition() {
        return reactor.controlRodPosition();
    }

    /**
     *
     * @return boolean if a valve is opened
     */
    @Override
    public boolean valveIsOpen(int valveNum) throws KeyNotFoundException {
        return getValve(valveNum).getOpen();
    }

    /**
     *
     * @return boolean if pump is active
     */
    @Override
    public boolean pumpIsActive(int pumpNum) throws KeyNotFoundException {
        return getPump(pumpNum).getStatus();

    }

    /**
     *
     * @return reactor pressure
     */
    @Override
    public Pressure reactorPressure() {
        return reactor.pressure();
    }

    /**
     *
     * @return water leavel of the reactor
     */
    @Override
    public Percentage reactorWaterLevel() {
        return reactor.waterLevel();
    }

    /**
     *
     * @param open To check whether the valve connecting reactor to turbine is
     * open or not
     */
    @Override
    public void setReactorToTurbine(boolean open) {
        allConnections.get(0).setOpen(open);
    }

    /**
     *
     * @return boolean
     */
    @Override
    public boolean getReactorToTurbine() {
        return allConnections.get(0).getOpen();
    }

    /**
     *
     * @return a list of components that may fail
     */
    @Override
    public ArrayList<FailableComponent> components() {
        ArrayList<FailableComponent> c = new ArrayList<FailableComponent>();
        c.add(0, turbine);
        c.add(1, reactor);
        c.add(2, condenser);
        c.add(3, getCondenserToReactor());
        c.add(4, getHeatsinkToCondenser());
        return c;
    }

    protected Pump getCondenserToReactor() {
        return allPumps.get(0);
    }

    protected Pump getHeatsinkToCondenser() {
        return allPumps.get(1);
    }

    /**
     *
     * Change a the state of a valve
     *
     * @param valve int
     * @param bool
     */
    @Override
    public void changeValveState(int valveNumber, boolean isOpen) throws KeyNotFoundException {
        getValve(valveNumber).setOpen(isOpen);
    }

    /**
     *
     * Change pump state
     *
     * @param pump int
     * @param bool
     */
    @Override
    public void changePumpState(int pumpNumber, boolean isPumping) throws CannotControlException, KeyNotFoundException {
        Pump pump = getPump(pumpNumber);

        if (pump.hasFailed()) {
            throw new CannotControlException("Pump " + pumpNumber + " is failed");
        }

        pump.setStatus(isPumping);
    }

    /**
     *
     * Repair a pump
     *
     * @param pump number
     */
    @Override
    public void repairPump(int pumpNumber) throws KeyNotFoundException, CannotRepairException {
        getPump(pumpNumber).repair();

        /*if (allPumps.containsKey(pumpNumber)) {
         allPumps.get(pumpNumber).repair();


         //These shouldn't need to be changed
         //allPumps.get(pumpNumber).setStatus(true);
         //allPumps.get(pumpNumber).setCapacity(kilograms(3));
         //allPumps.get(pumpNumber).stepWear(new Percentage(0));

         } else {
         throw new KeyNotFoundException("Pump " + pumpNumber + " does not exist");
         }*/
    }

    @Override
    public void repairCondenser() throws CannotRepairException {
        condenser.repair();
    }

    @Override
    public void repairTurbine() throws CannotRepairException {
        turbine.repair();
    }

    /**
     *
     * @return Temperature
     */
    @Override
    public Temperature condenserTemperature() {
        return condenser.getTemperature();
    }

    /**
     *
     * @return Pressure
     */
    @Override
    public Pressure condenserPressure() {
        return condenser.getPressure();
    }

    /**
     *
     * @return water level percentage
     */
    @Override
    public Percentage condenserWaterLevel() {
        return condenser.getWaterLevel();
    }

    /**
     *
     * @return bool fail
     */
    @Override
    public boolean turbineHasFailed() {
        return turbine.hasFailed();
    }

    /**
     *
     * @return bool has pump failed
     * @param int pump Number
     */
    public boolean getPumpFailed(int pumpNumber) {
        return getPump(pumpNumber).hasFailed();
    }

    /**
     *
     * Get status of a pump
     *
     * @return bool
     * @param int pump Number
     */
    public boolean getPumpStatus(int pumpNumber) {
        return getPump(pumpNumber).getStatus();
    }
}
