package LoadBalancer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class LoadBalancerMain {

    public static void main(String[] args) throws NumberFormatException, IOException {
		
	//Insert listening port or leave default port 5000
	int portNumber = 5000;   
   
        System.out.println("[LoadBalancerMain] LoadBalancer's IP/port:"+ InetAddress.getLocalHost().getHostAddress()+ ", port:" + portNumber ); 

        boolean listening = true;
        
        //listening serverSocket->accept connection to client->new client socket
        try (ServerSocket serverSocket = new ServerSocket(portNumber)){ 
            while (listening) {
            	System.out.println("[LoadBalancerMain] I'm Listening on :" + serverSocket.toString());
            	Thread t = new Thread(new LoadBalancerFunctionality(serverSocket.accept()));
                //load balancer shows the id of the thread that has been created and then starts it
            	System.out.println("Tread Id: " + t.getName());
            	t.start();
            }   
        }
        //If problem occures with listening port
        catch (IOException e) {
            System.err.println("[LoadBalancerMain] Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }	
}
