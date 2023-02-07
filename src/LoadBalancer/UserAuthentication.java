package LoadBalancer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class UserAuthentication {


    public Boolean checkUsrPwd(String usrnm, String pwd){ 
        Scanner s=new Scanner(System.in);
        //txt filepath for accounts.txt file that stores all acounts
        String filename="/Users/Filippos/Documents/Folder/Accounts/accounts.txt";
        Boolean isUsrValid = false;
        try{
            Path path = Paths.get(filename);
            InputStream input=Files.newInputStream(path);
            BufferedReader reader= new BufferedReader(new InputStreamReader(input));
            String _temp;
            String _user;
            String _pass;
            boolean found= false;
            //searching for username and password in accounts.txt file
            while((_temp=reader.readLine()) !=null){
                String[] account=_temp.split(",");
                _user=account[0];
                _pass=account[1];
                if (_user.equals(usrnm)&&_pass.equals(pwd)){
                    found=true;
                }
            }
            //account exists so returning true for the authentication
            if (found==true){
                System.out.println("Access granted!");
                isUsrValid=true;
            } 
            //account is not in the txt file so returing false for the authentication
            else{
                System.out.println("Access denied! Invalid username or password!");
                isUsrValid=false;
                reader.close();
            }
        }
        catch(Exception ex){
                System.out.println(ex.getMessage());
        }
        return isUsrValid;
    }
}