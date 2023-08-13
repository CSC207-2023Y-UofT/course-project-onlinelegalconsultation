package usecasetesting;

import adapter.controller.ControlContainer;
import businessrule.gateway.ClientGateway;
import businessrule.gateway.UserGatewayFactory;
import businessrule.inputboundary.UserLoginInputBoundary;
import businessrule.outputboundary.HomePageOutputBoundary;
import businessrule.requestmodel.UserLoginRequestModel;
import businessrule.responsemodel.HomePageResponseModel;
import businessrule.usecase.UserLoginInteractor;
import driver.database.*;
import entity.Client;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains test cases for the UserLoginUseCase.
 */

public class UserLoginUseCaseTest {
    final static int CLIENT_ID = 21345678;
    final static String PASSWORD = "abcdefg";
    private UserGatewayFactory userGatewayFactory;
    private ClientGateway clientGateway;
    private HomePageOutputBoundary homePageOutputBoundary;
    private UserLoginInputBoundary userLoginInputBoundary;

    /**
     * Set up the test environment by initializing the UserLoginUseCase instance.
     */
    public void setUpUserLoginUseCase(){
        userGatewayFactory = new UserGatewayFactory();
        clientGateway = new ClientRepository();

        homePageOutputBoundary = new HomePageOutputBoundary() {
            @Override
            public void setControlContainer(ControlContainer controlContainer) {

            }

            @Override
            public HomePageResponseModel prepareFail(String msg) {
                System.out.println(msg);
                return null;
            }

            @Override
            public HomePageResponseModel prepareSuccess(HomePageResponseModel homePageResponseModel) {
                return null;
            }
        };
        userLoginInputBoundary = new UserLoginInteractor(userGatewayFactory, homePageOutputBoundary);

        Client client = new Client();
        client.setUserId(CLIENT_ID);
        client.setPassword(PASSWORD);
        clientGateway.save(client);
    }
    /**
     * Test a successful user login scenario.
     */
    @Test
    public void TestLoginPass(){
        setUpUserLoginUseCase();
        UserLoginRequestModel inputData = new UserLoginRequestModel(CLIENT_ID, PASSWORD);
        userLoginInputBoundary.login(inputData);
        ClearAllRepository();
    }
    /**
     * Test user login fails because the provided ID does not exist.
     */
    @Test
    public void TestLoginFailIdDNE(){
        setUpUserLoginUseCase();
        UserLoginRequestModel inputData = new UserLoginRequestModel(1, PASSWORD);
        userLoginInputBoundary.login(inputData);
        ClearAllRepository();
    }
    /**
     * Test the scenario where user login fails because the provided password is incorrect.
     */
    @Test
    public void TestLoginFailWrongPassword(){
        setUpUserLoginUseCase();
        UserLoginRequestModel inputData = new UserLoginRequestModel(CLIENT_ID, "a");
        userLoginInputBoundary.login(inputData);
        ClearAllRepository();
    }

    /**
     * Delete all data in clientGateway.
     */
    public void ClearAllRepository(){
        clientGateway = new ClientRepository();
        clientGateway.deleteAll();
    }
}
