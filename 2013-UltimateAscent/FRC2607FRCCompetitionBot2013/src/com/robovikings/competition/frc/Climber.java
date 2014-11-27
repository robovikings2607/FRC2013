/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robovikings.competition.frc;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author frcdev
 */
public class Climber  implements Constants{
    public CANJaguar motorLeftHook, motorRightHook;
    public Compressor pneumaticCompressor;
    public Solenoid pneumaticOrientationinatorPiston1, pneumaticHookRotaryPiston1, pneumaticOrientationinatorPiston2, pneumaticHookRotaryPiston2;
    public DigitalInput diLeftHookTopLimitSwitch, diRightHookTopLimitSwitch, diLeftHookBottomLimitSwitch, diRightHookBottomLimitSwitch;
    public static Climber instance;
    private static boolean SYSTEM_FAILURE = false;
    private boolean pushPistonState = false;
    public static final double UP = 1, DOWN = -1;
    public static final boolean OUT = false, IN = true;
    private boolean hookState = false;
    double BOTTOM_SPEED_THRESHOLD = 0.8d;
    public static Climber getInstance(int lMotorChannel, int rMotorChannel, int compressorRelayChannel, int compressorSwitchChannel, 
                                      int hookPistonChannel1,int hookPistonChannel2, int lTopLimChannel, int lBottomLimChannel,
                                      int rTopLimChannel, int rBottomLimChannel) throws CANTimeoutException
    {
        if (instance==null)
        {
            instance = new Climber(lMotorChannel, rMotorChannel, compressorRelayChannel, 
                    compressorSwitchChannel, hookPistonChannel1, hookPistonChannel2, 
                    lTopLimChannel, lBottomLimChannel, rTopLimChannel, rBottomLimChannel);
        }
        return instance;
    }
    
    private Climber(int lMotorChannel, int rMotorChannel, int compressorRelayChannel, int compressorSwitchChannel, 
                    int hookPistonChannel1,int hookPistonChannel2, int lTopLimChannel, int lBottomLimChannel,
                    int rTopLimChannel, int rBottomLimChannel) throws CANTimeoutException
    {
        motorLeftHook = new CANJaguar(lMotorChannel);
        motorRightHook = new CANJaguar(rMotorChannel);
        
        //DO NOT BREAK THE LEADSCREWS! PLEASE!
        motorLeftHook.setExpiration(0.2);
        motorRightHook.setExpiration(0.2);
        motorLeftHook.setSafetyEnabled(true);
        motorRightHook.setSafetyEnabled(true);
        
        pneumaticCompressor = new Compressor(compressorSwitchChannel, compressorRelayChannel);
       // pneumaticOrientationinatorPiston1 = new Solenoid(2,2);
        pneumaticHookRotaryPiston1 = new Solenoid(hookPistonChannel1);
        //pneumaticOrientationinatorPiston2 = new Solenoid(pushPistonChannel2);
     //   pneumaticHookRotaryPiston2 = new Solenoid(hookPistonChannel2);
        diLeftHookTopLimitSwitch = new DigitalInput(lTopLimChannel);
        diRightHookTopLimitSwitch = new DigitalInput(rTopLimChannel);
        diLeftHookBottomLimitSwitch = new DigitalInput(lBottomLimChannel);
        diRightHookBottomLimitSwitch = new DigitalInput(rBottomLimChannel);
    }
    public double getClimbingCurrent()
    {
        try {
            return motorLeftHook.getOutputCurrent()+motorRightHook.getOutputCurrent();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        return -1;
    }
    public boolean moveClimbHooks(double speed)
    {
        if (true)
        {
            try
            {
                if ((speed>0&&(diLeftHookTopLimitSwitch.get()||diRightHookTopLimitSwitch.get())) || //diLeftHookTopLimitSwitch.get()
                    (speed<0&&(diLeftHookBottomLimitSwitch.get()||diRightHookBottomLimitSwitch.get())) || //diLeftHookBottomLimitSwitch.get()   
                    (Math.abs(speed)<BOTTOM_SPEED_THRESHOLD)) 
                {
                    motorLeftHook.setX(0);
                    motorRightHook.setX(0);
                    //System.out.println("Yes");
                    return false;
                }
                else
                {
                    motorLeftHook.setX(speed);
                    motorRightHook.setX(speed);
                }
            }
            catch (Exception ex)
            {
                //SYSTEM_FAILURE = true;
                try {
                    motorLeftHook.setX(0);
                    motorRightHook.setX(0);
                } catch (Exception AN_EXCEPTION_WAS_THROWN_WHILE_ANOTHER_EXCEPTION_WAS_BEING_CAUGHT__OH_NO) {
                    //ex.printStackTrace();
                }
            }
        }
        return true;
    }
    /**
     * 
     * @deprecated 
     */
    public void moveClimbHooksLeft(double speed)
    {
        if (!SYSTEM_FAILURE)
        {
            if ((speed>0&&(false))||Math.abs(speed)<BOTTOM_SPEED_THRESHOLD) //diLeftHookLimSwitch.get()
            {
                speed = 0;
            }
            try
            {
                motorLeftHook.set(speed);
            }
            catch (Exception ex)
            {
                SYSTEM_FAILURE = true;
            }
        }
        else
        {
            try {
                motorLeftHook.set(0);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
        }
    }
    /**
     * 
     * @deprecated 
     */
    public void moveClimbHooksRight(double speed)
    {
        if (!SYSTEM_FAILURE)
        {
            if ((speed>0&&(false))||Math.abs(speed)<BOTTOM_SPEED_THRESHOLD) //diRightHookLimSwitch.get()
            {
                speed = 0;
            }
            try
            {
                motorRightHook.set(speed);
            }
            catch (Exception ex)
            {
                SYSTEM_FAILURE = true;
            }
        }
        else
        {
            try {
                motorRightHook.set(0);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
        }
    }
    public void fixSystemFailure()
    {
        SYSTEM_FAILURE = false;
    }
    
    public void changeHookState(boolean newState) throws InterruptedException
    {
        if (!SYSTEM_FAILURE)
        {
            hookState = newState;
            
                        pneumaticHookRotaryPiston1.set(hookState);
                       
        }
    }
    
    /**
     * 
     * @deprecated DO NOT USE 
     */
    public void changePushPistonState(boolean newState)
    {
        if (!SYSTEM_FAILURE)
        {
            pushPistonState = newState;
         //   pneumaticOrientationinatorPiston1.set(newState);
            /*if (newState)
            {
                Thread newThread = new Thread(new Runnable(){
                    public void run()
                    {
                        pneumaticOrientationinatorPiston1.set(true);
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex) {
                            
                        }
                        finally
                        {
                            pneumaticOrientationinatorPiston1.set(false);
                        }
                    }
                });
                newThread.start();
            }
            else
            {
                Thread newThread = new Thread(new Runnable(){
                    public void run()
                    {
                        pneumaticOrientationinatorPiston2.set(true);
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex) {
                            
                        }
                        finally
                        {
                            pneumaticOrientationinatorPiston2.set(false);
                        }
                    }
                });
                newThread.start();
            }*/
        }
    }
    
    public void runCompressor(boolean newState)
    {
        if (!SYSTEM_FAILURE)
        {
            if (newState)
            {
                pneumaticCompressor.start();
            }
            else
            {
                pneumaticCompressor.stop();
            }
        }
        else
        {
            pneumaticCompressor.stop();
        }
    }
    public boolean getPushState()
    {
        return pushPistonState;
    }
    public boolean getHookState()
    {
        return hookState;
    }

    public boolean[] getLimitSwitches()
    {
     boolean yes[] = new boolean[]{false,false,false,false};
     yes[0] = diLeftHookTopLimitSwitch.get();
     yes[1] = diRightHookTopLimitSwitch.get();
     yes[2] = diLeftHookBottomLimitSwitch.get();
     yes[3] = diRightHookBottomLimitSwitch.get();
     return yes;
    }
}
