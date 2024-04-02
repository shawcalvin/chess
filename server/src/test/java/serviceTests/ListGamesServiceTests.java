package serviceTests;

import model.responseModels.FailureType;
import model.responseModels.ServiceResponse;
import org.junit.jupiter.api.*;
import model.responseModels.*;
import service.*;

class ListGamesServiceTests {
    private static String newUsername;
    private static String newPassword;
    private static String newEmail;
    private static String existingUsername;
    private static String existingPassword;
    private static String existingEmail;
    private static String existingAuthToken;
    private static ChessService service;
    private static String existingGameName;
    private static int existingGameID;

    private static String whiteColor;
    private  static String blackColor;

    @BeforeAll
    public static void init() {
        newUsername = "new-username";
        newPassword = "new-password";
        newEmail = "new-email";

        existingUsername = "existing-username";
        existingPassword = "existing-password";
        existingEmail = "existing-email";

        existingGameName = "cool-new-game";

        whiteColor = "WHITE";
        blackColor = "BLACK";

        service = new ChessService();
    }

    @BeforeEach
    public void setup() {
        service.clearDatabase();
        RegisterResponse registerResponse = (RegisterResponse) service.register(existingUsername, existingPassword, existingEmail);
        existingAuthToken = registerResponse.authToken();

        CreateGameResponse createGameResponse = (CreateGameResponse) service.createGame(existingAuthToken, existingGameName);
        existingGameID = createGameResponse.gameID();
    }

    @Test
    void listGamesValidUser() {
        RegisterResponse registerResponse = (RegisterResponse) service.register(newUsername, newPassword, newEmail);
        String newAuthToken = registerResponse.authToken();

        service.joinGame(existingAuthToken, whiteColor, existingGameID);
        service.joinGame(newAuthToken, blackColor, existingGameID);

        ServiceResponse response = service.listGames(existingAuthToken);
        Assertions.assertEquals(ListGamesResponse.class, response.getClass());
        ListGamesResponse listGamesResponse = (ListGamesResponse) response;
        Assertions.assertFalse(listGamesResponse.games().isEmpty());
    }

    @Test
    void listGamesBadAuth() {
        RegisterResponse registerResponse = (RegisterResponse) service.register(newUsername, newPassword, newEmail);
        String newAuthToken = registerResponse.authToken();

        service.joinGame(existingAuthToken, whiteColor, existingGameID);
        service.joinGame(newAuthToken, blackColor, existingGameID);

        ServiceResponse response = service.listGames("invalid-auth-token");
        Assertions.assertEquals(FailureResponse.class, response.getClass());
        FailureResponse failureResponse = (FailureResponse) response;
        Assertions.assertEquals(FailureType.UNAUTHORIZED_ACCESS, failureResponse.failureType());
    }

    @Test
    void listManyGames() {
        RegisterResponse registerResponse = (RegisterResponse) service.register(newUsername, newPassword, newEmail);
        String newAuthToken = registerResponse.authToken();

        CreateGameResponse createGameResponse1 = (CreateGameResponse) service.createGame(existingAuthToken, "new-game-1");
        CreateGameResponse createGameResponse2 = (CreateGameResponse) service.createGame(existingAuthToken, "new-game-1");
        CreateGameResponse createGameResponse3 = (CreateGameResponse) service.createGame(existingAuthToken, "new-game-1");

        int gameID1 = createGameResponse1.gameID();
        int gameID2 = createGameResponse2.gameID();
        int gameID3 = createGameResponse3.gameID();

        service.joinGame(existingAuthToken, whiteColor, existingGameID);
        service.joinGame(newAuthToken, blackColor, existingGameID);

        service.joinGame(existingAuthToken, whiteColor, gameID1);
        service.joinGame(newAuthToken, blackColor, gameID1);

        service.joinGame(existingAuthToken, whiteColor, gameID2);
        service.joinGame(newAuthToken, blackColor, gameID2);

        service.joinGame(existingAuthToken, whiteColor, gameID3);
        service.joinGame(newAuthToken, blackColor, gameID3);


        ServiceResponse response = service.listGames(existingAuthToken);
        Assertions.assertEquals(ListGamesResponse.class, response.getClass());
        ListGamesResponse listGamesResponse = (ListGamesResponse) response;
        Assertions.assertEquals(4, listGamesResponse.games().size());
    }
}