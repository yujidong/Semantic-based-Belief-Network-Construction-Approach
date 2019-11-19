package org.cloudbus.iotnetsim.fog.network;

import java.util.ArrayList;

import org.cloudbus.iotnetsim.Location;
import org.cloudbus.iotnetsim.fog.nodes.EdgeNode;

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

public class NetworkEdge {
	
	private int edgeId;
	private Location location;
	private ArrayList<EdgeNode> lst_EdgeNodes = new ArrayList<EdgeNode>();
	
	public NetworkEdge() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NetworkEdge(int edgeId, Location location, ArrayList<EdgeNode> lst_EdgeNodes) {
		super();
		this.edgeId = edgeId;
		this.location = location;
		this.lst_EdgeNodes = lst_EdgeNodes;
	}

	public void addEdgeNode(EdgeNode newNode) {
		lst_EdgeNodes.add(newNode);
	}
	
	public void removeEdgeNode(EdgeNode node) {
		lst_EdgeNodes.remove(node);
	}
	
	public void moveEdgeNode(EdgeNode node, NetworkEdge newEdge) {
		lst_EdgeNodes.remove(node);
		newEdge.addEdgeNode(node);
	}
	
	public int getEdgeId() {
		return edgeId;
	}

	public void setEdgeId(int edgeId) {
		this.edgeId = edgeId;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ArrayList<EdgeNode> getLst_EdgeNodes() {
		return lst_EdgeNodes;
	}

	public void setLst_EdgeNodes(ArrayList<EdgeNode> lst_EdgeNodes) {
		this.lst_EdgeNodes = lst_EdgeNodes;
	}
	
	

}
