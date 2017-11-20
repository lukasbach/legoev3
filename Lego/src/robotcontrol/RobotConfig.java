package robotcontrol;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

public class RobotConfig {
 //   public static final Port leftTouchSensorPort = SensorPort.S2;//1
    //public static final Port rightTouchSensorPort = SensorPort.S4; //2
    public static final Port colorSensorPort = SensorPort.S3; //3
    public static final Port ultrasonicSensorPort = SensorPort.S1;//SensorPort.S4; //4
    public static final Port gyroPort = SensorPort.S4;

    public static final String colorSensorMode = "Red";
    public static final String ultrasonicSensorMode = "Distance";

    public static final Port leftMotorPort = MotorPort.B;
    public static final Port rightMotorPort = MotorPort.C;
    public static final Port headMotorPort = MotorPort.D;

    //public static final int sensorSampleFrequency = 20;  
    
}
