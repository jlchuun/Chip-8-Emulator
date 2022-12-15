import javax.swing.*;
import java.awt.*;

public class Display extends JPanel {
    private Graphics g;
    private final int scale = 15;
    private final int WIDTH = 64;
    private final int HEIGHT = 32;

    private final int[][] pixels = new int[WIDTH][HEIGHT];

    public Dimension getPreferredSize() {
        return new Dimension(WIDTH * scale, HEIGHT * scale);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g = g;

        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH * scale, HEIGHT * scale);

        render();
    }

    public void render() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (pixels[x][y] == 1)
                    g.setColor(Color.WHITE);
                else
                    g.setColor(Color.BLACK);
                g.fillRect(x * scale, y * scale, scale, scale);
            }
        }
    }

    public void rerender() {
        repaint();
    }

    public void setPixel(int x, int y) {
        pixels[x][y] ^= 1;
    }

    public int getPixel(int x, int y) {
        return pixels[x][y];
    }
    public void clear() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                pixels[x][y] = 0;
            }
        }
    }

    public static void main(String[] args) {
        Display display = new Display();
        JFrame frame = new JFrame("CHIP-8 EMULATOR");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(display);
        frame.pack();
        frame.setVisible(true);
        for (int i = 0; i < 64 * 15; i++) {
           display.setPixel(i, 16);
        }
    }

}
