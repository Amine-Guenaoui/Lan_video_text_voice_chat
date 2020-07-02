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
public class Chat_Private_Messages_Boxes extends UnicastRemoteObject implements Chat_Private_Messages_Boxes_Int {

    private LinkedList<P_Box> boxes_list;

    public LinkedList<P_Box> getBoxes_list() {
        return boxes_list;
    }
    private int box_num;
    private boolean modif;
    public Chat_Private_Messages_Boxes() throws RemoteException{
        super();
        boxes_list = new LinkedList<P_Box>();
        box_num = 0;
        modif = false;
    }
    @Override
    public void createPrivateBox(LinkedList<User> users , String name) throws RemoteException {
        P_Box box = new P_Box(box_num, name, users);
        boxes_list.add(box);
        System.out.println("the box "+box.getName()+" has been created");
        box_num++;
        modif = true;
    }

    @Override
    public LinkedList<P_Box> verifyExistence(User user,LinkedList<P_Box> already_joined_boxes) throws RemoteException {
        
        LinkedList<P_Box> box_temp_list = new LinkedList<P_Box>();
        
        for (P_Box temp: boxes_list){
            if (temp.verify_user_exitence(user)){
                if (!already_joined_boxes.isEmpty()){
                    for (P_Box temp2: already_joined_boxes){

                        if(temp.getId() == temp2.getId()){
                            System.out.println(user.getName()+" already joined box :" +temp2.getName());
                            break;
                        }else{
                            box_temp_list.add(temp);
                            System.out.println(user.getName()+" has joined "+temp.getName()+" box");
                        }

                    }
                }
                else {
                    box_temp_list.add(temp);
                    System.out.println(user.getName()+" has joined "+temp.getName()+" box");
                }
                
            }
        }
        return box_temp_list;
    }

    @Override
    public void removePrivateBox(P_Box box) throws RemoteException {
        for (P_Box temp: boxes_list){
            if (temp.getId() == box.getId()){
                // remove 
                boxes_list.remove(temp);
                System.out.println("the box "+temp.getName()+" has been removed");
                box_num--;
            }
        }
        
    }

    @Override
    public boolean isModif() throws RemoteException {
        return modif;
    }

    @Override
    public void set_modif() throws RemoteException {
        modif = false;
    }
    
    @Override
    public void update_box(P_Box box){
        for(P_Box temp : boxes_list){
            if(temp.getId() == box.getId()){
                boxes_list.remove(temp);
                boxes_list.add(box);
                System.out.println("the box "+temp.getName()+" has been updated");
                
            }
            if(temp.getConcerned_users().size()<2){
                boxes_list.remove(temp);
                System.out.println("the box "+temp.getName()+" has been removed num_users="+temp.getConcerned_users().size());
            }
        }
        
    }

    @Override
    public P_Box get_p_box(P_Box box) throws RemoteException {
        for(P_Box temp : boxes_list){
            if(temp.getId() == box.getId()){
                return temp;
            }
        }
        return null;
    }
    
}
