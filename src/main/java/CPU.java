import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.random.*;

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

    public void loadROM(String filename) {
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

    public int fetchOpcode() {
        int opcode = memory[pc] << 8 | memory[pc + 1];
        pc += 2;
        return opcode;
    }

    public void decodeOpcode(int opcode) {
        switch (opcode) {
            case 0x00E0:    // 00E0: clear screen
                return;
            case 0x00EE:    // 00EE: return from subroutine
                return;
        }
        switch (opcode & 0xF000) {
            case 0x1000:    // 1NNN: jump to NNN
                return;
            case 0x2000:    // 2NNN: calls subroutine at NNN
                return;
            case 0x3000:    // 3XNN: skip if VX == NN
                return;
            case 0x4000:    // 4XNN: skip if VX != NN
                return;
            case 0x5000:    // 5XY0: skip if VX == VY
                return;
            case 0x6000:    // 6XNN: set VX = NN
                return;
            case 0x7000:    // 7XNN: add NN to VX
                return;
            case 0x9000:    // 9XY0: skip if VX != VY
                return;
            case 0xA000:    // ANNN: set index register to NNN
                return;
            case 0xB000:    // BNNN: jump with offset (v0 register)
                return;
            case 0xC000:    // CXNN: VX = random number AND NN
                return;
            case 0xD000:    // DXYN: display
                return;
        }
        switch (opcode & 0xF00F) {
            case 0x8000:    // 8XY0: VX = VY
                return;
            case 0x8001:    // 8XY1: VX = VX OR VY
                return;
            case 0x8002:    // 8XY2: VX = VX AND VY
                return;
            case 0x8003:    // 8XY3: VX = VX XOR VY
                return;
            case 0x8004:    // 8XY4: VX += VY
                return;
            case 0x8005:    // 8XY5: VX -= VY
                return;
            case 0x8006:    // 8XY6: shift right
                return;
            case 0x8007:    // 8XY7: VX = VY - VX
                return;
            case 0x800E:    // 8XYE: shift left
                return;
        }
        switch (opcode & 0XF0FF) {
            case 0xE09E:    // EX9E: skip if key press == VX
                return;
            case 0xE0A1:    // EXA1: skip if key press != VX
                return;
            case 0XF007:    // FX07: VX = delay timer
                return;
            case 0XF015:    // FX15: delay timer = VX
                return;
            case 0XF018:    // FX18: sound timer  = VX
                return;
            case 0XF01E:    // FX1E: index register += VX
                return;
            case 0XF00A:    // FX0A: blocks and waits for key press and released
                return;
            case 0XF029:    // FX29: index register = hexadecimal char in VX
                return;
            case 0XF033:    // FX33: binary-coded decimal conversion
                return;
            case 0XF055:    // FX55: store/load into memory
                return;
            case 0XF065:    // FX65: store/load memory into var registers
                return;
        }

    }
}
