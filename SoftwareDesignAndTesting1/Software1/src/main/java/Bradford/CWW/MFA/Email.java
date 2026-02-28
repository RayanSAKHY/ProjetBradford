package Bradford.CWW.MFA;

import Bradford.CWW.asssets.User;

public class Email implements Code {
    @Override
    public boolean TwoStepVerif(User user) {
        System.out.println("send a code to "+user.getEmail());
        return true;
    }
}
