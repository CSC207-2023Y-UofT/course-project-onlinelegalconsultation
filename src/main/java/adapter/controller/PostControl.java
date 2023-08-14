package adapter.controller;

import businessrule.inputboundary.PostInputBoundary;
import businessrule.requestmodel.PostRequestModel;
import businessrule.responsemodel.TheQuestionResponseModel;
import businessrule.responsemodel.UserResponseModel;

public class PostControl {
    private final PostInputBoundary postInput;

    public PostControl(PostInputBoundary postInput){
        this.postInput = postInput;
    }

    public TheQuestionResponseModel createPost(int questionId, String postText){
        PostRequestModel postRequestModel = new PostRequestModel(questionId, postText);

        return postInput.createPost(postRequestModel);
    }
}
