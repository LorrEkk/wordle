import java.util.*;
import java.util.Dictionary;

public class Keyboard { //class is responsible for the keyboard at the bottom of the game window
    //0-not used, 1-correct, 2-letter occurs in the answer but not in typed position, 3-used and not occurs in the answer
    Dictionary<Character, Integer> keyboardDictionary = new Hashtable<>();

    public Keyboard() {
        char[] letters = {'A', 'Ą', 'B', 'C', 'Ć', 'D', 'E', 'Ę', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'Ł', 'M', 'N', 'Ń', 'O', 'Ó', 'P', 'Q', 'R', 'S', 'Ś', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Ź', 'Ż'};

        for (char l : letters) {
            keyboardDictionary.put(l, 0);
        }
    }

    public void setLetterUsage(char letter, int usage) {
        int u = keyboardDictionary.get(letter);

        if (u == 0) {
            keyboardDictionary.put(letter, usage);
        } else if (u == 2 && usage == 1) {
            keyboardDictionary.put(letter, usage);
        }
    }

    public int getLetterUsage(char letter) {
        return keyboardDictionary.get(letter);
    }

    public void resetKeyboardDictionary() {
        Enumeration<Character> items = keyboardDictionary.keys();

        while (items.hasMoreElements()) {
            char key = items.nextElement();
            keyboardDictionary.put(key, 0);
        }
    }
}
