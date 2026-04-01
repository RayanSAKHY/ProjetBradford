package Bradford.CWW.MFA;

import Bradford.CWW.Input.UserInput;

import java.util.Scanner;

public class CodeSentBySMS implements IMFAStrategy {
    private final UserInput userInput;

    public CodeSentBySMS(UserInput userInput) {
        this.userInput = userInput;
    }

    @Override
    public boolean TwoStepVerif() {
        String phoneNumber = userInput.askInput("Please enter your phone number to receive a code by SMS: ");
        userInput.showMessage("send a code to "+phoneNumber);
        return true;
    }
}
