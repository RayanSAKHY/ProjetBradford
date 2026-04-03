package Bradford.CWW.GUI;

import Bradford.CWW.Input.JavaFXInput;
import Bradford.CWW.Input.UserInput;
import Bradford.CWW.Login;
import Bradford.CWW.assets.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.function.Consumer;

public class MFAController {
    private User user;

    @FXML
    private TextArea console;

    @FXML
    private Button validate;
    @FXML
    private Label info;

    @FXML
    private TextField codeField;

    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/fondBlanc.png")).toExternalForm());
        imageView.setImage(image);
    }

    public void setUser(User user) {
        this.user = user;
    }


    @FXML
    private void verifyMFA(javafx.event.ActionEvent event) {
        javafx.scene.control.Button mfaBtn = (javafx.scene.control.Button) event.getSource();
        int choice = 0;


        switch (mfaBtn.getId()) {
            case "mfaBtn1":
                choice = 1;
                break;
            case "mfaBtn2":
                choice = 2;
                break;
            case "mfaBtn3":
                choice = 3;
                break;
            case "mfaBtn4":
                choice = 4;
                break;
            case "mfaBtn5":
                choice = 5;
                break;
            default:
                break;
        }

        //info.setText("choice: " + choice);
        UserInput userInput = new JavaFXInput(console,codeField,validate);
        Login login = new Login(userInput,false);

        //info.setText("result : " +login.loginJavaFX(user.getUsername(), user.getPassword()));
        login.loginMFAJavaFX(choice,user,new Consumer<Boolean>() {
            @Override
            public void accept(Boolean result) {
                if (result) {
                    info.setText("MFA successfully verified!");
                }
                else {
                    info.setText("MFA failed!");
                }
            }
        }, imageView);

        initialize();
    }
}
