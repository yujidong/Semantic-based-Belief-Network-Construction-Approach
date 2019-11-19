package helper;

import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.power.models.PowerModelLinear;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G3PentiumD930;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G4Xeon3040;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G5Xeon3075;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerIbmX3250XeonX3470;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerIbmX3250XeonX3480;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerIbmX3550XeonX5670;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerIbmX3550XeonX5675;

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

/**
 * constants for running experiments on cloudsim
 *
 * @author mxs512
 *
 */
public class Constants {

	public final static boolean ENABLE_OUTPUT = true;
	public final static boolean OUTPUT_CSV    = true;
    public static int MILLION = 1000000;

	public final static double SCHEDULING_INTERVAL = 300;
	public final static double SIMULATION_LIMIT = 24 * 60 * 60;

	/*
	* Workload and Runtime Constants
	*/
	public final static int RUNTIME_INTERVAL = 864;   		//in seconds
	public final static double MONITORING_INTERVAL = 240;	//every x seconds

	/*
	* PlanetLabConstants
	* maximum number of hosts in datacenter
	*/
	public final static int NUMBER_OF_HOSTS = 1000;

	/*
	 * Host types:
	 * 	 0. Cloudsim Example 1 host
	 * 	 1. Network Example 
	 * 	 2. HP ProLiant ML110 G3 (1 x [Pentium D930 3000 MHz, 2 cores], 4GB) HD 2 x 160GB
	 *   3. HP ProLiant ML110 G4 (1 x [Xeon 3040 1860 MHz, 2 cores], 4GB) HD 2 x 146GB
	 *   4. HP ProLiant ML110 G5 (1 x [Xeon 3075 2660 MHz, 2 cores], 4GB) HD 2 x 160GB
	 *   5. We increase the memory size to enable over-subscription (x4)
	 * 6. IBM server x3250 (1 x [Xeon X3470 2933 MHz, 4 cores], 8GB) HD 1 x 250GB
	 * 7. IBM server x3250 (1 x [Xeon X3480 3067 MHz, 4 cores], 8GB) HD 1 x 250GB
	 * 8. IBM server x3550 (2 x [Xeon X5670 2933 MHz, 6 cores], 12GB) HD 1 x 50GB
	 * 9. IBM server x3550 (2 x [Xeon X5675 3067 MHz, 6 cores], 16GB) HD 1 x 50GB
	 */
/*	public final static int HOST_TYPES	 = 10;
	public final static int[] HOST_PES	 = {1, 1, 2, 2, 2, 2, 						4, 4, 12, 12}; 					//Processing Element (CPU)	//{ 2, 4 };
	public final static int[] HOST_MIPS	 = {1000, 1000, 3000, 1860, 2660, 2660, 	2933, 3067, 2933, 3067}; 				//{ 1860, 2660 };
	public final static int[] HOST_RAM	 = {2048, 16384, 4096, 4096, 4096, 16384, 8192, 8192, 12288, 16384}; 				//{ 2048, 4096 };
	public final static int[] HOST_BW	 = {10000, 10000, 10000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000}; 		//{ 10000, 1000000}; // 1 Gbit/s
	public final static int[] HOST_STORAGE = {1000000, 1000000, 320000000, 292000000, 320000000, 320000000, 250000000, 250000000, 50000000, 50000000}; 	//1000000; // 1 GB
*/
	/*
	 * Host types:
	 * 0. IBM server x3250 (1 x [Xeon X3470 2933 GHz, 4 cores], 8GB) HD 1 x 250GB
	 * 1. IBM server x3250 (1 x [Xeon X3480 3067 GHz, 4 cores], 8GB) HD 1 x 250GB
	 * 2. IBM server x3550 (2 x [Xeon X5670 2933 GHz, 6 cores], 120GB) HD 1 x 500GB
	 * 3. IBM server x3550 (2 x [Xeon X5675 3067 GHz, 6 cores], 256GB) HD 1 x 500GB
	 */
	public final static int HOST_TYPES	 = 4;
	public final static int[] HOST_PES	 = {4, 		4, 		12, 	12}; 					
	public final static int[] HOST_MIPS	 = {2933, 	3067, 	2933, 	3067}; 				
	public final static int[] HOST_RAM	 = {8192, 	8192, 	131072, 262144}; 			
	public final static int[] HOST_BW	 = {1000000, 1000000, 1000000, 1000000}; 		
	public final static int[] HOST_STORAGE = {250000000, 250000000, 500000000, 500000000}; 	
	
	public final static int[] VM_SCHEDULER = {	
			1,		//VmSchedulerSpaceShared
			2,		//VmSchedulerTimeShared
			3,		//VmSchedulerOportunisticSpaceShared
			4		//VmSchedulerTimeSharedOverSubscription
		};
		
	/*
	 * Power Models
	 * Power single threshold example 		
	 * double maxPower = 250; 				// 250W  
	 * double staticPowerPercent = 0.7; 	// 70%
	 */
	public final static PowerModel[] HOST_POWER = {
//		new PowerModelLinear(250, 0.7),
//		new PowerModelLinear(250, 0.7),
//		new PowerModelSpecPowerHpProLiantMl110G3PentiumD930(),
//		new PowerModelSpecPowerHpProLiantMl110G4Xeon3040(),
//		new PowerModelSpecPowerHpProLiantMl110G5Xeon3075(),
//		new PowerModelSpecPowerHpProLiantMl110G5Xeon3075(),
		new PowerModelSpecPowerIbmX3250XeonX3470(),
		new PowerModelSpecPowerIbmX3250XeonX3480(),
		new PowerModelSpecPowerIbmX3550XeonX5670(),
		new PowerModelSpecPowerIbmX3550XeonX5675()
	};

	 /*
	 * operating cost of hosts:
	 * 0. IBM server x3250
	 * 1. IBM server x3550
	 * ref: https://aws.amazon.com/ec2/dedicated-hosts/pricing/
	 * unit: $ per hour
	 */
	public final static double[] HOST_OPERATING_COST = {2.42, 2.42, 2.42, 2.42};

	/*
	 * VM instance types:
	 *   0. Cloudsim Example 1
	 *   1. Cloudsim Example 2
	 *   2. Cloudsim Example 3
	 *   3. Cloudsim Example 4, 5, 6, 7, 8 & network examples
	 *   4. High-Memory Extra Large Instance: 3.25 EC2 Compute Units, 8.55 GB // too much MIPS
	 *   5. High-CPU Medium Instance: 2.5 EC2 Compute Units, 0.85 GB
	 *   6. Extra Large Instance: 2 EC2 Compute Units, 3.75 GB
	 *   7. Small Instance: 1 EC2 Compute Unit, 1.7 GB
	 *   8. Micro Instance: 0.5 EC2 Compute Unit, 0.633 GB
	 *   We decrease the memory size two times to enable oversubscription
	 *   9. My VM instance Xen JEOS
	 *   10. My VM instance Xen JEOS - less Mips
	 *   11. My VM instance Xen JEOS + more Mips
	*/
/*	public final static int VM_TYPES	= 12;
	public final static int[] VM_PES	= {1, 1, 1, 1, 1, 1, 1, 1, 1, 									2, 		4, 		4}; 								//Processing Element (CPU)
	public final static int[] VM_MIPS	= {1000, 250, 250, 250, 3250, 2500, 2000, 1000, 500, 			2500, 	1700,	3067}; //
	public final static int[] VM_RAM	= {512, 512, 2048, 512, 8755, 870,  3840, 1740, 613, 			2048, 	2048, 	2048};
	public final static int[] VM_BW		= {1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 		1000, 	1000, 	1000}; 		// 100 Mbit/s
	public final static int[] VM_SIZE	= {10000, 10000, 10000, 10000, 2500, 2500, 2500, 2500, 2500, 	10000, 	10000, 	10000}; 				// 2.5 GB //image size (MB)
	public final static String[] VM_MONITOR	= {"Xen", "Xen", "Xen", "Xen", "Xen", "Xen", "Xen", "Xen", 	"Xen", "Xen", "Xen", "Xen"}; 	// Xen
*/
	/*
	 * VM instance types:
	 * ref: https://aws.amazon.com/ec2/instance-types/
	 * General Purpose Amazon EC2 Instances
	 * 0. m4.large 		(2 vCPU, 2.4 Clock Speed (GHz), 8 Memory (GiB), Storage (GB), 450 Dedicated EBS Bandwidth (Mbps))
	 * 1. m4.xlarge 	(4 vCPU, 2.4 Clock Speed (GHz), 16 Memory (GiB), Storage (GB), 750 Dedicated EBS Bandwidth (Mbps))
	 * 2. m4.2xlarge 	(8 vCPU, 2.4 Clock Speed (GHz), 32 Memory (GiB), Storage (GB), 1000 Dedicated EBS Bandwidth (Mbps))
	 * 3. m4.4xlarge 	(16 vCPU, 2.4 Clock Speed (GHz), 64 Memory (GiB), Storage (GB), 2000 Dedicated EBS Bandwidth (Mbps))
	 * 4. m4.10xlarge	(40 vCPU, 2.4 Clock Speed (GHz), 160 Memory (GiB), Storage (GB), 4000 Dedicated EBS Bandwidth (Mbps))
	 * 5. m4.16xlarge	(64 vCPU, 2.3 Clock Speed (GHz), 256 Memory (GiB), Storage (GB), 10000 Dedicated EBS Bandwidth (Mbps))
*/
	
	//public final static int VM_TYPES = 6;
	public final static int VM_TYPES = 3;
	public final static int[] VM_PES	= {2, 		4, 		8, 		16, 40, 64}; 								
	public final static int[] VM_MIPS	= {2400, 	2400,	2400,	2400, 2400, 2400}; 
	public final static int[] VM_RAM	= {8192, 	16384, 	32768,	65536, 163840, 262144};
	public final static int[] VM_BW		= {450, 	750, 	1000, 	2000, 4000, 10000}; 		
	public final static int[] VM_SIZE	= {10000, 	10000, 	10000,	10000, 10000, 10000};		//image size (MB) 				
	public final static String[] VM_MONITOR	= {"Xen", "Xen", "Xen", "Xen", "Xen", "Xen"}; 
	
	public final static int[] SERVICEREQUEST_SCHEDULER = {	
		1,		//CloudletSchedulerSpaceShared
		2,		//CloudletSchedulerTimeShared
		3		//CloudletSchedulerDynamicWorkload
	};
	
	 /*
	 * operating cost of vms:
	 * ref: https://aws.amazon.com/ec2/pricing/reserved-instances/pricing/
	 * unit: $ per hour
	 * 0. m4.large
	 * 1. m4.xlarge
	 * 2. m4.2xlarge 
	 * 3. m4.4xlarge
	 * 4. m4.10xlarge
	 * 5. m4.16xlarge
	 */
	public final static double[] VM_OPERATING_COST = {0.1, 0.2, 0.4, 0.8, 2.0, 3.2};

	//public final static long[] CLOUDLET_LENGTH = {400000, 400000, 400000, 400000}; 
	
	/*
	 * Cloudlet types:
	 *   Cloudsim Example 1 & Network examples
	 *   Cloudsim Example 2
	 *   Cloudsim Example 6
	 *   Power examples
	 *   RUBIS read-only service (BrowseCategories)
	 *   RUBIS read and write service (PutBid, RegisterItem, RegisterUser)
	 */
/*	public final static int SERVICE_TYPES	 = 6;
	public final static long[] SERVICE_LENGTH = {40000, 250000, 1000, 2500*(24*60*60), 10000, 20000}; 			//MIPS millions instructions per second
	public final static int[] SERVICE_PES	= {1, 1, 1, 1, 								1, 2};										//Processing Element (CPU)
	public final static long[] SERVICE_FILESIZE = {300, 300, 300, 300, 					300, 1200};
	public final static long[] SERVICE_OUTPUTSIZE = {300, 300, 300, 300,				300, 1200};
*/	
	/*
	 * Cloudlet (Service Request) types:
	 * RUBiS clients requests with different workload patterns
	 * 0. browsing only service (read-only) - Main, BrowseCategories
	 * 1. bidding only service (read and write) - PutBid, RegisterItem, RegisterUser
	 * mixed with adjustable composition of the two actions
	 * 70% browsing and 30% bidding, 50% browsing and 50% bidding, 30% browsing and 70% bidding. 
	 * 2.
	 * 3.
	 * 4 
	 */
	public final static int SERVICE_TYPES	 = 5;
	public final static long[] SERVICE_LENGTH = {10000, 20000, 12000, 15000, 17000}; 	//MIPS millions instructions per second
	public final static int[] SERVICE_PES	= {1, 1, 1, 1, 1};										//Processing Element (CPU)
	public final static long[] SERVICE_FILESIZE = {300, 1500, 600, 900, 1200};
	public final static long[] SERVICE_OUTPUTSIZE = {300, 1500, 600, 900, 1200};
	
	 /*
	 * cost of processing service requests:
	 * 0. cloudSim examples
	 * 1. 
	 */
	public final static int COST_MODELS	 = 2;
	public final static double[] COST_CPU	 		= {	3, 		0.04}; 	// the cost of using processing in this resource
	public final static double[] COST_Per_Mem 		= {	0.05, 	0.02};	// the cost of using memory in this resource
	public final static double[] COST_Per_Storage 	= {	0.1,	0.02};	// the cost of using storage in this resource
	public final static double[] COST_Per_Bw 		= {	0.1, 	0.01};	// the cost of using bw in this resource

	
	 /*
	 * Stability enabled:
	 */
	public final static boolean STABILITY_ANALYSIS_ENABLED = false;
	public final static boolean STABILITY_EVALUATION_ENABLED = false;

	
	
}
