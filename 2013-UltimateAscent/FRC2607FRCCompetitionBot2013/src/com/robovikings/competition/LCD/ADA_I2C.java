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
public class ADA_I2C {
I2C i2c;

  public ADA_I2C(I2C i2c)
  {
    this.i2c = i2c;
  }
      public  byte[] intToByteArray(int value) {
    return new byte[] {
            (byte)(value >>> 24),
            (byte)(value >>> 16),
            (byte)(value >>> 8),
            (byte)value};
}
  public void write8(int reg, int value)
  {
    //"Writes an 8-bit value to the specified register/address"
    try
    {
        byte[] e = new byte[]{(byte)reg,intToByteArray(value)[3]};
      //  System.err.println(e[0]);
        byte[] lol = new byte[1];
        i2c.transaction(e, 2, lol, 0);
        
    }
    catch (Exception e)
    {
        
    }
  }
  public byte readU8(int reg){
    //"Read an unsigned byte from the I2C device"
    try{
      byte result[] = new byte[1];
      i2c.read(reg, 1, result);
      return result[0];
              }
    catch (Exception e){
        System.out.println("NO!!!");
    return -1;}
              }
  public int readU16(int reg){
    //"Read an unsigned byte from the I2C device"
    try{
      byte result[] = new byte[2];
      i2c.read(reg, 2, result);
      int c= ((result[0]) << 8)|(result[1]);
      return c;
              }
    catch (Exception e){
    return -1;}
              }
}
