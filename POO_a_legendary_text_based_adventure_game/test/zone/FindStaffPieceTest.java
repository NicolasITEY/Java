package test.zone;

import static org.junit.Assert.*;

import org.junit.Test;

import src.game.World;
import src.hero.Hero;
import src.hero.item.Item;
import src.hero.item.Staff;
import src.zone.FindStaffPiece;

public class FindStaffPieceTest {

	@Test
	public void startest() {
		World.startGame();
		FindStaffPiece event = new FindStaffPiece();
		int occupiedSpace = World.getPlayer().getOccupiedSpace();
		int initoccupiedbagspace = World.getPlayer().getBag().getinitoccupiedspace();
		event.doEvent();
		assertTrue(initoccupiedbagspace < occupiedSpace);
		boolean foundStaff = false;
		for (Item item : World.getPlayer().getBag().getItems()) {
			if (item instanceof Staff && ((Staff) item).getPieceId() == 3) {
				foundStaff = true;
				break;
			}
		}
		assertTrue(foundStaff);
	}

	@Test
	public void testDoEventBagCapacity() {
		// Initialise le jeu et le héros
		World.startGame();
		Hero hero = World.getPlayer();
		FindStaffPiece event = new FindStaffPiece();

		// Remplir le sac presque complètement
		while (hero.getBag().getOccupiedSpace() < hero.getBag().getBagCapacity() - 1) {
			hero.gainItem(new Staff(99,"what even is this?"));
		}

		int beforeSpace = hero.getBag().getOccupiedSpace();

		// Active l'événement
		event.doEvent();

		// Récupère l'espace après l'événement
		int afterSpace = hero.getBag().getOccupiedSpace();
		assertTrue(afterSpace < beforeSpace);
		// Vérifie que le sac ne dépasse jamais sa capacité

	}

}
