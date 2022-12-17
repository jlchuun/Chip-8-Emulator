import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class Chip8 {
    private Keyboard keyboard;
    private Display display;
    private CPU cpu;
    private int delayTimer;
    private int soundTimer;
    private int opcode;
    JFrame frame = new JFrame("CHIP-8 EMULATOR");
    private double updateRate = 1 / 60;

    public Chip8() {
        createGUI();
    }

    public void createGUI() {
        keyboard = new Keyboard();
        display = new Display();
        cpu = new CPU(display, keyboard);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(display);
        frame.addKeyListener(keyboard);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void cycle() {
        double accumulator = 0;
        long currentTime, lastUpdate = System.currentTimeMillis();
        while (true) {
            currentTime = System.currentTimeMillis();
            accumulator += (currentTime - lastUpdate) / 1000;
            while (accumulator > updateRate) {
                opcode = cpu.fetchOpcode();
                cpu.decodeOpcode(opcode);
                if (cpu.isDrawFlag()) {
                    display.rerender();
                    cpu.setDrawFlag(false);
                }
                if (delayTimer > 0) {
                    cpu.setDelayTimer(cpu.getDelayTimer() - 1);
                }
                if (soundTimer > 0) {
                    cpu.setSoundTimer(cpu.getSoundTimer() - 1);
                }
                accumulator -= updateRate;
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
        }

    }
    public static void main(String[] args) {
        Chip8 chip8 = new Chip8();
        chip8.loadRom();
        chip8.cycle();
    }
}

