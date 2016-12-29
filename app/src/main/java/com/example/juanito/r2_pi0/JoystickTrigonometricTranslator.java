package com.example.juanito.r2_pi0;

/**
 * Created by davide on 29/12/16.
 */

public class JoystickTrigonometricTranslator implements JoystickTranslator {

    private final static double RAD = 57.2957795;

    public int getSpeed(double joyPower, double joyAngle)
    {
        return (int)(Math.sin(joyAngle/RAD)*joyPower);
    }

    public int getRotation(double joyPower, double joyAngle)
    {
        return (int)(Math.cos(joyAngle/RAD)*joyPower);
    }

}
