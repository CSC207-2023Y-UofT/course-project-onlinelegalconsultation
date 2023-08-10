package gatewaytesting;

import businessrule.requestmodel.RegistrationData;
import driver.database.*;
import entity.Question;
import entity.factory.AttorneyFactory;
import entity.factory.ClientFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import entity.Attorney;
import entity.Client;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AttorneyRepositoryTest {

    //Attorney
    final static int ATTORNEY_ID = 50;
    final static String ATTORNEY_USERNAME = "yao";

    //Question
    final static int QUESTION_ID = 15;
    final static int QUESTION_ID1 = 25;
    final static int QUESTION_ID2 = 35;
    final static int ASKED_BY_CLIENT = 10;
    final static int ASKED_BY_CLIENT1 = 20;
    final static int ASKED_BY_CLIENT2 = 30;

    @BeforeAll
    public static void setUp() {
        //Attorney
        String attorneyEmail = "yao.yao@gmail.com";
        String attorneyPassword = "yao123321";
        String attorneyPassword2 = "yao123321";
        String attorneyState = "ON";
        String attorneyPostalCode = "M8MO1P";

        RegistrationData registrationData1 = new RegistrationData(ATTORNEY_USERNAME, attorneyEmail, attorneyPassword, attorneyPassword2, attorneyState, attorneyPostalCode);
        AttorneyFactory attorneyFactory = new AttorneyFactory();
        Attorney a = attorneyFactory.createUser(registrationData1);
        a.setUserId(ATTORNEY_ID);

        AttorneyRepository repo = new AttorneyRepository();

        repo.deleteAll();
        repo.save(a);

        //Client
        String clientUsername = "bob";
        String clientEmail = "bob.bob@gmail.com";
        String clientPassword = "bob123321";
        String clientPassword2 = "bob123321";
        String clientState = "ON";
        String clientPostalCode = "M1MA6A";
        String clientEthnicity = "asian";
        int clientAge = 20;
        String clientGender = "Male";
        String clientMaritalStatus = "Single";
        int clientNumHouseHold = 1;
        float clientAnnualIncome = 100;

        RegistrationData registrationData2 = new RegistrationData(clientUsername, clientEmail, clientPassword, clientPassword2, clientState, clientPostalCode, clientEthnicity, clientAge, clientGender, clientMaritalStatus, clientNumHouseHold, clientAnnualIncome);

        ClientFactory clientFactory = new ClientFactory();
        Client c  = clientFactory.createUser(registrationData2);
        ClientRepository cRepo = new ClientRepository();

        cRepo.deleteAll();
        cRepo.save(c);

        //Question
        String type = "Theft";
        String title = "hi";
        LocalDate createAt = LocalDate.now();
        LocalDate legalDeadLine = LocalDate.now();

        Question q = new Question(QUESTION_ID, type, title, createAt, ASKED_BY_CLIENT, legalDeadLine);
        Question q1 = new Question(QUESTION_ID1, type, title, createAt, ASKED_BY_CLIENT1, legalDeadLine);
        Question q2 = new Question(QUESTION_ID2, type, title, createAt, ASKED_BY_CLIENT2, legalDeadLine);

        QuestionRepo qRepo = new QuestionRepo();

        qRepo.deleteAll();
        q.setTakenByAttorney(ATTORNEY_ID);
        q2.setTakenByAttorney(ATTORNEY_ID);
        qRepo.save(q);
        qRepo.save(q1);
        qRepo.save(q2);
    }

    @Test
    public void testExistsById(){
        AttorneyRepository repo = new AttorneyRepository();
        //test id exists
        assertTrue(repo.existsById(ATTORNEY_ID), "The id does not exist!");
        //test id does not exist
        assertFalse(repo.existsById(75), "The id exists!");
    }

    @Test
    public void testExistsByName() {
        AttorneyRepository repo = new AttorneyRepository();
        //test username exists
        assertTrue(repo.existsByName(ATTORNEY_USERNAME), "The username does not exist!");
        //test username does not exist
        assertFalse(repo.existsByName("John"), "The username exists!");
    }

    @Test
    public void testUpdateQuestionList() {
        AttorneyRepository repo = new AttorneyRepository();
        QuestionRepo qRepo = new QuestionRepo();

        //test updating list
        repo.updateQuestionList(ATTORNEY_ID, qRepo.get(QUESTION_ID));
        ArrayList<Question> expectedList1 = new ArrayList<>();
        expectedList1.add(qRepo.get(QUESTION_ID));
        assert expectedList1.equals(repo.get(ATTORNEY_ID).getQuestionsList());
    }

    @AfterAll
    public static void tearDown() {
        AttorneyRepository repo = new AttorneyRepository();
        ClientRepository cRepo = new ClientRepository();
        QuestionRepo qRepo = new QuestionRepo();
        repo.deleteAll();
        cRepo.deleteAll();
        qRepo.deleteAll();
    }

    @Test
    public void testGetUser() {
        String attorneyUsername = "yao";
        String attorneyEmail = "yao.yao@gmail.com";
        String attorneyPassword = "yao123321";
        String attorneyPassword2 = "yao123321";
        String attorneyState = "ON";
        String attorneyPostalCode = "M8MO1P";

        //constructors
        RegistrationData registrationData1 = new RegistrationData(attorneyUsername, attorneyEmail, attorneyPassword, attorneyPassword2, attorneyState, attorneyPostalCode);
        AttorneyFactory attorneyFactory = new AttorneyFactory();
        Attorney a = attorneyFactory.createUser(registrationData1);
        a.setUserId(ATTORNEY_ID);
        AttorneyRepository repo = new AttorneyRepository();

        //set up
        repo.deleteAll();
        repo.save(a);

        //test get the correct attorney
        assertEquals(a, repo.get(ATTORNEY_ID), "That is not the correct client!");
    }

    @Test
    public void testAddUser() {
        int attorneyId = 50;
        String attorneyUsername = "yao";
        String attorneyEmail = "yao.yao@gmail.com";
        String attorneyPassword = "yao123321";
        String attorneyPassword2 = "yao123321";
        String attorneyState = "ON";
        String attorneyPostalCode = "M8MO1P";

        //constructors
        RegistrationData registrationData1 = new RegistrationData(attorneyUsername, attorneyEmail, attorneyPassword, attorneyPassword2, attorneyState, attorneyPostalCode);
        AttorneyFactory attorneyFactory = new AttorneyFactory();
        Attorney a = attorneyFactory.createUser(registrationData1);
        a.setUserId(attorneyId);
        AttorneyRepository repo = new AttorneyRepository();

        //set up
        repo.deleteAll();

        //test adding a attorney into the database
        repo.save(a);
        assertTrue(repo.existsById(attorneyId), "The attorney is not added!");
    }

    @Test
    public void testDeleteUser() {
        int attorneyId = 50;
        String attorneyUsername = "yao";
        String attorneyEmail = "yao.yao@gmail.com";
        String attorneyPassword = "yao123321";
        String attorneyPassword2 = "yao123321";
        String attorneyState = "ON";
        String attorneyPostalCode = "M8MO1P";

        //constructors
        RegistrationData registrationData1 = new RegistrationData(attorneyUsername, attorneyEmail, attorneyPassword, attorneyPassword2, attorneyState, attorneyPostalCode);
        AttorneyFactory attorneyFactory = new AttorneyFactory();
        Attorney a = attorneyFactory.createUser(registrationData1);
        a.setUserId(attorneyId);
        AttorneyRepository repo = new AttorneyRepository();

        //set up
        repo.deleteAll();
        repo.save(a);

        //test delete an existing attorney from the database
        assertTrue(repo.existsById(attorneyId), "The attorney was not added!");
        repo.delete(attorneyId);
        assertFalse(repo.existsById(attorneyId), "The attorney is still in the database!");

        //recover
        repo.save(a);
    }

    @Test
    public void testDeleteAllUser() {
        int attorneyId = 50;
        int attorneyId2 = 55;
        String attorneyUsername = "yao";
        String attorneyEmail = "yao.yao@gmail.com";
        String attorneyPassword = "yao123321";
        String attorneyPassword2 = "yao123321";
        String attorneyState = "ON";
        String attorneyPostalCode = "M8MO1P";

        //constructors
        RegistrationData registrationData1 = new RegistrationData(attorneyUsername, attorneyEmail, attorneyPassword, attorneyPassword2, attorneyState, attorneyPostalCode);
        AttorneyFactory attorneyFactory = new AttorneyFactory();
        Attorney a = attorneyFactory.createUser(registrationData1);
        a.setUserId(attorneyId);

        RegistrationData registrationData2 = new RegistrationData("joseph", attorneyEmail, attorneyPassword, attorneyPassword2, attorneyState, attorneyPostalCode);
        Attorney a1 = attorneyFactory.createUser(registrationData2);
        a.setUserId(attorneyId2);

        RegistrationData registrationData3 = new RegistrationData("bob", "bob.bob@gmail.com", "bob123321", "bob123321", "ON",
                "M1MA6A", "asian", 20, "Male", "Single", 1, 1000.0f);

        ClientFactory clientFactory = new ClientFactory();
        Client c  = clientFactory.createUser(registrationData3);
        c.setUserId(100);

        AttorneyRepository repo = new AttorneyRepository();
        ClientRepository clientRepo = new ClientRepository();

        //set up
        repo.deleteAll();
        clientRepo.deleteAll();
        repo.save(a);
        repo.save(a1);
        clientRepo.save(c);

        //test remove two users
        repo.deleteAll();
        assertFalse(repo.existsById(attorneyId), "Attorney a has not been removed!");
        assertFalse(repo.existsById(attorneyId2), "Attorney a1 has not been removed!");

        //set up
        repo.save(a);
        repo.save(a1);
        EntityManager em = DatabaseConnection.getEntityManager();

        //test remove all client
        repo.deleteAll();
        Long count = em.createQuery("SELECT COUNT(a) FROM Attorney a", Long.class).getSingleResult();
        assertEquals(0, count, "The database still has saved client objects!");
        //test client a still in database
        assertTrue(clientRepo.existsById(100), "client c is no longer in the database");
        //test remove empty
        repo.deleteAll();
        assertEquals(0, count, "The database still has saved client objects!");

        //recover
        repo.save(a);
        repo.save(a1);
    }

}
