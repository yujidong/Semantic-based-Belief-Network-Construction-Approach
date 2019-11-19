package org.cloudbus.cloudsim.adv;

import java.util.LinkedList;
import java.util.List;

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
 * Class Service is the application service offered to the user. 
 * It stores all the information required for processing the service.
 * 
 * @author Maria Salama
 * 
 */

public class Service {

	/** The service ID. */
	private final int serviceId;

	/** The size of this Service to be executed in a CloudResource (unit: in MI).  */
	private long serviceLength;

	/** The num of Pe required to execute this job. */
	private int numberOfPes;

	/** The input file size of this Service before execution (unit: in byte). in byte = program + input data size   */
	private final long serviceInputFileSize;

	/** The output file size of this Service after execution (unit: in byte). */
	private final long serviceOutputFileSize;

	/** The class type of Service for resource scheduling. */
	private int classType;

	/** The ToS for sending Service over the network. */
	private int netToS;

	// Data service
	/** The required files. */
	private List<String> requiredFiles = null;   // list of required filenames

	
	/**
	 * Allocates a new Service object. The Service length, input and output file sizes should be
	 * greater than or equal to 1.
	 * 
	 * @param serviceId the unique ID of this service
	 * @param serviceLength the length or size (in MI) of this service to be executed in a
	 *            PowerDatacenter
	 * @param serviceFileSize the file size (in byte) of this service <tt>BEFORE</tt> submitting
	 *            to a PowerDatacenter
	 * @param serviceOutputSize the file size (in byte) of this service <tt>AFTER</tt> finish
	 *            executing by a PowerDatacenter
	 * @param record record the history of this object or not
	 * @param pesNumber the pes number
	 * @param utilizationModelCpu the utilization model cpu
	 * @param utilizationModelRam the utilization model ram
	 * @param utilizationModelBw the utilization model bw
	 * @pre serviceID >= 0
	 * @pre serviceLength >= 0.0
	 * @pre serviceFileSize >= 1
	 * @pre serviceOutputSize >= 1
	 * @post $none
	 */
	public Service(
			final int serviceId,
			final long serviceLength,
			final int pesNumber,
			final long serviceInputFileSize,
			final long serviceOutputFileSize) {

		this.serviceId = serviceId;
		this.numberOfPes = pesNumber;
		this.classType = 0;
		this.netToS = 0;

		// Service length, Input and Output size should be at least 1 byte.
		this.serviceLength = Math.max(1, serviceLength);
		this.serviceInputFileSize = Math.max(1, serviceInputFileSize);
		this.serviceOutputFileSize = Math.max(1, serviceOutputFileSize);

		requiredFiles = new LinkedList<String>();
	}
	

	/**
	 * Allocates a new Service object. The Service length, input and output file sizes should be
	 * greater than or equal to 1. By default this constructor sets the history of this object.
	 * 
	 * @param serviceId the unique ID of this Service
	 * @param serviceLength the length or size (in MI) of this service to be executed in a
	 *            PowerDatacenter
	 * @param serviceFileSize the file size (in byte) of this service <tt>BEFORE</tt> submitting
	 *            to a PowerDatacenter
	 * @param serviceOutputSize the file size (in byte) of this service <tt>AFTER</tt> finish
	 *            executing by a PowerDatacenter
	 * @param fileList list of files required by this service
	 * @param pesNumber the pes number
	 * @pre serviceID >= 0
	 * @pre serviceLength >= 0.0
	 * @pre serviceFileSize >= 1
	 * @pre serviceOutputSize >= 1
	 * @post $none
	 */
	public Service(
			final int serviceId,
			final long serviceLength,
			final int pesNumber,
			final long serviceFileSize,
			final long serviceOutputSize,
			final List<String> fileList) {
		
		this(
				serviceId,
				serviceLength,
				pesNumber,
				serviceFileSize,
				serviceOutputSize);

		requiredFiles = fileList;
	}




	/**
	 * Sets the length or size (in MI) of this Service to be executed in a CloudResource. This
	 * Service length is calculated for 1 Pe only <tt>not</tt> the total length.
	 * 
	 * @param serviceLength the length or size (in MI) of this Service to be executed in a CloudResource
	 * @return <tt>true</tt> if it is successful, <tt>false</tt> otherwise
	 * @pre serviceLength > 0
	 * @post $none
	 */
	public boolean setServiceLength(final long serviceLength) {
		if (serviceLength <= 0) {
			return false;
		}

		this.serviceLength = serviceLength;
		return true;
	}

	/**
	 * Sets the network service level for sending this service over a network.
	 * 
	 * @param netServiceLevel determines the kind of service this service receives in the network
	 *            (applicable to selected PacketScheduler class only)
	 * @return <code>true</code> if successful.
	 * @pre netServiceLevel >= 0
	 * @post $none
	 */
	public boolean setNetServiceLevel(final int netServiceLevel) {
		boolean success = false;
		if (netServiceLevel > 0) {
			netToS = netServiceLevel;
			success = true;
		}

		return success;
	}

	/**
	 * Gets the network service level for sending this service over a network.
	 * 
	 * @return the network service level
	 * @pre $none
	 * @post $none
	 */
	public int getNetServiceLevel() {
		return netToS;
	}


	/**
	 * Sets the classType or priority of this Service for scheduling on a resource.
	 * 
	 * @param classType classType of this Service
	 * @return <tt>true</tt> if it is successful, <tt>false</tt> otherwise
	 * @pre classType > 0
	 * @post $none
	 */
	public boolean setClassType(final int classType) {
		boolean success = false;
		if (classType > 0) {
			this.classType = classType;
			success = true;
		}

		return success;
	}

	/**
	 * Gets the classtype or priority of this Service for scheduling on a resource.
	 * 
	 * @return classtype of this service
	 * @pre $none
	 * @post $none
	 */
	public int getClassType() {
		return classType;
	}

	/**
	 * Sets the number of PEs required to run this Service. <br>
	 * NOTE: The Service length is computed only for 1 Pe for simplicity. <br>
	 * For example, this Service has a length of 500 MI and requires 2 PEs. This means each Pe will
	 * execute 500 MI of this Service.
	 * 
	 * @param numberOfPes number of Pe
	 * @return <tt>true</tt> if it is successful, <tt>false</tt> otherwise
	 * @pre numPE > 0
	 * @post $none
	 */
	public boolean setNumberOfPes(final int numberOfPes) {
		if (numberOfPes > 0) {
			this.numberOfPes = numberOfPes;
			return true;
		}
		return false;
	}

	/**
	 * Gets the number of PEs required to run this Service.
	 * 
	 * @return number of PEs
	 * @pre $none
	 * @post $none
	 */
	public int getNumberOfPes() {
		return numberOfPes;
	}


	/**
	 * Gets the input file size of this Service <tt>BEFORE</tt> submitting to a CloudResource.
	 * 
	 * @return the input file size of this Service
	 * @pre $none
	 * @post $result >= 1
	 */
	public long getServiceInputFileSize() {
		return serviceInputFileSize;
	}

	/**
	 * Gets the output size of this Service <tt>AFTER</tt> submitting and executing to a
	 * CloudResource.
	 * 
	 * @return the Service output file size
	 * @pre $none
	 * @post $result >= 1
	 */
	public long getServiceOutputFileSize() {
		return serviceOutputFileSize;
	}

	/**
	 * Gets the length of this Service.
	 * 
	 * @return the length of this Service
	 * @pre $none
	 * @post $result >= 0.0
	 */
	public long getServiceLength() {
		return serviceLength;
	}

	/**
	 * Gets the total length (across all PEs) of this Service.
	 * 
	 * @return the total length of this Service
	 * @pre $none
	 * @post $result >= 0.0
	 */
	public long getServiceTotalLength() {
		return getServiceLength() * getNumberOfPes();
	}

	/**
	 * Gets the cost running this Service in the latest CloudResource.
	 * 
	 * @return the cost associated with running this Service or <tt>0.0</tt> if none
	 * @pre $none
	 * @post $result >= 0.0
	 */

	/**
	 * Gets the ID of this Service.
	 * 
	 * @return Service Id
	 * @pre $none
	 * @post $none
	 */
	public int getServiceId() {
		return serviceId;
	}

	// Data service

	/**
	 * Gets the required files.
	 * 
	 * @return the required files
	 */
	public List<String> getRequiredFiles() {
		return requiredFiles;
	}

	/**
	 * Sets the required files.
	 * 
	 * @param requiredFiles the new required files
	 */
	protected void setRequiredFiles(final List<String> requiredFiles) {
		this.requiredFiles = requiredFiles;
	}

	/**
	 * Adds the required filename to the list.
	 * 
	 * @param fileName the required filename
	 * @return <tt>true</tt> if succesful, <tt>false</tt> otherwise
	 */
	public boolean addRequiredFile(final String fileName) {
		// if the list is empty
		if (getRequiredFiles() == null) {
			setRequiredFiles(new LinkedList<String>());
		}

		// then check whether filename already exists or not
		boolean result = false;
		for (int i = 0; i < getRequiredFiles().size(); i++) {
			final String temp = getRequiredFiles().get(i);
			if (temp.equals(fileName)) {
				result = true;
				break;
			}
		}

		if (!result) {
			getRequiredFiles().add(fileName);
		}

		return result;
	}

	/**
	 * Deletes the given filename from the list.
	 * 
	 * @param filename the given filename to be deleted
	 * @return <tt>true</tt> if succesful, <tt>false</tt> otherwise
	 */
	public boolean deleteRequiredFile(final String filename) {
		boolean result = false;
		if (getRequiredFiles() == null) {
			return result;
		}

		for (int i = 0; i < getRequiredFiles().size(); i++) {
			final String temp = getRequiredFiles().get(i);

			if (temp.equals(filename)) {
				getRequiredFiles().remove(i);
				result = true;

				break;
			}
		}

		return result;
	}

	/**
	 * Checks whether this service requires any files or not.
	 * 
	 * @return <tt>true</tt> if required, <tt>false</tt> otherwise
	 */
	public boolean requiresFiles() {
		boolean result = false;
		if (getRequiredFiles() != null && getRequiredFiles().size() > 0) {
			result = true;
		}

		return result;
	}

	
}
