package usecasetesting;

import businessrule.gateway.AttorneyGateway;
import businessrule.gateway.ClientGateway;
import businessrule.gateway.QuestionGateway;
import businessrule.inputboundary.BrowseInputBoundary;
import businessrule.outputboundary.ViewOutputBoundary;
import businessrule.requestmodel.BrowseRequestModel;
import businessrule.responsemodel.ViewResponseModel;
import businessrule.usecase.BrowseQuestionInterator;
import businessrule.usecase.QuestionDisplayFormatter;
import driver.database.*;

import entity.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
;

public class BrowseQuestionUseCaseTest {
    final static int CLIENT_ID = 21345678;
    final static int ATTORNEY_ID = 11345678;
    final static int SECOND_ATTORNEY_ID = 12222222;
    final static int QUESTION_ID = 323456789;
    final static int TAKEN_QUESTION_ID = 333333333;
    final static int CLOSED_QUESTION_ID = 322222222;
    final static int POST_ID = 455555555;
    private QuestionGateway questionGateway;
    private ClientGateway clientGateway;
    private AttorneyGateway attorneyGateway;
    private ViewOutputBoundary viewOutputBoundary = new ViewOutputBoundary() {
        @Override
        public ViewResponseModel prepareFail(String msg) {
            return null;
        }

        @Override
        public ViewResponseModel prepareSuccess(ViewResponseModel response) {
            assertEquals(2, response.getQuestionMap().size(), "The Question Map is not correct.");
            List<QuestionDisplayFormatter> arrayList;
            arrayList = new ArrayList<>(response.getQuestionMap().values());
            assertEquals("test title", arrayList.get(0).getTitle());
            return null;
        }
    };
    private BrowseInputBoundary browseInputBoundary;

    public void setUpBrowseUseCase(){
        questionGateway = new QuestionRepo();
        clientGateway = new ClientRepository();
        attorneyGateway = new AttorneyRepository();
        clientGateway.deleteAllUser();
        questionGateway.deleteAllQuestion();
        attorneyGateway.deleteAllUser();
        browseInputBoundary = new BrowseQuestionInterator(viewOutputBoundary, questionGateway, attorneyGateway);

        Client client = new Client();
        client.setUserId(CLIENT_ID);
        clientGateway.addUser(client);

        Attorney attorney = new Attorney();
        attorney.setUserId(ATTORNEY_ID);
        attorneyGateway.addUser(attorney);

        Attorney secondAttorney = new Attorney();
        attorney.setUserId(SECOND_ATTORNEY_ID);
        attorneyGateway.addUser(secondAttorney);

        Question question1 = new Question();
        question1.setQuestionId(QUESTION_ID);
        question1.setTitle("test title");
        Post post = new Post();
        post.setPostId(POST_ID);
        question1.addPosts(post);
        questionGateway.saveQuestion(question1);

        Question question2 = new Question();
        question2.setQuestionId(TAKEN_QUESTION_ID);
        question2.setTaken(true);
        question2.setTakenByAttorney(ATTORNEY_ID);
        questionGateway.saveQuestion(question2);

        Question question3 = new Question();
        question3.setQuestionId(CLOSED_QUESTION_ID);
        question3.setClose(true);
        questionGateway.saveQuestion(question3);
    }

    @Test
    public void TestAttorneyBrowseQuestionUseCase(){
        setUpBrowseUseCase();
        BrowseRequestModel inputData = new BrowseRequestModel(ATTORNEY_ID);

        browseInputBoundary.browseQuestion(inputData);
    }

}