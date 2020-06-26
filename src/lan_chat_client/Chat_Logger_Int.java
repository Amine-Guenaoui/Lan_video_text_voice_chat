/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lan_chat_client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

/**
 *
 * @author Guenaoui Amine MIV 1 USTHB
 */
public interface Chat_Logger_Int extends Remote{
    
    public void add_user(String name) throws RemoteException;
    public void remove_user(int id) throws RemoteException;
    public int get_logged_users_number() throws RemoteException;
    public String[] get_logged_users() throws RemoteException;
    public LinkedList<User> get_logged_users_list() throws RemoteException;
    public boolean stat_modified()throws RemoteException;
    public void set_stat_done() throws RemoteException;
}
