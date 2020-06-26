/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lan_chat_client;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guenaoui Amine MIV 1 USTHB
 */
public class Chat_File_Sharer extends UnicastRemoteObject implements Chat_File_Sharer_Int {
 
    DataInputStream dis;
    String content;
    byte[] datainBytes;
    public Chat_File_Sharer() throws RemoteException{
        super();
        content = "";
    }

    @Override
    public void upload_file(File file) throws RemoteException {
        try {
            dis =new DataInputStream (new FileInputStream (file.getPath()));
            datainBytes = new byte[dis.available()];
            dis.readFully(datainBytes);
            dis.close();
            content = new String(datainBytes, 0, datainBytes.length);
            System.out.println(content);
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }

    @Override
    public String download_file() throws RemoteException {
        return content;
    }
}
