package authentication;

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
    public boolean handleLogIn(String userName, String password) throws IOException{
        if(verifyLogIn(userName, password))
            return true;
        return false;
    }
    public boolean registerUser(String userName, String password) throws IOException {
        return jsonHandler.writeJSON(userName,hashAndSaltPassword(password),salt);
    }
    public boolean verifyLogIn(String userName, String password) {
        JSONObject jsonObject = jsonHandler.readJSON(jsonHandler.getLoginFilePath());
        JSONArray passwordKeeper = (JSONArray) jsonObject.get("logins");
        boolean logIn = false;
        if(passwordKeeper == null) {
            System.out.println("Problem with login file");
            return logIn;
        }else{
            for(int i = 0; i < passwordKeeper.size(); i++) {
                JSONObject obj = (JSONObject) passwordKeeper.get(i);
                String pass = (String) obj.get("Password");
                String salt = (String) obj.get("Salt");
                if(userName.equals(obj.get("UserName")) && pass.equals(unHashPassword(password,salt))){
                    logIn = true;
                    break;
                }
            }
        }
        return logIn;
    }
    private String hashAndSaltPassword(String password) {
        salt = RandomStringUtils.randomAscii(20);
        return DigestUtils.sha256Hex(password + salt);
    }

    private String unHashPassword(String password,String salt) {
        return DigestUtils.sha256Hex(password + salt);
    }
}