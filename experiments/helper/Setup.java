package helper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.VmSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.adv.AdvHost;
import org.cloudbus.cloudsim.adv.AdvVm;
import org.cloudbus.cloudsim.adv.DynamicDatacenterBroker;
import org.cloudbus.cloudsim.adv.ServiceRequestScheduler;
import org.cloudbus.cloudsim.adv.ServiceRequestSchedulerSpaceShared;
import org.cloudbus.cloudsim.adv.VmAllocationPolicyAdaptation;
import org.cloudbus.cloudsim.power.PowerDatacenterBroker;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.iotnetsim.IoTDatacenter;

/**
 * Title:        SAd/SAw CloudSim Toolkit
 * Description:  Modelling and Simulation of Self-Adaptive & Self-Aware Cloud Architectures 
 * 
 * Author: Maria Salama, University of Birmingham
 * Contact: m.salama@cs.bham.ac.uk
 *
 * If you are using any algorithms, policies or workload included in the SAd/SAw CloudSim Toolkit,
 * please cite the following paper:
 * 
 * M. Salama, R. Bahsoon, and R. Buyya, 
 * Modelling and Simulation Environment for Self-Adaptive and Self-Aware Cloud Architectures. 
 * Simulation Modelling Practice and Theory (revision submitted), 2018.
 * 
 */

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

public class Setup {

	/**
	 * Creates the datacenter for Cloudsim package.
	 */
	public static Datacenter createDatacenter(String name, List<? extends Host> hostList){

		// 5. Create a DatacenterCharacteristics object that stores the properties of a data center: 
		//	  architecture, OS, list of Machines, allocation policy: time- or space-shared, time zone
		//    and its price (G$/Pe time unit).
	
		String arch = "x86";      		// system architecture
		String os = "Linux";          	// operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage = 0.1;	// the cost of using storage in this resource
		double costPerBw = 0.1;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(
					name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	/**
	 * Creates the power datacenter.
	 * 
	 * @param name the name
	 * @param datacenterClass the datacenter class
	 * @param hostList the host list
	 * @param vmAllocationPolicy the vm allocation policy
	 * @param simulationLength
	 * 
	 * @return the power datacenter
	 * 
	 * @throws Exception the exception
	 */
	public static Datacenter createPowerDatacenter(
			String name,
			Class<? extends Datacenter> datacenterClass,
			List<PowerHost> hostList,
			VmAllocationPolicy vmAllocationPolicy) throws Exception {
	
		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		double time_zone = 10.0; // time zone this resource located
		double cost = 3.0; // the cost of using processing in this resource
		double costPerMem = 0.05; // the cost of using memory in this resource
		double costPerStorage = 0.001; // the cost of using storage in this resource
		double costPerBw = 0.0; // the cost of using bw in this resource

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

		Datacenter datacenter = null;
		try {
			datacenter = datacenterClass.getConstructor(
					String.class,
					DatacenterCharacteristics.class,
					VmAllocationPolicy.class,
					List.class,
					Double.TYPE).newInstance(
					name,
					characteristics,
					vmAllocationPolicy,
					new LinkedList<Storage>(),
					Constants.SCHEDULING_INTERVAL);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		return datacenter;
	}


	/**
	 * Creates the datacenter for IoTSim package.
	 */
	public static IoTDatacenter createIoTDatacenter
			(String name, List<? extends Host> hostList){

		// 5. Create a DatacenterCharacteristics object that stores the properties of a data center: 
		//	  architecture, OS, list of Machines, allocation policy: time- or space-shared, time zone
		//    and its price (G$/Pe time unit).

		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		//double cost = 3.0;              // the cost of using processing in this resource
		//double costPerMem = 0.05;		// the cost of using memory in this resource
		//double costPerStorage = 0.1;	// the cost of using storage in this resource
		//double costPerBw = 0.1;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, 
                Constants.COST_CPU[1], Constants.COST_Per_Mem[1], Constants.COST_Per_Storage[1], 
                Constants.COST_Per_Bw[1]);

		
		// 6. Finally, we need to create a PowerDatacenter object.
		IoTDatacenter datacenter = null;
		try {
			datacenter = new IoTDatacenter(
					name, characteristics, new VmAllocationPolicyAdaptation(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	/**
	 * Creates the broker.
	 * 
	 * @return the datacenter broker
	 */
	public static DatacenterBroker createBroker() {
		DatacenterBroker broker = null;
		try {
			broker = new PowerDatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return broker;
	}

	/**
	 * Creates the broker for runtime workload.
	 * 
	 * @return the datacenter broker
	 */
	//We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according to the specific rules of the simulated scenario
	public static DynamicDatacenterBroker createDynamicBroker() {
		DynamicDatacenterBroker broker = null;
		try {
			broker = new DynamicDatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return broker;
	}

	/**
	 * Creates the host list.
	 * 
	 * @param hostsNumber the hosts number
	 * 
	 * @return the list<host>
	 */
	public static List<Host> createHostList(int hostsNumber, int hostType, int vmSchedulerType) {
		
		// 1. We need to create a list to store one or more Machines
		List<Host> hostList = new ArrayList<Host>();

		//4. Create Hosts with its id and list of PEs and add them to the list of machines
		for (int i = 0; i < hostsNumber; i++) {
			// 2. A Machine contains one or more PEs or CPUs/Cores. 
			// Therefore, should create a list to store these PEs before creating a Machine.
			List<Pe> peList = new ArrayList<Pe>();
			for (int j = 0; j < Constants.HOST_PES[hostType]; j++) {
				peList.add(new Pe(j, new PeProvisionerSimple(Constants.HOST_MIPS[hostType])));
			}

			VmScheduler vmScheduler = null;
			switch(vmSchedulerType){
				case 1:	//VmSchedulerSpaceShared
					vmScheduler = new VmSchedulerSpaceShared(peList);
					break;
				case 2:	//VmSchedulerTimeShared
					vmScheduler = new VmSchedulerTimeShared(peList);
					break;
				case 3:	//VmSchedulerOportunisticSpaceShared
					//vmScheduler = new VmSchedulerOportunisticSpaceShared(peList);
					break;
				case 4:	//VmSchedulerTimeSharedOverSubscription
					vmScheduler = new VmSchedulerTimeSharedOverSubscription(peList);
					break;
			}
			
			hostList.add(
	    			new Host(
	    				i,
	    				hostType,
	    				new RamProvisionerSimple(Constants.HOST_RAM[hostType]),
	    				new BwProvisionerSimple(Constants.HOST_BW[hostType]),
	    				Constants.HOST_STORAGE[hostType],
	    				peList,
	    				vmScheduler
	    			)
	    		); 

		}
		return hostList;
	}

	/**
	 * Creates the host list.
	 * 
	 * @param hostsNumber the hosts number
	 * 
	 * @return the list< power host>
	 */
	public static List<PowerHost> createHostList(int hostsNumber) {
		List<PowerHost> hostList = new ArrayList<PowerHost>();
		for (int i = 0; i < hostsNumber; i++) {
			int hostType = i % Constants.HOST_TYPES;

			List<Pe> peList = new ArrayList<Pe>();
			for (int j = 0; j < Constants.HOST_PES[hostType]; j++) {
				peList.add(new Pe(j, new PeProvisionerSimple(Constants.HOST_MIPS[hostType])));
			}

			hostList.add(new PowerHostUtilizationHistory(
					i,
					new RamProvisionerSimple(Constants.HOST_RAM[hostType]),
					new BwProvisionerSimple(Constants.HOST_BW[hostType]),
					Constants.HOST_STORAGE[hostType],
					peList,
					new VmSchedulerTimeSharedOverSubscription(peList),
					Constants.HOST_POWER[hostType]));
		}
		return hostList;
	}


	/**
	 * Creates one host machine..
	 * 
	 * @param 
	 * 
	 * @return 
	 */
	public static AdvHost createAdvHost(int id, int hostType, int vmSchedulerType) {
		List<Pe> peList = new ArrayList<Pe>();
		for (int j = 0; j < Constants.HOST_PES[hostType]; j++) {
			peList.add(new Pe(j, new PeProvisionerSimple(Constants.HOST_MIPS[hostType])));
		}

		VmScheduler vmScheduler = null;
		switch(vmSchedulerType){
		case 1:	//VmSchedulerSpaceShared
			//vmScheduler = new VmSchedulerSpaceShared(peList);
			break;
		case 2:	//VmSchedulerTimeShared
			vmScheduler = new VmSchedulerTimeShared(peList);
			break;
		case 3:	//VmSchedulerOportunisticSpaceShared
			//vmScheduler = new VmSchedulerOportunisticSpaceShared(peList);
			break;
		case 4:	//VmSchedulerTimeSharedOverSubscription
			//vmScheduler = new VmSchedulerTimeSharedOverSubscription(peList);
			break;
		}

		return new AdvHost(
				id, 
				hostType,
				new RamProvisionerSimple(Constants.HOST_RAM[hostType]), 
				new BwProvisionerSimple(Constants.HOST_BW[hostType]), 
				Constants.HOST_STORAGE[hostType], 
				peList, 
				vmScheduler, 
				Constants.HOST_POWER[hostType]);
	}


	/**
	 * Creates the host list for Awareness package..
	 * 
	 * @param hostsNumber the hosts number
	 * 
	 * @return the list< power host>
	 */
	public static List<AdvHost> createAdvHostList(int hostsNumber, int hostType, int vmSchedulerType) {
		List<AdvHost> hostList = new ArrayList<AdvHost>();
		for (int i = 0; i < hostsNumber; i++) {
			List<Pe> peList = new ArrayList<Pe>();
			for (int j = 0; j < Constants.HOST_PES[hostType]; j++) {
				peList.add(new Pe(j, new PeProvisionerSimple(Constants.HOST_MIPS[hostType])));
			}
			
			VmScheduler vmScheduler = null;
			switch(vmSchedulerType){
				case 1:	//VmSchedulerSpaceShared
					//vmScheduler = new VmSchedulerSpaceShared(peList);
					break;
				case 2:	//VmSchedulerTimeShared
					vmScheduler = new VmSchedulerTimeShared(peList);
					break;
				case 3:	//VmSchedulerOportunisticSpaceShared
					//vmScheduler = new VmSchedulerOportunisticSpaceShared(peList);
					break;
				case 4:	//VmSchedulerTimeSharedOverSubscription
					//vmScheduler = new VmSchedulerTimeSharedOverSubscription(peList);
					break;
			}
			
			hostList.add(
					new AdvHost(
							i, 
							hostType,
							new RamProvisionerSimple(Constants.HOST_RAM[hostType]), 
							new BwProvisionerSimple(Constants.HOST_BW[hostType]), 
							Constants.HOST_STORAGE[hostType], 
							peList, 
							vmScheduler, 
							Constants.HOST_POWER[hostType])
					);
		}
		return hostList;
	}


	/**
	 * Creates the VM list.
	 * 
	 * @param brokerId the broker id
	 * @param vmsNumber the vms number
	 * 
	 * @return the list< vm>
	 */
	public static List<Vm> createVM(int userId, int vms, int vmType, int cloudletSchedulerType) {

		//Creates a container to store VMs. This list is passed to the broker later
		LinkedList<Vm> list = new LinkedList<Vm>();

		//VM Parameters
		long size = Constants.VM_SIZE[vmType]; //image size (MB)
		int ram = Constants.VM_RAM[vmType]; //vm memory (MB)
		int mips = Constants.VM_MIPS[vmType];
		long bw = Constants.VM_BW[vmType];
		int pesNumber = Constants.VM_PES[vmType]; //number of cpus
		String vmm = "Xen"; //VMM name

		//create VMs
		Vm[] vm = new Vm[vms];
		
		ServiceRequestScheduler cloudletScheduler = null;
		switch(cloudletSchedulerType){
		case 1:	//CloudletSchedulerSpaceShared
			cloudletScheduler = new ServiceRequestSchedulerSpaceShared();
			break;
		case 2:	//CloudletSchedulerTimeShared
			//cloudletScheduler = new CloudletSchedulerTimeShared();
			break;
		case 3:	//CloudletSchedulerDynamicWorkload
			//cloudletScheduler = new CloudletSchedulerDynamicWorkload(Constants.VM_MIPS[vmType], Constants.VM_PES[vmType]);
			break;
		}

		for(int i=0;i<vms;i++){
			vm[i] = new Vm(i, userId, vmType, mips, pesNumber, ram, bw, size, vmm,cloudletScheduler);
			list.add(vm[i]);
		}

		return list;
	}

	/**
	 * Creates the non-self-aware list for Awareness package..
	 * 
	 * @param brokerId the broker id
	 * @param vmsNumber the vms number
	 * 
	 * @return the list< vm>
	 */
	public static List<AdvVm> createAdvVmList(int brokerId, int vmsNumber, int vmType, int cloudletSchedulerType) {
		List<AdvVm> vms = new ArrayList<AdvVm>();

		for (int i = 0; i < vmsNumber; i++) {
			//int vmType = i / (int) Math.ceil((double) vmsNumber / Constants.VM_TYPES);

			ServiceRequestScheduler cloudletScheduler = null;
			switch(cloudletSchedulerType){
			case 1:	//CloudletSchedulerSpaceShared
				cloudletScheduler = new ServiceRequestSchedulerSpaceShared();
				break;
			case 2:	//CloudletSchedulerTimeShared
				//cloudletScheduler = new ServiceRequestSchedulerTimeShared();
				break;
			case 3:	//CloudletSchedulerDynamicWorkload
				//cloudletScheduler = new CloudletSchedulerDynamicWorkload(Constants.VM_MIPS[vmType], Constants.VM_PES[vmType]);
				break;
			}

			vms.add(new AdvVm(
					i, 
					brokerId, 
					vmType,
					Constants.VM_MIPS[vmType], 
					Constants.VM_PES[vmType], 
					Constants.VM_RAM[vmType], 
					Constants.VM_BW[vmType], 
					Constants.VM_SIZE[vmType], 
					1,
					Constants.VM_MONITOR[vmType],
					cloudletScheduler,
					Constants.SCHEDULING_INTERVAL)
					);
		}
		return vms;
	}

	/**
	 * Creates the non-self-aware list for Awareness package..
	 * 
	 * @param brokerId the broker id
	 * @param vmsNumber the vms number
	 * 
	 * @return the list< vm>
	 */
	public static List<AdvVm> createHeterogenousAdvVmList(int brokerId, int[] vmsNumber, int[] vmType, int cloudletSchedulerType) {

		List<AdvVm> vms = new ArrayList<AdvVm>();
		int idx = 0;
		
		for (int j = 0; j < vmType.length; j++){
			for (int i = 0; i < vmsNumber[j]; i++) {
				//int vmType = i / (int) Math.ceil((double) vmsNumber / Constants.VM_TYPES);
	
				ServiceRequestScheduler cloudletScheduler = null;
				switch(cloudletSchedulerType){
				case 1:	//CloudletSchedulerSpaceShared
					cloudletScheduler = new ServiceRequestSchedulerSpaceShared();
					break;
				case 2:	//CloudletSchedulerTimeShared
					//cloudletScheduler = new ServiceRequestSchedulerTimeShared();
					break;
				case 3:	//CloudletSchedulerDynamicWorkload
					//cloudletScheduler = new CloudletSchedulerDynamicWorkload(Constants.VM_MIPS[vmType], Constants.VM_PES[vmType]);
					break;
				}
	
				vms.add(new AdvVm(
						idx, 
						brokerId, 
						vmType[j],
						Constants.VM_MIPS[vmType[j]], 
						Constants.VM_PES[vmType[j]], 
						Constants.VM_RAM[vmType[j]], 
						Constants.VM_BW[vmType[j]], 
						Constants.VM_SIZE[vmType[j]], 
						1,
						Constants.VM_MONITOR[vmType[j]],
						cloudletScheduler,
						Constants.SCHEDULING_INTERVAL)
						);
				idx++;
			}
		}
		return vms;
	}

	
	

	//from Tao
	public static void CreateSensors(){
		/*			Create Sensors
		intervals = new LinkedList<Interval>();
		sensors = new HashMap<String, List<Sensor>> ();
		List<Sensor> list = null;
		Interval interval = new Interval(System.currentTimeMillis());
		for (Cloudlet c : cloudletList){
			list = new ArrayList<Sensor>();
			list.add(new ResponseTimeSensor());
			interval.setY(Integer.toString(c.getCloudletId()), new ResponseTimeSensor().getName(), new double[] { new Random().nextInt() });
			list.add(new WorkloadSensor());
			interval.setX(Integer.toString(c.getCloudletId()), new WorkloadSensor().getName(), new double[] { new Random().nextInt() });
			list.add(new ConcurrencySensor());
			interval.setX(Integer.toString(c.getCloudletId()), new ConcurrencySensor().getName(), new double[] { new Random().nextInt() });
			sensors.put(Integer.toString(c.getCloudletId()), list);
		}
		
		StringBuilder data = new StringBuilder(VM_ID + "=1\n");
		for (Map.Entry<String, List<Sensor>> entry : sensors.entrySet()) {
			convert(intervals, entry.getKey(), data, VM_ID + "=1\n");
		}
		System.out.print(data);
		sender.send(data.toString());
		intervals.clear();
	
		
		Log.printLine("Pirnt sensors data");
		Log.printLine(Arrays.deepToString(responseTimeSensor.getName()) + " sensor");
		responseTimeSensor.printResult(responseTimeSensor.runMonitoring());
		responseTimeSensor.destory();
	*/	
	}

}