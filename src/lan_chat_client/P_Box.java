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
    private byte[] file_content;
    private LinkedList<Video_Audio> videos;
    private int[] gotten_vids; //it is used to tell if a user is sharing his video or not (values are 0 or 2)
    private LinkedList<User> concerned_users;
    private LinkedList<String> concerned_messages;
    private boolean modif;

   
    public P_Box (int id ,String name,LinkedList<User> users ){
        this.id = id;
        concerned_users = (LinkedList<User>) users.clone(); 
        gotten_vids = new int[concerned_users.size()];
        concerned_messages = new LinkedList<String>();
        videos = new LinkedList<Video_Audio>();
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
    public int num_concerned_users(){
        return concerned_users.size();
    }
    
    
    public void upload_file(byte[] content,String  filename) {
        this.file_content = content;
        this.file_name = filename;
    }

    
    public byte[] download_file() {
        
        return file_content;
    }
    
    public String getName() {
        return name;
    }
    
    public void open_video(Video_Audio video){
        System.out.println(" a new video "+video.getName()+" is being shared");
        videos.add(video);
        
    }
    
    public void update_video(Video_Audio video){
        for(Video_Audio temp : videos){
            if(video.getId() == temp.getId()){
                videos.remove(temp);
                videos.add(video);
                System.out.println("the video "+temp.getName()+" has been updated");
                
            }
        }
    }
    public void remove_video(Video_Audio video){
        for(Video_Audio temp : videos){
            if(video.getId() == temp.getId()){
                videos.remove(temp);
                System.out.println("the video "+temp.getName()+" has been removed");
                
            }
        }
    }
    
    
    public LinkedList<Video_Audio> get_videos(){
        return videos;
    }
}
