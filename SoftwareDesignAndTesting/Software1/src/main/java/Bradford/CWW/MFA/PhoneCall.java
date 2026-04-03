package Bradford.CWW.MFA;

import Bradford.CWW.Input.UserInput;

import java.util.Scanner;
import java.util.function.Consumer;

public class PhoneCall implements IMFAStrategy {

    private final UserInput userInput;

    public PhoneCall(UserInput userInput) {
        this.userInput = userInput;
    }

    @Override
    public boolean TwoStepVerif() {
        String phoneNumber = userInput.askInput("Please enter your phone number to receive a call: ");
        userInput.showMessage("call to "+phoneNumber);
        return true;
    }

    @Override
    public void TwoStepVerifAsync(Consumer<String> queue) {
        userInput.askInputAsync("Please enter your phone number to receive a call: ", new Consumer<String>() {
            @Override
            public void accept(String phone) {
                userInput.showMessage("call to "+phone);
                queue.accept("success");
            }
        });
    }
}
