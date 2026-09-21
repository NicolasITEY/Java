package core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import core.pieces.Bishop;
import core.pieces.King;
import core.pieces.Knight;
import core.pieces.Pawn;
import core.pieces.Pieces;
import core.pieces.Queen;
import core.pieces.Rook;

/**
 * A utility class for generating Gson instances for serializing and deserializing chess objects.
 */
public class GsonGenerate {
	
	/**
     * Default constructor for GsonGenerate.
     */
    public GsonGenerate() {
		super();
	}
    
	/**
	 * Generates a Gson instance configured with type adapters for the Pieces class and its subclasses.
	 * This allows for proper serialization and deserialization of chess pieces when saving and loading game states
	 * @return a Gson instance configured for chess pieces serialization and deserialization
	 */
    public static Gson getChessGson() {
    	RuntimeTypeAdapterFactory<Pieces> pieceFactory = RuntimeTypeAdapterFactory
    			.of(Pieces.class, "type")
    			.registerSubtype(Pawn.class, "Pawn")
    			.registerSubtype(Rook.class, "Rook")
    			.registerSubtype(Knight.class, "Knight")
    			.registerSubtype(Bishop.class, "Bishop")
    			.registerSubtype(Queen.class, "Queen")
    			.registerSubtype(King.class, "King");
    	
    	return new GsonBuilder()
        		.registerTypeAdapterFactory(pieceFactory)
        		.setPrettyPrinting()
        		.create();
    }
    
}
