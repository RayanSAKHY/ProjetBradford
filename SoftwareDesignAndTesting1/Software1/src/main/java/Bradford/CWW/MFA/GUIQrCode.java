package Bradford.CWW.MFA;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GUIQrCode {
    private final JFrame frame;
    private final JLabel qrCodeLabel;

    public GUIQrCode(String filepath) {

        frame = new JFrame("QRCode");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setTitle("QR Code");

        qrCodeLabel = new JLabel("Loading QRCode");
        frame.setSize(400, 400);
        frame.add(qrCodeLabel, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        loadQrCode(filepath);


    }

    private void loadQrCode(String filepath) {
        if (filepath == null || filepath.isEmpty()) {
            qrCodeLabel.setText("No QR code filepath provided");
            return;
        }
        File qrCode = new File(filepath);

        if (!qrCode.exists()) {
            qrCodeLabel.setText("No QR code file exits");
            return;
        }

        ImageIcon qrCodeIcon = new ImageIcon(qrCode.getPath());

        qrCodeLabel.setIcon(qrCodeIcon);
        qrCodeLabel.setText(null);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.revalidate();
        frame.repaint();
    }


    public void printQrCode() {
        frame.setVisible(true);
    }

    public void end() {
        frame.dispose();
    }
}
