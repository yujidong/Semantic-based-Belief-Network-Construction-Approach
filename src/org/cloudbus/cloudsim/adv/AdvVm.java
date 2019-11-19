package org.cloudbus.cloudsim.adv;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.util.MathUtil;

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

public class AdvVm extends Vm {

	/** The Constant HISTORY_LENGTH. */
	public static final int HISTORY_LENGTH = 30;

	/** The utilization history. */
	private final List<Double> utilizationHistory = new LinkedList<Double>();

	/** The previous time. */
	private double previousTime;

	/** The scheduling interval. */
	private double schedulingInterval;
	
	/** Turned On/off for adaptation. */
	private boolean isOn;

	
	/**
	 * Creates a new VMCharacteristics object.
	 * 
	 * @param id unique ID of the VM
	 * @param userId ID of the VM's owner
	 * @param vm type constant 
	 * @param mips the mips
	 * @param numberOfPes amount of CPUs
	 * @param ram amount of ram
	 * @param bw amount of bandwidth
	 * @param size amount of storage
	 * @param priority the priority
	 * @param vmm virtual machine monitor
	 * @param cloudletScheduler cloudletScheduler policy for cloudlets
	 * @param schedulingInterval the scheduling interval
	 * @pre id >= 0
	 * @pre userId >= 0
	 * @pre size > 0
	 * @pre ram > 0
	 * @pre bw > 0
	 * @pre cpus > 0
	 * @pre priority >= 0
	 * @pre cloudletScheduler != null
	 * @post $none
	 */
	public AdvVm(
			int id,
			int userId,
			int type,
			double mips,
			int numberOfPes,
			int ram,
			long bw,
			long size,
			int priority,
			String vmm,
			ServiceRequestScheduler cloudletScheduler,
			double schedulingInterval) 
	{
		super(id, userId, type, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
		
		setSchedulingInterval(schedulingInterval);
		isOn = true;

	}

	/**
	 * Updates the processing of cloudlets running on this VM.
	 * 
	 * @param currentTime current simulation time
	 * @param mipsShare array with MIPS share of each Pe available to the scheduler
	 * @return time predicted completion time of the earliest finishing cloudlet, or 0 if there is no
	 *         next events
	 * @pre currentTime >= 0
	 * @post $none
	 */
	public double updateVmProcessing(double currentTime, List<Double> mipsShare) {
	
		if (mipsShare != null) {
			double time = 0.0;
			time = getServiceRequestScheduler().updateVmProcessing(currentTime, mipsShare);
			
			if (currentTime > getPreviousTime() && (currentTime - 0.1) % getSchedulingInterval() == 0) {
				double utilization = getTotalUtilizationOfCpu(getServiceRequestScheduler().getPreviousTime());
				if (CloudSim.clock() != 0 || utilization != 0) {
					addUtilizationHistoryValue(utilization);
				}
				setPreviousTime(currentTime);
			}
			return time;		
		}	
		return 0.0;
	}

	/**
	 * Gets the utilization MAD in MIPS.
	 * 
	 * @return the utilization mean in MIPS
	 */
	public double getUtilizationMad() {
		double mad = 0;
		if (!getUtilizationHistory().isEmpty()) {
			int n = HISTORY_LENGTH;
			if (HISTORY_LENGTH > getUtilizationHistory().size()) {
				n = getUtilizationHistory().size();
			}
			double median = MathUtil.median(getUtilizationHistory());
			double[] deviationSum = new double[n];
			for (int i = 0; i < n; i++) {
				deviationSum[i] = Math.abs(median - getUtilizationHistory().get(i));
			}
			mad = MathUtil.median(deviationSum);
		}
		return mad;
	}

	/**
	 * Gets the utilization mean in percents.
	 * 
	 * @return the utilization mean in MIPS
	 */
	public double getUtilizationMean() {
		double mean = 0;
		if (!getUtilizationHistory().isEmpty()) {
			int n = HISTORY_LENGTH;
			if (HISTORY_LENGTH > getUtilizationHistory().size()) {
				n = getUtilizationHistory().size();
			}
			for (int i = 0; i < n; i++) {
				mean += getUtilizationHistory().get(i);
			}
			mean /= n;
		}
		return mean * getMips();
	}

	/**
	 * Gets the utilization variance in MIPS.
	 * 
	 * @return the utilization variance in MIPS
	 */
	public double getUtilizationVariance() {
		double mean = getUtilizationMean();
		double variance = 0;
		if (!getUtilizationHistory().isEmpty()) {
			int n = HISTORY_LENGTH;
			if (HISTORY_LENGTH > getUtilizationHistory().size()) {
				n = getUtilizationHistory().size();
			}
			for (int i = 0; i < n; i++) {
				double tmp = getUtilizationHistory().get(i) * getMips() - mean;
				variance += tmp * tmp;
			}
			variance /= n;
		}
		return variance;
	}

	/**
	 * Adds the utilization history value.
	 * 
	 * @param utilization the utilization
	 */
	public void addUtilizationHistoryValue(final double utilization) {
		getUtilizationHistory().add(0, utilization);
		if (getUtilizationHistory().size() > HISTORY_LENGTH) {
			getUtilizationHistory().remove(HISTORY_LENGTH);
		}
	}

	/**
	 * Gets the utilization history.
	 * 
	 * @return the utilization history
	 */
	protected List<Double> getUtilizationHistory() {
		return utilizationHistory;
	}

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
	public void setPreviousTime(final double previousTime) {
		this.previousTime = previousTime;
	}

	/**
	 * Gets the scheduling interval.
	 * 
	 * @return the schedulingInterval
	 */
	public double getSchedulingInterval() {
		return schedulingInterval;
	}

	/**
	 * Sets the scheduling interval.
	 * 
	 * @param schedulingInterval the schedulingInterval to set
	 */
	protected void setSchedulingInterval(final double schedulingInterval) {
		this.schedulingInterval = schedulingInterval;
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}


}
