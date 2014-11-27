/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robovikings.competition.LCD;

import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.I2C;

/**
 *
 * @author frcdev
 */
public class LCD {
public I2C i2c;
LCD self = this;
  byte  OUTPUT = 0;
 byte   INPUT = 1;
    
 //byte   LED colors
  byte  RED = 0x01;
   byte GREEN = 0x02;
  byte  BLUE = 0x04;
  byte  YELLOW = 0x03;
  byte  TEAL = 0x06;
 byte   VIOLET = 0x05;
 byte   ON = 0x07;
 byte   OFF = 0x0;

 //   # buttons
  byte  SELECT = 0;
 byte   RIGHT = 1;
 byte   DOWN = 2;
 byte   UP = 3;
  byte  LEFT = 4;

  //  # commands
byte    LCD_CLEARDISPLAY             = 0x01;
byte    LCD_RETURNHOME                 = 0x02;
byte    LCD_ENTRYMODESET                 = 0x04;
byte    LCD_DISPLAYCONTROL                 = 0x08;
byte    LCD_CURSORSHIFT                 = 0x10;
 byte   LCD_FUNCTIONSET                 = 0x20;;
 byte   LCD_SETCGRAMADDR                 = 0x40;
int    LCD_SETDDRAMADDR                 = 0x80;

 //   # flags for display entry mode
  byte  LCD_ENTRYRIGHT                 = 0x00;
  byte  LCD_ENTRYLEFT                 = 0x02;
 byte   LCD_ENTRYSHIFTINCREMENT         = 0x01;
  byte  LCD_ENTRYSHIFTDECREMENT         = 0x00;

 //  # flags for display on/off control
 byte   LCD_DISPLAYON                 = 0x04;
   byte LCD_DISPLAYOFF                 = 0x00;
  byte  LCD_CURSORON                 = 0x02;
   byte LCD_CURSOROFF                 = 0x00;
   byte LCD_BLINKON                 = 0x01;
   byte LCD_BLINKOFF                 = 0x00;

   // # flags for display/cursor shift
  byte  LCD_DISPLAYMOVE                 = 0x08;
   byte LCD_CURSORMOVE                 = 0x00;

    //# flags for display/cursor shift
   byte LCD_MOVERIGHT                 = 0x04;
   byte LCD_MOVELEFT                 = 0x00;

   // # flags for function set
   byte LCD_8BITMODE                 = 0x10;
   byte LCD_4BITMODE                 = 0x00;
   byte LCD_2LINE                         = 0x08;
   byte LCD_1LINE                         = 0x00;
   byte LCD_5x10DOTS                 = 0x04;
   byte LCD_5x8DOTS                 = 0x00;


public MCP mcp;
int displaycontrol, displayfunction, displaymode;
boolean True = true, False = false;
int busnum=1;
int a = 0;
        int pin_rs=15+a;
        int pin_e=13+a;
        int pins_db[]=new int[]{12+a, 11+a, 10+a, 9+a};
        int pins_ds[]=new int[]{9+a, 10+a, 11+a, 12+a};
        int pin_rw=14+a;
    private boolean makeinput;
    int displayshift = 0;
    void lcd_init()
{
  
    //# Initialise display
//        self.write4bits(0x33);// # initialization
//	self.write4bits(0x32);// # initialization
//	self.write4bits(0x28);// # 2 line 5x7 matrix
//	self.write4bits(0x0C);// # turn cursor off 0x0E to enable cursor
//	self.write4bits(0x06);// # shift cursor right
  lcd_byte(0x33,false);sleep(2);
  lcd_byte(0x32,false);sleep(2);
  lcd_byte(0x28,false);sleep(2);
  lcd_byte(0x0E,false);sleep(2);//0x0C
  lcd_byte(0x06,false);sleep(2);
  lcd_byte(0x01,false);sleep(2); 
  lcd_byte(0xC0,false);sleep(2);

 }
    public LCD(I2C i3c)
    {
        this.i2c = i3c;
        //i2c.write(pin_rs, pin_e);
        mcp = new MCP(busnum,16,i2c);
        self.mcp.config(self.pin_e, self.OUTPUT);
        self.mcp.config(self.pin_rs,  self.OUTPUT);
        self.mcp.config(self.pin_rw,  self.OUTPUT);
        self.mcp.output(self.pin_rw, 0);
        self.mcp.output(self.pin_e, 0);
        
        for (int i = 0; i < 4; i ++)
        {
            int pin = pins_db[i];
            self.mcp.config(pin,  self.OUTPUT);
                    }


//        self.displaycontrol = self.LCD_DISPLAYON | self.LCD_CURSOROFF | self.LCD_BLINKOFF;
//
//        self.displayfunction = self.LCD_4BITMODE | self.LCD_1LINE | self.LCD_5x8DOTS;
//        self.displayfunction |= self.LCD_2LINE;

        //""" Initialize to default text direction (for romance languages) """
//        self.displaymode =  self.LCD_ENTRYLEFT | self.LCD_ENTRYSHIFTDECREMENT;
//        self.write4bits(self.LCD_ENTRYMODESET | self.displaymode);// #  set the entry mode

       // # turn on backlights!
        self.mcp.config(6+a, self.mcp.OUTPUT);
        self.mcp.config(7+a, self.mcp.OUTPUT);
        self.mcp.config(8+a, self.mcp.OUTPUT);
        self.mcp.output(6+a, 0);// # red
        self.mcp.output(7+a, 0);// # green 
        self.mcp.output(8+a, 0);// # blue
        //# turn on pullups
        self.mcp.pullup(self.SELECT, True);
        self.mcp.pullup(self.LEFT, True);
        self.mcp.pullup(self.RIGHT, True);
        self.mcp.pullup(self.UP, True);
        self.mcp.pullup(self.DOWN, True);
        self.mcp.config(self.SELECT, self.mcp.INPUT);
        self.mcp.config(self.LEFT, self.mcp.INPUT);
        self.mcp.config(self.RIGHT, self.mcp.INPUT);
        self.mcp.config(self.DOWN, self.mcp.INPUT);
        self.mcp.config(self.UP, self.mcp.INPUT);
        self.lcd_init();
    }
    int numlines, currline;
    void begin(int lines)
    {
        if (lines > 1)
        {
            self.numlines = lines;
            self.displayfunction |= self.LCD_2LINE;
        }
        self.currline = 0;
        self.clear();
   }
   void home()
   {
        self.write4bits(self.LCD_RETURNHOME);// # set cursor position to zero
        self.waitBFlow();// #wait for Busy flag low
                }

    void clear()
    {
        self.write4bits(self.LCD_CLEARDISPLAY);// # command to clear display
        self.waitBFlow();// #wait for Busy flag low
                
    }
    int[] row_offsets;
    void setCursor(int col,int row)
    {
        self.row_offsets = new int[]{ 0x00, 0x40, 0x14, 0x54 };
        if ( row > self.numlines )
        {
           }
        self.write4bits(self.LCD_SETDDRAMADDR | (col + self.row_offsets[row]));
    }
    void noDisplay()
    {
        //""" Turn the display off (quickly) """
        self.displaycontrol &= ~self.LCD_DISPLAYON;
        self.write4bits(self.LCD_DISPLAYCONTROL | self.displaycontrol);
                }
    void display()
    {
    //    """ Turn the display on (quickly) """
        self.displaycontrol |= self.LCD_DISPLAYON;
        self.write4bits(self.LCD_DISPLAYCONTROL | self.displaycontrol);
                }
    void noCursor()
    {
      //  """ underline cursor off """
        self.displaycontrol &= ~self.LCD_CURSORON;
        self.write4bits(self.LCD_DISPLAYCONTROL | self.displaycontrol);
                }

    void cursor()
    {
     //   """ underline Cursor On """
        self.displaycontrol |= self.LCD_CURSORON;
        self.write4bits(self.LCD_DISPLAYCONTROL | self.displaycontrol);
                }
    void ToggleCursor()
    {
       // """ Toggles the underline cursor On/Off bb"""
        self.displaycontrol ^= self.LCD_CURSORON;
        self.delayMicroseconds(200000);
        self.write4bits(self.LCD_DISPLAYCONTROL | self.displaycontrol);
                }
    void noBlink()
    {
      //  """ Turn  off the blinking cursor """
        self.displaycontrol &= ~self.LCD_BLINKON;
        self.write4bits(self.LCD_DISPLAYCONTROL | self.displaycontrol);
                }
    void blink()
    {
      //  """ Turn on the blinking cursor"""
        self.displaycontrol |= self.LCD_BLINKON;
        self.write4bits(self.LCD_DISPLAYCONTROL | self.displaycontrol);
                }
    void ToggleBlink()
    {
      //  """ Toggles the blinking cursor"""
        self.displaycontrol ^= self.LCD_BLINKON;
        self.delayMicroseconds(200000);
        self.write4bits(self.LCD_DISPLAYCONTROL | self.displaycontrol);
                }

    void DisplayLeft()
    {
       // """ These commands scroll the display without changing the RAM """
        self.write4bits(self.LCD_CURSORSHIFT | self.LCD_DISPLAYMOVE | self.LCD_MOVELEFT);
                }
    void scrollDisplayRight()
    {
     //   """ These commands scroll the display without changing the RAM """
        self.write4bits(self.LCD_CURSORSHIFT | self.LCD_DISPLAYMOVE | self.LCD_MOVERIGHT);
    }
    void leftToRight()
    {
       // """ This is for text that flows Left to Right """
        self.displaymode |= self.LCD_ENTRYLEFT;
        self.write4bits(self.LCD_ENTRYMODESET | self.displaymode);
    }
    void rightToLeft()
    {
      //  """ This is for text that flows Right to Left """
        self.displaymode &= ~self.LCD_ENTRYLEFT;
        self.write4bits(self.LCD_ENTRYMODESET | self.displaymode);
                }
    void autoscroll()
    {
       // """ This will 'right justify' text from the cursor """
        self.displaymode |= self.LCD_ENTRYSHIFTINCREMENT;
        self.write4bits(self.LCD_ENTRYMODESET | self.displaymode);
                }
    void noAutoscroll()
    {
     //   """ This will 'left justify' text from the cursor """
        self.displaymode &= ~self.LCD_ENTRYSHIFTINCREMENT;
        self.write4bits(self.LCD_ENTRYMODESET | self.displaymode);
                }
    boolean char_mode=False;
    static public boolean isZero( int value, int position)
{
position -= 1; // zero based position
int mask = 1; // single bit mask

// Move the mask bit in to position
mask <<= position; 

// Mask off all bits except for the target
value &= mask; 

// If the whole value is zero then the masked bit was zero
return value == 0;
}
    void write4bits(int bit)
    {
 //""" Send command to LCD """
   //     #self.delayMicroseconds(1000) # 1000 microsecond sleep
       // bits=bin(bits)[2:].zfill(8)
        self.mcp.output(self.pin_rs, char_mode?1:0);

        for (int i = 0; i <= 3; i ++)
        {
            if (isZero(bit,i))
                    {
                self.mcp.output(self.pins_db[i], 0);
                    }
            else
            {
                self.mcp.output(self.pins_db[i], 1);
            }
        }
        self.pulseEnable();

        for (int i = 4; i <= 7; i++)
        {
            if (isZero(bit,i))
            {
                self.mcp.output(self.pins_db[(i-4)], 0);
            }
            else
            {
                self.mcp.output(self.pins_db[(i-4)], 1);
            }
        }
        self.pulseEnable();
  } 
   
  
    
 //#See pg. 24 and 58 HD44780.pdf and http://www.avrbeginners.net/ Standard LCD bit mode
    /*def read4bits(self, char_mode=False):
            {
        """ Get Data from LCD, when char_mode = 0 get Busy Flag and Address Counter, else get RAM data """
#        print "First:", bin(self.mcp.direction)[2:].zfill(16)
        self.mcp.output(self.pin_rs, char_mode) # when char_mode = 0 get Busy Flag and Address Counter, else get RAM data
        self.mcp.output(self.pin_rw, 1) #set rw to 1
        #  Configure pins for input
        makeinput=True
        if makeinput:
            for pin in self.pins_db:
                self.mcp.config(pin,  self.INPUT)
        self.mcp.output(self.pin_e, True)   # set Enable high and keep while read first nibble    
        bits = range(8)
        # get the pins values
        for i in range(4):
            bt = self.mcp.input(self.pins_db[::-1][i], makeinput) # if makeinput is False, pin direction checking is supressed in input
            bits[i]=bt
            #print i,bt,bits[i]
        self.mcp.output(self.pin_e, False)   # set Enable low to finish first nibble   
        self.mcp.output(self.pin_e, True)   # set Enable high and keep while read 2nd nibble   
        # get the pins values
        for i in range(4,8):
            bt = self.mcp.input(self.pins_db[::-1][i-4],makeinput)
            bits[i]=bt
            #print i,bt,bits[i]
        self.mcp.output(self.pin_e, False)   # set Enable low to finish 2nd nibble   
        # restore the pins to output and rw to 0
        for pin in self.pins_db:
            self.mcp.config(pin,  self.OUTPUT)
        self.mcp.output(self.pin_rw, 0) # return rw to 0
#                print "Last :", bin(self.mcp.direction)[2:].zfill(16)
        return bits;
}*/
    boolean readBF()
    {
  //      """ Get Data from LCD, when char_mode = 0 get Busy Flag and Address Counter, else get RAM data """
//#        print "First:", bin(self.mcp.direction)[2:].zfill(16)
        self.mcp.output(self.pin_rs, 0);// # when char_mode = 0 get Busy Flag and Address Counter, else get RAM data
        self.mcp.output(self.pin_rw, 1);// #set rw to 1
    //    #  Configure pins for input
        makeinput=True;
        self.mcp.config(self.pins_db[1],  self.INPUT);
        self.mcp.output(self.pin_e, 1);//   # set Enable high and keep while read first nibble    
      //  # get the pins values
        int bt = self.mcp.input(self.pins_db[1]);// # if makeinput is False, pin direction checking is supressed in input
        self.mcp.output(self.pin_e, 0);//   # set Enable low to finish first nibble   
        self.pulseEnable();// # one more pulse to get (but ingore the 2nd nibble)
       // # restore the pins to output and rw to 0
        self.mcp.config(self.pins_db[1],  self.OUTPUT);
        self.mcp.output(self.pin_rw, 0);// # return rw to 0
        return bt==1;
}
    void waitBFlow()
            {
        for (int i = 0; i <= 10000; i ++)
        {
            if  (!self.readBF());
                    {
                        return;
                //#print cnt
                
                        }
                        }  }                   

    void delayMicroseconds(int microseconds)
    {
        //int seconds = microseconds / 1000000;//        # divide microseconds by 1 million for seconds
        try {
            Thread.sleep(microseconds);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
                }
    void pulseEnable()
    {
        self.mcp.output(self.pin_e, 1);
        self.delayMicroseconds(2);//                # 1 microsecond pause - enable pulse must be > 450ns 
        self.mcp.output(self.pin_e, 0);
        self.delayMicroseconds(1);//                # commands need > 37us to settle
                }
    void message(String text)
    {
        
       // """ Send string to LCD. Newline wraps to second line"""
        for (int i = 0; i < text.length(); i ++)
        {
            char cha = text.charAt(i);
            if (cha == '\n')
            {
                self.write4bits(0xC0);// # set DDRAM address 0x40 start of second line 
            }
            else
            {
                self.write4bits(cha);
            }
    }

}
    void backlight(boolean red, boolean blue, boolean green)
    {
        self.mcp.output(6,  red?0:1);

        self.mcp.output(7,  green?0:1);

        self.mcp.output(8,  blue?0:1);
//        self.mcp.output(6+a, 0);// # red
//        self.mcp.output(7+a, 0);// # green 
//        self.mcp.output(8+a, 0);// # blue
    }
int buttonPressed(int buttonname)
{
	if (buttonname > self.LEFT)
        {
		return 0;
                        }
	return  (self.mcp.input(buttonname));
                }

void lcd_string(String message, int line)
{

            int lines[] = new int[]{0x0,0x02,0xC0};
    lcd_byte(lines[line], false);
    sleep(4);
//  # Send string to display
//  # style=1 Left justified
//  # style=2 Centred
//  # style=3 Right justified

//  if (style==1)
//  {
//      
//    message = message.ljust(16," ");
//  }
//  if (style==2)
//  {
//    message = message.center(16," ");
//  }
//  if (style==3)
//  {
//    message = message.rjust(16," ");
//            }
    while (message.length()<16)
    {
        message+=" ";
    }
  for (int i = 0; i <16; i++)
  {
      
      int b = (int)(message.charAt(i));
   //   System.err.println(b);
    lcd_byte(b,true);
   // sleep(4);
  }
}
void lcd_byte(int bits,boolean mode)
{
    
        try {
            
            //       try {
                       //  # Send byte to data pins
                       //  # bits = data
                       //  # mode = True  for character
                       //  #        False for command
          //  Thread.sleep(2);
                       MCP GPIO = self.mcp;
                         self.mcp.output(this.pin_rs, mode);// # RS

                         //# High bits
                         
                         
                         
                         
                         if ((bits&0x10)==0x10)
                         {
                           GPIO.output(this.pins_db[0], True);
                         }
                         else
                         {
                             GPIO.output(this.pins_db[0], False);
                         }
                 
                 
                 
                 
                 
                         if ((bits&0x20)==0x20)
                                 {
                           GPIO.output(this.pins_db[1], True);
                                 }
                         else
                         {
                             GPIO.output(this.pins_db[1], False);
                         }
             
             
             
             
                   
                         if ((bits&0x40)==0x40)
                         {
                           GPIO.output(this.pins_db[2], True);
                         }
                         else
                         {
                             GPIO.output(this.pins_db[2], False);
                         }
               
                         if ((bits&0x80)==0x80)
                         {
                           GPIO.output(this.pins_db[3], True);
                         }
                         else
                         {
                             GPIO.output(this.pins_db[3], False);
                         }
                         //# Toggle 'Enable' pin
                       //     

                         GPIO.output(pin_e, True);
                         Thread.sleep(1);

                         GPIO.output(pin_e, False);
                   //               
                  //      Thread.sleep(1);
                         //# Low bits
             
                         
                        
                         if ((bits&0x01)==0x01)
                         {
                           GPIO.output(this.pins_db[0], True);
                         }
                          else
                         {
                             GPIO.output(this.pins_db[0], False);
                         }

                         if ((bits&0x02)==0x02)
                         {
                           GPIO.output(this.pins_db[1], True);
                         }
                         else
                         {
                             GPIO.output(this.pins_db[1], False);
                         }
                         if ((bits&0x04)==0x04)
                         {
                           GPIO.output(this.pins_db[2], True);
                         }
                         else
                         {
                             GPIO.output(this.pins_db[2], False);
                         }

                         if ((bits&0x08)==0x08)
                         {
                           GPIO.output(this.pins_db[3], True);
                         }
                         else
                         { 
                             GPIO.output(this.pins_db[3], False);
                             }
                         //# Toggle 'Enable' pin
                       //     
                            //Thread.sleep(1);
                         GPIO.output(pin_e, True);
                        //    
                            Thread.sleep(1);
                         GPIO.output(pin_e, False);
                  //       Thread.sleep(2);
                     //     
        } catch (Exception ex) {
            ex.printStackTrace();
        }


          }
public void sleep(int t)
{
        try {
            Thread.sleep(t);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
}
    }