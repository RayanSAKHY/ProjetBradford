package Bradford.CWW.MFA;

import Bradford.CWW.asssets.User;

public class AuthentificatorApp implements IMFAStrategy {

    @Override
    public boolean TwoStepVerif(User user) {
        System.out.println("Check the authentificator app");
        return true;
    }
}
