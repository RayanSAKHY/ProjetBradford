package Bradford.CWW.MFA;

import Bradford.CWW.asssets.User;

public class AppNotification implements IMFAStrategy {
    @Override
    public boolean TwoStepVerif(User user) {
        System.out.println("Check the app notification");
        return true;
    }
}
