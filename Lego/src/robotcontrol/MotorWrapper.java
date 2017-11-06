package robotcontrol;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.robotics.BaseMotor;

public class MotorWrapper {
    public BaseMotor leftMotor, rightMotor, headMotor;

    public MotorWrapper(RobotConfig config) {
        // TODO exceptions for undefined ports
        this.leftMotor = new EV3LargeRegulatedMotor(config.leftMotorPort);
        this.rightMotor = new EV3LargeRegulatedMotor(config.rightMotorPort);
        this.headMotor = new EV3MediumRegulatedMotor(config.headMptorPort);
    }
}
