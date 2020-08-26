package org.cloudbus.iotnetsim.iot.nodes;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.iotnetsim.IoTNodePower;
import org.cloudbus.iotnetsim.IoTNodeType;
import org.cloudbus.iotnetsim.Location;
import org.cloudbus.iotnetsim.network.NetConnection;

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

public abstract class IoTNode extends SimEntity {
	
	private Location location;
	private IoTNodeType nodeType ;
	private NetConnection connection;
	private IoTNodePower power;
	private int forwardNodeId;
	private String forwardNodeName;


	public IoTNode(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	
	public IoTNode(String name, 
			Location location, IoTNodeType nodeType, NetConnection connection, IoTNodePower power, 
			int forwardNodeId) {
		
		super(name);
		
		this.location = location;
		this.nodeType = nodeType;
		this.connection = connection;
		this.power = power;
		this.forwardNodeId = forwardNodeId;
	}

	public IoTNode(String name, 
			Location location, IoTNodeType nodeType, NetConnection connection, IoTNodePower power, 
			String forwardNodeName) {
		
		super(name);
		
		this.location = location;
		this.nodeType = nodeType;
		this.connection = connection;
		this.power = power;
		this.forwardNodeId = CloudSim.getEntityId(forwardNodeName);
		this.forwardNodeName = forwardNodeName;
	}

	public void startEntity() {
		// TODO Auto-generated method stub
		Log.printLine(getName() + " is starting...");
	}

	@Override
	public void shutdownEntity() {
		// TODO Auto-generated method stub
		Log.printLine(getName() + " is shutting down...");		
	}
	
	@Override
	public void processEvent(SimEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getTag()) {
		// Execute sending sensor data 
		case CloudSimTags.IOT_NODE_SEND_DATA_EVENT:
			processSendData(ev);
			break;

		case CloudSimTags.IOT_NODE_RECEIVE_DATA_EVENT:
			processReceiveData(ev);
			break;

		// other unknown tags are processed by this method
		default:
			processOtherEvent(ev);
			break;
		}				
	}

	protected void processSendData(SimEvent ev) {

	}
	
	protected void processReceiveData(SimEvent ev) {

	}
	
	protected void processOtherEvent(SimEvent ev) {
		if (ev == null) {
			Log.printLine(getName() + ".processOtherEvent(): " + "Error - an event is null.");
			return;
		}

		Log.printLine(getName() + ".processOtherEvent(): " + "Error - event unknown by this Sensor.");
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public IoTNodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(IoTNodeType nodeType) {
		this.nodeType = nodeType;
	}

	public NetConnection getConnection() {
		return connection;
	}

	public void setConnection(NetConnection connection) {
		this.connection = connection;
	}

	public IoTNodePower getPower() {
		return power;
	}

	public void setPower(IoTNodePower power) {
		this.power = power;
	}

	public int getForwardNodeId() {
		return forwardNodeId;
	}

	public String getForwardNodeName() {return forwardNodeName;}

	public void setForwardNodeId(int forwardNodeId) {
		this.forwardNodeId = forwardNodeId;
	}
	
	
}
