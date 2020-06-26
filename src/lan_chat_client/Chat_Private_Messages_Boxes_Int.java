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
public interface Chat_Private_Messages_Boxes_Int extends Remote{
    
    public void createPrivateBox(LinkedList<User> users , String name)throws RemoteException;
    public LinkedList<P_Box> verifyExistence(User user,LinkedList<P_Box> already_joined_boxes) throws RemoteException; // linked list cause it can be many private chats at once
    public void removePrivateBox(P_Box box) throws RemoteException;
    public boolean isModif() throws RemoteException;
    public void set_modif() throws RemoteException;
    public LinkedList<P_Box> getBoxes_list() throws RemoteException;
    public void update_box(P_Box box)throws RemoteException; // once p_box is set rreturn in back
    public P_Box get_p_box(P_Box box) throws RemoteException;
    
    
    
}
