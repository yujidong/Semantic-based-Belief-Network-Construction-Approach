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

public class NetConnection {
	
	private String connectionId;
	private NetConnectionType connectionType;
	private Double currentSignalStrength;

	public NetConnection() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NetConnection(String connectionId, 
			NetConnectionType connectionType, 
			Double currentSignalStrength) {
		super();
		this.connectionId = connectionId;
		this.connectionType = connectionType;
		this.currentSignalStrength = currentSignalStrength;
	}

	public NetConnectionType getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(NetConnectionType connectionType) {
		this.connectionType = connectionType;
	}

	public Double getCurrentSignalStrength() {
		return currentSignalStrength;
	}

	public void setCurrentSignalStrength(Double currentSignalStrength) {
		this.currentSignalStrength = currentSignalStrength;
	}

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}

}
