package GUI.Component.Chart.BarChart;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;

public class Chart extends JPanel {
    private ArrayList<ModelChart> data = new ArrayList<>();
    private String chartTitle = "";
    private Color[] colors = {
            new Color(47, 79, 79),
            new Color(151, 255, 255),
            new Color(141, 238, 238),
            new Color(121, 205, 205),
            new Color(82, 139, 139),
            new Color(108, 123, 139),
            new Color(159, 182, 205),
            new Color(185, 211, 238)
    };

    public Chart() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 20));
    }

    public void addData(ModelChart chartData) {
        data.add(chartData);
        repaint();
    }

    public void clear() {
        data.clear();
        repaint();
    }
    public void setChartTitle(String title) {
        this.chartTitle = title;
        repaint(); // Đảm bảo tiêu đề được vẽ lại
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data.isEmpty()) return;
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        int padding = 60;
        int barGap = 20;
        int labelPadding = 40;
        int maxBarHeight = height - padding * 2;
        int barAreaWidth = width - padding * 2 - labelPadding;
       // Vẽ tiêu đề biểu đồ
        if (chartTitle != null && !chartTitle.isEmpty()) {
            Font originalFont = g2d.getFont(); // Lưu font hiện tại
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(chartTitle);
            g2d.setColor(Color.BLACK);
            g2d.drawString(chartTitle, (width - titleWidth) / 2, 30); // Vẽ ở giữa phía trên
            g2d.setFont(originalFont); // Phục hồi font cũ
        }

        // Tìm giá trị lớn nhất
        double maxValue = data.stream()
                .flatMapToDouble(chart -> java.util.Arrays.stream(chart.getValues()))
                .max()
                .orElse(1);

        // Làm tròn maxValue lên bội số gần nhất của 5 để trục y không bị lặp
        maxValue = Math.ceil(maxValue / 5.0) * 5;

        // Vẽ trục y
        int yAxisX = padding + labelPadding - 20; // Dịch sang trái
        g2d.setColor(Color.BLACK);
        g2d.drawLine(yAxisX, padding, yAxisX, height - padding);

        // Chia các mức cho trục y
        int yTickCount = 5;
        double yTickStep = maxValue / yTickCount;
        DecimalFormat df = new DecimalFormat("#,###");

        for (int i = 0; i <= yTickCount; i++) {
            int y = height - padding - (int) (maxBarHeight * (i * yTickStep / maxValue));
            g2d.drawLine(yAxisX - 5, y, yAxisX, y);
            String label = df.format(Math.round(i * yTickStep));
            int labelWidth = g2d.getFontMetrics().stringWidth(label);
            g2d.drawString(label, yAxisX - labelWidth - 10, y + 5);
        }

        // Vẽ các cột
        int x = yAxisX + 30;
        int barWidth = (barAreaWidth - barGap * (data.size() - 1)) / data.size();

        for (int i = 0; i < data.size(); i++) {
            ModelChart chart = data.get(i);
            double[] values = chart.getValues();
            int barHeight = (int) (maxBarHeight * (values[0] / maxValue));
            int y = height - padding - barHeight;

            g2d.setColor(colors[i % colors.length]);
            g2d.fillRect(x, y, barWidth, barHeight);

            // Vẽ nhãn bên dưới cột (đã sửa)
            g2d.setColor(Color.BLACK);
            String label = chart.getLabel();
            FontMetrics fm = g2d.getFontMetrics();
            int lineHeight = fm.getHeight();

            // Tách label thành các dòng không vượt quá barWidth và ngắt tại dấu cách
            List<String> lines = new ArrayList<>();
            StringBuilder currentLine = new StringBuilder();
            for (String word : label.split(" ")) {
                if (fm.stringWidth(currentLine + word) > barWidth && currentLine.length() > 0) {
                    lines.add(currentLine.toString().trim());
                    currentLine = new StringBuilder(word).append(" ");
                } else {
                    currentLine.append(word).append(" ");
                }
            }
            if (currentLine.length() > 0) {
                lines.add(currentLine.toString().trim());
            }

            // Vẽ từng dòng, căn giữa toàn bộ block nhãn theo chiều ngang
            int labelY = height - padding + 15;
            for (String line : lines) {
                int labelWidth = fm.stringWidth(line);
                int labelX = x + (barWidth - labelWidth) / 2;
                g2d.drawString(line, labelX, labelY);
                labelY += lineHeight;
            }

            // Di chuyển sang cột tiếp theo
            x += barWidth + barGap;
        }
    }
    
}