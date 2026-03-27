package Bradford.CWW;

import Bradford.CWW.MFA.*;

import javax.swing.*;

public class MFALogin {
    private IMFAStrategy strategy;

    public MFALogin(IMFAStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean twoStepVerif() {
        boolean result = false;

        if (strategy != null) {
            result = strategy.TwoStepVerif();
        }
        return result;
    }

}
