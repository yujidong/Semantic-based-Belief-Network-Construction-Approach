package org.cloudbus.iotnetsim.iot.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * A Gateway Node is an IoT Node for aggregating data received from IoTodes (e.g. Sensors or LinkNodes) 
 * and sending the aggregated data to the next node in the network (e.g. another GatewayNode or cloud server).
 * 
 * @author Maria Salama
 * 
 */

public class GatewayNode extends IoTNode {

	private double forwardInterval;			//forward data every x seconds
	private double dataProcessingInterval;		//interval for processing data events

	private List<SensorReading> readingsDataReceived; 	//storing data received 
	protected Map<SensorType, Map<Double, Double>> readingsDataAggregated; 		//storing aggregated data, k: SensorType, v: aggregated value  

	
	public GatewayNode(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public GatewayNode(String name, 
			Location location, IoTNodeType nodeType, NetConnection connection, IoTNodePower power, 
			int forwardNodeId) {
		
		super(name, location, nodeType, connection, power, forwardNodeId);
		// TODO Auto-generated constructor stub
	}

	public GatewayNode(String name, 
			Location location, IoTNodeType nodeType, NetConnection connection, IoTNodePower power, 
			String forwardNodeName) {
		
		super(name, location, nodeType, connection, power, forwardNodeName);
		// TODO Auto-generated constructor stub
	}

	public GatewayNode(String name, 
			Location location, IoTNodeType nodeType, NetConnection connection, IoTNodePower power, 
			int forwardNodeId,
			double forwardInterval, double dataProcessingInterval) {
		
		super(name, location, nodeType, connection, power, forwardNodeId);
		
		this.forwardInterval = forwardInterval;
		this.dataProcessingInterval = dataProcessingInterval;
		
		// initialise data structures
		readingsDataReceived = new ArrayList<SensorReading>();
		readingsDataAggregated = new HashMap<SensorType, Map<Double, Double>>();
	}

	public GatewayNode(String name, 
			Location location, IoTNodeType nodeType, NetConnection connection, IoTNodePower power, 
			String forwardNodeName,
			double forwardInterval, double dataProcessingInterval) {
		
		super(name, location, nodeType, connection, power, forwardNodeName);
		
		this.forwardInterval = forwardInterval;
		this.dataProcessingInterval = dataProcessingInterval;
		
		// initialise data structures
		readingsDataReceived = new ArrayList<SensorReading>();
		readingsDataAggregated = new HashMap<SensorType, Map<Double, Double>>();
	}

	@Override
	public void startEntity() {
		// TODO Auto-generated method stub
		Log.printLine(getName() + " is starting...");				
						
		// schedule the first event for sending data
		schedule(this.getId(), this.forwardInterval + CloudSim.getMinTimeBetweenEvents(), CloudSimTags.IOT_GATEWAY_SEND_AGGREGATED_DATA_EVENT); 
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
		// receive data from Link Node
		case CloudSimTags.IOT_GATEWAY_RECEIVE_DATA_EVENT:
			receiveAndStoreData(ev);
			break;
		case CloudSimTags.IOT_GATEWAY_SEND_AGGREGATED_DATA_EVENT:
			sendAggregatedData();
			break;
		case CloudSimTags.IOT_GATEWAY_PROCESS_DATA_EVENT:
			processData();
			break;

		// other unknown tags are processed by this method
		default:
			processOtherEvent(ev);
			break;
		}				
	}

	@SuppressWarnings("unchecked")
	public void receiveAndStoreData(SimEvent ev) {
		List<SensorReading> evdata = new ArrayList<SensorReading>();
		evdata = (ArrayList<SensorReading>) ev.getData();
		
		int senderId = ev.getSource();

		this.readingsDataReceived.clear();

		if (evdata.size() > 0) {
			Log.printLine(CloudSim.clock() + ": [" + getName() + "] is receiving readings data from LinkNode " + CloudSim.getEntityName(senderId));
			
			evdata.forEach(item -> this.readingsDataReceived.add(item));
		} 
	}
	
	public void sendAggregatedData() { 
		if (readingsDataReceived.size() > 0) {
			aggregateData();

			Log.printLine(CloudSim.clock() + ": [" + getName() + "] is sending aggregated data to " + CloudSim.getEntityName(getForwardNodeId()));

			//send aggregated data to the Cloud
			schedule(this.getForwardNodeId(), CloudSim.getMinTimeBetweenEvents(), CloudSimTags.IOT_CLOUD_RECEIVE_DATA_EVENT, readingsDataAggregated);

			if (readingsDataReceived.get(readingsDataReceived.size()-1).getReadingDay() < configurations.ExperimentsConfigurations.EXP_NO_OF_DAYS) {
				// schedule the next event for forwarding data 
				scheduleNextForward();
			}
		}		
	}

	public void aggregateData() {
		Log.printLine(CloudSim.clock() + ": [" + getName() + "] is aggregating data received...");

		//clear data previously stored 
		readingsDataAggregated.clear();

		Map<SensorType, ArrayList<Double>> readingsDataCollected = new HashMap<SensorType, ArrayList<Double>>();

		//create an array list for each SensorType 
		for (SensorType t : SensorType.values()) {
			readingsDataCollected.computeIfAbsent(t, ignored -> new ArrayList<>());
		}

		//add readings received to each corresponding SensorType
		for (SensorReading r : readingsDataReceived) {
			SensorNode s = (SensorNode) CloudSim.getEntity(r.getSensorId());
			readingsDataCollected.get(s.getSensorType()).add(r.getReadingData());
		}
		
		double readingTime = readingsDataReceived.get(0).getReadingTime();

		//calculate the average of data readings for each SensorType and store it in readingsDataAggregated 
		readingsDataCollected.forEach((t, readings) -> readingsDataAggregated
				.computeIfAbsent(t, data -> new HashMap<>())
				.put(readingTime, readings.stream().mapToDouble((x) -> x).summaryStatistics().getAverage()));
	}
	
	public void processData() {
		Log.printLine(CloudSim.clock() + ": [" + getName() + "] is processing data..." );

		//TODO add processing data required
		
		
		// schedule the next event for processing data 
		scheduleNextDataProcessing();
	}
	
	private void scheduleNextForward(){
		schedule(this.getId(), this.getForwardInterval() + CloudSim.getMinTimeBetweenEvents(), CloudSimTags.IOT_GATEWAY_SEND_AGGREGATED_DATA_EVENT);
	}

	private void scheduleNextDataProcessing(){
		schedule(this.getId(), this.getDataProcessingInterval(), CloudSimTags.IOT_GATEWAY_PROCESS_DATA_EVENT);
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

	public double getDataProcessingInterval() {
		return dataProcessingInterval;
	}

	public void setDataProcessingInterval(double dataProcessingInterval) {
		this.dataProcessingInterval = dataProcessingInterval;
	}

	
}
