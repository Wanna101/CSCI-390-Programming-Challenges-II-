import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Chess game = new Chess();
        try {
            game.play();
        } catch (IOException e) {
            // Do nothing.
        }
    }
}
