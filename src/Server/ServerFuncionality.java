package Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;


public class ServerFuncionality implements Runnable {
    Socket clientSocket;
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
	 ServerFuncionality(Socket clientSocket) {
	      this.clientSocket = clientSocket;
	 }
    @Override
	public void run() {
	System.out.println("[tag3_1_EchoServerFuncionalityRunnable] Welcome Client on:" + this.clientSocket.toString());

        try (BufferedReader Rx_in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter Tx_out= new PrintWriter(clientSocket.getOutputStream(), true);){                    
            String inputLine;
            String filePath;
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
	    dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            //reading clients inputLine
            while ((inputLine = Rx_in.readLine()) != null) {
                //if client wants to download a song
                if (inputLine.equals("1")){
                    try{ 
                        while ((filePath = Rx_in.readLine()) != null){
                            sendFile(filePath,this.clientSocket);
                        }
                    }catch (Exception e){
                        System.out.println("Error "+e);
                    }
                }
                //if client wants to quit
                else if (inputLine.equals("2")){
                    Tx_out.println("quit");
                    break;
                }
                
            }
            clientSocket.close(); 
        } 
        catch (IOException ex){
            Logger.getLogger(ServerFuncionality.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    private static void sendFile(String path,Socket soc) throws Exception{ 
                        
        //int selection = Integer.parseInt(path)-1;
        int bytes;
	// Open the File where he located in your pc
	File file = new File(path);
	FileInputStream fileInputStream = new FileInputStream(file);
        dataOutputStream = new DataOutputStream(soc.getOutputStream());
        dataInputStream = new DataInputStream(soc.getInputStream()); 
	// Here we send the File to Server
	dataOutputStream.writeLong(file.length());
	// Here we break file into chunks
        byte[] buffer = new byte[4 * 1024];
	while ((bytes = fileInputStream.read(buffer))!= -1) {
            // Send the file to Server Socket
            dataOutputStream.write(buffer, 0, bytes);
            dataOutputStream.flush();
	}          
    // close the file here
    fileInputStream.close();
    }
}
