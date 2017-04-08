package raycasting;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.xml.ws.Holder;

public class Main {
    static enum Direction {
        NONE, UP, RIGHT, DOWN, LEFT; //declare directions in clockwise direction
    }
    public static void main(String[] args) {
        Main main = new Main();
        SwingUtilities.invokeLater(main::createUI);
    }
    
    private void createUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Ray Casting Demo");
        frame.setSize(800, 600);
        Holder<Point> mousePos = new Holder<>(new Point());
        Holder<Point> playerPos = new Holder<>(new Point(frame.getWidth() / 2, frame.getHeight() / 2));
        int blockNum = 5;
        int blockWidth = 50;
        Font font = new Font("Verdana", Font.PLAIN, 16);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x1 = playerPos.value.x;
                int x2 = mousePos.value.x;
                int y1 = playerPos.value.y;
                int y2 = mousePos.value.y;
                Direction dir = Direction.NONE;
                try {
                    //angle-finding code from http://wikicode.wikidot.com/get-angle-of-line-between-two-points
                    int angle = (int) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
                    if (inRange(angle, -60, 60)) {
                        dir = Direction.RIGHT;
                    }
                    else if (inRange(angle, 60, 150)) {
                        dir = Direction.DOWN;
                    }
                    else if (inRange(angle, 120, 180) || inRange(angle, -180, -150)) {
                        dir = Direction.LEFT;
                    }
                    else if (inRange(angle, -150, -60)) {
                        dir = Direction.UP;
                    }
                }
                catch (Exception e) {
                    
                }
                Graphics2D g2D = (Graphics2D) g.create();
                if (Point.distance(x1, y1, x2, y2) > blockNum * blockWidth) {
                    g2D.setColor(Color.ORANGE);
                }
                else {
                    g2D.setColor(Color.RED);
                }
                g2D.setFont(font);
                g2D.drawLine(playerPos.value.x, playerPos.value.y, mousePos.value.x, mousePos.value.y);
                if (g2D.getColor() != Color.RED) {
                    g2D.setColor(Color.RED);
                }
                g2D.drawString("dir: " + dir, 5, 15);
                g2D.dispose();
            }
        };
        panel.setBackground(Color.BLACK);
        panel.setSize(frame.getSize());
        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mousePos.value.setLocation(e.getX(), e.getY());
                panel.repaint();
            }
        });
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        frame.setVisible(false);
                        System.exit(0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        playerPos.value.x += 1;
                        break;
                    case KeyEvent.VK_LEFT:
                        playerPos.value.x -= 1;
                        break;
                    case KeyEvent.VK_UP:
                        playerPos.value.y -= 1;
                        break;
                    case KeyEvent.VK_DOWN:
                        playerPos.value.y += 1;
                        break;
                    default:
                        break;
                }
                playerPos.value.setLocation(clamp(playerPos.value.x, 0, panel.getWidth()), clamp(playerPos.value.y, 0, panel.getHeight()));
                panel.repaint();
            }
        });
        frame.setContentPane(panel);
        frame.setVisible(true);
    }
    
    public static int clamp(int num, int min, int max) {
        return num < min ? min : num > max ? max : num;
    }
    
    public static boolean inRange(int x, int min, int max, boolean exclusive) {
        return exclusive ? x > min && x < max : x >= min && x <= max;
    }
    
    public static boolean inRange(int x, int min, int max) {
        return inRange(x, min, max, false);
    }
}
