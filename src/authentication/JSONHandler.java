package authentication;

import constants.LoginConstants;
import constants.ServiceLogConstants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class JSONHandler {
    private final String LOGIN_FILE_PATH = "C:\\Git\\AccessControlLab\\login\\logins.json";
    private final String SERVICE_LOG_FILE_PATH = "C:\\Git\\AccessControlLab\\login\\servicelog.json";
    private final String PERMISSIONS_FILE_PATH = "C:\\Git\\AccessControlLab\\permission\\permissions.json";
    private final String OPERATIONS_FILE_PATH = "C:\\Git\\AccessControlLab\\operation\\operations.json";
    private final String ROLES_FILE_PATH = "C:\\Git\\AccessControlLab\\role\\roles.json";
    public JSONHandler() {
        try {
            createJSONLoginFile();
            createJSONServiceLogFile();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean writeJSONLogin(String userName, String password, String salt, String role) throws IOException {
        JSONObject mainObject = readJSON(LOGIN_FILE_PATH);
        JSONArray arr = (JSONArray) mainObject.get(LoginConstants.LOGINS);

        if(checkIfUserNameExists(arr,userName)) {
            System.out.println("That user name is taken, please choose another one");
            return false;
        }
        JSONObject valueObj = new JSONObject();
        valueObj.put(LoginConstants.USER_NAME, userName);
        valueObj.put(LoginConstants.PASSWORD, password);
        valueObj.put(LoginConstants.SALT, salt);
        valueObj.put(LoginConstants.ROLE, role);
        arr.add(valueObj);

        try(FileWriter file = new FileWriter(LOGIN_FILE_PATH)) {
            file.write(mainObject.toJSONString());
            System.out.println("User added successfully...");
        }
        return true;
    }

    public void writeJSONServiceLog(String userName, String operation) throws IOException {
        JSONObject mainObject = readJSON(SERVICE_LOG_FILE_PATH);
        JSONArray arr = (JSONArray) mainObject.get(ServiceLogConstants.OPERATIONS);
        JSONObject values = new JSONObject();
        values.put(ServiceLogConstants.USER_NAME, userName);
        values.put(ServiceLogConstants.OPERATION, operation);
        values.put(ServiceLogConstants.TIMESTAMP, System.currentTimeMillis());
        arr.add(values);
        try(FileWriter file = new FileWriter(SERVICE_LOG_FILE_PATH)) {
            file.write(mainObject.toJSONString());
            System.out.println("Service logged successfully...");
        }
    }
    public JSONObject readJSON(String filePath) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try{
            Object obj = parser.parse(new FileReader(filePath));
            jsonObject = (JSONObject) obj;
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public void createJSONLoginFile() throws IOException{
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        obj.put(LoginConstants.LOGINS, list);
        if(!checkIfFileExists(LOGIN_FILE_PATH)){
            try(FileWriter file = new FileWriter(LOGIN_FILE_PATH)) {
                file.write(obj.toJSONString());
                System.out.println("Login file created successfully...");
            }
        }
    }
    public void createJSONServiceLogFile() throws IOException{
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();
        obj.put(ServiceLogConstants.OPERATIONS, list);

        if(!checkIfFileExists(SERVICE_LOG_FILE_PATH)){
            try(FileWriter file = new FileWriter(SERVICE_LOG_FILE_PATH)) {
                file.write(obj.toJSONString());
                System.out.println("Service log file created successfully...");
            }
        }
    }
    public boolean checkIfUserNameExists(JSONArray passwordKeeper, String userName) {
        boolean userFound = false;
        for(int i = 0; i < passwordKeeper.size(); i++) {
            JSONObject obj = (JSONObject) passwordKeeper.get(i);
            if(userName.equals(obj.get(LoginConstants.USER_NAME))){
                userFound = true;
                break;
            }
        }
        return userFound;
    }

    public void updateRole(String userName, String newRole) throws IOException {
        JSONObject mainObject = readJSON(LOGIN_FILE_PATH);
        JSONArray arr = (JSONArray) mainObject.get(LoginConstants.LOGINS);

        for(int i = 0; i < arr.size(); i++) {
            JSONObject obj = (JSONObject) arr.get(i);
            String user = (String) obj.get(LoginConstants.USER_NAME);
            if(user.equals(userName))
                obj.replace(LoginConstants.ROLE, newRole);
        }

        try(FileWriter file = new FileWriter(LOGIN_FILE_PATH)) {
            file.write(mainObject.toJSONString());
            System.out.println("Role updated successfully...");
        }
    }

    public String getLoginFilePath() {
        return LOGIN_FILE_PATH;
    }
    public String getServiceLogFilePath() {
        return SERVICE_LOG_FILE_PATH;
    }
    public String getPermissionFilePath(){return PERMISSIONS_FILE_PATH;}
    public String getOperationsFilePath(){return OPERATIONS_FILE_PATH;}
    public String getRolesFilePath(){return ROLES_FILE_PATH;}
    private boolean checkIfFileExists(String filePath){
        File f = new File(filePath);
        return (f.exists() && !f.isDirectory());
    }
}




