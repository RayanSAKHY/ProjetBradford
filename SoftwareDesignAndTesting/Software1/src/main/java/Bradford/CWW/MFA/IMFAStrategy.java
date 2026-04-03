package Bradford.CWW.MFA;

import java.util.function.Consumer;

public interface IMFAStrategy {
    public boolean TwoStepVerif();
    public void TwoStepVerifAsync(Consumer<String> queue);
}
