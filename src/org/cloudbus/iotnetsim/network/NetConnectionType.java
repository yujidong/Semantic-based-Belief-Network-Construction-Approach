package org.cloudbus.iotnetsim.network;

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

public class NetConnectionType {
	
	private String connectionTypeId;
	private NetConnectionType connectionType;
	private double range;
	private double capacity;
	private double powerModel;
	private String trafficManagement;
	
	public NetConnectionType() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NetConnectionType(String connectionTypeId, 
			NetConnectionType connectionType, 
			double range, 
			double capacity,
			double powerModel, 
			String trafficManagement) {
		super();
		this.connectionTypeId = connectionTypeId;
		this.connectionType = connectionType;
		this.range = range;
		this.capacity = capacity;
		this.powerModel = powerModel;
		this.trafficManagement = trafficManagement;
	}
	
	
	public String getConnectionTypeId() {
		return connectionTypeId;
	}
	public void setConnectionTypeId(String connectionTypeId) {
		this.connectionTypeId = connectionTypeId;
	}
	public NetConnectionType getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(NetConnectionType connectionType) {
		this.connectionType = connectionType;
	}
	public double getRange() {
		return range;
	}
	public void setRange(double range) {
		this.range = range;
	}
	public double getCapacity() {
		return capacity;
	}
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
	public double getPowerModel() {
		return powerModel;
	}
	public void setPowerModel(double powerModel) {
		this.powerModel = powerModel;
	}
	public String getTrafficManagement() {
		return trafficManagement;
	}
	public void setTrafficManagement(String trafficManagement) {
		this.trafficManagement = trafficManagement;
	}

}
