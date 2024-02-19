import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Objects;

public class Engine {
    private final Controller controller;
    private final Keyboard gameKeyboard;
    private final Connection connection;
    private final Dictionary<Character, Integer> lettersOccurrencesDictionary;
    private final Dictionary<Character, String> plChars;
    private String answer;
    private String wordBuilder; //the word typed by user while guessing
    private int attempts;
    private int pointer;
    private boolean isGameOver;


    public Engine(Controller controller, Keyboard gameKeyboard){
        this.controller = controller;
        this.gameKeyboard = gameKeyboard;
        lettersOccurrencesDictionary = new Hashtable<>();
        attempts = 0;
        pointer = 0;
        wordBuilder = "";
        isGameOver = false;

//        polish special characters array
        plChars = new Hashtable<>();
        plChars.put('A', "Ą");
        plChars.put('C', "Ć");
        plChars.put('E', "Ę");
        plChars.put('L', "Ł");
        plChars.put('O', "Ó");
        plChars.put('S', "Ś");
        plChars.put('N', "Ń");
        plChars.put('Z', "Ż");
        plChars.put('X', "Ź");

        connection = new Connection();
        answer = drawAnswer();
    }

    private String drawAnswer() {
        answer = null;

        try {
            answer = connection.drawAnswer();

            System.out.println(answer);
        } catch (RuntimeException e) {
//            reconnect();
            drawAnswer();
        }

        return answer;
    }

//    private void reconnect() {
//        if (controller.showErrorAlert()) {
//            System.out.println("Reconnecting ...");
//
//            try {
//                connection.run();
//                System.out.println("Connected");
//            } catch (RuntimeException e) {
//                reconnect();
//            }
//        } else {
//            connection.closeConnection();
//            Platform.exit();
//            System.exit(0);
//        }
//    }

    public void addLetterToWordBuilder(KeyEvent event) {
        if (!isGameOver && wordBuilder.length() < 5 && event.getCode().isLetterKey()) {
            controller.setTileLetter(pointer, event.getText().toUpperCase());
            wordBuilder = wordBuilder + event.getText().toUpperCase();
            pointer++;
        }
    }

    public void addLetterToWordBuilder(String letter) {
        String plLetter = plChars.get(letter.toUpperCase().charAt(0));

        if (!isGameOver && wordBuilder.length() < 5 && (plLetter != null)) {
            controller.setTileLetter(pointer, plLetter);
            wordBuilder = wordBuilder + plLetter;
            pointer++;
        }
    }

    public void deleteLetterFromWordBuilder() { //delete letter - backspace
        if (!isGameOver && wordBuilder.length() > 0) {
            wordBuilder = wordBuilder.substring(0, wordBuilder.length() - 1);
            controller.setTileLetter(pointer - 1, "");
            pointer--;
        }
    }

    public void guess() {
        try {
            if (wordBuilder.length() == 5 && connection.checkWord(wordBuilder)) {
                boolean gameResult = Objects.equals(wordBuilder, answer); //check if the typed word is the correct answer

                checkPositionOfLetters(wordBuilder);

                wordBuilder = "";
                attempts++;

                if (gameResult || attempts > 5) {
                    isGameOver = true;

                    if (controller.showGameOverPopup(gameResult)) {
                        resetGame();
                    } else {
//                        connection.closeConnection();
                        Platform.exit();
                    }
                }
            }
        } catch (RuntimeException e) {
            System.out.println(e);
//            reconnect();
//            guess();
        }
    }

    private void countOccurrencesOfLetters(String word) {
        for (int i = 0; i < 5; i++) {
            char letter = word.charAt(i);
            int count = 0;

            for (int j = 0; j < 5; j++) {
                if (answer.charAt(j) == letter) { count++; }
            }

            lettersOccurrencesDictionary.put(letter, count);
        }
    }

    private void checkPositionOfLetters(String word) {
        String guessWord = word;
        countOccurrencesOfLetters(guessWord);

        //check only correct letters
        for (int i = 0; i < 5; i++) {
            char letter = guessWord.charAt(i);

            if (letter == answer.charAt(i)) {
                controller.setTileColor(attempts * 5 + i, "#009c24");
                guessWord = guessWord.substring(0, i) + "-" + guessWord.substring(i + 1);
                lettersOccurrencesDictionary.put(letter, lettersOccurrencesDictionary.get(letter) - 1);

                gameKeyboard.setLetterUsage(letter, 1);
            }
        }

        //check the other letters
        for (int i = 0; i < 5; i++) {
            char letter = guessWord.charAt(i);

            if (answer.contains(Character.toString(letter)) && lettersOccurrencesDictionary.get(letter) > 0) {
                controller.setTileColor(attempts * 5 + i, "#ffbf00");
                lettersOccurrencesDictionary.put(letter, lettersOccurrencesDictionary.get(letter) - 1);

                gameKeyboard.setLetterUsage(letter, 2);
            } else if (letter != '-'){
                controller.setTileColor(attempts * 5 + i, "#525252");

                gameKeyboard.setLetterUsage(letter, 3);
            }
        }

        controller.displayKeyboard();
    }

    private void resetGame() {
        answer = drawAnswer();

        attempts = 0;
        pointer = 0;
        wordBuilder = "";
        isGameOver = false;

        gameKeyboard.resetKeyboardDictionary();
        controller.displayKeyboard();
        controller.resetKeyboard();
    }
}
