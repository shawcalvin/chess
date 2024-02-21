package service;

import dataAccess.DataAccessException;
import model.GameData;
import server.responseModels.*;

import java.util.Collection;

public class CreateGameService extends Service {
    public ServiceResponse createGame(String authToken, String gameName) {
        if (authToken == null || gameName == null) {
            return new FailureResponse(FailureType.BAD_REQUEST, badRequestMessage);
        }
        if (!isValidAuthToken(authToken)) {
            return new FailureResponse(FailureType.UNAUTHORIZED_ACCESS, unauthorizedAccessMessage);
        }
        try {
            GameData game = gameDAO.createGame(gameName);
            return new CreateGameResponse(game.gameID());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new FailureResponse(FailureType.SERVER_ERROR, getServerErrorMessage(e));
        }
    }
}
