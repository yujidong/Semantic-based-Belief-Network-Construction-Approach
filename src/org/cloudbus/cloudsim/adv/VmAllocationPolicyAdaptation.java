package org.cloudbus.cloudsim.adv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
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
 * VmAllocationPolicyAdaptation is an VmAllocationPolicy that extends VmAllocationPolicySimple
 * by enabling and disabling hosts and vms for adaptation purposes.
 * 
 * @author Maria Salama
 * 
 */

public class VmAllocationPolicyAdaptation extends VmAllocationPolicySimple {


	/**
	 * Creates the new VmAllocationPolicyAdaptation object.
	 * 
	 * @param list the list
	 * @pre $none
	 * @post $none
	 */
	public VmAllocationPolicyAdaptation(List<? extends Host> list) {
		super(list);
	}


	/*
	 * (non-Javadoc)
	 * @see org.cloudbus.cloudsim.VmAllocationPolicy#allocateHostForVm(org.cloudbus.cloudsim.Vm,
	 * org.cloudbus.cloudsim.Host)
	 */
	@Override
	public boolean allocateHostForVm(Vm vm, Host host) {
		if (host.vmCreate(vm)) { // if vm has been succesfully created in the host
			getVmTable().put(vm.getUid(), host);

			int requiredPes = vm.getNumberOfPes();
			int idx = getHostList().indexOf(host);

			getUsedPes().put(vm.getUid(), requiredPes);

			Log.formatLine(
					"%.2f: VM #" + vm.getId() + " has been allocated to the host #" + host.getId(),
					CloudSim.clock());
			return true;
		}
		return false;
	}

	/**
	 * Releases the host used by a VM.
	 * 
	 * @param vm the vm
	 * @pre $none
	 * @post none
	 */
	@Override
	public void deallocateHostForVm(Vm vm) {
		Host host = getVmTable().remove(vm.getUid());
		int idx = getHostList().indexOf(host);
		if (getUsedPes().containsKey(vm.getUid())) {
			int pes = getUsedPes().remove(vm.getUid());
			if (host != null) { 
				host.vmDestroy(vm); 
				if ((getFreePes().contains(idx)) && (idx < getFreePes().size())) {
					getFreePes().set(idx, getFreePes().get(idx) + pes);
				}
			}
		}
	}
		
	/**
	 * Turn on the vm in the host used without new addition.
	 * 
	 * @param vm the vm
	 * @pre $none
	 * @post none
	 */
	@Override
	public void enableVm(Vm vm) {
		int requiredPes = vm.getNumberOfPes();
		int idx = getHostList().indexOf(vm.getHost());
		getUsedPes().put(vm.getUid(), requiredPes);
		if (getFreePes().contains(idx)) {
			getFreePes().set(idx, getFreePes().get(idx) - requiredPes);
		}
		
		Log.formatLine(
				"%.2f: VM #" + vm.getId() + " has been enabled in Host #" + vm.getHost().getId(),
				CloudSim.clock());		
	}
	
	/**
	 * Turn off the vm and free the host used without deletion.
	 * 
	 * @param vm the vm
	 * @pre $none
	 * @post none
	 */
	@Override
	public void disableVm(Vm vm) { 
		Host host = getVmTable().get(vm.getUid());
		if (host != null) {
			int idx = getHostList().indexOf(host);
			host.vmDisable(vm);
			if (getUsedPes().containsKey(idx)) {
				int pes = getUsedPes().remove(vm.getUid());
				if (getFreePes().contains(idx)) {
					getFreePes().set(idx, getFreePes().get(idx) + pes);
				} else {
					getFreePes().add(pes);
				}
			}
		}
		Log.formatLine(
				"%.2f: VM #" + vm.getId() + " has been disabled in Host #" + vm.getHost().getId(),
				CloudSim.clock());		
	}
	
	public void addHost(Host newHost) {
		//getHostList().add(newHost);
		getFreePes().add(newHost.getNumberOfPes());
	}

	public void removeHost(Host host) {
		int idx = getHostList().indexOf(host);
		getUsedPes().remove(idx);
		getFreePes().remove(idx);

		for (Map.Entry<String, Host> entry : getVmTable().entrySet()) {
		    if (entry.getValue() == host) {
		    	getVmTable().remove(entry.getKey(), entry.getValue());
		    }
		}
	}

	public void enableHost(Host newHost) {
		getFreePes().add(newHost.getNumberOfPes());
	}

	public void disableHost(Host host) {
		int idx = getHostList().indexOf(host);
		getUsedPes().remove(idx);
		if (getFreePes().contains(idx)) {
			getFreePes().remove(idx);
		}
	}
	
	
}
