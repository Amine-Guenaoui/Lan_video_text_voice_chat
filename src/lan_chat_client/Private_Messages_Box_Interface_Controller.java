/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lan_chat_client;

import java.io.File;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.Remote;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    P_Box self_box;
    User me;
    Remote chat_boxes;
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
        self_box = Lan_Chat_Main_Interface_Controller.my_not_opened_boxes.remove();
        p_box_title_text.setText(p_box_title_text.getText()+" "+self_box.getName());
        for (User user :self_box.getConcerned_users()){
                        private_box_users_list_view.getItems().add(user);
        }
        
        
        // getting private chat boxes if the user is concerned 
        try {
            chat_boxes = Naming.lookup("Chat_Private_Messages_Boxes");
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
                            if(self_box.isModif()){
                                private_box_text_area.setText("");
                                private_box_text_area.setText(self_box.get_messages());
                                private_box_users_list_view.getItems().clear();
                                for (User user :self_box.getConcerned_users()){
                                    private_box_users_list_view.getItems().add(user);
                                System.out.println("P_Box '"+self_box.getName()+"' has been updated");
                            }
                                //box_class.update_box(self_box);
                                
                            }
                            self_box.set_modif();
                            System.out.println("mesages "+self_box.get_messages());
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
        self_box.remove_user(me);
        self_box.add_message(" has disconnected from "+self_box.getName(), me);
        Stage stage = (Stage) private_box_text_field.getScene().getWindow();
        stage.close();
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
        File selected_file =  fileChooser.showOpenDialog((Stage)private_box_text_field.getScene().getWindow());
        choosen_file_path.setText(selected_file.getPath());
        
    }
    
}
