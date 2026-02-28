package Bradford.CWW.MFA;
import Bradford.CWW.asssets.User;

public interface IMFAStrategy {
    public boolean TwoStepVerif(User user);
}
