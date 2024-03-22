import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RoundedButton extends JButton {

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setForeground(Color.white);
        setBorder(new EmptyBorder(10, 20, 10, 20));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isArmed()) {
            g2d.setColor(getBackground().darker());
        } else {
            g2d.setColor(getBackground());
        }

        // Draw a rounded rectangle
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        super.paintComponent(g2d);

        g2d.dispose();
    }
}