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
public class Chat_Logger extends  UnicastRemoteObject implements Chat_Logger_Int {
    
    private LinkedList<User> Users;
    private int num_users;
    private boolean modifying;
    
    
    public Chat_Logger() throws RemoteException{
        super();
        Users = new LinkedList<User>();
        num_users = 0;
        modifying = false;
    }
    
    @Override
    public void add_user(String name) throws RemoteException {
        User x = new User(num_users , name);
        Users.add(x);
        num_users++;
        
        System.out.println(x.getName()+" has logged in");
        modifying = true;
    }

    @Override
    public void remove_user(int id) throws RemoteException {
        for(User user : Users) {
            if(user.getId()== id) {
               Users.remove(user);
               System.out.println(user.getName()+" has logged off");
               break;
            }
         } 
        num_users--;
        modifying = true;
    }

    @Override
    public int get_logged_users_number() throws RemoteException {
        return num_users;
    }

    @Override
    public String[] get_logged_users() throws RemoteException {
        String [] list = new String[num_users];
        int k=0;
        for(User user : Users) {
            list[k] = user.getName();
            k++;
         } 
        return list;
    }

    @Override
    public LinkedList<User> get_logged_users_list() throws RemoteException {
        return Users;
    }

    @Override
    public boolean stat_modified() throws RemoteException {
        return modifying;
    }

    @Override
    public void set_stat_done() throws RemoteException {
        modifying = false;
    }
    
    
    
}
