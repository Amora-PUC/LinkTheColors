import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class TelaInicial extends JFrame {

    private static final int WIDTH = 480;
    private static final int HEIGHT = 640;

    public TelaInicial() {
        super("Link The Colors - Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        // Fundo cinza escuro
        main.setBackground(new Color(45, 45, 55));
        setContentPane(main);

        TitlePanel titlePanel = new TitlePanel();
        titlePanel.setPreferredSize(new Dimension(WIDTH, 320));
        main.add(titlePanel, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        main.add(center, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JButton jogarBtn = createStyledButton("JOGAR");
        JButton instrBtn = createStyledButton("REGRAS");
        JButton creditBtn = createStyledButton("CRÉDITOS");

        jogarBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "", "JOGAR",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        instrBtn.addActionListener(e -> {
            String msg = "Objetivo:\nLigue cada par de bolas da mesma cor sem que as linhas se cruzem.\n\n"
                    + "Controles:\nClique e arraste entre duas bolas para conectar.\n\n"
                    + "Dica:\nPlaneje rotas e use espaços vazios.";
            JOptionPane.showMessageDialog(this, msg, "Instruções", JOptionPane.INFORMATION_MESSAGE);
        });

        creditBtn.addActionListener(e -> {
            String msg = "Créditos:\nDesenvolvedor: GRUPO AGGL\nProjeto: Link The Colors\nVersão: 0.1";
            JOptionPane.showMessageDialog(this, msg, "Créditos", JOptionPane.INFORMATION_MESSAGE);
        });

        gbc.gridy = 0;
        buttonPanel.add(jogarBtn, gbc);
        gbc.gridy = 1;
        buttonPanel.add(instrBtn, gbc);
        gbc.gridy = 2;
        buttonPanel.add(creditBtn, gbc);

        main.add(buttonPanel, BorderLayout.SOUTH);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
        }
    }

    private JButton createStyledButton(String text) {
        // Fundo branco com texto rosa
        Color textColor = new Color(255, 20, 147); // DeepPink
        Color bg = Color.WHITE;
        JButton b = new JButton(text);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 20f));
        b.setForeground(textColor);
        b.setBackground(bg);
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(textColor, 2),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b.setBackground(new Color(255, 240, 245)); // Rosa bem claro no hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                b.setBackground(bg);
            }
        });

        return b;
    }

    private static class TitlePanel extends JPanel {
        public TitlePanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            Font titleFont = new Font("ComicSans", Font.BOLD, Math.max(28, h / 8));
            g2.setFont(titleFont);
            FontMetrics fm = g2.getFontMetrics();

            String s1 = "LINK";
            String s2 = "THE";
            String s3 = "COLORS";

            int space = fm.stringWidth(" ");
            int totalWidth = fm.stringWidth(s1) + space + fm.stringWidth(s2) + space + fm.stringWidth(s3);
            int x = (w - totalWidth) / 2;
            int y = fm.getAscent() + 8;

            g2.setColor(Color.CYAN);
            g2.drawString(s1, x, y);
            x += fm.stringWidth(s1) + space;

            g2.setColor(Color.PINK);
            g2.drawString(s2, x, y);
            x += fm.stringWidth(s2) + space;

            g2.setColor(Color.LIGHT_GRAY);
            g2.drawString(s3, x, y);

            int marginTop = y + 12;
            int areaTop = marginTop;
            int areaHeight = h - areaTop - 10;
            int areaCenterY = areaTop + areaHeight / 2;
            int areaCenterX = w / 2;

            float spacing = Math.min(160, w / 3f);
            Point2D.Float topRed = new Point2D.Float(areaCenterX, areaTop + 10f);
            Point2D.Float bottomRed = new Point2D.Float(areaCenterX, areaTop + areaHeight - 10f);
            Point2D.Float leftBlue = new Point2D.Float(areaCenterX - spacing, areaCenterY - 10f);
            Point2D.Float rightBlue = new Point2D.Float(areaCenterX + spacing, areaCenterY - 10f);
            Point2D.Float green1 = new Point2D.Float(areaCenterX - spacing / 2f, areaCenterY + 30f);
            Point2D.Float green2 = new Point2D.Float(areaCenterX + spacing / 2f, areaCenterY + 30f);

            g2.setStroke(new BasicStroke(10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(Color.BLUE.brighter());
            Path2D.Float pathBlue = new Path2D.Float();
            pathBlue.moveTo(leftBlue.x, leftBlue.y);
            pathBlue.curveTo(leftBlue.x + spacing * 0.25, leftBlue.y - spacing * 0.5f,
                    rightBlue.x - spacing * 0.25, rightBlue.y - spacing * 0.5f,
                    rightBlue.x, rightBlue.y);
            g2.draw(pathBlue);

            g2.setColor(Color.MAGENTA.darker());
            Path2D.Float pathPink = new Path2D.Float();
            pathPink.moveTo(rightBlue.x, rightBlue.y + 18);
            pathPink.curveTo(rightBlue.x - spacing * 0.3, rightBlue.y + spacing * 0.6f,
                    leftBlue.x + spacing * 0.3, leftBlue.y + spacing * 0.6f,
                    leftBlue.x, leftBlue.y + 18);
            g2.draw(pathPink);

            g2.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(Color.GREEN.darker());
            Path2D.Float pathGreen = new Path2D.Float();
            pathGreen.moveTo(green1.x, green1.y);
            pathGreen.curveTo(green1.x + 20, green1.y - 30,
                    green2.x - 20, green2.y - 30,
                    green2.x, green2.y);
            g2.draw(pathGreen);

            drawEndpoint(g2, topRed.x, topRed.y, Math.max(12, h / 28), Color.RED);
            drawEndpoint(g2, bottomRed.x, bottomRed.y, Math.max(12, h / 28), Color.RED);
            drawEndpoint(g2, leftBlue.x, leftBlue.y, Math.max(14, h / 26), Color.BLUE);
            drawEndpoint(g2, rightBlue.x, rightBlue.y, Math.max(14, h / 26), Color.BLUE);
            drawEndpoint(g2, green1.x, green1.y, Math.max(11, h / 30), Color.GREEN);
            drawEndpoint(g2, green2.x, green2.y, Math.max(11, h / 30), Color.GREEN);

            g2.dispose();
        }

        private void drawEndpoint(Graphics2D g2, float cx, float cy, int radius, Color color) {
            int r = radius;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.18f));
            g2.setColor(Color.BLACK);
            g2.fillOval(Math.round(cx - r) + 2, Math.round(cy - r) + 4, 2 * r, 2 * r);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            GradientPaint gp = new GradientPaint(cx - r, cy - r, color.brighter(),
                    cx + r, cy + r, color.darker());
            g2.setPaint(gp);
            g2.fillOval(Math.round(cx - r), Math.round(cy - r), 2 * r, 2 * r);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(color.darker().darker());
            g2.drawOval(Math.round(cx - r), Math.round(cy - r), 2 * r, 2 * r);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(480, 320);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaInicial tela = new TelaInicial();
            tela.setVisible(true);
        });
    }
}