/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robovikings.competition.frc;

import edu.wpi.first.wpilibj.Solenoid;
/**
 *
 * @author frcdev
 */
public class SuperShifters implements Constants{
    Solenoid shiftingSolenoid, shiftSol2;
    public static final boolean LOW = false, HIGH = true;
    private boolean state = LOW;
    private long lastShift = 0;
    public SuperShifters(int sol)
    {
        shiftingSolenoid = new Solenoid(sol);
    }
    public long getTimeSinceShift()
    {
        return System.currentTimeMillis()-lastShift;
    }
    public void setShift(boolean s)
    {
        state = s;
      //  shiftSol2.set(s);
        shiftingSolenoid.set(s);
        lastShift = System.currentTimeMillis();
    }
    public boolean getShift()
    {
        return state;
    }
    public void shiftHigh()
    {
        setShift(HIGH);
    }
    public void shiftLow()
    {
        setShift(LOW);
    }
}