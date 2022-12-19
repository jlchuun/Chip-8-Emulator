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
        double accumulator = 0;
        long timer = 0;
        long currentTime, lastUpdate = System.nanoTime();
        while (true) {
            currentTime = System.nanoTime();
            accumulator += (currentTime - lastUpdate) / updateRate;
            timer += (currentTime - lastUpdate);
            lastUpdate = currentTime;
            while (accumulator >= 1) {
                opcode = cpu.fetchOpcode();
                fps++;
                cpu.decodeOpcode(opcode);
                if (cpu.isDrawFlag()) {
                    display.rerender();
                    cpu.setDrawFlag(false);
                }
                if (cpu.getDelayTimer() > 0) {
                    cpu.setDelayTimer(cpu.getDelayTimer() - 1);
                }
                if (cpu.getSoundTimer() > 0) {
                    cpu.setSoundTimer(cpu.getSoundTimer() - 1);
                }
                accumulator--;
            }
            if (timer >= 1000000000) {
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

