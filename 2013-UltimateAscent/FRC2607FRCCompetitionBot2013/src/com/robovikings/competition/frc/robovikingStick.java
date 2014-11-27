package com.robovikings.competition.frc;
public class robovikingStick extends edu.wpi.first.wpilibj.Joystick  implements Constants{
    private int previousState;
    public robovikingStick(int input)
    {
        super(input);
        previousState = 0;
    }
        public boolean getButtonToggle(int buttonNumber) {
            int bitValue = 0x1 << (buttonNumber - 1);
            boolean buttonWasOff = (bitValue & previousState) == 0;
            boolean buttonIsOn = getRawButton(buttonNumber);
            if (buttonIsOn) previousState |= bitValue;
            else previousState &= ~bitValue;
            return (buttonWasOff && buttonIsOn);
        }
}