package configurations;

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

public class ExperimentsConfigurations {
	
	//for setting the number of days to run the experiment
	public static int EXP_NO_OF_DAYS = 30;

	//variable readingInterval is used to represent the different reading intervals
	public static double READING_INTERVAL[] = {24*60*60, 12*60*60, 6*60*60, 3*60*60};			//every no_of_hours*60min*60sec = every x seconds


	//Runtime Workload Constants for IoT
	//public final static int WORKLOAD_INTERVAL = 24*60*60;   		//every one day in seconds
	public final static int WORKLOAD_INTERVAL[] = {24*60*60, 12*60*60, 6*60*60, 3*60*60};			//every no_of_hours*60min*60sec = every x seconds

}
