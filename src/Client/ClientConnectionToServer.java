package Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class ClientConnectionToServer {
    public static DataOutputStream dataOutputStream = null;
    public static DataInputStream dataInputStream = null;
    static String filePath;
    public ClientConnectionToServer(String ipFS, int portFS){
        //Server's hostname
        String hostName = ipFS; 
        //Server's listening port
        int portNumber = portFS; 
        try{
            //creates a new Socket and names it echoSocket
            Socket clientSocket = new Socket(hostName, portNumber);
                               
            System.out.println("[Client] Client connected to Socket: " + clientSocket.toString());

            PrintWriter out =new PrintWriter(clientSocket.getOutputStream(), true);
            //BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            BufferedReader stdIn =new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            // to store current position
            Long currentFrame =999999999L;
            Clip clip;
            String status;

            // current status of clip
            
            Scanner sc= new Scanner(System.in);
            AudioInputStream audioInputStream;
            
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            System.out.println("\n1.For available songs.");
            System.out.println("2.For quit.");
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                if (userInput.equals("1")){
                    try{
                        System.out.println("\nWELCOME!");
                        System.out.println("\nChoose song to download:\n");
                        String[] pathnames;
                        // Creates a new File instance by converting the given pathname string
                        // into an abstract pathname
                        File f = new File("/Users/Filippos/Documents/Folder/Songs");
                        // Populates the array with names of files and directories
                        pathnames = f.list();
                        int i=0;
                        List<String> list=new ArrayList<>();
                        // For each pathname in the pathnames array
                        for (String pathname : pathnames) {
                            i+=1;
                            list.add(f.getAbsolutePath()+"/"+pathname);
                            // Print the names of files and directories
                            System.out.println(i+"."+pathname);

                        }
                        int num = sc.nextInt();
                        
                        filePath = (list.get(num-1));
                        out.println(filePath);
                        receiveFile("downloaded.wav");
                        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        
                        // create clip reference
                        clip = AudioSystem.getClip();

                        // open audioInputStream to the clip
                        clip.open(audioInputStream);
                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                        clip.start();
          
                        status = "play";

                        while (true)
                        { 
                            //clip.start();
                            System.out.println("\nMENU:\n");
                            System.out.println("1.Resume");
                            System.out.println("2.Pause");
                            System.out.println("3.Restart");
                            System.out.println("4.Stop");
                            int c = sc.nextInt();        
                            if (c == 1){
                                //start the clip
                                if (status.equals("play")) 
                                {
                                    System.out.println("Audio is already "+
                                    "being played");
                                    continue;
                                }
                                System.out.println("\nSong resumed.");
                                clip.close();
                                audioInputStream = AudioSystem.getAudioInputStream(
                                new File(filePath).getAbsoluteFile());
                                clip.open(audioInputStream);
                                clip.loop(Clip.LOOP_CONTINUOUSLY);
                                
                                //currentFrame = clip.getMicrosecondPosition();
                                clip.setMicrosecondPosition(currentFrame);  
                                status = "play";          
                            }
                            else if (c == 2){
                                if (status.equals("paused")) 
                                {
                                    System.out.println("Audio is already paused");
                                    continue;
                                }
                                System.out.println("\nSong paused.");
                                currentFrame = clip.getMicrosecondPosition();
                                clip.stop();
                                clip.close();
                                status = "paused";
                                }
                            else if (c == 3){
                                System.out.println("\nSong restarted.");
                                clip.stop();
                                clip.close();
                                audioInputStream = AudioSystem.getAudioInputStream(
                                new File(filePath).getAbsoluteFile());
                                clip.open(audioInputStream);
                                clip.loop(Clip.LOOP_CONTINUOUSLY);
                                currentFrame = 0L;
                                clip.setMicrosecondPosition(0);
                                clip.start();
                                status = "play";
                            }
                            else if (c == 4){
                                System.out.println("\nSong stopped.");
                                currentFrame = 0L;
                                clip.stop();
                                clip.close();
                                break;
                            } 
                        }
                    System.out.println("\n1.For available songs.");
                    System.out.println("2.For quit.");
                    out.println(userInput);
                    }catch(Exception e){
                        System.out.println("Error: " + (e.toString()) );
                    }
                }
                else if (userInput.equals("2")){
                    System.out.println("\nSee you soon!");
                    break;
                }
            }
        clientSocket.close();
        } catch (IOException e) {
            System.out.println("Error: " + (e.toString()) );
        }
        
    }
    private static void receiveFile(String fileName) throws Exception{
    
    int bytes = 0;
    OutputStream fileOutputStream = new FileOutputStream(fileName);
    long size = dataInputStream.readLong(); // read file size
    byte[] buffer = new byte[4 * 1024];
    while (size > 0 && 
        (bytes = dataInputStream.read(buffer, 0,(int)Math.min(buffer.length, size)))!= -1) {
        // Here we write the file using write method
        fileOutputStream.write(buffer, 0, bytes);
        // read upto file size
        size -= bytes; 
    }
    // Here we received the file
    System.out.println("\nFile is downloaded!");
    System.out.println("Enjoy!");
    fileOutputStream.close();
    } 
}
