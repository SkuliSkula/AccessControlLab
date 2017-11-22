package server;

import authentication.Authentication;
import authentication.JSONHandler;
import constants.LoginConstants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;


/*Class to manage the server. Create users, change users, change user roles etc...*/
public class ServerManager {
    private JSONHandler jsonHandler;
    private Authentication authentication;
    public ServerManager(JSONHandler jsonHandler, Authentication authentication){
        this.jsonHandler = jsonHandler;
        this.authentication = authentication;
    }

    public boolean registerClient(String userName, String password, String role) throws IOException {
        if(!checkPasswordStrength(password)) {
            System.out.println("The password needs to contain upper case letter, characters and numbers and be 8 characters or longer");
            return false;
        }else if(!checkUserName(userName)) {
            System.out.println("The user name has to be longer than 5 characters");
            return false;
        }
        else {
            return authentication.registerUser(userName, password, role);
        }
    }

    public void changeRole(String userName, String newRole) throws IOException {
        jsonHandler.updateRole(userName,newRole);
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
    public void listUsersByRole(String role) {
        JSONObject jsonObject = jsonHandler.readJSON(jsonHandler.getLoginFilePath());
        JSONArray logins = (JSONArray) jsonObject.get(LoginConstants.LOGINS);
        System.out.println("All users with " + role + " role are:");
        for(int i = 0; i < logins.size(); i++) {
            JSONObject user = (JSONObject) logins.get(i);
            if(role.equals(user.get(LoginConstants.ROLE))){
                System.out.println(user.get(LoginConstants.USER_NAME));
            }
        }

    }
}
