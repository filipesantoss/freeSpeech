package org.academiadecodigo.bootcamp8.freespeach.client.service;

import javafx.scene.control.TextArea;
import org.academiadecodigo.bootcamp8.freespeach.shared.Values;
import org.academiadecodigo.bootcamp8.freespeach.shared.message.Message;
import org.academiadecodigo.bootcamp8.freespeach.shared.message.Sendable;
import org.academiadecodigo.bootcamp8.freespeach.shared.utils.Stream;

import java.io.*;
import java.net.Socket;

/**
 * Developed @ <Academia de Código_>
 * Created by
 * <Code Cadet> Filipe Santos Sá
 */

//public abstract class TempClientService implements ClientService {
public class TempClientService  {
    //TODO Make this an interface

    private Socket clientSocket;
    //private ObjectOutputStream output;
    //private ObjectInputStream input;

    public TempClientService() {
        try {
            clientSocket = new Socket(Values.HOST, Values.SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //setupStreams();
    }

    /**
     * Instantiates an ObjectOutputStream and an ObjectInputStream from and to the client socket.
     */
    /*
    public void setupStreams() {
        try {
            output = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
            input = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("output stream: " + output);
        System.out.println("input stream: " + input);
    }
    */

    /**
     * @see ClientService#sendUserText(TextArea)
     * @param textField
     */
    //@Override
    public void sendUserText(TextArea textField) {

        if (textField.getText().isEmpty()) {
            return;
        }

        Message<String> message = new Message<>(Message.Type.DATA, textField.getText());
        writeObject(message);
        System.out.println("SENT: " + message);
        textField.clear();
        textField.requestFocus();
    }

    //@Override
    public void closeClientSocket() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //@Override
    public InputStream getInput() throws IOException {
        return clientSocket.getInputStream();
    }

    /**
     * @see ClientService#writeObject(Sendable)
     * @param message
     */
    //@Override
    public void writeObject(Sendable message) {
        try {
            //TODO util Stream class
            //output.writeObject(message);
            Stream.writeObject(clientSocket.getOutputStream(), message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //@Override
    public Message readObject() {
        Object serverMessage = null;
        try {
            //TODO util Stream class
            //serverMessage = input.readObject();
            serverMessage = Stream.readObject(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (Message) serverMessage;
    }
}