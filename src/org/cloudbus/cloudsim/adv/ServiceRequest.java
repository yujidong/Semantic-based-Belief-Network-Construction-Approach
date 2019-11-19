package org.cloudbus.cloudsim.adv;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

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
 * Class
 * 
 * @author Maria Salama
 * 
 */

public class ServiceRequest extends Service {

	/**
	 * The User or Broker ID. It is advisable that broker set this ID with its own ID, so that
	 * CloudResource returns to it after the execution.
	 **/
	private int userId;
	
	private int serviceId;

	/** The serviceRequest ID. */
	private final int serviceRequestId;

	/** The status of this ServiceRequest. */
	private int status;

	/** The format of decimal numbers. */
	private DecimalFormat num;

	/** run-time arrival of the request */
	private double ArrivalTime = 0.0;

	/**
	 * Start time of executing this ServiceRequest. With new functionalities, such as CANCEL, PAUSED and
	 * RESUMED, this attribute only stores the latest execution time. Previous execution time are
	 * ignored.
	 */
	private double execStartTime;

	/** The time where this ServiceRequest completes. */
	private double finishTime;

	/** The ID of a reservation made for this serviceRequest. */
	private int reservationId = -1;

	/** The records the transaction history for this ServiceRequest. */
	private final boolean record;

	/** The newline. */
	private String newline;

	/** The history. */
	private StringBuffer history;

	/** The res list. */
	private final List<Resource> resList;

	/** The index. */
	private int index;

	/** The vm id. */
	protected int vmId;

	//Cost
	protected double costPerCPU;
	protected double costPerMemory;
	protected double costPerStorage;
	
	/** The cost per bw. */
	protected double costPerBw;

	/** The accumulated bw cost. */
	protected double accumulatedBwCost;
	
	protected double processingCost;
	protected double totalCost;

	// Utilization
	/** The utilization of cpu model. */
	private UtilizationModel utilizationModelCpu;

	/** The utilization of memory model. */
	private UtilizationModel utilizationModelRam;

	/** The utilization of bw model. */
	private UtilizationModel utilizationModelBw;


	// //////////////////////////////////////////
	// Below are CONSTANTS attributes
	/** The ServiceRequest has been created and added to the ServiceRequestList object. */
	public static final int CREATED = 0;

	/** The ServiceRequest has been assigned to a CloudResource object as planned. */
	public static final int READY = 1;

	/** The ServiceRequest has moved to a Cloud node. */
	public static final int QUEUED = 2;

	/** The ServiceRequest is in execution in a Cloud node. */
	public static final int INEXEC = 3;

	/** The ServiceRequest has been executed successfully. */
	public static final int SUCCESS = 4;

	/** The ServiceRequest is failed. */
	public static final int FAILED = 5;

	/** The ServiceRequest has been canceled. */
	public static final int CANCELED = 6;

	/**
	 * The ServiceRequest has been paused. It can be resumed by changing the status into <tt>RESUMED</tt>.
	 */
	public static final int PAUSED = 7;

	/** The ServiceRequest has been resumed from <tt>PAUSED</tt> state. */
	public static final int RESUMED = 8;

	/** The serviceRequest has failed due to a resource failure. */
	public static final int FAILED_RESOURCE_UNAVAILABLE = 9;

	// //////////////////////////////////////////


	/**
	 * Allocates a new ServiceRequest object. The ServiceRequest length, input and output file sizes should be
	 * greater than or equal to 1.
	 * 
	 * @param serviceRequestId the unique ID of this serviceRequest
	 * @param serviceRequestLength the length or size (in MI) of this serviceRequest to be executed in a
	 *            PowerDatacenter
	 * @param serviceRequestFileSize the file size (in byte) of this serviceRequest <tt>BEFORE</tt> submitting
	 *            to a PowerDatacenter
	 * @param serviceRequestOutputSize the file size (in byte) of this serviceRequest <tt>AFTER</tt> finish
	 *            executing by a PowerDatacenter
	 * @param record record the history of this object or not
	 * @param pesNumber the pes number
	 * @param utilizationModelCpu the utilization model cpu
	 * @param utilizationModelRam the utilization model ram
	 * @param utilizationModelBw the utilization model bw
	 * @pre serviceRequestID >= 0
	 * @pre serviceRequestLength >= 0.0
	 * @pre serviceRequestFileSize >= 1
	 * @pre serviceRequestOutputSize >= 1
	 * @post $none
	 */
	public ServiceRequest(
			final int serviceRequestId,
			final int serviceId,
			final long serviceRequestLength,
			final int pesNumber,
			final long serviceInputFileSize,
			final long serviceOutputFileSize,
			final UtilizationModel utilizationModelCpu,
			final UtilizationModel utilizationModelRam,
			final UtilizationModel utilizationModelBw,
			final boolean record) {
		
		super(serviceId, serviceRequestLength, pesNumber, serviceInputFileSize, serviceOutputFileSize);

		this.userId = -1;          // to be set by a Broker or user
		this.serviceId = serviceId;
		this.serviceRequestId = serviceRequestId;
		this.status = CREATED;
		this.execStartTime = 0.0;
		this.finishTime = -1.0;    // meaning this ServiceRequest hasn't finished yet

		// Normally, a ServiceRequest is only executed on a resource without being
		// migrated to others. Hence, to reduce memory consumption, set the
		// size of this ArrayList to be less than the default one.
		resList = new ArrayList<Resource>(2);
		index = -1;
		this.record = record;

		vmId = -1;
		accumulatedBwCost = 0.0;
		costPerCPU = 0.0;
		costPerMemory = 0.0;
		costPerStorage = 0.0;
		costPerBw = 0.0;
		processingCost = 0.0;
		totalCost = 0.0;

		setUtilizationModelCpu(utilizationModelCpu);
		setUtilizationModelRam(utilizationModelRam);
		setUtilizationModelBw(utilizationModelBw);
	}

	/**
	 * Allocates a new ServiceRequest object. The ServiceRequest length, input and output file sizes should be
	 * greater than or equal to 1. By default this constructor sets the history of this object.
	 * 
	 * @param serviceRequestId the unique ID of this ServiceRequest
	 * @param serviceRequestLength the length or size (in MI) of this serviceRequest to be executed in a
	 *            PowerDatacenter
	 * @param serviceRequestFileSize the file size (in byte) of this serviceRequest <tt>BEFORE</tt> submitting
	 *            to a PowerDatacenter
	 * @param serviceRequestOutputSize the file size (in byte) of this serviceRequest <tt>AFTER</tt> finish
	 *            executing by a PowerDatacenter
	 * @param pesNumber the pes number
	 * @param utilizationModelCpu the utilization model cpu
	 * @param utilizationModelRam the utilization model ram
	 * @param utilizationModelBw the utilization model bw
	 * @pre serviceRequestID >= 0
	 * @pre serviceRequestLength >= 0.0
	 * @pre serviceRequestFileSize >= 1
	 * @pre serviceRequestOutputSize >= 1
	 * @post $none
	 */
	public ServiceRequest(
			final int serviceRequestId,
			final int serviceId,
			final long serviceRequestLength,
			final int pesNumber,
			final long serviceInputFileSize,
			final long serviceOutputFileSize,
			final UtilizationModel utilizationModelCpu,
			final UtilizationModel utilizationModelRam,
			final UtilizationModel utilizationModelBw) {

		this(
				serviceRequestId,
				serviceId,
				serviceRequestLength,
				pesNumber,
				serviceInputFileSize,
				serviceOutputFileSize,
				utilizationModelCpu,
				utilizationModelRam,
				utilizationModelBw,
				false);

	}

	/**
	 * Allocates a new ServiceRequest object. The ServiceRequest length, input and output file sizes should be
	 * greater than or equal to 1.
	 * 
	 * @param serviceRequestId the unique ID of this serviceRequest
	 * @param serviceRequestLength the length or size (in MI) of this serviceRequest to be executed in a
	 *            PowerDatacenter
	 * @param serviceRequestFileSize the file size (in byte) of this serviceRequest <tt>BEFORE</tt> submitting
	 *            to a PowerDatacenter
	 * @param serviceRequestOutputSize the file size (in byte) of this serviceRequest <tt>AFTER</tt> finish
	 *            executing by a PowerDatacenter
	 * @param record record the history of this object or not
	 * @param fileList list of files required by this serviceRequest
	 * @param pesNumber the pes number
	 * @param utilizationModelCpu the utilization model cpu
	 * @param utilizationModelRam the utilization model ram
	 * @param utilizationModelBw the utilization model bw
	 * @pre serviceRequestID >= 0
	 * @pre serviceRequestLength >= 0.0
	 * @pre serviceRequestFileSize >= 1
	 * @pre serviceRequestOutputSize >= 1
	 * @post $none
	 */
	public ServiceRequest(
			final int serviceRequestId,
			final int serviceId,
			final long serviceRequestLength,
			final int pesNumber,
			final long serviceInputFileSize,
			final long serviceOutputFileSize,
			final UtilizationModel utilizationModelCpu,
			final UtilizationModel utilizationModelRam,
			final UtilizationModel utilizationModelBw,
			final boolean record,
			final List<String> fileList) {
		this(
				serviceRequestId,
				serviceId,
				serviceRequestLength,
				pesNumber,
				serviceInputFileSize,
				serviceOutputFileSize,
				utilizationModelCpu,
				utilizationModelRam,
				utilizationModelBw,
				record);

		super.setRequiredFiles(fileList);
	}

	/**
	 * Allocates a new ServiceRequest object. The ServiceRequest length, input and output file sizes should be
	 * greater than or equal to 1. By default this constructor sets the history of this object.
	 * 
	 * @param serviceRequestId the unique ID of this ServiceRequest
	 * @param serviceRequestLength the length or size (in MI) of this serviceRequest to be executed in a
	 *            PowerDatacenter
	 * @param serviceRequestFileSize the file size (in byte) of this serviceRequest <tt>BEFORE</tt> submitting
	 *            to a PowerDatacenter
	 * @param serviceRequestOutputSize the file size (in byte) of this serviceRequest <tt>AFTER</tt> finish
	 *            executing by a PowerDatacenter
	 * @param fileList list of files required by this serviceRequest
	 * @param pesNumber the pes number
	 * @param utilizationModelCpu the utilization model cpu
	 * @param utilizationModelRam the utilization model ram
	 * @param utilizationModelBw the utilization model bw
	 * @pre serviceRequestID >= 0
	 * @pre serviceRequestLength >= 0.0
	 * @pre serviceRequestFileSize >= 1
	 * @pre serviceRequestOutputSize >= 1
	 * @post $none
	 */
	public ServiceRequest(
			final int serviceRequestId,
			final int serviceId,
			final long serviceRequestLength,
			final int pesNumber,
			final long serviceInputFileSize,
			final long serviceOutputFileSize,
			final UtilizationModel utilizationModelCpu,
			final UtilizationModel utilizationModelRam,
			final UtilizationModel utilizationModelBw,
			final List<String> fileList) {
		this(
				serviceRequestId,
				serviceId,
				serviceRequestLength,
				pesNumber,
				serviceInputFileSize,
				serviceOutputFileSize,
				utilizationModelCpu,
				utilizationModelRam,
				utilizationModelBw,
				false);

		super.setRequiredFiles(fileList);
	}



	
	// ////////////////////// INTERNAL CLASS ///////////////////////////////////

	/**
	 * Internal class that keeps track ServiceRequest's movement in different CloudResources.
	 */
	private static class Resource {

		/** ServiceRequest's submission time to a CloudResource. */
		public double submissionTime = 0.0;

		/**
		 * The time of this ServiceRequest resides in a CloudResource (from arrival time until departure
		 * time).
		 */
		public double wallClockTime = 0.0;

		/** The total execution time of this ServiceRequest in a CloudResource. */
		public double actualCPUTime = 0.0;

		/** Cost per second a CloudResource charge to execute this ServiceRequest. */
		public double costPerSec = 0.0;

		/** ServiceRequest's length finished so far. */
		public long finishedSoFar = 0;

		/** a CloudResource id. */
		public int resourceId = -1;

		/** a CloudResource name. */
		public String resourceName = null;

	} // end of internal class

	// ////////////////////// End of Internal Class //////////////////////////


	/**
	 * Sets the id of the reservation made for this serviceRequest.
	 * 
	 * @param resId the reservation ID
	 * @return <tt>true</tt> if the ID has successfully been set or <tt>false</tt> otherwise.
	 */
	public boolean setReservationId(final int resId) {
		if (resId <= 0) {
			return false;
		}
		reservationId = resId;
		return true;
	}

	/**
	 * Gets the reservation ID that owns this ServiceRequest.
	 * 
	 * @return a reservation ID
	 * @pre $none
	 * @post $none
	 */
	public int getReservationId() {
		return reservationId;
	}

	/**
	 * Checks whether this ServiceRequest is submitted by reserving or not.
	 * 
	 * @return <tt>true</tt> if this ServiceRequest has reserved before, <tt>false</tt> otherwise
	 */
	public boolean hasReserved() {
		if (reservationId == -1) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the waiting time of this serviceRequest executed on a resource.
	 * 
	 * @return the waiting time
	 * @pre $none
	 * @post $none
	 */
	public double getWaitingTime() {
		if (index == -1) {
			return 0;
		}

		// use the latest resource submission time
		final double subTime = resList.get(index).submissionTime;
		return execStartTime - subTime;
	}

	/**
	 * Gets the history of this ServiceRequest. The layout of this history is in a readable table column
	 * with <tt>time</tt> and <tt>description</tt> as headers.
	 * 
	 * @return a String containing the history of this ServiceRequest object.
	 * @pre $none
	 * @post $result != null
	 */
	public String getServiceRequestHistory() {
		String msg = null;
		if (history == null) {
			msg = "No history is recorded for ServiceRequest #" + serviceRequestId;
		} else {
			msg = history.toString();
		}

		return msg;
	}

	/**
	 * Checks whether this ServiceRequest has finished execution or not.
	 * 
	 * @return <tt>true</tt> if this ServiceRequest has finished execution, <tt>false</tt> otherwise
	 * @pre $none
	 * @post $none
	 */
	public boolean isFinished() {
		if (index == -1) {
			return false;
		}

		boolean completed = false;

		// if result is 0 or -ve then this ServiceRequest has finished
		final long finish = resList.get(index).finishedSoFar;
		final long result = super.getServiceLength() - finish;
		if (result <= 0.0) {
			completed = true;
		}
		return completed;
	}

	/**
	 * Sets the length of this ServiceRequest that has been executed so far. This method is used by
	 * ResServiceRequest class when an application is decided to cancel or to move this ServiceRequest into
	 * different CloudResources.
	 * 
	 * @param length length of this ServiceRequest
	 * @see gridsim.AllocPolicy
	 * @see gridsim.ResServiceRequest
	 * @pre length >= 0.0
	 * @post $none
	 */
	public void setServiceRequestFinishedSoFar(final long length) {
		// if length is -ve then ignore
		if (length < 0.0 || index < 0) {
			return;
		}

		final Resource res = resList.get(index);
		res.finishedSoFar = length;

		if (record) {
			write("Sets the length's finished so far to " + length);
		}
	}

	/**
	 * Sets the user or owner ID of this ServiceRequest. It is <tt>VERY</tt> important to set the user ID,
	 * otherwise this ServiceRequest will not be executed in a CloudResource.
	 * 
	 * @param id the user ID
	 * @pre id >= 0
	 * @post $none
	 */
	public void setUserId(final int id) {
		userId = id;
		if (record) {
			write("Assigns the ServiceRequest to " + CloudSim.getEntityName(id) + " (ID #" + id + ")");
		}
	}

	/**
	 * Gets the user or owner ID of this ServiceRequest.
	 * 
	 * @return the user ID or <tt>-1</tt> if the user ID has not been set before
	 * @pre $none
	 * @post $result >= -1
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Gets the latest resource ID that processes this ServiceRequest.
	 * 
	 * @return the resource ID or <tt>-1</tt> if none
	 * @pre $none
	 * @post $result >= -1
	 */
	public int getResourceId() {
		if (index == -1) {
			return -1;
		}
		return resList.get(index).resourceId;
	}

	/**
	 * Sets the submission or arrival time of this ServiceRequest into a CloudResource.
	 * 
	 * @param clockTime the submission time
	 * @pre clockTime >= 0.0
	 * @post $none
	 */
	public void setSubmissionTime(final double clockTime) {
		if (clockTime < 0.0 || index < 0) {
			return;
		}

		final Resource res = resList.get(index);
		res.submissionTime = clockTime;

		if (record) {
			write("Sets the submission time to " + num.format(clockTime));
		}
	}

	/**
	 * Gets the submission or arrival time of this ServiceRequest from the latest CloudResource.
	 * 
	 * @return the submission time or <tt>0.0</tt> if none
	 * @pre $none
	 * @post $result >= 0.0
	 */
	public double getSubmissionTime() {
		if (index == -1) {
			return 0.0;
		}
		return resList.get(index).submissionTime;
	}

	/**
	 * Sets the execution start time of this ServiceRequest inside a CloudResource. <b>NOTE:</b> With new
	 * functionalities, such as being able to cancel / to pause / to resume this ServiceRequest, the
	 * execution start time only holds the latest one. Meaning, all previous execution start time
	 * are ignored.
	 * 
	 * @param clockTime the latest execution start time
	 * @pre clockTime >= 0.0
	 * @post $none
	 */
	public void setExecStartTime(final double clockTime) {
		execStartTime = clockTime;
		if (record) {
			write("Sets the execution start time to " + num.format(clockTime));
		}
	}

	/**
	 * Gets the latest execution start time.
	 * 
	 * @return the latest execution start time
	 * @pre $none
	 * @post $result >= 0.0
	 */
	public double getExecStartTime() {
		return execStartTime;
	}

	/**
	 * Sets this ServiceRequest's execution parameters. These parameters are set by the CloudResource
	 * before departure or sending back to the original ServiceRequest's owner.
	 * 
	 * @param wallTime the time of this ServiceRequest resides in a CloudResource (from arrival time until
	 *            departure time).
	 * @param actualTime the total execution time of this ServiceRequest in a CloudResource.
	 * @pre wallTime >= 0.0
	 * @pre actualTime >= 0.0
	 * @post $none
	 */
	public void setExecParam(final double wallTime, final double actualTime) {
		if (wallTime < 0.0 || actualTime < 0.0 || index < 0) {
			return;
		}

		final Resource res = resList.get(index);
		res.wallClockTime = wallTime;
		res.actualCPUTime = actualTime;

		if (record) {
			write("Sets the wall clock time to " + num.format(wallTime) + " and the actual CPU time to "
					+ num.format(actualTime));
		}
	}

	/**
	 * Sets the status code of this ServiceRequest.
	 * 
	 * @param newStatus the status code of this ServiceRequest
	 * @throws Exception Invalid range of ServiceRequest status
	 * @pre newStatus >= 0 && newStatus <= 8
	 * @post $none
	 */
	public void setServiceRequestStatus(final int newStatus) throws Exception {
		// if the new status is same as current one, then ignore the rest
		if (status == newStatus) {
			return;
		}

		// throws an exception if the new status is outside the range
		if (newStatus < ServiceRequest.CREATED || newStatus > ServiceRequest.FAILED_RESOURCE_UNAVAILABLE) {
			throw new Exception(
					"ServiceRequest.setServiceRequestStatus() : Error - Invalid integer range for ServiceRequest status.");
		}

		if (newStatus == ServiceRequest.SUCCESS) {
			finishTime = CloudSim.clock();
		}

		if (record) {
			write("Sets ServiceRequest status from " + getServiceRequestStatusString() + " to "
					+ ServiceRequest.getStatusString(newStatus));
		}
		status = newStatus;
	}

	/**
	 * Sets the total cost of processing or executing this ServiceRequest
	 * <tt>Processing Cost = input data transfer + processing cost + output transfer cost</tt> .
	 * 
	 * @return the total cost of processing ServiceRequest
	 * @pre $none
	 * @post $result >= 0.0
	 */
	public void setTotalCost() {
		// serviceRequest cost: execution cost...
		//double cost = getProcessingCost();
		
		// ...plus input data transfer cost...
		totalCost = accumulatedBwCost;
		// ...plus output cost
		totalCost += costPerBw * super.getServiceOutputFileSize();
		
		totalCost += costPerCPU * getActualCPUTime();
		
		DatacenterBroker broker = (DatacenterBroker) CloudSim.getEntity(userId);
		if (vmId < broker.getVmList().size()-1) {
			Vm vm = broker.getVmList().get(vmId);
			totalCost+= costPerMemory * vm.getRam();
			totalCost+= costPerStorage * vm.getSize();
		}
	}

	public void setProcessingCost() {
		// serviceRequest cost: execution cost...
		//double cost = getProcessingCost();
		processingCost = costPerCPU * getActualCPUTime();
		
		DatacenterBroker broker = (DatacenterBroker) CloudSim.getEntity(userId);
		if (vmId < broker.getVmList().size()-1) {
			Vm vm = broker.getVmList().get(vmId);
			processingCost += costPerMemory * vm.getRam();
		}
	}

	/**
	 * Gets the status code of this ServiceRequest.
	 * 
	 * @return the status code of this ServiceRequest
	 * @pre $none
	 * @post $result >= 0
	 */
	public int getServiceRequestStatus() {
		return status;
	}

	/**
	 * Gets the string representation of the current ServiceRequest status code.
	 * 
	 * @return the ServiceRequest status code as a string or <tt>null</tt> if the status code is unknown
	 * @pre $none
	 * @post $none
	 */
	public String getServiceRequestStatusString() {
		return ServiceRequest.getStatusString(status);
	}

	/**
	 * Gets the string representation of the given ServiceRequest status code.
	 * 
	 * @param status the ServiceRequest status code
	 * @return the ServiceRequest status code as a string or <tt>null</tt> if the status code is unknown
	 * @pre $none
	 * @post $none
	 */
	public static String getStatusString(final int status) {
		String statusString = null;
		switch (status) {
			case ServiceRequest.CREATED:
				statusString = "Created";
				break;

			case ServiceRequest.READY:
				statusString = "Ready";
				break;

			case ServiceRequest.INEXEC:
				statusString = "InExec";
				break;

			case ServiceRequest.SUCCESS:
				statusString = "Success";
				break;

			case ServiceRequest.QUEUED:
				statusString = "Queued";
				break;

			case ServiceRequest.FAILED:
				statusString = "Failed";
				break;

			case ServiceRequest.CANCELED:
				statusString = "Canceled";
				break;

			case ServiceRequest.PAUSED:
				statusString = "Paused";
				break;

			case ServiceRequest.RESUMED:
				statusString = "Resumed";
				break;

			case ServiceRequest.FAILED_RESOURCE_UNAVAILABLE:
				statusString = "Failed_resource_unavailable";
				break;

			default:
				break;
		}

		return statusString;
	}

	/**
	 * Gets the cost running this ServiceRequest in the latest CloudResource.
	 * 
	 * @return the cost associated with running this ServiceRequest or <tt>0.0</tt> if none
	 * @pre $none
	 * @post $result >= 0.0
	 */
	public double getCostPerSec() {
		if (index == -1) {
			return 0.0;
		}
		return resList.get(index).costPerSec;
	}

	/**
	 * Gets the cost running this ServiceRequest in a given CloudResource ID.
	 * 
	 * @param resId a CloudResource entity ID
	 * @return the cost associated with running this ServiceRequest or <tt>0.0</tt> if not found
	 * @pre resId >= 0
	 * @post $result >= 0.0
	 */
	public double getCostPerSec(final int resId) {
		Resource resource = getResourceById(resId);
		if (resource != null) {
			return resource.costPerSec;
		}
		return 0.0;
	}

	/**
	 * Gets all the CloudResource names that executed this ServiceRequest.
	 * 
	 * @return an array of CloudResource names or <tt>null</tt> if it has none
	 * @pre $none
	 * @post $none
	 */
	public String[] getAllResourceName() {
		final int size = resList.size();
		String[] data = null;

		if (size > 0) {
			data = new String[size];
			for (int i = 0; i < size; i++) {
				data[i] = resList.get(i).resourceName;
			}
		}

		return data;
	}

	/**
	 * Gets all the CloudResource IDs that executed this ServiceRequest.
	 * 
	 * @return an array of CloudResource IDs or <tt>null</tt> if it has none
	 * @pre $none
	 * @post $none
	 */
	public int[] getAllResourceId() {
		final int size = resList.size();
		int[] data = null;

		if (size > 0) {
			data = new int[size];
			for (int i = 0; i < size; i++) {
				data[i] = resList.get(i).resourceId;
			}
		}

		return data;
	}

	/**
	 * Gets the total execution time of this ServiceRequest in a given CloudResource ID.
	 * 
	 * @param resId a CloudResource entity ID
	 * @return the total execution time of this ServiceRequest in a CloudResource or <tt>0.0</tt> if not
	 *         found
	 * @pre resId >= 0
	 * @post $result >= 0.0
	 */
	public double getActualCPUTime(final int resId) {
		Resource resource = getResourceById(resId);
		if (resource != null) {
			return resource.actualCPUTime;
		}
		return 0.0;
	}

	/**
	 * Gets the length of this Cloudlet that has been executed so far from the latest CloudResource.
	 * This method is useful when trying to move this Cloudlet into different CloudResources or to
	 * cancel it.
	 * 
	 * @return the length of a partially executed Cloudlet or the full Cloudlet length if it is
	 *         completed
	 * @pre $none
	 * @post $result >= 0.0
	 */
	public long getServiceRequestFinishedSoFar() {
		if (index == -1) {
			return super.getServiceLength();
		}

		final long finish = resList.get(index).finishedSoFar;
		if (finish > super.getServiceLength()) {
			return super.getServiceLength();
		}

		return finish;
	}

	/**
	 * Gets the length of this ServiceRequest that has been executed so far in a given CloudResource ID.
	 * This method is useful when trying to move this ServiceRequest into different CloudResources or to
	 * cancel it.
	 * 
	 * @param resId a CloudResource entity ID
	 * @return the length of a partially executed ServiceRequest or the full ServiceRequest length if it is
	 *         completed or <tt>0.0</tt> if not found
	 * @pre resId >= 0
	 * @post $result >= 0.0
	 */
	public long getServiceRequestFinishedSoFar(final int resId) {
		Resource resource = getResourceById(resId);
		if (resource != null) {
			return resource.finishedSoFar;
		}
		return 0;
	}

	/**
	 * Gets the time of this ServiceRequest resides in the latest CloudResource (from arrival time until
	 * departure time).
	 * 
	 * @return the time of this ServiceRequest resides in a CloudResource
	 * @pre $none
	 * @post $result >= 0.0
	 */
	public double getWallClockTime() {
		if (index == -1) {
			return 0.0;
		}
		return resList.get(index).wallClockTime;
	}

	/**
	 * Gets the submission or arrival time of this ServiceRequest in the given CloudResource ID.
	 * 
	 * @param resId a CloudResource entity ID
	 * @return the submission time or <tt>0.0</tt> if not found
	 * @pre resId >= 0
	 * @post $result >= 0.0
	 */
	public double getSubmissionTime(final int resId) {
		Resource resource = getResourceById(resId);
		if (resource != null) {
			return resource.submissionTime;
		}
		return 0.0;
	}

	/**
	 * Gets the time of this ServiceRequest resides in a given CloudResource ID (from arrival time until
	 * departure time).
	 * 
	 * @param resId a CloudResource entity ID
	 * @return the time of this ServiceRequest resides in the CloudResource or <tt>0.0</tt> if not found
	 * @pre resId >= 0
	 * @post $result >= 0.0
	 */
	public double getWallClockTime(final int resId) {
		Resource resource = getResourceById(resId);
		if (resource != null) {
			return resource.wallClockTime;
		}
		return 0.0;
	}

	/**
	 * Gets the CloudResource name based on its ID.
	 * 
	 * @param resId a CloudResource entity ID
	 * @return the CloudResource name or <tt>null</tt> if not found
	 * @pre resId >= 0
	 * @post $none
	 */
	public String getResourceName(final int resId) {
		Resource resource = getResourceById(resId);
		if (resource != null) {
			return resource.resourceName;
		}
		return null;
	}

	/**
	 * Gets the resource by id.
	 * 
	 * @param resourceId the resource id
	 * @return the resource by id
	 */
	public Resource getResourceById(final int resourceId) {
		for (Resource resource : resList) {
			if (resource.resourceId == resourceId) {
				return resource;
			}
		}
		return null;
	}

	/**
	 * Gets the finish time of this ServiceRequest in a CloudResource.
	 * 
	 * @return the finish or completion time of this ServiceRequest or <tt>-1</tt> if not finished yet.
	 * @pre $none
	 * @post $result >= -1
	 */
	public double getFinishTime() {
		return finishTime;
	}

	// //////////////////////// PROTECTED METHODS //////////////////////////////

	/**
	 * Writes this particular history transaction of this ServiceRequest into a log.
	 * 
	 * @param str a history transaction of this ServiceRequest
	 * @pre str != null
	 * @post $none
	 */
	protected void write(final String str) {
		if (!record) {
			return;
		}

		if (num == null || history == null) { // Creates the history or
												// transactions of this ServiceRequest
			newline = System.getProperty("line.separator");
			num = new DecimalFormat("#0.00#"); // with 3 decimal spaces
			history = new StringBuffer(1000);
			history.append("Time below denotes the simulation time.");
			history.append(System.getProperty("line.separator"));
			history.append("Time (sec)       Description ServiceRequest #" + serviceRequestId);
			history.append(System.getProperty("line.separator"));
			history.append("------------------------------------------");
			history.append(System.getProperty("line.separator"));
			history.append(num.format(CloudSim.clock()));
			history.append("   Creates ServiceRequest ID #" + serviceRequestId);
			history.append(System.getProperty("line.separator"));
		}

		history.append(num.format(CloudSim.clock()));
		history.append("   " + str + newline);
	}

	/**
	 * Get the status of the ServiceRequest.
	 * 
	 * @return status of the ServiceRequest
	 * @pre $none
	 * @post $none
	 */
	public int getStatus() {
		return getServiceRequestStatus();
	}

	/**
	 * Gets the ID of this ServiceRequest.
	 * 
	 * @return ServiceRequest Id
	 * @pre $none
	 * @post $none
	 */
	public int getServiceRequestId() {
		return serviceRequestId;
	}

	/**
	 * Gets the ID of the VM that will run this ServiceRequest.
	 * 
	 * @return VM Id, -1 if the ServiceRequest was not assigned to a VM
	 * @pre $none
	 * @post $none
	 */
	public int getVmId() {
		return vmId;
	}

	/**
	 * Sets the ID of the VM that will run this ServiceRequest.
	 * 
	 * @param vmId the vm id
	 * @pre id >= 0
	 * @post $none
	 */
	public void setVmId(final int vmId) {
		this.vmId = vmId;
	}

	/**
	 * Returns the time the ServiceRequest actually run.
	 * 
	 * @return time in which the ServiceRequest was running
	 * @pre $none
	 * @post $none
	 */
	public double getActualCPUTime() {
		return getFinishTime() - getExecStartTime();
		//return getFinishTime() - getSubmissionTime();
	}

	/**
	 * Sets the resource parameters for which this ServiceRequest is going to be executed. <br>
	 * NOTE: This method <tt>should</tt> be called only by a resource entity, not the user or owner
	 * of this ServiceRequest.
	 * 
	 * @param resourceID the CloudResource ID
	 * @param cost the cost running this CloudResource per second
	 * @pre resourceID >= 0
	 * @pre cost > 0.0
	 * @post $none
	 */
	public void setResourceParameter(final int resourceID, final double cost) {
		final Resource res = new Resource();
		res.resourceId = resourceID;
		res.costPerSec = cost;
		res.resourceName = CloudSim.getEntityName(resourceID);

		// add into a list if moving to a new cloud resource
		resList.add(res);

		if (index == -1 && record) {
			write("Allocates this ServiceRequest to " + res.resourceName + " (ID #" + resourceID + ") with cost = $" + cost + "/sec");
		} else if (record) {
			final int id = resList.get(index).resourceId;
			final String name = resList.get(index).resourceName;
			write("Moves ServiceRequest from " + name + " (ID #" + id + ") to " + res.resourceName + " (ID #" + resourceID + ") with cost = $" + cost + "/sec");
		}

		index++;  // initially, index = -1
	}

	/**
	 * Sets the resource parameters for which this ServiceRequest is going to be executed. <br>
	 * NOTE: This method <tt>should</tt> be called only by a resource entity, not the user or owner
	 * of this ServiceRequest.
	 * 
	 * @param resourceID the CloudResource ID
	 * @param costPerCPU the cost running this ServiceRequest per second
	 * @param costPerBw the cost of data transfer to this PowerDatacenter
	 * @pre resourceID >= 0
	 * @pre cost > 0.0
	 * @post $none
	 */
	public void setResourceParameter(final int resourceID, final double costPerCPU, final double costPerBw) {
		setResourceParameter(resourceID, costPerCPU);
		
		this.costPerBw = costPerBw;
		accumulatedBwCost = costPerBw * super.getServiceInputFileSize();
	}

	/**
	 * Sets the resource parameters for which this ServiceRequest is going to be executed. <br>
	 * NOTE: This method <tt>should</tt> be called only by a resource entity, not the user or owner
	 * of this ServiceRequest.
	 * 
	 * @param resourceID the CloudResource ID
	 * @param costPerCPU the cost running this ServiceRequest per second
	 * @param costPerBw the cost of data transfer to this PowerDatacenter
	 * @pre resourceID >= 0
	 * @pre cost > 0.0
	 * @post $none
	 */
	public void setResourceParameter(final int resourceID, final double costPerCPU, final double costPerMemory, final double costPerStorage, final double costPerBw) {
		setResourceParameter(resourceID, costPerCPU);

		this.costPerBw = costPerBw;
		accumulatedBwCost = costPerBw * super.getServiceInputFileSize();

		this.costPerCPU = costPerCPU;
		this.costPerMemory = costPerMemory;
		this.costPerStorage = costPerStorage;

	}

	/**
	 * Gets the utilization model cpu.
	 * 
	 * @return the utilization model cpu
	 */
	public UtilizationModel getUtilizationModelCpu() {
		return utilizationModelCpu;
	}

	/**
	 * Sets the utilization model cpu.
	 * 
	 * @param utilizationModelCpu the new utilization model cpu
	 */
	public void setUtilizationModelCpu(final UtilizationModel utilizationModelCpu) {
		this.utilizationModelCpu = utilizationModelCpu;
	}

	/**
	 * Gets the utilization model ram.
	 * 
	 * @return the utilization model ram
	 */
	public UtilizationModel getUtilizationModelRam() {
		return utilizationModelRam;
	}

	/**
	 * Sets the utilization model ram.
	 * 
	 * @param utilizationModelRam the new utilization model ram
	 */
	public void setUtilizationModelRam(final UtilizationModel utilizationModelRam) {
		this.utilizationModelRam = utilizationModelRam;
	}

	/**
	 * Gets the utilization model bw.
	 * 
	 * @return the utilization model bw
	 */
	public UtilizationModel getUtilizationModelBw() {
		return utilizationModelBw;
	}

	/**
	 * Sets the utilization model bw.
	 * 
	 * @param utilizationModelBw the new utilization model bw
	 */
	public void setUtilizationModelBw(final UtilizationModel utilizationModelBw) {
		this.utilizationModelBw = utilizationModelBw;
	}

	/**
	 * Gets the total utilization of cpu.
	 * 
	 * @param time the time
	 * @return the utilization of cpu
	 */
	public double getUtilizationOfCpu(final double time) {
		return getUtilizationModelCpu().getUtilization(time);
	}

	/**
	 * Gets the utilization of memory.
	 * 
	 * @param time the time
	 * @return the utilization of memory
	 */
	public double getUtilizationOfRam(final double time) {
		return getUtilizationModelRam().getUtilization(time);
	}

	/**
	 * Gets the utilization of bw.
	 * 
	 * @param time the time
	 * @return the utilization of bw
	 */
	public double getUtilizationOfBw(final double time) {
		return getUtilizationModelBw().getUtilization(time);
	}

	public double getResponseTime(){
		return getFinishTime() - getSubmissionTime();
	}
	
	public double getLatencyTime(){
		return getExecStartTime() - getSubmissionTime();
	}
	
	public void setArrivalTime (double arrivalTime){
		ArrivalTime = arrivalTime;
	}
	
	public double getArrivalTime(){
		return ArrivalTime;
	}
	
	public double getProcessingCost() {
		return processingCost;
	}
	
	public double getTotalCost() {
		return totalCost;
	}

	
}
