package org.chat.netty.project;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private Network network;
    @FXML
    TextField msField;

    @FXML
    TextArea mainArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = new Network((args -> {
            mainArea.appendText((String)args[0]);
        }));
    }

    public void sendMsAction(ActionEvent actionEvent) {
        network.sendMessage(msField.getText());
        msField.clear();
        msField.requestFocus();
    }

    public void exitAction() {
        network.close();
        Platform.exit();
    }
}