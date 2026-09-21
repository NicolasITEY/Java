package src.zone;

import src.game.World;
import src.hero.item.Staff;

public class FindStaffPiece extends Event {

	private final int staffID = 3;

	@Override
	public void doEvent() {
		World.getPlayer().gainItem(new Staff(staffID,"a fancy stick"));
	}

}
