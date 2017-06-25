package org.academiadecodigo.bootcamp8.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.academiadecodigo.bootcamp8.client.service.ClientService;
import org.academiadecodigo.bootcamp8.client.utils.Navigation;
import org.academiadecodigo.bootcamp8.client.utils.Values;
import org.academiadecodigo.bootcamp8.message.Message;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Developed @ <Academia de Código_>
 * Created by
 * <Code Cadet> Filipe Santos Sá
 */

public class LoginController implements Controller {

    @FXML
    private GridPane root;
    @FXML
    private Button exitButton;
    @FXML
    private Label nameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private Button loginButton;
    @FXML
    private Label serverMessageLabel;
    @FXML
    private Button resgisterButton;

    private Stage stage;
    private ClientService clientService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serverMessageLabel.setVisible(false);
    }

    @Override
    public void init() {
    }


    @FXML
    void onLogin(ActionEvent event) {
        readFields(Message.Type.LOGIN);

        //TODO server response structure
        if (clientService.readObject() == new Message(Message.Type.LOGIN, new String("l ok"))) {
            Navigation.getInstance().loadScreen(Values.USER_SCENE);
        }
    }

    @FXML
    void onRegister(ActionEvent event) {
        readFields(Message.Type.REGISTER);
        ///TODO
        if (clientService.readObject() == new Message(Message.Type.LOGIN, new String("r ok"))) {
            serverMessageLabel.setVisible(true);
            serverMessageLabel.setText("REGISTER OK");
        }
    }
    private void readFields(Message.Type messageType) {
        Map<String, String> messageContent = new HashMap<>();
        messageContent.put(Values.NAME_KEY, nameField.getText());
        messageContent.put(Values.PASSWORD_KEY, passwordField.getText());

        Message<Map> message = new Message(messageType, messageContent);
        clientService.writeObject(message);
    }

    @FXML
    void onExitButton(ActionEvent event) {
        clientService.close();
        Navigation.getInstance().close();
    }

    @Override
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }
}