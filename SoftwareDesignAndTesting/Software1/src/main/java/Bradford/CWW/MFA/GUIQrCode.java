package Bradford.CWW.MFA;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.File;

public class GUIQrCode{
    private Stage stage;
    private final ImageView imageView;
    private final Label label;

    public GUIQrCode(String filepath,ImageView imageView) {
        this.imageView = imageView;
        this.label = new Label("Loading QR Code");
        loadQrCode(filepath);
    }

    public GUIQrCode(String filepath) {
        this.imageView = new ImageView();
        this.label = new Label("Loading QR Code");

        stage = new Stage();
        stage.setTitle("QR Code");

        StackPane root = new StackPane(imageView, label);
        Scene scene = new Scene(root, 400, 400);

        stage.setScene(scene);

        loadQrCode(filepath);
    }

    private void loadQrCode(String filepath){
        try {
            if (filepath == null || filepath.isEmpty()) {
                label.setText("No QR code filepath provided");
                return;
            }
            File qrCode = new File(filepath);
            if (!qrCode.exists()) {
                label.setText("No QR code file exits");
                return;
            }

            Image img = new Image(qrCode.toURI().toString());

            imageView.setImage(img);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(300);

            label.setText(null);

        } catch (Exception e) {
            label.setText("Error loading QR code");
        }
    }

    public void updateQrCode(String filepath) {
        loadQrCode(filepath);
    }

    public void end() {
        if (stage != null) {
            stage.close();
        }
    }

}
