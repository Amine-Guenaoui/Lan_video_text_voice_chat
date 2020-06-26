/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lan_chat_client;

import java.io.Serializable;
import java.util.LinkedList;
/**
 *
 * @author Guenaoui Amine MIV 1 USTHB
 */
public class P_Box implements Serializable{
    //private box 
    private int id;
    private String name;
    private String file_name;
    public String getName() {
        return name;
    }
    private LinkedList<User> concerned_users;
    private LinkedList<String> concerned_messages;
    private boolean modif;

   
    public P_Box (int id ,String name,LinkedList<User> users ){
        this.id = id;
        concerned_users = (LinkedList<User>) users.clone(); 
        concerned_messages = new LinkedList<String>();
        this.name = name;
        modif = false;
        file_name = "";
    }
    
    public void add_message(String msg ,User user){
        String text = user.getName() + ": "+msg;
        concerned_messages.add(text);
        modif = true;
    }
    
    public String get_messages(){
        String messages = "";
        for (String text : concerned_messages){
            messages+=text+"\n";
        }
        modif = true;
        return messages;
    }

    public LinkedList<User> getConcerned_users() { // to show them on the side of the window
        return concerned_users;
    }
    
    public void set_modif(){
        modif = false;
    }
    
    public boolean isModif() {
        return modif;
    }
    public boolean verify_user_exitence(User user){
        for (User temp : concerned_users){
            if (temp.getId() == user.getId()) return true;
        }
    return false;
    }

    public void remove_user(User user){
        for (User temp : concerned_users){
            if (temp.getId() == user.getId()){
                concerned_users.remove(user);
                System.out.println(user.getName()+" has been removed from "+this.name);
            }
        }
    }
    
    public void set_shared_file_name(String name){
        file_name = name;
    }
    
    public String get_shared_file_name(){
        return file_name;
    }
    
    public int getId() {
        return id;
    }
    
}
