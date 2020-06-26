/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lan_chat_client;

import java.rmi.Naming;
import java.util.Arrays;

/**
 *
 * @author Guenaoui Amine MIV 1 USTHB
 */
public class Server {
    public static void main (String args[]){
        
        System.out.println("Creating the server ...");
        
        try {
            //login
            Chat_Logger log = new Chat_Logger();
            Naming.rebind("Chat_logger" , log ); 
            System.out.println("Chat Logger has been created ..");
            //Naming.rebind("rmi://"+InetAddress.getLocalHost().getHostAddress()+"/Notes" , log );
            
            //public 
            Chat_Messages_Handler chat_messages = new Chat_Messages_Handler();
            Naming.rebind("Chat_Messages_Handler" , chat_messages ); 
            System.out.println("Chat Messages Handler  has been created ..");
            
            // private 
            Chat_Private_Messages_Boxes chat_boxes = new Chat_Private_Messages_Boxes();
            Naming.rebind("Chat_Private_Messages_Boxes" , chat_boxes );
            System.out.println("Chat Private Messages Boxes has been  created ..");
            
            //files 
            Chat_File_Sharer chat_file = new Chat_File_Sharer();
            Naming.rebind("Chat_File_Sharer" , chat_file );
            System.out.println("Chat File Sharer has been  created ..");
            
            while( log.stat_modified() || chat_boxes.isModif() || chat_messages.get_stat() ){
                System.out.println(Arrays.toString(log.get_logged_users()));
                System.out.println("num of logged users "+ log.get_logged_users_number());
                System.out.println("is modified "+ log.stat_modified());
                for (P_Box box : chat_boxes.getBoxes_list()){
                    System.out.println("box : "+box.getName());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
}
