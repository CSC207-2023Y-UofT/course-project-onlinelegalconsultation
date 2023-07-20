package askquestion;

import gateway.QuestionGateway;
import gateway.UserGateway;
import presenter.TheQuestionOutputBoundary;
import presenter.TheQuestionResponseModel;
import questionentities.Question;

import java.time.LocalDate;
import java.util.Random;

public class AskQuestionInteractor implements QuestionInputBoundary{
    final QuestionGateway questionGateway;
    final TheQuestionOutputBoundary theQuestionOutputBoundary;
    final QuestionFactory questionFactory;

    final UserGateway userGateway; // 需要client gateway， 且缺constructor

    public AskQuestionInteractor(QuestionGateway questionGateway, TheQuestionOutputBoundary theQuestionOutputBoundary,
                             QuestionFactory questionFactory){
        this.questionGateway = questionGateway;
        this.theQuestionOutputBoundary = theQuestionOutputBoundary;
        this.questionFactory = questionFactory;
    }

    public TheQuestionResponseModel createQuestion(QuestionRequestModel questionRequestModel){
        Random rand = new Random();
        int upperbound = 10000000;
        int int_random = rand.nextInt(upperbound);
        boolean ifExists = questionGateway.checkExistsByName(int_random);
        while (ifExists) {
            int_random = rand.nextInt(upperbound);
            ifExists = questionGateway.checkExistsByName(int_random);
        }
        LocalDate now = LocalDate.now();
        Question question = questionFactory.create(int_random, now, questionRequestModel.getAskedByClient(), questionRequestModel.getLegalDeadline());
        questionGateway.saveQuestion(question);

        TheQuestionResponseModel theQuestionResponseModel = new TheQuestionResponseModel();
        // 理论上说，response model里要放东西，然后prepareSuccess里面要放这个responseModel.
        return theQuestionOutputBoundary.prepareSuccess();
    }
}
