
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

//you will need to implement two functions in this file.
public class Piece {
    private final boolean color;
    private BufferedImage img;
    
    public Piece(boolean isWhite, String img_file) {
        this.color = isWhite;
        
        try {
            if (this.img == null) {
              this.img = ImageIO.read(getClass().getResource(img_file));
            }
          } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
          }
    }
    
    

    
    public boolean getColor() {
        return color;
    }
    
    public Image getImage() {
        return img;
    }
    
    public void draw(Graphics g, Square currentSquare) {
        int x = currentSquare.getX();
        int y = currentSquare.getY();
        
        g.drawImage(this.img, x, y, null);
    }
    
    
    // TO BE IMPLEMENTED!
    //return a list of every square that is "controlled" by this piece. A square is controlled
    //if the piece capture into it legally.
    public ArrayList<Square> getControlledSquares(Square[][] board, Square start) {
      ArrayList<Square> controlledSquares = new ArrayList<>();
      
      // Try all the directions this piece can control (2 squares in all directions)
      int[][] directions = {{2, 0}, {0, 2}, {-2, 0}, {0, -2}, {2, 2}, {-2, -2}, {2, -2}, {-2, 2}};
      
      for (int[] direction : directions) {
          int newRow = start.getRow() + direction[0];
          int newCol = start.getCol() + direction[1];
          
          // Check if within bounds
          if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
              Square targetSquare = board[newRow][newCol];
              if (targetSquare.getOccupyingPiece() != null && targetSquare.getOccupyingPiece().getColor() != this.color) {
                  controlledSquares.add(targetSquare);  // Opponent's piece is controlled
              }
          }
      }
      
      return controlledSquares;
  }
    

    //TO BE IMPLEMENTED!
    //implement the move function here
    //it's up to you how the piece moves, but at the very least the rules should be logical and it should never move off the board!
    //returns an arraylist of squares which are legal to move to
    //please note that your piece must have some sort of logic. Just being able to move to every square on the board is not
    //going to score any points.

    /*
     * 1. My piece is called the Digger piece
     * 2. It can move in any direction within two spaces (Vertically, Horizontally, and Diagonally)
     * 3. If there is an opponent piece in the way of the move, the digger piece will take it and reach it's destination
     * 4. If your own piece is in the way of the move, ignore the piece and reach it's destination
     */


    public ArrayList<Square> getLegalMoves(Board b, Square start) {
      ArrayList<Square> legalMoves = new ArrayList<>();
      Square[][] board = b.getSquareArray();
      
      // Try all the directions this piece can move (2 squares in all directions)
      int[][] directions = {{2, 0}, {0, 2}, {-2, 0}, {0, -2}, {2, 2}, {-2, -2}, {2, -2}, {-2, 2}};
      
      for (int[] direction : directions) {
        int newRow = start.getRow() + direction[0];
        int newCol = start.getCol() + direction[1];
      
          // Check if within bounds
          if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
            Square targetSquare = board[newRow][newCol];
            Piece targetPiece = targetSquare.getOccupyingPiece();
              
              // If the target square is empty or contains an opponent's piece, it's a valid move
              if (targetPiece == null || targetPiece.getColor() != this.color) {
                legalMoves.add(targetSquare);
                  
                  // If there's an opponent's piece in the way, capture it and land on the next square
                if (targetPiece != null && targetPiece.getColor() != this.color) {
                  int landingRow = newRow + direction[0];
                  int landingCol = newCol + direction[1];
                      
                  if (landingRow >= 0 && landingRow < 8 && landingCol >= 0 && landingCol < 8) {
                    Square landingSquare = board[landingRow][landingCol];
                      // Ensure landing square is empty
                      if (landingSquare.getOccupyingPiece() == null) {
                        legalMoves.add(landingSquare);  // Capture the opponent's piece and move to the next square
                      }
                  }
                }
              }
          }
      }
      
      return legalMoves;
  }
}
