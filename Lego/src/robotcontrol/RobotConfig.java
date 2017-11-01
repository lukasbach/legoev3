package robotcontrol;

import lejos.hardware.port.Port;

public class RobotConfig {
    public Port leftTouchSensorPort;
    public Port rightTouchSensorPort;
    public Port colorSensorPort;
    public Port ultrasonicSensorPort;

    public String colorSensorMode;
    public String ultrasonicSensorMode;

    public Port leftMotorPort;
    public Port rightMotorPort;
    public Port headMptorPort;

    public int sensorSampleFrequency = 20;
}
