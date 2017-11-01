package robotcontrol;


import lejos.hardware.sensor.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SensorWrapper {

    private class SingleValueSensorWrapper {

        private SensorMode mode;
        private float[] samples;

        SingleValueSensorWrapper(BaseSensor sensor, String mode) {
            this.mode = sensor.getMode(mode);
            this.samples = new float[this.mode.sampleSize()];
        }

        float getSample() {
            mode.fetchSample(samples, 0);
            return samples[0];
        }
    }

    private SingleValueSensorWrapper leftTouchSensor;
    private SingleValueSensorWrapper rightTouchSensor;
    private SingleValueSensorWrapper colorSensor;
    private SingleValueSensorWrapper ultrasonicSensor;
    private int sensorSampleFrequency;

    //private float touchLeft, touchRight, color, distance;

    public SensorWrapper(RobotConfig config) {
        this.leftTouchSensor       = config.leftTouchSensorPort == null ? null : new SingleValueSensorWrapper(new EV3TouchSensor(config.leftTouchSensorPort), "Touch");
        this.rightTouchSensor      = config.rightTouchSensorPort == null ? null : new SingleValueSensorWrapper(new EV3TouchSensor(config.rightTouchSensorPort), "Touch");
        this.colorSensor           = config.colorSensorPort == null ? null : new SingleValueSensorWrapper(new EV3ColorSensor((config.colorSensorPort)), config.colorSensorMode);
        this.ultrasonicSensor      = config.ultrasonicSensorPort == null ? null : new SingleValueSensorWrapper(new EV3UltrasonicSensor((config.ultrasonicSensorPort)), config.ultrasonicSensorMode);
        this.sensorSampleFrequency = config.sensorSampleFrequency;
    }

    /*public void run() {
        try {
            while (true) {
                if (this.leftTouchSensor != null) {
                    this.touchLeft = this.leftTouchSensor.getSample();
                }

                if (this.rightTouchSensor != null) {
                    this.touchRight = this.rightTouchSensor.getSample();
                }

                if (this.colorSensor != null) {
                    this.color = this.colorSensor.getSample();
                }

                if (this.ultrasonicSensor != null) {
                    this.distance = this.ultrasonicSensor.getSample();
                }

                Thread.sleep(this.sensorSampleFrequency);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * Get a sample of the defined sensor.
     * @return the sample data as float.
     * @throws PortNotDefinedException if the port of the accessed device is not defined in the roboter
     * config object.
     */
    public float getTouchLeft() throws PortNotDefinedException {
        if (this.leftTouchSensor != null) {
            return this.leftTouchSensor.getSample();
        } else {
            throw new PortNotDefinedException("Left touch sensor is being accessed, but not defined.");
        }
    }

    /**
     * Get a sample of the defined sensor.
     * @return the sample data as float.
     * @throws PortNotDefinedException if the port of the accessed device is not defined in the roboter
     * config object.
     */
    public float getTouchRight() throws PortNotDefinedException {
        if (this.rightTouchSensor != null) {
            return this.rightTouchSensor.getSample();
        } else {
            throw new PortNotDefinedException("Right touch sensor is being accessed, but not defined.");
        }
    }

    /**
     * Get a sample of the defined sensor.
     * @return the sample data as float.
     * @throws PortNotDefinedException if the port of the accessed device is not defined in the roboter
     * config object.
     */
    public float getColor() throws PortNotDefinedException {
        if (this.colorSensor != null) {
            return this.colorSensor.getSample();
        } else {
            throw new PortNotDefinedException("Color sensor is being accessed, but not defined.");
        }
    }

    /**
     * Get a sample of the defined sensor.
     * @return the sample data as float.
     * @throws PortNotDefinedException if the port of the accessed device is not defined in the roboter
     * config object.
     */
    public float getDistance() throws PortNotDefinedException {
        if (this.ultrasonicSensor != null) {
            return this.ultrasonicSensor.getSample();
        } else {
            throw new PortNotDefinedException("Ultrasonic sensor is being accessed, but not defined.");
        }
    }
}
