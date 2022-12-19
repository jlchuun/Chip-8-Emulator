import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.PrintStream;
import java.util.List;

public class Chip8 {
    private Keyboard keyboard;
    private Display display;
    private CPU cpu;
    private int opcode;
    JFrame frame = new JFrame("CHIP-8 EMULATOR");
    private double updateRate = 1000000000 / 600d;

    public Chip8() {
        createGUI();
    }

    public void createGUI() {
        keyboard = new Keyboard();
        display = new Display();
        cpu = new CPU(display, keyboard);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(display, BorderLayout.WEST);
        frame.addKeyListener(keyboard);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void cycle() {
        int fps = 0;
        long lastTime = System.nanoTime();
        double nsRate = 1000000000d / 500;

        double delta = 0;
        long lastTimer = System.currentTimeMillis();
        while (true) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / nsRate;
            lastTime = currentTime;
            while (delta >= 1) {
                opcode = cpu.fetchOpcode();
                cpu.decodeOpcode(opcode);
                delta -= 1;

                if (cpu.getDelayTimer() > 0) {
                    cpu.setDelayTimer(cpu.getDelayTimer() - 1);
                }
                if (cpu.getSoundTimer() > 0) {
                    if (cpu.getSoundTimer() == 1) {
                        System.out.println("BEEEP");
                    }
                    cpu.setSoundTimer(cpu.getSoundTimer() - 1);
                }
            }
            if (cpu.isDrawFlag()) {
                display.rerender();
                fps++;
                cpu.setDrawFlag(false);
            }
            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                System.out.println(String.format("FPS: %d", fps));
                fps = 0;
            }

        }

    }
    public void loadRom() {
        JFileChooser fileChooser = new JFileChooser("./roms");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Chip-8 ROM", "ch8");
        fileChooser.setFileFilter(filter);
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            cpu.loadROM("roms/" + fileChooser.getSelectedFile().getName());
        } else {
            System.exit(0);
            frame.setVisible(false);
            frame.dispose();
        }

    }
    public static void main(String[] args) {
        Chip8 chip8 = new Chip8();
        chip8.loadRom();
        chip8.cycle();
    }
}

