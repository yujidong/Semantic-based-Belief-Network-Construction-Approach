package org.cloudbus.iotnetsim.iot.nodes;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
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
 * A LinkNode is an IoT node for forwarding data received from Sensor nodes to the next node in the network.
 * 
 * @author Maria Salama
 * 
 */

public class LinkNode extends IoTNode {

	private double forwardInterval;			//send readings every x seconds
	
	private List<SensorReading> readingsData;
	

	public LinkNode(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public LinkNode(String name, 
			Location location, IoTNodeType nodeType, NetConnection connection, IoTNodePower power, String forwardNodeName) {
		
		super(name, location, nodeType, connection, power, forwardNodeName);
		// TODO Auto-generated constructor stub
	}

	
	public LinkNode(String name, 
			Location location, IoTNodeType nodeType, NetConnection connection, IoTNodePower power, String forwardNodeName,
			double forwardInterval) {
	
		super(name, location, nodeType, connection, power, forwardNodeName);
		
		this.forwardInterval = forwardInterval;
		
		readingsData = new ArrayList<SensorReading>();
	}

	@Override
	public void startEntity() {
		// TODO Auto-generated method stub
		Log.printLine(getName() + " is starting...");				
		
		// schedule the first event for sending data
		schedule(this.getId(), forwardInterval, CloudSimTags.IOT_LINK_FORWARD_DATA_EVENT);
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
		case CloudSimTags.IOT_LINK_RECEIVE_DATA_EVENT:
			processReceiveData(ev);
			break;
		case CloudSimTags.IOT_LINK_FORWARD_DATA_EVENT:
			processForwardData();
			break;

		// other unknown tags are processed by this method
		default:
			processOtherEvent(ev);
			break;
		}				
	}

	public void processReceiveData(SimEvent ev) {
		SensorReading evdata = (SensorReading) ev.getData(); 
		
		if (readingsData.size() > 0) {
			if (readingsData.get(readingsData.size()-1).getReadingTime() < evdata.getReadingTime()) {	//a new set of readings
				readingsData.clear();
			}
		}
		
		readingsData.add(evdata);
		
		Log.printLine(CloudSim.clock() + ": [" + getName() + "] is receiving reading data from Sensor " + CloudSim.getEntityName(evdata.getSensorId()) 
				+ " of value " + evdata.getReadingData());
	}
	
	public void processForwardData() {
		if (readingsData.size() > 0) {
			Log.printLine(CloudSim.clock() + ": [" + getName() + "] is forwarding reading data to " + CloudSim.getEntityName(getForwardNodeId()));

			//forward data to Gateway Node
			schedule(getForwardNodeId(), CloudSim.getMinTimeBetweenEvents(), CloudSimTags.IOT_GATEWAY_RECEIVE_DATA_EVENT, readingsData);

			if (readingsData.get(readingsData.size()-1).getReadingDay() < configurations.ExperimentsConfigurations.EXP_NO_OF_DAYS) {
				// schedule the next event for forwarding data 
				scheduleNextForward();
			}
		}
	}

	private void scheduleNextForward(){
		schedule(this.getId(), this.getForwardInterval(), CloudSimTags.IOT_LINK_FORWARD_DATA_EVENT);
	}

	protected void processOtherEvent(SimEvent ev) {
		if (ev == null) {
			Log.printLine(getName() + ".processOtherEvent(): " + "Error - an event is null.");
			return;
		}

		Log.printLine(getName() + ".processOtherEvent(): " + "Error - event unknown by this LinkNode.");
	}

	public double getForwardInterval() {
		return forwardInterval;
	}

	public void setForwardInterval(double forwardInterval) {
		this.forwardInterval = forwardInterval;
	}
	
}
