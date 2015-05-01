/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverlistener;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author Raymond
 */
public class ServerListener {
    public static ArrayList<PrintStream> outputs = new ArrayList();
    public static ArrayList<String> users = new ArrayList();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        // TODO code application logic here
        
        ServerSocket ss = new ServerSocket(5190);
        Socket s;
        while(true){
            s=ss.accept();
            new ProcessClient(s).start();
        }
    }
    
}

class ProcessClient extends Thread{
    Socket s;
    String username;
    ProcessClient(Socket snew){s = snew;}
    
    public void run(){
        try{
            Scanner sin = new Scanner(s.getInputStream());
            String input = sin.nextLine();
            
            PrintStream sout = new PrintStream(s.getOutputStream());
            
            //tell everyone someone has connected
            /*for (int i = 0; i < ServerListener.outputs.size(); i++) {
                    PrintStream out = ServerListener.outputs.get(i);
                    out.print(input + "has connected" );
                }
            */
            
            if (!ServerListener.outputs.contains(sout)) {
                ServerListener.outputs.add(sout);
                ServerListener.users.add(input);
                username = input;
            }
            //System.out.println(ServerListener.outputs.size());
            //System.out.println(input);
            sout.println("Welcome " + username);
            sout.flush();

            while (!input.equalsIgnoreCase("EXIT")){
                //sout.println(ServerListener.users.get(0) + ": " + input);
                //System.out.println(ServerListener.outputs.size());

                input = sin.nextLine();

                //System.out.println(input);
                for (int i = 0; i < ServerListener.outputs.size(); i++) {
                    PrintStream out = ServerListener.outputs.get(i);
                    out.println(username + ": " + input);
                    out.flush();
                }
            }
        }
        catch (IOException e){}
    }

}