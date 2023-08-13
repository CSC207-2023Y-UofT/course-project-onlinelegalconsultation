package usecasetesting;

import adapter.controller.ControlContainer;
import businessrule.gateway.*;
import businessrule.inputboundary.PostInputBoundary;
import businessrule.outputboundary.HomePageOutputBoundary;
import businessrule.requestmodel.PostRequestModel;
import businessrule.responsemodel.HomePageResponseModel;
import businessrule.usecase.ReplyInteractor;
import driver.database.*;

import entity.*;

import entity.factory.PostFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
;
/**
 * This class contains test cases for the ReplyUseCase.
 */

public class ReplyUseCaseTest {
    final static int CLIENT_ID = 21345678;
    final static int ATTORNEY_ID = 11345678;
    final static int SECOND_ATTORNEY_ID = 12222222;
    final static int QUESTION_ID = 323456789;
    final static int CLOSED_QUESTION_ID = 333333333;
    private QuestionGateway questionGateway;
    private PostGateway postGateway;
    private PostFactory postFactory;
    private UserGatewayFactory userGatewayFactory;
    private ClientGateway clientGateway;
    private AttorneyGateway attorneyGateway;
    private HomePageOutputBoundary homePageOutputBoundary;
    private PostInputBoundary postInputBoundary;

    /**
     * Set up the test environment by initializing the ReplyUseCase instance.
     */
    public void setUpReplyUseCase(){

        questionGateway = new QuestionRepo();
        postGateway = new PostRepo();
        postFactory = new PostFactory();
        userGatewayFactory = new UserGatewayFactory();
        clientGateway = new ClientRepository();
        attorneyGateway = new AttorneyRepository();;
        homePageOutputBoundary = new HomePageOutputBoundary() {
            @Override
            public void setControlContainer(ControlContainer controlContainer) {

            }

            @Override
            public HomePageResponseModel prepareFail(String msg) {
                assertEquals("You cannot reply to this question", msg);
                return null;
            }

            @Override
            public HomePageResponseModel prepareSuccess(HomePageResponseModel homePageResponseModel) {
                return null;
            }
        };
        postInputBoundary = new ReplyInteractor(questionGateway, postGateway, homePageOutputBoundary, postFactory, userGatewayFactory);

        Question question = new Question();
        question.setQuestionId(QUESTION_ID);
        questionGateway.save(question);

        Question closedQuestion = new Question();
        closedQuestion.setQuestionId(CLOSED_QUESTION_ID);
        questionGateway.save(closedQuestion);

        Client client = new Client();
        client.setUserId(CLIENT_ID);
        client.setEmail("josephpc0612@gmail.com");
        client.addQuestion(question);
        clientGateway.save(client);

        Attorney attorney = new Attorney();
        attorney.setUserId(ATTORNEY_ID);
        attorney.setEmail("josephpc0612@gmail.com");
        attorneyGateway.save(attorney);

        Attorney secondAttorney = new Attorney();
        secondAttorney.setUserId(SECOND_ATTORNEY_ID);
        attorneyGateway.save(secondAttorney);
    }

    /**
     * Test client's reply to a question.
     */
    @Test
    public void testClientReply(){
        setUpReplyUseCase();
        PostRequestModel inputData1 = new PostRequestModel(QUESTION_ID, CLIENT_ID, "Test text");
        postInputBoundary.createPost(inputData1);
        Question question = questionGateway.get(QUESTION_ID);
        Post post1 = question.getPosts().get(0);
        assertEquals(post1.getBelongsTo(), CLIENT_ID);
        assertEquals(post1.getPostText(), "Test text");
        ClearAllRepository();
    }

    /**
     * Test attorney's first reply to a question.
     */
    @Test
    public void testAttorneyFirstReply(){
        setUpReplyUseCase();

        PostRequestModel inputData2 = new PostRequestModel(QUESTION_ID, ATTORNEY_ID, "Test text");
        postInputBoundary.createPost(inputData2);
        Question question = questionGateway.get(QUESTION_ID);
        Post post2 = question.getPosts().get(0);
        assertEquals(post2.getBelongsTo(), ATTORNEY_ID);
        assertEquals(post2.getPostText(), "Test text");
        User user = attorneyGateway.get(ATTORNEY_ID);
        Question attorneyQuestion = user.getQuestionsList().get(0);
        assertEquals(attorneyQuestion.getQuestionId(), QUESTION_ID);
        assertEquals(attorneyQuestion.getTakenByAttorney(), ATTORNEY_ID);
        assertEquals(attorneyQuestion.isTaken(), true);
        ClearAllRepository();
    }
    /**
     * Test attorney's follow-up reply to a question.
     */
    @Test
    public void testAttorneyFollowUp(){
        setUpReplyUseCase();
        PostRequestModel inputData = new PostRequestModel(QUESTION_ID, ATTORNEY_ID, "Test text");
        postInputBoundary.createPost(inputData);

        User user = attorneyGateway.get(ATTORNEY_ID);
        Question attorneyquestion = user.getQuestionsList().get(0);
        assertEquals(attorneyquestion.getQuestionId(), QUESTION_ID);
        assertEquals(attorneyquestion.getTakenByAttorney(), ATTORNEY_ID);
        assertEquals(attorneyquestion.isTaken(), true);

        PostRequestModel inputData2 = new PostRequestModel(QUESTION_ID, ATTORNEY_ID, "Test text2");
        postInputBoundary.createPost(inputData2);
        Question question = questionGateway.get(QUESTION_ID);
        Post post2 = question.getPosts().get(1);
        assertEquals(post2.getBelongsTo(), ATTORNEY_ID);
        assertEquals(post2.getPostText(), "Test text2");
        assertEquals(attorneyquestion.getQuestionId(), QUESTION_ID);
        assertEquals(attorneyquestion.getTakenByAttorney(), ATTORNEY_ID);
        assertEquals(attorneyquestion.isTaken(), true);
        ClearAllRepository();

    }
    @Test
    public void testFailToReplyQuestionClosed(){
        setUpReplyUseCase();
        PostRequestModel inputData = new PostRequestModel(CLOSED_QUESTION_ID, ATTORNEY_ID, "Test text");
        postInputBoundary.createPost(inputData);
        ClearAllRepository();
    }

    /**
     * Test failure to reply to a closed question.
     */
    @Test
    public void testAttorneyFailToReplyQuestionTakenByOther(){
        setUpReplyUseCase();
        PostRequestModel inputData = new PostRequestModel(QUESTION_ID, ATTORNEY_ID, "Test text");
        postInputBoundary.createPost(inputData);

        PostRequestModel inputData2 = new PostRequestModel(QUESTION_ID, SECOND_ATTORNEY_ID, "Test text");
        postInputBoundary.createPost(inputData2);

        User user = attorneyGateway.get(ATTORNEY_ID);
        Question attorneyquestion = user.getQuestionsList().get(0);
        assertEquals(attorneyquestion.getQuestionId(), QUESTION_ID);
        assertEquals(attorneyquestion.getTakenByAttorney(), ATTORNEY_ID);
        assertEquals(attorneyquestion.isTaken(), true);
        ClearAllRepository();
    }

    /**
     * Delete all data in clientGateway, questionGateway, attorneyGateway, postGateway.
     */
    public void ClearAllRepository(){
        questionGateway = new QuestionRepo();
        clientGateway = new ClientRepository();
        attorneyGateway = new AttorneyRepository();
        postGateway = new PostRepo();
        clientGateway.deleteAll();
        questionGateway.deleteAll();
        attorneyGateway.deleteAll();
        postGateway.deleteAll();
    }
}
