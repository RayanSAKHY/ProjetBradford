package Bradford.CWW;

import Bradford.CWW.MFA.*;

import javax.swing.*;
import java.util.function.Consumer;

public class MFALogin {
    private final IMFAStrategy strategy;

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

    public void twoStepVerifAsync(Consumer<Boolean> queue) {
        if (strategy != null) {
            strategy.TwoStepVerifAsync(result ->
                queue.accept("success".equals(result))
            );
        }
    }

}
