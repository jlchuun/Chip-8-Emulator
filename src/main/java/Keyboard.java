import java.awt.event.*;
import java.lang.reflect.Field;

public class Keyboard implements KeyListener {
    private boolean keyStates[] = new boolean[16];  // key states for 0-F

    public Keyboard() {
        for (int i = 0; i < keyStates.length; i++) {
            keyStates[i] = false;
        }
    }

    public void keyTyped(KeyEvent e) {
        displayTest(e);
    }


    public boolean[] getKeyStates() {
        return keyStates;
    }
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case (int)'1':
                keyStates[1] = true;
                return;
            case (int)'2':
                keyStates[2] = true;
                return;
            case (int)'3':
                keyStates[3] = true;
                return;
            case (int)'4':
                keyStates[0xC] = true;
                return;
            case (int)'q':
                keyStates[4] = true;
                return;
            case (int)'w':
                keyStates[5] = true;
                return;
            case (int)'e':
                keyStates[6] = true;
                return;
            case (int)'r':
                keyStates[0xD] = true;
                return;
            case (int)'a':
                keyStates[7] = true;
                return;
            case (int)'s':
                keyStates[8] = true;
                return;
            case (int)'d':
                keyStates[9] = true;
                return;
            case (int)'f':
                keyStates[0xE] = true;
                return;
            case (int)'z':
                keyStates[0xA] = true;
                return;
            case (int)'x':
                keyStates[0] = true;
                return;
            case (int)'c':
                keyStates[0xB] = true;
                return;
            case (int)'v':
                keyStates[0xF] = true;
                return;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyChar()) {
            case (int)'1':
                keyStates[1] = false;
                return;
            case (int)'2':
                keyStates[2] = false;
                return;
            case (int)'3':
                keyStates[3] = false;
                return;
            case (int)'4':
                keyStates[0xC] = false;
                return;
            case (int)'q':
                keyStates[4] = false;
                return;
            case (int)'w':
                keyStates[5] = false;
                return;
            case (int)'e':
                keyStates[6] = false;
                return;
            case (int)'r':
                keyStates[0xD] = false;
                return;
            case (int)'a':
                keyStates[7] = false;
                return;
            case (int)'s':
                keyStates[8] = false;
                return;
            case (int)'d':
                keyStates[9] = false;
                return;
            case (int)'f':
                keyStates[0xE] = false;
                return;
            case (int)'z':
                keyStates[0xA] = false;
                return;
            case (int)'x':
                keyStates[0] = false;
                return;
            case (int)'c':
                keyStates[0xB] = false;
                return;
            case (int)'v':
                keyStates[0xF] = false;
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
