package Bradford.CWW.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MFAController {
    @FXML
    private TextArea console;

    @FXML
    private Label info;

    @FXML
    private TextField codeField;

    @FXML
    private ImageView imageView;
//
//    @FXML
//    public void initialize() {
//        Image image = new Image(getClass().getResource("/../java/Bradford/CWW/").toExternalForm());
//        imageView.setImage(image);
//    }

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


    }
}
