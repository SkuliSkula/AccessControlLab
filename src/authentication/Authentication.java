package authentication;

import constants.LoginConstants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.*;

public class Authentication {
    private JSONHandler jsonHandler;
    private String salt;
    public Authentication(JSONHandler jsonHandler) {
        this.jsonHandler = jsonHandler;
    }
    public String handleLogIn(String userName, String password) throws IOException{
        return verifyLogIn(userName, password);

    }
    public boolean registerUser(String userName, String password, String role) throws IOException {
        return jsonHandler.writeJSONLogin(userName,hashAndSaltPassword(password),salt, role);
    }
    public String verifyLogIn(String userName, String password) {
        JSONObject jsonObject = jsonHandler.readJSON(jsonHandler.getLoginFilePath());
        JSONArray passwordKeeper = (JSONArray) jsonObject.get(LoginConstants.LOGINS);
        String role = "";
        if(passwordKeeper == null) {
            System.out.println("Problem with login file");
            return role;
        }else{
            for(int i = 0; i < passwordKeeper.size(); i++) {
                JSONObject obj = (JSONObject) passwordKeeper.get(i);
                String pass = (String) obj.get(LoginConstants.PASSWORD);
                String salt = (String) obj.get(LoginConstants.SALT);
                if(userName.equals(obj.get(LoginConstants.USER_NAME)) && pass.equals(unHashPassword(password,salt))){
                    role = (String) obj.get(LoginConstants.ROLE);
                    break;
                }
            }
        }
        return role;
    }
    private String hashAndSaltPassword(String password) {
        salt = RandomStringUtils.randomAscii(20);
        return DigestUtils.sha256Hex(password + salt);
    }

    private String unHashPassword(String password,String salt) {
        return DigestUtils.sha256Hex(password + salt);
    }
}