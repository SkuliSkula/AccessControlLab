package operations;

import authentication.JSONHandler;
import constants.OperationConstants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;

public class Operation {
    private ArrayList<String> operations;
    private JSONHandler jsonHandler;
    public Operation(){
        jsonHandler = new JSONHandler();
        operations = new ArrayList<>();
        constructOperation();
    }

    private void constructOperation() {
        // Todo, read these values from a file!!!!!!
        JSONObject jsonObject = jsonHandler.readJSON(jsonHandler.getOperationsFilePath());
        JSONArray savedOperations = (JSONArray) jsonObject.get(OperationConstants.OPERATIONS);
        if(savedOperations == null){
            System.out.println("Error loading the operations...");
            return;
        }
        savedOperations.forEach(item -> operations.add((String)item));
    }

    public ArrayList<String> getOperations(String role) {
        ArrayList<String> returnList = new ArrayList<>();
        JSONObject jsonObject = jsonHandler.readJSON(jsonHandler.getPermissionFilePath());
        JSONArray jsonArray = (JSONArray) jsonObject.get(role); // Get the operations based on the role
        if(jsonArray == null){
            System.out.println("No operations found with that role: " + role);
        }else{
            for(int i = 0; i < jsonArray.size(); i++){
                for(int j = 0; j < operations.size(); j++) {
                    if(jsonArray.get(i).equals(operations.get(j))){
                        returnList.add(operations.get(j));
                        break;
                    }
                }
            }
        }
        returnList.add(0, "Quit"); // Manually add quit so the user can quit and log out
        return returnList;
    }
}
