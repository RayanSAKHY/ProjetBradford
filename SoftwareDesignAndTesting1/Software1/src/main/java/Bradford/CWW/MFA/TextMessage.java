package Bradford.CWW.MFA;

import Bradford.CWW.asssets.User;

public class TextMessage implements Code {
    @Override
    public boolean TwoStepVerif(User user) {
        System.out.println("send a code to "+user.getPhoneNumber());
        return true;
    }
}
