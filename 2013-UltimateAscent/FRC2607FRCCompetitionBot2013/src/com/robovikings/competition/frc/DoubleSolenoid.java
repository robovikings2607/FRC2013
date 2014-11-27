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
public class DoubleSolenoid implements Constants{
    private Solenoid onSolenoid, offSolenoid;
    private boolean state = false;
    public DoubleSolenoid(Solenoid onSolenoid, Solenoid offSolenoid)
    {
        this.onSolenoid = onSolenoid;
        this.offSolenoid = offSolenoid;
    }
    public void set(boolean newState)
    {
        state = newState;
        if (newState)
        {
            Thread newThread = new Thread(new Runnable(){
                        public void run()
                        {
                            onSolenoid.set(true);
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {

                            }
                            finally
                            {
                                onSolenoid.set(false);
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
                            offSolenoid.set(true);
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {

                            }
                            finally
                            {
                                offSolenoid.set(false);
                            }
                        }
                    });
            newThread.start();
        }
    }
    public void changeState()
    {
        state = !state;
        if (state)
        {
            Thread newThread = new Thread(new Runnable(){
                        public void run()
                        {
                            onSolenoid.set(true);
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {

                            }
                            finally
                            {
                                onSolenoid.set(false);
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
                            offSolenoid.set(true);
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {

                            }
                            finally
                            {
                                offSolenoid.set(false);
                            }
                        }
                    });
            newThread.start();
        }
    }
    public boolean get()
    {
        return state;
    }
}
