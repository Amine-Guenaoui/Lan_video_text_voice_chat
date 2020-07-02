/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lan_chat_client;

import java.io.Serializable;

/**
 *
 * @author Guenaoui Amine MIV 1 USTHB
 */
public class Video_Audio implements Serializable{
    
    private int id;
    private User user; 
    private byte[] image_bytes;
    private byte[] audio_bytes;
    private String name;

    public Video_Audio( User user, byte[] image_bytes, byte[] audio_bytes) {
        this.id = user.getId();
        this.user = user;
        this.image_bytes = image_bytes;
        this.audio_bytes = audio_bytes;
        this.name = user.getName()+" video_audio";
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public byte[] getImage_bytes() {
        return image_bytes;
    }

    public String getName() {
        return name;
    }

    public byte[] getAudio_bytes() {
        return audio_bytes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setImage_bytes(byte[] image_bytes) {
        this.image_bytes = image_bytes;
    }

    public void setAudio_bytes(byte[] audio_bytes) {
        this.audio_bytes = audio_bytes;
    }
    
    
}
