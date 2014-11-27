/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robovikings.competition.frc;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author frcdev
 */
public class Spinfusor  implements Constants{
    Victor stage1, stage2, stage3;
    Solenoid fireZeLaser;
    boolean isShooting = false;
    public float speed = 1.0f;
    public Spinfusor()
    {
        stage1 = new Victor(shooterStage1Victor);
        stage2 = new Victor(shooterStage2Victor);
        stage3 = new Victor(shooterStage3Victor);
        fireZeLaser = new Solenoid(shootSolenoid);
    }
    public void chargeShooter(double set)
    {
        set = Math.abs(set);
        stage1.set(set*speed);//1
        stage3.set(set*speed);//3
        stage2.set(-set*speed);//8
    }
    public boolean isShooting()
    {
        return isShooting;
    }
    public boolean shoot()
    {
        if (isShooting)
        {
            return false;//Command failed
        }
        else
        {
            Thread fireThread = new Thread(
                    new Runnable()
                    {
                        public void run() {
                            isShooting = true;
                            fireZeLaser.set(true);
                            RobotTemplate.LEDs.set(true);
                            
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            finally
                            {
                                fireZeLaser.set(false);
                                isShooting = false;
                            }
                            try {
                                Thread.sleep(150);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            finally
                            {
                                RobotTemplate.LEDs.set(false);
                            }
                        }
                    });
            fireThread.start();
            return true;
        }
    }
}