package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import purejavacomm.CommPortIdentifier;

import java.util.Enumeration;


public class Controller {

    @FXML
    private TextArea log;
    @FXML
    private Label label;
    @FXML
    private ComboBox<String> com;
    @FXML
    private Button startBtn;
    @FXML
    private Button stopBtn;

    private String chosenCom;

    private static volatile Controller ControllerInstance;

    public Controller(){
        ControllerInstance=this;
    }

    public static Controller getInstance(){

        if (null == ControllerInstance){

            synchronized (Controller.class){

                if (null == ControllerInstance){

                    ControllerInstance = new Controller();
                }
            }
        }
        return ControllerInstance;
    }

    public void onStartAction(ActionEvent actionEvent) {
        if(null!=com.getSelectionModel().getSelectedItem()){
            String comPort = com.getSelectionModel().getSelectedItem();
            Port.getInstance().open(comPort);
            Port.getInstance().startReceiving();
            startBtn.setDisable(true);
            stopBtn.setDisable(false);
            com.setDisable(true);
            label.setText("Click the STOP button to close the port and stop receiving data");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("COM port has not been chosen");
            alert.setHeaderText("To open the port you have to choose a COM port");
            alert.setContentText("Please, choose a COM port");
            alert.showAndWait();
        }
    }

    public void onStopAction(ActionEvent actionEvent) {
        Port.getInstance().close();
        log.appendText("Port has been closed...\n");
        stopBtn.setDisable(true);
        startBtn.setDisable(false);
        com.setDisable(false);
        Port.getInstance().StopAll=true;
        label.setText("Click the START button to open the port and start receiving data");
    }

    public void log(String text) {
        log.appendText(text);
    }

    public void onSelectCom(ActionEvent actionEvent) {
        if (null != com.getSelectionModel().getSelectedItem()){

            if ( !chosenCom.equals(com.getSelectionModel().getSelectedItem()) ) {
                chosenCom = com.getSelectionModel().getSelectedItem();
            }
        }
    }

    public void onListPorts(MouseEvent mouseEvent) {
        com.getItems().clear();

        Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();

        while(ports.hasMoreElements())
        {
            com.getItems().add(ports.nextElement().getName());
        }
    }
}
