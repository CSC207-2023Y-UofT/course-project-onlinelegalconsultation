package controllertesting;

import adapter.controller.SelectQuestionControl;
import businessrule.SessionManager;
import businessrule.UserSession;
import businessrule.inputboundary.SelectInputBoundary;
import businessrule.requestmodel.SelectRequestModel;
import businessrule.responsemodel.TheQuestionResponseModel;

import businessrule.responsemodel.UserResponseModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SelectQuestionControlTest {
    private static int QUESTION_ID = 323456783;
    private static int USER_ID = 11233456;
    private static UserResponseModel expectedResponse;

    public void setUpSelectQuestionControl() {
        // Arrange
        expectedResponse = new UserResponseModel(USER_ID, "SampleUser", "SampleType"); // Assuming suitable constructor

        // Set up user session if needed
        UserResponseModel userResponseModel = new UserResponseModel(USER_ID, "SampleUser", "SampleType");
        UserSession userSession = new UserSession(userResponseModel);
        SessionManager.setSession(userSession);
    }

    @Test
    public void testSelectQuestion() {
        setUpSelectQuestionControl();

        SelectInputBoundary mockInputBoundary = mock(SelectInputBoundary.class);
        when(mockInputBoundary.selectQuestion(any(SelectRequestModel.class))).thenReturn(expectedResponse);

        SelectQuestionControl control = new SelectQuestionControl(mockInputBoundary);

        // Act
        UserResponseModel actualResponse = control.selectQuestion(QUESTION_ID);

        // Assert
        assertEquals(expectedResponse, actualResponse);

        // Verify interactions
        verify(mockInputBoundary, times(1)).selectQuestion(any(SelectRequestModel.class));
    }
}

