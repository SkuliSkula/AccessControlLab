import accesscontrol.Role;
import client.Client;
import operations.Operation;
import server.Server;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestMain {

    public static void main(String[] args) {

        try{
            Server server = new Server();
            Registry registry = LocateRegistry.createRegistry(1099, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory(null, null, true));
            registry.bind("server", server);
            Client client = new Client(registry);
            Operation operations = new Operation();
            List<String> operationList = null;
            Scanner sc = new Scanner(System.in);
            int operation = 0;
            do {
                if(!client.isLoggedIn()){
                    startMenu();
                    operation = sc.nextInt();
                }
                else {
                    operationList = operations.getOperations(client.getRole());
                    System.out.println("User: " + client.getClientName());
                    System.out.println("Role: " + client.getRole());
                    for(int i = 0; i < operationList.size(); i++){
                        System.out.println(i + ") " + operationList.get(i));
                    }
                }

                handleOperation(client,operation, operationList, server);
            }while (operation != 0);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleOperation(Client client, int operation, List operations, Server server) {
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
                        sc = new Scanner(System.in);
                        System.out.println("Enter your user name");
                        userName = sc.nextLine();
                        System.out.println("Enter your password");
                        password = sc.nextLine();
                        String role = client.handleLogIn(userName,password);
                        if(!role.equals("")){
                            client.setClientName(userName);
                            client.setRole(role);
                            client.logClientIn(true);
                            System.out.println("********* Print server operations *************");
                        }
                        else
                            System.out.println("Client could NOT be logged in");
                        break;
                }
            }else {
                int selectedOperation = sc.nextInt();
                String op = (String) operations.get(selectedOperation);
                switch (op){
                    case "Quit":
                        client.logClientIn(false);
                        System.out.println(client.getClientName() + " logged out.");
                        break;
                    case "Register User":
                        boolean registered = false;
                        while (!registered){
                            Scanner scc = new Scanner(System.in);
                            System.out.print("Enter user name: ");
                            String userName = scc.nextLine();
                            System.out.print("Enter password: ");
                            String password = scc.nextLine();
                            System.out.print("Enter role: ");
                            String role = scc.nextLine();
                            if (!validateRole(role)){
                                System.out.println("Invalid Role! Valid Roles are: ");
                                server.getRoles().forEach(item-> System.out.println(item));
                            }

                            if(!client.registerClient(userName,password,role)) {
                                System.out.println("Something went wrong, user not registered...");
                            }else
                                registered = true;
                        }
                        break;
                    case "Change users role":
                        Scanner sccc = new Scanner(System.in);
                        System.out.print("Enter user name: ");
                        String userName = sccc.nextLine();
                        System.out.print("Enter new Role: ");
                        String role = sccc.nextLine();
                        if (!validateRole(role)){
                            System.out.println("Invalid Role! Valid Roles are: ");
                            server.getRoles().forEach(item-> System.out.println(item));
                        }else
                            client.changeUsersRole(userName, role);
                        break;
                    case "print":
                        client.print("MyFileName", "My printer");
                        break;
                    case "queue":
                        client.queue();
                        break;
                    case "topQueue":
                        client.topQueue(10);
                        break;
                    case "start":
                        client.start();
                        break;
                    case "stop":
                        client.stop();
                        break;
                    case "restart":
                        client.restart();
                        break;
                    case "status":
                        client.serverStatus();
                        break;
                    case "readConfig":
                        client.readConfig("Some parameter");
                        break;
                    case "setConfig":
                        client.setConfig("Some other parameter");
                        break;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startMenu(){
        System.out.println();
        System.out.println("******Welcome to Print server, choose an operation*******");
        System.out.println("Select an operation");
        System.out.println("0) Quit - terminate");
        System.out.println("1) Login");
        System.out.println();
    }

    private static boolean validateRole(String role) {
        return role.equals(Role.MANAGER) || role.equals(Role.TECHNICIAN) || role.equals(Role.POWER_USER) || role.equals(Role.USER);
    }
}
