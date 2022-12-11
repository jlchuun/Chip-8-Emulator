import java.awt.event.*;
import java.lang.reflect.Field;

public class Keyboard implements KeyListener {
    public void keyTyped(KeyEvent e) {
        displayTest(e);
    }

    public void keyPressed(KeyEvent e) {
        displayTest(e);
    }

    public void keyReleased(KeyEvent e) {
        displayTest(e);
    }

    public void displayTest(KeyEvent e) {
//        int scancode = getKeyEventScancode(e);
        int id = e.getID();
        String keyString;
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

    public static int getKeyEventScancode(KeyEvent e) {
        int code;
        Field field;
        try {
            field = KeyEvent.class.getDeclaredField("scancode");
        } catch (NoSuchFieldException exc) {
            exc.printStackTrace();
            return -1;
        }

        try {
            field.setAccessible(true);
        } catch (SecurityException exc) {
            exc.printStackTrace();
            return -1;
        }

        try {
            code = (int) field.getLong(e);
        } catch (IllegalAccessException exc) {
            exc.printStackTrace();
            return -1;
        }
        return code;
    }
}
