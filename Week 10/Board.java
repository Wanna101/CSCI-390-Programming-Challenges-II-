import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Board {
    Piece[][] boardOfPieces;

    public Board(Piece[][] boardOfPieces) {
        this.boardOfPieces = boardOfPieces;

        // white pieces
        boardOfPieces[6][0] = Piece.P;
        boardOfPieces[6][1] = Piece.P;
        boardOfPieces[6][2] = Piece.P;
        boardOfPieces[6][3] = Piece.P;
        boardOfPieces[6][4] = Piece.P;
        boardOfPieces[6][5] = Piece.P;
        boardOfPieces[6][6] = Piece.P;
        boardOfPieces[6][7] = Piece.P;
        boardOfPieces[7][0] = Piece.R;
        boardOfPieces[7][1] = Piece.N;
        boardOfPieces[7][2] = Piece.B;
        boardOfPieces[7][3] = Piece.Q;
        boardOfPieces[7][4] = Piece.K;
        boardOfPieces[7][5] = Piece.B;
        boardOfPieces[7][6] = Piece.N;
        boardOfPieces[7][7] = Piece.R;

        // black pieces
        boardOfPieces[0][0] = Piece.r;
        boardOfPieces[0][1] = Piece.n;
        boardOfPieces[0][2] = Piece.b;
        boardOfPieces[0][2] = Piece.b;
        boardOfPieces[0][3] = Piece.q;
        boardOfPieces[0][4] = Piece.k;
        boardOfPieces[0][5] = Piece.b;
        boardOfPieces[0][6] = Piece.n;
        boardOfPieces[0][7] = Piece.r;
        boardOfPieces[1][0] = Piece.p;
        boardOfPieces[1][1] = Piece.p;
        boardOfPieces[1][2] = Piece.p;
        boardOfPieces[1][3] = Piece.p;
        boardOfPieces[1][4] = Piece.p;
        boardOfPieces[1][5] = Piece.p;
        boardOfPieces[1][6] = Piece.p;
        boardOfPieces[1][7] = Piece.p;
    }

   @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int rankNum = 8;
        for (Piece[] rank : boardOfPieces) {
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
        return sb.toString();
    }

    public Piece[][] getBoardOfPieces() {
        return boardOfPieces;
    }


    public void movePiece(Piece fromPiece, int toRankIndex, int toFileIndex, int fromRankIndex, int fromFileIndex) {
        // If we have gotten here, that means the move is valid and update the board position
        boardOfPieces[toRankIndex][toFileIndex] = fromPiece;
        boardOfPieces[fromRankIndex][fromFileIndex] = null;
    }
}
