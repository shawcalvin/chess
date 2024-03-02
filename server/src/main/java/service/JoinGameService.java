package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.GameData;
import server.responseModels.*;

import java.util.Collection;

public class JoinGameService extends Service {
    public ServiceResponse joinGame(String authToken, String playerColor, Integer gameID ) {
        if (authToken == null || gameID == null) {
            return new FailureResponse(FailureType.BAD_REQUEST, badRequestMessage);
        }
        if (!isValidAuthToken(authToken)) {
            return new FailureResponse(FailureType.UNAUTHORIZED_ACCESS, unauthorizedAccessMessage);
        }
        try {
            GameData game = gameDAO.get(gameID);
            if (game == null) {
                return new FailureResponse(FailureType.BAD_REQUEST, badRequestMessage);
            }
            String whiteUsername = game.whiteUsername();
            String blackUsername = game.blackUsername();
            Collection<String> observerUsernames = game.observerUsernames();
            String playerUsername = authDAO.get(authToken).username();
            if (playerColor == null) {
                observerUsernames.add(playerUsername);
            } else if (playerColor.equals(whiteColor) && whiteUsername == null) {
                whiteUsername = playerUsername;
            } else if (playerColor.equals(blackColor) && blackUsername == null) {
                blackUsername = playerUsername;
            } else {
                return new FailureResponse(FailureType.FORBIDDEN_RESOURCE, forbiddenResourceMessage);
            }
            gameDAO.update(gameID, new GameData(gameID, whiteUsername, blackUsername, observerUsernames, game.gameName(), game.game()));
            return new JoinGameResponse();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            return new FailureResponse(FailureType.BAD_REQUEST, badRequestMessage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new FailureResponse(FailureType.SERVER_ERROR, getServerErrorMessage(e));
        }
    }
}
