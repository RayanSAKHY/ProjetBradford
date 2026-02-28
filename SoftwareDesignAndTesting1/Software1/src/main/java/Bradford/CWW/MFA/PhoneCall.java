package Bradford.CWW.MFA;

import Bradford.CWW.asssets.User;

public class PhoneCall implements IMFAStrategy {

    @Override
    public boolean TwoStepVerif(User user) {
        System.out.println("call to "+user.getPhoneNumber());
        return true;
    }
}
