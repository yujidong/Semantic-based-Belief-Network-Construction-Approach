package workload;

/**
 * Implements the generator of workloads for the broker.
 * It generates, for a given simulation time, the number
 * of requests to be generated and when the last burst
 * of requests will arrive. Subclasses must define different
 * approaches to define these parameters, e.g: reading a file,
 * reading a real workload, implementing statistical approximation
 * of workloads, etc. 
 * @author rodrigo
 *
 */
public abstract class RequestsGenerator {
	public abstract int nextRequests(double currentTime);
	
	public abstract double delayToNextEvent(double currentTime);
	
	//public abstract ServiceRequest nextRequests(double currentTime, int serviceTyoe);
	
}
