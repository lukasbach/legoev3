package robotcontrol;


import lejos.hardware.sensor.*;

public class SensorWrapper {
	public static final int COLOR_ID_RED = 0; // normally 0=none
	public static final int COLOR_ID_BLUE = 2;
	public static final int COLOR_ID_GROUND = 7; // 7=brown
	public static final int COLOR_ID_LINE = 6; // 6=white

	private class MultiSensorWrapper {
		protected BaseSensor sensor;
		private SensorMode mode;
		private float[] samples;

		MultiSensorWrapper(BaseSensor sensor, String mode) {
			this.sensor = sensor;
			this.mode = sensor.getMode(mode);
			this.samples = new float[this.mode.sampleSize()];
		}

		float[] getSamples() {
			mode.fetchSample(samples, 0);
			return samples;
		}
	}

	private class SingleValueSensorWrapper extends MultiSensorWrapper {
		SingleValueSensorWrapper(BaseSensor sensor, String mode) {
			super(sensor, mode);
		}

		float getSample() {
			return super.getSamples()[0];
		}
	}

	private class gyroSensorWrapper extends SingleValueSensorWrapper {
		gyroSensorWrapper(BaseSensor sensor, String mode) {
			super(sensor, mode);
		}

		void reset() {
			((EV3GyroSensor) this.sensor).reset();
		}
	}

	private SingleValueSensorWrapper touchSensor;
	private SingleValueSensorWrapper colorSensor;
	private SingleValueSensorWrapper ultrasonicSensor;
	private gyroSensorWrapper gyroSensor;
	private int sensorSampleFrequency;

	//private float touchLeft, touchRight, color, distance;

	public SensorWrapper() {
		this.touchSensor = RobotConfig.touchSensorPort == null ? null :
				new SingleValueSensorWrapper(new EV3TouchSensor(RobotConfig.touchSensorPort), "Touch");
		this.colorSensor = RobotConfig.colorSensorPort == null ? null :
				new SingleValueSensorWrapper(new EV3ColorSensor(RobotConfig.colorSensorPort), RobotConfig.colorSensorMode);
		this.ultrasonicSensor = RobotConfig.ultrasonicSensorPort == null ? null :
				new SingleValueSensorWrapper(new EV3UltrasonicSensor(RobotConfig.ultrasonicSensorPort), RobotConfig.ultrasonicSensorMode);
		this.gyroSensor = RobotConfig.gyroPort == null ? null :
				new gyroSensorWrapper(new EV3GyroSensor(RobotConfig.gyroPort), RobotConfig.gyroSensorMode);
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
	 *
	 * @return the sample data as float.
	 * @throws PortNotDefinedException if the port of the accessed device is not defined in the roboter
	 *                                 config object.
	 */
	public float getTouch() throws PortNotDefinedException {
		if (this.touchSensor != null) {
			return this.touchSensor.getSample();
		} else {
			throw new PortNotDefinedException("Touch sensor is being accessed, but not defined.");
		}
	}

	/**
	 * Get a sample of the defined sensor.
	 *
	 * @return the sample data as float.
	 * @throws PortNotDefinedException if the port of the accessed device is not defined in the roboter
	 *                                 config object.
	 */
	public float getColor() throws PortNotDefinedException {
		if (this.colorSensor != null) {
			return this.colorSensor.getSample();
		} else {
			throw new PortNotDefinedException("Color sensor is being accessed, but not defined.");
		}
	}

	public float[] getColors() throws PortNotDefinedException {
		if (this.colorSensor != null) {
			return this.colorSensor.getSamples();
		} else {
			throw new PortNotDefinedException("Color sensor is being accessed, but not defined.");
		}
	}

	/**
	 * Get a sample of the defined sensor.
	 *
	 * @return the sample data as float.
	 * @throws PortNotDefinedException if the port of the accessed device is not defined in the roboter
	 *                                 config object.
	 */
	public float getDistance() throws PortNotDefinedException {
		if (this.ultrasonicSensor != null) {
			return this.ultrasonicSensor.getSample();
		} else {
			throw new PortNotDefinedException("Ultrasonic sensor is being accessed, but not defined.");
		}
	}

	public float getGyro() throws PortNotDefinedException {
		if (this.gyroSensor != null) {
			return this.gyroSensor.getSample();
		} else {
			throw new PortNotDefinedException("Gyroscope sensor is being accessed, but not defined.");
		}
	}

	public void gyroReset() throws PortNotDefinedException {
		if (this.gyroSensor != null) {
			this.gyroSensor.reset();
		} else {
			throw new PortNotDefinedException("Gyroscope sensor is being accessed, but not defined.");
		}

	}
}
