package org.cloudbus.iotnetsim.iot.nodes;

/**
 * Title:        IoTNetSim Toolkit
 * Description:  Modelling and Simulation for End-to-End IoT Services & Networking 
 * 
 * Author: Maria Salama, Lancaster University
 * Contact: m.salama@lancaster.ac.uk
 *
 * If you are using any algorithms, policies or workload included in the SAd/SAw CloudSim Toolkit,
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
 * Enum
 * 
 * @author Maria Salama
 * 
 */

public enum SensorType {
	
	AIR_Temperature_SENSOR,
	AIR_Humidity_SENSOR,
	AIR_CO2_SENSOR,
	AIR_Pressure_SENSOR,
	AIR_WindSpeed_SENSOR,
	AIR_WindDirection_SENSOR,
	AIR_WindVelocity_SENSOR,
	
	WATER_SurfaceFlow_SENSOR,
	
	//SOIL_Temperature_SENSOR,
	//SOIL_Moisture_SENSOR
	
}
