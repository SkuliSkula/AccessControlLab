package client;

import server.IPrintServer;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements Serializable {

    private IPrintServer server;
    private boolean isLoggedIn = false;
    private String userName;
    private Registry registry;
    public Client(Registry registry) throws RemoteException {
        this.userName = "";
        this.registry = registry;
        try {
            setSSLSettings();
            server = (IPrintServer) this.registry.lookup("server");
        }catch (NotBoundException e) {
            System.out.println("Failed to find the server: " + e.getMessage());
        }
    }

    private void setSSLSettings() {
        String password = "Password";
        System.setProperty("javax.net.ssl.debug", "all");
        System.setProperty("javax.net.ssl.keyStore", "C:\\ssl\\keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", password);
        System.setProperty("javax.net.ssl.trustStore", "C:\\ssl\\truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", password);
    }
    public void serverStatus() throws RemoteException{
        server.status(this.userName);
    }
    public void print(String fileName, String printer) throws RemoteException {
        server.print(this.userName, fileName,printer);
    }
    public void queue() throws RemoteException {
        server.queue(this.userName);
    }
    public void topQueue(int job) throws RemoteException {
        server.topQueue(this.userName, job);
    }
    public void start() throws RemoteException {
        server.start(this.userName);
    }
    public void stop() throws RemoteException {
        server.stop(this.userName);
    }
    public void restart() throws RemoteException {
        server.restart(this.userName);
    }
    public void readConfig(String parameter) throws RemoteException {
        server.readConfig(this.userName, parameter);
    }
    public void setConfig(String parameter) throws RemoteException {
        server.setConfig(this.userName,parameter);
    }
    public void logClientIn(boolean value) {
        this.isLoggedIn = value;
    }
    public void setClientName(String userName) {
        this.userName = userName;
    }
    public boolean handleLogIn(String userName, String password) throws RemoteException {
        return server.handleLogIn(userName, password);
    }
    public boolean registerClient(String userName, String password) throws RemoteException {
        if(!checkPasswordStrength(password)) {
            System.out.println("The password needs to contain upper case letter, characters and numbers and be 8 characters or longer");
            return false;
        }else if(!checkUserName(userName)) {
            System.out.println("The user name has to longer than 5 characters");
            return false;
        }
        else
            return server.registerClient(userName, password);
    }
    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public String getClientName(){
        return this.userName;
    }
    private boolean checkPasswordStrength(String password){
        if(password.length() < 8){
            return false;
        }
        return password.matches("(([A-Z].*[0-9])|([0-9].*[A-Z]))");
    }

    private boolean checkUserName(String userName) {
        return userName.length() > 4;
    }

}
