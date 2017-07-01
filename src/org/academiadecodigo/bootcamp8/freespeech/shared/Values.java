package org.academiadecodigo.bootcamp8.freespeech.shared;

/**
 * Developed @ <Academia de Código_>
 * Created by
 * <Code Cadet> Filipe Santos Sá, PedroMAlves
 */

public class Values {

    public static final String HOST = "127.0.0.1";
    public static final int SERVER_PORT = 4040;

    public static final String URL_DBSERVER = "jdbc:mysql://localhost:3306/freespeech";
    public static final String USER_DBSERVER = "root";
    public static final String PASSWORD_DBSERVER = "";

    public static final String VIEW = "../view";
    public static final String LOGIN_SCENE = "login";
    public static final String USER_SCENE = "user";
    public static final Long UID_MESSAGE = 12345678998432L;

    public static final String NAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String DESTINY_USER = "to";
    public static final String MESSAGE = "message";

    public static final String STYLESHEET = "resources/stylesheet.css";
    public static final String TITLE = "Free Speech";

    public static final String LOGIN_OK = "ok";
    public static final String LOGIN_FAIL = "Unable to login";
    public static final String REGISTER_OK = "Successfully registered user";
    public static final String USER_TAKEN = "Username taken. Choose another one";
    public static final String CHECK_PASSWORD = "Please confirm you typed the same password";

    public static final String EMPTY_FIELDS = "No empty fields accepted. Thanks";


    public static final String SERVER_START = "SERVER START";
    public static final String SERVER_STOP = "SERVER STOP";
    public static final String SERVER_DBCONNECT = "SERVER CONNECTED DATABASE";
    public static final String CONNECT_CLIENT = "CLIENT CONNECTED";
    public static final String CLIENT_DISCONNECTED = "CLIENT DISCONNECTED";
    public static final String CLIENT_LOGINOK = "CLIENT LOGGED IN";
    public static final String CLIENT_LOGINFAILED= "CLIENT LOGIN FAILED";
    public static final String CLIENT_LOGOUT = "CLIENT LOGGED OUT";
    public static final String CLIENT_REGISTED = "CLIENT REGISTERED";
    public static final String CLIENT_REGISTER_FAILED = "FAILED TO REGISTER USER";
    public static final String CLIENT_PASSORD = "CLIENT CHANGED PASSWORD";

    public enum TypeEvent { SERVER,LOGIN, REGISTER, CLIENT, DATABASE }

    public static final String NEW_USER = "New User";

    public static final double LOGIN_HEIGHT = 450d;
    public static final double LOGIN_WIDTH = 350d;
    public static final double CLIENT_WIDTH = 900d;
    public static final double CLIENT_HEIGHT = 600d;
}
