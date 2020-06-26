/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lan_chat_client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Guenaoui Amine MIV 1 USTHB
 */
public interface Chat_Messages_Handler_Int extends Remote {
    
    public String get_public_messages(User u_reciever) throws RemoteException;
    public void add_public_message(String message,User u_sender) throws RemoteException;
    public void set_modif()throws RemoteException;
    public boolean get_stat() throws RemoteException;
}
