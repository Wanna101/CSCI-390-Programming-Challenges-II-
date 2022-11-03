import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 10/27/22
 * Notes:
 * - Pieces that are working for capturePiece: Pawn, Rook, Knight, Bishop, Queen
 * - Refactored move parsing, movePiece, capturePiece, and smaller private functions that are
 * called within those functions
 * - Somewhat refactored movePiece to be simpler
 *
 * TODO from Week 9:
 * - finish refining capture piece to work out all the different cases
 *      - making sure a team cannot capture its own pieces
 * - have movePiece and capturePiece with one parameter
 * - find similarities between movePiece and capturePiece to refactor into multiple methods
 *
 * 11/3/22
 * Notes:
 * - moved initialization of pieces and board to a new class called Board
 * - created methods that take the place of some of the Move methods within the new Board class
 * - moves pieces from one square to another
 * - turned print function into toString
 *
 * TODO from Week 10:
 * - separate the I/O for the capture/move methods
 *
 */


public class Chess {

    Piece[][] boardOfPieces = new Piece[8][8];

    public Chess() {

    }

    public void play() throws IOException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        Board board = new Board(boardOfPieces);
        Move mv = new Move(inputReader, board);
        while(!gameIsOver()) {
            System.out.print(board.toString());
	    String move = inputReader.readLine();
	    mv.my_move(move);
        }
        System.out.println("Game over.");
        System.out.println("Thanks for playing!");
    }

    private boolean gameIsOver() {
        return isPositionCheckmate() || isPositionStalemate();
    } 

    private boolean isPositionStalemate() {
        return false;
    }

    private boolean isPositionCheckmate() {
        return false;
    }

}
