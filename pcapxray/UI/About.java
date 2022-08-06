package pcapxray.UI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

public class About {
    public static void showAbout(JFrame parentFrame) {
        // Create and set up the window.
        final JDialog frame = new JDialog(parentFrame, "About PcapXray 1.0", true);

        JPanel contentPanel = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(10, 30, 10, 30);
        contentPanel.setBorder(padding);
        frame.setContentPane(contentPanel);


        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StringBuilder sb = new StringBuilder(64);
        sb.append("<html><center><h2>PcapXray is Dope!</h2><br>")
                .append(" PcapXray is a simple but effective tool")
                .append(" for monitoring your network security and packets")
                .append(" Thanks to using PcapXray</center></html>");
        JLabel textLabel = new JLabel(
                sb.toString(),
                SwingConstants.CENTER);
        textLabel.setPreferredSize(new Dimension(400, 400));
        frame.getContentPane().add(textLabel, BorderLayout.NORTH);

        // Display the window.
        frame.setLocationRelativeTo(parentFrame);
        frame.pack();
        frame.setVisible(true);
    }
}
