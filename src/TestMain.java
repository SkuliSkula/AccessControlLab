import client.Client;
import server.Server;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class TestMain {

    public static void main(String[] args) {

        try{
            Server server = new Server();
            Registry registry = LocateRegistry.createRegistry(1099, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null, null, true));
            registry.bind("server", server);
            Client client = new Client(registry);

            Scanner sc = new Scanner(System.in);
            int operation;
            do {
                if (!client.isLoggedIn())
                    logInMenu();
                else
                    printMenu();

                operation = sc.nextInt();
                handleOperation(client,operation);

            }while (operation != 0);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void handleOperation(Client client, int operation) {
        Scanner sc = new Scanner(System.in);
        try {
            if(!client.isLoggedIn()) {
                String userName;
                String password;
                switch (operation) {
                    case 0:
                        System.out.println("Good bye...");
                        System.out.println("Session terminated...");
                        break;
                    case 1:
                        System.out.println("Enter your user name");
                        userName = sc.nextLine();
                        System.out.println("Enter your password");
                        password = sc.nextLine();
                        if(client.handleLogIn(userName, password)){
                            client.setClientName(userName);
                            client.logClientIn(true);
                            System.out.println("Client logged in");
                        }
                        else
                            System.out.println("Client could NOT be logged in");
                        break;
                    case 2:
                        System.out.println("Select user name");
                        userName = sc.nextLine();
                        System.out.println("Create a password");
                        password = sc.nextLine();
                        if(client.registerClient(userName,password))
                            System.out.println(userName + " registered, now you can login");
                        else
                            System.out.println("Error occurred client not registered!");
                        break;
                }
            }else {
                switch (operation) {
                    case 1:
                        client.print("Filename", "myPrinter");
                        break;
                    case 2:
                        client.queue();
                        break;
                    case 3:
                        client.topQueue(1);
                        break;
                    case 4:
                        client.start();
                        break;
                    case 5:
                        client.stop();
                        break;
                    case 6:
                        client.restart();
                        break;
                    case 7:
                        client.serverStatus();
                        break;
                    case 8:
                        client.readConfig("MyParameter");
                        break;
                    case 9:
                        client.setConfig("MyParameter");
                        break;
                    case 10:
                        client.logClientIn(false);
                        break;
                    case 11:
                        System.out.println("Client Name: " + client.getClientName());
                        break;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("******Print server*******");
        System.out.println("Select an operation");
        System.out.println("1) print(String filename, String printer)");
        System.out.println("2) queue()");
        System.out.println("3) topQueue(int job)");
        System.out.println("4) start()");
        System.out.println("5) stop()");
        System.out.println("6) restart()");
        System.out.println("7) status()");
        System.out.println("8) readConfig(String parameter)");
        System.out.println("9) setConfig(String parameter, String value)");
        System.out.println("10) Logout");
        System.out.println("11) User details");
        System.out.println();
    }

    private static void logInMenu() {
        System.out.println();
        System.out.println("******Print server*******");
        System.out.println("Select an operation");
        System.out.println("0) Quit - terminate");
        System.out.println("1) Login");
        System.out.println("2) Register");
        System.out.println();

    }
}
