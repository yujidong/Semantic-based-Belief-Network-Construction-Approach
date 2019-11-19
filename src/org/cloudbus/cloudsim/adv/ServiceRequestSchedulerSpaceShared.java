package org.cloudbus.cloudsim.adv;

import helper.Constants;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.ResServiceRequest;
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

public class ServiceRequestSchedulerSpaceShared extends ServiceRequestScheduler {

	/** The cloudlet waiting list. */
	private List<? extends ResServiceRequest> serviceRequestWaitingList;

	/** The cloudlet exec list. */
	private List<? extends ResServiceRequest> serviceRequestExecList;

	/** The cloudlet paused list. */
	private List<? extends ResServiceRequest> serviceRequestPausedList;

	/** The cloudlet finished list. */
	private List<? extends ResServiceRequest> serviceRequestFinishedList;

	/** The current CPUs. */
	protected int currentCpus;

	/** The used PEs. */
	protected int usedPes;

	/**
	 * Creates a new CloudletSchedulerSpaceShared object. This method must be invoked before
	 * starting the actual simulation.
	 * 
	 * @pre $none
	 * @post $none
	 */
	public ServiceRequestSchedulerSpaceShared() {
		super();
		serviceRequestWaitingList = new ArrayList<ResServiceRequest>();
		serviceRequestExecList = new ArrayList<ResServiceRequest>();
		serviceRequestPausedList = new ArrayList<ResServiceRequest>();
		serviceRequestFinishedList = new ArrayList<ResServiceRequest>();
		usedPes = 0;
		currentCpus = 0;
	}

	/**
	 * Updates the processing of cloudlets running under management of this scheduler.
	 * 
	 * @param currentTime current simulation time
	 * @param mipsShare array with MIPS share of each processor available to the scheduler
	 * @return time predicted completion time of the earliest finishing cloudlet, or 0 if there is no next events
	 * @pre currentTime >= 0
	 * @post $none
	 */
	@Override
	public double updateVmProcessing(double currentTime, List<Double> mipsShare) {
		setCurrentMipsShare(mipsShare);
		double timeSpam = currentTime - getPreviousTime(); // time since last update
		double capacity = 0.0;
		int cpus = 0;

		for (Double mips : mipsShare) { // count the CPUs available to the VMM
			capacity += mips;
			if (mips > 0) {
				cpus++;
			}
		}
		currentCpus = cpus;
		capacity /= cpus; // average capacity of each cpu

		// each machine in the exec list has the same amount of cpu
		for (ResServiceRequest rcl : getServiceRequestExecList()) {
			rcl.updateServiceRequestFinishedSoFar((long) (capacity * timeSpam * rcl.getNumberOfPes() * Constants.MILLION));
		}

		// no more cloudlets in this scheduler
		if (getServiceRequestExecList().size() == 0 && getServiceRequestWaitingList().size() == 0) {
			setPreviousTime(currentTime);
			return 0.0;
		}

		// update each cloudlet
		int finished = 0;
		List<ResServiceRequest> toRemove = new ArrayList<ResServiceRequest>();
		for (ResServiceRequest rcl : getServiceRequestExecList()) {
			// finished anyway, rounding issue...
			if (rcl.getRemainingServiceRequestLength() == 0) {
				toRemove.add(rcl);
				serviceRequestFinish(rcl);
				finished++;
			}
		}
		getServiceRequestExecList().removeAll(toRemove);

		// for each finished cloudlet, add a new one from the waiting list
		if (!getServiceRequestWaitingList().isEmpty()) {
			for (int i = 0; i < finished; i++) {
				toRemove.clear();
				for (ResServiceRequest rcl : getServiceRequestWaitingList()) {
					if ((currentCpus - usedPes) >= rcl.getNumberOfPes()) {
						rcl.setServiceRequestStatus(ServiceRequest.INEXEC);
						for (int k = 0; k < rcl.getNumberOfPes(); k++) {
							rcl.setMachineAndPeId(0, i);
						}
						getServiceRequestExecList().add(rcl);
						usedPes += rcl.getNumberOfPes();
						toRemove.add(rcl);
						break;
					}
				}
				getServiceRequestWaitingList().removeAll(toRemove);
			}
		}

		// estimate finish time of cloudlets in the execution queue
		double nextEvent = Double.MAX_VALUE;
		for (ResServiceRequest rcl : getServiceRequestExecList()) {
			double remainingLength = rcl.getRemainingServiceRequestLength();
			double estimatedFinishTime = currentTime + (remainingLength / (capacity * rcl.getNumberOfPes()));
			if (estimatedFinishTime - currentTime < CloudSim.getMinTimeBetweenEvents()) {
				estimatedFinishTime = currentTime + CloudSim.getMinTimeBetweenEvents();
			}
			if (estimatedFinishTime < nextEvent) {
				nextEvent = estimatedFinishTime;
			}
		}
		setPreviousTime(currentTime);
		return nextEvent;
	}

	/**
	 * Cancels execution of a cloudlet.
	 * 
	 * @param cloudletId ID of the cloudlet being cancealed
	 * @return the canceled cloudlet, $null if not found
	 * @pre $none
	 * @post $none
	 */
	@Override
	public ServiceRequest serviceRequestCancel(int serviceRequestId) {
		// First, looks in the finished queue
		for (ResServiceRequest rcl : getServiceRequestFinishedList()) {
			if (rcl.getServiceRequestId() == serviceRequestId) {
				getServiceRequestFinishedList().remove(rcl);
				return rcl.getServiceRequest();
			}
		}

		// Then searches in the exec list
		for (ResServiceRequest rcl : getServiceRequestExecList()) {
			if (rcl.getServiceRequestId() == serviceRequestId) {
				getServiceRequestExecList().remove(rcl);
				if (rcl.getRemainingServiceRequestLength() == 0) {
					serviceRequestFinish(rcl);
				} else {
					rcl.setServiceRequestStatus(ServiceRequest.CANCELED);
				}
				return rcl.getServiceRequest();
			}
		}

		// Now, looks in the paused queue
		for (ResServiceRequest rcl : getServiceRequestPausedList()) {
			if (rcl.getServiceRequestId() == serviceRequestId) {
				getServiceRequestPausedList().remove(rcl);
				return rcl.getServiceRequest();
			}
		}

		// Finally, looks in the waiting list
		for (ResServiceRequest rcl : getServiceRequestWaitingList()) {
			if (rcl.getServiceRequestId() == serviceRequestId) {
				rcl.setServiceRequestStatus(ServiceRequest.CANCELED);
				getServiceRequestWaitingList().remove(rcl);
				return rcl.getServiceRequest();
			}
		}

		return null;
	}

	/**
	 * Removes a cloudlet from this scheduler.
	 * 
	 * @param cloudletId ID of the cloudlet being removed
	 * @return $true if cloudlet removed, $false otherwise
	 * @pre $none
	 * @post $none
	 */
	@Override
	public boolean serviceRequestRemove(int serviceRequestId) {
		for (ResServiceRequest rcl : getServiceRequestWaitingList()) {
			if (rcl.getServiceRequestId() == serviceRequestId) {
				getServiceRequestWaitingList().remove(rcl);
				return true;
			}
		}
		return false;
	}

	/**
	 * Pauses execution of a cloudlet.
	 * 
	 * @param cloudletId ID of the cloudlet being paused
	 * @return $true if cloudlet paused, $false otherwise
	 * @pre $none
	 * @post $none
	 */
	@Override
	public boolean serviceRequestPause(int serviceRequestId) {
		boolean found = false;
		int position = 0;

		// first, looks for the cloudlet in the exec list
		for (ResServiceRequest rcl : getServiceRequestExecList()) {
			if (rcl.getServiceRequestId() == serviceRequestId) {
				found = true;
				break;
			}
			position++;
		}

		if (found) {
			// moves to the paused list
			ResServiceRequest rgl = getServiceRequestExecList().remove(position);
			if (rgl.getRemainingServiceRequestLength() == 0) {
				serviceRequestFinish(rgl);
			} else {
				rgl.setServiceRequestStatus(ServiceRequest.PAUSED);
				getServiceRequestPausedList().add(rgl);
			}
			return true;

		}

		// now, look for the cloudlet in the waiting list
		position = 0;
		found = false;
		for (ResServiceRequest rcl : getServiceRequestWaitingList()) {
			if (rcl.getServiceRequestId() == serviceRequestId) {
				found = true;
				break;
			}
			position++;
		}

		if (found) {
			// moves to the paused list
			ResServiceRequest rgl = getServiceRequestWaitingList().remove(position);
			if (rgl.getRemainingServiceRequestLength() == 0) {
				serviceRequestFinish(rgl);
			} else {
				rgl.setServiceRequestStatus(ServiceRequest.PAUSED);
				getServiceRequestPausedList().add(rgl);
			}
			return true;

		}

		return false;
	}

	/**
	 * Processes a finished cloudlet.
	 * 
	 * @param rcl finished cloudlet
	 * @pre rgl != $null
	 * @post $none
	 */
	@Override
	public void serviceRequestFinish(ResServiceRequest rcl) {
		rcl.setServiceRequestStatus(ServiceRequest.SUCCESS);
		rcl.finalizeServiceRequest();
		getServiceRequestFinishedList().add(rcl);
		usedPes -= rcl.getNumberOfPes();
	}

	/**
	 * Resumes execution of a paused cloudlet.
	 * 
	 * @param cloudletId ID of the cloudlet being resumed
	 * @return $true if the cloudlet was resumed, $false otherwise
	 * @pre $none
	 * @post $none
	 */
	@Override
	public double serviceRequestResume(int serviceRequestId) {
		boolean found = false;
		int position = 0;

		// look for the cloudlet in the paused list
		for (ResServiceRequest rcl : getServiceRequestPausedList()) {
			if (rcl.getServiceRequestId() == serviceRequestId) {
				found = true;
				break;
			}
			position++;
		}

		if (found) {
			ResServiceRequest rcl = getServiceRequestPausedList().remove(position);

			// it can go to the exec list
			if ((currentCpus - usedPes) >= rcl.getNumberOfPes()) {
				rcl.setServiceRequestStatus(ServiceRequest.INEXEC);
				for (int i = 0; i < rcl.getNumberOfPes(); i++) {
					rcl.setMachineAndPeId(0, i);
				}

				long size = rcl.getRemainingServiceRequestLength();
				size *= rcl.getNumberOfPes();
				rcl.getServiceRequest().setServiceLength(size);

				getServiceRequestExecList().add(rcl);
				usedPes += rcl.getNumberOfPes();

				// calculate the expected time for cloudlet completion
				double capacity = 0.0;
				int cpus = 0;
				for (Double mips : getCurrentMipsShare()) {
					capacity += mips;
					if (mips > 0) {
						cpus++;
					}
				}
				currentCpus = cpus;
				capacity /= cpus;

				long remainingLength = rcl.getRemainingServiceRequestLength();
				double estimatedFinishTime = CloudSim.clock()
						+ (remainingLength / (capacity * rcl.getNumberOfPes()));

				return estimatedFinishTime;
			} else {// no enough free PEs: go to the waiting queue
				rcl.setServiceRequestStatus(ServiceRequest.QUEUED);

				long size = rcl.getRemainingServiceRequestLength();
				size *= rcl.getNumberOfPes();
				rcl.getServiceRequest().setServiceLength(size);

				getServiceRequestWaitingList().add(rcl);
				return 0.0;
			}

		}

		// not found in the paused list: either it is in in the queue, executing or not exist
		return 0.0;

	}

	/**
	 * Receives an cloudlet to be executed in the VM managed by this scheduler.
	 * 
	 * @param cloudlet the submited cloudlet
	 * @param fileTransferTime time required to move the required files from the SAN to the VM
	 * @return expected finish time of this cloudlet, or 0 if it is in the waiting queue
	 * @pre gl != null
	 * @post $none
	 */
	@Override
	public double serviceRequestSubmit(ServiceRequest cloudlet, double fileTransferTime) {
		// it can go to the exec list
		if ((currentCpus - usedPes) >= cloudlet.getNumberOfPes()) {
			ResServiceRequest rcl = new ResServiceRequest(cloudlet);
			rcl.setServiceRequestStatus(ServiceRequest.INEXEC);
			for (int i = 0; i < cloudlet.getNumberOfPes(); i++) {
				rcl.setMachineAndPeId(0, i);
			}
			getServiceRequestExecList().add(rcl);
			usedPes += cloudlet.getNumberOfPes();
		} else {// no enough free PEs: go to the waiting queue
			ResServiceRequest rcl = new ResServiceRequest(cloudlet);
			rcl.setServiceRequestStatus(ServiceRequest.QUEUED);
			getServiceRequestWaitingList().add(rcl);
			return 0.0;
		}

		// calculate the expected time for cloudlet completion
		double capacity = 0.0;
		int cpus = 0;
		for (Double mips : getCurrentMipsShare()) {
			capacity += mips;
			if (mips > 0) {
				cpus++;
			}
		}

		currentCpus = cpus;
		capacity /= cpus;

		// use the current capacity to estimate the extra amount of
		// time to file transferring. It must be added to the cloudlet length
		double extraSize = capacity * fileTransferTime;
		long length = cloudlet.getServiceLength();
		length += extraSize;
		cloudlet.setServiceLength(length);
		return cloudlet.getServiceLength() / capacity;
	}

	/*
	 * (non-Javadoc)
	 * @see cloudsim.CloudletScheduler#cloudletSubmit(cloudsim.Cloudlet)
	 */
	@Override
	public double serviceRequestSubmit(ServiceRequest cloudlet) {
		return serviceRequestSubmit(cloudlet, 0.0);
	}

	/**
	 * Gets the status of a cloudlet.
	 * 
	 * @param cloudletId ID of the cloudlet
	 * @return status of the cloudlet, -1 if cloudlet not found
	 * @pre $none
	 * @post $none
	 */
	@Override
	public int getServiceRequestStatus(int serviceRequestId) {
		for (ResServiceRequest rcl : getServiceRequestExecList()) {
			if (rcl.getServiceRequestId() == serviceRequestId) {
				return rcl.getServiceRequestStatus();
			}
		}

		for (ResServiceRequest rcl : getServiceRequestPausedList()) {
			if (rcl.getServiceRequestId() == serviceRequestId) {
				return rcl.getServiceRequestStatus();
			}
		}

		for (ResServiceRequest rcl : getServiceRequestWaitingList()) {
			if (rcl.getServiceRequestId() == serviceRequestId) {
				return rcl.getServiceRequestStatus();
			}
		}

		return -1;
	}

	/**
	 * Get utilization created by all cloudlets.
	 * 
	 * @param time the time
	 * @return total utilization
	 */
	@Override
	public double getTotalUtilizationOfCpu(double time) {
		double totalUtilization = 0;
		for (ResServiceRequest gl : getServiceRequestExecList()) {
			totalUtilization += gl.getServiceRequest().getUtilizationOfCpu(time);
		}
		return totalUtilization;
	}

	/**
	 * Informs about completion of some cloudlet in the VM managed by this scheduler.
	 * 
	 * @return $true if there is at least one finished cloudlet; $false otherwise
	 * @pre $none
	 * @post $none
	 */
	@Override
	public boolean isFinishedServiceRequests() {
		return getServiceRequestFinishedList().size() > 0;
	}

	/**
	 * Returns the next cloudlet in the finished list, $null if this list is empty.
	 * 
	 * @return a finished cloudlet
	 * @pre $none
	 * @post $none
	 */
	@Override
	public ServiceRequest getNextFinishedServiceRequest() {
		if (getServiceRequestFinishedList().size() > 0) {
			return getServiceRequestFinishedList().remove(0).getServiceRequest();
		}
		return null;
	}

	/**
	 * Returns the number of cloudlets runnning in the virtual machine.
	 * 
	 * @return number of cloudlets runnning
	 * @pre $none
	 * @post $none
	 */
	@Override
	public int runningServiceRequests() {
		return getServiceRequestExecList().size();
	}

	/**
	 * Returns one cloudlet to migrate to another vm.
	 * 
	 * @return one running cloudlet
	 * @pre $none
	 * @post $none
	 */
	@Override
	public ServiceRequest migrateServiceRequest() {
		ResServiceRequest rcl = getServiceRequestExecList().remove(0);
		rcl.finalizeServiceRequest();
		ServiceRequest cl = rcl.getServiceRequest();
		usedPes -= cl.getNumberOfPes();
		return cl;
	}

	/**
	 * Gets the cloudlet waiting list.
	 * 
	 * @param <T> the generic type
	 * @return the cloudlet waiting list
	 */
	@SuppressWarnings("unchecked")
	public <T extends ResServiceRequest> List<T> getServiceRequestWaitingList() {
		return (List<T>) serviceRequestWaitingList;
	}

	/**
	 * Cloudlet waiting list.
	 * 
	 * @param <T> the generic type
	 * @param cloudletWaitingList the cloudlet waiting list
	 */
	protected <T extends ResServiceRequest> void serviceRequestWaitingList(List<T> serviceRequestWaitingList) {
		this.serviceRequestWaitingList = serviceRequestWaitingList;
	}

	/**
	 * Gets the cloudlet exec list.
	 * 
	 * @param <T> the generic type
	 * @return the cloudlet exec list
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ResServiceRequest> List<T> getServiceRequestExecList() {
		return (List<T>) serviceRequestExecList;
	}

	/**
	 * Sets the cloudlet exec list.
	 * 
	 * @param <T> the generic type
	 * @param cloudletExecList the new cloudlet exec list
	 */
	protected <T extends ResServiceRequest> void setServiceRequestExecList(List<T> serviceRequestExecList) {
		this.serviceRequestExecList = serviceRequestExecList;
	}

	/**
	 * Gets the cloudlet paused list.
	 * 
	 * @param <T> the generic type
	 * @return the cloudlet paused list
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ResServiceRequest> List<T> getServiceRequestPausedList() {
		return (List<T>) serviceRequestPausedList;
	}

	/**
	 * Sets the cloudlet paused list.
	 * 
	 * @param <T> the generic type
	 * @param cloudletPausedList the new cloudlet paused list
	 */
	protected <T extends ResServiceRequest> void setServiceRequestPausedList(List<T> serviceRequestPausedList) {
		this.serviceRequestPausedList = serviceRequestPausedList;
	}

	/**
	 * Gets the cloudlet finished list.
	 * 
	 * @param <T> the generic type
	 * @return the cloudlet finished list
	 */
	@SuppressWarnings("unchecked")
	protected <T extends ResServiceRequest> List<T> getServiceRequestFinishedList() {
		return (List<T>) serviceRequestFinishedList;
	}

	/**
	 * Sets the cloudlet finished list.
	 * 
	 * @param <T> the generic type
	 * @param cloudletFinishedList the new cloudlet finished list
	 */
	protected <T extends ResServiceRequest> void setServiceRequestFinishedList(List<T> serviceRequestFinishedList) {
		this.serviceRequestFinishedList = serviceRequestFinishedList;
	}

	/*
	 * (non-Javadoc)
	 * @see org.cloudbus.cloudsim.CloudletScheduler#getCurrentRequestedMips()
	 */
	@Override
	public List<Double> getCurrentRequestedMips() {
		List<Double> mipsShare = new ArrayList<Double>();
		if (getCurrentMipsShare() != null) {
			for (Double mips : getCurrentMipsShare()) {
				mipsShare.add(mips);
			}
		}
		return mipsShare;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.cloudbus.cloudsim.CloudletScheduler#getTotalCurrentAvailableMipsForCloudlet(org.cloudbus
	 * .cloudsim.ResCloudlet, java.util.List)
	 */
	@Override
	public double getTotalCurrentAvailableMipsForServiceRequest(ResServiceRequest rcl, List<Double> mipsShare) {
		double capacity = 0.0;
		int cpus = 0;
		for (Double mips : mipsShare) { // count the cpus available to the vmm
			capacity += mips;
			if (mips > 0) {
				cpus++;
			}
		}
		currentCpus = cpus;
		capacity /= cpus; // average capacity of each cpu
		return capacity;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.cloudbus.cloudsim.CloudletScheduler#getTotalCurrentAllocatedMipsForCloudlet(org.cloudbus
	 * .cloudsim.ResCloudlet, double)
	 */
	@Override
	public double getTotalCurrentAllocatedMipsForServiceRequest(ResServiceRequest rcl, double time) {
		// TODO Auto-generated method stub
		return 0.0;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.cloudbus.cloudsim.CloudletScheduler#getTotalCurrentRequestedMipsForCloudlet(org.cloudbus
	 * .cloudsim.ResCloudlet, double)
	 */
	@Override
	public double getTotalCurrentRequestedMipsForServiceRequest(ResServiceRequest rcl, double time) {
		// TODO Auto-generated method stub
		return 0.0;
	}

	@Override
	public double getCurrentRequestedUtilizationOfRam() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCurrentRequestedUtilizationOfBw() {
		// TODO Auto-generated method stub
		return 0;
	}

}
