package GUI.Component;

import java.awt.*;
import javax.swing.*;


public class HeaderTitle extends JPanel {

    public HeaderTitle(String title) {
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 20f));
        this.add(lblTitle);
    }
}
