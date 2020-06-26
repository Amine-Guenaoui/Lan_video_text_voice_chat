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
public class User implements Serializable {
    public int id;
    public String name;
    //public String messages[]; // probably
    
    public User(int id,String name){
        this.id = id;
        this.name = name;
       
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name +" "+id;
    }
    
}
