import javax.swing.*;

public class Chip8 {
    private Keyboard keyboard;
    private Display display;
    private CPU cpu;
    private int delayTimer;
    private int soundTimer;
    private int opcode;
    private double updateRate = 1 / 60;

    public Chip8() {
        createGUI();
    }

    public void createGUI() {
        keyboard = new Keyboard();
        display = new Display();
        cpu = new CPU(display, keyboard);
        JFrame frame = new JFrame("CHIP-8 EMULATOR");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(display);
        frame.addKeyListener(keyboard);
        frame.pack();
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
    public void loadRom(String filename) {
        cpu.loadROM(filename);
    }
    public static void main(String[] args) {
        Chip8 chip8 = new Chip8();
        chip8.loadRom("roms/chip8-test-suite.ch8");
        chip8.cycle();
    }
}

