package org.cloudbus.iotnetsim.iot.nodes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	private Map<String, Map<Integer, Double>> semanticBasedDataAggregation;   // storing fussion data, k: semantic, v: fussion data
	private Map<Integer, Map<Integer, Double>> sensorBelieves;        //storing sensor uncertainty for each sensor based on time
	private Map<String, Map<Integer, Map<Integer, SensorReading>>> semanticBasedReadingDataCollected;   // semantic, sensorId, index of time, sensorReading
	private int gatewayClock = 1;

	private double faultDetect = 0.4;

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
		sensorBelieves = new HashMap<Integer, Map<Integer, Double>>();
		semanticBasedDataAggregation = new HashMap<String, Map<Integer, Double>>();
		semanticBasedReadingDataCollected = new HashMap<String, Map<Integer, Map<Integer, SensorReading>>>();
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
		sensorBelieves = new HashMap<Integer, Map<Integer, Double>>();
		semanticBasedDataAggregation = new HashMap<String, Map<Integer, Double>>();
		semanticBasedReadingDataCollected = new HashMap<String, Map<Integer, Map<Integer, SensorReading>>>();
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

//			for(SensorType sensorType : readingsDataAggregated.keySet()) {
//				Log.printLine(sensorType + ": ");
//				for(Map.Entry<Double, Double> entry : readingsDataAggregated.get(sensorType).entrySet()) {
//					Log.printLine("at " + entry.getKey() + " is value of " + entry.getValue());
//				}
//			}



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

		Map<SensorType, ArrayList<SensorReading>> readingsDataCollected = new HashMap<SensorType, ArrayList<SensorReading>>();


		//create an array list for each SensorType 
		for (SensorType t : SensorType.values()) {
			readingsDataCollected.computeIfAbsent(t, ignored -> new ArrayList<>());
		}

		//add readings received to each corresponding SensorType
		for (SensorReading r : readingsDataReceived) {
			SensorNode s = (SensorNode) CloudSim.getEntity(r.getSensorId());
			readingsDataCollected.get(s.getSensorType()).add(r);
			for(SensorReading sensorReading : readingsDataReceived) {
				if(!semanticBasedReadingDataCollected.containsKey(sensorReading.getSemantic())) {
					semanticBasedReadingDataCollected.put(sensorReading.getSemantic(), new HashMap<Integer, Map<Integer, SensorReading>>());
				} else if(!semanticBasedReadingDataCollected.get(sensorReading.getSemantic()).containsKey(sensorReading.getSensorId())){
					semanticBasedReadingDataCollected.get(sensorReading.getSemantic()).put(sensorReading.getSensorId(), new HashMap<Integer, SensorReading>());
				} else if(!semanticBasedReadingDataCollected.get(sensorReading.getSemantic()).get(sensorReading.getSensorId())
						.containsKey(sensorReading.getDataIndex())) {
					semanticBasedReadingDataCollected.get(sensorReading.getSemantic()).get(sensorReading.getSensorId())
							.put(sensorReading.getDataIndex(), sensorReading);
				} else {
					semanticBasedReadingDataCollected.get(sensorReading.getSemantic()).get(sensorReading.getSensorId())
							.put(sensorReading.getDataIndex(), sensorReading);
				}
				sensorBelieves.computeIfAbsent(sensorReading.getSensorId(), ignore -> new HashMap<Integer, Double>());
				if(!sensorBelieves.containsKey(sensorReading.getSensorId())) {
					sensorBelieves.put(sensorReading.getSensorId(), new HashMap<Integer, Double>());
				} else if(!sensorBelieves.get(sensorReading.getSensorId()).containsKey(sensorReading.getDataIndex())) {
					sensorBelieves.get(sensorReading.getSensorId()).put(sensorReading.getDataIndex(), sensorReading.getBeliefRate());
				}
			}
		}
		
		double readingTime = readingsDataReceived.get(0).getReadingTime();
		gatewayClock = readingsDataReceived.get(0).getDataIndex();
		double evaluatedValue2 = 0;

		for(String semantic : semanticBasedReadingDataCollected.keySet()) {
			double evaluateValue = 0;
			double beliefRate = 0;
			for(Map.Entry<Integer, Map<Integer, SensorReading>> matchedSensor : semanticBasedReadingDataCollected.get(semantic).entrySet()) {
				evaluateValue = evaluateValue + matchedSensor.getValue().get(gatewayClock).getReadingData()
						* matchedSensor.getValue().get(gatewayClock).getBeliefRate();
				beliefRate = beliefRate + matchedSensor.getValue().get(gatewayClock).getBeliefRate();
				Log.printLine("The data from sensor " + matchedSensor.getKey() + " is " + evaluateValue + " belief " + beliefRate);
			}
			if(beliefRate != 0) {
				evaluateValue = evaluateValue / beliefRate;
				evaluatedValue2 = evaluateValue;
			} else {
				evaluateValue = 0;
			}
			Log.printLine("The evaluated value is: " + evaluateValue);
			double newBlief = 0;
			int infectLength = 7;
			double infectRateCurrent = 0.3;
			double infectRateInitial = 0.4;
			for(Map.Entry<Integer, Map<Integer, SensorReading>> matchedSensor : semanticBasedReadingDataCollected.get(semantic).entrySet()) {
				SensorNode sensorNode = (SensorNode) CloudSim.getEntity(matchedSensor.getKey());
				if(!sensorNode.isStrongSensor()) {
					newBlief = 1 - Math.abs((evaluateValue - matchedSensor.getValue().get(gatewayClock).getReadingData()) / evaluateValue);
					double piBlief = 0;
					if (gatewayClock <= infectLength) {
						for (int i = 1; i <= gatewayClock; i++) {
							piBlief = piBlief + sensorBelieves.get(matchedSensor.getKey()).get(i);
						}
						newBlief = newBlief * infectRateCurrent + (1 - infectRateCurrent - infectRateInitial) * piBlief / gatewayClock
						+ matchedSensor.getValue().get(1).getBeliefRate() * infectRateInitial;
					} else {
						for (int i = gatewayClock; i > gatewayClock - infectLength; i--) {
							piBlief = piBlief + sensorBelieves.get(matchedSensor.getKey()).get(i);
						}
						if(newBlief * infectRateCurrent + (1 - infectRateCurrent) * piBlief / infectLength < faultDetect) {
							Log.printLine("The sensor " + sensorNode.getName() + " has potential problem, please check.");
							newBlief = newBlief * 0.1;
						} else {
							newBlief = newBlief * infectRateCurrent + (1 - infectRateCurrent - infectRateInitial) * piBlief / infectLength
									+ matchedSensor.getValue().get(1).getBeliefRate() * infectRateInitial;
						}
					}
					sensorBelieves.get(matchedSensor.getKey()).put(gatewayClock + 1, newBlief);
				} else {
					sensorBelieves.get(matchedSensor.getKey()).put(gatewayClock + 1, sensorBelieves.get(matchedSensor.getKey()).get(gatewayClock));
				}
				if(newBlief < 0) {
					Log.printLine("Strange thing happened. evaluateValue: " + evaluateValue + " sending data: " + matchedSensor.getValue().get(gatewayClock).getReadingData());
				}
				Log.printLine("The sensor " + CloudSim.getEntityName(matchedSensor.getKey()) + " sent data of " + matchedSensor.getValue().get(gatewayClock).getReadingData() + " has new belief of " + newBlief
				+ " at time index of " + String.valueOf(gatewayClock + 1));
			}
		}

		for(SensorType sensorType : readingsDataCollected.keySet()) {
			double evaluatedValue = 0;
			double beliefRate = 0;
			for(SensorReading sensorReading : readingsDataCollected.get(sensorType)) {
				evaluatedValue = evaluatedValue + sensorReading.getReadingData() * sensorReading.getBeliefRate();
				beliefRate = beliefRate + sensorReading.getBeliefRate();
			}
			if(beliefRate != 0) {
				evaluatedValue = evaluatedValue / beliefRate;
				evaluatedValue2 = evaluatedValue;
			} else {
				evaluatedValue = 0;
			}
			readingsDataAggregated.computeIfAbsent(sensorType, data -> new HashMap<>())
					.put(readingTime, evaluatedValue);

//			for(SensorReading sensorReading : readingsDataCollected.get(sensorType)) {
//				sensorBelieves.computeIfAbsent(sensorReading.getSensorId(), ignore -> new HashMap<Integer, Double>());
//				sensorBelieves.get(sensorReading.getSensorId())
//						.put(sensorReading.getDataIndex(), 1 - Math.abs((evaluatedValue - sensorReading.getReadingData())/evaluatedValue));
//			}
		}



		for(SensorReading sensorReading : readingsDataReceived) {
			scheduleNow(sensorReading.getSensorId(),
					CloudSimTags.IOT_SENSOR_UPDATE_Belief, sensorBelieves.get(sensorReading.getSensorId()).get(gatewayClock+1));
			recordData(CloudSim.getEntityName(sensorReading.getSensorId()),
					sensorReading.getDataIndex() + " ; " +
							String.format("%.02f",sensorReading.getReadingData()) + " ; " +
							String.format("%.02f",evaluatedValue2) + " ; " +
							String.format("%.02f",sensorReading.getRealData()) + " ; " +
							String.format("%.02f",sensorReading.getBeliefRate()) );
			// day, timeslot, reading data, evaluated data, real data, uncertainty
		}
		gatewayClock++;

	}

	public void recordData(String sensorName, String data) {
		BufferedWriter record = null;
		try {
			File file = new File(sensorName + ".csv");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter recordData = new FileWriter(file,true);
			record = new BufferedWriter(recordData);
			record.write(data + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try{
				if(record!=null)
					record.close();
			}catch(Exception ex){
				System.out.println("Error in closing the BufferedWriter"+ex);
			}
		}
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
