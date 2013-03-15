package dab.engine.simulator;

import static dab.engine.utilities.Units.percent;
import dab.engine.utilities.Energy;
import dab.engine.utilities.Percentage;
import dab.engine.utilities.Pressure;
import dab.engine.utilities.Temperature;
import dab.engine.seprphase2.GameOverException;

import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Manages software and hardware failures.
 *
 * The FailureModel is responsible for inducing software and hardware failures in the plant.
 *
 * Software failures are currently unimplemented; however, as all calls to the PhysicalModel are delegated through the
 * FailureModel, it is easy to alter this delegation in arbitrary ways to simulate control and/or reporting failures.
 *
 * Hardware failures are determined by inspecting the PhysicalModel to determine the status, wear, and operating
 * conditions of the various components, and then instructing the PhysicalModel to mark certain components as having
 * failed.
 *
 * Failures are constrained to one per timestep, which is enabled by the delegation of the step() method through the
 * FailureModel.
 *
 * @author Marius Dumitrescu
 */
public class FailureModel implements PlantController, PlantStatus {

    @JsonProperty
    PlantController controller;
    @JsonProperty
    PlantStatus status;
    private Random failChance = new Random();
    @JsonProperty
    private int numberOfTimesWaterLevelIsTooLow;
    private static int reactorOverheatThreshold = 8;
    private final Pressure condenserMaxPressure = new Pressure(30662500);
    private SoftFailReport lastFailReport = new SoftFailReport();
    private ArrayList<FailableComponent> repairList = new ArrayList<FailableComponent>();

    private FailureModel() {
    }

    public FailureModel(PlantController plantController,
            PlantStatus plantStatus) {
        this.controller = plantController;
        this.status = plantStatus;
    }

    /**
     * Step the PhysicalModel and determine any failures.
     *
     * Also implements reactor safety rules.
     *
     */
    public void step() throws GameOverException {
        controller.step(1);
        failStateCheck();
        checkReactorWaterLevel();
        checkCondenserPressure();
        checkTurbineFailure();
    }
    
    
    @Override
    public void step(int i) throws GameOverException {
        throw new RuntimeException("Oups... shouldn't call this one!");
        //controller.step(i);
    }

    /**
     * Method to determine failures
     *
     */
    public void failStateCheck() {
        ArrayList<FailableComponent> components = status.components();
        int failValue = failChance.nextInt(5000);  //A component that is 100% wear will have a 1 in 50 chance of failing
        int componentsFailChance = 0;
        for (int i = 0; i < components.size(); i++) {
            componentsFailChance += components.get(i).wear().points() / components.size();
            if (componentsFailChance > failValue) {
                components.get(i).fail();
                
                break; //We only want to induce one hardware failure! Break here.
            }

        }
    }

    @Override
    public String[] listFailedComponents() {
        return status.listFailedComponents();
    }

    /**
     * @return String[] list of repairing components
     */
    @Override
    public String[] listRepairingComponents(){
        return status.listRepairingComponents();
    }

    @Override
    public void moveControlRods(Percentage extracted) {
        if(softFailCheck(UserCommands.MOVE, extracted.points()))
            controller.moveControlRods(extracted);
    }

    @Override
    public void changeValveState(int valveNumber, boolean isOpen) throws KeyNotFoundException {
        UserCommands com;
        if(isOpen) com=UserCommands.OPEN; else com=UserCommands.CLOSE;
        if(softFailCheck(com, valveNumber))
            controller.changeValveState(valveNumber, isOpen);
    }

    @Override
    public void changePumpState(int pumpNumber, boolean isPumping) throws CannotControlException, KeyNotFoundException {
        UserCommands com;
        if(isPumping) com=UserCommands.TURNON; else com=UserCommands.TURNOFF;
        if(softFailCheck(com, pumpNumber))
            controller.changePumpState(pumpNumber, isPumping);
    }

    @Override
    public void repairPump(int pumpNumber) throws KeyNotFoundException, CannotRepairException {
        controller.repairPump(pumpNumber);
    }

    @Override
    public void repairCondenser() throws CannotRepairException {
        controller.repairCondenser();
    }

    @Override
    public void repairTurbine() throws CannotRepairException {
        controller.repairTurbine();
    }

    @Override
    public Percentage controlRodPosition() {
        return status.controlRodPosition();
    }

    @Override
    public boolean valveIsOpen(int valveNum) throws KeyNotFoundException{
        return status.valveIsOpen(valveNum);
    }

    @Override
    public boolean pumpIsActive(int pumpNum) throws KeyNotFoundException{
        return status.pumpIsActive(pumpNum);
    }

    @Override
    public Pressure reactorPressure() {
        return status.reactorPressure();
    }

    @Override
    public Temperature reactorTemperature() {
        return status.reactorTemperature();
    }

    @Override
    public Percentage reactorWaterLevel() {
        return status.reactorWaterLevel();
    }

    @Override
    public Energy energyGenerated() {
        return status.energyGenerated();
    }

    @Override
    public void setReactorToTurbine(boolean open) {
        controller.setReactorToTurbine(open);
    }

    @Override
    public boolean getReactorToTurbine() {
        return status.getReactorToTurbine();
    }

    @Override
    public Temperature condenserTemperature() {
        return status.condenserTemperature();
    }

    @Override
    public Pressure condenserPressure() {
        return status.condenserPressure();
    }

    @Override
    public Percentage condenserWaterLevel() {
        return status.condenserWaterLevel();
    }

    @Override
    public Percentage reactorMinimumWaterLevel() {
        return status.reactorMinimumWaterLevel();
    }

    @Override
    public void failCondenser() {
        controller.failCondenser();
    }

    @Override
    public void failReactor() {
        controller.failReactor();
    }


    @Override
    public boolean turbineHasFailed() {
        return status.turbineHasFailed();
    }

    @Override
    public ArrayList<FailableComponent> components() {
        return status.components();
    }

    /**
     * Fail reactor according to water level
     */
    private void checkReactorWaterLevel() {
        if (status.reactorWaterLevel().points() < status.reactorMinimumWaterLevel().points()) {
            System.out.println(numberOfTimesWaterLevelIsTooLow);
            numberOfTimesWaterLevelIsTooLow += 1;
            if (numberOfTimesWaterLevelIsTooLow > reactorOverheatThreshold) {
                controller.failReactor();
            }
        } else {
            numberOfTimesWaterLevelIsTooLow = 0;
        }
    }

    /**
     * Fail condenser if pressure too high
     */
    private void checkCondenserPressure() {
        if (status.condenserPressure().greaterThan(condenserMaxPressure)) {
            controller.failCondenser();
        }
    }
    /**
     * Set control rods to 0 if turbine's failed
     */
    private void checkTurbineFailure() {
        if (status.turbineHasFailed()) {
            controller.moveControlRods(percent(0));
        }
    }
    /**
     * @param UserCommand
     * @param double target
     * @return boolean (whether a software failure has occured)
     */
    public boolean softFailCheck(UserCommands targetCommand, double targetParameter){
        double failRoll = Math.random();
        if (failRoll<=0.2){ //chance of any failure (incorrect fail = this - unresponsive fail chance)
            if(failRoll<=0.05){ //chance of unresponsive failure
                //Unresponive commands do nothing
                lastFailReport = new SoftFailReport(FailMode.UNRESPONSIVE, targetCommand, targetParameter);
                return false;
            }
            //Incorrect commands behave as another command
            UserCommands actualCommand = UserCommands.randomCommand();
            double actualParameter = generateFailedParameter(actualCommand);
            doFailedCommand(actualCommand, actualParameter);
            lastFailReport = new SoftFailReport(FailMode.INCORRECT, targetCommand, targetParameter, actualCommand, actualParameter);
            return false;
        } else {
            //Working commands behave as intended
            lastFailReport = new SoftFailReport(FailMode.WORKING, targetCommand, targetParameter);
            return true;
        }
    }

    /**
     * @param UserCommand
     * @return double (a random failure for each command)
     */
    public double generateFailedParameter(UserCommands command){
        Random rand = new Random();
        switch(command) {
            case TURNON:
            case TURNOFF:
                System.out.println(rand.nextInt(2)+1);
                return rand.nextInt(2)+1;
            case OPEN:
            case CLOSE:
                return rand.nextInt(2)+1;
            case MOVE:
                return rand.nextInt(51);
            default:
                return 0;
        }
    }
    /**
     * @param UserCommand
     * @param double paramater
     * Execute the failed command to the command specified with the parameter specified
     */
    public void doFailedCommand(UserCommands command, double parameter){
        try{
            switch(command){
                case TURNON:
                    controller.changePumpState((int)parameter, true);
                    break;
                case TURNOFF:
                    controller.changePumpState((int)parameter, false);
                    break;
                case OPEN:
                    controller.changeValveState((int)parameter, true);
                    break;
                case CLOSE:
                    controller.changeValveState((int)parameter, false);
                    break;
                case MOVE:
                    try{
                        controller.moveControlRods(new Percentage(parameter));
                    }catch(IllegalArgumentException e){
                        e.printStackTrace();
                    }
                    break;
            }
        }catch(KeyNotFoundException e){
            e.printStackTrace();
        }catch(CannotControlException e){
            //TODO:Might need to do something more here, not sure.
        }
    }
    /**
     * @return Software failure report
     */
    public SoftFailReport getSoftFailReport(){
        return lastFailReport.getCopy();
    }


}

