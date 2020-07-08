/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lan_chat_client;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.Naming;
import java.rmi.Remote;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
/**
 * FXML Controller class
 *
 * @author Guenaoui Amine MIV 1 USTHB
 */
public class Private_Video_Sharing_Interface_Controller implements Initializable {

    @FXML
    private ImageView image_view_user_him;
    @FXML
    private ImageView image_view_user_me;
    
    User me;
    P_Box self_box;
    LinkedList<User> users;
    Remote chat_boxes;
    Video_Audio video;
    byte[] imagebytes;
    byte[] audiobytes;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //System.loadLibrary( "C:/Users/amino/Desktop/MIV/opencv/opencv/build/java/x64/opencv_java430.dll" );
        String location = FXML_Login_Client_Controller.location;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat matrix = new Mat();
        me = FXML_Login_Client_Controller.me;
        String file_path = "images/"+me.getName()+"sanpshot.jpg";
        Imgcodecs imageCodecs = new Imgcodecs();
        self_box = Private_Messages_Box_Interface_Controller.self_box;
        
        
        video = new Video_Audio(me, new byte[2], new byte[2]);
        try {
            chat_boxes = Naming.lookup(location+"Chat_Private_Messages_Boxes");
            //stub = Naming.lookup("rmi://"+InetAddress.getLocalHost().getHostAddress()+"/Chat_Messages_Handler");
            System.out.println(chat_boxes);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        if (chat_boxes instanceof Chat_Private_Messages_Boxes_Int ){
                    Chat_Private_Messages_Boxes_Int box_class = (Chat_Private_Messages_Boxes_Int)chat_boxes;
                    try {
                        self_box = box_class.get_p_box(self_box);
                        self_box.open_video(video);
                        
                    }catch(Exception e){
                         e.printStackTrace();
                     System.exit(1);
                    }   
                }
        
        
        
        VideoCapture capture = new VideoCapture();
        capture.open(Videoio.CAP_DSHOW );//camera
        //capture.open("images/video.mp4");//video
        AnimationTimer task = new AnimationTimer(){
            
            public void handle(long now) {
                capture.read(matrix);
                if( capture.isOpened()) {
                    // If there is next video frame
                    if (capture.read(matrix)) {
                       // Creating BuffredImage from the matrix
                       BufferedImage image = new BufferedImage(matrix.width(), 
                          matrix.height(), BufferedImage.TYPE_3BYTE_BGR);

                       WritableRaster raster = image.getRaster();
                       DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
                       byte[] data = dataBuffer.getData();
                       matrix.get(0, 0, data);


                       // Creating the Writable Image
                       WritableImage wimg = SwingFXUtils.toFXImage(image, null);
                       image_view_user_me.setImage(wimg);
                    }
                    //saving img 
                    imageCodecs.imwrite(file_path, matrix);
                }
                
                
                
                
                
                if (chat_boxes instanceof Chat_Private_Messages_Boxes_Int ){
                    Chat_Private_Messages_Boxes_Int box_class = (Chat_Private_Messages_Boxes_Int)chat_boxes;
                    try {
                        if(!box_class.isModif()){
                            File f = new File(file_path);
                            if(f.exists() && !f.isDirectory()) { 
                               imagebytes = Files.readAllBytes(Paths.get(file_path));
                               video = new Video_Audio(me, imagebytes, imagebytes);
                            }
                            self_box = box_class.get_p_box(self_box);
                            self_box.update_video(video);
                            //getsomeones content 
                            LinkedList<Video_Audio> others_videos = self_box.get_videos();
                            for (Video_Audio other : others_videos ){
                                if(other.getId() != video.getId()){
                                    byte[] otherimgBytes = other.getImage_bytes();
                                    Files.write(Paths.get("images/"+other.getName()+".jpg"), otherimgBytes,  StandardOpenOption.CREATE);
                                    image_view_user_him.setImage(new Image("images/"+other.getName()+".jpg"));
                                }
                            }
                            box_class.update_box(self_box);
                        }
                        box_class.set_modif();
                    }catch(Exception e){
                         e.printStackTrace();
                     System.exit(1);
                    }   
                }
            }
            
            
        };
        task.start();
        
        //rmi get the image , download it , and then show it repeatedly
    }    

    @FXML
    private void exit(ActionEvent event) {
        if (chat_boxes instanceof Chat_Private_Messages_Boxes_Int ){
                    Chat_Private_Messages_Boxes_Int box_class = (Chat_Private_Messages_Boxes_Int)chat_boxes;
                    try {
                        self_box = box_class.get_p_box(self_box);
                        self_box.remove_video(video);
                        box_class.update_box(self_box);
                        Stage current = (Stage) image_view_user_me.getScene().getWindow();
                        current.close();
                    }catch(Exception e){
                         e.printStackTrace();
                     System.exit(1);
                    }   
                }
    }

    
    
}
