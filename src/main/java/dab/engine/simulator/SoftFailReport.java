package dab.engine.simulator;


/**
 * 
 * Class implementing the failure model of the system.
 * 
 * @author Team Eel
 *
 */

public class SoftFailReport {
    private FailMode failMode;
    private UserCommands targetCommand, actualCommand;
    private double targetParameter, actualParameter;

    public SoftFailReport(){					//Default constructor
        failMode = FailMode.WORKING;
    }

    public SoftFailReport(FailMode failMode, UserCommands targetCommand,double targetParameter){
        if(failMode!=FailMode.INCORRECT){
            this.failMode = failMode;
            this.targetCommand = targetCommand;
            this.targetParameter = targetParameter;
            if(failMode==FailMode.WORKING){
                this.actualCommand = targetCommand;
                this.actualParameter = targetParameter;
            }else{
                this.actualCommand = null;
                this.actualParameter = 0.0;
            }
        }
    }

    public SoftFailReport(FailMode failMode, UserCommands targetCommand, double targetParameter, UserCommands actualCommand, double actualParameter){
        if(failMode==FailMode.INCORRECT){
            this.failMode = failMode;
            this.targetCommand = targetCommand;
            this.targetParameter = targetParameter;
            this.actualCommand = actualCommand;
            this.actualParameter = actualParameter;
        }
    }

    private SoftFailReport(SoftFailReport report){
        this.failMode = report.getFailMode();
        this.targetCommand = report.getTargetCommand();
        this.targetParameter = report.getTargetParameter();
        this.actualCommand = report.getActualCommand();
        this.actualParameter = report.getActualParameter();
    }
    /**
     *
     * @return Software failures report
     */
    public SoftFailReport getCopy(){
        return new SoftFailReport(this);
    }

    /**
     *
     * @return fail mode
     */
    public FailMode getFailMode(){
        return failMode;
    }

    /**
     *
     * @return boolean
     */
    public boolean getFailBool(){
        if(failMode == FailMode.WORKING)
            return true;
        else
            return false;
    }

    /**
     *
     * @return targetCommand
     */
    public UserCommands getTargetCommand(){
        return targetCommand;
    }

    /**
     *
     * @return actual command
     */
    public UserCommands getActualCommand(){
        return actualCommand;
    }

    public double getTargetParameter(){
        return targetParameter;
    }

    public double getActualParameter(){
        return actualParameter;
    }

    /**
     *
     * @return boolean (whether the command has executed)
     */
    public boolean didExecute(){
        if(failMode==FailMode.WORKING)
            return true;
        else
            return false;
    }
}
