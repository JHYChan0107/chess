/*
 * Author: Jophie Chan
 * Date: 03/06/2025
 * Description: Here is where I created the legal moves my piece can move in and the controlled moves, which are how my piece
 * can capture opponent pieces. 
 */


import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

// The Piece class represents a chess piece and contains methods for piece behavior such as movement and control of squares.
public class Digger extends Piece {
	//private boolean isWhite;

    // Constructor to initialize the piece with a color and an image file
    
    /*public Digger(boolean isWhite, String img_file) {
        this.color = isWhite;

        try {
            if (this.img == null) {
                // Load the image file for the piece using ImageIO
                this.img = ImageIO.read(getClass().getResource(img_file));
            }
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }
    */
    
    public Digger(boolean isWhite, String img_file) {
        super(isWhite, img_file);
    }

    @Override
    public String toString() {
        return "A " + super.toString() + " Digger.";
    }

    /*
    // Getter for the color of the piece
    public boolean getColor() {
        return color;
    }

    // Getter for the image of the piece
    public Image getImage() {
        return img;
    }

    // Method to draw the piece on the given square using Graphics
    public void draw(Graphics g, Square currentSquare) {
        int x = currentSquare.getX();  // Get the x-coordinate of the square
        int y = currentSquare.getY();  // Get the y-coordinate of the square

        // Draw the piece's image on the board at the square's position
        g.drawImage(this.img, x, y, null);
    }
    */


    // TO BE IMPLEMENTED!
    // This method returns a list of squares controlled by this piece.
    // A square is controlled if the piece can legally capture into it.
    public ArrayList<Square> getControlledSquares(Square[][] board, Square start) {
        ArrayList<Square> controlledSquares = new ArrayList<>();

        // Directions to check for controlled squares (2 squares in all directions: vertical, horizontal, and diagonal)
        int[][] directions = {{2, 0}, {0, 2}, {-2, 0}, {0, -2}, {2, 2}, {-2, -2}, {2, -2}, {-2, 2}};

        // Loop through each direction to check for controlled squares
        for (int[] direction : directions) {
            int newRow = start.getRow() + direction[0];  // New row after moving
            int newCol = start.getCol() + direction[1];  // New column after moving

            // Ensure the new position is within bounds of the board 
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) { 
                Square targetSquare = board[newRow][newCol];
                Digger targetPiece = targetSquare.getOccupyingPiece();

                // If the target square has an opponent's piece, it's considered controlled
                if (targetPiece != null && targetPiece.getColor() != this.color) {
                    controlledSquares.add(targetSquare);  // Opponent's piece is controlled
                }
            }
        }

        // Return the list of controlled squares
        return controlledSquares;
    }

    // TO BE IMPLEMENTED!
    // This method returns a list of legal moves for the piece, taking into account the rules of movement.
    // A legal move is defined by whether the piece can move to that square within the bounds of the board.


    //MY PIECE'S MOVEMENTS
    //Moves within two spaces
    //If there is a opponent piece in the way, it will take it and reach it's destination
    //If your own piece is in the way, ignore it and reach it's destination    
    public ArrayList<Square> getLegalMoves(Board b, Square start) {
        ArrayList<Square> legalMoves = new ArrayList<>();
        Square[][] board = b.getSquareArray();  // Get the array of squares representing the board

        // Directions to check for legal moves (2 squares in all directions: vertical, horizontal, and diagonal)
        int[][] directions = {{2, 0}, {0, 2}, {-2, 0}, {0, -2}, {2, 2}, {-2, -2}, {2, -2}, {-2, 2}};

        // Loop through each direction to check for legal moves
        for (int[] direction : directions) {
            int newRow = start.getRow() + direction[0];  // New row after moving
            int newCol = start.getCol() + direction[1];  // New column after moving

            // Ensure the new position is within bounds of the board
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                Square targetSquare = board[newRow][newCol];  // Get the target square
                Digger targetPiece = targetSquare.getOccupyingPiece();  // Get the piece occupying the target square

                // If the target square is empty or contains an opponent's piece, it's a valid move
                if (targetPiece == null || targetPiece.getColor() != this.color) {
                    legalMoves.add(targetSquare);  // Add to legal moves list

                    // If there's an opponent's piece in the way, capture it and move to the next square
                    if (targetPiece != null && targetPiece.getColor() != this.color) {
                        int landingRow = newRow + direction[0];  // Calculate the landing square row
                        int landingCol = newCol + direction[1];  // Calculate the landing square column

                        // Ensure the landing square is within bounds and empty
                        if (landingRow >= 0 && landingRow < 8 && landingCol >= 0 && landingCol < 8) {
                            Square landingSquare = board[landingRow][landingCol];

                            // If the landing square is empty, add it to the legal moves list
                            if (landingSquare.getOccupyingPiece() == null) {
                                legalMoves.add(landingSquare);  // Capture the opponent's piece and move to the next square
                            }
                        }
                    }
                }
            }
        }

        // Return the list of legal moves
        return legalMoves;
    }
}
