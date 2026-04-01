package Bradford.CWW.MFA;

import Bradford.CWW.Input.UserInput;

import java.util.Scanner;
//Source : https://stackoverflow.com/questions/884943/how-do-i-send-an-e-mail-in-java



public class CodeSentByEmail implements IMFAStrategy {
    private final UserInput userInput;

    public CodeSentByEmail(UserInput userInput) {
        this.userInput = userInput;
    }

    @Override
    public boolean TwoStepVerif() {
        String email = userInput.askInput("Please enter your email to receive a code by email: ");
        userInput.showMessage("sent a code to "+email) ;
        return true;
    }
}
