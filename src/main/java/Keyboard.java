import java.awt.event.*;
import java.lang.reflect.Field;

public class Keyboard implements KeyListener {
    private boolean keyStates[] = new boolean[16];  // key states for 0-F
    public void keyTyped(KeyEvent e) {
        displayTest(e);
    }

    public boolean[] getKeyStates() {
        return keyStates;
    }
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case '1':
                keyStates[0] = true;
                return;
            case '2':
                keyStates[1] = true;
                return;
            case '3':
                keyStates[2] = true;
                return;
            case '4':
                keyStates[3] = true;
                return;
            case 'q':
                keyStates[4] = true;
                return;
            case 'w':
                keyStates[5] = true;
                return;
            case 'e':
                keyStates[6] = true;
                return;
            case 'r':
                keyStates[7] = true;
                return;
            case 'a':
                keyStates[8] = true;
                return;
            case 's':
                keyStates[9] = true;
                return;
            case 'd':
                keyStates[10] = true;
                return;
            case 'f':
                keyStates[11] = true;
                return;
            case 'z':
                keyStates[12] = true;
                return;
            case 'x':
                keyStates[13] = true;
                return;
            case 'c':
                keyStates[14] = true;
                return;
            case 'v':
                keyStates[15] = true;
                return;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyChar()) {
            case '1':
                keyStates[0] = false;
                return;
            case '2':
                keyStates[1] = false;
                return;
            case '3':
                keyStates[2] = false;
                return;
            case '4':
                keyStates[3] = false;
                return;
            case 'q':
                keyStates[4] = false;
                return;
            case 'w':
                keyStates[5] = false;
                return;
            case 'e':
                keyStates[6] = false;
                return;
            case 'r':
                keyStates[7] = false;
                return;
            case 'a':
                keyStates[8] = false;
                return;
            case 's':
                keyStates[9] = false;
                return;
            case 'd':
                keyStates[10] = false;
                return;
            case 'f':
                keyStates[11] = false;
                return;
            case 'z':
                keyStates[12] = false;
                return;
            case 'x':
                keyStates[13] = false;
                return;
            case 'c':
                keyStates[14] = false;
                return;
            case 'v':
                keyStates[15] = false;
                return;
        }
    }

    public void displayTest(KeyEvent e) {
        int id = e.getID();
        if (id == KeyEvent.KEY_TYPED) {
            char c = e.getKeyChar();
            System.out.println("key character = " + c);
        } else {
            int keyCode = e.getKeyCode();
            System.out.println("key code = " + keyCode
                    + " ("
                    + KeyEvent.getKeyText(keyCode)
                    + ")");
        }
    }
}
