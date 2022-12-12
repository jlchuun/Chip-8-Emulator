import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CPU {
    final static int START_ADDRESS = 0x200;
    final static int RAM = 4096;
    private int[] memory = new int[RAM];
    private int pc;     // program counter
    private int[] stack = new int[16];      // 16 registers for memory stack
    private int sp = 0;     // stack pointer

    private int delayTimer;
    private int soundTimer;

    private static final int[] FONT = {
            0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
            0x20, 0x60, 0x20, 0x20, 0x70, // 1
            0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
            0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
            0x90, 0x90, 0xF0, 0x10, 0x10, // 4
            0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
            0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
            0xF0, 0x10, 0x20, 0x40, 0x40, // 7
            0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
            0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
            0xF0, 0x90, 0xF0, 0x90, 0x90, // A
            0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
            0xF0, 0x80, 0x80, 0x80, 0xF0, // C
            0xE0, 0x90, 0x90, 0x90, 0xE0, // D
            0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
            0xF0, 0x80, 0xF0, 0x80, 0x80  // F
    };

    private void loadFont() {
        for (int i = 0; i < FONT.length; i++) {
            memory[i] = FONT[i];
        }
    }

    private void loadROM(String filename) {
        try {
            File romFile = new File(filename);
            byte[] romBytes = new byte[(int) romFile.length()];
            FileInputStream romStream = new FileInputStream(romFile);
            romStream.read(romBytes);
            romStream.close();

            for (int i = 0; i < romBytes.length; i++) {
                memory[i + START_ADDRESS] = romBytes[i];
            }

        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        } catch (IOException exc) {
            exc.printStackTrace();
        }

    }

}
