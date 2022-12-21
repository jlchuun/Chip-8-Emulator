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

    @Test
    public void subroutineTest() {

    }

    @Test
    public void skipTest() {

    }

    @Test
    public void setTest() {

    }

    @Test
    public void addTest() {

    }

    @Test
    public void logicTest() {   // 8000 logical/arithmetic instructions

    }

    @Test
    public void setIndexTest() {

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
