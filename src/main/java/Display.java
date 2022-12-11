import javax.swing.*;
import java.awt.*;

public class Display extends JPanel {

    private static final int WIDTH = 64;
    private static final int HEIGHT = 32;
    private int scale = 12;
    private Graphics2D display;
    public int[][] pixels = new int[WIDTH][HEIGHT];

    public Display() {
        JFrame frame = new JFrame("Chip-8 Emulator");
        frame.setSize(400, 800);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(new Keyboard());
    }
    @Override
    public void paint(Graphics g) {
        display = (Graphics2D) g;
        display.setColor(Color.BLACK);
        display.fillRect(0, 0, 400, 800);
    }

    public void clear() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                pixels[x][y] = 0;
            }
        }
        render();
    }

    public void render() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (pixels[x][y] == 1) display.setColor(Color.BLACK);
                else display.setColor(Color.WHITE);
                display.fillRect(x * scale, y * scale, scale, scale);
            }
        }
    }

    public int getPixel(int x, int y) {
        return pixels[x][y];
    }

    public void setPixel(int x, int y) {
        pixels[x][y] ^= 1;
    }

    public static void main(String[] args) {
        Display d = new Display();
    }
}