package service;

import model.AuthData;
import server.responseModels.FailureResponse;
import server.responseModels.FailureType;
import server.responseModels.LoginResponse;
import server.responseModels.ServiceResponse;

public class LoginService extends Service {
    public ServiceResponse login(String username, String password) {
        if (username == null || password == null) {
            return new FailureResponse(FailureType.BAD_REQUEST, badRequestMessage);
        }
        try {
            if (!isValidUsernamePassword(username, password)) {
                return new FailureResponse(FailureType.UNAUTHORIZED_ACCESS, unauthorizedAccessMessage);
            }
            AuthData auth = authDAO.create(username, password);
            return new LoginResponse(auth.username(), auth.authToken());
        } catch (Exception e){
            return new FailureResponse(FailureType.SERVER_ERROR, getServerErrorMessage(e));
        }
    }
}