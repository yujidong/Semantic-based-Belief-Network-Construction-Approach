package org.cloudbus.iotnetsim.iot.nodes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

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

public class SensorNode extends IoTNode {
	private static double DEFAULT_BELIEF = 0.9;
	int patrolIndex = 0;
	private boolean ifMoving = false;
	private SensorType sensorType;
	private double readingInterval;			//send readings every x seconds
	private String readingsFile;
	//private List<Double> readingsDataset;	//to store readings data from a dataset
	//private Map<Date, ArrayList<Double>> readingsDataset; 			//to store readings data from a dataset with dates
	private Map<Integer, ArrayList<Double>> readingsDataset; 			//to store readings data from a dataset with days number
	private Map<Integer, ArrayList<Double>> realDataset;
	private int currentReadingDay;
	private int currentReadingIndex;
	private String forwardNodeName;
	private double beliefRate;
	private Random beliefGenerator;
	private Location location;
	private String semantic;
	private List<Location> locationTrack;
	private int numSendData = 1;
	private boolean strongSensor = false;


	public SensorNode(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public SensorNode(String name, 
			Location location, IoTNodeType nodeType, NetConnection connection, IoTNodePower power, String forwardNodeName) {

		super(name, location, nodeType, connection, power, forwardNodeName);
		// TODO Auto-generated constructor stub
	}

	public SensorNode(String name, 
			Location location, IoTNodeType nodeType, NetConnection connection, IoTNodePower power, String forwardNodeName,
			SensorType sensorType, double readingInterval, String readingsFile) {
		this(name, location, nodeType, connection, power, forwardNodeName, sensorType, readingInterval, readingsFile,
				DEFAULT_BELIEF, false, new ArrayList<Location>(), false);
	}

	public SensorNode(String name,
					  Location location, IoTNodeType nodeType, NetConnection connection, IoTNodePower power, String forwardNodeName,
					  SensorType sensorType, double readingInterval, String readingsFile, double beliefRate,
					  boolean ifMoving, List<Location> locations, boolean strongSensor) {

		super(name, location, nodeType, connection, power, forwardNodeName);

		//initialise data structures
		//this.readingsDataset = new LinkedList<Double>();
		//this.readingsDataset = new HashMap<Date, ArrayList<Double>>();
		this.readingsDataset = new HashMap<Integer, ArrayList<Double>>();
		this.realDataset = new HashMap<Integer, ArrayList<Double>>();

		this.sensorType = sensorType;
		this.readingInterval = readingInterval;
		this.readingsFile = readingsFile;
		this.currentReadingDay = 1;
		this.currentReadingIndex = 0;
		this.beliefRate = beliefRate;
		this.beliefGenerator = new Random();
		this.location = location;
		this.semantic = sensorType.toString() + "@" + location.toString();
		this.ifMoving = ifMoving;
		this.strongSensor = strongSensor;
		this.locationTrack = locations;
	}



	@Override
	public void startEntity() {
		// TODO Auto-generated method stub
		Log.printLine(getName() + " is starting...");

		try {
			getReadingsFromDataset();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// schedule the first event for sending data
		schedule(this.getId(), readingInterval, CloudSimTags.IOT_SENSOR_SEND_DATA_EVENT);
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
		case CloudSimTags.IOT_SENSOR_SEND_DATA_EVENT:
			processSendReadingsData();
			break;
//		case CloudSimTags.IOT_SENSOR_MOVE_EVENT:
//			moveNodeAndChangeLinkNode();
//			break;
		case CloudSimTags.IOT_SENSOR_UPDATE_Belief:
			updateBelief(ev);
			break;
			// other unknown tags are processed by this method
		default:
			processOtherEvent(ev);
			break;
		}				
	}

	private void updateBelief(SimEvent ev) {
		beliefRate = (Double) ev.getData();
		Log.printLine("The sensor " + this.getName() + " update the belief to " + beliefRate);
	}

	public void getReadingsFromDataset() throws ParseException {
		try {
			String line = null;
			Log.printLine("Get readings from dataset file " + this.readingsFile);
			BufferedReader reader = new BufferedReader(new FileReader(this.readingsFile));

			while((line = reader.readLine()) != null) {
				String[] v = line.split(";");

				//Date k = new SimpleDateFormat("dd/MM/y").parse(v[0]);
				//readingsDataset.computeIfAbsent(k, ignored -> new ArrayList<Double>());
				int dayNo = readingsDataset.size()+1;
				readingsDataset.computeIfAbsent(dayNo, ignored -> new ArrayList<Double>());
				realDataset.computeIfAbsent(dayNo, ignored -> new ArrayList<Double>());

				for (int i=0; i<(24/(readingInterval/60/60)); i++) {		//get required number of readings according the readingInterval				
					//readingsDataset.get(k).add(Double.parseDouble(v[i+1]));
					//readingsDataset.get(dayNo).add(Double.parseDouble(v[i+1]));
					double actualValue = Double.parseDouble(v[i+1]);
					readingsDataset.get(dayNo).add(actualValue + beliefGenerator.nextGaussian()*(1-beliefRate)*actualValue);
					realDataset.get(dayNo).add(actualValue);
				}

			}

			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error reading file '" +  this.readingsFile + "'");  
			e.printStackTrace();
		}   
	}

	public void moveNodeAndChangeLinkNode() {
		patrolIndex++;
		location = locationTrack.get(patrolIndex);
		for(SimEntity simEntity : CloudSim.getEntityList()) {
			if(simEntity.getName().contains("RelayNode")) {
				LinkNode newLinkNode = (LinkNode) simEntity;
				if(newLinkNode.getLocation().equals(location)) {
					forwardNodeName = newLinkNode.getName();
				}
			}
		}
	}


	public void processSendReadingsData() {

		Double readingValue = getNextReading();
		Double readingRealValue = getNextRealValue();
		
		SensorReading reading = new SensorReading(
				this.getId(), 
				this.getCurrentReadingDay(), 
				this.getCurrentReadingIndex(), 
				CloudSim.clock(), 
				readingValue,
				beliefRate,
				semantic,
				readingRealValue,
				numSendData
				);

		Log.printLine(CloudSim.clock() + ": [" + this.getName() + "] is sending ReadingData no. " + currentReadingIndex 
				+ " for Day " + currentReadingDay
				+ " with value of " + Double.toString(readingValue) 
				+ " to " + CloudSim.getEntityName(getForwardNodeId())
				+ " with belief rate of " + beliefRate);

		//send data to Link Node
		schedule(getForwardNodeId(), CloudSim.getMinTimeBetweenEvents(), CloudSimTags.IOT_LINK_RECEIVE_DATA_EVENT, reading);

		numSendData++;

		if(ifMoving) {
			if(patrolIndex < locationTrack.size() - 1) {
				moveNodeAndChangeLinkNode();
			} else {
				patrolIndex = -1;
				moveNodeAndChangeLinkNode();
			}
		}

		if (currentReadingDay < configurations.ExperimentsConfigurations.EXP_NO_OF_DAYS) {
			// schedule the next event for sending data 
			scheduleNextReading();
		}
	}

	private double getNextReading() {
		//double nextReading = readingsDataset.get(currentReadingIndex);
		double nextReading = readingsDataset.get(currentReadingDay).get(currentReadingIndex);

//		if (currentReadingIndex < (24/(readingInterval/60/60))-1) {		//to get number of readings required for this day
//			currentReadingIndex +=1;
//		} else {	//reset the index to 0 t start a new day
//			currentReadingDay +=1;
//			currentReadingIndex = 0;
//		}
		return nextReading;
	}

	private double getNextRealValue() {
		double nextReading = realDataset.get(currentReadingDay).get(currentReadingIndex);

		if (currentReadingIndex < (24/(readingInterval/60/60))-1) {		//to get number of readings required for this day
			currentReadingIndex +=1;
		} else {	//reset the index to 0 t start a new day
			currentReadingDay +=1;
			currentReadingIndex = 0;
		}
		return nextReading;
	}

	private void scheduleNextReading(){
		schedule(this.getId(), this.getReadingInterval(), CloudSimTags.IOT_SENSOR_SEND_DATA_EVENT);
	}

	protected void processOtherEvent(SimEvent ev) {
		if (ev == null) {
			Log.printLine(getName() + ".processOtherEvent(): " + "Error - an event is null.");
			return;
		}

		Log.printLine(getName() + ".processOtherEvent(): " + "Error - event unknown by this Sensor.");
	}

	public SensorType getSensorType() {
		return sensorType;
	}

	public void setSensorType(SensorType sensorType) {
		this.sensorType = sensorType;
	}

	public double getReadingInterval() {
		return readingInterval;
	}

	public void setReadingInterval(double readingInterval) {
		this.readingInterval = readingInterval;
	}

	public String getReadingsFile() {
		return readingsFile;
	}

	public void setReadingsFile(String readingsFile) {
		this.readingsFile = readingsFile;
	}

	public Map<Integer, ArrayList<Double>> getReadingsDataset() {
		return readingsDataset;
	}

	public void setReadingsDataset(Map<Integer, ArrayList<Double>> readingsDataset) {
		this.readingsDataset = readingsDataset;
	}

	public int getCurrentReadingDay() {
		return currentReadingDay;
	}

	public void setCurrentReadingDay(int currentReadingDay) {
		this.currentReadingDay = currentReadingDay;
	}

	public int getCurrentReadingIndex() {
		return currentReadingIndex;
	}

	public void setCurrentReadingIndex(int currentReadingIndex) {
		this.currentReadingIndex = currentReadingIndex;
	}

	public boolean isStrongSensor() {
		return strongSensor;
	}
}
