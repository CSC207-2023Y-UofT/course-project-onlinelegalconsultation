package usecasetesting;

import adapter.controller.ControlContainer;
import businessrule.SessionManager;
import businessrule.UserSession;
import businessrule.gateway.AttorneyGateway;
import businessrule.gateway.ClientGateway;
import businessrule.gateway.QuestionGateway;
import businessrule.inputboundary.QuestionInputBoundary;
import businessrule.outputboundary.TheQuestionOutputBoundary;
import businessrule.requestmodel.QuestionRequestModel;
import businessrule.responsemodel.TheQuestionResponseModel;
import businessrule.responsemodel.UserResponseModel;
import businessrule.usecase.AskQuestionInteractor;

import driver.database.*;
import entity.*;
import entity.factory.QuestionFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
;

public class AskQuestionUseCaseTest {
    final static int CLIENT_ID = 21345678;
    final static String CLIENT_USERNAME = "test client";
    final static String CLIENT_TYPE = "Client";
    final static int ATTORNEY_ID = 11345678;
    final static int SECOND_ATTORNEY_ID = 12222222;
    private QuestionGateway questionGateway;
    private QuestionFactory questionFactory;
    private ClientGateway clientGateway;
    private AttorneyGateway attorneyGateway;
    private TheQuestionOutputBoundary theQuestionOutputBoundary;
    private QuestionInputBoundary questionInputBoundary;

    public void setUpAskQuestionUseCase(){
        questionGateway = new QuestionRepo();
        questionFactory = new QuestionFactory();
        clientGateway = new ClientRepository();
        attorneyGateway = new AttorneyRepository();
        theQuestionOutputBoundary = new TheQuestionOutputBoundary() {

            @Override
            public TheQuestionResponseModel prepareFail(String msg) {
                assertEquals("Please specify your question type.", msg);
                return null;
            }

            @Override
            public TheQuestionResponseModel prepareSuccess(TheQuestionResponseModel response) {
                assertEquals(0, response.getPostMap().size(), "PostMap is not correct");
                return null;
            }

        };

        questionInputBoundary = new AskQuestionInteractor(questionGateway, theQuestionOutputBoundary, questionFactory, clientGateway);

        Client client = new Client();
        client.setUserId(CLIENT_ID);
        clientGateway.save(client);

        Attorney attorney = new Attorney();
        attorney.setUserId(ATTORNEY_ID);
        attorneyGateway.save(attorney);

        Attorney secondAttorney = new Attorney();
        attorney.setUserId(SECOND_ATTORNEY_ID);
        attorneyGateway.save(secondAttorney);

        UserResponseModel userResponseModel = new UserResponseModel(CLIENT_ID, CLIENT_USERNAME, CLIENT_TYPE);
        UserSession session = new UserSession(userResponseModel);
        SessionManager.setSession(session);
    }

    @Test
    public void TestAskQuestionPassed(){
        setUpAskQuestionUseCase();

        QuestionRequestModel inputData = new QuestionRequestModel("fraud", "Test title", LocalDate.now(), LocalDate.now());

        questionInputBoundary.createQuestion(inputData);

        User user = clientGateway.get(CLIENT_ID);
        assertEquals(1, user.getQuestionsList().size(), "The ask question use case failed.");
        ClearAllRepository();
    }

    @Test
    public void TestAskQuestionFailByEmptyCategory(){
        setUpAskQuestionUseCase();

        QuestionRequestModel inputData = new QuestionRequestModel(null, "Test title", LocalDate.now(), LocalDate.now());

        questionInputBoundary.createQuestion(inputData);

        User user = clientGateway.get(CLIENT_ID);
        assertEquals(0, user.getQuestionsList().size(), "The ask question use case failed.");
        ClearAllRepository();
    }

    public void ClearAllRepository(){
        questionGateway = new QuestionRepo();
        clientGateway = new ClientRepository();
        attorneyGateway = new AttorneyRepository();
        clientGateway.deleteAll();
        questionGateway.deleteAll();
        attorneyGateway.deleteAll();
    }
}
