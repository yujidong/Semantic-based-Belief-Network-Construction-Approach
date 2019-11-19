package org.cloudbus.iotnetsim.iot.nodes;

/**
 * Title:        IoTNetSim Toolkit
 * Description:  Modelling and Simulation for End-to-End IoT Services & Networking 
 * 
 * Author: Maria Salama, Lancaster University
 * Contact: m.salama@lancaster.ac.uk
 *
 * If you are using any algorithms, policies or workload included in the SAd/SAw CloudSim Toolkit,
 * please cite the following paper:
 * 
 * Maria Salama, Yehia Elkhatib, and Gordon Blair. 2019. 
 * IoTNetSim: A Modelling and Simulation Platform for End-to-End IoT Services and Networking.
 * In Proceedings of the IEEE/ACM 12th International Conference on Utility and Cloud Computing (UCC ’19), December 2–5, 2019, Auckland, New Zealand. 
 * ACM, NewYork,NY, USA, 11 pages. 
 * https://doi.org/10.1145/3344341.3368820
 * 
 */

/**
 * Class
 * 
 * @author Maria Salama
 * 
 */

public class SensorReading {
	
	private int sensorId;
	private int readingDay;		//day number fo the experiment
	private int readingIndex; 	//reading number within the day
	private double readingTime;	//reading time at the simulation time
	private double readingData;	//actual reading data
	
	
	public SensorReading() {
		super();
		// TODO Auto-generated constructor stub
	}

//	public SensorReading(int sensorId, double readingTime, double readingData) {
//		super();
//		this.sensorId = sensorId;
//		this.readingTime = readingTime;
//		this.readingData = readingData;
//	}

	public SensorReading(int sensorId, int readingDay, int readingIndex, double readingTime, double readingData) {
		this.sensorId = sensorId;
		this.readingDay = readingDay;
		this.readingIndex = readingIndex;
		this.readingTime = readingTime;
		this.readingData = readingData;
	}

	
	public int getSensorId() {
		return sensorId;
	}

	public void setSensorId(int sensorId) {
		this.sensorId = sensorId;
	}

	public int getReadingDay() {
		return readingDay;
	}

	public void setReadingDay(int readingDay) {
		this.readingDay = readingDay;
	}
	
	public double getReadingTime() {
		return readingTime;
	}

	public void setReadingTime(double readingTime) {
		this.readingTime = readingTime;
	}

	public int getReadingIndex() {
		return readingIndex;
	}

	public void setReadingIndex(int readingIndex) {
		this.readingIndex = readingIndex;
	}

	public double getReadingData() {
		return readingData;
	}

	public void setReadingData(double readingData) {
		this.readingData = readingData;
	}


	

}
