package org.cloudbus.iotnetsim.fog.applications;

import java.util.ArrayList;

import org.cloudbus.iotnetsim.IoTApplicationModel;
import org.cloudbus.iotnetsim.fog.nodes.EdgeNode;
import org.cloudbus.iotnetsim.fog.nodes.FogNode;
import org.cloudbus.iotnetsim.iot.nodes.IoTNode;

/**
 * Title:        IoTNetSim Toolkit
 * Description:  Modelling and Simulation for End-to-End IoT Services & Networking 
 * 
 * Author: Maria Salama, Lancaster University
 * Contact: m.salama@lancaster.ac.uk
 *
 * If you are using any algorithms, policies or workload included in the IoTNetSim Toolkit,
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
 * Class
 * 
 * @author Maria Salama
 * 
 */

public class EdgeApplication {
	
	private int applicationId;
	private String applicationName;
	private IoTApplicationModel applicationModel;
	
	private String datacenter;
	private ArrayList<IoTNode> lst_IoTNodes = new ArrayList<IoTNode>();
	private ArrayList<EdgeNode> lst_EdgeNodes = new ArrayList<EdgeNode>();
	private ArrayList<FogNode> lst_FogNodes = new ArrayList<FogNode>();
	
	public EdgeApplication() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EdgeApplication(int applicationId, String applicationName, IoTApplicationModel applicationModel, String datacenter) {
		super();
		this.applicationId = applicationId;
		this.applicationName = applicationName;
		this.applicationModel = applicationModel;
		this.datacenter = datacenter;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public IoTApplicationModel getApplicationModel() {
		return applicationModel;
	}

	public void setApplicationModel(IoTApplicationModel applicationModel) {
		this.applicationModel = applicationModel;
	}

	public String getDatacenter() {
		return datacenter;
	}

	public void setDatacenter(String datacenter) {
		this.datacenter = datacenter;
	}

	public ArrayList<IoTNode> getLst_IoTNodes() {
		return lst_IoTNodes;
	}

	public void setLst_IoTNodes(ArrayList<IoTNode> lst_IoTNodes) {
		this.lst_IoTNodes = lst_IoTNodes;
	}

	public ArrayList<EdgeNode> getLst_EdgeNodes() {
		return lst_EdgeNodes;
	}

	public void setLst_EdgeNodes(ArrayList<EdgeNode> lst_EdgeNodes) {
		this.lst_EdgeNodes = lst_EdgeNodes;
	}

	public ArrayList<FogNode> getLst_FogNodes() {
		return lst_FogNodes;
	}

	public void setLst_FogNodes(ArrayList<FogNode> lst_FogNodes) {
		this.lst_FogNodes = lst_FogNodes;
	}
	

}
