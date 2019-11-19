package org.cloudbus.iotnetsim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.iotnetsim.iot.nodes.SensorType;

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
 * Class IoTDatacenter extends CloudSim Datacenter, with IoT-related capabilities.
 * 
 * @author Maria Salama
 * 
 */

public class IoTDatacenter extends Datacenter {

	protected Map<SensorType, Map<Double, Double>> readingsData; 		//storing data, k: SensorType, v: (reading time, reading value) 
	
	
	/**
	 * Allocates a new IoTDatacenter object.
	 * 
	 * @param name the name to be associated with this entity (as required by Sim_entity class from
	 *            simjava package)
	 * @param characteristics an object of DatacenterCharacteristics
	 * @param storageList a LinkedList of storage elements, for data simulation
	 * @param vmAllocationPolicy the vmAllocationPolicy
	 * @throws Exception This happens when one of the following scenarios occur:
	 *             <ul>
	 *             <li>creating this entity before initializing CloudSim package
	 *             <li>this entity name is <tt>null</tt> or empty
	 *             <li>this entity has <tt>zero</tt> number of PEs (Processing Elements). <br>
	 *             No PEs mean the Cloudlets can't be processed. A CloudResource must contain one or
	 *             more Machines. A Machine must contain one or more PEs.
	 *             </ul>
	 * @pre name != null
	 * @pre resource != null
	 * @post $none
	 */
	@SuppressWarnings("unchecked")
	public IoTDatacenter(
			String name,
			DatacenterCharacteristics characteristics,
			VmAllocationPolicy vmAllocationPolicy,
			List<Storage> storageList,
			double schedulingInterval) 
					throws Exception 
	{
		super(name, characteristics, vmAllocationPolicy, storageList, schedulingInterval);

		// initialise data structures
		readingsData = new HashMap<SensorType, Map<Double, Double>>();
	}

	/**
	 * Processes events or services that are available for this Datacenter.
	 * 
	 * @param ev a Sim_event object
	 * @pre ev != null
	 * @post $none
	 */
	@Override
	public void processEvent(SimEvent ev) {
		super.processEvent(ev);

		switch (ev.getTag()) {
		// process receiving data from Gateway Node
		case CloudSimTags.IOT_CLOUD_RECEIVE_DATA_EVENT:
			receiveAndStoreData(ev);
			break;
		//process data received (storing, alerting..)
		case CloudSimTags.IOT_CLOUD_PROCESS_DATA_EVENT:
			processData(ev);
			break;

		}
	}
	
	@SuppressWarnings("unchecked")
	public void receiveAndStoreData(SimEvent ev) {
		Map<SensorType, Map<Double, Double>> evdata = new HashMap<SensorType, Map<Double, Double>>();
		evdata = (Map<SensorType, Map<Double, Double>>) ev.getData();
		
		int senderId = ev.getSource();
		
		Log.printLine(CloudSim.clock() + ": [" + getName() + "] is receiving aggregated data from GatewayNode " + CloudSim.getEntityName(senderId));

		//create a data structure for each SensorType
		for (SensorType t : SensorType.values()) {
			readingsData.computeIfAbsent(t, ignored -> new HashMap<>());
		}

		//put data entries for each SensorType
		for (Map.Entry<SensorType, Map<Double, Double>> e : evdata.entrySet()) {
			readingsData.get(e.getKey()).putAll(e.getValue());
		}
		//evdata.forEach((t, readings) -> readingsData.putIfAbsent(t, readings));
		
		//storing data
		Log.printLine(CloudSim.clock() + ": [" + getName() + "] is storing the readings: ");
		evdata.forEach((t, readings) -> Log.printLine("Sensor Type: " + t.name() 
											+ " values: " + readings.values().toString() ));	
		Log.printLine();
	}

	public void processData(SimEvent ev) {

	}

	public Map<SensorType, Map<Double, Double>> getReadingsData() {
		return readingsData;
	}

	public void setReadingsData(Map<SensorType, Map<Double, Double>> readingsData) {
		this.readingsData = readingsData;
	}



}
