package Bradford.CWW;

import Bradford.CWW.MFA.*;
import Bradford.CWW.asssets.User;

import javax.swing.*;
import java.util.Scanner;

public class MFALogin {
    private IMFAStrategy strategy;

    public MFALogin(IMFAStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean twoStepVerif(User user) {
        boolean result = false;

        if (strategy != null) {
            result = strategy.TwoStepVerif(user);
        }
        return result;
    }

}
