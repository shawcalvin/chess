package service;

import dataAccess.*;
import dataAccess.MemoryDataAccess.*;
import dataAccess.SQLDataAccess.SQLAuthDAO;
import dataAccess.SQLDataAccess.SQLGameDAO;
import dataAccess.SQLDataAccess.SQLUserDAO;
import model.chessModels.AuthData;
import model.chessModels.UserData;

public class Service {
    public static UserDAO userDAO;
    public static GameDAO gameDAO;
    public static AuthDAO authDAO;
    public static final String badRequestMessage = "Error: bad request";
    public static final String unauthorizedAccessMessage = "Error: unauthorized";
    public static final String forbiddenResourceMessage = "Error: already taken";
    public static final String whiteColor = "WHITE";
    public static final String blackColor = "BLACK";

    Service() {
        userDAO = SQLUserDAO.getInstance();
        gameDAO = SQLGameDAO.getInstance();
        authDAO = SQLAuthDAO.getInstance();
    }

    public boolean isValidAuthToken(String authToken) throws DataAccessException {
        AuthData auth = authDAO.get(authToken);
        return !(auth == null);
    }

    public boolean isValidUsernamePassword(String username, String password) throws DataAccessException {
        UserData user = userDAO.get(username);
        return user != null && password.equals(user.password());
    }

    public String getServerErrorMessage(Exception e) {
        return "Error: " + e.getMessage();
    }
}
