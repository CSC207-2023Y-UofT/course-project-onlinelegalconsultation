package adapter.controller;

import businessrule.usecase.ViewRateableQuestionInteractor;

public class ControlContainer {
    BrowseQuestionControl browseQuestionControl;
    ClientRegisterControl clientRegisterControl;
    CloseQuestionControl closeQuestionControl;
    PostControl postControl;
    QuestionControl questionControl;
    RateControl rateControl;
    SelectQuestionControl selectQuestionControl;
    UserLoginControl userLoginControl;
    ViewQuestionControl viewQuestionControl;
    ViewRateableQuestionControl viewRateableQuestionControl;

    public ControlContainer(BrowseQuestionControl browseQuestionControl,
                            ClientRegisterControl clientRegisterControl,
                            CloseQuestionControl closeQuestionControl,
                            PostControl postControl, QuestionControl questionControl,
                            RateControl rateControl, SelectQuestionControl selectQuestionControl,
                            UserLoginControl userLoginControl, ViewQuestionControl viewQuestionControl,
                            ViewRateableQuestionControl viewRateableQuestionControl) {
        this.browseQuestionControl = browseQuestionControl;
        this.clientRegisterControl = clientRegisterControl;
        this.closeQuestionControl = closeQuestionControl;
        this.postControl = postControl;
        this.questionControl = questionControl;
        this.rateControl = rateControl;
        this.selectQuestionControl = selectQuestionControl;
        this.userLoginControl = userLoginControl;
        this.viewQuestionControl = viewQuestionControl;
        this.viewRateableQuestionControl = viewRateableQuestionControl;
    }

    public BrowseQuestionControl getBrowseQuestionControl() {
        return browseQuestionControl;
    }

    public ClientRegisterControl getClientRegisterControl() {
        return clientRegisterControl;
    }

    public CloseQuestionControl getCloseQuestionControl() {
        return closeQuestionControl;
    }

    public PostControl getPostControl() {
        return postControl;
    }

    public QuestionControl getQuestionControl() {
        return questionControl;
    }

    public RateControl getRateControl() {
        return rateControl;
    }

    public SelectQuestionControl getSelectQuestionControl() {
        return selectQuestionControl;
    }

    public UserLoginControl getUserLoginControl() {
        return userLoginControl;
    }

    public ViewQuestionControl getViewQuestionControl() {
        return viewQuestionControl;
    }

    public ViewRateableQuestionControl getViewRateableQuestionControl() {
        return viewRateableQuestionControl;
    }
}
