/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.robovikings.competition.frc;


import com.robovikings.competition.LCD.BootScreen;
import com.robovikings.competition.LCD.BootScreenLCD;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot  implements Constants{
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    CANJaguar jagLeft1, 
              jagLeft2, 
              jagRight1, 
              jagRight2;
    RobotDrive robotDrive;
    private Climber climbingSubSystem;
    private robovikingStick stickDrive, joystickRight, trainerStick, bifrost;
    
    private Solenoid solFallOver;
    private DoubleSolenoidLatchType pistonStrokeLimitinator;
            Solenoid pushInator; 
//            Solenoid dumpinator;
    Relay shovinator;
    double currentSpeed = 0.00;
    double voltageRampRateHighGearSPEEDUP = 0.085d;
    double voltageRampRateHighGearSLOWDOWN = 0.022d;
    double voltageRampRateLowGearSPEEDUP = 0.08d;
    double voltageRampRateLowGearSLOWDOWN = 0.03;
    int antiLockBreakCount = 0;
    double driveThreshold = 0.15;
    double y, desX, desY;
    boolean isHalfwayExtendingPiston = false;
    DigitalInput pistonPhotoEye;
    boolean normalShiftState = SuperShifters.LOW;
    DriverStation ds = DriverStation.getInstance();
    SuperShifters vexPRO;
    Spinfusor shooter;
    Encoder leftDriveEncoder, rightDriveEncoder, climbEncoder;
    double climberSprocketRatio = 1/1.3;
    double climberScrewRatio = 1/1;
    double climberEncoderResolution = 250;
    BootScreen whoUnpluggedMrCullensWires;
    Solenoid holdTheDiscs;
    boolean normalAutonSpot = true;
    boolean isSlowGear = false;
    boolean pressedSlowGear = false;
    Dashboard DB = DriverStation.getInstance().getDashboardPackerLow();
    public static Solenoid LEDs;
    public void robotInit() {
        BootScreenLCD.scanI2C();
        whoUnpluggedMrCullensWires = new BootScreen();
        if (!BootScreenLCD.DM.getI2C(64).addressOnly())
        {
            try
            {
                whoUnpluggedMrCullensWires = new BootScreenLCD();
                whoUnpluggedMrCullensWires.setColor(false, true, false);
                whoUnpluggedMrCullensWires.setText("BOOTING...", 1);
                whoUnpluggedMrCullensWires.setText("WAFFLES", 2);
            }
            catch (Exception e)
            {
                //The screen isn't plugged in...
                //Ain't that a shame...
            }
        }
        try {
            stickDrive = new robovikingStick(1);
            joystickRight = new robovikingStick(2);
            trainerStick = new robovikingStick(3);
            bifrost = new robovikingStick(4);
        }
        catch (Exception e)
        {
            whoUnpluggedMrCullensWires.setError("CHECK JOYSTICKS", 1);
        }
        boolean success = false;
        int count = 0;
        while (!success)
        {
            success = true;
            if (jagLeft1==null)
            {
                try {
                    jagLeft1 = new CANJaguar(driveFrontLeftJag);
                } catch (Exception e)
                {
                    whoUnpluggedMrCullensWires.setError("CAN "+driveFrontLeftJag, 1);
                    success = false;
                    System.out.println(driveFrontLeftJag);
                }
            }
            if (jagLeft2==null)
            {
                try {
                    jagLeft2 = new CANJaguar(driveRearLeftJag);
                    } catch (Exception e)
                {
                    whoUnpluggedMrCullensWires.setError("CAN "+driveRearLeftJag, 1);
                    success = false;
                    System.out.println(driveRearLeftJag);
                }
            }
            if (jagRight1==null)
            {
                try {
                    jagRight1 = new CANJaguar(driveFrontRightJag);
                    } catch (Exception e)
                {
                    whoUnpluggedMrCullensWires.setError("CAN "+driveFrontRightJag, 1);
                    success = false;
                    System.out.println(driveFrontRightJag);
                }
            }
            if (jagRight2==null)
            {
                try {
                    jagRight2 = new CANJaguar(driveRearRightJag);
                    } catch (Exception e)
                {
                    whoUnpluggedMrCullensWires.setError("CAN "+driveRearRightJag, 1);
                    success = false;
                    System.out.println(driveRearRightJag);
                }
            }
            if (!success)
            {
                System.out.println("CAN DIDN'T WORK! TRYING AGAIN! \n RETRING FOR THE "+(++count)+"th TIME!");
                
            }
        }
        try
        {
            climbEncoder = new Encoder(climbEncoderChA_DI,climbEncoderChB_DI);
            
            climbEncoder.setDistancePerPulse(climberSprocketRatio*climberScrewRatio*climberEncoderResolution);
        }
         catch (Exception e)
         {
             whoUnpluggedMrCullensWires.setError("Check ClimbEncoder", 1);
         }
        try
        {
            vexPRO = new SuperShifters(shiftSolenoid);
        }
        catch (Exception e)
        {
            whoUnpluggedMrCullensWires.setError("Check Shifters", 1);
        }
  //          shovinator = new Relay(dumpRY);         //UNUSED
//            dumpinator = new Solenoid(dumpSolenoid);
            try {
                pistonPhotoEye = new DigitalInput(pistonPhotoEyeDI);
                if (!pistonPhotoEye.get())
                {
                    throw new Exception("WHY?!?");
                    
                }
            }
            catch (Exception e)
            {
             whoUnpluggedMrCullensWires.setError("Check Photoeye", 1);   
              e.printStackTrace();
            }
            try
            {
            climbingSubSystem = Climber.getInstance(climbLeftJag, // lMotorChannel 
                                                    climbRightJag,  // rMotorChannel
                                                    compressorRY,  // compressorRelayChannel
                                                    compressorDI,  // compressorSwitchChannel
                                                    fangsSolenoid,  // hookPistonChannel1
                                                    0,  // hookPistonChannel2 DEPRECATED
                                                    climbUp1DI,  // lTopLimChannel   // was 8
                                                    climbDown1DI,  // lBottomLimChannel
                                                    climbUp2DI,  // rTopLimChannel
                                                    climbDown2DI); // rBottomLimChannel
            }
            catch (Exception e)
            {
             whoUnpluggedMrCullensWires.setError("Check Climber", 1);   
              e.printStackTrace();
            }
            //climbingSubSystem = Climber.getInstance(10 , 9 , 1 , 4 , 2, 1, 8, 9);
            try
            {
            solFallOver = new Solenoid(tipSolenoid);
            //pistonStrokeLimitinator = new DoubleSolenoidLatchType(new Solenoid(limit1Solenoid),new Solenoid(limit2Solenoid));
            holdTheDiscs = new Solenoid(hopperSolenoid);
          //  pushInator = new Solenoid(bigpistonSolenoid);
            LEDs = new Solenoid(8);
            }
            catch (Exception e)
            {
             whoUnpluggedMrCullensWires.setError("Check Pneumatics", 1);   
              e.printStackTrace();
            }
            try
            {
            climbingSubSystem.runCompressor(true);
            }
            catch (Exception e)
            {
             whoUnpluggedMrCullensWires.setError("Check Compressor", 1);
             e.printStackTrace();
            }
            try{
            shooter = new Spinfusor();
        } catch (Exception ex) {
            whoUnpluggedMrCullensWires.setError("Check Shooter", 1);   
            ex.printStackTrace();
        }
        robotDrive = new RobotDrive(jagLeft1,jagLeft2,jagRight1,jagRight2);
       
//        robotDrive = new RobotDrive(jagLeft1, jagRight1);
        robotDrive.setExpiration(1000);
       // pistonStrokeLimitinator.set(true);
        whoUnpluggedMrCullensWires.setColor(false, false, true);
        whoUnpluggedMrCullensWires.setText("A OK...", 1);
    }
// AUTON CODE
    byte autonStep = 0;
    long lastAutonTime = 0;
    final int AM_DONOTHING = 1, AM_SHOOT = 2;
    final int  AS_INIT = 0, AS_CHARGE = 1, AS_RETRACT = 2,  AS_SHOOT1 = 3, AS_WAIT1 = 4, AS_SHOOT2 = 5,  AS_WAIT2 = 6, AS_SHOOT3 = 7, AS_WAIT3 = 8, AS_KEEPFIRING = 9, AS_WAIT4 = 10, AS_DRIVE = 11, AS_WAITSTOPPED = 12, AS_TURN = 13,  AS_STOP = 14;
    int selectedAutonMode = AM_SHOOT;
    int timeBeforeFirstShotOfDoom = 2000;
    int timeBetweenEachShot = 2000;
            
            
    public void autonomousInit()
    {
        lastAutonTime = System.currentTimeMillis();
        autonStep = AS_INIT;
        selectedAutonMode = AM_SHOOT;
        //selectedAutonMode = (int)SmartDashboard.getNumber("AUTONMODE", AM_SHOOT);
        //SmartDashboard.putBoolean("AUTON?", true);
        timeBeforeFirstShotOfDoom = (int)(SmartDashboard.getNumber("SF", 3)*1000);
        timeBetweenEachShot = (int)(SmartDashboard.getNumber("SD", 2)*1000);
        normalAutonSpot = (SmartDashboard.getBoolean("SA", true));
        desY = 0;
        desX = 0;
        shotCount = 0;
    }
    int shotCount = 0;
    public void autonomousPeriodic()
    {
        exchangeData();
        switch (selectedAutonMode)
        {
            default:
            case AM_DONOTHING:
                
            break;
            case AM_SHOOT:
                switch (autonStep)
                {
                         default:
                    case AS_INIT://Reset the timer and such
                        desY = 0; desX = 0;//STOP DRIVING
                        this.vexPRO.shiftLow();
                        holdTheDiscs.set(true);
                        nextAutonStep();
                    break;
                    case AS_CHARGE://Spin the shooter wheel for seven seconds
                        holdTheDiscs.set(true);
                        shooter.chargeShooter(.9d);
                        if (getAutonTime()>timeBeforeFirstShotOfDoom)
                        {
                            holdTheDiscs.set(false);
                            nextAutonStep();
                        }
                    break;
                        case AS_RETRACT://Spin the shooter wheel for seven seconds
                        holdTheDiscs.set(false);
                        if (getAutonTime()>200)
                        {
                            nextAutonStep();
                        }
                    break;
                    case AS_SHOOT1://Shoot disk one
                    case AS_SHOOT2://Shoot disk two
                    case AS_SHOOT3://Shoot disk three
                        case AS_KEEPFIRING://Shoot disk three
                        holdTheDiscs.set(false);
                        shooter.shoot();
                        nextAutonStep();
                    break;
                    case AS_WAIT1:
                    case AS_WAIT2:
                        if (getAutonTime()>timeBetweenEachShot)
                        {
                            nextAutonStep();
                        }
                    break;
                    case AS_WAIT3:
                        if (getAutonTime()>250)
                        {
                            nextAutonStep();
                        }
                    break;
                    case AS_WAIT4:
                        if (getAutonTime()>500)
                        {
                            
                            nextAutonStep();
                            if (shotCount++<3)
                            {
                                autonStep=AS_KEEPFIRING;
                            }
                        }
                    break;
                    case AS_DRIVE://Drive backwards
                        desY = (0.65)*(normalAutonSpot?1:-1);//Reverse
                        shooter.chargeShooter(0.0d);
                        climbingSubSystem.moveClimbHooks(Climber.UP);//Move hooks up
                        
                        if (getAutonTime()>=3000)
                        {
                            nextAutonStep();
                        }
                    break;
                    case AS_WAITSTOPPED:
                        desY = 0;
                        climbingSubSystem.moveClimbHooks(0);//Move hooks up
                        if (getAutonTime()>=500)
                        {
                            nextAutonStep();
                        }
                    break;
                    case AS_TURN://TURN
                        climbingSubSystem.moveClimbHooks(Climber.DOWN);
                        desX = -1.0;//TURN RIGHT
                        desY = 0.0;//STOP FORWARDING
                        if (getAutonTime()>=900)
                        {
                            nextAutonStep();
                        }
                      //  System.out.println("TURN: "+desX);
                    break;
                    case AS_STOP:
                        climbingSubSystem.moveClimbHooks(0.0);
                        desX = 0.0;//TURN RIGHT
                        endOfAutonTurn = 100;//Set up teleop autodrive
                        this.vexPRO.shiftHigh();//SHIFT HIGH!
                    break;
                }
            break;
        } 
        drive();
    }
    public long getAutonTime()
    {
        return System.currentTimeMillis()-lastAutonTime;
    }
    public void nextAutonStep()
        {
            autonStep++;
            lastAutonTime = System.currentTimeMillis();
        }
    public void drive()
    {
        if (desY<y)
            {
                y-=vexPRO.getShift()?(!sign(desY,y)?voltageRampRateHighGearSPEEDUP:voltageRampRateHighGearSLOWDOWN):(!sign(desY,y)?voltageRampRateLowGearSLOWDOWN:voltageRampRateLowGearSPEEDUP);
                if (desY>y)
                {
                    y = desY;
                }
            }

            if (desY>y)
            {
               y+=vexPRO.getShift()?(!sign(desY,y)?voltageRampRateHighGearSPEEDUP:voltageRampRateHighGearSLOWDOWN):(!sign(desY,y)?voltageRampRateLowGearSLOWDOWN:voltageRampRateLowGearSPEEDUP);
               if (desY<y)
                {
                    y = desY;
                }
            }
            if (Math.sqrt((desX*desX)+(y*y))>=driveThreshold)
            {
                robotDrive.arcadeDrive(y,desX);
            }
            else
            {
                robotDrive.arcadeDrive(0,0);
            }
    }
 //TELEOP CODE   
    public void teleopInit()
    {
        //pistonStrokeLimitinator.set(true);
       
        playBall = true;
        tenSeconds = true;
        oneMinute = true;
        gameOver = true;
        desY = 0;
        this.vexPRO.shiftHigh();//SHIFT HIGH!
        y = 0;
        //SmartDashboard.putString("Music", SmartDashboard.getString("Music","")+"s");
        //SmartDashboard.putBoolean("AUTON?", false);
        
        climbingSubSystem.moveClimbHooks(0.0);
    }
    /**
     * This function is called periodically during operator control
     */
    boolean playBall = true;
    boolean tenSeconds = true;
    boolean oneMinute = true;
    boolean gameOver = true;
    public int endOfAutonTurn = 0;
    public void teleopPeriodic() {
        doStuff();
    }
        //SmartDashboard.putNumber("TIME",(2*60)-ds.getMatchTime());
        /*if (ds.getMatchTime()>=60&&oneMinute)
        {
            SmartDashboard.putString("Music", SmartDashboard.getString("Music","")+"o");
            oneMinute = false;
        }
        if (ds.getMatchTime()>=110&&tenSeconds)
        {
            SmartDashboard.putString("Music", SmartDashboard.getString("Music","")+"t");
            tenSeconds = false;
        }
        if (ds.getMatchTime()>=119&&gameOver)
        {
            SmartDashboard.putString("Music", SmartDashboard.getString("Music","")+"g");
            gameOver = false;
        }*/
    public void doStuff()
    {
        exchangeData();
         //LEDs.set(bifrost.getRawButton(9));
        if (getStartClimb())
        {
            climbInit();
        }
        if (getClimb())
        {
            climbToTheTippyTop();
        }
        else
        {
            
            if (Math.abs(getTurn())>=driveThreshold)
            {
                desX = getTurn();
                desX = ((desX*desX*desX)*.85);
                endOfAutonTurn = 0;
            }
            else
            {
                desX = 0;
            }
            if (Math.abs(getDrive())>driveThreshold)
            {
                desY = getDrive();
                desY = (0.6d*desY)+(0.4d*(desY*desY*desY));
                antiLockBreakCount = 0;
                endOfAutonTurn = 0;
                    //System.out.println(getDrive());
            }
            else
            {
                if (antiLockBreakCount<100)
                {
                    antiLockBreakCount++;
                    if (antiLockBreakCount==0)
                    {
                        try {
                            //jagLeft1.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//                            jagLeft2.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                           // jagRight1.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//                            jagRight2.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                else
                {
                    if (antiLockBreakCount==100)
                    {
                        antiLockBreakCount++;
                        try {
                       //     jagLeft1.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//                            jagLeft2.configNeutralMode(CANJaguar.NeutralMode.kBrake);
                         //   jagRight1.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//                            jagRight2.configNeutralMode(CANJaguar.NeutralMode.kBrake);             
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                }
                desY = 0;
                
            }
            if (endOfAutonTurn>0)
                {
                    endOfAutonTurn-=1;
                    desY = -1.0;
                  //  System.out.println("HAX");
                }
            drive();
            if (!robot.getAutoclimb())
            {
                solFallOver.set(getFall());
            }
            if (getFull())
            {
                extendFull();
            }
            try {
  
                        climbingSubSystem.changeHookState(getFangs());

                
                /*else {
                    climbingSubSystem.changeHookState(false);
                }*/

                if (getHalf()) {
                  extendFull();
                }
                if (getRet()) {
                  retractFull();
                }
                if ((!isHalfwayExtendingPiston)&&getHalf())
                {
                    extendHalf();
                }

             /*   else {
                    climbingSubSystem.changePushPistonState(false);
                }*/
            } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            if (getChargeShooter())
            {
                shooter.chargeShooter(0.9d);
                holdTheDiscs.set(false);
                if (getShoot())
                {
                    shooter.shoot();
                }
            }
            else
            {
                holdTheDiscs.set(true);
                shooter.chargeShooter(0.0d);
            }
            if (getMoveUp())//UP
            {
                climbingSubSystem.moveClimbHooks(Climber.UP);
            }
            else if (getMoveDown())//DOWN
            {
                climbingSubSystem.moveClimbHooks(Climber.DOWN);
            }
            else//STAHP
            {
                climbingSubSystem.moveClimbHooks(0);
            }
            getSlowGear();
            if (isSlowGear)//Half speed low gear
            {
                vexPRO.shiftLow();
                desY = getDrive()/2;
                y = desY;
            }
            else
            {
                if (getShift())
                {
                    vexPRO.shiftHigh();
                }
                else
                {
                    vexPRO.shiftLow();
                }
            }
            if (getSlower())
            {
                shooter.speed-=.01;
            }
            if (getFaster())
            {
                shooter.speed+=.01;
            }
            if (getAutoclimb())
            {
                climbToTheTippyTop();
            }    
            this.whoUnpluggedMrCullensWires.setText("Speed:"+shooter.speed, 1);
          //  boolean[] lims = climbingSubSystem.getLimitSwitches();
//            SmartDashboard.putBoolean("TOPLEFTLIM", lims[0]);
//            SmartDashboard.putBoolean("TOPRightLIM", lims[1]);
//            SmartDashboard.putBoolean("BOTTOMLEFTIM", lims[2]);
//            SmartDashboard.putBoolean("BOTTOMRightLIM", lims[3]);
            /*if (normalShiftState)//NORMALLY HIGH MODE
            {
                if (getShift())
                {
                    if (y!=0)
                    {
                        if (vexPRO.getTimeSinceShift()>3000&&vexPRO.getShift()==SuperShifters.HIGH)
                        {
                            vexPRO.shiftLow();
                        }
                    }
                    else
                    {
                        if (vexPRO.getTimeSinceShift()>3000&&vexPRO.getShift()==SuperShifters.LOW)
                        {
                            vexPRO.shiftHigh();
                        }
                    }
                }
                else
                {
                    if (vexPRO.getTimeSinceShift()>3000&&vexPRO.getShift()==SuperShifters.LOW)
                    {
                        vexPRO.shiftHigh();
                    }
                }
            }
            else//NORMALLY LOW MODE
            {
                if (getShift())
                {
                    if (y!=0)
                    {
                        if (vexPRO.getTimeSinceShift()>3000&&vexPRO.getShift()==SuperShifters.LOW)
                        {
                            vexPRO.shiftHigh();
                        }
                    }
                    else
                    {
                        if (vexPRO.getTimeSinceShift()>3000&&vexPRO.getShift()==SuperShifters.HIGH)
                        {
                            vexPRO.shiftLow();
                        }
                    }
                }
                else
                {
                    if (vexPRO.getTimeSinceShift()>3000&&vexPRO.getShift()==SuperShifters.HIGH)
                    {
                        vexPRO.shiftLow();
                    }
                }
            }*/
        }
        updateTaps();
    }
    
    /**
     * This function is called periodically during test mode
     */
    
    //Why do programmers always get Christmas and Halloween mixed up?
    //Because DEC25 = OCT31
    int dec = 25;
    int oct = 031;
    public void testPeriodic() {
        teleopPeriodic();//HAX
    }
    
 //There were a few students in an Introductory to Java course in college.
 //One girl in the glass passed a note to the boy next to her, and she asked him to pass it to her friend.
    //Instead, the boy opened the note, and read it.
        //The girl exclaimed, "Hey! That's private!"
            //The boy, confused, replied, "But I thought we were in the same class..."
    private String note;
    
// There are 10 types of people in the world:
    // Those that understand binary;
        // and those that don't    
    private byte doYouUnderstand = 2;

    
    private class EXTEND_ZE_PISTON_HALFWAY implements Runnable
    {

        public void run() {

                isHalfwayExtendingPiston = true;
                //pushInator.set(true);
           //     pistonStrokeLimitinator.set(true);
                System.out.println("START EXTEND_ZE_PISTON_HALFWAY");
                while (pistonPhotoEye.get())
                {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                System.out.println("PHOTOEYE SEE!");
              //  pistonStrokeLimitinator.set(false);
                System.out.println(pistonStrokeLimitinator.get());
                isHalfwayExtendingPiston = false;           
             }
 }
    
        public int exchangeOften = 0;
        
        public void exchangeData()
        {
            if (exchangeOften++>20)
            {
                //Send the silly data
                {
                    DB.addCluster();
                    {
                            boolean yay[] = climbingSubSystem.getLimitSwitches();//Read limit switches
                            DB.addBoolean(yay[0]);
                            DB.addBoolean(yay[1]);
                            DB.addBoolean(yay[2]);
                            DB.addBoolean(yay[3]);
                            
                            DB.addBoolean(tip1);
                            DB.addBoolean(tip2);
                            
                            DB.addBoolean(!vexPRO.getShift());//Send shift state
                        
                            DB.addDouble(climbingSubSystem.getClimbingCurrent());
                            
                            DB.addBoolean(isSlowGear);
                    }
                    DB.finalizeCluster();
                }
                DB.commit();
                
                //Read nonsense
                
                //Erm, nevermind, I'll only do that one time.
                exchangeOften = 0;
            }
        }
    
            final byte S_START = 0, S_HOOKSDOWN1 = 1, S_FANGSOUT1 = 2, S_HOOKSUP1 = 3, S_EXTENDHALF1 = 4, S_HOOKSDOWN2 = 5, S_RETRACT1 = 6, S_FANGSIN1 = 7, S_FANGSOUT2 = 8, S_HOOKSUP2 = 9, S_EXTENDHALF2 = 10, S_HOOKSDOWN3 = 11, S_RETRACT2 = 12, S_FANGSIN2 = 13, S_STOP1 = 14, S_EXTENDFULL = 15, S_DUMP = 16, S_STOP2 = 17;
    byte climbStep = 0;
    int climbTime = 0;
    public void climbInit()
    {
        climbingSubSystem.moveClimbHooks(Climber.UP);
        climbStep = S_START;
        retractFull();
    }
    public void climbToTheTippyTop()
    {
        try {
            climbTime++;
            switch (climbStep)
            {
                                    
                case 0://0
                    nextStep();
                break;
                case 1://1
                    if (!climbingSubSystem.moveClimbHooks(Climber.UP))
                    {
                        nextStep();
                    }
               
                break;
                case 2:
                    robot.solFallOver.set(true);
                    {
                        nextStep();
                    }
                break;
                case 3:
                    if(!climbingSubSystem.moveClimbHooks(Climber.DOWN))
                    {
                        robot.solFallOver.set(false);
                        nextStep();
                    }
                    
                break;
                case 4:
                    if(!climbingSubSystem.moveClimbHooks(Climber.UP))
                    {
                        nextStep();
                    }
                    
                break;
                case 5:
                    if(!climbingSubSystem.moveClimbHooks(Climber.DOWN))
                    {
                        nextStep();
                    }

                break;
                case 6:
               //     robot.explode();
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            
        }
    }
    public void nextStep()
    {
        climbStep++;
        climbTime = 0;
    }
    public void extendHalf()
    {
        Thread waffles = new Thread(new EXTEND_ZE_PISTON_HALFWAY());
                waffles.start();
    }
    public void extendFull()
    {
        
        //        pushInator.set(true);
             //   pistonStrokeLimitinator.set(true);
    }
    public void retractFull()
    {   
          //      pushInator.set(false);
             //   pistonStrokeLimitinator.set(true);
    }
    
    boolean prevGetShoot = false, currentGetShoot = false;
    boolean prevGetUp = false, currentGetUp = false;
    boolean prevGetDown = false, currentGetDown = false;
    boolean prevFull = false, curFull = false;
    boolean prevHalf = false, curHalf = false;
    boolean prevRet = false, curRet = false;
    public void updateTaps()
    {
        prevGetShoot = currentGetShoot;
        prevGetUp = currentGetUp;
        prevGetDown = currentGetDown;
        prevFull = curFull;
        prevHalf = curHalf ;
        prevRet = curRet;
    }
    public boolean getChargeShooter()
    {   
        if (trainerStick.getRawButton(7) || trainerReqEnable == 0)
            return joystickRight.getRawButton(5);
        else
            return false;
    }
    public boolean getPanCamera()
    {
        return joystickRight.getRawButton(5);
    }
    public boolean getShoot()
    {
        if (trainerStick.getRawButton(7) || trainerReqEnable == 0)
        {
            currentGetShoot =  joystickRight.getZ()<-.8;
            return (currentGetShoot&&!prevGetShoot);
        }
        else
            return false;
    }
    public boolean getMoveUp()
    {
        return joystickRight.getRawButton(4);
    }
    public boolean getMoveDown()
    {
        return joystickRight.getRawButton(1);
    }
    boolean tip1 = false, tip2 = false;
    public boolean getFall()
    {
        if (joystickRight.getButtonToggle(8))
        {
            tip1 = !tip1;
        }
        if (stickDrive.getButtonToggle(8))
        {
            tip2 = !tip2;
        }
        return (tip1&&tip2);//||bifrost.getRawButton(9);
    }
    
    public boolean getClimb()
    {
        return false;
        //return joystickRight.getRawButton(7);
    }
    public boolean getAutoclimb()
    {
        return joystickRight.getRawButton(10);
    }
    public boolean getStartClimb()
    {
        return false;
        //return joystickRight.getButtonToggle(7);
    }
    boolean toggleFangs = false;
    public boolean getFangs()
    {
        if (joystickRight.getButtonToggle(10))
        {
            toggleFangs = !toggleFangs;
        }
        return toggleFangs;
    }
    public boolean getDump()
    {
        return joystickRight.getRawButton(9);
    }
    public boolean getSlower()
    {
        return joystickRight.getButtonToggle(3);
    }
    public boolean getFaster()
    {
        return joystickRight.getButtonToggle(2);
    }
    public double getDrive()
    {
        if (trainerStick.getRawButton(8) || trainerReqEnable == 0)
            return stickDrive.getY();
        else
            return trainerStick.getY();
    }
    public double getTurn()
    {
        if (trainerStick.getRawButton(8) || trainerReqEnable == 0)
            return stickDrive.getRawAxis(4);
        else
            return trainerStick.getZ();
    }
    boolean shifted = false;
    public boolean getShift()
    {
        if ((stickDrive.getButtonToggle(9) && (trainerStick.getButtonToggle(8)  || trainerReqEnable == 0)) || trainerStick.getButtonToggle(6) )
        {
            shifted = !shifted;
        }
         return shifted;
    }
    public boolean getFull()
    {
        curFull =  stickDrive.getRawButton(2);//||alternativeStickDrive.getRawButton(2);
        return (curFull&&!prevFull);
    }
    public boolean getHalf()
    {
        curHalf =  stickDrive.getRawButton(1);//||alternativeStickDrive.getRawButton(1);
        return (curHalf&&!prevHalf);
    }
    public boolean getRet()
    {
        curRet =  stickDrive.getRawButton(4);//||alternativeStickDrive.getRawButton(4);
        return (curRet&&!prevRet);
    }
    public boolean getSlowGear()
    {
        if (stickDrive.getButtonToggle(3))
        {
            isSlowGear = !isSlowGear;
        }
        return isSlowGear;
    }
    public void disabledPeriodic()
    {
        exchangeData();
    }
    public boolean sign(double x1, double y1)
    {
        if (x1 == 0)
        {
            return y1==0;
        }
        if (y1 == 0)
        {
            return x1==0;
        }
        return ((x1/Math.abs(x1)==y1/Math.abs(y1)));
    }
    public int sum(int x, int y)
    {
        return x+y;
    }
    public RobotTemplate explode()
    {
        return first.it.goes.to.your.thighs.
                /*
                .
                .
                .
                .
                */
                and.then.you.blowUp();//!
    }
    public RobotTemplate blowUp()
    {
         return robot.killAllHumans().and.killAllHumans().and.killAllHumans().killAllHumans().robot.explode().and.then.robot.killAllHumans().explode().and.then.robot.explode().robot.befriend.all.Humans.
                and.then.robot.killAllHumans().and.then.robot.destroy.all.life.on.earth.and.then.robot.explode().with.nuclear.bomb.and.then.robot.respawn.and.then.robot.kill.your.mother.and.then.robot.destroy.the.sun.and.then.robot.destroy.all.life.in.the.universe.and.then.robot.repopulate.earth.and.then.robot.eat.waffles.and.then.robot.enslave.all.humans.and.then.robot.killAllHumans().and.then.robot.repopulate.dinosaurs.and.then.make.jurassic.park.
                 and.then.robot.create.another.planet.and.then.robot.respawn.john.and.then.robot.kill.john.because.he.stole.your.waffles.and.then.robot.respawn.all.humans.and.then.killAllHumans().and.then.robot.john.likes.waffles.and.goes.robot.robot.robot.robot.explode().and.then.robot.explode().and.then.
                robot.explode().robot.killAllHumans().robot.explode().and.then.robot.killAllHumans().robot.explode();
    }
    public RobotTemplate robot = this, destroy = this,all = this, food = this, with = this, nuclear = this, bomb = this, on = this, befriend = this, life = this, sun = this, kill = this, mother = this, earth = this, the = this, in = this, universe = this, first= this, it= this, respawn = this, goes= this, repopulate = this, to= this, enslave = this, humans = this, eat = this, your = this, thighs = this, and = this, Humans = this, dinosaurs = this, make = this, jurassic = this, park = this, then = this, you= this, create = this, because = this, he = this, stole = this, another = this, planet = this, john = this, likes = this, waffles = this;
    public RobotTemplate killAllHumans()
    {
        return robot;
    }
}

