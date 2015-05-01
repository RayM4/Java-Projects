/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientmessage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author Raymond
 */
public class ClientMessage {
    public static String sendText = "";
    public static JTextArea textArea = new JTextArea(20, 50);
    public static Boolean changed = false;
    
    public static JTextField textBox = new JTextField("Text Box", 40);
    public static PrintStream sout;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        // TODO code application logic here
        JFrame jf = new JFrame("Client");
        jf.setSize(600,400);
        jf.setResizable(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel display = new JPanel();
        JPanel bottom = new JPanel();
        jf.add(display, "North");
        jf.add(bottom, "South");
        
        //JTextArea textArea = new JTextArea(20, 50);
        textArea.setText("Enter Username in Text Box and press send to connect to server");
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setViewportView(textArea);
        display.add(scrollPane);
        
        //JTextField textBox = new JTextField("Text Box", 40);
        textBox.addActionListener(new BoxPress());
        bottom.add(textBox, "Left");
        
        JButton send = new JButton("Send");
        send.addActionListener(new ButtonPress());
        bottom.add(send, "right");
        
        jf.pack();
        jf.setVisible(true);
        
        while (!changed) {
            //if (!(sendText.equals("") || sendText.equals("Text Box"))) {
            System.out.println("");
            if (changed) {
                //System.out.println("p");
                Socket s = new Socket("localhost", 5190);
                new ProcessClient(s).start();
            }
        }
        
    }
    
    
    /*static class ButtonPress implements ActionListener{
        JTextField box;
        ButtonPress(JTextField t) {
            box = t;
        }
        @Override
        public void actionPerformed(ActionEvent ae) {
            String text = box.getText();
            sendText = text;
            changed = true;
        }
    }*/
    
    static class ButtonPress implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!changed) {
                sendText = textBox.getText();
            }
            else {
                sout.println(textBox.getText());
                sout.flush();
                textBox.setText("");
            }
            changed = true;
        }
    }
    
    static class BoxPress implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae) {
            JTextField t = (JTextField)ae.getSource();
            if (t.getText().equals("Text Box")) {
                t.setText("");
            }
        }
    }
    
    public static void sendTextSer(PrintStream sout) {
        sout.println(sendText);
        sout.println("89545463");
        sout.println("problems");
        sendText = "";
        changed = false;
        sout.flush();
    }
}

class ProcessClient extends Thread{
    Socket s;
    ProcessClient(Socket snew){s = snew;}
    
    public void run(){
        try {
            System.out.println(ClientMessage.sendText);
            Scanner sin = new Scanner(s.getInputStream());
            ClientMessage.sout = new PrintStream(s.getOutputStream());
            //ClientMessage.sendTextSer(ClientMessage.sout);
            ClientMessage.sout.println(ClientMessage.sendText);
            ClientMessage.sendText = "";
            ClientMessage.sout.flush();

            String input = sin.nextLine();
            //System.out.println(input);
            
            while (true) {
                //if (!ClientMessage.sendText.equals("")) {
                /*if (ClientMessage.changed) {
                    ClientMessage.sendTextSer(ClientMessage.sout);
                }*/
                
                input = sin.nextLine();
                ClientMessage.textArea.setText(ClientMessage.textArea.getText() + "\n" + input);
                
                //System.out.println(input);
                
            }
            
        } catch(IOException e) {}
    }
}
