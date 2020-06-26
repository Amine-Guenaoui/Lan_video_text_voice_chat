/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lan_chat_client;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Guenaoui Amine MIV 1 USTHB
 */
public interface Chat_File_Sharer_Int extends Remote{
    
    public void upload_file (File file)throws RemoteException;
    public String download_file () throws RemoteException;
    
}
