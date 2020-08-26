/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.core;

/**
 * Contains various static command tags that indicate a type of action that needs to be undertaken
 * by CloudSim entities when they receive or send events. <b>NOTE:</b> To avoid conflicts with other
 * tags, CloudSim reserves negative numbers, 0 - 299, and 9600.
 * 
 * @author Manzur Murshed
 * @author Rajkumar Buyya
 * @author Anthony Sulistio
 * @since CloudSim Toolkit 1.0
 */

/**
 * Uodated to add SAd/SAw-related tags
 * 
 * @author m.salama
 * @version SAdSAwCloudSim
 */

public class CloudSimTags {

	/** 
	 * Starting constant value bases 
	*/

	/** Starting constant value for cloud-related tags **/
	private static final int BASE = 0;

	/** Starting constant value for network-related tags **/
	private static final int NETBASE = 100;

	/** Starting constant value for SAd-related tags **/
	private static final int SAD_BASE = 200;

	/** Starting constant value for SAw-related tags **/
	private static final int SAW_BASE = 300;

	/** Starting constant value for SAd-related tags for stability **/
	private static final int SAD_STABILITY_BASE = 400;

	/** Starting constant value for NetIotSim-related tags **/
	private static final int IOT_BASE = 500;

	
	/** 
	 * Starting cloud-related tags 
	*/

	/** Denotes boolean <tt>true</tt> in <tt>int</tt> value */
	public static final int TRUE = 1;

	/** Denotes boolean <tt>false</tt> in <tt>int</tt> value */
	public static final int FALSE = 0;

	/** Denotes the default baud rate for some CloudSim entities */
	public static final int DEFAULT_BAUD_RATE = 9600;

	/** Schedules an entity without any delay */
	public static final double SCHEDULE_NOW = 0.0;

	/** Denotes the end of simulation */
	public static final int END_OF_SIMULATION = -1;

	/**
	 * Denotes an abrupt end of simulation. That is, one event of this type is enough for
	 * {@link CloudSimShutdown} to trigger the end of the simulation
	 */
	public static final int ABRUPT_END_OF_SIMULATION = -2;

	/**
	 * Denotes insignificant simulation entity or time. This tag will not be used for identification
	 * purposes.
	 */
	public static final int INSIGNIFICANT = BASE + 0;

	/** Sends an Experiment object between UserEntity and Broker entity */
	public static final int EXPERIMENT = BASE + 1;

	/**
	 * Denotes a grid resource to be registered. This tag is normally used between
	 * CloudInformationService and CloudResouce entity.
	 */
	public static final int REGISTER_RESOURCE = BASE + 2;

	/**
	 * Denotes a grid resource, that can support advance reservation, to be registered. This tag is
	 * normally used between CloudInformationService and CloudResouce entity.
	 */
	public static final int REGISTER_RESOURCE_AR = BASE + 3;

	/**
	 * Denotes a list of all hostList, including the ones that can support advance reservation. This
	 * tag is normally used between CloudInformationService and CloudSim entity.
	 */
	public static final int RESOURCE_LIST = BASE + 4;

	/**
	 * Denotes a list of hostList that only support advance reservation. This tag is normally used
	 * between CloudInformationService and CloudSim entity.
	 */
	public static final int RESOURCE_AR_LIST = BASE + 5;

	/**
	 * Denotes grid resource characteristics information. This tag is normally used between CloudSim
	 * and CloudResource entity.
	 */
	public static final int RESOURCE_CHARACTERISTICS = BASE + 6;

	/**
	 * Denotes grid resource allocation policy. This tag is normally used between CloudSim and
	 * CloudResource entity.
	 */
	public static final int RESOURCE_DYNAMICS = BASE + 7;

	/**
	 * Denotes a request to get the total number of Processing Elements (PEs) of a resource. This
	 * tag is normally used between CloudSim and CloudResource entity.
	 */
	public static final int RESOURCE_NUM_PE = BASE + 8;

	/**
	 * Denotes a request to get the total number of free Processing Elements (PEs) of a resource.
	 * This tag is normally used between CloudSim and CloudResource entity.
	 */
	public static final int RESOURCE_NUM_FREE_PE = BASE + 9;

	/**
	 * Denotes a request to record events for statistical purposes. This tag is normally used
	 * between CloudSim and CloudStatistics entity.
	 */
	public static final int RECORD_STATISTICS = BASE + 10;

	/** Denotes a request to get a statistical list. */
	public static final int RETURN_STAT_LIST = BASE + 11;

	/**
	 * Denotes a request to send an Accumulator object based on category into an event scheduler.
	 * This tag is normally used between ReportWriter and CloudStatistics entity.
	 */
	public static final int RETURN_ACC_STATISTICS_BY_CATEGORY = BASE + 12;

	/**
	 * Denotes a request to register a CloudResource entity to a regional CloudInformationService
	 * (GIS) entity
	 */
	public static final int REGISTER_REGIONAL_GIS = BASE + 13;

	/**
	 * Denotes a request to get a list of other regional GIS entities from the system GIS entity
	 */
	public static final int REQUEST_REGIONAL_GIS = BASE + 14;

	/**
	 * Denotes request for grid resource characteristics information. This tag is normally used
	 * between CloudSim and CloudResource entity.
	 */
	public static final int RESOURCE_CHARACTERISTICS_REQUEST = BASE + 15;

	/**
	 * Denotes the return of a Cloudlet back to sender. This tag is normally used by CloudResource
	 * entity.
	 */
	public static final int CLOUDLET_RETURN = BASE + 20;

	/**
	 * Denotes the submission of a Cloudlet. This tag is normally used between CloudSim User and
	 * CloudResource entity.
	 */
	public static final int CLOUDLET_SUBMIT = BASE + 21;

	/**
	 * Denotes the submission of a Cloudlet with an acknowledgement. This tag is normally used
	 * between CloudSim User and CloudResource entity.
	 */
	public static final int CLOUDLET_SUBMIT_ACK = BASE + 22;

	/** Cancels a Cloudlet submitted in the CloudResource entity. */
	public static final int CLOUDLET_CANCEL = BASE + 23;

	/** Denotes the status of a Cloudlet. */
	public static final int CLOUDLET_STATUS = BASE + 24;

	/** Pauses a Cloudlet submitted in the CloudResource entity. */
	public static final int CLOUDLET_PAUSE = BASE + 25;

	/**
	 * Pauses a Cloudlet submitted in the CloudResource entity with an acknowledgement.
	 */
	public static final int CLOUDLET_PAUSE_ACK = BASE + 26;

	/** Resumes a Cloudlet submitted in the CloudResource entity. */
	public static final int CLOUDLET_RESUME = BASE + 27;

	/**
	 * Resumes a Cloudlet submitted in the CloudResource entity with an acknowledgement.
	 */
	public static final int CLOUDLET_RESUME_ACK = BASE + 28;

	/** Moves a Cloudlet to another CloudResource entity. */
	public static final int CLOUDLET_MOVE = BASE + 29;

	/**
	 * Moves a Cloudlet to another CloudResource entity with an acknowledgement.
	 */
	public static final int CLOUDLET_MOVE_ACK = BASE + 30;

	/**
	 * Denotes a request to create a new VM in a Datacentre With acknowledgement information sent by
	 * the Datacentre
	 */
	public static final int VM_CREATE = BASE + 31;

	/**
	 * Denotes a request to create a new VM in a Datacentre With acknowledgement information sent by
	 * the Datacentre
	 */
	public static final int VM_CREATE_ACK = BASE + 32;

	/**
	 * Denotes a request to destroy a new VM in a Datacentre
	 */
	public static final int VM_DESTROY = BASE + 33;

	/** Denotes a request to destroy a new VM in a Datacentre */
	public static final int VM_DESTROY_ACK = BASE + 34;

	/** Denotes a request to migrate a new VM in a Datacentre */
	public static final int VM_MIGRATE = BASE + 35;

	/** Denotes a request to migrate a new VM in a Datacentre With acknowledgement information sent 
	 * by the Datacenter */
	public static final int VM_MIGRATE_ACK = BASE + 36;

	/** Denotes an event to send a file from a user to a datacenter */
	public static final int VM_DATA_ADD = BASE + 37;

	/** Denotes an event to send a file from a user to a datacenter */
	public static final int VM_DATA_ADD_ACK = BASE + 38;

	/** Denotes an event to remove a file from a datacenter */
	public static final int VM_DATA_DEL = BASE + 39;

	/** Denotes an event to remove a file from a datacenter */
	public static final int VM_DATA_DEL_ACK = BASE + 40;

	/** Denotes an internal event generated in a PowerDatacenter */
	public static final int VM_DATACENTER_EVENT = BASE + 41;

	/** Denotes an internal event generated in a Broker */
	public static final int VM_BROKER_EVENT = BASE + 42;

	public static final int Network_Event_UP = BASE + 43;

	public static final int Network_Event_send = BASE + 44;

	public static final int RESOURCE_Register = BASE + 45;

	public static final int Network_Event_DOWN = BASE + 46;

	public static final int Network_Event_Host = BASE + 47;

	public static final int NextCycle = BASE + 48;
	
	
	/** 
	 * Starting netwrok-related tags 
	*/

	/** This tag is used by an entity to send ping requests */
	public static final int INFOPKT_SUBMIT = NETBASE + 5;

	/** This tag is used to return the ping request back to sender */
	public static final int INFOPKT_RETURN = NETBASE + 6;


	/** 
	 * Starting SAd related tags 
	*/

	/** This tag is used to set QoS goals for adaptation */
	public static final int SAD_SET_GOALS = SAD_BASE + 1;
	
	/** This tag is used for monitoring goals for adaptation */
	public static final int SAD_MONITOR = SAD_BASE + 2;
	
	/** This tag is used for detecting violations in goals for adaptation */
	public static final int SAD_DETECT_VIOLATIONS = SAD_BASE + 3;
	
	/** This tag is used for taking adaptation decisions */
	public static final int SAD_ADAPTATION_DECISION = SAD_BASE + 4;
	
	/** This tag is used for executing adaptation decisions */
	public static final int SAD_EXECUTE_ADAPTATION = SAD_BASE + 5;
	
	/** This tag is used for vertical scaling (increasing the capabilities of VMs) */
	public static final int VERTICAL_SCALING_CAP = SAD_BASE + 6;
	
	/** This tag is used for vertical de-scaling (decreasing the capabilities of VMs) */
	public static final int VERTICAL_DESCALING_CAP = SAD_BASE + 7;
	
	/** This tag is used for vertical scaling (increasing the number of VMs) */
	public static final int VERTICAL_SCALING_NUM = SAD_BASE + 8;
	
	/** This tag is used for vertical de-scaling (decreasing the number of VMs) */
	public static final int VERTICAL_DESCALING_NUM = SAD_BASE + 9;
	
	/** This tag is used for horizontal scaling (increasing the number of PMs) */
	public static final int HORIZONTAL_SCALING = SAD_BASE + 10;
	
	/** This tag is used for horizontal de-scaling (decreasing the number of PMs) */
	public static final int HORIZONTAL_DESCALING = SAD_BASE + 11;
	
	/** This tag is used for VM consolidation (migrating VMs and shut down unused PMs) */
	public static final int VM_CONSOLIDATION = SAD_BASE + 12;
	
	/** This tag is used for reflecting self-awareness decisions in the next time intervals */
	public static final int REFLECT_ADAPTATION= SAW_BASE + 14;
	
	
	/** 
	 * Starting SAw related tags 
	*/
	
	/** This tag is used to set QoS runtime goals for adaptation */
	public static final int SAW_SET_RT_GOALS = SAW_BASE + 1;
	
	/** This tag is used for QoS Monitor */
	public static final int SAW_QOS_MONITOR = SAD_BASE + 2;

	/** This tag is used for taking adaptation decisions using self-awareness capabilities */
	public static final int SAW_SELF_AWARENESS = SAW_BASE + 3;

	/** This tag is used for running stimulus-awareness */
	public static final int SAW_STIMULUS_AWARENESS = SAW_BASE + 4;
	
	/** This tag is used for running goal-awareness */
	public static final int SAW_GOAL_AWARENESS = SAW_BASE + 5;
	
	/** This tag is used for running time-awareness */
	public static final int SAW_TIME_AWARENESS = SAW_BASE + 6;
	
	/** This tag is used for running interaction-awareness */
	public static final int SAW_INTERACTION_AWARENESS = SAW_BASE + 7;

	/** This tag is used for running goal-awareness */
	public static final int SAW_META_SELF_AWARENESS = SAW_BASE + 8;
	
	/** This tag is used for executing meta-self-awareness decisions */
	public static final int SAW_SELF_EXPRESSION = SAW_BASE + 9;

	
	
	/** 
	 * Starting SAd related tags for stability
	*/

	/** This tag is used for monitoring goals for adaptation */
	public static final int SAD_MONITOR_STABILITY = SAD_STABILITY_BASE + 2;
	
	/** This tag is used for detecting violations in goals for adaptation */
	public static final int SAD_DETECT_VIOLATIONS_STABILITY = SAD_STABILITY_BASE + 3;
	
	/** This tag is used for taking adaptation decisions */
	public static final int SAD_ADAPTATION_DECISION_STABILITY = SAD_STABILITY_BASE + 4;
	
	/** This tag is used for executing adaptation decisions */
	public static final int SAD_EXECUTE_ADAPTATION_STABILITY = SAD_STABILITY_BASE + 5;
	

	
	/** 
	 * Starting IoTNetSim related tags 
	*/
	
	/** This tag is used for the generic IoT Node sending data to the next node */
	public static final int IOT_NODE_SEND_DATA_EVENT = IOT_BASE + 1;

	/** This tag is used for the generic IoT Node receiving data */
	public static final int IOT_NODE_RECEIVE_DATA_EVENT = IOT_BASE + 2;

	/** This tag is used for sending data from sensors to the next node */
	public static final int IOT_SENSOR_SEND_DATA_EVENT = IOT_BASE + 3;

	/** This tag is used for a link node to receive data from a node  */
	public static final int IOT_LINK_RECEIVE_DATA_EVENT = IOT_BASE + 4;
	
	/** This tag is used for a link node to forward data previously received from a node  */
	public static final int IOT_LINK_FORWARD_DATA_EVENT = IOT_BASE + 5;
	
	/** This tag is used for a gateway node to receive data from a link node  */
	public static final int IOT_GATEWAY_RECEIVE_DATA_EVENT = IOT_BASE + 6;
	
	/** This tag is used for a gateway node to process data received  */
	public static final int IOT_GATEWAY_PROCESS_DATA_EVENT = IOT_BASE + 7;

	/** This tag is used for a gateway node to forward data previously received from a node  */
	public static final int IOT_GATEWAY_SEND_AGGREGATED_DATA_EVENT = IOT_BASE + 8;
	
	/** This tag is used for a cloud node to receive data from a gateway node  */
	public static final int IOT_CLOUD_RECEIVE_DATA_EVENT = IOT_BASE + 9;
	
	/** This tag is used for a cloud node to process data received  */
	public static final int IOT_CLOUD_PROCESS_DATA_EVENT = IOT_BASE + 10;

	/** This tag is used for a sensor to move and change a related link node */
	public static final int IOT_SENSOR_MOVE_EVENT = IOT_BASE + 11;

	/** This tag is used for a sensor to update its uncertainty rate */
	public static final int IOT_SENSOR_UPDATE_Belief = IOT_BASE + 12;

	public static final int EDGE_NODE_RECEIVE_DATA_EVENT = IOT_BASE + 20;
	public static final int EDGE_NODE_FILTER_DATA_EVENT = IOT_BASE + 21;
	public static final int EDGE_NODE_INTERPRET_DATA_EVENT = IOT_BASE + 22;
	public static final int EDGE_NODE_PROCESS_DATA_EVENT = IOT_BASE + 23;
	public static final int EDGE_NODE_SEND_DATA_EVENT = IOT_BASE + 24;
	public static final int EDGE_NODE_SEND_PROCESSED_DATA_EVENT = IOT_BASE + 25;
	public static final int EDGE_NODE_ACT_EVENT = IOT_BASE + 26;

	public static final int FOG_NODE_RECEIVE_DATA_EVENT = IOT_BASE + 30;
	public static final int FOG_NODE_SEND_DATA_EVENT = IOT_BASE + 31;
	public static final int FOG_NODE_ACT_EVENT = IOT_BASE + 32;

	
	/** Private Constructor */
	private CloudSimTags() {
		throw new UnsupportedOperationException("CloudSim Tags cannot be instantiated");
	}

}
