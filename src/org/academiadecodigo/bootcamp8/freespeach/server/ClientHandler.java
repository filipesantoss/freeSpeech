package org.academiadecodigo.bootcamp8.freespeach.server;

import org.academiadecodigo.bootcamp8.freespeach.server.communication.Communication;
import org.academiadecodigo.bootcamp8.freespeach.server.communication.CommunicationService;
import org.academiadecodigo.bootcamp8.freespeach.shared.message.MessageType;
import org.academiadecodigo.bootcamp8.freespeach.shared.message.Sendable;
import org.academiadecodigo.bootcamp8.freespeach.server.utils.User;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

/**
 * Developed @ <Academia de Código_>
 * Created by
 * <Code Cadet> PedroMAlves / Fábio Fernandes
 */

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Server server;
    private Communication communication;


    public ClientHandler(Server server, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.server = server;
        communication = new CommunicationService();
    }


    @Override
    public void run() {

        try {

            buildBufferStreams();
            System.out.println("oi");
            //authenticateClient();
            System.out.println("ble");
            readFromClient();

        } catch (IOException e) {
            e.printStackTrace();
            closeSocket();
            return;
        }

    }


    private void buildBufferStreams() throws IOException {

        communication.openOutputChannel(clientSocket);
        communication.openInputChannel(clientSocket);
    }


    private void authenticateClient() throws IOException, ClassNotFoundException {

        Sendable sendable = null;
        boolean exit = false;
        String message = "";

        while (!exit) {

            sendable = communication.retrieveMessage();

            if (sendable.getType() == MessageType.LOGIN) {

                if(exit = makeLogIn(sendable)){
                    message = "OK";
                    server.addActiveUser(this);
                } else {
                    message = "NOTOK";
                }

            }

            if (sendable.getType() == MessageType.REGISTER) {

                if (exit = makeRegistry(sendable)) { //TODO: registry is enough to log in??
                    message = "OK";
                } else {
                    message = "NOTOK";
                }

            }

            communication.sendMessage(sendable.updateMessage(sendable.getType(), message));
        }

    }

    private boolean makeLogIn(Sendable sendable) {

        HashMap<String, String> map = (HashMap<String, String>) sendable.getContent();
        String username = map.get("username");
        String password = map.get("password");

        return server.getUserService().authenticate(username,password);

    }

    private boolean makeRegistry(Sendable sendable) {

        HashMap<String, String> mapR = (HashMap<String, String>) sendable.getContent();
        String username = mapR.get("username");
        boolean exit = false;

        synchronized (server.getUserService()) {

            if (server.getUserService().getUser(username) == null) {

                server.getUserService().addUser(new User(mapR.get("username"), mapR.get("password")));
                server.getUserService().notifyAll();
                exit = true;
            } else {
                server.getUserService().notifyAll();
            }
        }
        return exit;
    }

    private void readFromClient() {
        Sendable msg;
        while ((msg = communication.retrieveMessage()) != null) {


            System.out.println("sending message");
            server.writeToAll(msg);

        }
        closeSocket();
    }

    private void closeSocket() {
        try {
            clientSocket.close();

        } catch (IOException e) {
            System.out.println(Thread.currentThread().getName() + ": Olha, fudeu!");
            e.printStackTrace();

        }
    }



    public void write(Sendable sendable) {
        System.out.println("inside write method sending to client");
        communication.sendMessage(sendable);

    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
    }
}
