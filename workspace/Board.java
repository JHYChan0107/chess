/*
 * Author: Jophie Chan
 * Date: 03/06/2025
 * Description: This is chess which a twist as I added a piece to replace the knight. Here, I created the board, including the 
 * pieces, and the mouse functions like drag and release. 
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

// The Board class represents the chess board and handles piece movement and user interaction
@SuppressWarnings("serial")
public class Board extends JPanel implements MouseListener, MouseMotionListener {
    
    // Resource location constants for piece images
    private static final String RESOURCES_WBISHOP_PNG = "wbishop.png";
    private static final String RESOURCES_BBISHOP_PNG = "bbishop.png";
    private static final String RESOURCES_WKNIGHT_PNG = "wknight.png";
    private static final String RESOURCES_BKNIGHT_PNG = "bknight.png";
    private static final String RESOURCES_WROOK_PNG = "wrook.png";
    private static final String RESOURCES_BROOK_PNG = "brook.png";
    private static final String RESOURCES_WKING_PNG = "wking.png";
    private static final String RESOURCES_BKING_PNG = "bking.png";
    private static final String RESOURCES_BQUEEN_PNG = "bqueen.png";
    private static final String RESOURCES_WQUEEN_PNG = "wqueen.png";
    private static final String RESOURCES_WPAWN_PNG = "wpawn.png";
    private static final String RESOURCES_BPAWN_PNG = "bpawn.png";

    private static final String RESOURCES_WDIG_PNG = "Chess Piece Sprite W.png";
    private static final String RESOURCES_BDIG_PNG = "Chess Piece Sprite B.png";

    // Logical and graphical representations of board
    private final Square[][] board;
    private final GameWindow g;

    // Tracks whose turn it is, true for white, false for black
    private boolean whiteTurn;

    // The current piece being dragged by the player
    private Digger currPiece;
    
    // The square from which the piece was moved
    private Square fromMoveSquare;

    // Used to track mouse coordinates for dragging pieces
    private int currX;
    private int currY;

    public Board(GameWindow g) {
        this.g = g;
        board = new Square[8][8];
        setLayout(new GridLayout(8, 8, 0, 0));  // Setting up the board as a grid layout

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        // Initialize the board by creating squares and adding them to the JPanel
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                boolean isWhite = (r + c) % 2 == 0;
                board[r][c] = new Square(this, isWhite, r, c);  // Alternates colors for each square
                this.add(board[r][c]);  // Add the square to the grid layout
            }
        }

        initializePieces();  // Set up the initial placement of the pieces

        // Set the preferred size and other layout constraints for the board
        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));

        whiteTurn = true;  // White always goes first
    }

    // Set up the initial positions of the pieces for both players
    private void initializePieces() {
        // White pieces
        board[0][0].put(new Rook(false, RESOURCES_BROOK_PNG));
        board[0][1].put(new Digger(false, RESOURCES_BDIG_PNG));   // My Piece (this is a custom piece)
        board[0][2].put(new Bishop(false, RESOURCES_BBISHOP_PNG));
        board[0][3].put(new Queen(false, RESOURCES_BQUEEN_PNG));
        board[0][4].put(new King(false, RESOURCES_BKING_PNG));
        board[0][5].put(new Bishop(false, RESOURCES_BBISHOP_PNG));
        board[0][6].put(new Digger(false, RESOURCES_BDIG_PNG));   // My Piece (this is a custom piece)
        board[0][7].put(new Rook(false, RESOURCES_BROOK_PNG));

        // Place white pawns
        for (int i = 0; i < 8; i++) {
            board[1][i].put(new Pawn(false, RESOURCES_BPAWN_PNG));
        }

        // Black pieces
        board[7][0].put(new Rook(true, RESOURCES_WROOK_PNG));
        board[7][1].put(new Digger(true, RESOURCES_WDIG_PNG));   // My Piece (this is a custom piece)
        board[7][2].put(new Bishop(true, RESOURCES_WBISHOP_PNG));
        board[7][3].put(new Queen(true, RESOURCES_WQUEEN_PNG));
        board[7][4].put(new King(true, RESOURCES_WKING_PNG));
        board[7][5].put(new Bishop(true, RESOURCES_WBISHOP_PNG));
        board[7][6].put(new Digger(true, RESOURCES_WDIG_PNG));   // My Piece (this is a custom piece)
        board[7][7].put(new Rook(true, RESOURCES_WROOK_PNG));

        // Place black pawns
        for (int i = 0; i < 8; i++) {
            board[6][i].put(new Pawn(true, RESOURCES_WPAWN_PNG));
        }
    }

    // Return the 2D array of squares representing the board
    public Square[][] getSquareArray() {
        return this.board;
    }

    // Return the current turn (true if white's turn)
    public boolean getTurn() {
        return whiteTurn;
    }

    // Set the current piece being dragged
    public void setCurrPiece(Digger p) {
        this.currPiece = p;
    }

    // Get the current piece being dragged
    public Digger getCurrPiece() {
        return this.currPiece;
    }

    @Override
    public void paintComponent(Graphics g) {
        // Draw all squares on the board
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square sq = board[x][y];
                // Highlight the square from which the piece is being moved (if any)
                if(sq == fromMoveSquare)
                    sq.setBorder(BorderFactory.createLineBorder(Color.blue));
                sq.paintComponent(g);
            }
        }

        // If a piece is being dragged, display it at the current mouse location
        if (currPiece != null) {
            if ((currPiece.getColor() && whiteTurn)
                    || (!currPiece.getColor() && !whiteTurn)) {
                final Image img = currPiece.getImage();
                g.drawImage(img, currX, currY, null);  // Draw the piece image at the cursor
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currX = e.getX();
        currY = e.getY();

        // Determine which square the mouse was clicked on
        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        // If the square is occupied, start dragging the piece
        if (sq.isOccupied()) {
            currPiece = sq.getOccupyingPiece();
            fromMoveSquare = sq;
            // Check if the current player is allowed to move the piece
            if (currPiece.getColor() != whiteTurn) {
                currPiece = null;
                return;  // Cannot move opponent's pieces
            }

            sq.setDisplay(false);  // Hide the piece in the original square
        }
        repaint();
    }

    // This method moves the piece to a new square if the move is legal
    private void resetMove() {
        // Remove borders from all squares and reset the display
        for (Square[] row : board) {
            for (Square s : row) {
                s.setBorder(null);
            }
        }

        // Restore the display of the original square
        if (fromMoveSquare != null) {
            fromMoveSquare.setDisplay(true);
        }
        currPiece = null;
        fromMoveSquare = null;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Determine the square where the piece is being dropped
        Square endSquare = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        // If the piece and the fromSquare are valid, proceed with the move
        if (currPiece != null && fromMoveSquare != null) {
            ArrayList<Square> legalMoves = currPiece.getLegalMoves(this, fromMoveSquare);
            

            if (legalMoves.contains(endSquare)) {  // Check if the move is legal
                if (endSquare.isOccupied()) {  // Handle capturing opponent pieces
                    Digger capturedPiece = endSquare.getOccupyingPiece();
                    if (capturedPiece.getColor() != currPiece.getColor()) {
                        endSquare.removePiece();  // Remove captured piece
                    } else {
                        resetMove();  // Can't move onto own piece, reset
                        return;
                    }
                }

                // Move the piece to the new square
                endSquare.put(currPiece);
                fromMoveSquare.removePiece();  // Clear the original square

                boolean After = isInCheck(whiteTurn);
                if(After) {
                    endSquare.removePiece();
                    fromMoveSquare.put(currPiece);
                    repaint();
                    return;
                }

                // Check if the player was in check at the start of the turn
            if (isInCheck(whiteTurn)) {
                // If the player is in check at the start, ensure the move gets them out of check
                boolean isCheckmate = true;
                for (Square s : currPiece.getLegalMoves(this, fromMoveSquare)) {
                    if (!isInCheck(whiteTurn)) {
                        isCheckmate = false;
                        break;
                    }
                }

                if (isCheckmate) {
                    // If no move can get the player out of check, cancel the move
                    fromMoveSquare.put(currPiece);
                    endSquare.removePiece();
                    resetMove();
                    return;
                }
            }

                whiteTurn = !whiteTurn;  // Switch turns
            }
        }

        resetMove();  // Reset after the move
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currX = e.getX() - 24;  // Offset to center the piece
        currY = e.getY() - 24;

        if (currPiece != null) {
            // Highlight all legal move squares in red
            for (Square s : currPiece.getLegalMoves(this, fromMoveSquare)) {
                s.setBorder(BorderFactory.createLineBorder(Color.red));
            }

            // Highlight squares controlled by the piece in blue
            for (Square s1 : currPiece.getControlledSquares(board, fromMoveSquare)) { 
                s1.setBorder(BorderFactory.createLineBorder(Color.blue));
            }
        }

        repaint();
    }

    //precondition - the board is initialized and contains a king of either color. The boolean kingColor corresponds to the color of the king we wish to know the status of.
    //postcondition - returns true of the king is in check and false otherwise.
	public boolean isInCheck(boolean kingColor) {
        // Find the king of the specified color
        Square kingSquare = null;
        Boolean check = true;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board[r][c].getOccupyingPiece();
                if (piece instanceof King && piece.getColor() == kingColor) {
                    kingSquare = board[r][c];  // Found the king's square
                    break;
                }
            }
            if (kingSquare != null) break;  // Exit outer loop once king is found
        }
    
        // If no king is found, return false (this should never happen in a valid game)
        if (kingSquare == null) return false;
    
        // Now check if any piece of the opposite color can attack the king
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board[r][c].getOccupyingPiece();
                if (piece != null && piece.getColor() != kingColor) {
                    // Check if this piece can control the king's square
                    List<Square> controlledSquares = piece.getControlledSquares(board, board[r][c]);
                    if (controlledSquares.contains(kingSquare)) {
                        return true;  // The king is in check
                    }
                }
            }
        }
        
        System.out.println(check);
        return false;  // The king is not in check
    }
    
    

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
