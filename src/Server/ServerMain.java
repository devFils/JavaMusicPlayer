package Server;

import java.net.*;
import java.io.*;

public class ServerMain {
    public static void main(String[] args) throws IOException {

        int portNumber; //Server's listening port
        boolean listening = true;
        //working port for the servers to work with the load balancer[5001,5002,5003]
        System.out.println("Input the port for the server: ");
        
        BufferedReader stdIn =new BufferedReader(new InputStreamReader(System.in));
        portNumber = Integer.parseInt(stdIn.readLine());     
        
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
            while (listening) {
            	System.out.println("[Echo Server] Listening on :"+ serverSocket.toString());
                //creating new thread
            	Thread t = new Thread(new ServerFuncionality(serverSocket.accept()));
                //thread start
            	t.start();

                System.out.println("[Echo Server] This client will be served from: "+ t.getName());  	        
                }   
	    }
        catch (IOException e) {
            System.err.println("[Echo Server] Could not listen on port " + portNumber);
            System.exit(-1);
        } 
    }
}
