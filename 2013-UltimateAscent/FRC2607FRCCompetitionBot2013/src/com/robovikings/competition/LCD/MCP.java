/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.robovikings.competition.LCD;

import edu.wpi.first.wpilibj.I2C;

/**
 *
 * @author frcdev
 */
public class MCP {



    int MCP23017_IODIRA = 0x00;
    int MCP23017_IODIRB = 0x01;
    int MCP23017_GPPUA  = 0x0C;
    int MCP23017_GPPUB  = 0x0D;
    int MCP23017_GPIOA  = 0x12;
    int MCP23017_GPIOB  = 0x13;
    int MCP23017_OLATA  = 0x14;
    int MCP23017_OLATB  = 0x15;
    int MCP23008_IODIR  = 0x00;
    int MCP23008_GPIO   = 0x09;
    int MCP23008_GPPU   = 0x06;
    int MCP23008_OLAT   = 0x0A;
int OUTPUT = 0;
int INPUT = 1;

ADA_I2C i2c;
int direction;
MCP self;
int num_gpios;
    public MCP(int busnum,int address, I2C i2c)
    {
        //assert num_gpios >= 0 and num_gpios <= 16, "Number of GPIOs must be between 0 and 16"
        //self.i2c = Adafruit_I2C(address, smbus.SMBus(busnum))
        self = this;
        this.i2c = new ADA_I2C(i2c);
        self.num_gpios = num_gpios;
        
        //# set defaults

        if (num_gpios <= 8)
        {
            this.i2c.write8(MCP23008_IODIR, 0xFF);//  # all inputs on port A
            this.i2c.write8(MCP23008_GPPU, 0);
            outputvalue = this.i2c.readU8(MCP23008_IODIR);
            //self.i2c.write8(self.MCP23008_GPPUA, 0x00);
        }
            else if (num_gpios > 8 && num_gpios <= 16)
        {
            self.i2c.write8(MCP23017_IODIRA, 0xFF);  //# all inputs on port A
            self.i2c.write8(MCP23017_IODIRB, 0xFF);//  # all inputs on port B
            self.direction = self.i2c.readU8(MCP23017_IODIRA);
            self.direction |= self.i2c.readU8(MCP23017_IODIRB) << 8;
            self.i2c.write8(MCP23017_GPPUA, 0x00);
            self.i2c.write8(MCP23017_GPPUB, 0x00);
                    }
      }
    int _changebit(int bitmap, int bit, int value)
    {
       // assert value == 1 or value == 0, "Value is %s must be 1 or 0" % value
        if (value == 0)
        {
            return bitmap & ~(1 << (bit));
         }
         else
         {
            return bitmap | (1 << (bit));
                    }
      }

int currvalue = -1;
    int _readandchangepin(int port,int pin, int value)
    {
        //assert pin >= 0 and pin < self.num_gpios, "Pin number %s is invalid, only 0-%s are valid" % (pin, self.num_gpios)
        //#assert self.direction & (1 << pin) == 0, "Pin %s not set to output" % pin
             currvalue = (self.i2c.readU8(port));
       // sleep(1);
       // System.out.println(currvalue);
        int newvalue = self._changebit(currvalue, pin, value);
   //     System.out.println("OUTPUTING: "+newvalue +" WAS "+currvalue);
        self.i2c.write8(port, newvalue);
        return newvalue;
    }
    public void outputAll(int port,int out)
    {
      //  sleep(4);
       // System.out.println(currvalue);
        //int newvalue = self._changebit(currvalue, pin, value);
        self.i2c.write8(port, out);
      //  sleep(4);
    }
    void sleep(long t)
    {
        try {
            Thread.sleep(t);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    int _readandchangepin(int port,int pin, int value, int current)
    {
        //assert pin >= 0 and pin < self.num_gpios, "Pin number %s is invalid, only 0-%s are valid" % (pin, self.num_gpios)
        //#assert self.direction & (1 << pin) == 0, "Pin %s not set to output" % pin
        currvalue = current;
        int newvalue = self._changebit(currvalue, pin, value);
   //     System.out.println("OUTPUTING: "+newvalue +" WAS "+currvalue);
        self.i2c.write8(port, newvalue);
        return newvalue;
    }
    int pullup(int pin,boolean value)
    {
        if (self.num_gpios <= 8)
        {
            return self._readandchangepin(MCP23008_GPPU, pin, value?1:0);
        }
        if (self.num_gpios <= 16)
        {
            if (pin < 8)
            {
                return self._readandchangepin(MCP23017_GPPUA, pin, value?1:0);
                        }
            else
            {
                return self._readandchangepin(MCP23017_GPPUB, pin-8, value?1:0);
                        }
         }
        return -1;
    }
    //# Set pin to either input or output mode
    int config(int pin,int mode)
    {
        if (self.num_gpios <= 8)
        {
            self.direction = self._readandchangepin(MCP23008_IODIR, pin, mode);
                    }
        if (self.num_gpios <= 16)
        {
            if (pin < 8)
            {
                self.direction = self._readandchangepin(MCP23017_IODIRA, pin, mode);
            }
            else
            {
                self.direction = self._readandchangepin(MCP23017_IODIRB, pin-8, mode);
            }
                        }
        return self.direction;
     }
    int outputvalue;
    int output(int pin,int value)
    {
        //# assert self.direction & (1 << pin) == 0, "Pin %s not set to output" % pin
        if (self.num_gpios <= 8)
        {
            self.outputvalue = self._readandchangepin(MCP23008_GPIO, pin, value, self.i2c.readU8(MCP23008_OLAT));
                    }
        if (self.num_gpios <= 16)
        {
            if (pin < 8)
            {
                int a = self.i2c.readU8(MCP23017_OLATA);
         //       System.out.println("OLATA: "+a);
              //  sleep(1);
                self.outputvalue = self._readandchangepin(MCP23017_GPIOA, pin, value, a);
                        }
            else
            {
                int a = self.i2c.readU8(MCP23017_OLATB);
           //     System.out.println("OLATB: "+a);
            //  //  sleep(1);
                self.outputvalue = self._readandchangepin(MCP23017_GPIOB, pin-8, value, (a));

                        }
        }

        return self.outputvalue;

    }    int output(int pin,boolean value2)
    {
        int value = value2?1:0;
        //# assert self.direction & (1 << pin) == 0, "Pin %s not set to output" % pin
      
        return self.output(pin,value);

    }
    int input(int pin)
    {
        int value = 0;
        // pin >= 0 and pin < self.num_gpios, "Pin number %s is invalid, only 0-%s are valid" % (pin, self.num_gpios)
        //assert self.direction & (1 << pin) != 0, "Pin %s not set to input" % pin
        if (self.num_gpios <= 8)
        {
            value = self.i2c.readU8(MCP23008_GPIO);
        }
        if (self.num_gpios > 8 && self.num_gpios <= 16)
        {
            value = self.i2c.readU16(MCP23017_GPIOA);
            int temp = value >> 8;
            value <<= 8;
            value |= temp;
                    }
        return value & (1 << pin);
                }
            int input()
    {
        int value = 0;
        // pin >= 0 and pin < self.num_gpios, "Pin number %s is invalid, only 0-%s are valid" % (pin, self.num_gpios)
        //assert self.direction & (1 << pin) != 0, "Pin %s not set to input" % pin
        if (self.num_gpios <= 8)
        {
            value = self.i2c.readU8(MCP23008_GPIO);
        }
        if (self.num_gpios > 8 && self.num_gpios <= 16)
        {
            value = self.i2c.readU16(MCP23017_GPIOA);
                    }
        return value;
                }

}
