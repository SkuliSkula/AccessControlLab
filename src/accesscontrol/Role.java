package accesscontrol;

import authentication.JSONHandler;
import constants.RoleConstants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public  class Role {
    public static final String MANAGER = "Manager";
    public static final String TECHNICIAN = "Technician";
    public static final String POWER_USER = "PowerUser";
    public static final String USER = "User";
    private ArrayList<String> rolesList;
    private JSONHandler jsonHandler;
    public Role(JSONHandler jsonHandler){
        this.jsonHandler = jsonHandler;
        this.rolesList = new ArrayList<>();
        constructRolesList();
    }

    private void constructRolesList() {
        JSONObject jsonObject = jsonHandler.readJSON(jsonHandler.getRolesFilePath());
        JSONArray roles = (JSONArray) jsonObject.get(RoleConstants.ROLES);
        roles.forEach(item ->rolesList.add((String)item));
    }

    public ArrayList<String> getRolesList() {
        return rolesList;
    }
}
