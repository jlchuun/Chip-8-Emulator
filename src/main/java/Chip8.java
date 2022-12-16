import javax.swing.*;

public class Chip8 {
    private Keyboard keyboard;
    private Display display;
    private CPU cpu;
    private int delayTimer;
    private int soundTimer;
    private int opcode;

    private int cpuFreq = 60;
    private int period = 1000 / cpuFreq;

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
        long now;
        int refreshCycles = 0;
        int i = 0;
        while (i <= 64) {
            now = System.currentTimeMillis();

            opcode = cpu.fetchOpcode();
            cpu.decodeOpcode(opcode);

            if (refreshCycles % (cpuFreq / 60) == 0) {
                refreshCycles = 0;
                if (cpu.isDrawFlag()) {
                    display.rerender();
                    cpu.setDrawFlag(false);
                }
                if (delayTimer > 0) {
                    delayTimer--;
                }
                if (soundTimer > 0) {
                    soundTimer--;
                }
            }
            refreshCycles++;

            long diff = System.currentTimeMillis() - now;
            while (diff < period) {
                try {
                    diff = System.currentTimeMillis() - now;
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }

    }
    public void loadRom(String filename) {
        cpu.loadROM(filename);
    }
    public static void main(String[] args) {
        Chip8 chip8 = new Chip8();
        chip8.loadRom("roms/IBM Logo.ch8");
        chip8.cycle();
    }
}

