package experiments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
 * NaturalEnvironmentIoT stress experiment
 * simulating the testbed of x location
 * for reading interval every x hours
 * for x number of months
 *  
 *  Each experiment is run for 30 runs to calculate the average of ActualSimulationTime and UsedMemory. 
 */

public class NaturalEnvIoT_stressEval_exp {
	/**
	 * Creates main() to run this example.
	 *
	 * @param args the args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {

		//simulation parameters
		double exp_readingInterval = ExperimentsConfigurations.READING_INTERVAL[3];			//variable scenario is used to represent the reading interval
		int exp_workloadInterval = ExperimentsConfigurations.WORKLOAD_INTERVAL[3];
		int exp_no_months = 24;		//variable exp_no_months is used for varying the number of months
		int exp_no_locations = 1;		
		int exp_no_runs = 30;			//variable exp_no_runs is used to set running each experiment for x times

		//set the experiment number of days according to the number of months specified for the experiment
		ExperimentsConfigurations.EXP_NO_OF_DAYS = exp_no_months*30;	//*30days

		try {
			String workingDir = System.getProperty("user.dir");
			String datasetsFolder = workingDir + "//experiments//datasets//";
			String workloadFolder = workingDir + "/experiments//workload//";
			String resultsFolder = workingDir + "//experiments//results//";
			String outputFileName = "NaturalEnvIoT_stressEval" + exp_no_months;

			//Comment to stop writing output log in the txt files
			//OutputStream output = new FileOutputStream(resultsFolder+ outputFileName + ".txt");
			//Log.setOutput(output);	

			//create excelsheet for saving results
			//Create blank workbook
			@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook();
			//Create a blank spreadsheet
			XSSFSheet spreadsheet = workbook.createSheet(outputFileName);  

			//write header row
			int rowNum = 0;
			XSSFRow header = spreadsheet.createRow(rowNum++);
			Cell h_cellA = header.createCell(0);
			Cell h_cellB = header.createCell(1);
			Cell h_cellC = header.createCell(2);
			Cell h_cellD = header.createCell(3);
			Cell h_cellE = header.createCell(4);

			h_cellA.setCellValue("Reading_Interval_(hr)");
			h_cellB.setCellValue("No_of_Months");
			h_cellC.setCellValue("Exp_run");
			h_cellD.setCellValue("Simulation_Time_(ms)");
			h_cellE.setCellValue("Used_Memory_(MB)");

			//Write the workbook in file system
			FileOutputStream fileOut = new FileOutputStream(new File(resultsFolder + outputFileName + ".xlsx"));
			workbook.write(fileOut);
			fileOut.close();

			Log.printLine("Starting NaturalEnvironmentIoT stress experiment...");
			Log.printLine("simulating the testbed of " + exp_no_locations + " location(s)");
			Log.printLine("for " + exp_no_months + " months");
			Log.printLine("for reading interval " + exp_readingInterval/60/60 + " hours");

			for (int run = 1; run <= exp_no_runs; run++) {
				Runtime.getRuntime().gc(); 
				//record the start local time
				LocalDateTime startTime = java.time.LocalDateTime.now();

				Log.printLine("***** starting run no. " + run + "*****");
				
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
						workloadFolder + "iot//" + "iot_workload_minimal.txt", exp_workloadInterval,
						brokerId, serviceId, serviceType, utilizationModel);

				// submit cloudlet list to the broker
				broker.submitServiceRequestList(cloudletList);

				for (int j=1; j <= exp_no_locations; j++) {
					//create one IoT testbed
					createTestbed(datacenter0, exp_readingInterval, datasetsFolder);
				}

				double lastClock = CloudSim.startSimulation();

				CloudSim.stopSimulation();

				Log.printLine();
				Log.printLine("Experiment finished!");	

				LocalDateTime finishTime = java.time.LocalDateTime.now();

				//calculate actual simulation time in Nanoseconds
				Log.printLine();
				long actualSimTime = Duration.between(startTime, finishTime).toMillis();
				Log.printLine("Actual simulation time: " + actualSimTime);
				Log.printLine();

				//get used memory
				Runtime runtime = Runtime.getRuntime();
				long usedMemory = (runtime.totalMemory() - runtime.freeMemory())/1024*1024;
				Log.printLine("Used memory: " + usedMemory);
				Log.printLine();

				//write results in Results sheet
				InputStream fileIn = new FileInputStream(resultsFolder + outputFileName + ".xlsx");
				Workbook wb = WorkbookFactory.create(fileIn);
				Sheet sheet = wb.getSheetAt(0);

				Row row = sheet.createRow(rowNum++);
				Cell cell1 = row.createCell(0);
				Cell cell2 = row.createCell(1);
				Cell cell3 = row.createCell(2);
				Cell cell4 = row.createCell(3);
				Cell cell5 = row.createCell(4);

				cell1.setCellValue(exp_readingInterval/60/60);
				cell2.setCellValue(exp_no_months);
				cell3.setCellValue(run);
				cell4.setCellValue(actualSimTime);
				cell5.setCellValue(usedMemory);

				FileOutputStream out = new FileOutputStream(resultsFolder + outputFileName + ".xlsx");
				wb.write(out);
				out.close();				
			} //end 30 runs

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
					datasetsFolder+"ukcp09_mean-temperature_360month.csv");
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
					datasetsFolder+"ukcp09_rainfall_360month.csv");
		}
	}


	private static void WriteResults() {

	}

}
