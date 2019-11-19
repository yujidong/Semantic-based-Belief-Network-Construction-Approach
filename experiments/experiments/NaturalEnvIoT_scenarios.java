package experiments;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.adv.AdvHost;
import org.cloudbus.cloudsim.adv.AdvVm;
import org.cloudbus.cloudsim.adv.DynamicDatacenterBroker;
import org.cloudbus.cloudsim.adv.Service;
import org.cloudbus.cloudsim.adv.ServiceRequest;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.iotnetsim.GeographicRegion;
import org.cloudbus.iotnetsim.IoTDatacenter;
import org.cloudbus.iotnetsim.IoTNodePower;
import org.cloudbus.iotnetsim.IoTNodePowerType;
import org.cloudbus.iotnetsim.IoTNodeType;
import org.cloudbus.iotnetsim.Location;
import org.cloudbus.iotnetsim.iot.nodes.GatewayNode;
import org.cloudbus.iotnetsim.iot.nodes.LinkNode;
import org.cloudbus.iotnetsim.iot.nodes.SensorNode;
import org.cloudbus.iotnetsim.iot.nodes.SensorType;
import org.cloudbus.iotnetsim.network.NetConnection;
import org.cloudbus.iotnetsim.network.NetConnectionType;

import configurations.ExperimentsConfigurations;
import helper.Setup;
import helper.Workload;

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
 * NaturalEnvironmentIoT simple experiment
 * simulating the testbed of 1 location
 * running for 1 month
 * for different reading intervals
 *  
 */

public class NaturalEnvIoT_scenarios {
	/**
	 * Creates main() to run this example.
	 *
	 * @param args the args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {

		try {
			String workingDir = System.getProperty("user.dir");
			String datasetsFolder = workingDir + "//experiments//datasets//";
			String inputFolder_workload = workingDir + "/experiments//workload//";

			OutputStream output = new FileOutputStream(workingDir + "//experiments//results//NaturalEnvIoT_scenarios.txt");
			Log.setOutput(output);	//Uncomment to write output log in the txt files

			Log.printLine("Starting NaturalEnvironmentIoT simple experiment...");
			Log.printLine("simulating the testbed of 1 location");
			Log.printLine("running for 1 month");
			Log.printLine("for the 3 different scenarios (dry, rainy, flood)");

			//variable scenario is used to represent the number of scenarios
			for (int i=0; i < ExperimentsConfigurations.READING_INTERVAL.length; i++) {	//scenarios
				Log.printLine("Scenario with reading every " + ExperimentsConfigurations.READING_INTERVAL[i]/60/60 + " hours");
				
				//record the start local time
				LocalTime startTime = java.time.LocalTime.now();

				//initialise the CloudSim package. It should be called before creating any entities.
				int num_user = 1; // number of cloud users
				Calendar calendar = Calendar.getInstance();
				boolean trace_flag = false; // mean trace events

				//initialise the CloudSim library
				CloudSim.init(num_user, calendar, trace_flag);

				//create Hosts
				List<AdvHost> hostList = new ArrayList<AdvHost>();
				hostList = Setup.createAdvHostList(1, 3, 2);  //2 = VmSchedulerTimeShared
				//create Datacenter
				IoTDatacenter datacenter0 = Setup.createIoTDatacenter("Datacenter_0", hostList);

				//create Broker
				DynamicDatacenterBroker broker = Setup.createDynamicBroker();
				int brokerId = broker.getId();

				//create VMs
				List<? extends Vm> vmlist = new ArrayList<AdvVm>();
				int[] vmType = {1};
				int[] vmsNumber = {1};
				vmlist = Setup.createHeterogenousAdvVmList(brokerId, vmsNumber, vmType, 1);
				// submit VMs list to the broker
				broker.submitVmList(vmlist);

				//create ServiceType
				int serviceType = 0;
				int serviceId = 1;
				Service service = Workload.createService(serviceId, serviceType);

				//create Cloudlets
				UtilizationModel utilizationModel = new UtilizationModelFull();
				List<ServiceRequest> cloudletList = new ArrayList<ServiceRequest>();
				//cloudletList = Workload.createServiceRequest(brokerId, serviceId, 100, serviceType, utilizationModel);

				cloudletList = Workload.generateWorkloadRuntime(
						inputFolder_workload + "iot//" + "iot_workload_minimal.txt", ExperimentsConfigurations.WORKLOAD_INTERVAL[i],
						brokerId, serviceId, serviceType, utilizationModel);

				// submit cloudlet list to the broker
				broker.submitServiceRequestList(cloudletList);

				//create one IoT testbed
				createTestbed(datacenter0, configurations.ExperimentsConfigurations.READING_INTERVAL[i], datasetsFolder);

				double lastClock = CloudSim.startSimulation();

				CloudSim.stopSimulation();

				Log.printLine();
				Log.printLine("Experiment finished!");	

				LocalTime finishTime = java.time.LocalTime.now();

				//calculate actual simulation time in Nanoseconds
				Log.printLine();
				Log.printLine("Actual simulation time: " + Duration.between(startTime,finishTime).getNano());
				Log.printLine();

				//get used memory
				Runtime runtime = Runtime.getRuntime();
				Log.printLine("Used memory: " + (runtime.totalMemory() - runtime.freeMemory()/1024*1024));
				Log.printLine();

			} //end scenarios

		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}
	}

	private static void createTestbed(Datacenter datacenter, double readingInterval, String datasetsFolder) {
		//create one IoT testbed
		Log.printLine("Creating one testbed...");
		GeographicRegion region = new GeographicRegion("Conwy_NorthWales", 100.00);

		//create CloudServer
		AdvHost cloudServer = Setup.createAdvHost(100, 3, 2);

		//create GatewayNode
		GatewayNode gatewayNode = new GatewayNode(
				"GatewayNode", 
				new Location(200*100, 200*100, 0), 
				IoTNodeType.GATEWAY_Node, 
				new NetConnection("conn_3G", new NetConnectionType(), 100.00), 
				new IoTNodePower(IoTNodePowerType.CONTINUOUS_POWER, true, false, true, 100.00, 0.00, 0.00),
				datacenter.getId(), 
				readingInterval+CloudSim.getMinTimeBetweenEvents()*3, 
				readingInterval+CloudSim.getMinTimeBetweenEvents()*3);

		//create LinkNode
		LinkNode relayNode = new LinkNode(
				"RelayNode", 
				new Location(300*100, 300*100, 0), 
				IoTNodeType.LINK_NODE, 
				new NetConnection("conn_longRadio", new NetConnectionType(), 100.00), 
				new IoTNodePower(IoTNodePowerType.USB_CAHRGING, true, false, true, 100.00, 0.00, 0.00),
				"GatewayNode", 
				readingInterval+CloudSim.getMinTimeBetweenEvents()*2);

		//create Sensors
		for (int s=1; s<=3; s++) {
			SensorNode tempSensor = new SensorNode(
					"TempSensor"+s, 
					new Location(400+s*100, 400+s*100, 0), 
					IoTNodeType.SENSOR, 
					new NetConnection("conn_shortRadio"+s, new NetConnectionType(), 100.00),
					new IoTNodePower(IoTNodePowerType.BATTERY, false, true, false, 100, 0.1, 10.00),
					"RelayNode", 
					SensorType.AIR_Temperature_SENSOR, 
					readingInterval, 
					datasetsFolder+"ukcp09_mean-temperature_1month.csv");
		}
		for (int s=1; s<=3; s++) {
			SensorNode percipSensor = new SensorNode(
					"PercipSensor"+s, 
					new Location(400+s*100, 400+s*100, 0), 
					IoTNodeType.SENSOR, 
					new NetConnection("conn_shortRadio"+s, new NetConnectionType(), 100.00),
					new IoTNodePower(IoTNodePowerType.BATTERY, false, true, false, 100, 0.1, 10.00),
					"RelayNode", 
					SensorType.WATER_SurfaceFlow_SENSOR, 
					readingInterval, 
					datasetsFolder+"ukcp09_rainfall_1month.csv");
		}
	}

}
