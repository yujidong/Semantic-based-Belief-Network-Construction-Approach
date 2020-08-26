package experiments;

import configurations.ExperimentsConfigurations;
import helper.Setup;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.adv.AdvHost;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.iotnetsim.*;
import org.cloudbus.iotnetsim.iot.nodes.GatewayNode;
import org.cloudbus.iotnetsim.iot.nodes.LinkNode;
import org.cloudbus.iotnetsim.iot.nodes.SensorNode;
import org.cloudbus.iotnetsim.iot.nodes.SensorType;
import org.cloudbus.iotnetsim.network.NetConnection;
import org.cloudbus.iotnetsim.network.NetConnectionType;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ForestFireIoT {
    public static void main(String[] args) {
        //simulation parameters
        double exp_readingInterval = ExperimentsConfigurations.READING_INTERVAL[0];			//variable scenario is used to represent the reading interval
        int exp_workloadInterval = ExperimentsConfigurations.WORKLOAD_INTERVAL[0];
        int exp_no_months = 1;		//variable exp_no_months is used for varying the number of months
        int exp_no_locations = 279;
        int exp_no_runs = 1;			//variable exp_no_runs is used to set running each experiment for x times

        //set the experiment number of days according to the number of months specified for the experiment
        ExperimentsConfigurations.EXP_NO_OF_DAYS = exp_no_months*30;	//*30days

        try {
            String workingDir = System.getProperty("user.dir");
            String datasetsFolder = workingDir + "//experiments//datasets//";
            String workloadFolder = workingDir + "/experiments//workload//";
            String resultsFolder = workingDir + "//experiments//results//";

            OutputStream output = new FileOutputStream(resultsFolder+ "ForestFireIoT.txt");
            //Log.setOutput(output);	//Uncomment to write output log in the txt files


            Log.printLine("Starting ForestFireIoT experiment...");
            Log.printLine("running the simulation for " + exp_no_months + " month ");
            Log.printLine("for " + ExperimentsConfigurations.READING_INTERVAL.length + " different scenarios");
            Log.printLine("for each scenario we vary the number of locations from 1 to 300 locations ");

        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("Unwanted errors happen");
        }
    }

    private static void createTestbed(Datacenter datacenter, double readingInterval, String datasetsFolder,
                                      int area, int numResourceNode, int numLinkNode, int numSensor, int numMovingSensor) {
        //create one IoT testbed
        Log.printLine("Creating one testbed...");
        GeographicRegion region = new GeographicRegion("A virtual forest", area);

        int resourceNodeDistance = (int) Math.sqrt(area/numResourceNode);
        int sensorNodeDistance;

        Map<String, Location> locationMap = new HashMap<String, Location>();


        //create CloudServer
        AdvHost cloudServer = Setup.createAdvHost(100, 3, 2);

        for(int i = 1; i <= numResourceNode; i++){
            GatewayNode resourceNode = new GatewayNode(
                    "resourceNode" + i,
                    locationMap.get("resourceNode"+i),
                    IoTNodeType.GATEWAY_Node,
                    new NetConnection("conn_3G", new NetConnectionType(),100.00),
                    new IoTNodePower(IoTNodePowerType.CONTINUOUS_POWER, true, false, true, 100.00, 0, 0),
                    datacenter.getId(),
                    readingInterval + CloudSim.getMinTimeBetweenEvents()*3,
                    readingInterval+CloudSim.getMinTimeBetweenEvents()*3
            );
        }

        for(int i = 1; i <=numResourceNode; i++){
            LinkNode relayNode = new LinkNode(
                    "linkNode" + i,
                    locationMap.get("linkNode"+i),
                    IoTNodeType.LINK_NODE,
                    new NetConnection("conn_longRadio", new NetConnectionType(), 100.00),
                    new IoTNodePower(IoTNodePowerType.USB_CAHRGING, true, false, true, 100.00, 0.00, 0.00),
                    "resourceNode" + i,
                    readingInterval+CloudSim.getMinTimeBetweenEvents()*2
            );
        }

        //create Sensors
        for (int s=1; s<=3; s++) {
            SensorNode tempSensor = new SensorNode(
                    "TempSensor"+s,
                    new Location(400+s*100, 400+s*100, 0),
                    IoTNodeType.SENSOR,
                    new NetConnection("conn_shortRadio"+s, new NetConnectionType(), 100.00),
                    new IoTNodePower(IoTNodePowerType.BATTERY, false, true, false, 100, 0.1, 10.00),
                    "RelayNode",
                    SensorType.AIR_Temperature_SENSOR,
                    readingInterval,
                    datasetsFolder+"ukcp09_mean-temperature_360month.csv");
        }
        for (int s=1; s<=3; s++) {
            SensorNode percipSensor = new SensorNode(
                    "PercipSensor"+s,
                    new Location(400+s*100, 400+s*100, 0),
                    IoTNodeType.SENSOR,
                    new NetConnection("conn_shortRadio"+s, new NetConnectionType(), 100.00),
                    new IoTNodePower(IoTNodePowerType.BATTERY, false, true, false, 100, 0.1, 10.00),
                    "RelayNode",
                    SensorType.WATER_SurfaceFlow_SENSOR,
                    readingInterval,
                    datasetsFolder+"ukcp09_rainfall_360month.csv");
        }
    }
}
