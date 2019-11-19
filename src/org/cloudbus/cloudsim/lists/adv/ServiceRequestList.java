package org.cloudbus.cloudsim.lists.adv;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
 * Class
 * 
 * @author Maria Salama
 * 
 */

public class ServiceRequestList {

	/**
	 * Gets the by id.
	 * 
	 * @param cloudletList the cloudlet list
	 * @param id the id
	 * @return the by id
	 */
	public static <T extends ServiceRequest> T getById(List<T> cloudletList, int id) {
		for (T cloudlet : cloudletList) {
			if (cloudlet.getServiceRequestId() == id) {
				return cloudlet;
			}
		}
		return null;
	}

	/**
	 * Returns the position of the cloudlet with that id, if it exists. Otherwise -1.
	 * @param cloudletList - the list of cloudlets.
	 * @param id - the id we search for.
	 * @return - the position of the cloudlet with that id, or -1 otherwise.
	 */
	public static <T extends ServiceRequest> int getPositionById(List<T> cloudletList, int id) {
		int i = 0 ;
	        for (T cloudlet : cloudletList) {
			if (cloudlet.getServiceRequestId() == id) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	/**
	 * Sorts the Cloudlets in a list based on their lengths.
	 * 
	 * @param cloudletList the cloudlet list
	 * @pre $none
	 * @post $none
	 */
	public static <T extends ServiceRequest> void sort(List<T> cloudletList) {
		Collections.sort(cloudletList, new Comparator<T>() {

			/**
			 * Compares two objects.
			 * 
			 * @param a the first Object to be compared
			 * @param b the second Object to be compared
			 * @return the value 0 if both Objects are numerically equal; a value less than 0 if the
			 *         first Object is numerically less than the second Object; and a value greater
			 *         than 0 if the first Object is numerically greater than the second Object.
			 * @throws ClassCastException <tt>a</tt> and <tt>b</tt> are expected to be of type
			 *             <tt>Cloudlet</tt>
			 * @pre a != null
			 * @pre b != null
			 * @post $none
			 */
			@Override
			public int compare(T a, T b) throws ClassCastException {
				Double cla = Double.valueOf(a.getServiceTotalLength());
				Double clb = Double.valueOf(b.getServiceTotalLength());
				return cla.compareTo(clb);
			}
		});
	}

}
