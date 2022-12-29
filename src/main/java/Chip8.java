import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;


public class Chip8 {
    private Keyboard keyboard;
    private Display display;
    private CPU cpu;
    private int opcode;
    private int updateRate = 600;   // updates per second
    JFrame frame = new JFrame("CHIP-8 EMULATOR");

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
        long lastTime = System.nanoTime();
        double nsRate = 1000000000d / updateRate;
        double delta = 0;

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
                cpu.setDrawFlag(false);
            }
        }

    }

    public void setUpdateRate() {
        updateRate = Integer.parseInt(JOptionPane.showInputDialog("Enter update ticks per sec"));
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
        chip8.setUpdateRate();
        chip8.cycle();
    }
}

