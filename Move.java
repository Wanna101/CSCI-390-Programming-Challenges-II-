import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Move {
    // TODO: Homework - Is this really the chess game's responsibility to do the move conversion?
    //                  Refactor this code to make it the responsibility of a different class.
    //       We'll be doing a simpler notation for our chess game. Notation will be a 5 or 6 character length. Form
    //       will take the shape of {a-h}{1-8}(\-x}{a-h}{1-8}{rnbqRNBQ}.
    //       First character is the from file
    //       Second character is the from rank
    //       Third character is - for move, x for capture
    //       Forth character is the to file
    //       Fifth character is the to rank
    //       Sixth character is the promotion of a pawn to a piece type. This is optional
    BufferedReader inputReader;
    boolean playerTurnIsWhite;
    Board board;

    public Move(BufferedReader inputReader, Board board) {
        this.board = board;
	    this.inputReader = inputReader;
        this.playerTurnIsWhite = true;
    }

    Pattern movePattern;
    Matcher moveMatcher;
    Square fromSquare;
    boolean capture;
    int toFileIndex;
    int toRankIndex;
    String pawnPromotionPiece;

    public void my_move(String move) { 
	    //move = inputReader.readLine();
	    movePattern = Pattern.compile("^[a-h][1-8][-x][a-h][1-8][rnbqRNBQ]{0,1}");

	    moveMatcher = movePattern.matcher(move);
	    if(!moveMatcher.find()) {
		    //Move is invalid;
		    System.out.println("Move is invalid. Please input a valid move.");
		    return;
	    }
	    fromSquare = new Square();
	    fromSquare.setFileIndex(calcFileIndex(move.charAt(0)));
	    fromSquare.setRankIndex(calcRankIndex(Integer.valueOf(move.substring(1,2))));
	    capture = (move.charAt(2) == 'x');
	    toFileIndex = calcFileIndex(move.charAt(3));
	    toRankIndex = calcRankIndex(Integer.valueOf(move.substring(4,5)));

	    pawnPromotionPiece = null;
	    if(move.length() == 6) {
		    pawnPromotionPiece = move.substring(5,6);
	    }
	    if(!capture) {
		    movePiece(fromSquare, toFileIndex, toRankIndex, pawnPromotionPiece);
	    } else {
		    capturePiece(fromSquare.getFileIndex(), fromSquare.getRankIndex(), toFileIndex, toRankIndex, pawnPromotionPiece);
	    }
	    // We are not going to worry about special moves like castling and en passant
    } 

    private void movePiece(Square fromSquare, int toFileIndex, int toRankIndex, String pawnPromotionPiece) {
        // letter
        int fromFileIndex = fromSquare.getFileIndex();
        // number
        int fromRankIndex = fromSquare.getRankIndex();
        Piece fromPiece = board.getBoardOfPieces()[fromRankIndex][fromFileIndex];

        if(fromPiece == null) {
            System.out.println("Select a square with a piece.");
            return;
        }

        if (correctPlayerNotMovingTheirPiece(fromPiece)) return;

        //Validate Piece Movement
        if(fromPiece.toString().equalsIgnoreCase("n")) {
            if (!((Math.abs(fromFileIndex - toFileIndex) == 2 && Math.abs(fromRankIndex - toRankIndex) == 1) || (Math.abs(fromFileIndex - toFileIndex) == 1 && Math.abs(fromRankIndex - toRankIndex) == 2))) {
                System.out.println("Invalid move for Knight.");
                return;
            }
        } else if(fromPiece.toString().equalsIgnoreCase("r")) {
            validateRookMove(toFileIndex, toRankIndex, fromFileIndex, fromRankIndex);
        } else if(fromPiece.toString().equalsIgnoreCase("b")) {
            validateBishopMove(toFileIndex, toRankIndex, fromFileIndex, fromRankIndex);
        } else if (fromPiece.toString().equalsIgnoreCase("q")) {
            validateRookMove(toFileIndex, toRankIndex, fromFileIndex, fromRankIndex);
            validateBishopMove(toFileIndex, toRankIndex, fromFileIndex, fromRankIndex);
        } else if(fromPiece.toString().equalsIgnoreCase("k")) {
            validateKingMove(toFileIndex, toRankIndex, fromFileIndex, fromRankIndex);
        } else if (fromPiece.toString().equalsIgnoreCase("p")) {
            if(fromFileIndex != toFileIndex) {
                System.out.println("Cannot create valid path for Pawn.");
                return;
            }
            if(playerTurnIsWhite) {
                if(fromRankIndex == 6) {
                    int rankDelta = fromRankIndex - toRankIndex;
                    if(rankDelta > 2 || rankDelta < 1) {
                        System.out.println("Cannot create valid path for Pawn.");
                        return;
                    } else if (rankDelta == 1) {
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
                            System.out.println("Cannot create valid path for Pawn.");
                            return;
                        }
                    } else if (rankDelta == 2) {
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null || board.getBoardOfPieces()[toRankIndex -1][toFileIndex] != null) {
                            System.out.println("Cannot create valid path for Pawn.");
                            return;
                        }
                    }
                } else {
                    int rankDelta = fromRankIndex - toRankIndex;
                    if(rankDelta != 1) {
                        System.out.println("Cannot create valid path for Pawn.");
                        return;
                    } else {
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
                            System.out.println("Cannot create valid path for Pawn.");
                            return;
                        }
                    }
                }
            } else {
                if(fromRankIndex == 1) {
                    int rankDelta = fromRankIndex - toRankIndex;
                    if(rankDelta < -2 || rankDelta > -1) {
                        System.out.println("Cannot create valid path for Pawn.");
                        return;
                    } else if (rankDelta == -1) {
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
                            System.out.println("Cannot create valid path for Pawn.");
                            return;
                        }
                    } else if (rankDelta == -2) {
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null || board.getBoardOfPieces()[toRankIndex +1][toFileIndex] != null) {
                            System.out.println("Cannot create valid path for Pawn.");
                            return;
                        }
                    }
                } else {
                    int rankDelta = fromRankIndex - toRankIndex;
                    if(rankDelta != -1) {
                        System.out.println("Cannot create valid path for Pawn.");
                        return;
                    } else {
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
                            System.out.println("Cannot create valid path for Pawn.");
                            return;
                        }
                    }
                }
            }
        }

        //Handle the promotion of a pawn.
        if(fromPiece.toString().equalsIgnoreCase("p")) {
            if(playerTurnIsWhite && toRankIndex == 0) {
                if(pawnPromotionPiece == null) {
                    System.out.println("Pawn Promotion Piece must be specified for this pawn move.");
                    return;
                }
                if(!pawnPromotionPiece.toUpperCase().equals(pawnPromotionPiece)) {
                    System.out.println("Pawn Promotion Piece must be for White. Input should be uppercase.");
                    return;
                }
                fromPiece = Piece.valueOf(pawnPromotionPiece);
            } else if(!playerTurnIsWhite && toRankIndex == 7) {
                if(pawnPromotionPiece == null) {
                    System.out.println("Pawn Promotion Piece must be specified for this pawn move.");
                    return;
                }
                if(!pawnPromotionPiece.toLowerCase().equals(pawnPromotionPiece)) {
                    System.out.println("Pawn Promotion Piece must be for Black. Input should be lowercase.");
                    return;
                }
                fromPiece = Piece.valueOf(pawnPromotionPiece);
            }
        }

        // move piece to new location
        board.movePiece(fromPiece, toRankIndex, toFileIndex, fromRankIndex, fromFileIndex);

        //Change the player's turn
        playerTurnIsWhite = !playerTurnIsWhite;
    }

    private void validateRookMove(int toFileIndex, int toRankIndex, int fromFileIndex, int fromRankIndex) {
        if(fromFileIndex == toFileIndex && toRankIndex == fromRankIndex) {
            System.out.println("Rook must move at least 1 square.");
            return;
        } else if(fromFileIndex == toFileIndex) {
            if(toRankIndex > fromRankIndex) {
                for(int i = fromRankIndex +1; i<= toRankIndex; i++) {
                    if(board.getBoardOfPieces()[i][fromFileIndex] != null) {
                        System.out.println("Cannot create valid path for Rook.");
                        return;
                    }
                }
            } else {
                for(int i = fromRankIndex -1; i>= toRankIndex; i--) {
                    if(board.getBoardOfPieces()[i][fromFileIndex] != null) {
                        System.out.println("Cannot create valid path for Rook.");
                        return;
                    }
                }
            }
        } else if(fromRankIndex == toRankIndex){
            if(toFileIndex > fromFileIndex) {
                for(int i = fromFileIndex +1; i<= toFileIndex; i++) {
                    if(board.getBoardOfPieces()[fromRankIndex][i] != null) {
                        System.out.println("Cannot create valid path for Rook.");
                        return;
                    }
                }
            } else {
                for(int i = fromFileIndex -1; i>= fromFileIndex; i--) {
                    if(board.getBoardOfPieces()[fromRankIndex][i] != null) {
                        System.out.println("Cannot create valid path for Rook.");
                        return;
                    }
                }
            }
        } else {
            System.out.println("Cannot create valid path for Rook.");
            return;
        }
    }
    private void validateBishopMove(int toFileIndex, int toRankIndex, int fromFileIndex, int fromRankIndex) {
        if(fromFileIndex == toFileIndex || toRankIndex == fromRankIndex) {
            System.out.println("Cannot create valid path for Bishop.");
            return;
        } else if(Math.abs(fromFileIndex- toFileIndex) != Math.abs(fromRankIndex- toRankIndex)) {
            System.out.println("Cannot create valid path for Bishop.");
            return;
        } else {
            if(fromFileIndex < toFileIndex && fromRankIndex < toRankIndex) {
                for(int i = 1; i <= toFileIndex -fromFileIndex; i++) {
                    if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
                        System.out.println("Cannot create valid path for Bishop.");
                        return;
                    }
                }
            } else if(fromFileIndex < toFileIndex && fromRankIndex > toRankIndex) {
                for(int i = 1; i <= toFileIndex -fromFileIndex; i++) {
                    if(board.getBoardOfPieces()[toRankIndex][toFileIndex+i] != null) {
                        System.out.println("Cannot create valid path for Bishop.");
                        return;
                    }
                }
            } else if(fromFileIndex > toFileIndex && fromRankIndex > toRankIndex) {
                for(int i = 1; i <= fromFileIndex- toFileIndex; i++) {
                    if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
                        System.out.println("Cannot create valid path for Bishop.");
                        return;
                    }
                }
            } else if(fromFileIndex > toFileIndex && fromRankIndex < toRankIndex) {
                for(int i = 1; i <= fromFileIndex- toFileIndex; i++) {
                    if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
                        System.out.println("Cannot create valid path for Bishop.");
                        return;
                    }
                }
            }
        }
    }

    private void validateKingMove(int toFileIndex, int toRankIndex, int fromFileIndex, int fromRankIndex) {
        if(fromFileIndex == toFileIndex && toRankIndex == fromRankIndex) {
            System.out.println("Cannot create valid path for King.");
            return;
        } else if (Math.abs(fromFileIndex- toFileIndex) > 1) {
            System.out.println("Cannot create valid path for King.");
            return;
        } else if (Math.abs(fromRankIndex- toRankIndex) > 1) {
            System.out.println("Cannot create valid path for King.");
            return;
        } else if (board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
            System.out.println("Cannot create valid path for King.");
            return;
        }
    }
    private boolean correctPlayerNotMovingTheirPiece(Piece fromPiece) {
        //Check that the piece is owned by the correct player.
        if(playerTurnIsWhite) {
            if(fromPiece.toString().toLowerCase() == fromPiece.toString()) {
                System.out.println("Select a square with a white piece.");
                return true;
            }
        } else {
            if(fromPiece.toString().toUpperCase() == fromPiece.toString()) {
                System.out.println("Select a square with a black piece.");
                return true;
            }
        }
        return false;
    }

    // TODO: Homework - Refactor this method to use a single parameter

    private void capturePiece(int fromFileIndex, int fromRankIndex, int toFileIndex, int toRankIndex, String pawnPromotionPiece) {
        // TODO: Homework - Create capture logic when a piece is capturing another piece
        //           Remember: Pieces can only capture opposing pieces
        //                     Pawns can only capture diagonally in front of them
        //                     We are not worrying about en passant. This is just the simple and basic moves.
        //                     Use inspiration from the move method. Think about what can be refactored.
        //                     Extract method is your friend.

        Piece fromPiece = board.getBoardOfPieces()[fromRankIndex][fromFileIndex];

        if(fromPiece == null) {
            System.out.println("Select a square with a piece.");
            return;
        }

        if (correctPlayerNotMovingTheirPiece(fromPiece)) return;

        //Validate Piece Movement
        if(fromPiece.toString().equalsIgnoreCase("n")) {
            if (!((Math.abs(fromFileIndex - toFileIndex) == 2 && Math.abs(fromRankIndex - toRankIndex) == 1) || (Math.abs(fromFileIndex - toFileIndex) == 1 && Math.abs(fromRankIndex - toRankIndex) == 2))) {
                System.out.println("Invalid move for Knight.");
                return;
            }
        } else if(fromPiece.toString().equalsIgnoreCase("r")) {
            validateRookMove(toFileIndex, toRankIndex, fromFileIndex, fromRankIndex);
        } else if(fromPiece.toString().equalsIgnoreCase("b")) {
            if(fromFileIndex == toFileIndex || toRankIndex == fromRankIndex) {
                System.out.println("Cannot create valid path for Bishop.");
                return;
            } else if(Math.abs(fromFileIndex- toFileIndex) != Math.abs(fromRankIndex- toRankIndex)) {
                System.out.println("Cannot create valid path for Bishop.");
                return;
            } else {
                if(fromFileIndex < toFileIndex && fromRankIndex < toRankIndex) {
		            int capture_count = 0;
                    for(int i = 1; i <= toFileIndex -fromFileIndex; i++) {
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
                            capture_count++;
                            //System.out.println("Cannot create valid path for Bishop.");
                            //return;
                        }
			            else if(capture_count != 1) {
                            System.out.println("Cannot create valid path for Bishop.");
                            return;
			            }
                    }
                } else if(fromFileIndex < toFileIndex && fromRankIndex > toRankIndex) {
		            int capture_count = 0;
                    for(int i = 1; i <= toFileIndex -fromFileIndex; i++) {
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
			                capture_count++;
                            //System.out.println("Cannot create valid path for Bishop.");
                            //return;
                        }
                        //System.out.println("capture_count: " + capture_count);
			            else if(capture_count != 1) {
                            System.out.println("Cannot create valid path for Bishop.");
                            return;
			            }
                    }
                } else if(fromFileIndex > toFileIndex && fromRankIndex > toRankIndex) {
                    int capture_count = 0;
                    for(int i = 1; i <= fromFileIndex- toFileIndex; i++) {
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
                            capture_count++;
                            //System.out.println("Cannot create valid path for Bishop.");
                            //return;
                        }
                        //System.out.println("capture_count:" + capture_count);
                        else if(capture_count != 1) {
                            System.out.println("Cannot create valid path for Bishop.");
                            return;
                        }
                    }
                } else if(fromFileIndex > toFileIndex && fromRankIndex < toRankIndex) {
                    int capture_count = 0;
                    for(int i = 1; i <= fromFileIndex- toFileIndex; i++) {
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
                            capture_count++;
                            //System.out.println("Cannot create valid path for Bishop.");
                            //return;
                        }
                        else if(capture_count != 1) {
                            System.out.println("Cannot create valid path for Bishop.");
                            return;
                        }
                    }
                }
            }
        } else if (fromPiece.toString().equalsIgnoreCase("q")) {
            validateRookMove(toFileIndex, toRankIndex, fromFileIndex, fromRankIndex);
            if (Math.abs(fromFileIndex- toFileIndex) != Math.abs(fromRankIndex- toRankIndex)) {
                System.out.println("HERE5");
                System.out.println("Cannot create valid path for Queen.");
                return;
            } else {
                if (fromFileIndex < toFileIndex && fromRankIndex < toRankIndex) {
                    int capture_count = 0;
                    for (int i = 1; i <= toFileIndex - fromFileIndex; i++) {
                        if (board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
                            capture_count++;
                            System.out.println("HERE6");
                            //System.out.println("Cannot create valid path for Queen.");
                            //return;
                        }
                        else if(capture_count != 1) {
                            System.out.println("6");
                            System.out.println("Cannot create valid path for Queen.");
                            return;
                        }
                    }
                } else if (fromFileIndex < toFileIndex && fromRankIndex > toRankIndex) {
                    int capture_count = 0;
                    for (int i = 1; i <= toFileIndex - fromFileIndex; i++) {
                        if (board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
                            capture_count++;
                            System.out.println("HERE7");
                            //System.out.println("Cannot create valid path for Queen.");
                            //return;
                        }
                        else if(capture_count != 1) {
                            System.out.println("7");
                            System.out.println("Cannot create valid path for Queen.");
                            return;
                        }
                    }
                } else if (fromFileIndex > toFileIndex && fromRankIndex > toRankIndex) {
                    int capture_count = 0;
                    for (int i = 1; i <= fromFileIndex - toFileIndex; i++) {
                        if (board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
                            capture_count++;
                            System.out.println("HERE8");
                            //System.out.println("Cannot create valid path for Queen.");
                            //return;
                        }
                        //System.out.println("capture_count: " + capture_count);
                        else if(capture_count != 1) {

                            System.out.println("CHANGED");
                            System.out.println("8");
                            System.out.println("Cannot create valid path for Queen.");
                            return;
                        }
                    }
                } else if (fromFileIndex > toFileIndex && fromRankIndex < toRankIndex) {
                    int capture_count = 0;
                    for (int i = 1; i <= fromFileIndex - toFileIndex; i++) {
                        if (board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
                            capture_count++;
                            System.out.println("HERE9");
                            //System.out.println("Cannot create valid path for Queen.");
                            //return;
                        }
                        else if(capture_count != 1) {
                            System.out.println("9");
                            System.out.println("Cannot create valid path for Queen.");
                            return;
                        }
                    }
                }
            }
        } else if(fromPiece.toString().equalsIgnoreCase("k")) {
            if(fromFileIndex == toFileIndex && toRankIndex == fromRankIndex) {
                System.out.println("Cannot create valid path for King.");
                return;
            } else if (Math.abs(fromFileIndex- toFileIndex) > 1) {
                System.out.println("Cannot create valid path for King.");
                return;
            } else if (Math.abs(fromRankIndex- toRankIndex) > 1) {
                System.out.println("Cannot create valid path for King.");
                return;
            } else if (board.getBoardOfPieces()[toRankIndex][toFileIndex] != null) {
                System.out.println("Cannot create valid path for King.");
                return;
            }
        } else if (fromPiece.toString().equalsIgnoreCase("p")) {
	        // diagonal piece capture needs to be occupied
            if (fromFileIndex != toFileIndex && board.getBoardOfPieces()[toRankIndex][toFileIndex] == null) {
                System.out.println("Cannot create valid path for Pawn.");
                return;
            }
            if (playerTurnIsWhite) {
                if (fromRankIndex == 6) {
                    int rankDelta = fromRankIndex - toRankIndex;
                    int fileDelta = fromFileIndex - toFileIndex;
                    if(rankDelta > 2 || rankDelta < 1) {
                        System.out.println("Cannot create valid path for Pawn.");
                        return;
                    } else if (rankDelta == 1) {
			            // Capture the PAWN!
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null && fileDelta != 0) {
                            //System.out.println("Cannot create valid path for Pawn.");
                            //return;
                        }
			            else {
                            System.out.println("Cannot create valid path for Pawn.");
                            return;
                        }
                    } else if (rankDelta == 2) {
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null || board.getBoardOfPieces()[toRankIndex -1][toFileIndex] != null) {
                            System.out.println("Cannot create valid path for Pawn.");
                            return;
                        }
                    }
                } else {
                    int rankDelta = fromRankIndex - toRankIndex;
                    int fileDelta = fromFileIndex - toFileIndex;
                    if(rankDelta != 1) {
                        System.out.println("Cannot create valid path for Pawn.");
                        return;
                    } else {
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null && fileDelta != 0) {
                            //System.out.println("Cannot create valid path for Pawn.");
                            //return;
                        }
			            else {
                            System.out.println("Cannot create valid path for Pawn.");
                            return;
			            }
                    }
                }
            } else {
                if(fromRankIndex == 1) {
                    int rankDelta = fromRankIndex - toRankIndex;
                    int fileDelta = fromFileIndex - toFileIndex;
                    if(rankDelta < -2 || rankDelta > -1) {
                        System.out.println("Cannot create valid path for Pawn.");
                        return;
                    } else if (rankDelta == -1) {
                        // Capture the PAWN!
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null && fileDelta != 0) {
                            //System.out.println("Cannot create valid path for Pawn.");
                            //return;
                        }
                        else {
                            System.out.println("Cannot create valid path for Pawn.");
                            return;
                        }
                    } else if (rankDelta == -2) {
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null || board.getBoardOfPieces()[toRankIndex +1][toFileIndex] != null) {
                            System.out.println("Cannot create valid path for Pawn.");
                            return;
                        }
                    }
                } else {
                    int rankDelta = fromRankIndex - toRankIndex;
                    int fileDelta = fromFileIndex - toFileIndex;
                    if(rankDelta != -1) {
                        System.out.println("Cannot create valid path for Pawn.");
                        return;
                    } else {
                        if(board.getBoardOfPieces()[toRankIndex][toFileIndex] != null && fileDelta != 0) {
                            //System.out.println("Cannot create valid path for Pawn.");
                            //return;
                        }
                        else {
                            System.out.println("Cannot create valid path for Pawn.");
                            return;
                        }
                    }
                }
            }
        }

        //Handle the promotion of a pawn.
        if(fromPiece.toString().equalsIgnoreCase("p")) {
            if(playerTurnIsWhite && toRankIndex == 0) {
                if(pawnPromotionPiece == null) {
                    System.out.println("Pawn Promotion Piece must be specified for this pawn move.");
                    return;
                }
                if(!pawnPromotionPiece.toUpperCase().equals(pawnPromotionPiece)) {
                    System.out.println("Pawn Promotion Piece must be for White. Input should be uppercase.");
                    return;
                }
                fromPiece = Piece.valueOf(pawnPromotionPiece);
            } else if(!playerTurnIsWhite && toRankIndex == 7) {
                if(pawnPromotionPiece == null) {
                    System.out.println("Pawn Promotion Piece must be specified for this pawn move.");
                    return;
                }
                if(!pawnPromotionPiece.toLowerCase().equals(pawnPromotionPiece)) {
                    System.out.println("Pawn Promotion Piece must be for Black. Input should be lowercase.");
                    return;
                }
                fromPiece = Piece.valueOf(pawnPromotionPiece);
            }
        }

        //Move piece, if the move is allowed.
        board.movePiece(fromPiece, toRankIndex, toFileIndex, fromRankIndex, fromFileIndex);

        //Change the player's turn
        playerTurnIsWhite = !playerTurnIsWhite;
    }

    private static int calcFileIndex(Character file) {
        // Files are associated as follows: a->7, b->6, c->5, d->4, e->3, f->2, g->1, h->0
        switch(file) {
            case 'a' :
                return 0;
            case 'b' :
                return 1;
            case 'c' :
                return 2;
            case 'd' :
                return 3;
            case 'e' :
                return 4;
            case 'f' :
                return 5;
            case 'g' :
                return 6;
            case 'h' :
                return 7;
            default :
                throw new IllegalArgumentException("File Character '" + file + "' is invalid.");
        }
    }

    private static int calcRankIndex(int rankNumber) {
        // Ranks are associated as follows: 1->7, 2->6, 3->5, 4->4, 5->3, 6->2, 7->1, 8->0
        switch(rankNumber) {
            case 1 :
                return 7;
            case 2 :
                return 6;
            case 3 :
                return 5;
            case 4 :
                return 4;
            case 5 :
                return 3;
            case 6 :
                return 2;
            case 7 :
                return 1;
            case 8 :
                return 0;
            default:
                throw new IllegalArgumentException("Rank Value '" + rankNumber + "' is invalid.");

        }
    }


}
