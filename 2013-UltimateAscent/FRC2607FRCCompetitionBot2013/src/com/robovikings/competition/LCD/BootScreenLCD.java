/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.robovikings.competition.LCD;


import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import java.util.Random;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class BootScreenLCD extends BootScreen{
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    LCD lcd;
    I2C i2c;
    boolean isBusy = false;
    public static DigitalModule DM = DigitalModule.getInstance(1);
    public static void scanI2C()
    {
        for (int i = 0; i < 255; i ++)
                {
                    if (!DM.getI2C(i).addressOnly())
                    {
                        System.out.println(i);
                    }
                    else
                    {
                        System.out.println("NOT HERE!");
                    }
                }
    }
    public BootScreenLCD() {
    /**/
        
     /*  
                }*/
        i2c = DigitalModule.getInstance(1).getI2C(64);
       // i2c.setCompatabilityMode(true);
    lcd = new LCD(i2c);
        try {
            lcd.clear();
          //  lcd.lcd_string("Global Waffles?",1);
           Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
 //   lcd.backlight(true,true,true);//RACISM
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//        lcd.lcd_byte(0x80, false);
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//    lcd.mcp.output(lcd.pin_rs, true);
//    lcd.mcp.output(lcd.pins_db[0], false);
//    lcd.mcp.output(lcd.pins_db[1], false);
//    lcd.mcp.output(lcd.pins_db[2], false);
//    lcd.mcp.output(lcd.pins_db[3], false);
//    sleep(1);
//    lcd.mcp.output(lcd.pins_db[0], false);
//    lcd.mcp.output(lcd.pins_db[1], true);
//    lcd.mcp.output(lcd.pins_db[2], true);
//    lcd.mcp.output(lcd.pins_db[3], false);
//    sleep(2);
//    lcd.mcp.output(lcd.pin_e,true);
//    sleep(1);
//    lcd.mcp.output(lcd.pin_e,false);
//    sleep(2);
//    lcd.mcp.output(lcd.pins_db[0], false);
//    lcd.mcp.output(lcd.pins_db[1], false);
//    lcd.mcp.output(lcd.pins_db[2], false);
//    lcd.mcp.output(lcd.pins_db[3], true);
//    sleep(2);
//    lcd.mcp.output(lcd.pin_e,true);
//    sleep(1);
//    lcd.mcp.output(lcd.pin_e,false);
//    sleep(2);
   // System.out.println("~~~~~~~~~~~~~~~~TEST~~~~~~~~~~~~~~~~");
     //lcd.backlight(false,true,true);//RACISM
  //
    //lcd.lcd_string("JohnLikesWaffles", 1);
//   // lcd.lcd_byte(0x41, true);
//       // lcd.lcd_byte(0x80, false);
//        for (int i = 0; i < 16; i++)
//        {
//            lcd.lcd_byte(0x41, true);
//            try {
//            Thread.sleep(10);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//        }
    }
    void sleep(long time)
    {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    public void setText(final String text,final int line)
    {
         
        
            Thread st = new Thread(new Runnable(){public void run(){
                while (isBusy)
        {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        //ex.printStackTrace();
                    }
        }
                isBusy = true;
            lcd.lcd_string(text,line);
            isBusy = false;}});
            st.start();//Setting text takes a while, blocking for long no es bueno
    }
    public void setColor(boolean red, boolean blue, boolean green)
    {
            lcd.backlight(red, blue, green);

    }
    public void setError(final String text,final int line)
    {
            setColor(true,false,false);
            setText(text,line);
    }
}
