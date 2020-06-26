/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lan_chat_client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

/**
 *
 * @author Guenaoui Amine MIV 1 USTHB
 */
public class Chat_Messages_Handler extends UnicastRemoteObject implements Chat_Messages_Handler_Int {

    private LinkedList<String> public_messages; 
    private boolean modif;
    
    public Chat_Messages_Handler() throws RemoteException  {
        super();
        public_messages = new LinkedList<String>();
        modif = false;
    }
    
    @Override
    public String get_public_messages(User u_reciever) throws RemoteException {
        String messages = "";
        for (String text : public_messages){
            messages+=text+"\n";
        }
        modif = true;
        return messages;
        
    }

    @Override
    public void add_public_message(String message, User u_sender) throws RemoteException {
        String text = u_sender.getName()+" : "+ message;
        public_messages.add(text);
        modif = true;
    }

    @Override
    public void set_modif() throws RemoteException {
        modif = false;
    }

    @Override
    public boolean get_stat() throws RemoteException {
        return modif;
    }

   
    
}
