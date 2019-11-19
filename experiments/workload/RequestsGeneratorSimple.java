package workload;

import java.util.Random;

import org.cloudbus.cloudsim.distributions.ExponentialDistr;

public class RequestsGeneratorSimple extends RequestsGenerator {
	
	ExponentialDistr arrivals;
	Random generator;
	int requests;
	int totalRequests;
	long generatedRequests;

	public RequestsGeneratorSimple(double mean, int requests, int totalRequests) {
		this.arrivals = new ExponentialDistr(System.currentTimeMillis(),mean);
		this.requests = requests;
		this.totalRequests = totalRequests;
		this.generator = new Random(System.currentTimeMillis());
		this.generatedRequests = 0;
	}

	@Override
	public double delayToNextEvent(double currentTime) {
		if (this.generatedRequests>=this.totalRequests) return -1.0;
		return arrivals.sample();
	}

	@Override
	public int nextRequests(double currentTime) {
		int reqs = generator.nextInt(requests)+1;
		if (this.generatedRequests >= this.totalRequests) reqs=0;
		this.generatedRequests += reqs;
		
		//int serviceClass;
		//double type = generator.nextDouble();
		//if(type<=0.20){
			//serviceClass = Service.SERVICE_CLASS_BIG;
		//} else if (type<=0.70){
			//serviceClass = Service.SERVICE_CLASS_DEFAULT;
		//} else {
			//serviceClass = Service.SERVICE_CLASS_SMALL;
		//}
		//return new ServiceRequest(serviceTyoe,reqs);
		return reqs;
	}


	
}
