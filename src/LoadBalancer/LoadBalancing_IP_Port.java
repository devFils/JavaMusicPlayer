package LoadBalancer;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LoadBalancing_IP_Port {
    
    public int chooseRandomServerPort (){

        int randomPort;

        //Choose Random Port for the new server
        List<Integer> portList = Arrays.asList(5001,5002,5003);
        Random rand = new Random();
        randomPort = portList.get(rand.nextInt(portList.size()));

        System.out.println("[LoadBalancing_IP_Port] Use this server/port:" + randomPort);
        //return the new random port
        return randomPort;
    }
    

    public String chooseRandomServerIP (){
    	
        String randomIP;
        //Choose IP for the new server (in this case always 127.0.0.1)
    	List<String> ipList = Arrays.asList("127.0.0.1");	            
        Random rand = new Random();
        randomIP = ipList.get(rand.nextInt(ipList.size()));
        
        System.out.println("[LoadBalancing_IP_Port] Use this server/IP:" + randomIP);
        //return Ip address for the server
        return randomIP;
    }
}
