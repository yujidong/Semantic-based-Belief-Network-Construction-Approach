package helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelNull;
import org.cloudbus.cloudsim.UtilizationModelPlanetLabInMemory;
import org.cloudbus.cloudsim.adv.Service;
import org.cloudbus.cloudsim.adv.ServiceRequest;

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

public class Workload {
	

	public static Service createService(int id, int serviceType){
		//service parameters
		long length = Constants.SERVICE_LENGTH[serviceType];
		long fileSize = Constants.SERVICE_FILESIZE[serviceType];
		long outputSize = Constants.SERVICE_OUTPUTSIZE[serviceType];
		int pesNumber = Constants.SERVICE_PES[serviceType];
		
		Service service = new Service(id, length, pesNumber, fileSize, outputSize);
		
		return service;
	}
	
	
	/**
	 * Creates the requests list and their utilisation model.
	 * 
	 * @param brokerId the broker id
	 * @param serviceType the service Type
	 * @param requests the number of requests
	 * @param utilizationModel utilisation model of the cloudlet
	 * @return List<ServiceRequest> list of requests created
	 */
	public static List<ServiceRequest> createServiceRequest(int userId, int serviceId, int requests, 
			int serviceType, UtilizationModel utilizationModel){
		// Creates a container to store Cloudlets
		LinkedList<ServiceRequest> list = new LinkedList<ServiceRequest>();

		//cloudlet parameters
		long length = Constants.SERVICE_LENGTH[serviceType];
		long fileSize = Constants.SERVICE_FILESIZE[serviceType];
		long outputSize = Constants.SERVICE_OUTPUTSIZE[serviceType];
		int pesNumber = Constants.SERVICE_PES[serviceType];

		ServiceRequest[] cloudlet = new ServiceRequest[requests];

		for(int i=0; i<requests; i++){
			cloudlet[i] = new ServiceRequest(i, serviceId, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
			// setting the owner of these Cloudlets
			cloudlet[i].setUserId(userId);
			cloudlet[i].setVmId(i);
			list.add(cloudlet[i]);
		}

		return list;
	}

	/**
	 * Creates the cloudlet list planet lab.
	 * 
	 * @param brokerId the broker id
	 * @param cloudletType the cloudlet Type
	 * @param inputFolderName the input folder name
	 * @return the list
	 * @throws FileNotFoundException the file not found exception
	 */
	public static List<Cloudlet> createCloudletListPlanetLab(int brokerId, int cloudletType, String inputFolderName)
			throws FileNotFoundException {
		
		List<Cloudlet> list = new ArrayList<Cloudlet>();

		//cloudlet parameters
		long length = Constants.SERVICE_LENGTH[cloudletType];
		long fileSize = Constants.SERVICE_FILESIZE[cloudletType];
		long outputSize = Constants.SERVICE_OUTPUTSIZE[cloudletType];
		int pesNumber = Constants.SERVICE_PES[cloudletType];
		UtilizationModel utilizationModel = new UtilizationModelNull();

		File inputFolder = new File(inputFolderName);
		File[] files = inputFolder.listFiles();

		for (int i = 0; i < files.length; i++) {
			Cloudlet cloudlet = null;
			try {
				UtilizationModelPlanetLabInMemory utilizationModelCPU = new UtilizationModelPlanetLabInMemory(
						files[i].getAbsolutePath(), Constants.SCHEDULING_INTERVAL);
				cloudlet = new Cloudlet(i, length, pesNumber, fileSize, outputSize, utilizationModelCPU, utilizationModel, utilizationModel);				
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			cloudlet.setUserId(brokerId);
			cloudlet.setVmId(i);
			list.add(cloudlet);
		}

		return list;
	}

	/**
	 * Creates the requests list and their utilisation model from workload folfer.
	 * 
	 * @param brokerId the broker id
	 * @param inputFolderName the input folder name
	 * @param requests the number for requests
	 * @param serviceType the service Type
	 * @return List<ServiceRequest> list of requests created
	 * @throws FileNotFoundException the file not found exception
	 */
	public static List<ServiceRequest> createCloudlet(int userId, String inputFolderName, int serviceId, int serviceType){
		// Creates a container to store Cloudlets
		LinkedList<ServiceRequest> list = new LinkedList<ServiceRequest>();

		//cloudlet parameters
		long length = Constants.SERVICE_LENGTH[serviceType];
		long fileSize = Constants.SERVICE_FILESIZE[serviceType];
		long outputSize = Constants.SERVICE_OUTPUTSIZE[serviceType];
		int pesNumber = Constants.SERVICE_PES[serviceType];

		File inputFolder = new File(inputFolderName);
		File[] files = inputFolder.listFiles();

		for (int i = 0; i < files.length; i++) {
			ServiceRequest cloudlet = null;
			try {
				UtilizationModel utilizationModelNull = new UtilizationModelNull();
				UtilizationModel utilizationModel = new UtilizationModelPlanetLabInMemory(
						files[i].getAbsolutePath(),
						Constants.SCHEDULING_INTERVAL);

				cloudlet = new ServiceRequest(i, serviceId, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModelNull, utilizationModelNull);				
				cloudlet.setUserId(userId);
				cloudlet.setVmId(i);
				list.add(cloudlet);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		
		return list;
	}


	/**
	 * Creates the requests list from workload folder.
	 * 
	 * @param brokerId the broker id
	 * @param inputFolderName the input folder name
	 * @param serviceType the service Type
	 * @param utilizationModel utilisation model of the cloudlet
	 * @return List<ServiceRequest> list of requests created
	 * @throws FileNotFoundException the file not found exception
	 */
	public static List<ServiceRequest> createServiceRequest(int userId, String inputFolderName, int serviceId, int serviceType, 
			UtilizationModel utilizationModel){

		// Creates a container to store Cloudlets
		LinkedList<ServiceRequest> list = new LinkedList<ServiceRequest>();

		//cloudlet parameters
		long length = Constants.SERVICE_LENGTH[serviceType];
		long fileSize = Constants.SERVICE_FILESIZE[serviceType];
		long outputSize = Constants.SERVICE_OUTPUTSIZE[serviceType];
		int pesNumber = Constants.SERVICE_PES[serviceType];

		File inputFolder = new File(inputFolderName);
		File[] files = inputFolder.listFiles();

		for (int i = 0; i < files.length; i++) {
			ServiceRequest cloudlet = null;
			try {
				cloudlet = new ServiceRequest(i, serviceId, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);				
				cloudlet.setUserId(userId);
				cloudlet.setVmId(i);
				list.add(cloudlet);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		
		return list;
	}


	/**
	 * Creates the requests list from workload binary file.
	 * 
	 * @param inputBinaryWorkloadFileName the input file name
	 * @param brokerId the broker id
	 * @param serviceType the service Type
	 * @param utilizationModel utilisation model of the cloudlet
	 * @return List<ServiceRequest> list of requests created
	 * @throws FileNotFoundException the file not found exception
	 */
	public static List<ServiceRequest> generateWorkload(String inputBinaryWorkloadFileName, int userId, int serviceId, int serviceType, UtilizationModel utilizationModel) 
			throws FileNotFoundException {
		List<ServiceRequest> list = new LinkedList<ServiceRequest>();

		//cloudlet parameters
		long length = Constants.SERVICE_LENGTH[serviceType];
		long fileSize = Constants.SERVICE_FILESIZE[serviceType];
		long outputSize = Constants.SERVICE_OUTPUTSIZE[serviceType];
		int pesNumber = Constants.SERVICE_PES[serviceType];
		
		FileInputStream inputStream = new FileInputStream(inputBinaryWorkloadFileName);
		byte[] buffer = new byte[1000];
		int total = 0;
        int nRead = 0;
        try {
        	System.out.println("Start reading workload file " + inputBinaryWorkloadFileName);
			while((nRead = inputStream.read(buffer)) != -1) {
				total += nRead;
			    //System.out.println(total);
			}
			inputStream.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error reading file '" + inputBinaryWorkloadFileName + "'");  
			e.printStackTrace();
		}   
		
		for (int i = 0; i < total; i++) {
			ServiceRequest cloudlet = null;
			try {
				cloudlet = new ServiceRequest(i, serviceId, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);				
				cloudlet.setUserId(userId);
				cloudlet.setVmId(i);
				list.add(cloudlet);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		return list;
	
	}
	
	
	/**
	 * Creates the run-time requests list from workload file.
	 * 
	 * @param inputWorkloadFileName the input file name
	 * @param brokerId the broker id
	 * @param serviceId the service id
	 * @param serviceType the service Type
	 * @param utilizationModel utilisation model of the cloudlet
	 * @return List<ServiceRequest> list of requests created
	 * @throws FileNotFoundException the file not found exception
	 */
	public static List<ServiceRequest> generateWorkloadRuntime(String inputWorkloadFileName, int brokerId, int serviceId, int serviceType, UtilizationModel utilizationModel) 
			throws FileNotFoundException {
		List<ServiceRequest> list = new LinkedList<ServiceRequest>();

		//cloudlet parameters
		long length = Constants.SERVICE_LENGTH[serviceType];
		long fileSize = Constants.SERVICE_FILESIZE[serviceType];
		long outputSize = Constants.SERVICE_OUTPUTSIZE[serviceType];
		int pesNumber = Constants.SERVICE_PES[serviceType];
		
        try {
			String line = null;
			double arrivalTime = 0.0;
			int cloudletId = 0;
			Log.printLine("Start reading workload file " + inputWorkloadFileName);
			BufferedReader reader = new BufferedReader(new FileReader(inputWorkloadFileName));

			while((line = reader.readLine()) != null) {
				int numCloudlets = Integer.parseInt(line);
				//System.out.print(" ***** "+line+"\n");

				for (int i = 0; i < numCloudlets; i++) {
					ServiceRequest cloudlet = null;
					try {
						cloudlet = new ServiceRequest(cloudletId, serviceId, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);				
						cloudlet.setUserId(brokerId);
						cloudlet.setVmId(i);
						cloudlet.setArrivalTime(arrivalTime);
						list.add(cloudlet);
						cloudletId++;
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(0);
					}
				}
				arrivalTime += Constants.RUNTIME_INTERVAL;
			}
			
			reader.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error reading file '" + inputWorkloadFileName + "'");  
			e.printStackTrace();
		}   

		return list;	
	}
	
	/**
	 * Creates the run-time requests list from workload file.
	 * 
	 * @param inputWorkloadFileName the input file name
	 * @param workloadInterval runtime workload interval
	 * @param brokerId the broker id
	 * @param serviceId the service id
	 * @param serviceType the service Type
	 * @param utilizationModel utilisation model of the cloudlet
	 * @return List<ServiceRequest> list of requests created
	 * @throws FileNotFoundException the file not found exception
	 */
	public static List<ServiceRequest> generateWorkloadRuntime(
			String inputWorkloadFileName, int workloadInterval,
			int brokerId, int serviceId, int serviceType, UtilizationModel utilizationModel) 
			throws FileNotFoundException {
		
		List<ServiceRequest> list = new LinkedList<ServiceRequest>();

		//cloudlet parameters
		long length = Constants.SERVICE_LENGTH[serviceType];
		long fileSize = Constants.SERVICE_FILESIZE[serviceType];
		long outputSize = Constants.SERVICE_OUTPUTSIZE[serviceType];
		int pesNumber = Constants.SERVICE_PES[serviceType];
		
        try {
			String line = null;
			double arrivalTime = 0.0;
			int cloudletId = 0;
			Log.printLine("Start reading workload file " + inputWorkloadFileName);
			BufferedReader reader = new BufferedReader(new FileReader(inputWorkloadFileName));

			while((line = reader.readLine()) != null) {
				int numCloudlets = Integer.parseInt(line);
				//System.out.print(" ***** "+line+"\n");

				for (int i = 0; i < numCloudlets; i++) {
					ServiceRequest cloudlet = null;
					try {
						cloudlet = new ServiceRequest(cloudletId, serviceId, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);				
						cloudlet.setUserId(brokerId);
						cloudlet.setVmId(i);
						cloudlet.setArrivalTime(arrivalTime);
						list.add(cloudlet);
						cloudletId++;
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(0);
					}
				}
				arrivalTime += workloadInterval;
			}
			
			reader.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error reading file '" + inputWorkloadFileName + "'");  
			e.printStackTrace();
		}   

		return list;	
	}

	
	/**
	 * Parses the experiment name.
	 * 
	 * @param name the name
	 * @return the string
	 */
	public static String parseExperimentName(String name) {
		Scanner scanner = new Scanner(name);
		StringBuilder csvName = new StringBuilder();
		scanner.useDelimiter("_");
		for (int i = 0; i < 4; i++) {
			if (scanner.hasNext()) {
				csvName.append(scanner.next() + ",");
			} else {
				csvName.append(",");
			}
		}
		scanner.close();
		return csvName.toString();
	}


}
