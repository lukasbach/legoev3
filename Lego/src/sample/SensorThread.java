package sample;


public class SensorThread extends Thread {

	private SingleValueSensorWrapper Stouch1;
	private SingleValueSensorWrapper Stouch2;
	private SingleValueSensorWrapper Scolor;
	private SingleValueSensorWrapper Sdistance;

	public float touchLeft, touchRight, color, distance;

	public SensorThread(SingleValueSensorWrapper Stouch1, SingleValueSensorWrapper Stouch2, SingleValueSensorWrapper Scolor, SingleValueSensorWrapper Sdistance) {
		this.Stouch1 = Stouch1;
		this.Stouch2 = Stouch2;
		this.Scolor = Scolor;
		this.Sdistance = Sdistance;
	}

	public void run() {
		try {
			while (true) {
				this.touchLeft = this.Stouch1.getSample();
				this.touchRight = this.Stouch2.getSample();
				this.color = this.Scolor.getSample();
				this.distance = this.Sdistance.getSample();
				Thread.sleep(20);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
