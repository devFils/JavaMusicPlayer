package Client;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.*;
import java.util.Scanner;

public class ClientMain {
     
    public static void main(String[] args) throws IOException {
        Scanner s=new Scanner(System.in);
        String filename="/Users/Filippos/Documents/Folder/Accounts/accounts.txt";
    	String client_Tx;
    	String hostIP = "127.0.0.1";
    	int portNumber  =5000;   
    	int newServerPort;
    	String newServerIP;
       
        System.out.println("[ClientMain] I am trying to connect on load balancer with IP:" + hostIP + " on port:" + portNumber );
        //Getting input and output data 
        try (
            Socket echoSocket = new Socket(hostIP, portNumber);
            PrintWriter out_Tx = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in_Rx = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));  
            ) 
        {                       
                Path path = Paths.get(filename);
                //InputStream input=Files.newInputStream(path);
                BufferedReader stdIn =new BufferedReader(new InputStreamReader(System.in));
                while(true){
                    System.out.println("1.Create user account");
                    System.out.println("2.Login to user account");
                    System.out.println("Enter choice[1-2]:");
                    String choice=s.nextLine();
                    if (choice.equals("1")){
                       try{                            
                            OutputStream output= new BufferedOutputStream(Files.newOutputStream(path, APPEND));
                            BufferedWriter writer= new BufferedWriter(new OutputStreamWriter(output));
                            System.out.println("\nCREATE ACCOUNT \n ");
                            System.out.println("Enter username:");
                            String username=s.nextLine();
                            System.out.println("Enter password:");
                            String password=s.nextLine();

                            writer.write(username + "," + password);
                            writer.newLine();
                            System.out.println("Account has been succesfully created!");
                            writer.close();
                            output.close();
                            System.out.println("Press any key to continue...");
                            String proc=s.nextLine();
                            
                        }
                        catch(Exception ex){
                                System.out.println(ex.getMessage());
                        }
                    } 
                    else if (choice.equals("2")){
                       break;
                    } 
                    else{           
                        System.out.println("Invalid choice!");
                        System.out.println("Press any key to continue...");
                        String proc=s.nextLine();
                    }
                }  
                System.out.println("\nLOGIN \n ");
                System.out.println("Enter username:");
                String client_Tx_Usrname = stdIn.readLine();
                System.out.println("Enter password:");
                String client_Tx_PWD = stdIn.readLine();
                client_Tx = client_Tx_Usrname + " " + client_Tx_PWD;
                
                System.out.println("[EchoClient] Sends to server:" + client_Tx);
                
                while (client_Tx != null) {
                    out_Tx.println(client_Tx);
                    
                    String[] splited = in_Rx.readLine().split("\\s+"); //s+ space
                    newServerIP = splited[0];
                    newServerPort = Integer.parseInt(splited[1]);
                    System.out.println("[EchoClient] echo Server IP/Port: " + newServerIP + " " + newServerPort);
                    out_Tx.println("quit");
                    System.out.println("[EchoClient] I have disconnected from LoadBalancer");
                    
                    ClientConnectionToServer echoClient = 
                    		new ClientConnectionToServer(newServerIP, newServerPort);
                    break;   
                }
                echoSocket.close();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostIP);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostIP);
            System.exit(1);
        } 
    }
}

