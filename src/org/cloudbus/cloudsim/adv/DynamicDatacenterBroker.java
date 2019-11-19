package org.cloudbus.cloudsim.adv;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.lists.VmList;

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

public class DynamicDatacenterBroker extends DatacenterBroker {
	
	
	public DynamicDatacenterBroker(String name) throws Exception {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Submit cloudlets to the created VMs.
	 * allows multiple cloudltes to be executed by one VM, 
	 * submitting cloudlets to the VM with the least number of cloudlets assigned.
	 * Cloudlets are runtime workload and are assigned 
	 * using a single queue for each vm.
	 * 
	 * @pre $none
	 * @post $none
	 */
	protected void submitServiceRequests() {
		Log.printLine(CloudSim.clock() + ": [" + getName() + "] Sending Service Requests to VMs... ");	
		List<Double> intervals = getWorkloadTimeIntervals();
		
		for (double interval : intervals) {
			//get requests of this time interval
			List<ServiceRequest> interval_cloudlets = getIntervalCloudlets(interval);

			int vmIndex = 0;
			for (ServiceRequest cloudlet : interval_cloudlets) {
				Vm vm;			
				if (cloudlet.getVmId() == -1) {// if user didn't bind this cloudlet and it has not been executed yet
					vm = VmList.getById(getVmsCreatedList(), cloudlet.getVmId());
				} else { // submit to the specific vm
					vm = super.getVmsCreatedList().get(vmIndex);
					//get the next vm
					vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
					if (vm == null) { // vm was not created
						Log.printLine(CloudSim.clock() + ": " + getName() + ": Postponing execution of Service Request " 
								+ cloudlet.getServiceRequestId() + ": bount VM not available");
						continue;
					}
				}
				scheduleServiceRequestToVm(cloudlet, vm);
				cloudletsSubmitted++;
				getServiceRequestSubmittedList().add(cloudlet);		
			}
			Log.printLine();
		}
		
		// remove submitted cloudlets from waiting list
		for (ServiceRequest cloudlet : getServiceRequestSubmittedList()) {
			getServiceRequestList().remove(cloudlet);
		}
	}
	
	protected void scheduleServiceRequestToVm (ServiceRequest cloudlet, Vm vm) {
		//Log.printLine(CloudSim.clock() + ": [" + getName() + "] Sending Service Request " + cloudlet.getServiceRequestId() + " to VM #" + vm.getId());
		cloudlet.setVmId(vm.getId());
		vm.getAssignedServiceRequestList().add(cloudlet);		

		//schedule request processing at its arrival time
		schedule(getVmsToDatacentersMap().get(vm.getId()), cloudlet.getArrivalTime(), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
	}

	protected List<Double> getWorkloadTimeIntervals() {
		List<Double> intervals = new ArrayList<Double>();

		double temp = 0.0;
		intervals.add(temp);
		for (ServiceRequest cloudlet : getServiceRequestList()) {
			if (cloudlet.getArrivalTime() != temp) {
				temp = cloudlet.getArrivalTime();
				intervals.add(temp);
			}
		}
		return intervals;	
	}

	protected List<ServiceRequest> getIntervalCloudlets(double intervalTime) {
		List<ServiceRequest> interval_cloudlets = new ArrayList<ServiceRequest>();
	
		for (ServiceRequest cloudlet : getServiceRequestList()) {
			if (cloudlet.getArrivalTime() == intervalTime){
				interval_cloudlets.add(cloudlet);
			}
		}
		return interval_cloudlets;
	}

	
}
