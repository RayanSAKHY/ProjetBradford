package Bradford.CWW.MFA;

import java.util.function.Consumer;

public interface IMFAStrategy {
    boolean TwoStepVerif();
    void TwoStepVerifAsync(Consumer<String> queue);
}
