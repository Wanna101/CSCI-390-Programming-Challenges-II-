import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
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
 * - find similarities between movePiece and capturePiece to refactor into a function
 */


public class Chess {

    Piece[][] board = new Piece[8][8];

    public Chess() {
        setupBlackPieces();

        board[6][0] = Piece.P;
        board[6][1] = Piece.P;
        board[6][2] = Piece.P;
        board[6][3] = Piece.P;
        board[6][4] = Piece.P;
        board[6][5] = Piece.P;
        board[6][6] = Piece.P;
        board[6][7] = Piece.P;
        board[7][0] = Piece.R;
        board[7][1] = Piece.N;
        board[7][2] = Piece.B;
        //board[2][4] = Piece.B;
        board[7][3] = Piece.Q;
        board[7][4] = Piece.K;
        board[7][5] = Piece.B;
        board[7][6] = Piece.N;
        board[7][7] = Piece.R;

    }

    private void setupBlackPieces() {
        board[0][0] = Piece.r;
        board[0][1] = Piece.n;
        board[0][2] = Piece.b;
        //board[5][5] = Piece.b;
        board[0][2] = Piece.b;
        board[0][3] = Piece.q;
        board[0][4] = Piece.k;
        board[0][5] = Piece.b;
        board[0][6] = Piece.n;
        board[0][7] = Piece.r;
        board[1][0] = Piece.p;
        board[1][1] = Piece.p;
        board[1][2] = Piece.p;
        board[1][3] = Piece.p;
        board[1][4] = Piece.p;
        board[1][5] = Piece.p;
        board[1][6] = Piece.p;
        //board[5][5] = Piece.p;
        board[1][7] = Piece.p;
    }

    public void play() throws IOException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        Move mv = new Move(inputReader, board);
        while(!gameIsOver()) {
            printBoardToConsole();
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

    private void printBoardToConsole() {
        StringBuilder sb = new StringBuilder();
        int rankNum = 8;
        for (Piece[] rank : board) {
            sb.append(rankNum + " ");
            for(Piece piece : rank) {
                if(piece != null) {
                    sb.append(piece);
                } else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
            rankNum--;
        }
        sb.append("  abcdefgh");
        System.out.println(sb);
    }
}
