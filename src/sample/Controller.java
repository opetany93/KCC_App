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

    private String units;

    float frequency=200000000;

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
            Port.getInstance().sendStart();
            long impulses=Port.getInstance().readTime();
            float time=calculateTime(impulses);
            log("Estimated time is: "+time+" " +units +"\n");
            Port.getInstance().sendStop();
            Port.getInstance().close();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("COM port has not been chosen");
            alert.setHeaderText("To open the port you have to choose a COM port");
            alert.setContentText("Please, choose a COM port");
            alert.showAndWait();
        }
    }

    private float calculateTime(long impulses) {
        float time=impulses/(16*frequency);
        if (time>1000){
            time=time/1000;
            units="s";
        }
        else if(time<1){
            time=time*1000;
            units="us";
        }
        else if(time<0.001){
            time=time*1000000;
            units="ns";
        }
        else{
            units="ms";
        }
        return time;
    }

    public void onStopAction(ActionEvent actionEvent) {
        log.clear();
    }

    public void log(String text) {
        log.appendText(text);
    }

    public void onSelectCom(ActionEvent actionEvent) {
        if (null != com.getSelectionModel().getSelectedItem()){

            if (null == chosenCom)
                chosenCom = "";

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
