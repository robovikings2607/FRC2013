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
public class DoubleSolenoidLatchType{
    private Solenoid onSolenoid, offSolenoid;
    private boolean state = false;
    public DoubleSolenoidLatchType(Solenoid onSolenoid, Solenoid offSolenoid)
    {
        this.onSolenoid = onSolenoid;
        this.offSolenoid = offSolenoid;
    }
    public void set(boolean newState)
    {
         
            state = newState;
            offSolenoid.set(!newState);
            onSolenoid.set(newState);
      
    }
    public void changeState()
    {
        offSolenoid.set(state);
        state = !state;
        onSolenoid.set(state);
                           
    }
    public boolean get()
    {
        return state;
    }
}
