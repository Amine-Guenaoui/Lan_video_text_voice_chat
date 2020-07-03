/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lan_chat_client;

import java.net.URL;
import java.rmi.Naming;
import java.rmi.Remote;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static lan_chat_client.FXML_Login_Client_Controller.me;

/**
 * FXML Controller class
 *
 * @author Guenaoui Amine MIV 1 USTHB
 */
public class Lan_Chat_Main_Interface_Controller implements Initializable {

    /**
     * Initializes the controller class.
     */
    Remote logger_stub;
    Remote messages_stub;
    Remote chat_boxes;
    User me;
    public static LinkedList<P_Box> my_boxes;
    public static LinkedList<P_Box> my_opened_boxes;
    public static LinkedList<P_Box> my_not_opened_boxes;
    @FXML
    private TextArea user_chat_box_text_area;
    @FXML
    private ListView<User> logged_isers_list_view;
    @FXML
    private TextField user_message;
    @FXML
    private Text user_name_text;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //setting selection mode to multiple 
        logged_isers_list_view.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        my_boxes = new LinkedList<P_Box>();
        my_not_opened_boxes = new LinkedList<P_Box>();
        me = FXML_Login_Client_Controller.me;
        user_name_text.setText(user_name_text.getText()+" "+me.getName());
        String location = "rmi://192.168.1.2:1099/"; 
        try {
            logger_stub = Naming.lookup(location+"Chat_logger");
            //stub = Naming.lookup("rmi://"+InetAddress.getLocalHost().getHostAddress()+"/Chat_logger");
            System.out.println(logger_stub);
            
        
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        try {
             messages_stub = Naming.lookup(location+"Chat_Messages_Handler");
            //stub = Naming.lookup("rmi://"+InetAddress.getLocalHost().getHostAddress()+"/Chat_Messages_Handler");
            System.out.println(messages_stub);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        // getting private chat boxes if the user is concerned 
        try {
            chat_boxes = Naming.lookup(location+"Chat_Private_Messages_Boxes");
            //stub = Naming.lookup("rmi://"+InetAddress.getLocalHost().getHostAddress()+"/Chat_Messages_Handler");
            System.out.println(chat_boxes);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        
        // timer to refresh stuff
//        Timer timer = new Timer();
        AnimationTimer task = new AnimationTimer(){
            
            public void handle(long now) {
                if (logger_stub instanceof Chat_Logger_Int ){
                    Chat_Logger_Int s = (Chat_Logger_Int)logger_stub;
                    try {
                        if (s.stat_modified() || logged_isers_list_view.getItems().isEmpty() ){
                            System.out.println(Arrays.toString(s.get_logged_users()));
                            LinkedList<User> list = s.get_logged_users_list(); 
                            logged_isers_list_view.getItems().clear();
                            for (User user:list ) {

                                logged_isers_list_view.getItems().add(user);
                                //System.out.println();
                            }
                            s.set_stat_done();
                        }
                    } catch (Exception ex) {
                        System.out.println("Error !");
                        ex.printStackTrace();
                        System.exit(1);
                    }
                } 
                if (messages_stub instanceof Chat_Messages_Handler_Int ){
                    Chat_Messages_Handler_Int s = (Chat_Messages_Handler_Int)messages_stub;
                    try {
                       
                        if (s.get_stat()|| user_chat_box_text_area.getText().isEmpty() ){
                            user_chat_box_text_area.setText("");
                            String messages = s.get_public_messages(me);
                            System.out.println(messages);
                            user_chat_box_text_area.setText(messages);
                            s.set_modif(); // done
                        }
                    } catch (Exception ex) {
                        System.out.println("Error !");
                        ex.printStackTrace();
                        System.exit(1);
                    }
                } 
                 if (chat_boxes instanceof Chat_Private_Messages_Boxes_Int ){
                    Chat_Private_Messages_Boxes_Int box_class = (Chat_Private_Messages_Boxes_Int)chat_boxes;
                    try {
                        LinkedList<P_Box> tmp;
                        if(box_class.isModif()){
                            // retrieve the boxes that you joined to ur base
                            tmp = box_class.verifyExistence(me, my_boxes);
                            //add those boxes that you didn't add to ur base
                            for (P_Box box : tmp){
                                //create a window for box 
                                System.out.println(" joining "+ box.getName() + " box");
                                my_boxes.add(box);
                                my_not_opened_boxes.add(box);
                            }
                            
                       }
                       box_class.set_modif();
                    } catch (Exception ex) {
                        System.out.println("Error !");
                        ex.printStackTrace();
                        System.exit(1);
                    }
                 }
            
                while (!my_not_opened_boxes.isEmpty()){
                    // the new window will take care of emptying the list
                    create_chat_box_window(); 
                }
            }

            
        };
        task.start();
//        timer.scheduleAtFixedRate(task, 100, 100);
    }    

    @FXML
    private void send_message(ActionEvent event) {
        if (messages_stub instanceof Chat_Messages_Handler_Int ){
                    Chat_Messages_Handler_Int s = (Chat_Messages_Handler_Int)messages_stub;
                    try {
                        String text = user_message.getText();
                        s.add_public_message(text, me);
                        
                    } catch (Exception ex) {
                        System.out.println("Error !");
                        ex.printStackTrace();
                        System.exit(1);
                    }
        }
    }

    @FXML
    private void log_off(ActionEvent event) {
        if (logger_stub instanceof Chat_Logger_Int ){
                    Chat_Logger_Int s = (Chat_Logger_Int)logger_stub;
                    try {
                     s.remove_user(me.getId());
                    if (messages_stub instanceof Chat_Messages_Handler_Int ){
                        Chat_Messages_Handler_Int s2 = (Chat_Messages_Handler_Int)messages_stub;
                        try {
                            String text = "has left conversation";
                            s2.add_public_message(text, me);
                            Stage stage = (Stage) user_message.getScene().getWindow();
                            stage.close();
                        } catch (Exception ex) {
                            System.out.println("Error !");
                            ex.printStackTrace();
                            System.exit(1);
                        }
                    }
                    System.exit(0);
                    } catch (Exception ex) {
                        System.out.println("Error !");
                        ex.printStackTrace();
                        System.exit(1);
                    }
        }
    }

    @FXML
    private void create_private_box(ActionEvent event) {
        //selected users from logged in users 
        boolean error=false;
        ObservableList<User> selectedUsers =  logged_isers_list_view.getSelectionModel().getSelectedItems();
        LinkedList<User> concerned_users = new LinkedList<User>();
        concerned_users.add(me); // the creator
        for (User user : selectedUsers){
            //fetching concerned users
            System.out.println(user.getName()+" has been selected");
            if(user.getId() == me.getId()) {
                error=true;
                break;
            }
            concerned_users.add(user);
        }
        if(error){
            Alert a = new Alert(AlertType.ERROR); 
  
                // set content text 
                a.setContentText("You shouldn't select yourself (auto selected)"); 
  
                // show the dialog 
                a.show();
        }else{
        if (chat_boxes instanceof Chat_Private_Messages_Boxes_Int ){
            Chat_Private_Messages_Boxes_Int box_class = (Chat_Private_Messages_Boxes_Int)chat_boxes;
            try {

                if(!box_class.isModif()){
                    box_class.createPrivateBox(concerned_users, me.getName()+" Box");
                }
            } catch (Exception ex) {
                System.out.println("Error !");
                ex.printStackTrace();
                System.exit(1);
            }
        }
        }
    }
    
    public void create_chat_box_window(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Private_Messages_Box_Interface.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
//            stage.initModality(Modality.NONE);
//            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Private Chat Box");
            stage.setScene(new Scene(root1));  
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
