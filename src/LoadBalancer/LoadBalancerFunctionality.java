package LoadBalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LoadBalancerFunctionality implements Runnable{
	
    Socket clientSocket;
    LoadBalancerFunctionality(Socket csocket) throws IOException {
	this.clientSocket = csocket;   
	System.out.println("[LoadBalancerFunctionality] Client socket" + this.clientSocket.toString());
    }

    @Override
    public void run() {	
    try (    	
        //variable to send data out
        PrintWriter out_Tx =new PrintWriter(clientSocket.getOutputStream(), true);
        //variable to receive the data in
        BufferedReader in_Rx = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) 
        {
            //Initiate Load Balancer to return port 
            LoadBalancing_IP_Port load_balancer_Port = new LoadBalancing_IP_Port();
            int newServer_Port = load_balancer_Port.chooseRandomServerPort();
            //Initiate Load Balancer to return ip
            LoadBalancing_IP_Port load_balancer_IP = new LoadBalancing_IP_Port();
            String newServer_iP = load_balancer_IP.chooseRandomServerIP();
	    UserAuthentication testUsrAuth = new UserAuthentication();
            String clientResponse;
            String usrnme = null;
            String pwd = null;
              
            Boolean result;
              
            //AUTHENTICATION START     
            //Send new Server's port to client
            clientResponse = in_Rx.readLine();
            try {
                System.out.println(clientResponse);
                String[] x = clientResponse.split("\\s+");
                usrnme = x[0];
                pwd = x[1];              
            }
            catch (Exception e){
                System.out.println("Error!");
            }

            while (true) {     
                                
                result = testUsrAuth.checkUsrPwd(usrnme, pwd);
                System.out.println("[LoadBalancerFunctionality] Result> " + result);
 
                // Authentication END 
                if (result) {
                    out_Tx.println(newServer_iP + " " + newServer_Port);              
                    System.out.println("[LoadBalancerFunctionality] Send " + newServer_iP + " " + newServer_Port);
                    break;
                } 
                else {
                    clientSocket.close(); 
                    System.out.println("Authentication Failed!");          
                } 
                if (in_Rx.readLine().equals("2")){
                    out_Tx.println("2");       
                    break;
                }    
            }	
            clientSocket.close();  
            System.out.println("[LoadBalancerFunctionality] Client sent 'quit'");
    
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port or listening for a connection. \n "
            + "!To see open ports in linux: $ netstat -ltnp and $kill -9 pid ");
            System.out.println(e.getMessage());
        }
    }
}


