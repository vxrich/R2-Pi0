package com.example.juanito.r2_pi0;

/**
 * Created by davide on 29/12/16.
 */

public interface JoystickTranslator {

    int getSpeed(double joyPower, double joyAngle);

    int getRotation(double joyPower, double joyAngle);

}
