import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.awt.font.TextAttribute;

public class UserInterfaceController {

    private static final Color BG_BLACK = new Color(10, 10, 10);
    private static final Color PANEL_BLACK = new Color(18, 18, 18);
    private static final Color ACCENT_YELLOW = new Color(255, 214, 10);
    private static final Color TEXT_SOFT = new Color(240, 240, 240);
    private static final String CUSTOM_FONT_RESOURCE = "/fonts/HACKED.ttf";

    private final DriverService driverService;

    public UserInterfaceController(DriverService driverService) {
        this.driverService = driverService;
    }

    public void createAndShowUI() {
        Font uiBase = loadUiFont();

        JFrame frame = new JFrame("WATCHDOG CONTROL");
        frame.setMinimumSize(new Dimension(600, 300));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(BG_BLACK);

        JTextField urlField = new JTextField("https://www.animeunity.so/", 30);
        JButton startButton = new NeonButton("START");
        JButton stopButton = new NeonButton("STOP");
        stopButton.setVisible(false);

        JLabel statusLabel = new JLabel("STATUS: STOPPED");
        JLabel urlLabel = new JLabel("URL TARGET");

        urlLabel.setForeground(ACCENT_YELLOW);
        urlLabel.setFont(withTracking(uiBase.deriveFont(Font.BOLD, 16f), 0.08f));
        urlLabel.setHorizontalAlignment(SwingConstants.LEFT);

        statusLabel.setForeground(TEXT_SOFT);
        statusLabel.setFont(withTracking(uiBase.deriveFont(Font.PLAIN, 18f), 0.06f));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);

        styleTextField(urlField, uiBase);
        styleButton(startButton, uiBase);
        styleButton(stopButton, uiBase);

        startButton.addActionListener(e -> {
            String url = urlField.getText() == null ? "" : urlField.getText().trim();
            if (url.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "INSERT VALID URL", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
                urlField.setText(url);
            }

            try {
                driverService.startAutomation(url);
                statusLabel.setText("ON RUNNING");
                statusLabel.setForeground(ACCENT_YELLOW);
                startButton.setVisible(false);
                stopButton.setVisible(true);
            } catch (Exception ex) {
                driverService.stopAutomation();
                statusLabel.setText("ERROR");
                statusLabel.setForeground(new Color(255, 90, 90));
                startButton.setVisible(true);
                stopButton.setVisible(false);
                JOptionPane.showMessageDialog(frame, "Avvio fallito: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        stopButton.addActionListener(e -> {
            driverService.stopAutomation();
            statusLabel.setText("STATUS: STOPPED");
            statusLabel.setForeground(TEXT_SOFT);
            startButton.setVisible(true);
            stopButton.setVisible(false);
        });

        JPanel controls = new JPanel(new GridBagLayout());
        controls.setBackground(PANEL_BLACK);
        controls.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        controls.add(urlLabel, c);

        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 2;
        c.weightx = 1;
        controls.add(urlField, c);

        c.gridy = 2;
        c.gridwidth = 1;
        c.weightx = 0.5;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;

        c.gridx = 0;
        controls.add(startButton, c);

        c.gridx = 1;
        controls.add(stopButton, c);

        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setBackground(BG_BLACK);
        wrapper.setBorder(BorderFactory.createEmptyBorder(16, 16, 12, 16));

        wrapper.add(controls, BorderLayout.CENTER);
        wrapper.add(statusLabel, BorderLayout.SOUTH);

        frame.setLayout(new BorderLayout());
        frame.add(wrapper, BorderLayout.CENTER);
        frame.setSize(560, 250);
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                driverService.stopAutomation();
            }
        });

        frame.setVisible(true);
    }

    private static void styleTextField(JTextField field, Font uiBase) {
        field.setBackground(BG_BLACK);
        field.setForeground(TEXT_SOFT);
        field.setCaretColor(ACCENT_YELLOW);
        field.setSelectionColor(new Color(255, 214, 10, 90));
        field.setSelectedTextColor(TEXT_SOFT);
        field.setFont(withTracking(uiBase.deriveFont(Font.PLAIN, 24f), 0.06f));
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_YELLOW, 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        );
        field.setBorder(border);
    }

    private static void styleButton(JButton button, Font uiBase) {
        button.setBackground(new Color(0, 0, 0, 0));
        button.setForeground(new Color(255, 230, 120));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        button.setFont(withTracking(uiBase.deriveFont(Font.BOLD, 34f), 0.09f));
        button.setPreferredSize(new Dimension(170, 72));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setForeground(new Color(255, 250, 180));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(button.isEnabled() ? new Color(255, 230, 120) : new Color(130, 130, 130));
            }
        });
        button.setForeground(button.isEnabled() ? new Color(255, 230, 120) : new Color(130, 130, 130));
    }

    private static Font loadUiFont() {
        try (InputStream fontStream = UserInterfaceController.class.getResourceAsStream(CUSTOM_FONT_RESOURCE)) {
            if (fontStream != null) {
                Font custom = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(custom);
                return custom.deriveFont(Font.PLAIN, 13f);
            }
        } catch (Exception ignored) {
        }
        return new Font("Segoe UI", Font.PLAIN, 13);
    }

    private static Font withTracking(Font base, float tracking) {
        return base.deriveFont(java.util.Map.of(TextAttribute.TRACKING, tracking));
    }

    private static class NeonButton extends JButton {
        private NeonButton(String text) {
            super(text);
            setRolloverEnabled(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            String text = getText();
            FontMetrics fm = g2.getFontMetrics(getFont());
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

            Color glow = isEnabled()
                    ? (getModel().isRollover() ? new Color(255, 214, 10, 210) : new Color(255, 214, 10, 150))
                    : new Color(120, 120, 120, 120);
            Color core = isEnabled()
                    ? (getModel().isRollover() ? new Color(255, 252, 200) : new Color(255, 235, 140))
                    : new Color(145, 145, 145);

            for (int i = 6; i >= 2; i--) {
                g2.setColor(new Color(glow.getRed(), glow.getGreen(), glow.getBlue(), Math.max(18, glow.getAlpha() / (i + 1))));
                g2.drawString(text, x - 1, y + i / 3);
                g2.drawString(text, x + 1, y + i / 3);
            }
            g2.setColor(core);
            g2.drawString(text, x, y);
            g2.dispose();
        }
    }
}