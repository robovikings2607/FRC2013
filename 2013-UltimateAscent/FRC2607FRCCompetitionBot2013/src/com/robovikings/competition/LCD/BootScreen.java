/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robovikings.competition.LCD;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;

/**
 *
 * @author frcdev
 */
public class BootScreen {
    DriverStationLCD dslcd = DriverStationLCD.getInstance();
    public BootScreen()
    {
        
    }
    public void setError(String text, int line)
    {
        
    }
    public void setText(String text, int line)
    {
        dslcd.println(DriverStationLCD.Line.kUser1, 1, text);
        dslcd.updateLCD();
    }
    public void setColor(boolean r, boolean b, boolean g)
    {
        
    }
    public boolean getLCD()
    {
        return false;
    }
}
