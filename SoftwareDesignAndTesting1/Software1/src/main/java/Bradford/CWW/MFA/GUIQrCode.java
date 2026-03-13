package Bradford.CWW.MFA;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUIQrCode {
    private final JFrame frame;
    private final JLabel qrCodeLabel;

    public GUIQrCode(String filepath) {

        frame = new JFrame("QRCode");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setTitle("QR Code");

        qrCodeLabel = new JLabel("Loading QRCode");
        frame.setSize(400, 400);
        frame.add(qrCodeLabel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        qrCodeLabel.setIcon(null);
        loadQrCode(filepath);


    }

    private void loadQrCode(String filepath){
        try {

            if (filepath == null || filepath.isEmpty()) {
                qrCodeLabel.setText("No QR code filepath provided");
                return;
            }
            File qrCode = new File(filepath);
            if (!qrCode.exists()) {
                qrCodeLabel.setText("No QR code file exits");
                return;
            }


            BufferedImage img = ImageIO.read(qrCode);
            ImageIcon qrCodeIcon = new ImageIcon(img);

            qrCodeLabel.setIcon(qrCodeIcon);
            qrCodeLabel.setText(null);
            frame.setLocationRelativeTo(null);
            frame.pack();
            frame.revalidate();
            frame.repaint();
        } catch (IOException e) {
            qrCodeLabel.setText("Error loading QR code");
        }
    }


    public void printQrCode() {
        frame.repaint();
        frame.setVisible(true);
    }

    public void updateQrCode(String filepath) {
        loadQrCode(filepath);
        frame.revalidate();
        frame.repaint();
    }

    public void end() {
        frame.dispose();
    }
}
