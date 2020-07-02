/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lan_chat_client;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.Naming;
import java.rmi.Remote;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Guenaoui Amine MIV 1 USTHB
 */
public class Private_Messages_Box_Interface_Controller implements Initializable {

    public static P_Box self_box;
    User me;
    Remote chat_boxes;
    Remote chat_file;
    String upload_path,download_path;
    File selected_file;
    @FXML
    private TextArea private_box_text_area;
    @FXML
    private Text p_box_title_text;
    @FXML
    private ListView<User> private_box_users_list_view;
    @FXML
    private TextField private_box_text_field;
    @FXML
    private TextField choosen_file_path;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        me = FXML_Login_Client_Controller.me;
        self_box = Lan_Chat_Main_Interface_Controller.my_not_opened_boxes.remove();// removing from not opened boxes
        //Lan_Chat_Main_Interface_Controller.my_not_opened_boxes.add(self_box); // adding to already opened boxes
        p_box_title_text.setText(p_box_title_text.getText()+" "+self_box.getName());
        String location = "rmi://192.168.1.4:1099/"; 
        for (User user :self_box.getConcerned_users()){
                        private_box_users_list_view.getItems().add(user);
        }
        
        download_path = "downloads/";
        // getting private chat boxes if the user is concerned 
        try {
            chat_boxes = Naming.lookup(location+"Chat_Private_Messages_Boxes");
            //stub = Naming.lookup("rmi://"+InetAddress.getLocalHost().getHostAddress()+"/Chat_Messages_Handler");
            System.out.println(chat_boxes);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        
        AnimationTimer task = new AnimationTimer(){
            
            public void handle(long now) {
                 if (chat_boxes instanceof Chat_Private_Messages_Boxes_Int ){
                        Chat_Private_Messages_Boxes_Int box_class = (Chat_Private_Messages_Boxes_Int)chat_boxes;
                        try {
                            self_box = box_class.get_p_box(self_box);
                            if(self_box.getConcerned_users().size() < 2){
                                box_class.removePrivateBox(self_box);
                                Stage current = (Stage)private_box_users_list_view.getScene().getWindow();
                                current.close();
                            }
                            if(self_box.isModif()){
                                private_box_text_area.setText("");
                                private_box_text_area.setText(self_box.get_messages());
                                private_box_users_list_view.getItems().clear();
                                for (User user :self_box.getConcerned_users()){
                                    private_box_users_list_view.getItems().add(user);
                                //System.out.println("P_Box '"+self_box.getName()+"' has been updated");
                                }
                                if(!"".equals(self_box.get_shared_file_name()) ){
                                    choosen_file_path.setText("a file has been upload press download to download it ");
                                }
                                //box_class.update_box(self_box);
                                
                            }
                            self_box.set_modif();
                            //System.out.println("mesages "+self_box.get_messages());
                            } catch (Exception ex) {
                                    System.out.println("Error !");
                                    ex.printStackTrace();
                                    System.exit(1);
                             }
                     
                } 
            }
        };
        task.start();
        System.out.println("P_Box id = "+self_box.getId());
    }    

    @FXML
    private void log_off_from_p_box(ActionEvent event) {
        
        if (chat_boxes instanceof Chat_Private_Messages_Boxes_Int ){
            Chat_Private_Messages_Boxes_Int box_class = (Chat_Private_Messages_Boxes_Int)chat_boxes;
            try {
                self_box = box_class.get_p_box(self_box);
                self_box.remove_user(me);
                self_box.add_message(" has disconnected from "+self_box.getName(), me);
                box_class.update_box(self_box);
                Stage stage = (Stage) private_box_text_field.getScene().getWindow();
                stage.close();
                private_box_text_field.setText("");
            } catch (Exception ex) {
                        System.out.println("in sending message !");
                        ex.printStackTrace();
                        System.exit(1);
            }
        
        }
    }

    @FXML
    private void send_message(ActionEvent event) {
        
        String text = private_box_text_field.getText();
        if (chat_boxes instanceof Chat_Private_Messages_Boxes_Int ){
            Chat_Private_Messages_Boxes_Int box_class = (Chat_Private_Messages_Boxes_Int)chat_boxes;
            try {
                self_box = box_class.get_p_box(self_box);
                self_box.add_message(text, me);
                box_class.update_box(self_box);
                private_box_text_field.setText("");
            } catch (Exception ex) {
                        System.out.println("in sending message !");
                        ex.printStackTrace();
                        System.exit(1);
            }
        
        }
    }

    @FXML
    private void browse_file(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("get path of file you want to share");
        selected_file =  fileChooser.showOpenDialog((Stage)private_box_text_field.getScene().getWindow());
        choosen_file_path.setText(selected_file.getPath());
        if (chat_boxes instanceof Chat_Private_Messages_Boxes_Int ){
            Chat_Private_Messages_Boxes_Int box_class = (Chat_Private_Messages_Boxes_Int)chat_boxes;
            try {
                self_box = box_class.get_p_box(self_box);
                self_box.set_shared_file_name(selected_file.getName());
                box_class.update_box(self_box);
                upload_path = selected_file.getPath();
                
                
            } catch (Exception ex) {
                        System.out.println("in file name can't be set !");
                        ex.printStackTrace();
                        System.exit(1);
            }
        } 
        
        
    }

    @FXML
    private void download_attached_file(ActionEvent event) {
    
        String file_path=download_path;
        //getting file name 
        if (chat_boxes instanceof Chat_Private_Messages_Boxes_Int ){
            Chat_Private_Messages_Boxes_Int box_class = (Chat_Private_Messages_Boxes_Int)chat_boxes;
            try {
                self_box = box_class.get_p_box(self_box);
                
                file_path += self_box.get_shared_file_name();
                System.out.println("download path :"+ file_path);
                byte[] file_content= self_box.download_file();
                Files.write( Paths.get(file_path) ,//StandardCharsets.UTF_8
                       file_content,
                       StandardOpenOption.CREATE);
            } catch (Exception ex) {
                        System.out.println("in file name can't be set !");
                        ex.printStackTrace();
                        System.exit(1);
            }
        }
        
        
    }

    @FXML
    private void upload_attached_file(ActionEvent event) {
    
        
     
      if (chat_boxes instanceof Chat_Private_Messages_Boxes_Int ){
            Chat_Private_Messages_Boxes_Int box_class = (Chat_Private_Messages_Boxes_Int)chat_boxes;
            try {
                self_box = box_class.get_p_box(self_box);
                byte[] content = Files.readAllBytes(selected_file.toPath());
                self_box.add_message("have uploaded "+selected_file.getName()+",\n "+selected_file.length()+" octets , \npress download to download it  ", me);
                self_box.upload_file(content,selected_file.getName());
                box_class.update_box(self_box);
            } catch (Exception ex) {
                        System.out.println("in file name can't be set !");
                        ex.printStackTrace();
                        System.exit(1);
            }
        }
     
    }

    @FXML
    private void video_call(ActionEvent event) {
    
        System.out.println("requesting a video call from them");
        //rmi video call condition verify if yes open a new window else do nothing such thing anywayt
        create_video_share_window();
    }
    
    public void create_video_share_window(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Private_Video_Sharing_Interface.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
//            stage.initModality(Modality.NONE);
//            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle(me.getName()+"'s Private Video Sharing");
            stage.setScene(new Scene(root1));  
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
