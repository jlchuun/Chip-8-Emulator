import java.io.*;
import java.util.Random;

public class CPU {
    final static int START_ADDRESS = 0x200;
    final static int RAM = 4096;
    private int[] memory = new int[RAM];
    private int pc;     // program counter
    private int[] v = new int[16];      // 16 v registers
    private int[] stack = new int[16];
    private int sp = 0;     // stack pointer
    private int index;      // index register
    private Display display;
    private Keyboard keyboard;
    private int delayTimer;
    private int soundTimer;
    private boolean drawFlag = false;
    private Random random;

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

    public CPU(Display display, Keyboard keyboard) {
        pc = START_ADDRESS;
        this.display = display;
        this.keyboard = keyboard;
        for (int i = 0; i < memory.length; i++) {
            memory[i] = 0;
        }
        for (int i = 0; i < stack.length; i++) {
            stack[i] = 0;
            v[i] = 0;
        }
        loadFont();
        index = 0;
        drawFlag = true;
        delayTimer = 0;
        soundTimer = 0;
        setRandom(new Random());
    }

    private void loadFont() {
        for (int i = 0; i < FONT.length; i++) {
            memory[i + 0x050] = FONT[i];
        }
    }

    public void loadROM(String filename) {
        try {
            File romFile = new File(filename);
            byte[] romBytes = new byte[(int) romFile.length()];
            DataInputStream romStream = new DataInputStream(new BufferedInputStream(new FileInputStream(romFile)));
            romStream.read(romBytes);
            romStream.close();

            for (int i = 0; i < romBytes.length; i++) {
                memory[i + START_ADDRESS] = romBytes[i] & 0xFF;
            }

        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public int[] getMemory() {
        return memory;
    }
    public Display getDisplay() {
        return display;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public int getSp() {
        return sp;
    }

    public int[] getStack() {
        return stack;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public int getPc() {
        return pc;
    }

    public int getIndex() {
        return index;
    }

    public int[] getRegisters() {
        return v;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public int getDelayTimer() {
        return delayTimer;
    }

    public int getSoundTimer() {
        return soundTimer;
    }

    public void setDelayTimer(int delay) {
        delayTimer = delay;
    }

    public void setSoundTimer(int sound) {
        soundTimer = sound;
    }

    public int fetchOpcode() {
        int opcode = (memory[pc] << 8) | (memory[pc + 1]);
        pc += 2;
        return opcode;
    }

    public boolean isDrawFlag() {
        return drawFlag;
    }

    public void setDrawFlag(boolean drawFlag) {
        this.drawFlag = drawFlag;
    }

    public void decodeOpcode(int opcode) {
        int vx;
        int vy;
        int x;
        int y;
        int height;
//        System.out.println(String.format("%04x", opcode));
        switch (opcode) {
            case 0x00E0:    // 00E0: clear screen
                display.clear();
                drawFlag = true;
                return;
            case 0x00EE:    // 00EE: return from subroutine
                sp--;
                pc = stack[sp];
                return;
        }
        switch (opcode & 0xF000) {
            case 0x1000:    // 1NNN: jump to NNN
                pc = opcode & 0x0FFF;
                return;
            case 0x2000:    // 2NNN: calls subroutine at NNN
                stack[sp] = pc;
                sp++;
                pc = opcode & 0x0FFF;
                return;
            case 0x3000:    // 3XNN: skip if VX == NN
                vx = (opcode & 0x0F00) >>> 8;
                if (v[vx] == (opcode & 0x00FF)) {
                    pc += 2;
                }
                return;
            case 0x4000:    // 4XNN: skip if VX != NN
                vx = (opcode & 0x0F00) >>> 8;
                if (v[vx] != (opcode & 0x00FF)) {
                    pc += 2;
                }
                return;
            case 0x5000:    // 5XY0: skip if VX == VY
                vx = (opcode & 0x0F00) >>> 8;
                vy = (opcode & 0x00F0) >>> 4;
                if (v[vx] == v[vy]) {
                    pc += 2;
                }
                return;
            case 0x6000:    // 6XNN: set VX = NN
                vx = (opcode & 0x0F00) >>> 8;
                v[vx] = (opcode & 0x00FF);
                return;
            case 0x7000:    // 7XNN: add NN to VX
                vx = (opcode & 0x0F00) >>> 8;
                v[vx] += (opcode & 0x00FF);
                v[vx] &= 0xFF;  // handle 8-bit overflow

                return;
            case 0x9000:    // 9XY0: skip if VX != VY
                vx = (opcode & 0x0F00) >>> 8;
                vy = (opcode & 0x00F0) >>> 4;
                if (v[vx] != v[vy]) {
                    pc += 2;
                }
                return;
            case 0xA000:    // ANNN: set index register to NNN
                index = opcode & 0x0FFF;
                return;
            case 0xB000:    // BNNN: jump with offset (v0 register)
                pc = (opcode & 0x0FFF) + v[0];
                return;
            case 0xC000:    // CXNN: VX = random number AND NN
                vx = (opcode & 0x0F00) >>> 8;
                v[vx] = (random.nextInt(256)) & (opcode & 0x00FF);
                return;
            case 0xD000:    // DXYN: display
                vx = (opcode & 0x0F00) >>> 8;
                vy = (opcode & 0x00F0) >>> 4;
                x = v[vx] % 64;
                y = v[vy] % 32;
                height = opcode & 0x000F;
                v[15] = 0;

                for (int row = 0; row < height; row++) {
                    int spriteByte = memory[index + row];
                    for (int col = 0; col < 8; col++) {
                        if ((spriteByte & (0x80 >>> col)) != 0) {
                            int xCoord = (x + col) % 64;
                            int yCoord = (y + row) % 32;
                            if (display.getPixel(xCoord, yCoord) == 1) {
                                v[15] = 1;
                            }
                            display.setPixel(xCoord, yCoord);
                        }
                    }
                }
                drawFlag = true;
                return;
        }
        switch (opcode & 0xF00F) {
            case 0x8000:    // 8XY0: VX = VY
                vx = (opcode & 0x0F00) >>> 8;
                vy = (opcode & 0x00F0) >> 4;
                v[vx] = v[vy];
                return;
            case 0x8001:    // 8XY1: VX = VX OR VY
                vx = (opcode & 0x0F00) >>> 8;
                vy = (opcode & 0x00F0) >> 4;
                v[vx] = v[vx] | v[vy];
                return;
            case 0x8002:    // 8XY2: VX = VX AND VY
                vx = (opcode & 0x0F00) >>> 8;
                vy = (opcode & 0x00F0) >> 4;
                v[vx] = v[vx] & v[vy];
                return;
            case 0x8003:    // 8XY3: VX = VX XOR VY
                vx = (opcode & 0x0F00) >>> 8;
                vy = (opcode & 0x00F0) >> 4;
                v[vx] = v[vx] ^ v[vy];
                return;
            case 0x8004:    // 8XY4: VX += VY
                vx = (opcode & 0x0F00) >>> 8;
                vy = (opcode & 0x00F0) >> 4;
                v[vx] += v[vy];
                if (v[vx] > 255) {
                    v[15] = 1;
                } else {
                    v[15] = 0;
                }
                v[vx] &= 0xFF;
                return;
            case 0x8005:    // 8XY5: VX -= VY
                vx = (opcode & 0x0F00) >>> 8;
                vy = (opcode & 0x00F0) >>> 4;
                v[vx] = (v[vx] - v[vy]) & 0xFF;
                if (v[vy] > v[vx]) {
                    v[15] = 0;
                } else {
                    v[15] = 1;
                }
                return;
            case 0x8006:    // 8XY6: shift right
                vx = (opcode & 0x0F00) >>> 8;
                vy = (opcode & 0x00F0) >>> 4;
                int temp = v[vx];
                v[vx] = v[vy];
                v[vx] >>= 1;
                v[15] = temp & 0x1;
                return;
            case 0x8007:    // 8XY7: VX = VY - VX
                vx = (opcode & 0x0F00) >>> 8;
                vy = (opcode & 0x00F0) >>> 4;
                v[vx] = (v[vy] - v[vx]) & 0xFF;
                if (v[vy] > v[vx]) {
                    v[15] = 1;
                } else {
                    v[15] = 0;
                }
                return;
            case 0x800E:    // 8XYE: shift left
                vx = (opcode & 0x0F00) >>> 8;
                vy = (opcode & 0x00F0) >> 4;
                v[vx] = v[vy];
                v[vx] = (v[vx] << 0x1) & 0xFF;
                v[15] = v[vx] >>> 7;
                return;
        }
        switch (opcode & 0XF0FF) {
            case 0xE09E:    // EX9E: skip if key press == VX
                vx = (opcode & 0x0F00) >>> 8;
                int key = v[vx];
                if (keyboard.getKeyStates()[key]) {
                    pc += 2;
                }
                return;
            case 0xE0A1:    // EXA1: skip if key press != VX
                vx = (opcode & 0x0F00) >>> 8;
                key = v[vx];
                if (!keyboard.getKeyStates()[key]) {
                    pc += 2;
                }
                return;
            case 0XF007:    // FX07: VX = delay timer
                vx = (opcode & 0x0F00) >>> 8;
                v[vx] = delayTimer;
                return;
            case 0XF015:    // FX15: delay timer = VX
                vx = (opcode & 0x0F00) >>> 8;
                delayTimer = v[vx];
                return;
            case 0XF018:    // FX18: sound timer  = VX
                vx = (opcode & 0x0F00) >>> 8;
                soundTimer = v[vx];
                return;
            case 0XF01E:    // FX1E: index register += VX
                vx = (opcode & 0x0F00) >>> 8;
                index += v[vx];
                return;
            case 0XF00A:    // FX0A: blocks and waits for key press and released
                vx = (opcode & 0x0F00) >>> 8;
                for (int i = 0; i < 16; i++) {
                    if (keyboard.getKeyStates()[i]) {
                        v[vx] = i;
                        return;
                    }
                }
                pc -= 2;
                return;
            case 0XF029:    // FX29: index register = hexadecimal char in VX
                vx = (opcode & 0x0F00) >>> 8;
                index = (v[vx] * 5) + 0x050;     // font start offset
                return;
            case 0XF033:    // FX33: binary-coded decimal conversion
                vx = (opcode & 0x0F00) >>> 8;
                int temp = v[vx];
                memory[index + 2] = temp % 10;
                temp /= 10;
                memory[index + 1] = temp % 10;
                temp /= 10;
                memory[index] = temp % 10;
                return;
            case 0XF055:    // FX55: store/load into memory
                vx = (opcode & 0x0F00) >>> 8;
                for (int i = 0; i <= vx; i++) {
                    memory[index + i] = v[i];
                }
                return;
            case 0XF065:    // FX65: store/load memory into var registers
                vx = (opcode & 0x0F00) >>> 8;
                for (int i = 0; i <= vx; i++) {
                    v[i] = memory[index + i];
                }
                return;
        }

    }
}
