package Bradford.CWW.MFA;

import Bradford.CWW.Input.UserInput;

import java.util.Scanner;
import java.util.function.Consumer;

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

    @Override
    public void TwoStepVerifAsync(Consumer<String> queue) {
        userInput.askInputAsync("Please enter your phone number to receive a code by SMS: ", new Consumer<String>() {
            @Override
            public void accept(String phone) {
                userInput.showMessage("send a code to "+phone);
                queue.accept("success");
            }
        });
    }
}
