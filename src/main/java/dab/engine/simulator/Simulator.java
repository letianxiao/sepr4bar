package dab.engine.simulator;

import dab.engine.persistence.FileSystem;
import dab.engine.persistence.SaveGame;
import dab.engine.utilities.Energy;
import dab.engine.utilities.Percentage;
import dab.engine.utilities.Pressure;
import dab.engine.utilities.Temperature;
import dab.engine.seprphase2.GameOverException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import dab.engine.simulator.views.PumpView;
import dab.engine.simulator.views.ReactorView;
import java.util.HashMap;


/**
 * The simulator API has been encapsulated within a single class - an example of the Facade
 * design pattern. This has simplified interactions between simulator and the TextInterface.
 * To avoid coupling issues associated with the Facade design pattern, the Simulator consists
 * primarily of delegated calls to its sub-components.
 *
 *
 * @author David
 */
public class Simulator implements PlantController, PlantStatus, GameManager {

    private PhysicalModel physicalModel;
    private FailureModel failureModel;
    private String userName;

    public Simulator() {
        physicalModel = new PhysicalModel();
        failureModel = new FailureModel(physicalModel, physicalModel);
        userName = "";
    }

    public ReactorView getReactor() {
        return physicalModel.getReactor();
    }
    
    public ArrayList<PumpView> getPumps() {
        ArrayList<PumpView> pumps = new ArrayList<>();
        for (Pump p : physicalModel.getPumps()) 
            pumps.add(p);
        return pumps;
    }
    
    
    
    /**
     *
     * @param String user Name
     */
    @Override
    public void setUsername(String userName) {
        this.userName = userName;
    }

    /**
     *
     * Save game
     * @throws JsonProcessignException
     */
    @Override
    public void saveGame() throws JsonProcessingException {
        SaveGame saveGame = new SaveGame(physicalModel, failureModel, userName);
        try {
            saveGame.save();
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }
    /**
     *
     * Load a game
     * @param game Number
     */
    @Override
    public void loadGame(int gameNumber) {
        try {
            SaveGame saveGame = SaveGame.load(listGames()[gameNumber]);
            this.physicalModel = saveGame.getPhysicalModel();
            this.failureModel = new FailureModel(physicalModel, physicalModel);
            this.userName = saveGame.getUserName();
        } catch (JsonParseException ex) {
        } catch (IOException ex) {
        }
    }

    /**
     *
     * @return bool failed
     * @param int pump number
     */
    public boolean getPumpFailed(int pumpNum){
        return physicalModel.getPumpFailed(pumpNum);
    }

    /**
     *
     * @return String[] of saved games
     */
    @Override
    public String[] listGames() {
        return FileSystem.listSaveGames();
    }

    /**
     *
     * @return String[] failed components
     */
    @Override
    public String[] listFailedComponents() {
        return failureModel.listFailedComponents();
    }

    /**
     * Execute the calculations for every component
     * @throws GameOverException
     * 
     */
    public void step() throws GameOverException {
        try {
            failureModel.step();
        } catch (GameOverException e) {
            // FIXME: shouldn't it be sufficient to just throw the gameover exception
            throw new GameOverException("Dear " + userName + ",\n\n" +
                    "YOU HAVE FAILED\n\n" +
                    "The reactor vessel has failed catastrophically,\n"+
                    "and everyone within a 100km radius is now either\n " +
                    "dead or dying of radiation\n" +"poisioning.\n\n" +
                    "However, you did successfully generate \n" + failureModel.energyGenerated() +
            "\nof energy before this occurred.");
        }
    }

    public void failStateCheck() {
        failureModel.failStateCheck();
    }
    /**
     *
     * @param Percentage
     */
    @Override
    public void moveControlRods(Percentage extracted) {
        failureModel.moveControlRods(extracted);
    }

    /**
     *
     * @param int valveNumber
     * @param bool
     */
    @Override
    public void changeValveState(int valveNumber, boolean isOpen) throws KeyNotFoundException {
        failureModel.changeValveState(valveNumber, isOpen);
    }

    /**
     *
     * @param int pump number
     * @param bool
     */
    @Override
    public void changePumpState(int pumpNumber, boolean isPumping) throws CannotControlException, KeyNotFoundException {
        failureModel.changePumpState(pumpNumber, isPumping);
    }

    /**
     *
     * @param int pump number
     */
    @Override
    public void repairPump(int pumpNumber) throws KeyNotFoundException, CannotRepairException {
        failureModel.repairPump(pumpNumber);
    }

    /**
     *
     * @throws CannotRepairException
     */
    @Override
    public void repairCondenser() throws CannotRepairException {
        failureModel.repairCondenser();
    }

    /**
     *
     * @throws CannotRepairException
     */
    @Override
    public void repairTurbine() throws CannotRepairException {
        failureModel.repairTurbine();
    }

    /**
     *
     * @return Percentage
     */
    @Override
    public Percentage controlRodPosition() {
        return failureModel.controlRodPosition();
    }

    /**
     *
     * @return boolean if opened
     * @param int valve number
     */
    @Override
    public boolean valveIsOpen(int valveNum) throws KeyNotFoundException{
        return physicalModel.valveIsOpen(valveNum);
    }

    /**
     *@return bool if active
     *@param int pump number
     * 
     */
    @Override
    public boolean pumpIsActive(int pumpNum) throws KeyNotFoundException{
        return physicalModel.pumpIsActive(pumpNum);
    }


    /**
     *
     * @return Pressure
     */
    @Override
    public Pressure reactorPressure() {
        return failureModel.reactorPressure();
    }

    /**
     *
     * @return Temperature
     */
    @Override
    public Temperature reactorTemperature() {
        return failureModel.reactorTemperature();
    }

    /**
     *
     * @return water level percentage
     */
    @Override
    public Percentage reactorWaterLevel() {
        return failureModel.reactorWaterLevel();
    }

    /**
     *
     * @return energy generated
     */
    @Override
    public Energy energyGenerated() {
        return failureModel.energyGenerated();
    }

    @Override
    public void setReactorToTurbine(boolean open) {
        failureModel.setReactorToTurbine(open);
    }

    @Override
    public boolean getReactorToTurbine() {
        return failureModel.getReactorToTurbine();
    }
    /**
     *
     * @return condenser temperature
     */
    @Override
    public Temperature condenserTemperature() {
        return failureModel.condenserTemperature();
    }

    /**
     *
     * @return condenser pressure
     */
    @Override
    public Pressure condenserPressure() {
        return failureModel.condenserPressure();
    }

    /**
     *
     * @return condenser water level percentage
     */
    @Override
    public Percentage condenserWaterLevel() {
        return failureModel.condenserWaterLevel();
    }

    /**
     *
     * @return minimum water level percentage
     */
    @Override
    public Percentage reactorMinimumWaterLevel() {
        return failureModel.reactorMinimumWaterLevel();
    }

    /**
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public void failCondenser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public void failReactor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @throws UnsupportedOperationException
     */
    @Override
    public void step(int steps) throws GameOverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @return boolean
     */
    @Override
    public boolean turbineHasFailed() {
        return failureModel.turbineHasFailed();
    }

    /**
     *
     * @return arraylist with failalbe components
     */
    @Override
    public ArrayList<FailableComponent> components() {
        return failureModel.components();
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @return Software failures report
     */
    public SoftFailReport getSoftFailReport(){
        return failureModel.getSoftFailReport();
    }

    /**
     *
     * @return player's name
     */
    @Override
    public String getUsername() {
        return userName;
    }
}
