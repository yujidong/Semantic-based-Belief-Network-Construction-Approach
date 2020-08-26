package org.cloudbus.iotnetsim.iot.nodes;

import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.iotnetsim.IoTNodePower;
import org.cloudbus.iotnetsim.IoTNodeType;
import org.cloudbus.iotnetsim.Location;
import org.cloudbus.iotnetsim.network.NetConnection;

import java.util.Map;

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

public class MobileSensor extends SensorNode implements IoTNodeMobile {
	
	private Location currentLocation;
	private Map<Integer, Location> locationTrack;


	public MobileSensor(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public MobileSensor(String name, 
			Location location, IoTNodeType nodeType, NetConnection connection, IoTNodePower power, String forwardNodeName) {
		
		super(name, location, nodeType, connection, power, forwardNodeName);
		// TODO Auto-generated constructor stub
		
		this.currentLocation = location;
	}

	public MobileSensor(String name, 
			Location location, IoTNodeType nodeType, NetConnection connection, IoTNodePower power, String forwardNodeName, 
			SensorType sensorType, double readingInterval, String readingsFile) {
		
		super(name, location, nodeType, connection, power, forwardNodeName, sensorType, readingInterval, readingsFile);
		// TODO Auto-generated constructor stub
		
		this.currentLocation = location;
	}

	@Override
	public void processEvent(SimEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getTag()) {
			// Execute sending sensor data
			case CloudSimTags.IOT_SENSOR_SEND_DATA_EVENT:
				processSendReadingsData();
				break;
			case CloudSimTags.IOT_SENSOR_MOVE_EVENT:
				moveNodeAndChangeLinkNode();
				break;

			// other unknown tags are processed by this method
			default:
				processOtherEvent(ev);
				break;
		}
	}

	public void changeAltitude(double newZ) {
		
	}

	public void moveNodeAndChangeLinkNode() {

	}
	
	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}


}
