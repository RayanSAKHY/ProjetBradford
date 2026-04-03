package Bradford.CWW.MFA;

import Bradford.CWW.Input.UserInput;

import java.util.Scanner;
import java.util.function.Consumer;
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

    @Override
    public void TwoStepVerifAsync(Consumer<String> queue) {
        userInput.askInputAsync("Please enter your email to receive a code by email: ", new Consumer<String>() {
            @Override
            public void accept(String email) {
                userInput.showMessage("sent a code to "+email);
                queue.accept("success");
            }
        });
    }
}
