package businessrule.usecase;


import businessrule.gateway.ClientGateway;
import businessrule.inputboundary.ClientRegisterInputBoundary;
import businessrule.outputboundary.RegisterOutputBoundary;
import businessrule.requestmodel.ClientRegisterRequestModel;
import businessrule.responsemodel.RegisterResponseModel;
import entity.Client;
import entity.ClientFactory;
import entity.CredentialChecker;
import entity.RandomNumberGenerator;

public class ClientRegisterInteractor implements ClientRegisterInputBoundary {
    final ClientGateway clientGateway;
    final RegisterOutputBoundary outputBoundary;
    final ClientFactory clientFactory;

    public ClientRegisterInteractor(ClientGateway clientGateway, RegisterOutputBoundary outputBoundary, ClientFactory clientFactory) {
        this.clientGateway = clientGateway;
        this.outputBoundary = outputBoundary;
        this.clientFactory = clientFactory;
    }

    @Override
    public RegisterResponseModel create(ClientRegisterRequestModel requestModel){
        // prepare input data
        String inputUserName = requestModel.getUserName();
        String inputEmail = requestModel.getEmail();
        String inputPassword1 = requestModel.getPassword();
        String inputPassword2 = requestModel.getPassword2();
        String inputStateAbb = requestModel.getStateAbb();
        String inputPostalCode = requestModel.getPostalCode();
        String inputEthnicity = requestModel.getEthnicity();
        int inputAge = requestModel.getAge();
        String inputGender = requestModel.getGender();
        String inputMaritalStatus = requestModel.getMaritalStatus();
        int inputNumberOfHousehold = requestModel.getNumberOfHousehold();
        float inputAnnualIncome = requestModel.getAnnualIncome();

        // validate input data
        CredentialChecker checker = new CredentialChecker();
        if (clientGateway.existsByName(inputUserName)){
            return outputBoundary.prepareFail("User name already exists");
        } else if (!inputPassword1.equals(inputPassword2)) {
            return outputBoundary.prepareFail("Passwords does not match");
        } else if (inputPassword1.length() < 8){
            return outputBoundary.prepareFail("Password is less than 8 characters");
        } else if (!checker.checkEmail(inputEmail)){
            return outputBoundary.prepareFail("Email is invalid");
        } else if (!checker.checkAge(inputAge)){
            return outputBoundary.prepareFail("Age is invalid");
        } else if (!checker.checkPostalCode(inputPostalCode)){
            return outputBoundary.prepareFail("Postal Code is invalid");
        }

        // create user id
        RandomNumberGenerator generator = new RandomNumberGenerator();
        int randomUserId = generator.generateClientId(8);
        boolean exists = clientGateway.existsById(randomUserId);
        while (exists){
            randomUserId = generator.generateClientId(8);
            exists = clientGateway.existsById(randomUserId);
        }

        // add user
        Client client = clientFactory.create(randomUserId, inputUserName, inputEmail,
                inputPassword1, inputStateAbb, inputPostalCode, inputEthnicity, inputAge,
                inputGender, inputMaritalStatus, inputNumberOfHousehold, inputAnnualIncome);
        clientGateway.addUser(client);
        String message = "Your userId is " + randomUserId;
        return outputBoundary.prepareSuccess(message);
    }
}