package core;

import java.io.Serializable;

/**
 * Class representing a factory for creating a board from a FEN string
 * The FEN string is a standard notation for describing a particular board position of a chess game. 
 * It contains information about the placement of pieces, the player to move, castling rights, 
 * en passant target square, halfmove clock, and fullmove number.
 * 
 * The makeBoard method will parse the FEN string and create a Board object based on the information contained in the FEN string. 
 * It will also perform validation checks on the FEN string to ensure that it is well-formed and represents a valid board position. 
 * If the FEN string is not valid, it will throw an exception with a message describing the error.
 */
public class BoardFactory implements Serializable {

    /**
     * Default constructor for BoardFactory.
     */
    public BoardFactory() {
        super();
    }

    /**
     * Method to create a board from a FEN string, it will throw an exception if the FEN string is not valid
     * 
     * @param fen the FEN string representing the board position
     * @param g the game to which the board belongs
     * 
     * @return a Board object representing the board position described by the FEN string
     * 
     * @throws Exception if the FEN string is not valid, it will throw an exception with a message describing the error
     */
    public static Board makeBoard(String fen,Game g) throws Exception {
        /* Variable Fen look like this : "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1" */
        String[] info = fen.split(" ");
        String[] lines = info[0].split("/");
        if (!info[1].equals("w") && !info[1].equals("b")) {
            throw new Exception("player turn incorrect");
        }
        if (info[2].equals("") || !info[2].matches("((K?Q?k?q?)|-)")) {
            throw new Exception("castling rights incorrect");
        }
        /* Any character, any digits or nothing */
        if (info[3].length() > 2 || !info[3].matches("(\\w\\d)|-")) {
            throw new Exception("en passant position incorrect");
        }
        int halfClock = Integer.parseInt(info[4]);
        if (halfClock > 50 || halfClock < 0) {
            //la regle des 50 coups doit etre respecter c'est pas possible que ce soit supérieur à 50, ni que ce soit négatif
            //la regle indique qu'il y a match nul après 50 coups qui n'est pas été effectué par un pion ou que rien n'a été capturé pendant les 50 coups
            throw new Exception("half clock timer range incorrect");
        }
        int moveNumber = Integer.parseInt(info[5]);
        if (moveNumber <= 0) {
            throw new Exception("turn number must be positive");
        }

        int prevLineWidth = -1;

        for (String line : lines) {
            int nextPosLine = 0;
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (Character.isDigit(c)) {
                    nextPosLine += (int) line.charAt(i) - (int) '0';
                } else {

                    nextPosLine++;
                }

            }
            if (prevLineWidth > 0) {
                if (prevLineWidth != nextPosLine) {
                    throw new Exception("irregular line width");
                }
                if (prevLineWidth > 23) {
                    throw new Exception("board can't be over 23 characters long due to column conflicts with pgn notation");
                }
            } else {

                prevLineWidth = nextPosLine;
            }
            //on vérifie ligne par ligne si la longueur reste la même si non on envoie une exceotion
        }

        return new Board(fen,g);
    }
    
}
