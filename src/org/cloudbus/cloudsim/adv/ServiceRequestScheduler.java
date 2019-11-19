package org.cloudbus.cloudsim.adv;

import java.util.List;

import org.cloudbus.cloudsim.ResServiceRequest;

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

public abstract class ServiceRequestScheduler {

	/** The previous time. */
	private double previousTime;

	/** The current mips share. */
	private List<Double> currentMipsShare;

	/**
	 * Creates a new CloudletScheduler object. This method must be invoked before starting the
	 * actual simulation.
	 * 
	 * @pre $none
	 * @post $none
	 */
	public ServiceRequestScheduler() {
		setPreviousTime(0.0);
	}

	/**
	 * Updates the processing of cloudlets running under management of this scheduler.
	 * 
	 * @param currentTime current simulation time
	 * @param mipsShare array with MIPS share of each processor available to the scheduler
	 * @return time predicted completion time of the earliest finishing cloudlet, or 0 if there is no
	 *         next events
	 * @pre currentTime >= 0
	 * @post $none
	 */
	public abstract double updateVmProcessing(double currentTime, List<Double> mipsShare);

	/**
	 * Receives an cloudlet to be executed in the VM managed by this scheduler.
	 * 
	 * @param gl the submited cloudlet
	 * @param fileTransferTime time required to move the required files from the SAN to the VM
	 * @return expected finish time of this cloudlet, or 0 if it is in a waiting queue
	 * @pre gl != null
	 * @post $none
	 */
	public abstract double serviceRequestSubmit(ServiceRequest gl, double fileTransferTime);

	/**
	 * Receives an cloudlet to be executed in the VM managed by this scheduler.
	 * 
	 * @param gl the submited cloudlet
	 * @return expected finish time of this cloudlet, or 0 if it is in a waiting queue
	 * @pre gl != null
	 * @post $none
	 */
	public abstract double serviceRequestSubmit(ServiceRequest gl);

	/**
	 * Cancels execution of a cloudlet.
	 * 
	 * @param clId ID of the cloudlet being cancealed
	 * @return the canceled cloudlet, $null if not found
	 * @pre $none
	 * @post $none
	 */
	public abstract ServiceRequest serviceRequestCancel(int clId);

	/**
	 * Pauses execution of a cloudlet.
	 * 
	 * @param clId ID of the cloudlet being paused
	 * @return $true if cloudlet paused, $false otherwise
	 * @pre $none
	 * @post $none
	 */
	public abstract boolean serviceRequestPause(int clId);

	/**
	 * Resumes execution of a paused cloudlet.
	 * 
	 * @param clId ID of the cloudlet being resumed
	 * @return expected finish time of the cloudlet, 0.0 if queued
	 * @pre $none
	 * @post $none
	 */
	public abstract double serviceRequestResume(int clId);

	/**
	 * Processes a finished cloudlet.
	 * 
	 * @param rcl finished cloudlet
	 * @pre rgl != $null
	 * @post $none
	 */
	public abstract void serviceRequestFinish(ResServiceRequest rcl);

	/**
	 * Gets the status of a cloudlet.
	 * 
	 * @param clId ID of the cloudlet
	 * @return status of the cloudlet, -1 if cloudlet not found
	 * @pre $none
	 * @post $none
	 */
	public abstract int getServiceRequestStatus(int clId);

	/**
	 * Informs about completion of some cloudlet in the VM managed by this scheduler.
	 * 
	 * @return $true if there is at least one finished cloudlet; $false otherwise
	 * @pre $none
	 * @post $none
	 */
	public abstract boolean isFinishedServiceRequests();

	/**
	 * Returns the next cloudlet in the finished list, $null if this list is empty.
	 * 
	 * @return a finished cloudlet
	 * @pre $none
	 * @post $none
	 */
	public abstract ServiceRequest getNextFinishedServiceRequest();

	/**
	 * Returns the number of cloudlets runnning in the virtual machine.
	 * 
	 * @return number of cloudlets runnning
	 * @pre $none
	 * @post $none
	 */
	public abstract int runningServiceRequests();

	/**
	 * Returns one cloudlet to migrate to another vm.
	 * 
	 * @return one running cloudlet
	 * @pre $none
	 * @post $none
	 */
	public abstract ServiceRequest migrateServiceRequest();

	/**
	 * Get utilization created by all cloudlets.
	 * 
	 * @param time the time
	 * @return total utilization
	 */
	public abstract double getTotalUtilizationOfCpu(double time);

	/**
	 * Gets the current requested mips.
	 * 
	 * @return the current mips
	 */
	public abstract List<Double> getCurrentRequestedMips();

	/**
	 * Gets the total current mips for the Cloudlet.
	 * 
	 * @param rcl the rcl
	 * @param mipsShare the mips share
	 * @return the total current mips
	 */
	public abstract double getTotalCurrentAvailableMipsForServiceRequest(ResServiceRequest rcl, List<Double> mipsShare);

	/**
	 * Gets the total current requested mips for cloudlet.
	 * 
	 * @param rcl the rcl
	 * @param time the time
	 * @return the total current requested mips for cloudlet
	 */
	public abstract double getTotalCurrentRequestedMipsForServiceRequest(ResServiceRequest rcl, double time);

	/**
	 * Gets the total current allocated mips for cloudlet.
	 * 
	 * @param rcl the rcl
	 * @param time the time
	 * @return the total current allocated mips for cloudlet
	 */
	public abstract double getTotalCurrentAllocatedMipsForServiceRequest(ResServiceRequest rcl, double time);

	/**
	 * Gets the current requested ram.
	 * 
	 * @return the current requested ram
	 */
	public abstract double getCurrentRequestedUtilizationOfRam();

	/**
	 * Gets the current requested bw.
	 * 
	 * @return the current requested bw
	 */
	public abstract double getCurrentRequestedUtilizationOfBw();

	/**
	 * Gets the previous time.
	 * 
	 * @return the previous time
	 */
	public double getPreviousTime() {
		return previousTime;
	}

	/**
	 * Sets the previous time.
	 * 
	 * @param previousTime the new previous time
	 */
	protected void setPreviousTime(double previousTime) {
		this.previousTime = previousTime;
	}

	/**
	 * Sets the current mips share.
	 * 
	 * @param currentMipsShare the new current mips share
	 */
	protected void setCurrentMipsShare(List<Double> currentMipsShare) {
		this.currentMipsShare = currentMipsShare;
	}

	/**
	 * Gets the current mips share.
	 * 
	 * @return the current mips share
	 */
	public List<Double> getCurrentMipsShare() {
		return currentMipsShare;
	}

	public boolean serviceRequestRemove(int serviceRequestId) {
		// TODO Auto-generated method stub
		return false;
	}

}
