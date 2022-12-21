import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class CPUTest {
    // set up Display, Keyboard
    private final int WIDTH = 64;
    private final int HEIGHT = 32;
    private Keyboard keyboard;
    private Display display;
    private CPU cpu;

    @BeforeEach
    public void setUp() {
        keyboard = new Keyboard();
        display = new Display();
        cpu = new CPU(display, keyboard);
    }


    // OPCODE Tests
    @Test   // 00E0 clear screen
    public void clearTest() {

        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                cpu.getDisplay().setPixel(col, row);
                assumeTrue(cpu.getDisplay().getPixel(col, row) == 1);
            }
        }
        cpu.decodeOpcode(0x00e0);
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                assertEquals(cpu.getDisplay().getPixel(col, row), 0);
            }
        }
    }

    @Test   // 2nnn and 00ee call subroutine and return
    public void subroutineTest() {
        assumeTrue(cpu.getPc() == 0x200);
        assumeTrue(cpu.getSp() == 0);
        cpu.setPc(cpu.getPc() + 2);
        cpu.decodeOpcode(0x2123);
        assertEquals(cpu.getPc(), 0x123);
        assertEquals(cpu.getSp(), 1);

        cpu.decodeOpcode(0x00ee);
        assertEquals(cpu.getPc(), cpu.getStack()[cpu.getSp()]);
        assertEquals(cpu.getSp(), 0);
    }

    @Test   // 3xnn, 4xnn, 5xy0, 9xy0 skip instruction
    public void skipTest() {
        int testPc = cpu.getPc();
        assumeTrue(cpu.getPc() == 0x200);
        cpu.getRegisters()[1] = 0xab;
        cpu.getRegisters()[2] = 0x0f;
        cpu.getRegisters()[3] = 0x0f;

        // 3xnn success skip
        cpu.setPc(cpu.getPc() + 2);
        testPc = cpu.getPc();
        cpu.decodeOpcode(0x320f);
        assumeTrue(cpu.getRegisters()[2] == 0xf);
        assertEquals(testPc + 2, cpu.getPc());
        testPc = cpu.getPc();

        // 3xnn fail skip
        cpu.setPc(cpu.getPc() + 2);
        testPc = cpu.getPc();
        cpu.decodeOpcode(0x3212);
        assumeTrue(cpu.getRegisters()[2] == 0xf);
        assertEquals(testPc, cpu.getPc());
        testPc = cpu.getPc();

        // 4xnn success skip
        cpu.setPc(cpu.getPc() + 2);
        testPc = cpu.getPc();
        cpu.decodeOpcode(0x4212);
        assumeTrue(cpu.getRegisters()[2] == 0xf);
        assertEquals(testPc + 2, cpu.getPc());
        testPc = cpu.getPc();

        // 4xnn fail skip
        cpu.setPc(cpu.getPc() + 2);
        testPc = cpu.getPc();
        cpu.decodeOpcode(0x420f);
        assumeTrue(cpu.getRegisters()[2] == 0xf);
        assertEquals(testPc, cpu.getPc());
        testPc = cpu.getPc();

        // 5xy0 success skip
        cpu.setPc(cpu.getPc() + 2);
        testPc = cpu.getPc();
        cpu.decodeOpcode(0x5230);
        assumeTrue(cpu.getRegisters()[2] == cpu.getRegisters()[3]);
        assertEquals(testPc + 2, cpu.getPc());
        testPc = cpu.getPc();

        // 5xy0 fail skip
        cpu.setPc(cpu.getPc() + 2);
        testPc = cpu.getPc();
        cpu.decodeOpcode(0x5130);
        assumeTrue(cpu.getRegisters()[1] != cpu.getRegisters()[3]);
        assertEquals(testPc, cpu.getPc());
        testPc = cpu.getPc();

        // 9xy0 success skip
        cpu.setPc(cpu.getPc() + 2);
        testPc = cpu.getPc();
        cpu.decodeOpcode(0x9130);
        assumeTrue(cpu.getRegisters()[1] != cpu.getRegisters()[3]);
        assertEquals(testPc + 2, cpu.getPc());
        testPc = cpu.getPc();

        cpu.setPc(cpu.getPc() + 2);
        testPc = cpu.getPc();
        cpu.decodeOpcode(0x9230);
        assumeTrue(cpu.getRegisters()[2] == cpu.getRegisters()[3]);
        assertEquals(testPc, cpu.getPc());
        testPc = cpu.getPc();
    }

    @Test   // 8xy0, annn set instruction
    public void setTest() {
        cpu.getRegisters()[1] = 0xab;
        cpu.getRegisters()[2] = 0x0f;
        assumeTrue(cpu.getRegisters()[1] != cpu.getRegisters()[2]);

        // 8xy0 set vx = vy
        cpu.decodeOpcode(0x8120);
        assertEquals(cpu.getRegisters()[1], cpu.getRegisters()[2]);

        // annn set index register
        assumeTrue(cpu.getIndex() == 0);
        cpu.decodeOpcode(0xa114);
        assertEquals(0x114, cpu.getIndex());


    }

    @Test
    public void addTest() {

    }

    @Test
    public void logicTest() {   // 8000 logical/arithmetic instructions

    }

    @Test
    public void jumpTest() {

    }

    @Test
    public void randomTest() {

    }

    @Test
    public void displayTest() {

    }

    @Test
    public void skipIfTest() {

    }

    @Test
    public void timersTest() {

    }

    @Test
    public void addToIndexTest() {

    }

    @Test
    public void getKeyTest() {

    }

    @Test
    public void fontCharTest() {

    }

    @Test
    public void conversionTest() {

    }

    @Test
    public void storeLoadTest() {

    }
}
