package org.academiadecodigo.bootcamp8.freespeech.server;

import org.academiadecodigo.bootcamp8.freespeech.server.utils.JdbcUserService;
import org.academiadecodigo.bootcamp8.freespeech.server.utils.TempUserService;
import org.academiadecodigo.bootcamp8.freespeech.server.utils.UserService;
import org.academiadecodigo.bootcamp8.freespeech.shared.Values;
import org.academiadecodigo.bootcamp8.freespeech.shared.Values;
import org.academiadecodigo.bootcamp8.freespeech.shared.message.SealedSendable;
import org.academiadecodigo.bootcamp8.freespeech.shared.message.Sendable;
import org.academiadecodigo.bootcamp8.freespeech.shared.utils.Crypto;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Developed @ <Academia de Código_>
 * Created by
 * <Code Cadet> PedroMAlves / Fábio Fernandes
 */

public class Server {
    private Key symKey;
    private UserService userService;
    private static int port;
    private static ServerSocket serverSocket = null;
    private ExecutorService cachedPool;
    private CopyOnWriteArrayList<ClientHandler> loggedUsers;

    /**
     *
     *
     * @param port
     */
    public Server(int port) {
        Server.port = port;
        loggedUsers = new CopyOnWriteArrayList<>();
    }

    /**
     * Initializates de serverSocket, the threadPool and the UserService
     * @throws IOException
     */

    public void init() throws IOException {
        Crypto crypto = new Crypto();
        crypto.generateSymmetricKey();
        symKey = crypto.getSymmetricKey();

        System.out.println("SERVER SYM KEY " + symKey);

        serverSocket = new ServerSocket(port);
        cachedPool = Executors.newCachedThreadPool();
        //userService = TempUserService.getInstance();
        userService = JdbcUserService.getInstance();
    }

    /**
     * It waits for new connects and, for each new connection, it gives a thread each will be dedicated to that
     * connections
     *
     * @throws IOException
     */

    public void start() throws IOException {
        userService.eventlogger(Values.TypeEvent.SERVER, Values.SERVER_START);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println(Thread.currentThread().getName() + ": handshake");
            cachedPool.submit(new ClientHandler(this, clientSocket, symKey));
            userService.eventlogger(Values.TypeEvent.CLIENT, Values.CONNECT_CLIENT + "--" + clientSocket.toString());
        }

    }

    /**
     * closes de ServerSocket...
     */
    public void closeServerSocket() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                userService.eventlogger(Values.TypeEvent.SERVER, Values.SERVER_STOP);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * returns the UserService
     * @return
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * add a new log in user to the list of active clientHandlers
     * @param client
     */
    public void addActiveUser(ClientHandler client) {
        loggedUsers.add(client);
    }

    /**
     * removes clientHandles from the list of active clientHandlers.
     * This method is called went a connections is closed, intentionaly or not.
     * @param client
     */
    public void logOutUser(ClientHandler client) {

        loggedUsers.remove(client);
    }

    /**
     * Iterates the list os ClientHandlers, calling the write method of each of the clientHandlers.
     * This method is called by a ClientHandler when it receives a new input from the client.
     * @param sendable
     */
    public void writeToAll(SealedSendable sendable) {
        for (ClientHandler c : loggedUsers) {
            c.write(sendable);

        }
    }

    /**
     * Iterates the list of ClientHandlers, searching for the client that the Sendable msg is destined to.
     * When it finds it, it calls the write method of that client, sending the message.
     * The Sendable has to respect the following structure: The type must be PRIVATE_TEXT or PRIVATE_DATA;
     * and the content must be an HashMap with 2 String: an Values.Destiny_User field and a text field.
     * @param msg
     */
    public void write(SealedSendable msg) {

        HashMap<String,String> content = null;
        try {
            content = (HashMap<String,String>)msg.getContent(symKey).getContent(HashMap.class);
            String destiny = content.get(Values.DESTINY_USER);

            for (ClientHandler c : loggedUsers){
                if(c.getName().equals(destiny)){

                    c.write(msg);
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * It returns a list of usernames of the active online users at the time.
     * @return
     */
    public List<String> getUsersOnlineList() {

        //TODO think of a better idea
        LinkedList<String> usersList = new LinkedList<>();

        for (ClientHandler c :loggedUsers){
            usersList.add(c.getName());
        }

        return usersList;
    }
}
