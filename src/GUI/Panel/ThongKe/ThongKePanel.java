package GUI.Panel;

import javax.swing.*;
import java.awt.*;

public class ThongKePanel extends JPanel {
    public ThongKePanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        // Ví dụ: thêm một tiêu đề
        JLabel title = new JLabel("THỐNG KÊ", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        add(title, BorderLayout.NORTH);

        // TODO: Thêm các thành phần thống kê khác tại đây
    }
}
