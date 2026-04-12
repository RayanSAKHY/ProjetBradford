package Bradford.CWW.GUI;

import Bradford.CWW.assets.UserData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import Bradford.CWW.Login;
import javafx.stage.Stage;
import Bradford.CWW.assets.UserDataSingleton;

public class Controller {
    public Button validateBtn;
    public Button loginBtn;
    public Button registerBtn;
    private int nbEssai = 0;
    private final UserData users;

    public Controller() {
        users = UserDataSingleton.getInstance();
    }

    @FXML
    private TextField nameField;

    @FXML
    private Label resultLabel;

    @FXML
    private TextField passwordField;


    @FXML
    private void handleLogin() {
        Login login = new Login(false);
        String username = nameField.getText();
        String password = passwordField.getText();
        boolean connected = login.loginJavaFX(username, password);
        if (connected) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MFA.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));
                stage.setTitle("Vérification MFA");

                MFAController controller = loader.getController();
                controller.setUser(users.getUser(username));

                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            nbEssai++;
            if (nbEssai > 3) {
                resultLabel.setText("Maximum attempts reached");
                closeWindow();
            }
            else {
                resultLabel.setText("Wrong Credentials. Attempts remaining: "+(3-nbEssai));
                nameField.clear();
                passwordField.clear();
            }
        }
    }


    @FXML
    private void closeWindow() {
        Stage stage = (Stage)  resultLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void register() {
        resultLabel.setText("");
        registerBtn.setDisable(true);
        loginBtn.setDisable(false);
        validateBtn.setDisable(false);
        validateBtn.setOnAction(e -> addUser());
        nbEssai = 0;
    }

    @FXML
    private void addUser() {
        resultLabel.setText("Please enter your username and password");
        String username = nameField.getText();
        String password = passwordField.getText();
        users.addUser(username, password);
        resultLabel.setText("User successfully added");
    }

    @FXML
    private void login() {
        resultLabel.setText("");
        registerBtn.setDisable(false);
        loginBtn.setDisable(true);
        validateBtn.setDisable(false);
        validateBtn.setOnAction(e -> handleLogin());
    }

}